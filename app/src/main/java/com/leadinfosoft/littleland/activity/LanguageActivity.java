package com.leadinfosoft.littleland.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.MyKidsListModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.adapter.MyKidsListAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageActivity extends AppCompatActivity {

    Context context;

    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        commonFunction();
        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase("") || sharedPreferencesClass.getSelected_Language().length() == 0) {
            sharedPreferencesClass.setUserSelectLang(true);
            sharedPreferencesClass.setSelected_Language(Common.Selected_Language_EN);

            startActivity(new Intent(LanguageActivity.this, FirstActivity.class));
            finish();
            this.overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        } else {
            if (sharedPreferencesClass.getIs_Login().equalsIgnoreCase("true")) {
                startActivity(new Intent(LanguageActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(LanguageActivity.this, FirstActivity.class));
                finish();
            }
        }
        ButterKnife.bind(this);

        GetWordsWebService();


    }

    private void commonFunction() {
        context = LanguageActivity.this;
        con = new ConnectionDetector(context);
        sharedPreferencesClass = new SharedPreferencesClass(context);
        customDialogClass = new CustomDialogClass(LanguageActivity.this);
    }

    @OnClick(R.id.tv_eng)
    public void onClickEng() {
        sharedPreferencesClass.setUserSelectLang(true);
        startActivity(new Intent(LanguageActivity.this, FirstActivity.class));
        finish();
        this.overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        sharedPreferencesClass.setSelected_Language(Common.Selected_Language_EN);
    }

    @OnClick(R.id.tv_arb)
    public void onClickArabic() {
        sharedPreferencesClass.setUserSelectLang(false);
        startActivity(new Intent(LanguageActivity.this, FirstActivity.class));
        finish();
        this.overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        sharedPreferencesClass.setSelected_Language(Common.Selected_Language_AR);


    }

    public void GetWordsWebService() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetWordsWebService Webservice Response" + result + "");

                    Common.data(result);

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
            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_words);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }

    }

}
