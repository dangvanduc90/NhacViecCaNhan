package com.example.chung.nhacvieccanhan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import com.example.chung.nhacvieccanhan.helpers.AlarmHelper;
import com.example.chung.nhacvieccanhan.model.CongViec;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SuaCongViecActivity extends AppCompatActivity {
    private static final String TAG = "SuaCongViecActivity";

    ArrayAdapter arrayAdapter;

    EditText edtTen, edtMoTa, edtDate, edtTime, edtDiaDiem;
    Spinner spnLoaiCV;
    Button btnDatePicker, btnTimePicker, btnSubmit, btnCancel;

    Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Cursor cursor;
    int loaiCV;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_cong_viec);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        id = Integer.parseInt(intent.getStringExtra("id"));

        initView();

        calendar = Calendar.getInstance();

        cursor = MainActivity.db.GetData("SELECT * FROM LoaiCongViec");

        final List<Integer> maLoaiCVList = new ArrayList<>();
        List<String> tenLoaiCVList = new ArrayList<>();

        while (cursor.moveToNext()) {
            maLoaiCVList.add(cursor.getInt(0));
            tenLoaiCVList.add(cursor.getString(1));
        }
        cursor.close();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tenLoaiCVList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnLoaiCV.setAdapter(arrayAdapter);

        spnLoaiCV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loaiCV = maLoaiCVList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cursor = MainActivity.db.GetData("SELECT * FROM CongViec where id = " + id);
        cursor.moveToFirst();

        String ten = cursor.getString(1);
        String moTa = cursor.getString(2);
        String ngay = cursor.getString(3);
        String thoiGian = cursor.getString(4);
        String diaDiem = cursor.getString(5);
        int maLoaiCV = cursor.getInt(6);

        edtTen.setText(ten);
        edtMoTa.setText(moTa);
        edtDate.setText(ngay);
        edtTime.setText(thoiGian);
        edtDiaDiem.setText(diaDiem);

        cursor.close();

        for (int i = 0; i < maLoaiCVList.size(); i++) {
            if (maLoaiCVList.get(i) == maLoaiCV) {
                spnLoaiCV.setSelection(i);
            }
        }

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SuaCongViecActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                mHour = calendar.get(Calendar.HOUR);
                mMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(SuaCongViecActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                String ten = edtTen.getText().toString();
                String moTa = edtMoTa.getText().toString();
                String date = edtDate.getText().toString();
                String time = edtTime.getText().toString();
                String diaDiem = edtDiaDiem.getText().toString();

                MainActivity.db.QueryData("UPDATE CongViec SET TenCV = '"+ten+"', MoTa ='"+moTa+"', Ngay = '"+date+"', ThoiGian = '"+time+"', DiaDiem = '"+diaDiem+"', MaLoaiCV = "+loaiCV);

                CongViec congViec = new CongViec(
                        id,
                        ten,
                        moTa,
                        date,
                        time,
                        diaDiem,
                        loaiCV);
                AlarmHelper.deleteAlarm(SuaCongViecActivity.this, congViec);
                AlarmHelper.createAlarm(SuaCongViecActivity.this, congViec);
                startActivity(new Intent(SuaCongViecActivity.this, CongViecActivity.class));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuaCongViecActivity.this, CongViecActivity.class));
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
