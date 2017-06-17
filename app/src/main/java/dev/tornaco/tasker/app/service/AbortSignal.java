package dev.tornaco.tasker.app.service;

import java.util.Observable;

/**
 * Created by Nick on 2017/6/14 10:03
 */

public class AbortSignal extends Observable {

    public void abort(String reason) {
        setChanged();
        notifyObservers("Abort with reason:" + reason);
    }

    public void abort() {
        setChanged();
        notifyObservers();
    }
}
