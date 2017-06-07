package dev.tornaco.tasker.app;

import org.newstand.logger.Logger;

import java.util.concurrent.Executor;

import dev.tornaco.tasker.app.utils.Enforcer;
import dev.tornaco.tasker.app.utils.SharedExecutor;
import dev.tornaco.tasker.test.UnitTest;

/**
 * Created by Nick on 2017/5/9 17:39
 * E-Mail: Tornaco@163.com
 * All right reserved.
 */
class TestLauncher {

    static void launchAsync(final UnitTest test) {
        launchAsync(test, SharedExecutor.getService());
    }

    static void launchAsync(final UnitTest test, Executor executor) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                launch(test);
            }
        });
    }

    static void launch(UnitTest test) {
        Enforcer.enforceWorkerThread("TestLauncher#launch");
        String testCmd = testsCmd(test.getClz(), test.getMethod(), test.getTestPkg());
        Logger.i("Running command: %s", testCmd);
        Enforcer.enforceRoot().runCommand(testCmd);
    }

    private static String testsCmd(String clz, String method, String testPkg) {
        return String.format("am instrument -w -r -e debug false -e class %s#%s %s/android.support.test.runner.AndroidJUnitRunner",
                clz, method, testPkg);
    }
}
