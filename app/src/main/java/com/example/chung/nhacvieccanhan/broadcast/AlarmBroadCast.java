package com.example.chung.nhacvieccanhan.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.chung.nhacvieccanhan.model.SongService;

public class AlarmBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, SongService.class);
        myIntent.putExtra("extra", intent.getExtras().getString("extra"));
        Log.d("extra", intent.getExtras().getString("extra"));
        context.startService(myIntent);


//        // TODO Auto-generated method stub
//        String action = intent.getAction();
//        if (!TextUtils.isEmpty(action)) {
//            Log.d("action", action);
//        }
    }
}
