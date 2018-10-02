package com.example.chung.nhacvieccanhan;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chung.nhacvieccanhan.helpers.AlarmHelper;
import com.example.chung.nhacvieccanhan.model.CongViec;

import java.util.ArrayList;
import java.util.List;

import static com.example.chung.nhacvieccanhan.ultils.ConstClass.DELETE_SUCCESS;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_ID_CONGVIEC;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.SNOOZE_SUCCESS;

public class AlarmScreenActivity extends AppCompatActivity {
    private static final String TAG = "AlarmScreenActivity";

    Button btnDismiss, btnSnooze;
    TextView tvAlarmTenCV, tvAlarmMoTaCV;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        long id = getIntent().getExtras().getLong(INTENT_ID_CONGVIEC);
        cursor = MainActivity.db.GetData("SELECT * FROM CongViec where id = " + id);
        cursor.moveToFirst();

        String ten = cursor.getString(1);
        String moTa = cursor.getString(2);
        String ngay = cursor.getString(3);
        String thoiGian = cursor.getString(4);
        String diaDiem = cursor.getString(5);
        int maLoaiCV = cursor.getInt(6);
        int thoiGianLap = cursor.getInt(7);

        final CongViec congViec = new CongViec(
                id,
                ten,
                moTa,
                ngay,
                thoiGian,
                diaDiem,
                maLoaiCV,
                thoiGianLap
        );

        initView();

        tvAlarmTenCV.setText(ten);
        tvAlarmMoTaCV.setText(moTa);

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmHelper.deleteAlarm(AlarmScreenActivity.this, congViec);
                Toast.makeText(AlarmScreenActivity.this, DELETE_SUCCESS, Toast.LENGTH_SHORT).show();
            }
        });

        btnSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmHelper.deleteAlarm(AlarmScreenActivity.this, congViec);
                AlarmHelper.SnoozeAlarm(AlarmScreenActivity.this, congViec);
                Toast.makeText(AlarmScreenActivity.this, SNOOZE_SUCCESS, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        btnDismiss = (Button) findViewById(R.id.btnDismiss);
        btnSnooze = (Button) findViewById(R.id.btnSnooze);
        tvAlarmTenCV = (TextView) findViewById(R.id.tvAlarmTenCV);
        tvAlarmMoTaCV = (TextView) findViewById(R.id.tvAlarmMoTaCV);
    }
}
