package com.leadinfosoft.littleland.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by radhe on 8/29/2017.
 */
public class MyPlayerWebViewActivity extends AppCompatActivity implements EasyVideoCallback {

    private static String TEST_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

//    private EasyVideoPlayer player;

    RelativeLayout rl_withlogo, rl_withtitle;
    ImageView iv_back, iv_bell1;
    TextView tv_title, tv_subtitle;

    Context context;

    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    AVLoadingIndicatorView loader;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_video_play);

        // Grabs a reference to the player view

        commonFunction();

        initHeader();

        setTypeface_Text();

        TEST_URL = getIntent().getStringExtra("video_url");

//        player = (EasyVideoPlayer) findViewById(R.id.player);
        loader = (AVLoadingIndicatorView) findViewById(R.id.loader);
        loader.setVisibility(View.GONE);

        webView = (WebView) findViewById(R.id.webView);

        String full_url = Common.URL_video_player + TEST_URL;

        Logger.e("07/03 full_url ----> " + full_url + "");

        // Sets the callback to this Activity, since it inherits EasyVideoCallback
//        player.setCallback(this);

        // Sets the source to the HTTP URL held in the TEST_URL variable.
        // To play files, you can use Uri.fromFile(new File("..."))

        if (con.isConnectingToInternet()) {
//            player.setSource(Uri.parse(TEST_URL));

            /*webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(full_url);
            webView.setHorizontalScrollBarEnabled(false);*/

            startWebView(full_url);

        } else {
            Toast.makeText(MyPlayerWebViewActivity.this, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }


        // From here, the player view will show a progress indicator until the player is prepared.
        // Once it's prepared, the progress indicator goes away and the controls become enabled for the user to begin playback.
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

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {


            tv_title.setTypeface(tf);

            tv_title.setText(Common.filter("lbl_video", "ar"));

        } else {
            tv_title.setTypeface(tf);

            tv_title.setText(Common.filter("lbl_video", "en"));

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
        tv_title.setText("Video");

        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        tv_subtitle.setVisibility(View.GONE);
//        tv_subtitle.setText(sharedPreferencesClass.getSelected_Child_Name());


    }

    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {
//            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                /*if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(ShowWebView.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }*/

                loader.setVisibility(View.VISIBLE);

            }

            public void onPageFinished(WebView view, String url) {
                try {
                   /* if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }*/

                    if (loader.isShown()) {
                        loader.setVisibility(View.GONE);
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);

        // Other webview options
        /*
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        */

        /*
         String summary = "<html><body>You scored <b>192</b> points.</body></html>";
         webview.loadData(summary, "text/html", null);
         */

        //Load url in webview
        webView.loadUrl(url);


    }


    @Override
    public void onPause() {
        super.onPause();
        // Make sure the player stops playing if the user presses the home button.
//        player.pause();
    }

    // Methods for the implemented EasyVideoCallback

    @Override
    public void onPreparing(EasyVideoPlayer player) {
        // TODO handle if needed

        Logger.e("27/01 onPreparing");

        loader.setVisibility(View.VISIBLE);


    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {
        // TODO handle

        Logger.e("27/01 onPrepared");

        if (loader.isShown()) {
            loader.setVisibility(View.GONE);

        }

        player.setAutoPlay(true);


    }

    @Override
    public void onBuffering(int percent) {
        // TODO handle if needed

        Logger.e("27/01 onBuffering ");
        Logger.e("27/01 onBuffering percent ---->" + percent + "");


    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        // TODO handle

        Logger.e("27/01 onError");


    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {
        // TODO handle if needed

        Logger.e("27/01 onCompletion");


    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {
        // TODO handle if used

        Logger.e("27/01 onRetry");


    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {
        // TODO handle if used

        Logger.e("27/01 onSubmit");

    }

    @Override
    public void onStarted(EasyVideoPlayer player) {
        // TODO handle if needed

        Logger.e("27/01 onStarted");


    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
        // TODO handle if needed

        Logger.e("27/01 onPaused");


    }
}