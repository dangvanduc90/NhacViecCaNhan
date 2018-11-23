package com.example.chung.nhacvieccanhan.helpers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.chung.nhacvieccanhan.model.CongViec;
import com.example.chung.nhacvieccanhan.service.SongService;
import com.example.chung.nhacvieccanhan.receiver.AlarmReceiver;
import com.example.chung.nhacvieccanhan.ultils.UtilLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.chung.nhacvieccanhan.ultils.ConstClass.EXTRA_ON_OF;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_ID_CONGVIEC;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.OFF;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.ON;

public class AlarmHelper {
    private static final String TAG = "AlarmHelper";

    public static String formatDateTime(CongViec congViec) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date mDate = new Date();
        mDate.setTime(congViec.getThoigian());
        return simpleDateFormat.format(mDate);
    }

    public static String formatDate(CongViec congViec) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date mDate = new Date();
        mDate.setTime(congViec.getThoigian());
        return simpleDateFormat.format(mDate);
    }

    public static String formatTime(CongViec congViec) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date mDate = new Date();
        mDate.setTime(congViec.getThoigian());
        return simpleDateFormat.format(mDate);
    }

    public static void deleteAlarm(Context mContext, CongViec congViec) {
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, SongService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) congViec.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

//        NotificationManager notifManager= (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        notifManager.cancelAll();

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
                mContext, (int) congViec.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        long congViecDateTimeMillis = congViec.getThoigian();
        long currentTime = new Date().getTime();
        // thời gian hẹn giờ phải lớn hơn thời gian hiện tai
        if (congViecDateTimeMillis > currentTime) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, congViecDateTimeMillis, pendingIntent);
        }
    }

    public static void SnoozeAlarm(Context mContext, CongViec congViec) {
        if (congViec.getThoiGianLap() > 0) {
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(mContext, SongService.class);
            intent.putExtra(EXTRA_ON_OF, ON);
            intent.putExtra(INTENT_ID_CONGVIEC, congViec.getId());
            PendingIntent pendingIntent = PendingIntent.getService(
                    mContext, (int) congViec.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT
            );

            long congViecDateTimeMillis = congViec.getThoigian();
            long nextTime = congViecDateTimeMillis + congViec.getThoiGianLap() * 60000;

//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
//        cal.setTimeInMillis(nextTime);
//        String date = sdf.format(cal.getTime());
//        UtilLog.log_d(TAG, congViec.getThoiGianLap() + "");
//        UtilLog.log_d(TAG, date);

            alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pendingIntent);

        }
    }
}
