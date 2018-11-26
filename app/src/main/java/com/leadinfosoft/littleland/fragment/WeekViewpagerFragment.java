package com.leadinfosoft.littleland.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.ModelClass.AttendenceReportListModel;
import com.leadinfosoft.littleland.ModelClass.AttendenceReportWeeklyListModel;
import com.leadinfosoft.littleland.ModelClass.MonthlyAttendenceReportModel;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.adapter.MonthAttendRecyAdapter;
import com.leadinfosoft.littleland.adapter.WeekAttendanceAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.utill.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class WeekViewpagerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    ArrayList<AttendenceReportWeeklyListModel> attendenceReportListModelArrayList = new ArrayList<>();

    int position = 0;

    ArrayList<MonthlyAttendenceReportModel> param2 = new ArrayList<>();

    RecyclerView rv_weekattend;
    TextView tv_no_reports;

    SharedPreferencesClass sharedPreferencesClass;

    Context context;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;


    public WeekViewpagerFragment() {
        // Required empty public constructor
    }


    public static WeekViewpagerFragment newInstance(String param1, ArrayList<MonthlyAttendenceReportModel> param2) {
        WeekViewpagerFragment fragment = new WeekViewpagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            param2 = (ArrayList<MonthlyAttendenceReportModel>) getArguments().getSerializable(ARG_PARAM2);

            Logger.e("25/08 view pager position maparam1 ======> " + mParam1 + "");
            Logger.e("25/08 view pager report mParam2 ======> " + param2.size() + "");

            position = Integer.parseInt(mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = null;
        commonFunction();

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            rootView = inflater.inflate(R.layout.fragment_week_viewpager_ar, container, false);


        } else {
            rootView = inflater.inflate(R.layout.fragment_week_viewpager, container, false);


        }

        rv_weekattend = (RecyclerView) rootView.findViewById(R.id.rv_weekattend);

        tv_no_reports = (TextView) rootView.findViewById(R.id.tv_no_reports);
        tv_no_reports.setVisibility(View.GONE);
//        rv_month.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_weekattend.setLayoutManager(llm);

        setTypeface_Text();

        GetReportDetails();

        return rootView;
    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            tv_no_reports.setTypeface(tf);
            tv_no_reports.setText(Common.filter("lbl_No_Reports_Found", "ar"));

        } else {
            tv_no_reports.setTypeface(tf);
            tv_no_reports.setText(Common.filter("lbl_No_Reports_Found", "en"));
        }

    }

    private void commonFunction() {
        context = getActivity();
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

    private void GetReportDetails() {

        try {
            JSONArray jsonArrayReport = new JSONArray(param2.get(position).getReport());


            Logger.e("25/08 weekly report ==========> " + jsonArrayReport + "");

            if (jsonArrayReport.length() > 0) {
                attendenceReportListModelArrayList.clear();
                tv_no_reports.setVisibility(View.GONE);
                rv_weekattend.setVisibility(View.VISIBLE);


                for (int i = 0; i < jsonArrayReport.length(); i++) {
                    JSONObject jsonObject1 = jsonArrayReport.optJSONObject(i);

                    String date = jsonObject1.optString("date");
                    String status = jsonObject1.optString("status");
                    String remark = jsonObject1.optString("remark");


                    AttendenceReportWeeklyListModel attendenceReportListModel = new AttendenceReportWeeklyListModel();
                    attendenceReportListModel.setDate(date);
                    attendenceReportListModel.setStatus(status);
                    attendenceReportListModel.setRemark(remark);

                    attendenceReportListModelArrayList.add(attendenceReportListModel);


                }

            } else {
                //jsonArrayReport length 0
                attendenceReportListModelArrayList.clear();
                tv_no_reports.setVisibility(View.VISIBLE);
                rv_weekattend.setVisibility(View.GONE);


            }

            if (Utils.userSelectedLang(getActivity())) {
                WeekAttendanceAdapter weekAttendanceAdapter = new WeekAttendanceAdapter(getActivity(), true, attendenceReportListModelArrayList);
                rv_weekattend.setAdapter(weekAttendanceAdapter);
            } else {
                WeekAttendanceAdapter weekAttendanceAdapter = new WeekAttendanceAdapter(getActivity(), false, attendenceReportListModelArrayList);
                rv_weekattend.setAdapter(weekAttendanceAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
