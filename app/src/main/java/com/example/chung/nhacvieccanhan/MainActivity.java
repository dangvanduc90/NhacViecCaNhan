package com.example.chung.nhacvieccanhan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chung.nhacvieccanhan.data.SQLite;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLoaiCongViec, btnCongViec, btnThoiGianBaoThuc, btnThoiGianLap;
    static SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        btnLoaiCongViec.setOnClickListener(this);
        btnCongViec.setOnClickListener(this);
        btnThoiGianBaoThuc.setOnClickListener(this);
        btnThoiGianLap.setOnClickListener(this);

        try {
            db = new SQLite(MainActivity.this, "NhacViecCaNhan.sqlite", null, 1);
            // tao bang
            db.QueryData("DROP TABLE IF EXISTS LoaiCongViec");
            db.QueryData("DROP TABLE IF EXISTS CongViec");
            db.QueryData("DROP TABLE IF EXISTS ThoiGianBaoThuc");
            db.QueryData("DROP TABLE IF EXISTS ThoiGianLap");

            db.QueryData("CREATE TABLE LoaiCongViec (id INTEGER PRIMARY KEY AUTOINCREMENT, TenLoaiCV VARCHAR, MoTaLoaiCV VARCHAR)");
            db.QueryData("CREATE TABLE CongViec (id INTEGER PRIMARY KEY AUTOINCREMENT, TenCV VARCHAR, MoTa VARCHAR, Ngay date, ThoiGian time, DiaDiem VARCHAR, MaLoaiCV INTEGER)");
            db.QueryData("CREATE TABLE ThoiGianBaoThuc (id INTEGER PRIMARY KEY AUTOINCREMENT, MaCV INTEGER, Ngay date, ThoiGian time)");
            db.QueryData("CREATE TABLE ThoiGianLap (id INTEGER PRIMARY KEY AUTOINCREMENT, SoThoiGianLap INTEGER)");

            // add record
            db.QueryData("INSERT INTO CongViec VALUES (null, 'đi mua điện thoại', 'mô tả công viêc,ád', '2014-12-22', '12:10:59', 'định công', 1)");
            db.QueryData("INSERT INTO LoaiCongViec VALUES (null, 'mua dien thoai samsung j3 ex', '12312323a')");
            db.QueryData("INSERT INTO LoaiCongViec VALUES (null, 'mua dien thoai samsung j3 null', '12312323a')");
            db.QueryData("INSERT INTO ThoiGianBaoThuc VALUES (null, 1, '2014-12-22', '12:11:00')");
            db.QueryData("INSERT INTO ThoiGianBaoThuc VALUES (null, 1, '2014-12-22', '12:15:00')");
//            db.QueryData("INSERT INTO ThoiGianLap VALUES (null, 12)");
//            db.QueryData("INSERT INTO ThoiGianLap VALUES (null, 13)");
//            db.QueryData("INSERT INTO ThoiGianLap VALUES (null, 14)");

        } catch (Exception e) {
            Log.d("sqlerror", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        btnLoaiCongViec = (Button) findViewById(R.id.btnLoaiCongViec);
        btnCongViec = (Button) findViewById(R.id.btnCongViec);
        btnThoiGianBaoThuc = (Button) findViewById(R.id.btnThoiGianBaoThuc);
        btnThoiGianLap = (Button) findViewById(R.id.btnThoiGianLap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoaiCongViec:
                startActivity(new Intent(MainActivity.this, LoaiCongViecActivity.class));
                break;
            case R.id.btnCongViec:
                startActivity(new Intent(MainActivity.this, CongViecActivity.class));
                break;
            case R.id.btnThoiGianBaoThuc:
                startActivity(new Intent(MainActivity.this, ThoiGianBaoThucActivity.class));
                break;
            case R.id.btnThoiGianLap:
                startActivity(new Intent(MainActivity.this, ThoiGianLapActivity.class));
                break;
            default:
                break;
        }
    }
}
