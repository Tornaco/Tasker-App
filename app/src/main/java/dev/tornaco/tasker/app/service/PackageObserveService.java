package dev.tornaco.tasker.app.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.newstand.logger.Logger;

import dev.tornaco.tasker.repo.ModulePackageLoader;
import dev.tornaco.tasker.test.ModulePackage;
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
        ModulePackage modulePackage = ModulePackageLoader.loadModulePackageFrom(this, pkg);
        if (modulePackage != null) {
            // Send notification.
            NotificationMachine.buildNotification(getApplicationContext(), "Module installed", "Click to see details", 0);
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
