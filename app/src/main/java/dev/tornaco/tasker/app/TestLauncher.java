package dev.tornaco.tasker.app;

import dev.tornaco.tasker.app.utils.Enforcer;
import dev.tornaco.tasker.test.UnitTest;

/**
 * Created by Nick on 2017/5/9 17:39
 * E-Mail: Tornaco@163.com
 * All right reserved.
 */
class TestLauncher {

    static void launch(UnitTest test) {
        Enforcer.enforceWorkerThread("TestLauncher#launch");
        Enforcer.enforceRoot().runCommand(testsCmd(test.getClz(), test.getMethod(), test.getTestPkg()));
    }

    private static String testsCmd(String clz, String method, String testPkg) {
        return String.format("am instrument -w -r -e debug false -e class %s#%s %s/android.support.test.runner.AndroidJUnitRunner", clz, method, testPkg);
    }
}
