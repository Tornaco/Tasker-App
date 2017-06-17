package dev.tornaco.tasker.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.tornaco.tasker.test.Module;

/**
 * Created by Nick on 2017/6/14 9:50
 */

public class InstrumentationLauncherService extends Service {

    private ExecutorService SINGLE_THREAD_SERVICE = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceStub();
    }

    private class ServiceStub extends Binder implements InstrumentationLauncher {

        @Override
        public String launch(Module module, AbortSignal signal)
                throws Exception {
            final ModuleFuture moduleFuture = new ModuleFuture(module);
            SINGLE_THREAD_SERVICE.submit(moduleFuture);

            signal.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    moduleFuture.cancel(true);
                }
            });

            return moduleFuture.get();
        }
    }
}
