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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by radhe on 8/25/2017.
 */
public class ContactUsActivity extends Activity implements View.OnClickListener {


    RelativeLayout rl_withlogo, rl_withtitle;
    ImageView iv_back, iv_bell1;
    TextView tv_title, tv_subtitle;


    EditText et_subject, et_desc;
    TextView btn_submit;

    Context context;

    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    String str_et_subject = "";
    String str_et_desc = "";

    String fontPath = "";
    Typeface tf;

    JSONObject jsonObjectUserDetails = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        commonFunction();

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            setContentView(R.layout.activity_contact_us_ar);

        } else {
            setContentView(R.layout.activity_contact_us);

        }

        try {
            jsonObjectUserDetails = new JSONObject(sharedPreferencesClass.getUSER_DETAILS());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initHeader();

        init();

        setTypeface_Text();


    }

    private void init() {

        et_subject = (EditText) findViewById(R.id.et_subject);
        et_desc = (EditText) findViewById(R.id.et_desc);

        btn_submit = (TextView) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            et_subject.setTypeface(tf);
            et_desc.setTypeface(tf);
            btn_submit.setTypeface(tf);
            tv_title.setTypeface(tf);

            et_subject.setHint(Common.filter("lbl_subject", "ar").toUpperCase());
            et_desc.setHint(Common.filter("lbl_description", "ar").toUpperCase());
            btn_submit.setText(Common.filter("lbl_send", "ar"));

            tv_title.setText(Common.filter("ibl_CONTACT_US", "ar"));


        } else {
            et_subject.setTypeface(tf);
            et_desc.setTypeface(tf);
            btn_submit.setTypeface(tf);
            tv_title.setTypeface(tf);


            et_subject.setHint(Common.filter("lbl_subject", "en").toUpperCase());
            et_desc.setHint(Common.filter("lbl_description", "en").toUpperCase());
            btn_submit.setText(Common.filter("lbl_send", "en"));

            tv_title.setText(Common.filter("ibl_CONTACT_US", "en"));


        }

    }

    @Override
    public void onClick(View v) {


        str_et_subject = et_subject.getText().toString();
        str_et_desc = et_desc.getText().toString();

        if (v == btn_submit) {

            if (isValid()) {
                ContactUsWebservice();
            }
        }

    }

    private boolean isValid() {

        Boolean is_valid = true;

        if (str_et_subject.equalsIgnoreCase("") || str_et_subject.length() == 0) {

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                et_subject.setError(Common.filter("error_Please_Enter_Subject", "ar"));

            } else {
                et_subject.setError(Common.filter("error_Please_Enter_Subject", "en"));


            }

            is_valid = false;
        } else if (str_et_desc.equalsIgnoreCase("") || str_et_desc.length() == 0) {

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                et_desc.setError(Common.filter("error_Please_Enter_Description", "ar"));

            } else {
                et_desc.setError(Common.filter("error_Please_Enter_Description", "en"));


            }

            is_valid = false;
        }

        return is_valid;
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
        tv_title.setText("CONTACT US");

        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        tv_subtitle.setVisibility(View.GONE);
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

    }

    private void ContactUsWebservice() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("ContactUsWebservice Webservice Response" + result + "");

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
     uid	REQUIRED	User ID
subject	REQUIRED
body	REQUIRED	*/

        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));

        if (jsonObjectUserDetails != null) {
            params.add(new BasicNameValuePair("name", jsonObjectUserDetails.optString("fname") + " " + jsonObjectUserDetails.optString("lname")));
            params.add(new BasicNameValuePair("email", jsonObjectUserDetails.optString("email")));
            params.add(new BasicNameValuePair("phone", jsonObjectUserDetails.optString("mobile")));

        } else {

        }

        params.add(new BasicNameValuePair("subject", str_et_subject));
        params.add(new BasicNameValuePair("message", str_et_desc));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));

        Logger.e("ContactUsWebservice Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, ContactUsActivity.this);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_contact_us);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }

}
