package com.example.chung.nhacvieccanhan;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.chung.nhacvieccanhan.helpers.AlarmHelper;
import com.example.chung.nhacvieccanhan.model.CongViec;
import com.example.chung.nhacvieccanhan.model.SongService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.chung.nhacvieccanhan.ultils.ConstClass.EXTRA_ON_OF;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_ID_CONGVIEC;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.ON;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.REQUEST_CODE_ALARM_MANAGER;

public class AlarmScreenActivity extends AppCompatActivity {
    private static final String TAG = "AlarmScreenActivity";

    Button btnDismiss, btnSnooze;
    TextView tvAlarmTenCV, tvAlarmMoTaCV;
    private List<Integer> soThoiGianLapList;
    int countSnoozeAlarm;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        long id = Long.parseLong(getIntent().getExtras().getString(INTENT_ID_CONGVIEC));
        cursor = MainActivity.db.GetData("SELECT * FROM CongViec where id = " + id);
        cursor.moveToFirst();

        String ten = cursor.getString(1);
        String moTa = cursor.getString(2);
        String ngay = cursor.getString(3);
        String thoiGian = cursor.getString(4);
        String diaDiem = cursor.getString(5);
        int maLoaiCV = cursor.getInt(6);

        final CongViec congViec = new CongViec(
                id,
                ten,
                moTa,
                ngay,
                thoiGian,
                diaDiem,
                maLoaiCV);

        initView();

        tvAlarmTenCV.setText(ten);
        tvAlarmMoTaCV.setText(moTa);

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
                AlarmHelper.deleteAlarm(AlarmScreenActivity.this, congViec);
                finish();
            }
        });

        btnSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmHelper.deleteAlarm(AlarmScreenActivity.this, congViec);
                SnoozeAlarm();
            }
        });
    }

    private void SnoozeAlarm() {
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(AlarmScreenActivity.this, SongService.class);
        intent.putExtra(EXTRA_ON_OF, ON);
        PendingIntent pendingIntent = PendingIntent.getService(
                AlarmScreenActivity.this, REQUEST_CODE_ALARM_MANAGER, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (soThoiGianLapList.size() > 0 && countSnoozeAlarm < soThoiGianLapList.size()) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime + soThoiGianLapList.get(countSnoozeAlarm) * 60000, pendingIntent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (soThoiGianLapList.size() > 0 && countSnoozeAlarm < soThoiGianLapList.size()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, currentTime + soThoiGianLapList.get(countSnoozeAlarm) * 60000, pendingIntent);
            }
        }

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
}
