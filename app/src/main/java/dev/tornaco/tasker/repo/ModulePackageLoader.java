package dev.tornaco.tasker.repo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.common.io.Files;

import org.newstand.logger.Logger;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dev.tornaco.tasker.common.Consumer;
import dev.tornaco.tasker.protocal.Constants;
import dev.tornaco.tasker.test.Module;
import dev.tornaco.tasker.test.ModulePackage;
import dev.tornaco.tasker.test.PackageModuleParser;
import dev.tornaco.tasker.utils.ApkUtil;
import dev.tornaco.tasker.utils.Collections;
import dev.tornaco.tasker.utils.Enforcer;
import dev.tornaco.tasker.utils.SharedExecutor;

/**
 * Created by Nick on 2017/6/13 13:33
 */

public abstract class ModulePackageLoader {

    public static void loadModulePackagesAsync(final Context context,
                                               final Consumer<List<ModulePackage>> consumer) {
        SharedExecutor.execute(new Runnable() {
            @Override
            public void run() {
                consumer.accept(loadModulePackages(context));
            }
        });
    }

    @NonNull
    public static List<ModulePackage> loadModulePackages(final Context context) {
        final PackageManager pm = Enforcer.enforceNoNull(context.getApplicationContext()).getPackageManager();

        List<PackageInfo> packageInfoList;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            packageInfoList = pm.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);
        } else {
            packageInfoList = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        }

        final List<ModulePackage> modulePackages = new ArrayList<>();

        Collections.consumeRemaining(packageInfoList, new Consumer<PackageInfo>() {
            @Override
            public void accept(@Nullable PackageInfo packageInfo) {
                String pkg;
                if (packageInfo != null) {
                    pkg = packageInfo.packageName;
                    ModulePackage modulePackage = loadModulePackageFrom(context, pkg);
                    if (modulePackage != null) modulePackages.add(modulePackage);
                }
            }
        });
        return modulePackages;
    }

    public static void loadModulePackageFromAsync(final Context context, final String pkg, final Consumer<ModulePackage> consumer) {
        SharedExecutor.execute(new Runnable() {
            @Override
            public void run() {
                consumer.accept(loadModulePackageFrom(context, pkg));
            }
        });
    }

    @Nullable
    public static ModulePackage loadModulePackageFrom(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA);
            if (applicationInfo.metaData == null) return null;

            String packageTitle = applicationInfo.metaData.getString(Constants.META_DATA_KEY_MODULE_TITLE);
            if (TextUtils.isEmpty(packageTitle)) return null;
            String packageDesc = applicationInfo.metaData.getString(Constants.META_DATA_KEY_MODULE_DESCRIPTION);
            if (TextUtils.isEmpty(packageDesc)) return null;

            String apkPath = applicationInfo.publicSourceDir;
            String newPath = context.getExternalCacheDir() + File.separator + UUID.randomUUID().toString();
            try {
                Files.copy(new File(apkPath), new File(newPath));
            } catch (IOException e) {
                Logger.e(e, "Fail copy apk file to tmp");
            }

            Logger.i("Tmp apk path: %s", newPath);

            boolean exists = ZipUtil.containsEntry(new File(newPath), Constants.MODULE_LIST_PATH);

            if (!exists) {
                Logger.d("Module list file not exists in: %s", Constants.MODULE_LIST_PATH);

                // Delete tmp file.
                // noinspection ResultOfMethodCallIgnored
                new File(newPath).delete();
                return null;
            }

            byte[] bytes = ZipUtil.unpackEntry(new File(newPath), Constants.MODULE_LIST_PATH);
            String content = new String(bytes);

            ArrayList<Module> moduleList = null;
            try {
                moduleList = PackageModuleParser.parse(content);
            } catch (Throwable throwable) {
                Logger.e(throwable, "Fail parse module list");
                return null;
            }
            if (moduleList != null && moduleList.size() > 0) {
                for (Module m : moduleList) {
                    Logger.i("This package has module:%s", m);
                }
            } else {
                Logger.d("No module defined in module list, ignore this package");
                return null;
            }
            // noinspection ResultOfMethodCallIgnored
            new File(newPath).delete();

            return new ModulePackage(pkg, packageTitle, packageDesc, moduleList, ApkUtil.loadIconByPkgName(context, pkg));
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e, "Fail getApplicationInfo for %s", pkg);
            return null;
        }
    }
}
