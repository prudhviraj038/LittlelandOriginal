package com.leadinfosoft.littleland.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.RequestMakerBg;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.MyKidsListModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.adapter.FirstActivitySliderAdapter;
import com.leadinfosoft.littleland.adapter.MyKidsListAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstActivity extends AppCompatActivity {

    @BindView(R.id.iv_teacher)
    ImageView iv_teacher;
    @BindView(R.id.iv_parent)
    ImageView iv_parent;

    MyHeaderTextView tv_techer, tv_techtxt;
    MyHeaderTextView tv_parent, tv_parenttxt;

    Context context;
    SharedPreferencesClass sharedPreferencesClass;

    Response_string<String> resp;
    RequestMakerBg req;

    RequestMaker requestMaker;

    ConnectionDetector con;
    CustomDialogClass customDialogClass;

    ImageView iv_slider_image;
    ViewPager mPager;
    private static int currentPage = 0;

    FirstActivitySliderAdapter firstActivitySliderAdapter = null;

    ArrayList<String> arrayList_Slider = new ArrayList<>();

    String fontPath = "";
    Typeface tf;

    String str_slider_image_url = "";

    ImageLoader imageLoader;
    DisplayImageOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();


        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            setContentView(R.layout.activity_first_ar);

        } else {
            setContentView(R.layout.activity_first);

        }


        iv_slider_image = (ImageView) findViewById(R.id.iv_slider_image);


        tv_techer = (MyHeaderTextView) findViewById(R.id.tv_techer);
        tv_techtxt = (MyHeaderTextView) findViewById(R.id.tv_techtxt);
        tv_parent = (MyHeaderTextView) findViewById(R.id.tv_parent);
        tv_parenttxt = (MyHeaderTextView) findViewById(R.id.tv_parenttxt);


        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setVisibility(View.GONE);

        ButterKnife.bind(this);

        setPermission();

        if (sharedPreferencesClass.getLanguage_Response().equalsIgnoreCase("") || sharedPreferencesClass.getLanguage_Response().length() == 0) {

        } else {
            Common.data(sharedPreferencesClass.getLanguage_Response());
        }

        setTypeface_Text();

        GetWordsWebService();

        GetMyslidersImagesListWebService();

    }


    public void GetWordsWebService() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetWordsWebService Webservice Response" + result + "");

                    Common.data(result);

                    setTypeface_Text();

                    sharedPreferencesClass.setLanguage_Response(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();


       /* action	REQUIRED	get_kids
        uid	REQUIRED	User ID - Logged-in parent's UID*/

        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));

        Logger.e("GetWordsWebService Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            requestMaker = new RequestMaker(resp, params, context);
            requestMaker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_words);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }

    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            tv_techer.setTypeface(tf);
            tv_techtxt.setTypeface(tf);
            tv_parent.setTypeface(tf);
            tv_parenttxt.setTypeface(tf);

            tv_techer.setText(Common.filter("lbl_teachers", "ar"));
            tv_techtxt.setText(Common.filter("lbl_loginhere", "ar"));

            tv_parent.setText(Common.filter("lbl_parents", "ar"));
            tv_parenttxt.setText(Common.filter("lbl_loginhere", "ar"));

        } else {
            tv_techer.setTypeface(tf);
            tv_techtxt.setTypeface(tf);
            tv_parent.setTypeface(tf);
            tv_parenttxt.setTypeface(tf);

            tv_techer.setText(Common.filter("lbl_teachers", "en"));
            tv_techtxt.setText(Common.filter("lbl_loginhere", "en"));

            tv_parent.setText(Common.filter("lbl_parents", "en"));
            tv_parenttxt.setText(Common.filter("lbl_loginhere", "en"));
        }

    }

    private void setPermission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (!Common.hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions(FirstActivity.this, PERMISSIONS, PERMISSION_ALL);
        } else {

        }
    }

    public void GetMyslidersImagesListWebService() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetMyslidersImagesListWebService Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();


                        JSONArray jsonArraysuccess = jsonObject.optJSONArray("success");

                        if (jsonArraysuccess.length() > 0) {

                            arrayList_Slider.clear();
                            mPager.setVisibility(View.GONE);
                            iv_slider_image.setVisibility(View.VISIBLE);

                            for (int i = 0; i < jsonArraysuccess.length(); i++) {
                                JSONObject jsonObject1 = jsonArraysuccess.optJSONObject(i);

                                String photo = jsonObject1.optString("photo");
                                String type = jsonObject1.optString("type");

                                if (type.equalsIgnoreCase("home")) {
                                    str_slider_image_url = photo;
                                    sharedPreferencesClass.setFirst_Page_Slider_image_url(str_slider_image_url);
                                } else if (type.equalsIgnoreCase("login")) {
                                    sharedPreferencesClass.setLogin_Slider_image_url(photo);

                                }

                                arrayList_Slider.add(photo);

                            }


                        } else {
                            arrayList_Slider.clear();
                            mPager.setVisibility(View.GONE);
                            iv_slider_image.setVisibility(View.VISIBLE);


                        }

                        imageLoader.displayImage(str_slider_image_url, iv_slider_image, options);


                        firstActivitySliderAdapter = new FirstActivitySliderAdapter(context, arrayList_Slider);
                        mPager.setAdapter(firstActivitySliderAdapter);

                        // Auto start of viewpager
                        final Handler handler = new Handler();
                        final Runnable Update = new Runnable() {
                            public void run() {
                                if (currentPage == arrayList_Slider.size()) {
                                    currentPage = 0;
                                }
                                mPager.setCurrentItem(currentPage++, true);
                            }
                        };
                        Timer swipeTimer = new Timer();
                        swipeTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(Update);
                            }
                        }, 2500, 2500);

                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();


       /* action	REQUIRED	get_kids
        uid	REQUIRED	User ID - Logged-in parent's UID*/

        params.add(new BasicNameValuePair("action", "sliders"));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("GetMyslidersImagesListWebService Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMakerBg(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_get_data);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }

    }

    private void init() {
        context = FirstActivity.this;
        sharedPreferencesClass = new SharedPreferencesClass(context);
        con = new ConnectionDetector(context);
//        sharedPreferencesClass.setUserSelectLang(true);

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.baby)
                .showImageOnFail(R.drawable.baby)
                .showImageOnLoading(R.drawable.loading).build();

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            fontPath = "fonts/GE_Flow_Regular.otf";
            tf = Typeface.createFromAsset(getAssets(), fontPath);
        } else {
            fontPath = "fonts/Lato-Bold.ttf";
            tf = Typeface.createFromAsset(getAssets(), fontPath);
        }


    }

    @OnClick(R.id.iv_teacher)
    public void onClickTecher() {
        startActivity(new Intent(FirstActivity.this, LoginActivity.class));
    }

    @OnClick(R.id.iv_parent)
    public void onClickParent() {
//        startActivity(new Intent(FirstActivity.this,MainActivity.class));
//        finish();

        startActivity(new Intent(FirstActivity.this, ParentsLoginActivity.class));

    }


}
