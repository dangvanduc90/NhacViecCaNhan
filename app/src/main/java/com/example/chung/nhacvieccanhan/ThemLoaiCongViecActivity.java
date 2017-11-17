package com.example.chung.nhacvieccanhan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ThemLoaiCongViecActivity extends AppCompatActivity {

    EditText edtTenLoaiCV, edtMoTaLoaiCV;
    Button btnThem, btnHuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_loai_cong_viec);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initView();

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThemLoaiCongViecActivity.this, LoaiCongViecActivity.class));
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TenLoaiCV = edtTenLoaiCV.getText().toString();
                String MoTaLoaiCV = edtMoTaLoaiCV.getText().toString();
                MainActivity.db.QueryData("INSERT INTO LoaiCongViec VALUES (null, '"+TenLoaiCV+"', '"+MoTaLoaiCV+"')");
                startActivity(new Intent(ThemLoaiCongViecActivity.this, LoaiCongViecActivity.class));
            }
        });
    }

    private void initView() {
        edtTenLoaiCV = (EditText) findViewById(R.id.edtTenLoaiCV);
        edtMoTaLoaiCV = (EditText) findViewById(R.id.edtMoTaLoaiCV);
        btnThem = (Button) findViewById(R.id.btnThem);
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
