package com.leadinfosoft.littleland.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.MonthlyAttendenceReportModel;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.adapter.MonthNewPagerAdapter;
import com.leadinfosoft.littleland.adapter.WeekAttendanceAdapter;
import com.leadinfosoft.littleland.adapter.WeekPagerAdapter;
import com.leadinfosoft.littleland.fragment.MonthDemoFra;
import com.leadinfosoft.littleland.fragment.WeekViewpagerFragment;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.utill.Utils;
import com.leadinfosoft.littleland.widget.LoadingDialog;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.leadinfosoft.littleland.widget.MyTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeekAttendanceActivity extends AppCompatActivity {

    //    @BindView(R.id.rv_weekattend)
//    RecyclerView rv_weekattend;
    @BindView(R.id.tv_title)
    MyHeaderTextView tv_title;
    @BindView(R.id.tv_subtitle)
    MyTextView tv_subtitle;

    @BindView(R.id.vp_monthly)
    ViewPager vp_monthly;
    @BindView(R.id.hsv_month)
    HorizontalScrollView hsv_month;
    @BindView(R.id.ll_horizon_scroll)
    LinearLayout ll_horizon_scroll;
    @BindView(R.id.ll_data)
    LinearLayout ll_data;
    @BindView(R.id.ll_loder)
    LinearLayout ll_loder;
    @BindView(R.id.avi_loader)
    AVLoadingIndicatorView avi_loader;

    ArrayList<View> horizontalView;
    Context context;
    LoadingDialog loadingDialog;
    WeekPagerAdapter weekPagerAdapter;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    Response_string<String> resp;
    RequestMaker req;

    ImageLoader imageLoader;
    DisplayImageOptions options;

    ArrayList<MonthlyAttendenceReportModel> monthlyAttendenceReportModelArrayList = new ArrayList<>();

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        commonFunction();

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            setContentView(R.layout.activity_week_attendance);

        } else {
            setContentView(R.layout.activity_week_attendance);

        }

        ButterKnife.bind(this);
        initHeader();

        setTypeface_Text();

        weekPagerAdapter = new WeekPagerAdapter(getSupportFragmentManager());
        vp_monthly.setAdapter(weekPagerAdapter);
        avi_loader.show();


//        getFragmentlist();
        Log.e("11111111", "====>>>");
//        getHoizontalScrollList();

       /* new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                ll_loder.setVisibility(View.GONE);
                ll_data.setVisibility(View.VISIBLE);

            }
        }, 3000);*/

        vp_monthly.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Log.e("onnnnn111111111111", "1111111111111111111");

                smoothScroll(horizontalView.get(position));

                for (int i = 0; i < horizontalView.size(); i++) {
                    if (i == position) {
                        horizontalView.get(i).findViewById(R.id.iv_disable_user).setVisibility(View.GONE);
                        ((MyHeaderTextView) horizontalView.get(i).findViewById(R.id.tv_studentname)).setTextColor(getResources().getColor(R.color.white));
                        ((MyHeaderTextView) horizontalView.get(i).findViewById(R.id.tv_studentname)).setTextSize(12);
                    } else {
                        horizontalView.get(i).findViewById(R.id.iv_disable_user).setVisibility(View.VISIBLE);
                        ((MyHeaderTextView) horizontalView.get(i).findViewById(R.id.tv_studentname)).setTextColor(getResources().getColor(R.color.attend_disable_txt));
                        ((MyHeaderTextView) horizontalView.get(i).findViewById(R.id.tv_studentname)).setTextSize(12);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       /* LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_weekattend.setLayoutManager(llm);
        WeekAttendanceAdapter weekAttendanceAdapter=new WeekAttendanceAdapter(this,Utils.userSelectedLang(WeekAttendanceActivity.this));
        rv_weekattend.setAdapter(weekAttendanceAdapter);*/

        GetWeeklyReportWebservice();

    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            tv_title.setTypeface(tf);
            tv_subtitle.setTypeface(tf);

            tv_title.setText(Common.filter("lbl_Weekly_attendanced", "ar"));

        } else {
            tv_title.setTypeface(tf);
            tv_subtitle.setTypeface(tf);

            tv_title.setText(Common.filter("lbl_Weekly_attendanced", "en"));
        }

    }

    private void commonFunction() {
        context = WeekAttendanceActivity.this;
        con = new ConnectionDetector(context);
        sharedPreferencesClass = new SharedPreferencesClass(context);

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.no_image_available)
                .showImageOnFail(R.drawable.no_image_available)
                .showImageOnLoading(R.drawable.loading).build();

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            fontPath = "fonts/GE_Flow_Regular.otf";
            tf = Typeface.createFromAsset(getAssets(), fontPath);
        } else {
            fontPath = "fonts/Lato-Bold.ttf";
            tf = Typeface.createFromAsset(getAssets(), fontPath);
        }

        fontPath_numeric = "fonts/Lato-Bold.ttf";
        tf_numeric = Typeface.createFromAsset(getAssets(), fontPath_numeric);

    }

    private void getFragmentlist() {

        for (int i = 0; i < monthlyAttendenceReportModelArrayList.size(); i++) {
            weekPagerAdapter.addFrag(new WeekViewpagerFragment().newInstance(i + "", monthlyAttendenceReportModelArrayList));

        }
      /*  weekPagerAdapter.addFrag(new WeekViewpagerFragment());
        weekPagerAdapter.addFrag(new WeekViewpagerFragment());
        weekPagerAdapter.addFrag(new WeekViewpagerFragment());
        weekPagerAdapter.addFrag(new WeekViewpagerFragment());
        weekPagerAdapter.addFrag(new WeekViewpagerFragment());
        weekPagerAdapter.addFrag(new WeekViewpagerFragment());*/


        weekPagerAdapter.notifyDataSetChanged();

    }

    private void getHoizontalScrollList() {
        horizontalView = new ArrayList<View>();
        for (int i = 0; i < monthlyAttendenceReportModelArrayList.size(); i++) {
            LayoutInflater vi = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.horizontalscroll_rowlist, null);
            LinearLayout lv_student = (LinearLayout) v.findViewById(R.id.lv_student);

            CircularImageView iv_user = (CircularImageView) v.findViewById(R.id.iv_user);
            CircularImageView iv_disable_user = (CircularImageView) v.findViewById(R.id.iv_disable_user);
            MyHeaderTextView tv_studentname = (MyHeaderTextView) v.findViewById(R.id.tv_studentname);


            imageLoader.displayImage(monthlyAttendenceReportModelArrayList.get(i).getProfile_pic(), iv_user, options);
//            imageLoader.displayImage(monthlyAttendenceReportModelArrayList.get(i).getProfile_pic(), iv_disable_user, options);

            tv_studentname.setText(monthlyAttendenceReportModelArrayList.get(i).getFname());

            final int finalI = i;
            lv_student.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vp_monthly.setCurrentItem(finalI);
                }
            });

            if (i == 0) {
                v.findViewById(R.id.iv_disable_user).setVisibility(View.GONE);
                ((MyHeaderTextView) v.findViewById(R.id.tv_studentname)).setTextColor(getResources().getColor(R.color.white));
                ((MyHeaderTextView) v.findViewById(R.id.tv_studentname)).setTextSize(12);

            }
            // insert into main view
            ViewGroup insertPoint = (ViewGroup) ll_horizon_scroll;
            insertPoint.addView(v);
            horizontalView.add(v);
        }


    }

    private void smoothScroll(View view) {
        int vLeft = view.getLeft();
        int vRight = view.getRight();
        int sWidth = hsv_month.getWidth();
        ObjectAnimator animator = ObjectAnimator.ofInt(hsv_month, "scrollX", ((vLeft + vRight - sWidth) / 2));
        animator.setDuration(100);
        animator.start();

        hsv_month.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);

    }

    @OnClick(R.id.iv_back)
    public void onClickBack() {
        finish();
        this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    private void initHeader() {

        if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase(Common.USER_TYPE_TEACHER)) {
            tv_title.setText("Weekly attendanced");
            tv_subtitle.setTextColor(getResources().getColor(R.color.techer_parpal));

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

            } else {
                tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


            }

//            tv_subtitle.setText("Class - " + sharedPreferencesClass.getSelected_Class_Name());
        } else {
            tv_title.setText("Weekly attendanced");
            tv_subtitle.setVisibility(View.VISIBLE);
            tv_subtitle.setTextColor(getResources().getColor(R.color.techer_parpal));

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

            } else {
                tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


            }

//            tv_subtitle.setText("Class - " + sharedPreferencesClass.getSelected_Class_Name());


        }

       /* if (Utils.userSelectedLang(WeekAttendanceActivity.this)) {
            tv_title.setText("Weekly attendanced");
            tv_subtitle.setText("Class - Twin Toe");
        } else {
            tv_title.setText("الحضور أسبوعيا");
            tv_subtitle.setText("فئة - التوأم تو");
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    private void GetWeeklyReportWebservice() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetWeeklyReportWebservice Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();


                        JSONArray jsonArraysuccess = jsonObject.optJSONArray("success");
                        if (jsonArraysuccess.length() > 0) {
                            monthlyAttendenceReportModelArrayList.clear();


                            for (int i = 0; i < jsonArraysuccess.length(); i++) {
                                JSONObject jsonObject1 = jsonArraysuccess.optJSONObject(i);


                               /* "uid": "727",
                                        "fname": "Abdulaziz",
                                        "lname": "Al-Adsani",
                                        "profile_pic": "http:\/\/server.mywebdemo.in\/nurcery\/scripts\/images\/uploads\/b762269e5eef3f0cd6a5a30b18e1a58019.jpg",
                                        "report": [
                                {
                                    "date": "2017-08-25",
                                        "status": "absent",
                                        "remark": ""
                                }
                                ]*/


                                String uid = jsonObject1.optString("uid");
                                String fname = jsonObject1.optString("fname");
                                String profile_pic = jsonObject1.optString("profile_pic");
                                String report = jsonObject1.optJSONArray("report") + "";


                                MonthlyAttendenceReportModel monthlyAttendenceReportModel = new MonthlyAttendenceReportModel();
                                monthlyAttendenceReportModel.setUid(uid);
                                monthlyAttendenceReportModel.setFname(fname);
                                monthlyAttendenceReportModel.setProfile_pic(profile_pic);
                                monthlyAttendenceReportModel.setReport(report);

                                monthlyAttendenceReportModelArrayList.add(monthlyAttendenceReportModel);

                            }
                        } else {
                            //jsonarray length 0

                            monthlyAttendenceReportModelArrayList.clear();
                        }

                        getFragmentlist();

                        getHoizontalScrollList();

                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();


     /*   	REQUIRED
        uid	REQUIRED	Logged-in User's UID
        class_id	REQUIRED	Class ID
        type	REQUIRED	teacher or parent
        date	REQUIRED	Pass current date: YYYY-mm-dd
*/
        params.add(new BasicNameValuePair("action", "weekly_report"));

        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("class_id", sharedPreferencesClass.getSelected_Class_Id()));
        params.add(new BasicNameValuePair("type", sharedPreferencesClass.getUser_Type()));
        params.add(new BasicNameValuePair("date", Common.Current_Date()));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));



        Logger.e("GetWeeklyReportWebservice Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_attendance_report);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }
}
