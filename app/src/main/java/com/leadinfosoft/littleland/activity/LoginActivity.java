package com.leadinfosoft.littleland.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.widget.MyEditText;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_back;
    MyEditText et_techid, et_pass;
    MyHeaderTextView tv_signin;
    MyHeaderTextView tv_forgot, tv_admin;

    Response_string<String> resp;
    RequestMaker req;

    Context context;
    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;


    String str_et_techid = "";
    String str_et_pass = "";

    String Str_Login_Type = "teacher";

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    ImageView iv_baby_image;
    ImageLoader imageLoader;
    DisplayImageOptions options;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonFunction();


        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            setContentView(R.layout.activity_login_new_ar);

        } else {
            setContentView(R.layout.activity_login_new);

        }

        init();

        setTypeface_Text();


    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            et_techid.setTypeface(tf);
            et_pass.setTypeface(tf);
            tv_signin.setTypeface(tf);
            tv_forgot.setTypeface(tf);
            tv_admin.setTypeface(tf);

            et_techid.setHint(Common.filter("lbl_teacher_id", "ar").toUpperCase());
            et_pass.setHint(Common.filter("lbl_password", "ar").toUpperCase());


            tv_signin.setText(Common.filter("lbl_signin", "ar"));
            tv_forgot.setText(Common.filter("lbl_forgot", "ar"));

            tv_admin.setText(Common.filter("lbl_contact_admin", "ar"));

        } else {
            et_techid.setTypeface(tf);
            et_pass.setTypeface(tf);
            tv_signin.setTypeface(tf);
            tv_forgot.setTypeface(tf);
            tv_admin.setTypeface(tf);

            et_techid.setHint(Common.filter("lbl_teacher_id", "en").toUpperCase());
            et_pass.setHint(Common.filter("lbl_password", "en").toUpperCase());

            tv_signin.setText(Common.filter("lbl_signin", "en"));
            tv_forgot.setText(Common.filter("lbl_forgot", "en"));

            tv_admin.setText(Common.filter("lbl_contact_admin", "en"));
        }

        et_techid.setTypeface(tf_numeric);
        et_pass.setTypeface(tf_numeric);

    }

    private void CommonFunction() {
        context = getApplicationContext();
        con = new ConnectionDetector(context);
        sharedPreferencesClass = new SharedPreferencesClass(context);
        customDialogClass = new CustomDialogClass(LoginActivity.this);

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

        fontPath_numeric = "fonts/Lato-Bold.ttf";
        tf_numeric = Typeface.createFromAsset(getAssets(), fontPath_numeric);


    }

    private void init() {

        iv_back = (ImageView) findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_techid = (MyEditText) findViewById(R.id.et_techid);
        et_pass = (MyEditText) findViewById(R.id.et_pass);

        tv_signin = (MyHeaderTextView) findViewById(R.id.tv_signin);
        tv_signin.setOnClickListener(this);

        tv_forgot = (MyHeaderTextView) findViewById(R.id.tv_forgot);
        tv_admin = (MyHeaderTextView) findViewById(R.id.tv_admin);

        iv_baby_image = (ImageView) findViewById(R.id.iv_baby_image);

        imageLoader.displayImage(sharedPreferencesClass.getLogin_Slider_image_url(), iv_baby_image, options);



    }

   /* @OnClick(R.id.tv_signin)
    public void onClickSignin() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }*/

    @Override
    public void onClick(View v) {
        if (v == tv_signin) {


            LoginWebservice();

          /*  startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();*/

        }

    }

    private void LoginWebservice() {
        if (isValid()) {

            resp = new Response_string<String>() {
                @Override
                public void OnRead_response(String result) {

                    try {
                        Logger.e("Login Webservice Teacher  Response" + result + "");

                        JSONObject jsonObject = new JSONObject(result);

                        String error = jsonObject.optString("error");

                        if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

                            sharedPreferencesClass.setUSER_DETAILS(jsonObject.optString("success") + "");

                            JSONObject jsonObjectUser = jsonObject.optJSONObject("success");

                            sharedPreferencesClass.setUSER_Id(jsonObjectUser.optString("uid"));
                            sharedPreferencesClass.setUser_Type(jsonObjectUser.optString("type"));
                            sharedPreferencesClass.setIs_Login("true");

                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();

                          /*  startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();*/

                        } else {
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            sharedPreferencesClass.setIs_Login("false");


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            ArrayList<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("type", Str_Login_Type));
            params.add(new BasicNameValuePair("username", str_et_techid));
            params.add(new BasicNameValuePair("password", str_et_pass));
            params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));

            Logger.e("Login Webservice Teacher Params =====> " + params + "");

            if (con.isConnectingToInternet()) {
                req = new RequestMaker(resp, params, LoginActivity.this);
                req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_Login_User);
            } else {
                Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isValid() {
        Boolean is_valid = true;

        str_et_techid = et_techid.getText().toString();
        str_et_pass = et_pass.getText().toString();

        if (str_et_techid.equalsIgnoreCase("") || str_et_techid.length() == 0) {
            is_valid = false;

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                et_techid.setError(Common.filter("error_Please_Enter_Teacher_Id", "ar"));

            } else {
                et_techid.setError(Common.filter("error_Please_Enter_Teacher_Id", "en"));


            }


        } else if (str_et_pass.equalsIgnoreCase("") || str_et_pass.length() == 0) {
            is_valid = false;

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                et_pass.setError(Common.filter("error_Please_Enter_Password", "ar"));

            } else {
                et_pass.setError(Common.filter("error_Please_Enter_Password", "en"));


            }

        }


        return is_valid;
    }
}
