package com.example.chung.nhacvieccanhan.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.chung.nhacvieccanhan.AlarmScreenActivity;
import com.example.chung.nhacvieccanhan.R;

import java.io.IOException;

import static android.app.Notification.VISIBILITY_PUBLIC;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.EXTRA_ON_OF;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_ID_CONGVIEC;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_MOTA_CONGVIEC;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_TEN_CONGVIEC;

public class SongService extends Service {

    private static final int MY_NOTIFICATION_ID = 12345;
    MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String string_receive = intent.getExtras().getString(EXTRA_ON_OF);
        Log.d("string_receive", string_receive);
        if (string_receive.equals("off")) {
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    Notification builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_clock)
                        .setAutoCancel(false)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_clock))
                        .setColor(Color.BLUE)
                        .setContentTitle("Bao thuc")
                        .setContentText("Chuyen de thuc hanh")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setVisibility(VISIBILITY_PUBLIC)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setFullScreenIntent(null, true)
                        .addAction(R.mipmap.snoone, "Action", null)
                        .addAction(R.mipmap.cancel, "Dismiss", null)
                        .build();
                    NotificationManager notificationService  =
                            (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationService.notify(MY_NOTIFICATION_ID, builder);
                }

                AssetFileDescriptor descriptor = getAssets().openFd("nhac_chuong.mp3");
                player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                player.prepare();
                player.start();

                Intent alarmIntent = new Intent(getBaseContext(), AlarmScreenActivity.class);
                alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (intent.hasExtra(INTENT_ID_CONGVIEC)) {
                    String id = intent.getExtras().getString(INTENT_ID_CONGVIEC);
                    String tenCV = intent.getExtras().getString(INTENT_TEN_CONGVIEC);
                    String moTaCV = intent.getExtras().getString(INTENT_MOTA_CONGVIEC);

                    alarmIntent.putExtra(INTENT_TEN_CONGVIEC, tenCV);
                    alarmIntent.putExtra(INTENT_MOTA_CONGVIEC, moTaCV);
                    getApplication().startActivity(alarmIntent);
                }

                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Log.d("mediap" , "stopped");
                    }
                });

            } catch (IOException e) {
                Log.d("error11111111111111", e.getMessage());
                e.printStackTrace();
            }
        }
        return START_NOT_STICKY;
    }
}
