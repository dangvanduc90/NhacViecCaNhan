package com.example.chung.nhacvieccanhan;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.chung.nhacvieccanhan.adapter.BaoThucListViewAdapter;
import com.example.chung.nhacvieccanhan.model.CongViec;
import com.example.chung.nhacvieccanhan.model.SongService;
import com.example.chung.nhacvieccanhan.model.ThoiGianBaoThuc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.example.chung.nhacvieccanhan.ultils.ConstClass.EXTRA_ON_OF;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_ID_CONGVIEC;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_MOTA_CONGVIEC;
import static com.example.chung.nhacvieccanhan.ultils.ConstClass.INTENT_TEN_CONGVIEC;

public class ChiTietCongViecActivity extends AppCompatActivity {

    Button btnQuayLai;
    ImageButton ibtnAddBaoThuc;
    TextView tvTenCV, tvMoTaCV, tvDate, tvTime, tvDiaDiem, tvLoaiCV;
    ListView lvBaoThuc;
    List<ThoiGianBaoThuc> thoiGianBaoThucList;
    List<String> hienThiThoiGian;
    BaoThucListViewAdapter adapter;
    CongViec congViec;

    int year;
    int month;
    int day;
    int hour;
    int minute;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_cong_viec);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        thoiGianBaoThucList = new ArrayList<>();
        hienThiThoiGian = new ArrayList<>();
        Intent intent = getIntent();
        id = Integer.parseInt(intent.getStringExtra("id"));

        initView();
        registerForContextMenu(lvBaoThuc);

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChiTietCongViecActivity.this, CongViecActivity.class));
            }
        });

        Cursor cursor = MainActivity.db.GetData("SELECT * FROM CongViec where id = " + id);
        cursor.moveToFirst();

        int id = cursor.getInt(0);
        String ten = cursor.getString(1);
        String moTa = cursor.getString(2);
        String ngay = cursor.getString(3);
        String thoiGian = cursor.getString(4);
        String diaDiem = cursor.getString(5);
        int maLoaiCV = cursor.getInt(6);
        congViec = new CongViec(id, ten, moTa, ngay, thoiGian, diaDiem, maLoaiCV);
        cursor.close();

        tvTenCV.setText(ten);
        tvMoTaCV.setText(moTa);
        tvDate.setText(ngay);
        tvTime.setText(thoiGian);
        tvDiaDiem.setText(diaDiem);

        Cursor cursorQr = MainActivity.db.GetData("SELECT * FROM LoaiCongViec where id = " + maLoaiCV);
        cursorQr.moveToFirst();
        String tenLoaiCV = cursorQr.getString(1);
        cursorQr.close();

        tvLoaiCV.setText(tenLoaiCV);

        thoiGianBaoThucList = getThoiGianByMaCV(id);
        adapter = new BaoThucListViewAdapter(this, R.layout.row_bao_thuc, thoiGianBaoThucList);
        lvBaoThuc.setAdapter(adapter);
        lvBaoThuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                thoiGianBaoThucList.get(i).getId();
            }
        });

        ibtnAddBaoThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAlarmTime();
            }
        });
    }

    private List<ThoiGianBaoThuc> getThoiGianByMaCV(int maCV) {
        List<String> hienThiThoiGian = new ArrayList<>();
        Cursor cursorBaoThuc = MainActivity.db.GetData("SELECT * FROM ThoiGianBaoThuc where MaCV = " + maCV);
        while (cursorBaoThuc.moveToNext()) {
            thoiGianBaoThucList.add(new ThoiGianBaoThuc(cursorBaoThuc.getInt(0), cursorBaoThuc.getInt(1), cursorBaoThuc.getString(2), cursorBaoThuc.getString(3)));
            hienThiThoiGian.add(cursorBaoThuc.getString(2) + " " + cursorBaoThuc.getString(3));
        }
        cursorBaoThuc.close();
        return thoiGianBaoThucList;
    }

    private void dialogAlarmDate() {
        final Dialog dialogAlarmDate = new Dialog(this);
        dialogAlarmDate.setContentView(R.layout.activity_dialog_alarm_datepicker);
        dialogAlarmDate.setTitle("Alarm");
        dialogAlarmDate.show();

        Button btnAlarmOk = (Button) dialogAlarmDate.findViewById(R.id.btnAlarmOk);
        Button btnAlarmCancel = (Button) dialogAlarmDate.findViewById(R.id.btnAlarmCancel);
        final DatePicker tpAlarm = (DatePicker) dialogAlarmDate.findViewById(R.id.tpAlarm);

        btnAlarmOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Calendar calendar;
//                calendar = Calendar.getInstance();
//                calendar.set(Calendar.YEAR, tpAlarm.getYear());
//                calendar.set(Calendar.MONTH, tpAlarm.getMonth());
//                calendar.set(Calendar.DAY_OF_MONTH, tpAlarm.getDayOfMonth());

            year = tpAlarm.getYear();
            month = tpAlarm.getMonth();
            day = tpAlarm.getDayOfMonth();

            GregorianCalendar calendar = new GregorianCalendar(year,month,day, hour, minute);

            AlarmManager alarmManager;
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(ChiTietCongViecActivity.this, SongService.class);
            intent.putExtra(EXTRA_ON_OF, "on");
            intent.putExtra(INTENT_ID_CONGVIEC, congViec.getId());
            intent.putExtra(INTENT_TEN_CONGVIEC, congViec.getTenCV());
            intent.putExtra(INTENT_MOTA_CONGVIEC, congViec.getMoTa());
            PendingIntent pendingIntent = PendingIntent.getService(
                    ChiTietCongViecActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            );

            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

            Toast.makeText(getBaseContext(), "Alarm is set successfully",Toast.LENGTH_SHORT).show();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
            String date = dateFormat.format(new Date(calendar.getTimeInMillis()));
            String time = timeFormat.format(new Date(calendar.getTimeInMillis()));
            Log.d("datetime", ""+date + time);

            dialogAlarmDate.dismiss();

            MainActivity.db.QueryData("INSERT INTO ThoiGianBaoThuc VALUES (null, "+id+", '"+date+"', '"+time+"')");
            thoiGianBaoThucList.clear();
            thoiGianBaoThucList = getThoiGianByMaCV(id);
            adapter.notifyDataSetChanged();
            }
        });
        btnAlarmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAlarmDate.dismiss();
            }
        });
    }

    private void dialogAlarmTime() {
        final Dialog dialogAlarm = new Dialog(this);
        dialogAlarm.setContentView(R.layout.activity_dialog_alarm_timepicker);
        dialogAlarm.setTitle("Alarm");
        dialogAlarm.show();

        Button btnAlarmOk = (Button) dialogAlarm.findViewById(R.id.btnAlarmOk);
        Button btnAlarmCancel = (Button) dialogAlarm.findViewById(R.id.btnAlarmCancel);
        final TimePicker tpAlarm;
        tpAlarm = (TimePicker) dialogAlarm.findViewById(R.id.tpAlarm);


        btnAlarmOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Calendar calendar;
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, tpAlarm.getCurrentHour());
            calendar.set(Calendar.MINUTE, tpAlarm.getCurrentMinute());
            calendar.set(Calendar.SECOND, 0);

            dialogAlarm.dismiss();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hour = tpAlarm.getHour();
                minute = tpAlarm.getMinute();
            }

            dialogAlarmDate();
            }
        });
        btnAlarmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAlarm.dismiss();
            }
        });
    }

    private void deleteAlarm() {
        AlarmManager alarmManager;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(ChiTietCongViecActivity.this, SongService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ChiTietCongViecActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        intent.putExtra(EXTRA_ON_OF, "off");
        startService(intent);
    }

    private void initView() {
        tvTenCV = (TextView) findViewById(R.id.tvTenCV);
        tvMoTaCV = (TextView) findViewById(R.id.tvMoTaCV);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvDiaDiem = (TextView) findViewById(R.id.tvDiaDiem);
        tvLoaiCV = (TextView) findViewById(R.id.tvLoaiCV);
        btnQuayLai = (Button) findViewById(R.id.btnQuayLai);
        lvBaoThuc = (ListView) findViewById(R.id.lvBaoThuc);
        ibtnAddBaoThuc = (ImageButton) findViewById(R.id.ibtnAddBaoThuc);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.baothuc_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = info.position;
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
                        ThoiGianBaoThuc thoiGianBaoThuc = thoiGianBaoThucList.get(position);
                        MainActivity.db.QueryData("DELETE FROM ThoiGianBaoThuc where id = " + thoiGianBaoThuc.getId());
                        thoiGianBaoThucList.clear();
                        thoiGianBaoThucList = getThoiGianByMaCV(id);
                        adapter.notifyDataSetChanged();
                        deleteAlarm();
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
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
