package dev.tornaco.tasker.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import org.newstand.logger.Logger;

import dev.tornaco.tasker.service.ITask;
import dev.tornaco.tasker.service.ITaskExecutor;
import dev.tornaco.tasker.service.ITaskerBridgeService;

/**
 * Created by Nick on 2017/5/9 17:07
 * E-Mail: Tornaco@163.com
 * All right reserved.
 */

public class TaskerBridgeService extends Service implements ITaskerBridgeService {

    private boolean taskDone;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return asBinder();
    }


    @Override
    public IBinder asBinder() {
        return new Bridge();
    }

    @Override
    public void onExecutorCreate(ITaskExecutor executor) throws RemoteException {
        Logger.d("onExecutorCreate: %s", executor.getSerial());
        String res = executor.execute(new ITask("pressHome", "ID", "Data"));
        Logger.d("pressHome: %s", res);
        taskDone = true;
    }

    @Override
    public boolean shouldTerminate(ITaskExecutor executor) throws RemoteException {
        return true;
    }

    private class Bridge extends Stub {

        @Override
        public void onExecutorCreate(ITaskExecutor executor) throws RemoteException {
            TaskerBridgeService.this.onExecutorCreate(executor);
        }

        @Override
        public boolean shouldTerminate(ITaskExecutor executor) throws RemoteException {
            return TaskerBridgeService.this.shouldTerminate(executor);
        }
    }
}
