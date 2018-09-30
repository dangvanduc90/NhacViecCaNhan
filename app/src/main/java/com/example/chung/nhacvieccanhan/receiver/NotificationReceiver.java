package com.example.chung.nhacvieccanhan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.chung.nhacvieccanhan.ultils.UtilLog;

import static com.example.chung.nhacvieccanhan.ultils.ConstClass.ACTION_OFF_TOAST;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.ACTION_ON_TOAST;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_ID_CONGVIEC;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            long id = intent.getExtras().getLong(INTENT_ID_CONGVIEC);
            UtilLog.log_d(TAG, id+"");
            switch (intent.getAction()) {
                case ACTION_ON_TOAST:
                    Toast.makeText(context, "ACTION_ON_TOAST", Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_OFF_TOAST:
                    Toast.makeText(context, "ACTION_OFF_TOAST", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}