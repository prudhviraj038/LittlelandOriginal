package com.leadinfosoft.littleland.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.CalenderEventsList;
import com.leadinfosoft.littleland.ModelClass.HomeClassListDetails;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.DailyAttendance;
import com.leadinfosoft.littleland.activity.MainActivity;
import com.leadinfosoft.littleland.adapter.CalenderEventsListAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.widget.CalendarView;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.leadinfosoft.littleland.widget.MyTextView;
import com.leadinfosoft.littleland.widget.NonScrollListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Calender extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String previous_screen_select_month = "";
    private String previous_screen_select_year = "";

//    ArrayList<HashMap<String, Date>> eventmap = new ArrayList<HashMap<String,Date>>();

    HashMap<String, Date> eventmap = new HashMap<String, Date>();

    Context context;

    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    String current_month = "";
    String current_year = "";

    @BindView(R.id.calendar_view)
    CalendarView calendar_view;

    private OnFragmentInteractionListener mListener;

    ArrayList<CalenderEventsList> calenderEventsListArrayList = new ArrayList<>();

    NonScrollListView lv_eventList;

    TextView tv_no_events;

    public Calender() {
        // Required empty public constructor
    }


    public static Calender newInstance(String param1, String param2) {
        Calender fragment = new Calender();
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
            previous_screen_select_month = getArguments().getString(ARG_PARAM1);
            previous_screen_select_year = getArguments().getString(ARG_PARAM2);

            Logger.e("25/08 previous_screen_select_month =======> " + previous_screen_select_month + "");
            Logger.e("25/08 previous_screen_select_year =======> " + previous_screen_select_year + "");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;

        commonFunction();

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            rootView=inflater.inflate(R.layout.fragment_calender, container, false);

        } else {
            rootView=inflater.inflate(R.layout.fragment_calender, container, false);


        }

        lv_eventList = (NonScrollListView) rootView.findViewById(R.id.lv_eventList);
        tv_no_events = (TextView) rootView.findViewById(R.id.tv_no_events);
        tv_no_events.setVisibility(View.GONE);

        ButterKnife.bind(this, rootView);
      /*  try {

            eventmap.put("meeting", new SimpleDateFormat("yyyy-MM-dd").parse("2017-07-10"));
            eventmap.put("festival", new SimpleDateFormat("yyyy-MM-dd").parse("2017-07-23"));
            eventmap.put("holiday", new SimpleDateFormat("yyyy-MM-dd").parse("2017-07-17"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar_view.updateCalendar(eventmap);*/
        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        Logger.e("08/08 year ====> " + year + "");

        int month = c.get(Calendar.MONTH) + 1;
        Logger.e("08/08 month ====> " + month + "");

        if (previous_screen_select_month.equalsIgnoreCase("") || previous_screen_select_month.length() == 0) {
            current_month = month + "";
            current_year = year + "";
        } else {
            current_month = previous_screen_select_month + "";
            current_year = previous_screen_select_year + "";
        }

        calendar_view.setMonth(Integer.parseInt(current_month));


        initHeader();

        calendar_view.setEventHandler(new CalendarView.EventHandler() {
            @Override
            public void onDayLongPress(Date date) {
                String date1 = date.getMonth() + "";
                Logger.e("08/08 date1 =======> " + date1 + "");
            }

            @Override
            public void onMonthChangeListener(int pos) {
                int month = pos + 1;
                Logger.e("month " + month + "");
                Logger.e("year " + year + "");

                current_month = month + "";
                current_year = year + "";

                GetEventACtionWebservice();


//                Logger.e("year " + year + "");


            }
        });


        GetEventACtionWebservice();


        return rootView;
    }

    private void initHeader() {

        getActivity().findViewById(R.id.rl_withtitle).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.rl_withlogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_orang).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_star).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_applogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.rl_spiner).setVisibility(View.GONE);

        getActivity().findViewById(R.id.iv_add).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.iv_bell1).setVisibility(View.INVISIBLE);

        getActivity().findViewById(R.id.iv_back).setVisibility(View.VISIBLE);

        MainActivity.tv_title.setText("MONTHLY CALENDER");

        MainActivity.tv_title.setVisibility(View.VISIBLE);
        MainActivity.tv_subtitle.setVisibility(View.VISIBLE);

        MainActivity.iv_profile_pic.setVisibility(View.GONE);



        if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase(Common.USER_TYPE_TEACHER)) {

            MainActivity.tv_subtitle.setTextColor(getActivity().getResources().getColor(R.color.techer_parpal));

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

            } else {
                MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


            }

//            MainActivity.tv_subtitle.setText("CLASS - " + sharedPreferencesClass.getSelected_Class_Name());

        } else {
            MainActivity.tv_subtitle.setTextColor(getActivity().getResources().getColor(R.color.techer_parpal));

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

            } else {
                MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


            }

//            MainActivity.tv_subtitle.setText("CLASS - " + sharedPreferencesClass.getSelected_Class_Name());

        }


        getActivity().findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();

               /* getActivity().findViewById(R.id.iv_back).setEnabled(false);

                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                getActivity().findViewById(R.id.iv_back).setEnabled(true);
                            }
                        });
                    }
                }, 5000);*/

            }
        });


        if (sharedPreferencesClass.getSelected_Class_Name().equalsIgnoreCase("") || sharedPreferencesClass.getSelected_Class_Name().length() == 0) {
            MainActivity.tv_select_class.setText("Select Class");

        } else {
            MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Class_Name());

        }

        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void commonFunction() {
        context = getActivity();
        con = new ConnectionDetector(context);
        sharedPreferencesClass = new SharedPreferencesClass(context);
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    private void GetEventACtionWebservice() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("Get URL_Event_Action WebService  Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

                        JSONObject jsonObjectSuccess = jsonObject.optJSONObject("success");

                        JSONArray jsonArraydays = jsonObjectSuccess.optJSONArray("days");
                        JSONArray jsonArrayevents = jsonObjectSuccess.optJSONArray("events");

                        if (jsonArraydays.length() > 0) {
                            for (int i = 0; i < jsonArraydays.length(); i++) {
                                String date = jsonArraydays.optString(i);

                                try {
                                    Logger.e("Calender Live Date ======> " + date);

                                    /*HashMap<String, Date> map = new HashMap<String, Date>();
                                    map.put("",new SimpleDateFormat("yyyy-MM-dd").parse(date));
                                    eventmap.add(map);*/

                                    eventmap.put(i + "", new SimpleDateFormat("yyyy-MM-dd").parse(date));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }

                            calendar_view.updateCalendar(eventmap);
                        }


                        if (jsonArrayevents.length() > 0) {
                            calenderEventsListArrayList.clear();
                            lv_eventList.setVisibility(View.VISIBLE);
                            tv_no_events.setVisibility(View.GONE);

                            for (int i = 0; i < jsonArrayevents.length(); i++) {

                                JSONObject jsonObject1 = jsonArrayevents.optJSONObject(i);

                                String id = jsonObject1.optString("id");
                                String datetime = jsonObject1.optString("datetime");
                                String color = jsonObject1.optString("color");
                                String title = jsonObject1.optString("title");
                                String title_ar = jsonObject1.optString("title_ar");
                                String note = jsonObject1.optString("note");
                                String note_ar = jsonObject1.optString("note_ar");

                                CalenderEventsList calenderEventsList = new CalenderEventsList();
                                calenderEventsList.setId(id);
                                calenderEventsList.setDatetime(datetime);
                                calenderEventsList.setColor(color);
                                calenderEventsList.setTitle(title);
                                calenderEventsList.setTitle_ar(title_ar);
                                calenderEventsList.setNote(note);
                                calenderEventsList.setNote_ar(note_ar);

                                calenderEventsListArrayList.add(calenderEventsList);

                            }
                            CalenderEventsListAdapter calenderEventsListAdapter = new CalenderEventsListAdapter(context, calenderEventsListArrayList);
                            lv_eventList.setAdapter(calenderEventsListAdapter);
                            calenderEventsListAdapter.notifyDataSetChanged();

                            lv_eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    /*startActivity(new Intent(getActivity(), DailyAttendance.class));
                                    getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);*/
                                }
                            });

                        } else {
                            calenderEventsListArrayList.clear();
                            CalenderEventsListAdapter calenderEventsListAdapter = new CalenderEventsListAdapter(context, calenderEventsListArrayList);
                            lv_eventList.setAdapter(calenderEventsListAdapter);
                            calenderEventsListAdapter.notifyDataSetChanged();
                            lv_eventList.setVisibility(View.GONE);

                            tv_no_events.setVisibility(View.VISIBLE);
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

        params.add(new BasicNameValuePair("action", "get"));
        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("type", sharedPreferencesClass.getUser_Type()));
        params.add(new BasicNameValuePair("month", current_month));
        params.add(new BasicNameValuePair("year", current_year));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("Get URL_Event_Action WebService Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_Event_Action);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }

}
