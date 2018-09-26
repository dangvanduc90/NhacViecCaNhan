package com.example.chung.nhacvieccanhan;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChiTietLoaiCongViecActivity extends AppCompatActivity {

    EditText edtTenLoaiCV, edtMoTaLoaiCV;
    Button btnQuayLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_loai_cong_viec);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        int id = Integer.parseInt(intent.getStringExtra("id"));

        initView();

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChiTietLoaiCongViecActivity.this, LoaiCongViecActivity.class));
            }
        });

        Cursor cursor = MainActivity.db.GetData("SELECT * FROM LoaiCongViec where id = " + id);
        cursor.moveToFirst();

        String ten = cursor.getString(1);
        String moTa = cursor.getString(2);
        edtTenLoaiCV.setText(ten);
        edtMoTaLoaiCV.setText(moTa);

        cursor.close();
    }

    private void initView() {
        edtTenLoaiCV = (EditText) findViewById(R.id.edtTenLoaiCV);
        edtMoTaLoaiCV = (EditText) findViewById(R.id.edtMoTaLoaiCV);
        btnQuayLai = (Button) findViewById(R.id.btnQuayLai);
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
