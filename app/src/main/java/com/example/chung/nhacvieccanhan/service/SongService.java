package com.example.chung.nhacvieccanhan.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.chung.nhacvieccanhan.ChiTietCongViecActivity;
import com.example.chung.nhacvieccanhan.R;
import com.example.chung.nhacvieccanhan.data.SQLite;
import com.example.chung.nhacvieccanhan.model.CongViec;
import com.example.chung.nhacvieccanhan.receiver.NotificationReceiver;
import com.example.chung.nhacvieccanhan.ultils.ConstClass;
import com.example.chung.nhacvieccanhan.ultils.UtilLog;

import java.io.IOException;

public class SongService extends Service {
    private static final String TAG = "SongService";

    MediaPlayer player;
    private NotificationReceiver mNotification;
    static SQLite db;
    long id;
    CongViec congViec;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotification = new NotificationReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConstClass.ACTION_ON_TOAST);
        mFilter.addAction(ConstClass.ACTION_OFF_TOAST);
        registerReceiver(mNotification, mFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String string_receive = intent.getExtras().getString(ConstClass.EXTRA_ON_OF, null);
        id = intent.getExtras().getLong(ConstClass.INTENT_ID_CONGVIEC);
        db = new SQLite(getBaseContext(), ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);

        Cursor cursor = db.GetData("SELECT * FROM CongViec where id = " + id);
        cursor.moveToFirst();

        String ten = cursor.getString(1);
        String moTa = cursor.getString(2);
        long thoiGian = cursor.getLong(3);
        String diaDiem = cursor.getString(4);
        int maLoaiCV = cursor.getInt(5);
        int thoiGianLap = cursor.getInt(6);

        congViec = new CongViec(
                id,
                ten,
                moTa,
                thoiGian,
                diaDiem,
                maLoaiCV,
                thoiGianLap
        );
        cursor.close();

        if (string_receive == ConstClass.OFF) {
            if (player != null) {
                if (player.isPlaying()) {
                    player.stop();
                    player.reset();
                    player.release();
                    player = null;
                }
            }
        } else {
            player = new MediaPlayer();
            try {
                Intent mIntent1 = new Intent();
                mIntent1.setAction(ConstClass.ACTION_ON_TOAST);
                mIntent1.putExtra(ConstClass.INTENT_ID_CONGVIEC, id);
                PendingIntent pendingIntentOn = PendingIntent.getBroadcast(this, (int) id, mIntent1, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent mIntent2 = new Intent();
                mIntent2.setAction(ConstClass.ACTION_OFF_TOAST);
                mIntent2.putExtra(ConstClass.INTENT_ID_CONGVIEC, id);
                PendingIntent pendingIntentOff = PendingIntent.getBroadcast(this, (int) id, mIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_clock)
                        .setAutoCancel(false)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_clock))
                        .setColor(Color.BLUE)
                        .setContentTitle("BTL Android")
                        .setContentText(congViec.getTenCV())
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setFullScreenIntent(null, true)
                        .addAction(R.mipmap.snooze, "Snooze", pendingIntentOn)
                        .addAction(R.mipmap.cancel, "Dismiss", pendingIntentOff)
                        .setAutoCancel(true)
                        .build();
                NotificationManager notificationService =
                        (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationService.notify(ConstClass.MY_NOTIFICATION_ID, builder);
                startForeground(ConstClass.MY_NOTIFICATION_ID, builder);

                AssetFileDescriptor descriptor = getAssets().openFd("nhac_chuong.mp3");
                player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                player.prepare();
                player.start();

                Intent mIntent = new Intent(getBaseContext(), ChiTietCongViecActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.putExtra(ConstClass.INTENT_ID_CONGVIEC, id);
                getApplication().startActivity(mIntent);

            } catch (IOException e) {
                UtilLog.log_d(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
        return START_NOT_STICKY;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNotification);
    }
}
