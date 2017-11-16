package com.example.chung.nhacvieccanhan;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.chung.nhacvieccanhan.model.SongService;

import static com.example.chung.nhacvieccanhan.ultils.ConstClass.EXTRA_ON_OF;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_MOTA_CONGVIEC;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_TEN_CONGVIEC;

public class AlarmScreenActivity extends AppCompatActivity {

    Button btnDismiss, btnSnooze;
    TextView tvAlarmTenCV, tvAlarmMoTaCV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        String tenCV = getIntent().getExtras().getString(INTENT_TEN_CONGVIEC);
        String moTaCV = getIntent().getExtras().getString(INTENT_MOTA_CONGVIEC);

        initView();

        tvAlarmTenCV.setText(tenCV);
        tvAlarmMoTaCV.setText(moTaCV);

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAlarm();
                finish();
            }
        });

        btnSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SnoozeAlarm();
            }
        });
    }

    private void SnoozeAlarm() {
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(AlarmScreenActivity.this, SongService.class);
        intent.putExtra(EXTRA_ON_OF, "on");
        PendingIntent pendingIntent = PendingIntent.getService(
                AlarmScreenActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        alarmManager.set(AlarmManager.RTC_WAKEUP, 1000, pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, 1000, pendingIntent);
        }
    }

    private void initView() {
        btnDismiss = (Button) findViewById(R.id.btnDismiss);
        btnSnooze = (Button) findViewById(R.id.btnSnooze);
        tvAlarmTenCV = (TextView) findViewById(R.id.tvAlarmTenCV);
        tvAlarmMoTaCV = (TextView) findViewById(R.id.tvAlarmMoTaCV);
    }

    private void deleteAlarm() {
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(AlarmScreenActivity.this, SongService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmScreenActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        intent.putExtra(EXTRA_ON_OF, "off");
        startService(intent);
    }
}
