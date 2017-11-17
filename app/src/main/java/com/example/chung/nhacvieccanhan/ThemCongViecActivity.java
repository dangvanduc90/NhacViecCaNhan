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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThemCongViecActivity extends AppCompatActivity {

    ArrayAdapter arrayAdapter;

    EditText edtTen, edtMoTa, edtDate, edtTime, edtDiaDiem;
    Spinner spnLoaiCV;
    Button btnDatePicker, btnTimePicker, btnSubmit, btnCancel;

    Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute;

    int loaiCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_cong_viec);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        
        initView();
        calendar = Calendar.getInstance();

        Cursor cursor = MainActivity.db.GetData("SELECT * FROM LoaiCongViec");

        final List<Integer> maLoaiCVList = new ArrayList<>();
        List<String> tenLoaiCVList = new ArrayList<>();

        while (cursor.moveToNext()) {
            maLoaiCVList.add(cursor.getInt(0));
            tenLoaiCVList.add(cursor.getString(1));
        }
        cursor.close();
        if (maLoaiCVList.size() > 0) {
            loaiCV = maLoaiCVList.get(0);
        }

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

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ThemCongViecActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edtDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(ThemCongViecActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        edtTime.setText(hourOfDay + ":" + minute);
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

                MainActivity.db.QueryData("INSERT INTO CongViec VALUES (null, '"+ten+"', '"+moTa+"', '"+date+"', '"+time+"', '"+diaDiem+"', "+loaiCV+")");
                startActivity(new Intent(ThemCongViecActivity.this, CongViecActivity.class));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThemCongViecActivity.this, CongViecActivity.class));
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
