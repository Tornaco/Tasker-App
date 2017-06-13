package dev.tornaco.tasker.app.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.common.io.Files;

import org.newstand.logger.Logger;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import dev.tornaco.tasker.protocal.Constants;
import dev.tornaco.tasker.test.Module;
import dev.tornaco.tasker.test.PackageModuleParser;
import dev.tornaco.tasker.utils.SharedExecutor;

/**
 * Created by Nick on 2017/6/7 13:37
 */

public class PackageObserveService extends Service {

    private PackageObserver mPackageObserver;

    public static void start(Context context) {
        context.startService(new Intent(context, PackageObserveService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPackageObserver = new PackageObserver(new PackageHandler() {
            @Override
            public void onPackageAdd(String pkg) {
                Logger.d("onPackageAdd:%s", pkg);
                dumpPackage(pkg);
            }

            @Override
            public void onPackageReplaced(final String pkg) {
                Logger.d("onPackageReplaced:%s", pkg);
                SharedExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        dumpPackage(pkg);
                    }
                });
            }
        });
    }

    private void dumpPackage(String pkg) {
        PackageManager pm = getPackageManager();
        PackageInfo packageInfo;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                packageInfo = pm.getPackageInfo(pkg, PackageManager.MATCH_UNINSTALLED_PACKAGES);
            } else {
                packageInfo = pm.getPackageInfo(pkg, PackageManager.GET_UNINSTALLED_PACKAGES);
            }

            InstrumentationInfo[] instrumentationInfos = packageInfo.instrumentation;
            Logger.d("instrumentationInfos: %s", Arrays.toString(instrumentationInfos));

            ApplicationInfo applicationInfo = pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA);
            String metaData = applicationInfo.metaData.toString();
            Logger.d("metaData: %s", metaData);

            String packageTitle = applicationInfo.metaData.getString(Constants.META_DATA_KEY_MODULE_TITLE);
            Logger.d("packageTitle: %s", packageTitle);
            String packageDesc = applicationInfo.metaData.getString(Constants.META_DATA_KEY_MODULE_DESCRIPTION);
            Logger.d("packageDesc: %s", packageDesc);

            String apkPath = applicationInfo.publicSourceDir;
            String newPath = getExternalCacheDir() + File.separator + UUID.randomUUID().toString();
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
                return;
            }

            byte[] bytes = ZipUtil.unpackEntry(new File(newPath), Constants.MODULE_LIST_PATH);
            String content = new String(bytes);
            Logger.d("content: %s", content);

            ArrayList<Module> moduleList = null;
            try {
                moduleList = PackageModuleParser.parse(content);
            } catch (Throwable throwable) {
                Logger.e(throwable, "Fail parse module list");
            }
            if (moduleList != null) {
                for (Module m : moduleList) {
                    Logger.i("Found module:%s", m);
                }
            } else {
                Logger.d("No module defined in module list, ignore this package");
            }
            // noinspection ResultOfMethodCallIgnored
            new File(newPath).delete();

            // Send notification.
            NotificationMachine.buildNotification(getApplicationContext(), "Module installed", "Click to see details", 0);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e, "Package not found for:%s", pkg);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPackageObserver.register(this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPackageObserver.unRegister(this);
    }

    private interface PackageHandler {
        void onPackageAdd(String pkg);

        void onPackageReplaced(String pkg);
    }

    private class PackageObserver extends BroadcastReceiver {

        PackageHandler mHandler;

        public PackageObserver(PackageHandler handler) {
            this.mHandler = handler;
        }

        public void register(Context context) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
            intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
            intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
            intentFilter.addDataScheme("package");
            context.registerReceiver(this, intentFilter);
            Logger.d("Registering PackageObserver...");
        }

        public void unRegister(Context context) {
            context.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                // They send us bad action~
                return;
            }

            Logger.i("PackageObserver::onReceive: %s", action);

            switch (action) {
                case Intent.ACTION_PACKAGE_ADDED:
                    String packageName = intent.getData().getSchemeSpecificPart();
                    if (packageName == null) return;
                    mHandler.onPackageReplaced(packageName);
                    break;
                case Intent.ACTION_PACKAGE_REPLACED:
                    packageName = intent.getData().getSchemeSpecificPart();
                    if (packageName == null) return;
                    mHandler.onPackageAdd(packageName);
                    break;
            }
        }
    }

}
