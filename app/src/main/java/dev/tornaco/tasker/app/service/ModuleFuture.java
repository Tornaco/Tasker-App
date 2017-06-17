package dev.tornaco.tasker.app.service;

import android.support.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import dev.tornaco.tasker.test.Module;

/**
 * Created by Nick on 2017/6/14 9:54
 */

public class ModuleFuture extends FutureTask<String> {

    public ModuleFuture(@NonNull final Module module) {
        super(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return InstrumentationCaller.launch(module);
            }
        });
    }
}
