package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.RequestMakerBg;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Common.ToastClass;
import com.leadinfosoft.littleland.ModelClass.GetAttendenceDailyListModel;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.DailyAttendance;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.utill.Utils;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lead on 7/25/2017.
 */

public class DailyAttendanceAdapter extends RecyclerView.Adapter<DailyAttendanceAdapter.MyViewHolder> {

    Context context;
    boolean isEnglish;
    ArrayList<GetAttendenceDailyListModel> getAttendenceDailyListModelArrayList = new ArrayList<>();

    ImageLoader imageLoader;
    DisplayImageOptions options;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;

    Response_string<String> resp;
    RequestMakerBg req;

    String currentdate = "";

    String str_attendance = "";

    String fontPath = "";
    Typeface tf;

    public DailyAttendanceAdapter(Context context, boolean isEnglish, ArrayList<GetAttendenceDailyListModel> getAttendenceDailyListModelArrayList) {
        this.context = context;
        this.isEnglish = isEnglish;
        this.getAttendenceDailyListModelArrayList = getAttendenceDailyListModelArrayList;

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.no_image_available)
                .showImageOnFail(R.drawable.no_image_available)
                .showImageOnLoading(R.drawable.loading).build();

        con = new ConnectionDetector(context);
        sharedPreferencesClass = new SharedPreferencesClass(context);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentdate = df.format(c.getTime());

        Logger.e("08/08 absent or present daily attendence current daet=====> " + currentdate + "");

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            fontPath = "fonts/GE_Flow_Regular.otf";
            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        } else {
            fontPath = "fonts/Lato-Bold.ttf";
            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

//        itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.dailyattendence_rowlist, parent, false);

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dailyattendence_rowlist_ar, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dailyattendence_rowlist, parent, false);
        }


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (position % 2 == 0) {
            //holder.rootView.setBackgroundColor(Color.BLACK);
            holder.ll_root.setBackgroundResource(R.color.month_attend_gray);
        } else {
            //holder.rootView.setBackgroundColor(Color.WHITE);
            holder.ll_root.setBackgroundResource(R.color.white);
        }

        if (getAttendenceDailyListModelArrayList.get(position).getPresent().equalsIgnoreCase("-1")) {
            holder.rl_atten.setVisibility(View.VISIBLE);
            holder.tv_absent_status.setVisibility(View.GONE);
            holder.tv_present_status.setVisibility(View.GONE);
        } else if (getAttendenceDailyListModelArrayList.get(position).getPresent().equalsIgnoreCase("1")) {
            holder.rl_atten.setVisibility(View.GONE);
            holder.tv_absent_status.setVisibility(View.GONE);
            holder.tv_present_status.setVisibility(View.VISIBLE);
        } else {
            holder.rl_atten.setVisibility(View.GONE);
            holder.tv_absent_status.setVisibility(View.VISIBLE);
            holder.tv_present_status.setVisibility(View.GONE);
        }

        holder.tv_present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.rl_atten.setVisibility(View.GONE);
                holder.tv_present_status.setVisibility(View.VISIBLE);

                str_attendance = "present";

                GetDailyAttendenceWebservice(getAttendenceDailyListModelArrayList.get(position).getUid());


                GetAttendenceDailyListModel getAttendenceDailyListModel = getAttendenceDailyListModelArrayList.get(position);

                getAttendenceDailyListModel.setPresent("1");


            }
        });
        holder.tv_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.rl_atten.setVisibility(View.GONE);
                holder.tv_absent_status.setVisibility(View.VISIBLE);

                str_attendance = "absent";

                GetDailyAttendenceWebservice(getAttendenceDailyListModelArrayList.get(position).getUid());

                GetAttendenceDailyListModel getAttendenceDailyListModel = getAttendenceDailyListModelArrayList.get(position);

                getAttendenceDailyListModel.setPresent("0");


            }
        });


        holder.tv_absent_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rl_atten.setVisibility(View.GONE);
                holder.tv_absent_status.setVisibility(View.GONE);
                holder.tv_present_status.setVisibility(View.VISIBLE);

                str_attendance = "present";

                GetDailyAttendenceWebservice(getAttendenceDailyListModelArrayList.get(position).getUid());

                GetAttendenceDailyListModel getAttendenceDailyListModel = getAttendenceDailyListModelArrayList.get(position);

                getAttendenceDailyListModel.setPresent("1");


            }
        });

        holder.tv_present_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rl_atten.setVisibility(View.GONE);
                holder.tv_absent_status.setVisibility(View.VISIBLE);
                holder.tv_present_status.setVisibility(View.GONE);

                str_attendance = "absent";

                GetDailyAttendenceWebservice(getAttendenceDailyListModelArrayList.get(position).getUid());

                GetAttendenceDailyListModel getAttendenceDailyListModel = getAttendenceDailyListModelArrayList.get(position);

                getAttendenceDailyListModel.setPresent("0");


            }
        });

        holder.tv_name.setText(getAttendenceDailyListModelArrayList.get(position).getFname() + " " + getAttendenceDailyListModelArrayList.get(position).getLname());
        imageLoader.displayImage(getAttendenceDailyListModelArrayList.get(position).getProfile_pic(), holder.iv_profile, options);

        setTypeface_Text(holder);


    }

    private void setTypeface_Text(MyViewHolder holder) {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            holder.tv_absent.setTypeface(tf);
            holder.tv_absent_status.setTypeface(tf);
            holder.tv_name.setTypeface(tf);
            holder.tv_present.setTypeface(tf);
            holder.tv_present_status.setTypeface(tf);


        } else {
            holder.tv_absent.setTypeface(tf);
            holder.tv_absent_status.setTypeface(tf);
            holder.tv_name.setTypeface(tf);
            holder.tv_present.setTypeface(tf);
            holder.tv_present_status.setTypeface(tf);
        }

    }

    @Override
    public int getItemCount() {
        return getAttendenceDailyListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.ll_root)
        LinearLayout ll_root;
        @BindView(R.id.tv_present)
        MyHeaderTextView tv_present;
        @BindView(R.id.tv_absent)
        MyHeaderTextView tv_absent;
        @BindView(R.id.tv_present_status)
        MyHeaderTextView tv_present_status;
        @BindView(R.id.tv_absent_status)
        MyHeaderTextView tv_absent_status;
        @BindView(R.id.rl_atten)
        RelativeLayout rl_atten;
        @BindView(R.id.tv_name)
        MyHeaderTextView tv_name;
        @BindView(R.id.iv_profile)
        CircularImageView iv_profile;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    private void GetDailyAttendenceWebservice(String student_uid) {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("Present OR Absent WebService  Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                        ToastClass.ToastSuccess(context, jsonObject + "");
                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("action", "add_attendance"));
        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("student_uid", student_uid));

        params.add(new BasicNameValuePair("class_id", sharedPreferencesClass.getSelected_Class_Id()));

        params.add(new BasicNameValuePair("type", sharedPreferencesClass.getUser_Type()));
        params.add(new BasicNameValuePair("date", currentdate));
        params.add(new BasicNameValuePair("attendance", str_attendance));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("Present OR Absent WebService Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMakerBg(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_attendance_daily);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }
}
