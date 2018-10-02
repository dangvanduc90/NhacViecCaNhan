package com.example.chung.nhacvieccanhan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chung.nhacvieccanhan.data.SQLite;
import com.example.chung.nhacvieccanhan.ultils.ConstClass;
import com.example.chung.nhacvieccanhan.ultils.UtilLog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    Button btnLoaiCongViec, btnCongViec, btnBanDo;
    static SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        btnLoaiCongViec.setOnClickListener(this);
        btnCongViec.setOnClickListener(this);
        btnBanDo.setOnClickListener(this);

        try {
            db = new SQLite(MainActivity.this, "NhacViecCaNhan.sqlite", null, ConstClass.VERSION_DATABASE);
            // tao bang
            db.QueryData("DROP TABLE IF EXISTS LoaiCongViec");
            db.QueryData("DROP TABLE IF EXISTS CongViec");

            db.QueryData("CREATE TABLE LoaiCongViec (id INTEGER PRIMARY KEY AUTOINCREMENT, TenLoaiCV VARCHAR, MoTaLoaiCV VARCHAR)");
            db.QueryData("CREATE TABLE CongViec (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "TenCV VARCHAR, " +
                    "MoTa VARCHAR, " +
                    "Ngay date, " +
                    "ThoiGian time, " +
                    "DiaDiem VARCHAR, " +
                    "MaLoaiCV INTEGER," +
                    "ThoiGianLap INTEGER" +
                    ")");
            db.QueryData("INSERT INTO LoaiCongViec VALUES (1, 'mua dien thoai samsung j3 ex', '12312323a')");
            db.QueryData("INSERT INTO LoaiCongViec VALUES (2, 'mua dien thoai samsung j3 null', '12312323a')");

        } catch (Exception e) {
            UtilLog.log_d(TAG, e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        btnLoaiCongViec = (Button) findViewById(R.id.btnLoaiCongViec);
        btnCongViec = (Button) findViewById(R.id.btnCongViec);
        btnBanDo = (Button) findViewById(R.id.btnBanDo);
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
            case R.id.btnBanDo:
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                break;
            default:
                break;
        }
    }
}
