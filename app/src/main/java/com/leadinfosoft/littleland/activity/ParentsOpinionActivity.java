package com.leadinfosoft.littleland.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.ViewPostMediaArrayModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.adapter.ViewPostImagePagerAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by radhe on 8/25/2017.
 */
public class ParentsOpinionActivity extends Activity implements View.OnClickListener {


    RelativeLayout rl_withlogo, rl_withtitle;
    ImageView iv_back, iv_bell1;
    TextView tv_title, tv_subtitle;

    LinearLayout ll_disagree, ll_agree;

    Context context;

    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    TextView tv_agree, tv_disagree;
    TextView tv_lldis_agree, tv_lldis_disagree;
    EditText et_remark;
    TextView tv_submit;

    String str_post_id = "";

    String str_is_opinion = "1";

    JSONArray jsonArrayagree = null;
    JSONArray jsonArraydisagree = null;
    JSONArray jsonArraypending = null;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        commonFunction();

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            setContentView(R.layout.activity_parent_opinion_ar);

        } else {
            setContentView(R.layout.activity_parent_opinion);

        }

        initHeader();

        init();

        setTypeface_Text();

        agree();

        str_post_id = getIntent().getStringExtra("post_id");

        GetOpinionWebservice();

    }

    private void init() {

        ll_disagree = (LinearLayout) findViewById(R.id.ll_disagree);
        ll_agree = (LinearLayout) findViewById(R.id.ll_agree);

        tv_agree = (TextView) findViewById(R.id.tv_agree);
        tv_agree.setOnClickListener(this);

        tv_disagree = (TextView) findViewById(R.id.tv_disagree);
        tv_disagree.setOnClickListener(this);

        tv_lldis_agree = (TextView) findViewById(R.id.tv_lldis_agree);
        tv_lldis_agree.setOnClickListener(this);


        tv_lldis_disagree = (TextView) findViewById(R.id.tv_lldis_disagree);
        tv_lldis_disagree.setOnClickListener(this);

        et_remark = (EditText) findViewById(R.id.et_remark);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);


    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            tv_agree.setTypeface(tf);
            tv_disagree.setTypeface(tf);
            et_remark.setTypeface(tf);
            tv_submit.setTypeface(tf);

            tv_lldis_agree.setTypeface(tf);
            tv_lldis_disagree.setTypeface(tf);


            tv_title.setTypeface(tf);
            tv_subtitle.setTypeface(tf);

            tv_agree.setText(Common.filter("lbl_agree", "ar"));
            tv_disagree.setText(Common.filter("lbl_disagree", "ar"));

            tv_lldis_agree.setText(Common.filter("lbl_agree", "ar"));
            tv_lldis_disagree.setText(Common.filter("lbl_disagree", "ar"));

            tv_submit.setText(Common.filter("lbl_submit", "ar"));

            tv_title.setText(Common.filter("lbl_my_opinion", "ar"));


            et_remark.setHint(Common.filter("lbl_hint_remark", "ar").toUpperCase());


        } else {
            tv_agree.setTypeface(tf);
            tv_disagree.setTypeface(tf);
            et_remark.setTypeface(tf);
            tv_submit.setTypeface(tf);

            tv_lldis_agree.setTypeface(tf);
            tv_lldis_disagree.setTypeface(tf);


            tv_title.setTypeface(tf);
            tv_subtitle.setTypeface(tf);

            tv_agree.setText(Common.filter("lbl_agree", "en"));
            tv_disagree.setText(Common.filter("lbl_disagree", "en"));

            tv_lldis_agree.setText(Common.filter("lbl_agree", "en"));
            tv_lldis_disagree.setText(Common.filter("lbl_disagree", "en"));

            tv_submit.setText(Common.filter("lbl_submit", "en"));

            tv_title.setText(Common.filter("lbl_my_opinion", "en"));


            et_remark.setHint(Common.filter("lbl_remark", "en").toUpperCase());
        }

    }

    private void initHeader() {


        rl_withlogo = (RelativeLayout) findViewById(R.id.rl_withlogo);
        rl_withlogo.setVisibility(View.GONE);

        rl_withtitle = (RelativeLayout) findViewById(R.id.rl_withtitle);
        rl_withtitle.setVisibility(View.VISIBLE);


        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_bell1 = (ImageView) findViewById(R.id.iv_bell1);
        iv_bell1.setVisibility(View.INVISIBLE);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("MY OPINION");

        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        tv_subtitle.setTextColor(getResources().getColor(R.color.techer_parpal));

        tv_subtitle.setVisibility(View.VISIBLE);

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

        } else {
            tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


        }

//        tv_subtitle.setText("Class - " + sharedPreferencesClass.getSelected_Class_Name());

//        tv_subtitle.setText(sharedPreferencesClass.getSelected_Child_Name());

    }

    private void commonFunction() {
        context = getApplicationContext();
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

    @Override
    public void onClick(View v) {
        if (v == tv_agree) {

            disagree();

        } else if (v == tv_disagree) {

            disagree();


        } else if (v == tv_lldis_agree) {

            agree();


        } else if (v == tv_lldis_disagree) {

            agree();


        } else if (v == tv_submit) {

            if (et_remark.getText().toString().equalsIgnoreCase("") || et_remark.getText().toString().length() == 0) {


                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    et_remark.setError(Common.filter("error_PLEASE_ENTER_REMARK", "ar"));

                } else {
                    et_remark.setError(Common.filter("error_PLEASE_ENTER_REMARK", "en"));


                }


            } else {
                SendOpinionWebservice();
            }

        }
    }

    public void agree() {

        str_is_opinion = "1";

       /* tv_agree.setBackgroundResource(R.drawable.radius_with_purple_stoke);
        tv_agree.setTextColor(getResources().getColor(R.color.white));
        tv_disagree.setBackgroundColor(getResources().getColor(R.color.white));
        tv_disagree.setTextColor(getResources().getColor(R.color.gray_text));*/


        ll_agree.setVisibility(View.VISIBLE);
        ll_disagree.setVisibility(View.GONE);

    }

    public void disagree() {

        str_is_opinion = "0";

/*
        tv_disagree.setBackgroundResource(R.drawable.radius_with_purple_stoke);
        tv_disagree.setTextColor(getResources().getColor(R.color.white));
        tv_agree.setBackgroundColor(getResources().getColor(R.color.white));
        tv_agree.setTextColor(getResources().getColor(R.color.gray_text));*/

        ll_agree.setVisibility(View.GONE);
        ll_disagree.setVisibility(View.VISIBLE);

    }

    private void SendOpinionWebservice() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("SendOpinionWebservice Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
                        Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

                        finish();


                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();

/*
        action	REQUIRED	send
        post_id	REQUIRED
        uid	REQUIRED	User ID
        opinion	REQUIRED	1 or 0
        remark	REQUIRED*/

        params.add(new BasicNameValuePair("action", "send"));
        params.add(new BasicNameValuePair("post_id", str_post_id));
        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("opinion", str_is_opinion));
        params.add(new BasicNameValuePair("remark", et_remark.getText().toString()));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));



        Logger.e("SendOpinionWebservice Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, ParentsOpinionActivity.this);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_opinion_cms);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }

    private void GetOpinionWebservice() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetOpinionWebservice Parent Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {


                        JSONObject jsonObjectsuccess = jsonObject.optJSONObject("success");

                        str_is_opinion = jsonObjectsuccess.optString("opinion");
                        et_remark.setText(jsonObjectsuccess.optString("remark"));

                        if (str_is_opinion.equalsIgnoreCase("1")) {
                            agree();
                        } else {
                            disagree();
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


    /*    action	REQUIRED	get
        post_id	REQUIRED
        uid	REQUIRED	User ID
        type	REQUIRED	teacher or parent
*/
        params.add(new BasicNameValuePair("action", "get"));
        params.add(new BasicNameValuePair("post_id", str_post_id));
        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("type", sharedPreferencesClass.getUser_Type()));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("GetOpinionWebservice Parent Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, ParentsOpinionActivity.this);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_opinion_cms);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }
}
