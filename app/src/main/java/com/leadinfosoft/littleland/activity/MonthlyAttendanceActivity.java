package com.leadinfosoft.littleland.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.HomeChildListDetailsModel;
import com.leadinfosoft.littleland.ModelClass.HomeClassListDetails;
import com.leadinfosoft.littleland.ModelClass.MonthlyAttendenceReportModel;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.adapter.MonthNewPagerAdapter;
import com.leadinfosoft.littleland.fragment.MonthDemoFra;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MonthlyAttendanceActivity extends AppCompatActivity {

    @BindView(R.id.vp_monthly)
    ViewPager vp_monthly;
    @BindView(R.id.hsv_month)
    HorizontalScrollView hsv_month;
    @BindView(R.id.ll_horizon_scroll)
    LinearLayout ll_horizon_scroll;
    @BindView(R.id.tv_title)
    MyHeaderTextView tv_title;
    @BindView(R.id.tv_subtitle)
    MyTextView tv_subtitle;

    @BindView(R.id.ll_data)
    LinearLayout ll_data;
    @BindView(R.id.ll_loder)
    LinearLayout ll_loder;
    @BindView(R.id.avi_loader)
    AVLoadingIndicatorView avi_loader;

    ArrayList<View> horizontalView;

    Context context;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    LoadingDialog loadingDialog;
    MonthNewPagerAdapter monthAttendPagerAdapter;
    int totalSize = 6;

    Response_string<String> resp;
    RequestMaker req;

    ArrayList<MonthlyAttendenceReportModel> monthlyAttendenceReportModelArrayList = new ArrayList<>();

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    /*@Override
    protected void onStart() {
        super.onStart();

    }*/


    ImageLoader imageLoader;
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        context=MonthlyAttendanceActivity.this;
//        loadingDialog=new LoadingDialog(context);
//        loadingDialog.show();

        commonFunction();


        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            setContentView(R.layout.activity_monthly_attendance);

        } else {
            setContentView(R.layout.activity_monthly_attendance);

        }

        ButterKnife.bind(this);
        monthAttendPagerAdapter = new MonthNewPagerAdapter(getSupportFragmentManager());
        vp_monthly.setAdapter(monthAttendPagerAdapter);
        avi_loader.show();


        Log.e("11111111", "====>>>");

      /*  new Handler().postDelayed(new Runnable() {

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

        initHeader();

        setTypeface_Text();

        GetMonthlyReportWebservice();
    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            tv_subtitle.setTypeface(tf);
            tv_title.setTypeface(tf);

            tv_title.setText(Common.filter("lbl_monthly_attendanced", "ar"));


        } else {
            tv_subtitle.setTypeface(tf);
            tv_title.setTypeface(tf);

            tv_title.setText(Common.filter("lbl_monthly_attendanced", "en"));
        }

    }

    private void commonFunction() {
        context = MonthlyAttendanceActivity.this;
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
            monthAttendPagerAdapter.addFrag(new MonthDemoFra().newInstance(i + "", monthlyAttendenceReportModelArrayList));

        }
        monthAttendPagerAdapter.notifyDataSetChanged();


      /*  monthAttendPagerAdapter.addFrag(new MonthDemoFra());
        monthAttendPagerAdapter.addFrag(new MonthDemoFra());
        monthAttendPagerAdapter.addFrag(new MonthDemoFra());
        monthAttendPagerAdapter.addFrag(new MonthDemoFra());
        monthAttendPagerAdapter.addFrag(new MonthDemoFra());
        monthAttendPagerAdapter.addFrag(new MonthDemoFra());*/


    }


    @OnClick(R.id.iv_back)
    public void onClickBack() {
        finish();
        this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
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

    private void initHeader() {

        if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase(Common.USER_TYPE_TEACHER)) {
            tv_title.setText("Monthly attendanced");
            tv_subtitle.setTextColor(getResources().getColor(R.color.techer_parpal));

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

            } else {
                tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


            }

//            tv_subtitle.setText("Class - " + sharedPreferencesClass.getSelected_Class_Name());
        } else {
            tv_title.setText("Monthly attendanced");
            tv_subtitle.setTextColor(getResources().getColor(R.color.techer_parpal));

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

            } else {
                tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


            }

//            tv_subtitle.setText("Class - " + sharedPreferencesClass.getSelected_Class_Name());


        }

/*
        if (Utils.userSelectedLang(MonthlyAttendanceActivity.this)) {
            tv_title.setText("Monthly attendanced");
            tv_subtitle.setText("Class - Twin Toe");
        } else {
            tv_title.setText("شهري الحضور");
            tv_subtitle.setText("فئة - التوأم تو");
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    private void GetMonthlyReportWebservice() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetMonthlyReportWebservice Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();


                        JSONArray jsonArraysuccess = jsonObject.optJSONArray("success");
                        if (jsonArraysuccess.length() > 0) {
                            monthlyAttendenceReportModelArrayList.clear();


                            for (int i = 0; i < jsonArraysuccess.length(); i++) {
                                JSONObject jsonObject1 = jsonArraysuccess.optJSONObject(i);


                             /*   "uid" : 666,
                                        "fname" : Abdulatif,
                                        "profile_pic" : http:\/\/server.mywebdemo.in\/nurcery\/scripts\/images\/uploads\/48696e2e2b4b8adb9707991904d6e46d17.jpg,
                                        "report" : +[ ... ]*/


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
        params.add(new BasicNameValuePair("action", "monthly_report"));

        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("class_id", sharedPreferencesClass.getSelected_Class_Id()));
        params.add(new BasicNameValuePair("type", sharedPreferencesClass.getUser_Type()));
        params.add(new BasicNameValuePair("date", Common.Current_Date()));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));



        Logger.e("GetMonthlyReportWebservice Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_attendance_report);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }
}
