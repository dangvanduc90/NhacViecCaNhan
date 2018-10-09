package com.example.chung.nhacvieccanhan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.chung.nhacvieccanhan.data.SQLite;
import com.example.chung.nhacvieccanhan.helpers.AlarmHelper;
import com.example.chung.nhacvieccanhan.model.CongViec;
import com.example.chung.nhacvieccanhan.ultils.ConstClass;
import com.example.chung.nhacvieccanhan.ultils.UtilLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThemCongViecActivity extends AppCompatActivity {
    private final String TAG = "ThemCongViecActivity";

    ArrayAdapter arrLoaiCongViec, arrThoiGianLap;
    EditText edtTen, edtMoTa, edtDate, edtTime, edtDiaDiem;
    Spinner spnLoaiCV, spnThoiGianLap;
    Button btnDatePicker, btnTimePicker, btnSubmit, btnCancel;
    Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute, maLoaiCV, thoiGianLap;
    List<String> tenLoaiCVList;
    List<Integer> thoiGianLapList, maLoaiCVList;
    static SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_cong_viec);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        db = new SQLite(this, ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);
        initView();
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        Cursor cursor = db.GetData("SELECT * FROM LoaiCongViec");

        maLoaiCVList = new ArrayList<>();
        tenLoaiCVList = new ArrayList<>();
        thoiGianLapList = new ArrayList<>();

        while (cursor.moveToNext()) {
            maLoaiCVList.add(cursor.getInt(0));
            tenLoaiCVList.add(cursor.getString(1));
        }
        cursor.close();

        for (int i = 0; i < 60; i++) {
            thoiGianLapList.add(i);
        }

        if (maLoaiCVList.size() > 0) {
            maLoaiCV = maLoaiCVList.get(0);
        }
        if (thoiGianLapList.size() > 0) {
            thoiGianLap = thoiGianLapList.get(0);
        }

        arrLoaiCongViec = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tenLoaiCVList);
        arrLoaiCongViec.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnLoaiCV.setAdapter(arrLoaiCongViec);

        arrThoiGianLap = new ArrayAdapter(this, android.R.layout.simple_spinner_item, thoiGianLapList);
        arrThoiGianLap.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        spnThoiGianLap.setAdapter(arrThoiGianLap);

        spnLoaiCV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maLoaiCV = maLoaiCVList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spnThoiGianLap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                thoiGianLap = thoiGianLapList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ThemCongViecActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edtDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ThemCongViecActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        edtTime.setText(hourOfDay + ":" + minute);
                        mHour = hourOfDay;
                        mMinute = minute;
                    }
                }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(mYear, mMonth, mDay, mHour, mMinute, 0);

                CongViec congViec = new CongViec(
                        edtTen.getText().toString(),
                        edtMoTa.getText().toString(),
                        calendar.getTimeInMillis(),
                        edtDiaDiem.getText().toString(),
                        maLoaiCV,
                        thoiGianLap
                );

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put("TenCV", congViec.getTenCV());
                values.put("MoTa", congViec.getMoTa());
                values.put("ThoiGian", congViec.getThoigian());
                values.put("DiaDiem", congViec.getDiaDiem());
                values.put("MaLoaiCV", congViec.getMaLoaiCV());
                values.put("ThoiGianLap", congViec.getThoiGianLap());

                long id = db.Insert("CongViec", values);
                congViec.setId(id);

                AlarmHelper.createAlarm(ThemCongViecActivity.this, congViec);
                onBackPressed();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        edtTen = (EditText) findViewById(R.id.edtTenCV);
        edtMoTa = (EditText) findViewById(R.id.edtMoTaCV);
        edtDate = (EditText) findViewById(R.id.edtDate);
        edtTime = (EditText) findViewById(R.id.edtTime);
        edtDiaDiem = (EditText) findViewById(R.id.edtDiaDiem);
        spnLoaiCV = (Spinner) findViewById(R.id.spnLoaiCV);
        spnThoiGianLap = (Spinner) findViewById(R.id.spnThoiGianLap);
        btnDatePicker = (Button) findViewById(R.id.btnDatePicker);
        btnTimePicker = (Button) findViewById(R.id.btnTimePicker);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnCancel = (Button) findViewById(R.id.btnCancel);
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
