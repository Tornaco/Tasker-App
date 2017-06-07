package dev.tornaco.tasker;

import android.app.Application;

import org.newstand.logger.Logger;
import org.newstand.logger.Settings;

import dev.tornaco.tasker.app.service.PackageObserveService;

/**
 * Created by Nick on 2017/5/10 14:11
 * E-Mail: Tornaco@163.com
 * All right reserved.
 */

public class TaskerApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.config(Settings.builder().tag("Tasker-App").logLevel(Logger.LogLevel.ALL).build());
        PackageObserveService.start(getApplicationContext());
    }
}
