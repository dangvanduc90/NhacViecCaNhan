package com.example.chung.nhacvieccanhan.model;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by dangvanduc90 on 11/8/2017.
 */

public class TestService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("123", "123");
        return new MyBinder();
    }

    public int add(int a, int b) {
        return a + b;
    }

    public class MyBinder extends Binder {
        public TestService getService() {
            return TestService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("123", "1234");
        return START_REDELIVER_INTENT;
    }
}
