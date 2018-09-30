package com.example.chung.nhacvieccanhan.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.chung.nhacvieccanhan.data.SQLite;
import com.example.chung.nhacvieccanhan.model.CongViec;
import com.example.chung.nhacvieccanhan.model.SongService;
import com.example.chung.nhacvieccanhan.receiver.AlarmReceiver;
import com.example.chung.nhacvieccanhan.ultils.UtilLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.chung.nhacvieccanhan.ultils.ConstClass.EXTRA_ON_OF;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_ID_CONGVIEC;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.OFF;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.ON;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.REQUEST_CODE_ALARM_MANAGER;

public class AlarmHelper {
    private static final String TAG = "AlarmHelper";

    public static void deleteAlarm(Context mContext, CongViec congViec) {
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, SongService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, REQUEST_CODE_ALARM_MANAGER, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        intent.putExtra(EXTRA_ON_OF, OFF);
        intent.putExtra(INTENT_ID_CONGVIEC, congViec.getId());
        mContext.startService(intent);
    }

    public static void createAlarm(Context mContext, CongViec congViec) {
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra(EXTRA_ON_OF, ON);
        intent.putExtra(INTENT_ID_CONGVIEC, congViec.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                mContext, REQUEST_CODE_ALARM_MANAGER, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date mDate = null;
        try {
            mDate = dateFormat.parse(congViec.getNgay());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int hours = Integer.parseInt(congViec.getThoigian().split(":")[0]);
        int minutes = Integer.parseInt(congViec.getThoigian().split(":")[1]);
        mDate.setHours(hours);
        mDate.setMinutes(minutes);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mDate.getTime());
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
    
    public static void SnoozeAlarm(Context mContext, CongViec congViec, SQLite db) {
        List<Integer> soThoiGianLapList = new ArrayList<>();
        int countSnoozeAlarm = 0;

        Cursor cursor = db.GetData("SELECT * FROM ThoiGianLap");
        while (cursor.moveToNext()) {
            soThoiGianLapList.add(cursor.getInt(1));
        }
        cursor.close();

        if (countSnoozeAlarm < soThoiGianLapList.size()) {
            countSnoozeAlarm++;
        } else {
            countSnoozeAlarm = soThoiGianLapList.size() - 1;
        }

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, SongService.class);
        intent.putExtra(EXTRA_ON_OF, ON);
        intent.putExtra(INTENT_ID_CONGVIEC, congViec.getId());
        PendingIntent pendingIntent = PendingIntent.getService(
                mContext, REQUEST_CODE_ALARM_MANAGER, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (soThoiGianLapList.size() > 0 && countSnoozeAlarm < soThoiGianLapList.size()) {
            long nextTime = currentTime + soThoiGianLapList.get(countSnoozeAlarm) * 60000;
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pendingIntent);
        }
    }
}
