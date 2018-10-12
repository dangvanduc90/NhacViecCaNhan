package com.example.chung.nhacvieccanhan;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.example.chung.nhacvieccanhan.adapter.CongViecListViewAdapter;
import com.example.chung.nhacvieccanhan.data.SQLite;
import com.example.chung.nhacvieccanhan.model.CongViec;
import com.example.chung.nhacvieccanhan.ultils.ConstClass;
import com.example.chung.nhacvieccanhan.ultils.UtilLog;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private static final String TAG = "FilterActivity";
    private EditText edtKeyword;
    private Spinner spnFilterBy;
    private Button btnFilter;
    GridView gridView;
    CongViecListViewAdapter adapter;

    private String keyword = null;
    private int idFilterBy = 0;
    static SQLite db;
    Cursor cursor;
    private List<CongViec> congViecList, mCongViecList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initView();
        db = new SQLite(this, ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);
        congViecList = new ArrayList<>();
        mCongViecList = new ArrayList<>();

        registerForContextMenu(gridView);

        adapter = new CongViecListViewAdapter(this, congViecList, db);
        gridView.setAdapter(adapter);

        String arr[] = getResources().getStringArray(R.array.testArray);
        ArrayAdapter<String> adapterSpn = new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        arr
                );

        spnFilterBy.setAdapter(adapterSpn);
        //thiết lập sự kiện chọn phần tử cho Spinner
        spnFilterBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idFilterBy = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = edtKeyword.getText().toString();
                String queryClause = generateQueryClause(keyword, idFilterBy);
                Cursor cursor = db.GetData(queryClause);

                congViecList.clear();
                while (cursor.moveToNext()) {
                    congViecList.add(
                            new CongViec(
                                    cursor.getInt(0),
                                    cursor.getString(1),
                                    cursor.getString(2),
                                    cursor.getLong(3),
                                    cursor.getString(4),
                                    cursor.getInt(5),
                                    cursor.getInt(6)
                            )
                    );
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        edtKeyword = (EditText) findViewById(R.id.edtKeyword);
        spnFilterBy = (Spinner) findViewById(R.id.spnFilterBy);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        gridView = (GridView) findViewById(R.id.gridView);
    }

    private String generateQueryClause(String keyword, int idFilterBy) {
        String queryClause = "SELECT * FROM CongViec where TenCV LIKE '%" + keyword + "%'";
        switch (idFilterBy) {
            case 0:
                queryClause += " ORDER BY TenCV ASC";
                break;
            case 1:
                queryClause += " ORDER BY TenCV DESC";
                break;
            case 2:
                queryClause += " and ThoiGian >= " + System.currentTimeMillis();
                break;
            case 3:
                queryClause += " and ThoiGian < " + System.currentTimeMillis();
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                break;
        }
        return queryClause;
    }
}
