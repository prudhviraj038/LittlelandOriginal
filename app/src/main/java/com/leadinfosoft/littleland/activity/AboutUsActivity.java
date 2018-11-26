package com.leadinfosoft.littleland.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;

/**
 * Created by radhe on 8/25/2017.
 */
public class AboutUsActivity extends Activity {


    RelativeLayout rl_withlogo, rl_withtitle;
    ImageView iv_back, iv_bell1;
    TextView tv_title, tv_subtitle;

    Context context;

    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    WebView webView1;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        commonFunction();


        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            setContentView(R.layout.activity_about_us_ar);

        } else {
            setContentView(R.layout.activity_about_us);

        }


        initHeader();
        setTypeface_Text();


        webView1 = (WebView) findViewById(R.id.webView1);

        if (con.isConnectingToInternet()) {

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                webView1.loadUrl(Common.Url_Static_about_us_ar);

            } else {
                webView1.loadUrl(Common.Url_Static_about_us);

            }

        } else {
            Toast.makeText(AboutUsActivity.this, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }



    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            tv_title.setTypeface(tf);

            tv_title.setText(Common.filter("lbl_about_us", "ar"));

        } else {
            tv_title.setTypeface(tf);

            tv_title.setText(Common.filter("lbl_about_us", "en"));
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

        tv_title.setText("ABOUT US");

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
            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        } else {
            fontPath = "fonts/Lato-Bold.ttf";
            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
        }

        fontPath_numeric = "fonts/Lato-Bold.ttf";
        tf_numeric = Typeface.createFromAsset(context.getAssets(), fontPath_numeric);


    }
}
