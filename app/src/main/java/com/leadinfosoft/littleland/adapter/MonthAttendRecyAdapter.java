package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.ModelClass.AttendenceReportListModel;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.leadinfosoft.littleland.widget.MyTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lead on 7/15/2017.
 */

public class MonthAttendRecyAdapter extends RecyclerView.Adapter<MonthAttendRecyAdapter.MyViewHolder> {

    Context context;
    boolean isEnglish;
    ArrayList<AttendenceReportListModel> attendenceReportListModelArrayList = new ArrayList<>();

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    SharedPreferencesClass sharedPreferencesClass;

    public MonthAttendRecyAdapter(Context context, boolean isEnglish, ArrayList<AttendenceReportListModel> attendenceReportListModelArrayList) {
        this.context = context;
        this.isEnglish = isEnglish;
        this.attendenceReportListModelArrayList = attendenceReportListModelArrayList;

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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.monthattendrecy_rowlist_ar, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.monthattendrecy_rowlist, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        holder.tv_year.setText(attendenceReportListModelArrayList.get(position).getYear());

        holder.tv_month.setText(attendenceReportListModelArrayList.get(position).getMonth());

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            holder.tv_present.setText(attendenceReportListModelArrayList.get(position).getPresent() + " " + Common.filter("lbl_days", "ar"));
            holder.tv_absent.setText(attendenceReportListModelArrayList.get(position).getAbsent() + " " + Common.filter("lbl_days", "ar"));
        } else {
            holder.tv_present.setText(attendenceReportListModelArrayList.get(position).getPresent() + " " + Common.filter("lbl_days", "en"));
            holder.tv_absent.setText(attendenceReportListModelArrayList.get(position).getAbsent() + " " + Common.filter("lbl_days", "en"));
        }


      /*  holder.tv_present.setText(attendenceReportListModelArrayList.get(position).getPresent() + Common.filter("lbl_days", "ar"));
        holder.tv_absent.setText(attendenceReportListModelArrayList.get(position).getAbsent());*/

        setTypeface_Text(holder);


        if (position == 0) {
            holder.tv_year.setVisibility(View.VISIBLE);

        } else {
            holder.tv_year.setVisibility(View.VISIBLE);
        }
        if (position % 2 == 0) {
            //holder.rootView.setBackgroundColor(Color.BLACK);
            holder.ll_root.setBackgroundResource(R.color.month_attend_gray);
        } else {
            //holder.rootView.setBackgroundColor(Color.WHITE);
            holder.ll_root.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return attendenceReportListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_root)
        LinearLayout ll_root;

        MyTextView tv_year;
        MyHeaderTextView tv_month, tv_present, tv_absent;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            tv_year = (MyTextView) view.findViewById(R.id.tv_year);
            tv_month = (MyHeaderTextView) view.findViewById(R.id.tv_month);
            tv_present = (MyHeaderTextView) view.findViewById(R.id.tv_present);
            tv_absent = (MyHeaderTextView) view.findViewById(R.id.tv_absent);

        }

    }

    private void setTypeface_Text(MyViewHolder holder) {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            holder.tv_absent.setTypeface(tf);
            holder.tv_month.setTypeface(tf);
            holder.tv_present.setTypeface(tf);
            holder.tv_year.setTypeface(tf);

        } else {
            holder.tv_absent.setTypeface(tf);
            holder.tv_month.setTypeface(tf);
            holder.tv_present.setTypeface(tf);
            holder.tv_year.setTypeface(tf);
        }

        holder.tv_absent.setTypeface(tf_numeric);
        holder.tv_month.setTypeface(tf);
        holder.tv_present.setTypeface(tf_numeric);
        holder.tv_year.setTypeface(tf_numeric);

    }
}

