package com.leadinfosoft.littleland.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.CalenderEventsList;
import com.leadinfosoft.littleland.ModelClass.GetAttendenceDailyListModel;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.adapter.CalenderEventsListAdapter;
import com.leadinfosoft.littleland.adapter.DailyAttendanceAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.utill.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DailyAttendance extends AppCompatActivity {

    Response_string<String> resp;
    RequestMaker req;

    Context context;
    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    String currentdate = "";

    @BindView(R.id.rv_daily_attend)
    RecyclerView rv_daily_attend;

    String str_class_id = "";

    ArrayList<GetAttendenceDailyListModel> getAttendenceDailyListModelArrayList = new ArrayList<>();

    TextView tv_title, tv_subtitle;

    String fontPath = "";
    Typeface tf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        commonFunction();


        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            setContentView(R.layout.activity_daily_attendance_ar);

        } else {
            setContentView(R.layout.activity_daily_attendance);

        }

        ButterKnife.bind(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_daily_attend.setLayoutManager(llm);

        init();


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentdate = df.format(c.getTime());

        Logger.e("08/08 daily attendence current daet=====> " + currentdate + "");

        setTypeface_Text();

        GetDailyAttendenceWebservice();


    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            tv_title.setTypeface(tf);
            tv_subtitle.setTypeface(tf);

            tv_title.setText(Common.filter("lbl_daily_attendence", "ar"));

        } else {
            tv_title.setTypeface(tf);
            tv_subtitle.setTypeface(tf);

            tv_title.setText(Common.filter("lbl_daily_attendence", "en"));
        }

    }

    private void commonFunction() {
        context = DailyAttendance.this;
        con = new ConnectionDetector(context);
        sharedPreferencesClass = new SharedPreferencesClass(context);

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            fontPath = "fonts/GE_Flow_Regular.otf";
            tf = Typeface.createFromAsset(getAssets(), fontPath);
        } else {
            fontPath = "fonts/Lato-Bold.ttf";
            tf = Typeface.createFromAsset(getAssets(), fontPath);
        }
    }

    private void init() {

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        tv_subtitle.setTextColor(getResources().getColor(R.color.techer_parpal));


        if (sharedPreferencesClass.getSelected_Class_Name().equalsIgnoreCase("") || sharedPreferencesClass.getSelected_Class_Name().length() == 0) {
            tv_subtitle.setText("Select Class");

        } else {

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

            } else {
                tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


            }

//            tv_subtitle.setText("Class - " + sharedPreferencesClass.getSelected_Class_Name());


        }

    }

    @OnClick(R.id.iv_back)
    public void onClickBack() {
        finish();
        this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    private void GetDailyAttendenceWebservice() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetDailyAttendenceWebservice WebService  Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

                        JSONArray jsonArraySuccess = jsonObject.optJSONArray("success");

                        if (jsonArraySuccess.length() > 0) {
                            getAttendenceDailyListModelArrayList.clear();
                            for (int i = 0; i < jsonArraySuccess.length(); i++) {
                                JSONObject jsonObject1 = jsonArraySuccess.optJSONObject(i);

                                String uid = jsonObject1.optString("uid");
                                String fname = jsonObject1.optString("fname");
                                String lname = jsonObject1.optString("lname");
                                String profile_pic = jsonObject1.optString("profile_pic");
                                String present = jsonObject1.optString("present");

                                GetAttendenceDailyListModel getAttendenceDailyListModel = new GetAttendenceDailyListModel();
                                getAttendenceDailyListModel.setUid(uid);
                                getAttendenceDailyListModel.setFname(fname);
                                getAttendenceDailyListModel.setLname(lname);
                                getAttendenceDailyListModel.setProfile_pic(profile_pic);
                                getAttendenceDailyListModel.setPresent(present);

                                getAttendenceDailyListModelArrayList.add(getAttendenceDailyListModel);

                            }

                            if (Utils.userSelectedLang(context)) {
                                DailyAttendanceAdapter dailyAttendance = new DailyAttendanceAdapter(DailyAttendance.this, true, getAttendenceDailyListModelArrayList);
                                rv_daily_attend.setAdapter(dailyAttendance);
                            } else {
                                DailyAttendanceAdapter dailyAttendance = new DailyAttendanceAdapter(DailyAttendance.this, false, getAttendenceDailyListModelArrayList);
                                rv_daily_attend.setAdapter(dailyAttendance);
                            }

                        } else {
                            getAttendenceDailyListModelArrayList.clear();
                        }


                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("action", "get"));
        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("class_id", sharedPreferencesClass.getSelected_Class_Id()));

        params.add(new BasicNameValuePair("type", sharedPreferencesClass.getUser_Type()));
        params.add(new BasicNameValuePair("date", currentdate));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("GetDailyAttendenceWebservice WebService Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, DailyAttendance.this);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_attendance_daily);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }
}
