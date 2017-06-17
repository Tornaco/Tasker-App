package dev.tornaco.tasker.app.service;

import android.support.annotation.WorkerThread;

import dev.tornaco.tasker.test.Module;

/**
 * Created by Nick on 2017/6/14 9:50
 */

public interface InstrumentationLauncher {
    @WorkerThread
    String launch(Module module, AbortSignal signal) throws Exception;
}
