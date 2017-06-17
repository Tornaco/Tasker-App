package dev.tornaco.tasker.app.service;

import com.chrisplus.rootmanager.container.Result;
import com.google.gson.Gson;

import org.newstand.logger.Logger;

import dev.tornaco.tasker.test.Module;
import dev.tornaco.tasker.utils.Enforcer;

/**
 * Created by Nick on 2017/5/9 17:39
 * E-Mail: Tornaco@163.com
 * All right reserved.
 */
class InstrumentationCaller {

    static String launch(Module module) {
        Logger.d("launch module:%s", new Gson().toJson(module));
        Enforcer.enforceWorkerThread("InstrumentationCaller#launch");
        String testCmd = formatCommand(module.getClz(), module.getMethod(), module.getTestPkg());
        Logger.i("Running command: %s", testCmd);
        Result r = Enforcer.enforceRoot().runCommand(testCmd);
        return r.getMessage();
    }

    private static String formatCommand(String clz, String method, String testPkg) {
        return String.format("am instrument -w -r -e debug false -e class %s#%s %s/android.support.test.runner.AndroidJUnitRunner",
                clz, method, testPkg);
    }
}
