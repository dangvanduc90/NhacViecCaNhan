package com.example.chung.nhacvieccanhan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.chung.nhacvieccanhan.model.ThoiGianLap;

import java.util.ArrayList;
import java.util.List;

public class ThoiGianLapActivity extends AppCompatActivity {

    private List<ThoiGianLap> thoiGianLapList;
    GridView gridView;
    ArrayAdapter adapter;
    private List<Integer> soThoiGianLapList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoi_gian_lap);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ThoiGianLapActivity.this, ThemThoiGianLapActivity.class));
            }
        });
        thoiGianLapList = new ArrayList<>();
        soThoiGianLapList = new ArrayList<>();

        Cursor cursor = MainActivity.db.GetData("SELECT * FROM ThoiGianLap");

        while (cursor.moveToNext()) {
            thoiGianLapList.add(new ThoiGianLap(cursor.getInt(0), cursor.getInt(1)));
            soThoiGianLapList.add(cursor.getInt(1));
        }
        cursor.close();

        initView();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, soThoiGianLapList);
        registerForContextMenu(gridView);

        gridView.setAdapter(adapter);
        
    }
    
    private void initView() {
        gridView = (GridView) findViewById(R.id.gridView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.thoigianlap_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = info.position;
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(ThoiGianLapActivity.this);
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
                        ThoiGianLap thoiGianLap = thoiGianLapList.get(position);
                        MainActivity.db.QueryData("DELETE FROM ThoiGianLap where id = " + thoiGianLap.getId());

                        thoiGianLapList.clear();
                        soThoiGianLapList.clear();

                        Cursor cursor = MainActivity.db.GetData("SELECT * FROM ThoiGianLap");
                        while (cursor.moveToNext()) {
                            thoiGianLapList.add(new ThoiGianLap(cursor.getInt(0), cursor.getInt(1)));
                            soThoiGianLapList.add(cursor.getInt(1));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
}
