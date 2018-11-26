package com.leadinfosoft.littleland.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.ViewPostListModelClass;
import com.leadinfosoft.littleland.ModelClass.ViewPostMediaArrayModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.adapter.ViewPostAdapter;
import com.leadinfosoft.littleland.adapter.ViewPostImagePagerAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.utill.Utils;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.leadinfosoft.littleland.widget.MyTextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewPostDetail extends AppCompatActivity implements View.OnClickListener {

    ViewPager vp_pager;
    ImageView iv_back;

    MyHeaderTextView tv_title;

    MyTextView tv_subtitle;

    ImageView iv_left, iv_right;


    boolean isEnglish;

    Context context;

    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    String post_id = "";

    TextView tv_time, tv_date;
    TextView tv_title_main, tv_body_main;

    TextView tv_parent_opinion, tv_teacher_viewall_opinion;

    ArrayList<ViewPostMediaArrayModelClass> viewPostMediaArrayModelClassArrayList = new ArrayList<>();

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        commonFunction();

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            setContentView(R.layout.activity_view_post_detail_ar);
            isEnglish = false;


        } else {
            setContentView(R.layout.activity_view_post_detail);
            isEnglish = true;


        }

        init();

        initHeader();

        setTypeface_Text();


        post_id = getIntent().getExtras().getString("post_id");

        GetViewPostDetailsWebservice();

        /*vp_pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });*/
    }

    private void init() {

        vp_pager = (ViewPager) findViewById(R.id.vp_pager);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);

        tv_title = (MyHeaderTextView) findViewById(R.id.tv_title);

        tv_subtitle = (MyTextView) findViewById(R.id.tv_subtitle);
        tv_subtitle.setTextColor(getResources().getColor(R.color.techer_parpal));


        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

        } else {
            tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


        }

//        tv_subtitle.setText("Class - " + sharedPreferencesClass.getSelected_Class_Name());

        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_title_main = (TextView) findViewById(R.id.tv_title_main);
        tv_body_main = (TextView) findViewById(R.id.tv_body_main);

        tv_parent_opinion = (TextView) findViewById(R.id.tv_parent_opinion);
        tv_parent_opinion.setVisibility(View.GONE);
        tv_parent_opinion.setOnClickListener(this);

        tv_teacher_viewall_opinion = (TextView) findViewById(R.id.tv_teacher_viewall_opinion);
        tv_teacher_viewall_opinion.setVisibility(View.GONE);
        tv_teacher_viewall_opinion.setOnClickListener(this);


        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = vp_pager.getCurrentItem();
                if (index != 0) {
                    vp_pager.setCurrentItem(index - 1);
                }
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = vp_pager.getCurrentItem();
                if (index != 5) {
                    vp_pager.setCurrentItem(index + 1);
                }

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                context.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
            }
        });

    }

    public void SplitDateandTime(String stamp) {
        String dateToParse = stamp;

        DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = f.parse(dateToParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat time = new SimpleDateFormat("HH:mm:ss");
        System.out.println("Date: " + date.format(d));
        System.out.println("Time: " + time.format(d));

        tv_time.setText(time.format(d));
        tv_date.setText(date.format(d));

    }


    private void commonFunction() {
        context = ViewPostDetail.this;
        con = new ConnectionDetector(context);
        sharedPreferencesClass = new SharedPreferencesClass(context);

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

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            tv_title.setTypeface(tf);
            tv_subtitle.setTypeface(tf);
            tv_title_main.setTypeface(tf);
            tv_body_main.setTypeface(tf);
            tv_time.setTypeface(tf);
            tv_date.setTypeface(tf);

            tv_teacher_viewall_opinion.setTypeface(tf);
            tv_parent_opinion.setTypeface(tf);

            tv_teacher_viewall_opinion.setText(Common.filter("lbl_viewall_opinion", "ar"));
            tv_parent_opinion.setText(Common.filter("lbl_my_opinion", "ar"));

        } else {
            tv_title.setTypeface(tf);
            tv_subtitle.setTypeface(tf);
            tv_title_main.setTypeface(tf);
            tv_body_main.setTypeface(tf);
            tv_time.setTypeface(tf);
            tv_date.setTypeface(tf);

            tv_teacher_viewall_opinion.setTypeface(tf);
            tv_parent_opinion.setTypeface(tf);

            tv_teacher_viewall_opinion.setText(Common.filter("lbl_viewall_opinion", "en"));
            tv_parent_opinion.setText(Common.filter("lbl_my_opinion", "en"));
        }

        tv_date.setTypeface(tf_numeric);
        tv_time.setTypeface(tf_numeric);

    }


    private void initHeader() {

        if (isEnglish) {
            tv_title.setText("");
            tv_subtitle.setText("");
        } else {
            tv_title.setText("");
            tv_subtitle.setText("");
        }
    }

    private void GetViewPostDetailsWebservice() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetViewPostDetailsWebservice Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

                        JSONObject jsonObjectsuccess = jsonObject.optJSONObject("success");

                        tv_title.setText(jsonObjectsuccess.optString("title"));

                        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                            tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

                        } else {
                            tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


                        }

//                        tv_subtitle.setText("Class - " + sharedPreferencesClass.getSelected_Class_Name());

                        tv_title_main.setText(jsonObjectsuccess.optString("title"));
                        tv_body_main.setText(jsonObjectsuccess.optString("body"));

                        SplitDateandTime(jsonObjectsuccess.optString("stamp"));

                        String str_is_opinion = jsonObjectsuccess.optString("is_opinion");

                        if (str_is_opinion.equalsIgnoreCase("1")) {
                            if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase(Common.USER_TYPE_TEACHER)) {
                                tv_teacher_viewall_opinion.setVisibility(View.VISIBLE);
                                tv_parent_opinion.setVisibility(View.GONE);

                            } else {
                                tv_teacher_viewall_opinion.setVisibility(View.GONE);
                                tv_parent_opinion.setVisibility(View.VISIBLE);
                            }
                        } else {
                            tv_teacher_viewall_opinion.setVisibility(View.GONE);
                            tv_parent_opinion.setVisibility(View.GONE);
                        }


                        JSONArray jsonArraymedia = jsonObjectsuccess.optJSONArray("media");

                        if (jsonArraymedia != null) {

                            if (jsonArraymedia.length() > 0) {

                                viewPostMediaArrayModelClassArrayList.clear();

                                for (int i = 0; i < jsonArraymedia.length(); i++) {

                                    JSONObject jsonObject1 = jsonArraymedia.optJSONObject(i);

                                   /* "media_type" : image,
                                            "path" : http:\/\/server.mywebdemo.in\/nurcery\/scripts\/images\/uploads\/default\/post.png
*/

                                    String media_type = jsonObject1.optString("media_type");
                                    String path = jsonObject1.optString("path");
                                    String thumb = jsonObject1.optString("thumb");

                                    ViewPostMediaArrayModelClass viewPostMediaArrayModelClass = new ViewPostMediaArrayModelClass();
                                    viewPostMediaArrayModelClass.setMedia_type(media_type);
                                    viewPostMediaArrayModelClass.setPath(path);
                                    viewPostMediaArrayModelClass.setThumb(thumb);


                                    viewPostMediaArrayModelClassArrayList.add(viewPostMediaArrayModelClass);


                                }


                            } else {
                                //length 0

                                viewPostMediaArrayModelClassArrayList.clear();
                            }


                            ViewPostImagePagerAdapter
                                    viewPostImagePagerAdapter = new ViewPostImagePagerAdapter(context, viewPostMediaArrayModelClassArrayList);
                            vp_pager.setAdapter(viewPostImagePagerAdapter);


                            if (viewPostMediaArrayModelClassArrayList.size() > 1) {
                                iv_left.setVisibility(View.VISIBLE);
                                iv_right.setVisibility(View.VISIBLE);

                            } else {
                                iv_left.setVisibility(View.GONE);
                                iv_right.setVisibility(View.GONE);


                            }

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

        params.add(new BasicNameValuePair("action", "get_post_details"));
        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("post_id", post_id));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("GetViewPostDetailsWebservice Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_New_Post_Or_Direct_Message);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    @Override
    public void onClick(View v) {
        if (v == tv_parent_opinion) {

            Intent i = new Intent(context, ParentsOpinionActivity.class);
            i.putExtra("post_id", post_id);
            startActivity(i);

        } else if (v == tv_teacher_viewall_opinion) {
            Intent i = new Intent(context, TeachersViewOpinionActivity.class);
            i.putExtra("post_id", post_id);
            startActivity(i);
        }
    }
}
