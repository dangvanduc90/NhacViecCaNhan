package com.example.chung.nhacvieccanhan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chung.nhacvieccanhan.data.SQLite;
import com.example.chung.nhacvieccanhan.ultils.ConstClass;
import com.example.chung.nhacvieccanhan.ultils.UtilLog;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    Button btnLoaiCongViec, btnCongViec, btnFilter;
    static SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        btnLoaiCongViec.setOnClickListener(this);
        btnCongViec.setOnClickListener(this);
        btnFilter.setOnClickListener(this);

        try {
            db = new SQLite(MainActivity.this, ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);
            // tao bang
            db.QueryData("DROP TABLE IF EXISTS LoaiCongViec");
            db.QueryData("DROP TABLE IF EXISTS CongViec");

            db.QueryData("CREATE TABLE IF NOT EXISTS LoaiCongViec (id INTEGER PRIMARY KEY AUTOINCREMENT, TenLoaiCV VARCHAR, MoTaLoaiCV VARCHAR)");
            db.QueryData("CREATE TABLE IF NOT EXISTS CongViec (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "TenCV VARCHAR, " +
                    "MoTa VARCHAR, " +
                    "ThoiGian INTEGER, " +
                    "DiaDiem VARCHAR, " +
                    "MaLoaiCV INTEGER," +
                    "ThoiGianLap INTEGER" +
                    ")");
//            db.QueryData("INSERT INTO LoaiCongViec VALUES (1, 'mua dien thoai samsung j3 ex', '12312323a')");
//            db.QueryData("INSERT INTO LoaiCongViec VALUES (2, 'mua dien thoai samsung j3 null', '12312323a')");
//
//            db.QueryData("INSERT INTO CongViec VALUES (" +
//                    "1, " +
//                    "'đi mua điện thoại', " +
//                    "'mô tả công viêc mua điện thoại', " +
//                    "1539097140003, " +
//                    "'định công'," +
//                    "1," +
//                    "0" +
//                    ")");
//            db.QueryData("INSERT INTO CongViec VALUES (" +
//                    "2, " +
//                    "'đi mua quần áo', " +
//                    "'mô tả mua quần áo', " +
//                    "1539097140003, " +
//                    "'định công2'," +
//                    "1," +
//                    "0" +
//                    ")");
//
//            db.QueryData("INSERT INTO CongViec VALUES (" +
//                    "3, " +
//                    "'đi mua laptop', " +
//                    "'mô tả công viêc mua laptop', " +
//                    "1539097140003, " +
//                    "'định công3'," +
//                    "1," +
//                    "0" +
//                    ")");

        } catch (Exception e) {
            UtilLog.log_d(TAG, e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        btnLoaiCongViec = (Button) findViewById(R.id.btnLoaiCongViec);
        btnCongViec = (Button) findViewById(R.id.btnCongViec);
        btnFilter = (Button) findViewById(R.id.btnFilter);
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
            case R.id.btnFilter:
                startActivity(new Intent(MainActivity.this, FilterActivity.class));
                break;
            default:
                break;
        }
    }
}
