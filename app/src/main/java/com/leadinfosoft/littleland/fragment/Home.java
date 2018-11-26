package com.leadinfosoft.littleland.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.CalenderActivity;
import com.leadinfosoft.littleland.activity.DailyAttendance;
import com.leadinfosoft.littleland.activity.LoginActivity;
import com.leadinfosoft.littleland.activity.MainActivity;
import com.leadinfosoft.littleland.activity.MonthlyAttendanceActivity;
import com.leadinfosoft.littleland.activity.WeekAttendanceActivity;
import com.leadinfosoft.littleland.adapter.HomeEventPageAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.utill.Utils;
import com.leadinfosoft.littleland.widget.LoadingDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Home extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.vp_pager)
    ViewPager vp_pager;

    @BindView(R.id.rl_calender)
    RelativeLayout rl_calender;

    boolean isEnglish;
    HomeEventPageAdapter homeEventPageAdapter;
    public static LoadingDialog loadingDialog;
    Context context;

    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    private OnFragmentInteractionListener mListener;

    public Home() {
        // Required empty public constructor
    }


    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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

        if (Utils.userSelectedLang(getActivity())) {
            isEnglish = true;
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
        } else {
            isEnglish = false;
            rootView = inflater.inflate(R.layout.fragment_home_ar, container, false);
        }

        ButterKnife.bind(this, rootView);

        loadingDialog = new LoadingDialog(context);
        initHeader();
        homeEventPageAdapter = new HomeEventPageAdapter(getActivity(), isEnglish);
        vp_pager.setAdapter(homeEventPageAdapter);


        if (sharedPreferencesClass.getDEVICE_TOKEN().equalsIgnoreCase("") || sharedPreferencesClass.getDEVICE_TOKEN().length() == 0) {
            sharedPreferencesClass.setDEVICE_TOKEN("N/A");
        }
        if (sharedPreferencesClass.getDEVICE_CODE_UUID().equalsIgnoreCase("") || sharedPreferencesClass.getDEVICE_CODE_UUID().length() == 0) {
            sharedPreferencesClass.setDEVICE_CODE_UUID(UUID.randomUUID().toString() + "");
        }

        GetInitialWebservice();


        return rootView;
    }

    private void commonFunction() {
        context = getActivity();
        con = new ConnectionDetector(context);
        sharedPreferencesClass = new SharedPreferencesClass(context);
    }

    @OnClick(R.id.rl_calender)
    public void onClickCalender() {
        openFragment(new Teacher());
//        loadingDialog.show();
//        startActivity(new Intent(getActivity(), CalenderActivity.class));
//        getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

    }

    @OnClick(R.id.rl_attend)
    public void onClickAttendance() {
//        openFragment(new Calender());
        startActivity(new Intent(getActivity(), DailyAttendance.class));
        getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }

    @OnClick(R.id.tv_msg)
    public void onClickMsg() {
        startActivity(new Intent(getActivity(), WeekAttendanceActivity.class));
        getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//        openFragment(new ViewPost());
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTrasaction = fm.beginTransaction();
        fragmentTrasaction.replace(R.id.frame, fragment);
        fragmentTrasaction.addToBackStack(fragment.toString());
        fragmentTrasaction.commit();
    }


    private void initHeader() {

        getActivity().findViewById(R.id.rl_withtitle).setVisibility(View.GONE);
        getActivity().findViewById(R.id.rl_withlogo).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.iv_orang).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_star).setVisibility(View.GONE);
        getActivity().findViewById(R.id.rl_spiner).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_applogo).setVisibility(View.VISIBLE);

        MainActivity.iv_profile_pic.setVisibility(View.GONE);


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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void GetInitialWebservice() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("Get Initial Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();


                        JSONObject jsonObjectSuccess = jsonObject.optJSONObject("success");

                        sharedPreferencesClass.setUSER_DETAILS(jsonObjectSuccess.optJSONObject("user") + "");

                        JSONObject jsonObjectUser = jsonObjectSuccess.optJSONObject("user");

                        sharedPreferencesClass.setUSER_Id(jsonObjectUser.optString("uid"));
                        sharedPreferencesClass.setUser_Type(jsonObjectUser.optString("type"));

                        sharedPreferencesClass.setGet_Initial_Days_Array(jsonObjectSuccess.optJSONArray("days") + "");

                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("dev_token", sharedPreferencesClass.getDEVICE_TOKEN()));
        params.add(new BasicNameValuePair("dev_type", Common.Device_Type));
        params.add(new BasicNameValuePair("code", sharedPreferencesClass.getDEVICE_CODE_UUID()));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));



        Logger.e("Get Initial Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_Get_Initial);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }
}


