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
import android.widget.GridView;

import com.example.chung.nhacvieccanhan.adapter.LoaiCongViecListViewAdapter;
import com.example.chung.nhacvieccanhan.data.SQLite;
import com.example.chung.nhacvieccanhan.model.LoaiCongViec;
import com.example.chung.nhacvieccanhan.ultils.ConstClass;
import com.example.chung.nhacvieccanhan.ultils.UtilLog;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.AdapterContextMenuInfo;

public class LoaiCongViecActivity extends AppCompatActivity {
    private static final String TAG = "LoaiCongViecActivity";

    GridView gridView;
    LoaiCongViecListViewAdapter adapter;
    List<LoaiCongViec> loaiCongViecList;
    static SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai_cong_viec);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        db = new SQLite(this, ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoaiCongViecActivity.this, ThemLoaiCongViecActivity.class));
            }
        });

        loaiCongViecList = new ArrayList<>();

        Cursor cursor = db.GetData("SELECT * FROM LoaiCongViec");

        while (cursor.moveToNext()) {
            loaiCongViecList.add(new LoaiCongViec(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
        }
        cursor.close();
        adapter = new LoaiCongViecListViewAdapter(this, R.layout.row_loai_cong_viec, loaiCongViecList);
        gridView = (GridView) findViewById(R.id.gridView);
        registerForContextMenu(gridView);

        gridView.setAdapter(adapter);
//        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                intent.putExtra("id", loaiCongViecList.get(position).getId() + "");
//                startActivity(intent);
//                return true;
//            }
//        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        final int position = info.position;
        LoaiCongViec loaiCongViec;
        Intent intent;
        switch (item.getItemId()) {
            case R.id.update:
                loaiCongViec = loaiCongViecList.get(position);
                intent = new Intent(LoaiCongViecActivity.this, SuaLoaiCongViecActivity.class);
                intent.putExtra(ConstClass.INTENT_ID_LOADICONGVIEC, loaiCongViec.getId());
                startActivity(intent);
                break;
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(LoaiCongViecActivity.this);
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
                        LoaiCongViec loaiCongViec = loaiCongViecList.get(position);
                        db.QueryData("DELETE FROM LoaiCongViec where id = " + loaiCongViec.getId());

                        loaiCongViecList.clear();
                        Cursor cursor = db.GetData("SELECT * FROM LoaiCongViec");
                        while (cursor.moveToNext()) {
                            loaiCongViecList.add(new LoaiCongViec(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = db.GetData("SELECT * FROM LoaiCongViec");
        loaiCongViecList.clear();
        while (cursor.moveToNext()) {
            loaiCongViecList.add(new LoaiCongViec(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
