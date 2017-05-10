package dev.tornaco.tasker.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import org.newstand.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dev.tornaco.tasker.app.provider.SettingStore;
import dev.tornaco.tasker.app.provider.Settings;
import dev.tornaco.tasker.service.ITask;
import dev.tornaco.tasker.service.ITaskListener;
import dev.tornaco.tasker.service.ITaskerBridgeService;

/**
 * Created by Nick on 2017/5/9 17:07
 * E-Mail: Tornaco@163.com
 * All right reserved.
 */

public class TaskerBridgeService extends Service implements ITaskerBridgeService {

    private List<ITask> tasks = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Settings settings = new SettingStore("TaskerBridgeService");

        for (int i = 0; i < 10; i++) {
            ITask t = new ITask("Task@" + i, UUID.randomUUID().toString(), "Body@" + i);
            tasks.add(t);
            Logger.d("Adding task %s", t);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return asBinder();
    }

    @Override
    public String version() throws RemoteException {
        return "1.0.0-ALPHA";
    }

    @Override
    public ITask nextTask() throws RemoteException {
        return tasks.remove(0);
    }

    @Override
    public boolean hasNextTask() throws RemoteException {
        return tasks.size() != 0;
    }

    @Override
    public void setTaskListener(ITaskListener listener) throws RemoteException {

    }

    @Override
    public IBinder asBinder() {
        return new Bridge();
    }

    private class Bridge extends Stub {

        @Override
        public String version() throws RemoteException {
            return TaskerBridgeService.this.version();
        }

        @Override
        public ITask nextTask() throws RemoteException {
            return TaskerBridgeService.this.nextTask();
        }

        @Override
        public boolean hasNextTask() throws RemoteException {
            return TaskerBridgeService.this.hasNextTask();
        }

        @Override
        public void setTaskListener(ITaskListener listener) throws RemoteException {

        }
    }
}
