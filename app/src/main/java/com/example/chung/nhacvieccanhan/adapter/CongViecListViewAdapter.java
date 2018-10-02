package com.example.chung.nhacvieccanhan.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chung.nhacvieccanhan.MainActivity;
import com.example.chung.nhacvieccanhan.R;
import com.example.chung.nhacvieccanhan.data.SQLite;
import com.example.chung.nhacvieccanhan.model.CongViec;
import com.example.chung.nhacvieccanhan.ultils.UtilLog;

import java.util.List;

public class CongViecListViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<CongViec> congViecList;
    private SQLite db;

    public CongViecListViewAdapter(Context context, int layout, List<CongViec> congViecList, SQLite db) {
        this.context = context;
        this.layout = layout;
        this.congViecList = congViecList;
        this.db = db;
    }

    @Override
    public int getCount() {
        return congViecList.size();
    }

    @Override
    public Object getItem(int position) {
        return congViecList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return congViecList.get(position).getId();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_cong_viec, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTenCongViec = (TextView) convertView.findViewById(R.id.tvTenCongViec);
            viewHolder.tvMoTaCongViec = (TextView) convertView.findViewById(R.id.tvMoTaCongViec);
            viewHolder.tvNgayCongViec = (TextView) convertView.findViewById(R.id.tvNgayCongViec);
            viewHolder.tvThoiGianCongViec = (TextView) convertView.findViewById(R.id.tvThoiGianCongViec);
            viewHolder.tvTDiaDiemCongViec = (TextView) convertView.findViewById(R.id.tvDiaDiemCongViec);
            viewHolder.tvMaLoaiCongViec = (TextView) convertView.findViewById(R.id.tvMaLoaiCongViec);
            viewHolder.tvThoiGianLap = (TextView) convertView.findViewById(R.id.tvThoiGianLap);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CongViec congViec = congViecList.get(position);

        viewHolder.tvTenCongViec.setText(congViec.getTenCV());
        viewHolder.tvMoTaCongViec.setText(congViec.getMoTa());
        viewHolder.tvNgayCongViec.setText(congViec.getNgay());
        viewHolder.tvThoiGianCongViec.setText(congViec.getThoigian());
        viewHolder.tvTDiaDiemCongViec.setText(congViec.getDiaDiem());
        viewHolder.tvThoiGianLap.setText("Lặp lai sau: " + congViec.getThoiGianLap() + " phút");

        Cursor cursor = db.GetData("SELECT * FROM LoaiCongViec where id = " + congViec.getMaLoaiCV());
        cursor.moveToFirst();
        String ten = cursor.getString(1);
        viewHolder.tvMaLoaiCongViec.setText(ten);

        return convertView;
    }

    class ViewHolder {
        TextView tvTenCongViec, tvMoTaCongViec, tvNgayCongViec, tvThoiGianCongViec, tvTDiaDiemCongViec, tvMaLoaiCongViec, tvThoiGianLap;
    }

}
