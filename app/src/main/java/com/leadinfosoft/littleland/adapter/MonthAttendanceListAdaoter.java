package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.leadinfosoft.littleland.R;

/**
 * Created by Lead on 7/24/2017.
 */

public class MonthAttendanceListAdaoter extends BaseAdapter {
    Context  context;
    LayoutInflater layoutInflater;
    public MonthAttendanceListAdaoter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        convertView= layoutInflater.inflate(R.layout.monthattendrecy_rowlist, null);

        return convertView;
    }
}
