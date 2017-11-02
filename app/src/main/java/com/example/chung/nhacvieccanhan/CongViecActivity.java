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
import android.widget.GridView;

import com.example.chung.nhacvieccanhan.adapter.CongViecListViewAdapter;
import com.example.chung.nhacvieccanhan.model.CongViec;

import java.util.ArrayList;
import java.util.List;

public class CongViecActivity extends AppCompatActivity {

    private List<CongViec> congViecList;
    GridView gridView;
    CongViecListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cong_viec);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CongViecActivity.this, ThemCongViecActivity.class));
            }
        });

        congViecList = new ArrayList<>();

        Cursor cursor = MainActivity.db.GetData("SELECT * FROM CongViec");

        while (cursor.moveToNext()) {
            congViecList.add(new CongViec(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6)));
        }
        cursor.close();

        initView();
        adapter = new CongViecListViewAdapter(this, R.layout.row_cong_viec, congViecList);
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
        inflater.inflate(R.menu.general_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = info.position;
        CongViec congViec;
        Intent intent;
        switch (item.getItemId()) {
            case R.id.detail:
                congViec = congViecList.get(position);
                intent = new Intent(CongViecActivity.this, ChiTietCongViecActivity.class);
                intent.putExtra("id", congViec.getId() + "");
                startActivity(intent);
                break;
            case R.id.update:
                congViec = congViecList.get(position);
                intent = new Intent(CongViecActivity.this, SuaCongViecActivity.class);
                intent.putExtra("id", congViec.getId() + "");
                startActivity(intent);
                break;
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(CongViecActivity.this);
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
                        CongViec congViec1 = congViecList.get(position);
                        MainActivity.db.QueryData("DELETE FROM CongViec where id = " + congViec1.getId());

                        congViecList.clear();
                        Cursor cursor = MainActivity.db.GetData("SELECT * FROM CongViec");
                        while (cursor.moveToNext()) {
                            congViecList.add(new CongViec(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6)));
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
