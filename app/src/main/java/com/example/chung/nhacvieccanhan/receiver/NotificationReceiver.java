package com.example.chung.nhacvieccanhan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import com.example.chung.nhacvieccanhan.data.SQLite;
import com.example.chung.nhacvieccanhan.helpers.AlarmHelper;
import com.example.chung.nhacvieccanhan.model.CongViec;
import com.example.chung.nhacvieccanhan.ultils.ConstClass;
import com.example.chung.nhacvieccanhan.ultils.UtilLog;

import static com.example.chung.nhacvieccanhan.ultils.ConstClass.ACTION_OFF_TOAST;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.ACTION_ON_TOAST;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.DELETE_SUCCESS;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_ID_CONGVIEC;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.SNOOZE_SUCCESS;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";
    static SQLite db;
    private Cursor cursor;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            long id = intent.getExtras().getLong(INTENT_ID_CONGVIEC);
            db = new SQLite(context, "NhacViecCaNhan.sqlite", null, ConstClass.VERSION_DATABASE);
            cursor = db.GetData("SELECT * FROM CongViec where id = " + id);
            cursor.moveToFirst();

            String ten = cursor.getString(1);
            String moTa = cursor.getString(2);
            String ngay = cursor.getString(3);
            String thoiGian = cursor.getString(4);
            String diaDiem = cursor.getString(5);
            int maLoaiCV = cursor.getInt(6);
            CongViec congViec = new CongViec(
                    id,
                    ten,
                    moTa,
                    ngay,
                    thoiGian,
                    diaDiem,
                    maLoaiCV);

            switch (intent.getAction()) {
                case ACTION_ON_TOAST:
                    Toast.makeText(context, SNOOZE_SUCCESS, Toast.LENGTH_SHORT).show();
                    AlarmHelper.deleteAlarm(context, congViec);
                    AlarmHelper.SnoozeAlarm(context, congViec);
                    break;
                case ACTION_OFF_TOAST:
                    Toast.makeText(context, DELETE_SUCCESS, Toast.LENGTH_SHORT).show();
                    AlarmHelper.deleteAlarm(context, congViec);
                    break;
            }
        }
    }
}