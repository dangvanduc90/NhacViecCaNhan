package com.example.chung.nhacvieccanhan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chung.nhacvieccanhan.data.SQLite;
import com.example.chung.nhacvieccanhan.helpers.AlarmHelper;
import com.example.chung.nhacvieccanhan.model.CongViec;
import com.example.chung.nhacvieccanhan.ultils.ConstClass;
import com.example.chung.nhacvieccanhan.ultils.UtilLog;

public class ChiTietCongViecActivity extends AppCompatActivity {
    private static final String TAG = "ChiTietCongViecActivity";

    Button btnQuayLai;
    TextView tvTenCV, tvMoTaCV, tvDate, tvTime, tvDiaDiem, tvLoaiCV;
    CongViec congViec;
    long id;
    boolean isFromService;
    static SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_cong_viec);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        id = intent.getLongExtra(ConstClass.INTENT_ID_CONGVIEC, 0);
        isFromService = intent.getBooleanExtra(ConstClass.INTENT_FROM_SERVICE, false);

        db = new SQLite(this, ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);
        initView();

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Cursor cursor = db.GetData("SELECT * FROM CongViec where id = " + id);
        cursor.moveToFirst();

        long id = cursor.getLong(0);
        String ten = cursor.getString(1);
        String moTa = cursor.getString(2);
        long thoiGian = cursor.getLong(3);
        String diaDiem = cursor.getString(4);
        int maLoaiCV = cursor.getInt(5);
        int thoiGianLap = cursor.getInt(6);

        congViec = new CongViec(
                id,
                ten,
                moTa,
                thoiGian,
                diaDiem,
                maLoaiCV,
                thoiGianLap
        );
        cursor.close();

        tvTenCV.setText(ten);
        tvMoTaCV.setText(moTa);
        tvTime.setText(AlarmHelper.formatDateTime(congViec));
        tvDiaDiem.setText(diaDiem);

        Cursor cursorQr = db.GetData("SELECT * FROM LoaiCongViec where id = " + maLoaiCV);
        cursorQr.moveToFirst();
        String tenLoaiCV = cursorQr.getString(1);
        cursorQr.close();

        tvLoaiCV.setText(tenLoaiCV);
    }

    private void initView() {
        tvTenCV = (TextView) findViewById(R.id.tvTenCV);
        tvMoTaCV = (TextView) findViewById(R.id.tvMoTaCV);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvDiaDiem = (TextView) findViewById(R.id.tvDiaDiem);
        tvLoaiCV = (TextView) findViewById(R.id.tvLoaiCV);
        btnQuayLai = (Button) findViewById(R.id.btnQuayLai);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.baothuc_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        final int position = info.position;
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(ChiTietCongViecActivity.this);
                builder.setMessage("Bạn có chắc chắn muốn xóa");
                builder.setCancelable(true);
                builder.setTitle("Xác nhận xóa");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlarmHelper.deleteAlarm(ChiTietCongViecActivity.this, congViec);
                        Toast.makeText(ChiTietCongViecActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isFromService) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
