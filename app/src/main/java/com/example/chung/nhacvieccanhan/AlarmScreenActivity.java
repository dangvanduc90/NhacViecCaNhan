package com.example.chung.nhacvieccanhan;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.chung.nhacvieccanhan.model.SongService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.chung.nhacvieccanhan.ultils.ConstClass.EXTRA_ON_OF;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_MOTA_CONGVIEC;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_TEN_CONGVIEC;

public class AlarmScreenActivity extends AppCompatActivity {

    Button btnDismiss, btnSnooze;
    TextView tvAlarmTenCV, tvAlarmMoTaCV;
    private List<Integer> soThoiGianLapList;
    int countSnoozeAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        String tenCV = getIntent().getExtras().getString(INTENT_TEN_CONGVIEC);
        String moTaCV = getIntent().getExtras().getString(INTENT_MOTA_CONGVIEC);

        initView();

        tvAlarmTenCV.setText(tenCV);
        tvAlarmMoTaCV.setText(moTaCV);

        soThoiGianLapList = new ArrayList<>();
        countSnoozeAlarm = 0;

        Cursor cursor = MainActivity.db.GetData("SELECT * FROM ThoiGianLap");

        while (cursor.moveToNext()) {
            soThoiGianLapList.add(cursor.getInt(1));
        }
        cursor.close();

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

        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (soThoiGianLapList.size() > 0 && countSnoozeAlarm < soThoiGianLapList.size()) {
            Log.d("1234", soThoiGianLapList.get(countSnoozeAlarm) * 60000+"");
            Log.d("1234", currentTime+"");
            Log.d("1234", countSnoozeAlarm+"");

            alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime + soThoiGianLapList.get(countSnoozeAlarm) * 60000, pendingIntent);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (soThoiGianLapList.size() > 0 && countSnoozeAlarm < soThoiGianLapList.size()) {
//                Log.d("1234", soThoiGianLapList.get(countSnoozeAlarm) * 60000+"");
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, currentTime + soThoiGianLapList.get(countSnoozeAlarm) * 60000, pendingIntent);
//            }
//        }

        if (countSnoozeAlarm < soThoiGianLapList.size()) {
            countSnoozeAlarm++;
        } else {
            countSnoozeAlarm = soThoiGianLapList.size() - 1;
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
