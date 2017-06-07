package dev.tornaco.tasker.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


/**
 * Created by Nick on 2017/5/9 17:07
 * E-Mail: Tornaco@163.com
 * All right reserved.
 */

public class TaskerBridgeService extends Service{

    private boolean mIsDestroyed;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
