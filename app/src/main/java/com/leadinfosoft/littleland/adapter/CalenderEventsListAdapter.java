package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.ModelClass.CalenderEventsList;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by admin on 1/21/2017.
 */

public class CalenderEventsListAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater = null;
    ArrayList<CalenderEventsList> calenderEventsListArrayList = new ArrayList<CalenderEventsList>();

    SharedPreferencesClass sharedPreferencesClass;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    public CalenderEventsListAdapter(Context context, ArrayList<CalenderEventsList> calenderEventsListArrayList) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.calenderEventsListArrayList = calenderEventsListArrayList;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        sharedPreferencesClass = new SharedPreferencesClass(context);

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            fontPath = "fonts/GE_Flow_Regular.otf";
            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        } else {
            fontPath = "fonts/Lato-Bold.ttf";
            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        }

        fontPath_numeric = "fonts/Lato-Bold.ttf";
        tf_numeric = Typeface.createFromAsset(context.getAssets(), fontPath_numeric);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return calenderEventsListArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        LinearLayout ll_main;
        TextView tv_date, tv_event_name;
        ImageView iv_bell;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            rowView = inflater.inflate(R.layout.custom_calender_events_ar, null);

        } else {
            rowView = inflater.inflate(R.layout.custom_calender_events, null);

        }


        String color = "#" + calenderEventsListArrayList.get(position).getColor();
        Drawable mDrawable = null;
        try {
            mDrawable = context.getResources().getDrawable(R.drawable.calender_green_round_bg);
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor(color), PorterDuff.Mode.ADD));
        } catch (Exception e) {
            e.printStackTrace();
            mDrawable = context.getResources().getDrawable(R.drawable.calender_green_round_bg);
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.ADD));
        }


        holder.ll_main = (LinearLayout) rowView.findViewById(R.id.ll_main);

        holder.ll_main.setBackgroundDrawable(mDrawable);

        holder.tv_date = (TextView) rowView.findViewById(R.id.tv_date);
        holder.tv_event_name = (TextView) rowView.findViewById(R.id.tv_event_name);
        holder.iv_bell = (ImageView) rowView.findViewById(R.id.iv_bell);

        holder.tv_date.setTypeface(tf_numeric);
        holder.tv_event_name.setTypeface(tf);

        try {
            holder.tv_date.setText(getMonth(calenderEventsListArrayList.get(position).getDatetime()) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tv_event_name.setText(calenderEventsListArrayList.get(position).getNote());
//        holder.tv_description.setText(coinsListModelArrayList.get(position).getDescription());

        return rowView;
    }

    public String convertW3CTODeviceTimeZone(String strDate) throws Exception {
        Date simpleDateFormatW3C = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(strDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(simpleDateFormatW3C);
        String monthName = new SimpleDateFormat("MM").format(cal.getTime());
        return monthName;

    }

    private static int getMonth(String str_date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(str_date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day;
    }

}
