package com.leadinfosoft.littleland.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.HomeChildListDetailsModel;
import com.leadinfosoft.littleland.ModelClass.HomeClassListDetails;
import com.leadinfosoft.littleland.ModelClass.ViewPostListModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.MainActivity;
import com.leadinfosoft.littleland.adapter.ViewPostAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.utill.Utils;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.leadinfosoft.littleland.widget.MyTextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewPost extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    @BindView(R.id.rv_viewpost)
    RecyclerView rv_viewpost;

    TextView tv_no_data;

    TextView tv_week, tv_today, tv_month;
    ImageView iv_week, iv_today, iv_month;

    boolean isEnglish;
    private OnFragmentInteractionListener mListener;

    Context context;

    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    String str_time_period = "today";

    ArrayList<ViewPostListModelClass> viewPostListModelClassArrayList = new ArrayList<>();

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    public ViewPost() {
        // Required empty public constructor
    }

    public static ViewPost newInstance(String param1, String param2) {
        ViewPost fragment = new ViewPost();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        commonFunction();

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            rootView = inflater.inflate(R.layout.fragment_view_post_ar, container, false);
            isEnglish = false;


        } else {
            rootView = inflater.inflate(R.layout.fragment_view_post, container, false);
            isEnglish = true;


        }

        ButterKnife.bind(this, rootView);

        init(rootView);

        initHeader();
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_viewpost.setLayoutManager(llm);

        setTypeface_Text();

        onClickToday();
        GetViewPostWebservice();
        return rootView;
    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            MainActivity.tv_title.setTypeface(tf);
            MainActivity.tv_subtitle.setTypeface(tf);

            tv_week.setTypeface(tf);
            tv_month.setTypeface(tf);
            tv_no_data.setTypeface(tf);
            tv_today.setTypeface(tf);


            if (mParam1.equalsIgnoreCase("news")) {
                ((MyHeaderTextView) getActivity().findViewById(R.id.tv_title)).setText(Common.filter("lbl_news", "ar"));
            } else {
                ((MyHeaderTextView) getActivity().findViewById(R.id.tv_title)).setText(Common.filter("lbl_view_post", "ar"));

            }

//            MainActivity.tv_title.setText(Common.filter("lbl_view_post", "ar"));
            tv_week.setText(Common.filter("lbl_weekly", "ar"));
            tv_month.setText(Common.filter("lbl_monthly", "ar"));
            tv_no_data.setText(Common.filter("lbl_No_Post_Available", "ar"));
            tv_today.setText(Common.filter("lbl_today", "ar"));


        } else {

            MainActivity.tv_title.setTypeface(tf);
            MainActivity.tv_subtitle.setTypeface(tf);

            tv_week.setTypeface(tf);
            tv_month.setTypeface(tf);
            tv_no_data.setTypeface(tf);
            tv_today.setTypeface(tf);

            if (mParam1.equalsIgnoreCase("news")) {
                ((MyHeaderTextView) getActivity().findViewById(R.id.tv_title)).setText(Common.filter("lbl_news", "en"));
            } else {
                ((MyHeaderTextView) getActivity().findViewById(R.id.tv_title)).setText(Common.filter("lbl_view_post", "en"));

            }

//            MainActivity.tv_title.setText(Common.filter("lbl_view_post", "en"));
            tv_week.setText(Common.filter("lbl_weekly", "en"));
            tv_month.setText(Common.filter("lbl_monthly", "en"));
            tv_no_data.setText(Common.filter("lbl_No_Post_Available", "en"));
            tv_today.setText(Common.filter("lbl_today", "en"));
        }

    }

    private void init(View rootView) {

        tv_no_data = (TextView) rootView.findViewById(R.id.tv_no_data);
        tv_no_data.setVisibility(View.GONE);

        tv_week = (TextView) rootView.findViewById(R.id.tv_week);
        tv_week.setOnClickListener(this);

        tv_today = (TextView) rootView.findViewById(R.id.tv_today);
        tv_today.setOnClickListener(this);


        tv_month = (TextView) rootView.findViewById(R.id.tv_month);
        tv_month.setOnClickListener(this);


        iv_week = (ImageView) rootView.findViewById(R.id.iv_week);
        iv_today = (ImageView) rootView.findViewById(R.id.iv_today);
        iv_month = (ImageView) rootView.findViewById(R.id.iv_month);


    }

    private void commonFunction() {
        context = getActivity();
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

    private void initHeader() {

        getActivity().findViewById(R.id.rl_withlogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.rl_withtitle).setVisibility(View.VISIBLE);

        getActivity().findViewById(R.id.iv_add).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.iv_bell1).setVisibility(View.INVISIBLE);

        getActivity().findViewById(R.id.tv_title).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.tv_subtitle).setVisibility(View.VISIBLE);

        MainActivity.iv_profile_pic.setVisibility(View.GONE);


        if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase(Common.USER_TYPE_TEACHER)) {

            if (mParam1.equalsIgnoreCase("news")) {
                ((MyHeaderTextView) getActivity().findViewById(R.id.tv_title)).setText("NEWS");
                MainActivity.tv_subtitle.setTextColor(getActivity().getResources().getColor(R.color.techer_parpal));

                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

                } else {
                    MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


                }

//                    MainActivity.tv_subtitle.setText("CLASS - " + sharedPreferencesClass.getSelected_Class_Name());

            } else {
                ((MyHeaderTextView) getActivity().findViewById(R.id.tv_title)).setText("View Post");
                MainActivity.tv_subtitle.setTextColor(getActivity().getResources().getColor(R.color.techer_parpal));

                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

                } else {
                    MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


                }

//                    MainActivity.tv_subtitle.setText("CLASS - " + sharedPreferencesClass.getSelected_Class_Name());

            }

//            ((MyTextView) getActivity().findViewById(R.id.tv_subtitle)).setText("class - " + sharedPreferencesClass.getSelected_Class_Name());
        } else {
            if (mParam1.equalsIgnoreCase("news")) {
                ((MyHeaderTextView) getActivity().findViewById(R.id.tv_title)).setText("NEWS");

            } else {
                ((MyHeaderTextView) getActivity().findViewById(R.id.tv_title)).setText("View Post");

            }
            MainActivity.tv_subtitle.setTextColor(getActivity().getResources().getColor(R.color.techer_parpal));

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

            } else {
                MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


            }

//                MainActivity.tv_subtitle.setText("CLASS - " + sharedPreferencesClass.getSelected_Class_Name());
        }


        getActivity().findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onClickWeek() {
        iv_week.setVisibility(View.VISIBLE);
        iv_today.setVisibility(View.INVISIBLE);
        iv_month.setVisibility(View.INVISIBLE);

        GetViewPostWebservice();


        /*LinearLayout.LayoutParams txt2Params = new LinearLayout.LayoutParams(0  , 55, 1);
        txt2Params.setMargins(10, 0, 0, 0);
        tv_week.setBackgroundResource(R.drawable.viewpost_today);
        tv_week.setLayoutParams(txt2Params);
        tv_week.setTextColor(getResources().getColor(R.color.white));

        LinearLayout.LayoutParams txt1Params = new LinearLayout.LayoutParams(0, 50, 1);
        txt1Params.setMargins(10, 0, 0, 14);
        tv_today.setBackgroundResource(R.drawable.viewpost_today_bg);
        tv_today.setLayoutParams(txt1Params);
        tv_today.setTextColor(getResources().getColor(R.color.white));

        LinearLayout.LayoutParams txt3Params = new LinearLayout.LayoutParams(0, 50, 1);
        txt3Params.setMargins(10, 0, 0, 14);
        tv_month.setBackgroundResource(R.drawable.viewpost_monthly_bg);
        tv_month.setLayoutParams(txt3Params);
        tv_month.setTextColor(getResources().getColor(R.color.white));*/
    }

    public void onClickToday() {
        iv_week.setVisibility(View.INVISIBLE);
        iv_today.setVisibility(View.VISIBLE);
        iv_month.setVisibility(View.INVISIBLE);

        GetViewPostWebservice();

    }

    public void onClickMonth() {
        iv_week.setVisibility(View.INVISIBLE);
        iv_today.setVisibility(View.INVISIBLE);
        iv_month.setVisibility(View.VISIBLE);

        GetViewPostWebservice();

    }

    @Override
    public void onClick(View v) {

        if (v == tv_week) {
            str_time_period = "week";
            onClickWeek();
        } else if (v == tv_today) {
            str_time_period = "today";
            onClickToday();
        } else if (v == tv_month) {
            str_time_period = "month";
            onClickMonth();
        }

    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    private void GetViewPostWebservice() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetViewPostWebservice Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

                        JSONArray jsonArraysuccess = jsonObject.optJSONArray("success");

                        if (jsonArraysuccess != null) {
                            if (jsonArraysuccess.length() > 0) {

                                rv_viewpost.setVisibility(View.VISIBLE);
                                tv_no_data.setVisibility(View.GONE);

                                viewPostListModelClassArrayList.clear();
                                for (int i = 0; i < jsonArraysuccess.length(); i++) {
                                    JSONObject jsonObject1 = jsonArraysuccess.optJSONObject(i);

                                    String id = jsonObject1.optString("id");
                                    String uid = jsonObject1.optString("uid");
                                    String title = jsonObject1.optString("title");
                                    String stamp = jsonObject1.optString("stamp");
                                    String image = jsonObject1.optString("image");

                                    ViewPostListModelClass viewPostListModelClass = new ViewPostListModelClass();
                                    viewPostListModelClass.setId(id);
                                    viewPostListModelClass.setUid(uid);
                                    viewPostListModelClass.setTitle(title);
                                    viewPostListModelClass.setStamp(stamp);
                                    viewPostListModelClass.setImage(image);

                                    viewPostListModelClassArrayList.add(viewPostListModelClass);

                                }


                            } else {
                                //jsonarraysuccess length 0

                                viewPostListModelClassArrayList.clear();

                                rv_viewpost.setVisibility(View.GONE);
                                tv_no_data.setVisibility(View.VISIBLE);

                            }

                            ViewPostAdapter viewPostAdapter = new ViewPostAdapter(getActivity(), isEnglish, viewPostListModelClassArrayList);
                            rv_viewpost.setAdapter(viewPostAdapter);

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

        Logger.e("26/08 mParam1 from page =====> " + mParam1 + "");


        if (mParam1.equalsIgnoreCase("news")) {
            params.add(new BasicNameValuePair("action", "get_news"));

        } else {
            params.add(new BasicNameValuePair("action", "get_list"));
            params.add(new BasicNameValuePair("class_id", sharedPreferencesClass.getSelected_Class_Id()));


        }

        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("time_period", str_time_period));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("GetViewPostWebservice Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_New_Post_Or_Direct_Message);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }
}
