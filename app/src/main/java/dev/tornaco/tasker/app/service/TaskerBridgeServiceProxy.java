package dev.tornaco.tasker.app.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import dev.tornaco.tasker.service.ITaskerBridgeService;

/**
 * Created by Nick on 2017/5/9 17:23
 * E-Mail: Tornaco@163.com
 * All right reserved.
 */

public class TaskerBridgeServiceProxy extends ServiceProxy {

    private ITaskerBridgeService mService;

    private TaskerBridgeServiceProxy(Context context, Intent intent) {
        super(context, intent);
    }

    private static TaskerBridgeServiceProxy create(Context c) {
        Intent i = new Intent("dev.tornaco.tasker.ACTION_START_SERVICE");
        i.setClassName("dev.tornaco.tasker.app", "dev.tornaco.tasker.app.service.TaskerBridgeService");
        return new TaskerBridgeServiceProxy(c, i);
    }

    public static void start(Context context) {
        Intent i = new Intent("dev.tornaco.tasker.ACTION_START_SERVICE");
        i.setClassName("dev.tornaco.tasker.app", "dev.tornaco.tasker.app.service.TaskerBridgeService");
        context.startService(i);
    }


    @Override
    public void onConnected(IBinder binder) {
        mService = ITaskerBridgeService.Stub.asInterface(binder);
    }

}
