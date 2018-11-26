package com.leadinfosoft.littleland.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.ViewOpinionModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.adapter.ViewOpinionListAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by radhe on 8/25/2017.
 */
public class TeachersViewOpinionActivity extends Activity implements View.OnClickListener {


    RelativeLayout rl_withlogo, rl_withtitle;
    ImageView iv_back, iv_bell1;
    TextView tv_title, tv_subtitle;

    Context context;

    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;


    TextView tv_agree, tv_disagree, tv_pending;
    ImageView iv_agree, iv_disagree, iv_pending;

    ListView lv_view_opinion;

    TextView tv_no_data;

    String str_post_id = "";

    String str_is_opinion = "1";

    ArrayList<ViewOpinionModelClass> viewOpinionModelClassArrayList = new ArrayList<>();

    ViewOpinionListAdapter viewOpinionListAdapter = null;

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
            setContentView(R.layout.activity_teacher_view_opinion_ar);

        } else {
            setContentView(R.layout.activity_teacher_view_opinion);

        }

        initHeader();

        init();

        setTypeface_Text();


        str_post_id = getIntent().getStringExtra("post_id");


        GetOpinionWebservice();

    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            tv_agree.setTypeface(tf);
            tv_disagree.setTypeface(tf);
            tv_pending.setTypeface(tf);


            tv_title.setTypeface(tf);
            tv_subtitle.setTypeface(tf);

            tv_agree.setText(Common.filter("lbl_agree", "ar"));
            tv_disagree.setText(Common.filter("lbl_disagree", "ar"));
            tv_pending.setText(Common.filter("lbl_pending", "ar"));

            tv_title.setText(Common.filter("lbl_view_opinion", "ar"));


        } else {
            tv_agree.setTypeface(tf);
            tv_disagree.setTypeface(tf);
            tv_pending.setTypeface(tf);


            tv_title.setTypeface(tf);
            tv_subtitle.setTypeface(tf);

            tv_agree.setText(Common.filter("lbl_agree", "en"));
            tv_disagree.setText(Common.filter("lbl_disagree", "en"));
            tv_pending.setText(Common.filter("lbl_pending", "en"));

            tv_title.setText(Common.filter("lbl_view_opinion", "en"));

        }

    }

    private void init() {

        tv_agree = (TextView) findViewById(R.id.tv_agree);
        tv_agree.setOnClickListener(this);

        tv_disagree = (TextView) findViewById(R.id.tv_disagree);
        tv_disagree.setOnClickListener(this);


        tv_pending = (TextView) findViewById(R.id.tv_pending);
        tv_pending.setOnClickListener(this);

        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
        tv_no_data.setVisibility(View.GONE);


        iv_agree = (ImageView) findViewById(R.id.iv_agree);
        iv_agree.setVisibility(View.INVISIBLE);

        iv_disagree = (ImageView) findViewById(R.id.iv_disagree);
        iv_disagree.setVisibility(View.INVISIBLE);

        iv_pending = (ImageView) findViewById(R.id.iv_pending);
        iv_pending.setVisibility(View.INVISIBLE);

        lv_view_opinion = (ListView) findViewById(R.id.lv_view_opinion);

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
        tv_title.setText("VIEW OPINION");

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

            setAgree();

        } else if (v == tv_disagree) {

            setDisAgree();


        } else if (v == tv_pending) {
            setPending();


        }

    }

    private void setPending() {
        iv_agree.setVisibility(View.INVISIBLE);
        iv_disagree.setVisibility(View.INVISIBLE);
        iv_pending.setVisibility(View.VISIBLE);

        setPendingData();

    }

    private void setDisAgree() {

        iv_agree.setVisibility(View.INVISIBLE);
        iv_disagree.setVisibility(View.VISIBLE);
        iv_pending.setVisibility(View.INVISIBLE);

        setDisAgreeData();

    }

    private void setAgree() {

        iv_agree.setVisibility(View.VISIBLE);
        iv_disagree.setVisibility(View.INVISIBLE);
        iv_pending.setVisibility(View.INVISIBLE);

        setAgreeData();


    }


    private void GetOpinionWebservice() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetOpinionWebservice Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
                        JSONObject jsonObjectsuccess = jsonObject.optJSONObject("success");

                        jsonArrayagree = jsonObjectsuccess.optJSONArray("agree");
                        jsonArraydisagree = jsonObjectsuccess.optJSONArray("disagree");
                        jsonArraypending = jsonObjectsuccess.optJSONArray("pending");

                        setAgree();

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


        Logger.e("GetOpinionWebservice Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, TeachersViewOpinionActivity.this);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_opinion_cms);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }

    private void setAgreeData() {

        if (jsonArrayagree.length() > 0) {

            viewOpinionModelClassArrayList.clear();

            tv_no_data.setVisibility(View.GONE);


            for (int i = 0; i < jsonArrayagree.length(); i++) {

                JSONObject jsonObject1 = jsonArrayagree.optJSONObject(i);
//
//                                "uid" : 1,
//                                        "name" : Parent Mother Of Abdulaziz Al-Shuwaib Al-Shuwaib,
//                                        "profile_pic" : http:\/\/server.mywebdemo.in\/nurcery\/scripts\/images\/uploads\/5e5ea775fbf3f7aa322098d22a9d6b7012.jpg,
//                                        "child_name" : Javed Al-shuwaib, Abdulaziz Al-Adsani, Hamid Al-shuwaib,
//                                        "opinion" : 1,
//                                        "remark" : TWDY

                String uid = jsonObject1.optString("uid");
                String name = jsonObject1.optString("name");
                String profile_pic = jsonObject1.optString("profile_pic");
                String child_name = jsonObject1.optString("child_name");
                String opinion = jsonObject1.optString("opinion");
                String remark = jsonObject1.optString("remark");
                String stamp = jsonObject1.optString("stamp");

                ViewOpinionModelClass viewOpinionModelClass = new ViewOpinionModelClass();
                viewOpinionModelClass.setUid(uid);
                viewOpinionModelClass.setName(name);
                viewOpinionModelClass.setProfile_pic(profile_pic);
                viewOpinionModelClass.setChild_name(child_name);
                viewOpinionModelClass.setOpinion(opinion);
                viewOpinionModelClass.setRemark(remark);
                viewOpinionModelClass.setStamp(stamp);

                viewOpinionModelClassArrayList.add(viewOpinionModelClass);

            }

        } else {
            viewOpinionModelClassArrayList.clear();

            tv_no_data.setVisibility(View.VISIBLE);


        }

        viewOpinionListAdapter = new ViewOpinionListAdapter(context, viewOpinionModelClassArrayList);
        lv_view_opinion.setAdapter(viewOpinionListAdapter);
        viewOpinionListAdapter.notifyDataSetChanged();

    }

    private void setDisAgreeData() {

        if (jsonArraydisagree.length() > 0) {
            tv_no_data.setVisibility(View.GONE);

            viewOpinionModelClassArrayList.clear();
            for (int i = 0; i < jsonArraydisagree.length(); i++) {

                JSONObject jsonObject1 = jsonArraydisagree.optJSONObject(i);
//
//                                "uid" : 1,
//                                        "name" : Parent Mother Of Abdulaziz Al-Shuwaib Al-Shuwaib,
//                                        "profile_pic" : http:\/\/server.mywebdemo.in\/nurcery\/scripts\/images\/uploads\/5e5ea775fbf3f7aa322098d22a9d6b7012.jpg,
//                                        "child_name" : Javed Al-shuwaib, Abdulaziz Al-Adsani, Hamid Al-shuwaib,
//                                        "opinion" : 1,
//                                        "remark" : TWDY

                String uid = jsonObject1.optString("uid");
                String name = jsonObject1.optString("name");
                String profile_pic = jsonObject1.optString("profile_pic");
                String child_name = jsonObject1.optString("child_name");
                String opinion = jsonObject1.optString("opinion");
                String remark = jsonObject1.optString("remark");
                String stamp = jsonObject1.optString("stamp");

                ViewOpinionModelClass viewOpinionModelClass = new ViewOpinionModelClass();
                viewOpinionModelClass.setUid(uid);
                viewOpinionModelClass.setName(name);
                viewOpinionModelClass.setProfile_pic(profile_pic);
                viewOpinionModelClass.setChild_name(child_name);
                viewOpinionModelClass.setOpinion(opinion);
                viewOpinionModelClass.setRemark(remark);
                viewOpinionModelClass.setStamp(stamp);

                viewOpinionModelClassArrayList.add(viewOpinionModelClass);

            }

        } else {
            viewOpinionModelClassArrayList.clear();
            tv_no_data.setVisibility(View.VISIBLE);


        }

        viewOpinionListAdapter = new ViewOpinionListAdapter(context, viewOpinionModelClassArrayList);
        lv_view_opinion.setAdapter(viewOpinionListAdapter);
        viewOpinionListAdapter.notifyDataSetChanged();

    }

    private void setPendingData() {

        if (jsonArraypending.length() > 0) {

            viewOpinionModelClassArrayList.clear();
            tv_no_data.setVisibility(View.GONE);

            for (int i = 0; i < jsonArraypending.length(); i++) {

                JSONObject jsonObject1 = jsonArraypending.optJSONObject(i);
//
//                                "uid" : 1,
//                                        "name" : Parent Mother Of Abdulaziz Al-Shuwaib Al-Shuwaib,
//                                        "profile_pic" : http:\/\/server.mywebdemo.in\/nurcery\/scripts\/images\/uploads\/5e5ea775fbf3f7aa322098d22a9d6b7012.jpg,
//                                        "child_name" : Javed Al-shuwaib, Abdulaziz Al-Adsani, Hamid Al-shuwaib,
//                                        "opinion" : 1,
//                                        "remark" : TWDY

                String uid = jsonObject1.optString("uid");
                String name = jsonObject1.optString("name");
                String profile_pic = jsonObject1.optString("profile_pic");
                String child_name = jsonObject1.optString("child_name");
                String opinion = jsonObject1.optString("opinion");
                String remark = jsonObject1.optString("remark");
                String stamp = jsonObject1.optString("stamp");

                ViewOpinionModelClass viewOpinionModelClass = new ViewOpinionModelClass();
                viewOpinionModelClass.setUid(uid);
                viewOpinionModelClass.setName(name);
                viewOpinionModelClass.setProfile_pic(profile_pic);
                viewOpinionModelClass.setChild_name(child_name);
                viewOpinionModelClass.setOpinion(opinion);
                viewOpinionModelClass.setRemark(remark);
                viewOpinionModelClass.setStamp(stamp);

                viewOpinionModelClassArrayList.add(viewOpinionModelClass);

            }

        } else {
            viewOpinionModelClassArrayList.clear();
            tv_no_data.setVisibility(View.VISIBLE);


        }

        viewOpinionListAdapter = new ViewOpinionListAdapter(context, viewOpinionModelClassArrayList);
        lv_view_opinion.setAdapter(viewOpinionListAdapter);
        viewOpinionListAdapter.notifyDataSetChanged();

    }

}
