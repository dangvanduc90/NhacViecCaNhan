package com.example.chung.nhacvieccanhan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chung.nhacvieccanhan.R;
import com.example.chung.nhacvieccanhan.model.ThoiGianBaoThuc;

import java.util.List;

public class BaoThucListViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<ThoiGianBaoThuc> ThoiGianBaoThucList;

    public BaoThucListViewAdapter(Context context, int layout, List<ThoiGianBaoThuc> ThoiGianBaoThucList) {
        this.context = context;
        this.layout = layout;
        this.ThoiGianBaoThucList = ThoiGianBaoThucList;
    }

    @Override
    public int getCount() {
        return ThoiGianBaoThucList.size();
    }

    @Override
    public Object getItem(int position) {
        return ThoiGianBaoThucList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ThoiGianBaoThucList.get(position).getId();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_bao_thuc, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ThoiGianBaoThuc ThoiGianBaoThuc = ThoiGianBaoThucList.get(position);

        viewHolder.tvDate.setText(ThoiGianBaoThuc.getNgay());
        viewHolder.tvTime.setText(ThoiGianBaoThuc.getThoiGian());

        return convertView;
    }

    class ViewHolder {
        TextView tvDate, tvTime;
    }

}
