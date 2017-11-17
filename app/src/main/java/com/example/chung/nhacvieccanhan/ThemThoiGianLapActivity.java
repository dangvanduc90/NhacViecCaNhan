package com.example.chung.nhacvieccanhan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ThemThoiGianLapActivity extends AppCompatActivity {

    int hour;
    int minute;
    Button btnAlarmOk, btnAlarmCancel;
    Spinner spThoiGianLap;
    List<Integer> hourList, minuteList;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_thoi_gian_lap);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initView();
        hourList = new ArrayList<>();
        minuteList = new ArrayList<>();

        for (int i = 0; i <= 23; i++) {
            hourList.add(i);
        }
        for (int i = 0; i <= 59; i++) {
            minuteList.add(i);
        }
        adapter = new ArrayAdapter(ThemThoiGianLapActivity.this, android.R.layout.simple_spinner_item, minuteList);
        spThoiGianLap.setAdapter(adapter);
        spThoiGianLap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minute = minuteList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                minute = minuteList.get(0);
            }
        });
        btnAlarmOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.db.QueryData("INSERT INTO ThoiGianLap VALUES (null, "+minute+")");
                startActivity(new Intent(ThemThoiGianLapActivity.this, ThoiGianLapActivity.class));
            }
        });
        btnAlarmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        btnAlarmOk = (Button) findViewById(R.id.btnAlarmOk);
        btnAlarmCancel = (Button) findViewById(R.id.btnAlarmCancel);
        spThoiGianLap = (Spinner) findViewById(R.id.spThoiGianLap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
