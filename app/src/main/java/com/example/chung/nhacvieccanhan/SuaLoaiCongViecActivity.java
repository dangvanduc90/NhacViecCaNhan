package com.example.chung.nhacvieccanhan;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chung.nhacvieccanhan.model.LoaiCongViec;
import com.example.chung.nhacvieccanhan.ultils.ConstClass;

public class SuaLoaiCongViecActivity extends AppCompatActivity {

    EditText edtTenLoaiCV, edtMoTaLoaiCV;
    Button btnSua, btnHuy;
    LoaiCongViec loaiCongViec;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_loai_cong_viec);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        id = intent.getLongExtra(ConstClass.INTENT_ID_LOADICONGVIEC, 0);
        initView();

        Cursor cursor = MainActivity.db.GetData("SELECT * FROM LoaiCongViec where id =" + id);
        cursor.moveToFirst();

        loaiCongViec = new LoaiCongViec(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2)
        );
        String ten = loaiCongViec.getTenLoaiCV();
        String moTa = loaiCongViec.getMoTaLoaiCV();

        edtTenLoaiCV.setText(ten);
        edtMoTaLoaiCV.setText(moTa);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TenLoaiCV = edtTenLoaiCV.getText().toString();
                String MoTaLoaiCV = edtMoTaLoaiCV.getText().toString();
                MainActivity.db.QueryData("UPDATE LoaiCongViec SET TenLoaiCV = '"+TenLoaiCV+"', MoTaLoaiCV = '"+MoTaLoaiCV+"' where id = " + id);
                onBackPressed();
            }
        });
    }

    private void initView() {
        edtTenLoaiCV = (EditText) findViewById(R.id.edtTenLoaiCV);
        edtMoTaLoaiCV = (EditText) findViewById(R.id.edtMoTaLoaiCV);
        btnSua = (Button) findViewById(R.id.btnSua);
        btnHuy = (Button) findViewById(R.id.btnHuy);
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
