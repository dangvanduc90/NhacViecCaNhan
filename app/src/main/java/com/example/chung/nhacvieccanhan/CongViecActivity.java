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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.example.chung.nhacvieccanhan.adapter.CongViecListViewAdapter;
import com.example.chung.nhacvieccanhan.data.SQLite;
import com.example.chung.nhacvieccanhan.helpers.AlarmHelper;
import com.example.chung.nhacvieccanhan.model.CongViec;
import com.example.chung.nhacvieccanhan.ultils.ConstClass;
import com.example.chung.nhacvieccanhan.ultils.UtilLog;

import java.util.ArrayList;
import java.util.List;

public class CongViecActivity extends AppCompatActivity {
    private static final String TAG = "CongViecActivity";

    private List<CongViec> congViecList, mCongViecList;
    GridView gridView;
    Button btnSearch;
    EditText edtKeyword;
    CongViecListViewAdapter adapter;
    static SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cong_viec);
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
                startActivity(new Intent(CongViecActivity.this, ThemCongViecActivity.class));
            }
        });

        congViecList = new ArrayList<>();
        mCongViecList = new ArrayList<>();

        Cursor cursor = db.GetData("SELECT * FROM CongViec");

        while (cursor.moveToNext()) {
            congViecList.add(
                    new CongViec(
                            cursor.getLong(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getInt(6),
                            cursor.getInt(7)
                    ));
        }
        cursor.close();
        mCongViecList.addAll(congViecList);

        initView();
        adapter = new CongViecListViewAdapter(this, R.layout.row_cong_viec, congViecList, db);
        registerForContextMenu(gridView);

        gridView.setAdapter(adapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = edtKeyword.getText().toString().trim();
                int text_length = keyword.length();
                if (text_length > 0) {
                    congViecList.clear();
                    for (int i = 0; i < mCongViecList.size(); i++) {
                        CongViec mCongViec = mCongViecList.get(i);
                        if (mCongViec.getTenCV().toLowerCase().contains(keyword.toLowerCase())) {
                            congViecList.add(mCongViec);
                        }
                    }
                } else {
                    congViecList.addAll(mCongViecList);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridView);
        edtKeyword = (EditText) findViewById(R.id.edtKeyword);
        btnSearch = (Button) findViewById(R.id.btnSearch);
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
        final CongViec congViec;
        Intent intent;
        switch (item.getItemId()) {
            case R.id.detail:
                congViec = congViecList.get(position);
                intent = new Intent(CongViecActivity.this, ChiTietCongViecActivity.class);
                intent.putExtra(ConstClass.INTENT_ID_CONGVIEC, congViec.getId());
                startActivity(intent);
                break;
            case R.id.update:
                congViec = congViecList.get(position);
                intent = new Intent(CongViecActivity.this, SuaCongViecActivity.class);
                intent.putExtra(ConstClass.INTENT_ID_CONGVIEC, congViec.getId());
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
                        db.QueryData("DELETE FROM CongViec where id = " + congViecList.get(position).getId());
                        AlarmHelper.deleteAlarm(CongViecActivity.this, congViecList.get(position));
                        congViecList.clear();
                        Cursor cursor = db.GetData("SELECT * FROM CongViec");
                        while (cursor.moveToNext()) {
                            congViecList.add(
                                    new CongViec(
                                            cursor.getLong(0),
                                            cursor.getString(1),
                                            cursor.getString(2),
                                            cursor.getString(3),
                                            cursor.getString(4),
                                            cursor.getString(5),
                                            cursor.getInt(6),
                                            cursor.getInt(7)
                                    ));
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
        Cursor cursor = db.GetData("SELECT * FROM CongViec");
        congViecList.clear();
        while (cursor.moveToNext()) {
            congViecList.add(
                    new CongViec(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getInt(6),
                            cursor.getInt(7)
                    ));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
