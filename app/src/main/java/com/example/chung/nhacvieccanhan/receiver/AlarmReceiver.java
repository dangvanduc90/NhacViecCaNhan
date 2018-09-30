package com.example.chung.nhacvieccanhan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.chung.nhacvieccanhan.model.SongService;
import com.example.chung.nhacvieccanhan.ultils.UtilLog;

import static com.example.chung.nhacvieccanhan.ultils.ConstClass.EXTRA_ON_OF;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_ID_CONGVIEC;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, SongService.class);
        myIntent.putExtra(EXTRA_ON_OF, intent.getExtras().getString(EXTRA_ON_OF));
        myIntent.putExtra(INTENT_ID_CONGVIEC, intent.getExtras().getLong(INTENT_ID_CONGVIEC));
        context.startService(myIntent);

        // TODO Auto-generated method stub
        String action = intent.getAction();
        if (!TextUtils.isEmpty(action)) {
            UtilLog.log_d(TAG, action);
        }
    }
}
