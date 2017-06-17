package dev.tornaco.tasker.app.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import dev.tornaco.tasker.common.Holder;
import dev.tornaco.tasker.test.Module;

/**
 * Created by Nick on 2017/6/14 10:12
 */

public class InstrumentationLauncherServiceProxy extends ServiceProxy implements InstrumentationLauncher {

    private InstrumentationLauncher mService;

    public InstrumentationLauncherServiceProxy(Context context) {
        super(context, new Intent(context, InstrumentationLauncherService.class));
    }

    @Override
    public void onConnected(IBinder binder) {
        mService = (InstrumentationLauncher) binder;
    }

    @Override
    public String launch(final Module module, final AbortSignal signal)
            throws Exception {
        final Holder<Exception> err = new Holder<>();
        final Holder<String> holder = new Holder<>();
        setTask(new ProxyTask() {
            @Override
            public void run() throws RemoteException {
                try {
                    holder.setData(mService.launch(module, signal));
                } catch (Exception e) {
                    err.setData(e);
                }
            }
        }, "launch");
        waitForCompletion();
        if (err.getData() != null) throw err.getData();
        return holder.getData();
    }
}
