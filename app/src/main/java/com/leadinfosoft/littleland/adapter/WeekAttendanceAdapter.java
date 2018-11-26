package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.ModelClass.AttendenceReportWeeklyListModel;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.leadinfosoft.littleland.widget.MyTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lead on 7/14/2017.
 */

public class WeekAttendanceAdapter extends RecyclerView.Adapter<WeekAttendanceAdapter.MyViewHolder> {

    Context context;
    boolean isEnglish;
    ArrayList<AttendenceReportWeeklyListModel> attendenceReportListModelArrayList = new ArrayList<>();

    SharedPreferencesClass sharedPreferencesClass;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;


    public WeekAttendanceAdapter(Context context, boolean isEnglish, ArrayList<AttendenceReportWeeklyListModel> attendenceReportListModelArrayList) {
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
                    .inflate(R.layout.weekattendance_rowlist_ar, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.weekattendance_rowlist, parent, false);
        }


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        if (attendenceReportListModelArrayList.get(position).getDate().equalsIgnoreCase("") || attendenceReportListModelArrayList.get(position).getDate().length() == 0) {

        } else {
            holder.tv_date.setText(Common.changeDateFormat_yyyy_mm_dd(attendenceReportListModelArrayList.get(position).getDate()));
            holder.tv_absent_date.setText(Common.changeDateFormat_yyyy_mm_dd(attendenceReportListModelArrayList.get(position).getDate()));

        }

        holder.tv_date.setTypeface(tf_numeric);
        holder.tv_absent_date.setTypeface(tf_numeric);
        holder.tv_day.setTypeface(tf_numeric);
        holder.tv_status.setTypeface(tf);
        holder.tv_remark.setTypeface(tf);

        holder.tv_absent_day.setTypeface(tf);
        holder.tv_absent_status.setTypeface(tf);
        holder.tv_absent_remark.setTypeface(tf);


        holder.tv_day.setText(Common.GetDayofMonth(attendenceReportListModelArrayList.get(position).getDate()));
        holder.tv_status.setText(attendenceReportListModelArrayList.get(position).getStatus());
        holder.tv_remark.setText(attendenceReportListModelArrayList.get(position).getRemark());

        holder.tv_absent_day.setText(Common.GetDayofMonth(attendenceReportListModelArrayList.get(position).getDate()));
        holder.tv_absent_status.setText(attendenceReportListModelArrayList.get(position).getStatus());
        holder.tv_absent_remark.setText(attendenceReportListModelArrayList.get(position).getRemark());

        if (attendenceReportListModelArrayList.get(position).getRemark().equalsIgnoreCase("") || attendenceReportListModelArrayList.get(position).getRemark().length() == 0) {

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                holder.tv_remark.setText(Common.filter("lbl_no_remarks", "ar"));
                holder.tv_absent_remark.setText(Common.filter("lbl_no_remarks", "ar"));
            } else {
                holder.tv_remark.setText(Common.filter("lbl_no_remarks", "en"));
                holder.tv_absent_remark.setText(Common.filter("lbl_no_remarks", "en"));

            }

           /* holder.tv_remark.setText("No Remarks");
            holder.tv_absent_remark.setText("No Remarks");*/

        } else {
            holder.tv_remark.setText(attendenceReportListModelArrayList.get(position).getRemark());
            holder.tv_absent_remark.setText(attendenceReportListModelArrayList.get(position).getRemark());


        }


        if (attendenceReportListModelArrayList.get(position).getStatus().equalsIgnoreCase("ABSENT")) {

            holder.rl_absent.setVisibility(View.VISIBLE);
            holder.rl_present.setVisibility(View.GONE);
        } else {
            holder.rl_absent.setVisibility(View.GONE);
            holder.rl_present.setVisibility(View.VISIBLE);

        }

        /*if (position==5)
        {
            holder.rl_attend.setBackgroundResource(R.drawable.weekattend_absent);
            holder.rl_notes.setBackgroundResource(R.drawable.weekattend_absent);
            holder.iv_status.setImageResource(R.drawable.cancel);
             if (isEnglish)
            {
             holder.iv_arrow.setImageResource(R.drawable.ic_absent_arrow);
            }
            else {
                 holder.iv_arrow.setImageResource(R.drawable.ic_absent_arrow_ar);
            }


        }*/
    }

    @Override
    public int getItemCount() {

        Logger.e("25/08 attendenceReportListModelArrayList size=======>" + attendenceReportListModelArrayList.size() + "");

        return attendenceReportListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_attend)
        RelativeLayout rl_attend;
        @BindView(R.id.iv_status)
        ImageView iv_status;
        @BindView(R.id.iv_arrow)
        ImageView iv_arrow;
        @BindView(R.id.rl_notes)
        RelativeLayout rl_notes;

        @BindView(R.id.rl_absent_attend)
        RelativeLayout rl_absent_attend;
        @BindView(R.id.iv_absent_status)
        ImageView iv_absent_status;
        @BindView(R.id.iv_absent_arrow)
        ImageView iv_absent_arrow;
        @BindView(R.id.rl_absent_notes)
        RelativeLayout rl_absent_notes;

        RelativeLayout rl_present, rl_absent;


        MyTextView tv_date, tv_remark, tv_absent_date, tv_absent_remark;
        MyHeaderTextView tv_day, tv_status, tv_absent_day, tv_absent_status;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            rl_present = (RelativeLayout) view.findViewById(R.id.rl_present);
            rl_absent = (RelativeLayout) view.findViewById(R.id.rl_absent);

            tv_date = (MyTextView) view.findViewById(R.id.tv_date);
            tv_day = (MyHeaderTextView) view.findViewById(R.id.tv_day);
            tv_status = (MyHeaderTextView) view.findViewById(R.id.tv_status);
            tv_remark = (MyTextView) view.findViewById(R.id.tv_remark);


            tv_absent_date = (MyTextView) view.findViewById(R.id.tv_absent_date);
            tv_absent_day = (MyHeaderTextView) view.findViewById(R.id.tv_absent_day);
            tv_absent_status = (MyHeaderTextView) view.findViewById(R.id.tv_absent_status);
            tv_absent_remark = (MyTextView) view.findViewById(R.id.tv_absent_remark);

        }

    }
}
