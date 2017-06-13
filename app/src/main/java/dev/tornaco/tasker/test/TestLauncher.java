package dev.tornaco.tasker.test;

import com.chrisplus.rootmanager.container.Result;
import com.google.gson.Gson;

import org.newstand.logger.Logger;

import java.util.concurrent.Executor;

import dev.tornaco.tasker.utils.Enforcer;
import dev.tornaco.tasker.utils.SharedExecutor;

/**
 * Created by Nick on 2017/5/9 17:39
 * E-Mail: Tornaco@163.com
 * All right reserved.
 */
public class TestLauncher {

    public static void launchAsync(final Module test) {
        launchAsync(test, SharedExecutor.getService());
    }

    public static void launchAsync(final Module test, Executor executor) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                launch(test);
            }
        });
    }

    public static void launch(Module test) {
        Logger.d("launch module:%s", new Gson().toJson(test));
        Enforcer.enforceWorkerThread("TestLauncher#launch");
        String testCmd = testsCmd(test.getClz(), test.getMethod(), test.getTestPkg());
        Logger.i("Running command: %s", testCmd);
        Result r = Enforcer.enforceRoot().runCommand(testCmd);
        Logger.d("Runner result:%s", r.getMessage());
    }

    private static String testsCmd(String clz, String method, String testPkg) {
        return String.format("am instrument -w -r -e debug false -e class %s#%s %s/android.support.test.runner.AndroidJUnitRunner",
                clz, method, testPkg);
    }
}
