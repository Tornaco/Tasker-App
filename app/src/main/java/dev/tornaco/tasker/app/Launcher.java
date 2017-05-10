package dev.tornaco.tasker.app;

import android.content.Context;

import dev.tornaco.tasker.app.utils.Enforcer;

/**
 * Created by Nick on 2017/5/9 17:39
 * E-Mail: Tornaco@163.com
 * All right reserved.
 */
class Launcher {

    static void launch(Context context) {
        Enforcer.enforceWorkerThread("Launcher#launch");
        Enforcer.enforceRoot().runCommand(launcherCommand());
    }

    private static String launcherCommand() {
        return testsCmd("dev.tornaco.tasker.Launcher", "start", "dev.tornaco.tasker");
    }

    private static String testsCmd(String clz, String method, String testPkg) {
        return String.format("am instrument -w -r -e debug false -e class %s#%s %s/android.support.test.runner.AndroidJUnitRunner", clz, method, testPkg);
    }
}
