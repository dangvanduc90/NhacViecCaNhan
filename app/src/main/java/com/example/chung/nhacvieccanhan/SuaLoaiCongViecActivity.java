package com.example.chung.nhacvieccanhan;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chung.nhacvieccanhan.model.LoaiCongViec;

public class SuaLoaiCongViecActivity extends AppCompatActivity {

    EditText edtTenLoaiCV, edtMoTaLoaiCV;
    Button btnSua, btnHuy;
    LoaiCongViec loaiCongViec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_loai_cong_viec);

        Intent intent = getIntent();
        final int id = Integer.parseInt(intent.getStringExtra("id"));
        Toast.makeText(this, intent.getStringExtra("id") + "" , Toast.LENGTH_LONG).show();

        initView();

        Cursor cursor = MainActivity.db.GetData("SELECT * FROM LoaiCongViec where id =" + id);
        cursor.moveToFirst();

        String ten = cursor.getString(1);
        String moTa = cursor.getString(2);

        edtTenLoaiCV.setText(ten);
        edtMoTaLoaiCV.setText(moTa);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuaLoaiCongViecActivity.this, LoaiCongViecActivity.class));
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TenLoaiCV = edtTenLoaiCV.getText().toString();
                String MoTaLoaiCV = edtMoTaLoaiCV.getText().toString();
                MainActivity.db.QueryData("UPDATE LoaiCongViec SET TenLoaiCV = '"+TenLoaiCV+"', MoTaLoaiCV = '"+MoTaLoaiCV+"' where id = " + id);
                startActivity(new Intent(SuaLoaiCongViecActivity.this, LoaiCongViecActivity.class));
            }
        });
    }
    private void initView() {
        edtTenLoaiCV = (EditText) findViewById(R.id.edtTenLoaiCV);
        edtMoTaLoaiCV = (EditText) findViewById(R.id.edtMoTaLoaiCV);
        btnSua = (Button) findViewById(R.id.btnSua);
        btnHuy = (Button) findViewById(R.id.btnHuy);
    }
}
