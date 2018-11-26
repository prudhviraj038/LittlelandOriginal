package com.leadinfosoft.littleland.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.ModelClass.CalendarCollection;
import com.leadinfosoft.littleland.ModelClass.CalenderEventsList;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.MainActivity;
import com.leadinfosoft.littleland.adapter.CalendarAdapterCustom;
import com.leadinfosoft.littleland.adapter.CalenderEventsListAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.utill.Utils;
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
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 8/31/2017.
 */

public class CalendarNewFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String previous_screen_select_month = "";
    private String previous_screen_select_year = "";

    @BindView(R.id.gv_calendar)
    public GridView gv_calendar;
    private CalendarAdapterCustom cal_adapter;
    @BindView(R.id.tv_mon1)
    public MyHeaderTextView tv_mon1;
    @BindView(R.id.tv_mon2)
    public MyHeaderTextView tv_mon2;
    @BindView(R.id.tv_mon3)
    public MyHeaderTextView tv_mon3;
    @BindView(R.id.tv_mon4)
    public MyHeaderTextView tv_mon4;
    @BindView(R.id.tv_mon5)
    public MyHeaderTextView tv_mon5;
    @BindView(R.id.tv_mon6)
    public MyHeaderTextView tv_mon6;
    @BindView(R.id.tv_mon7)
    public MyHeaderTextView tv_mon7;
    @BindView(R.id.tv_mon8)
    public MyHeaderTextView tv_mon8;
    @BindView(R.id.tv_mon9)
    public MyHeaderTextView tv_mon9;
    @BindView(R.id.tv_mon10)
    public MyHeaderTextView tv_mon10;
    @BindView(R.id.tv_mon11)
    public MyHeaderTextView tv_mon11;
    @BindView(R.id.tv_mon12)
    public MyHeaderTextView tv_mon12;
    @BindView(R.id.ll_month)
    public LinearLayout ll_month;
    private GregorianCalendar cal_month;
    @BindView(R.id.hScroll)
    public HorizontalScrollView hScroll;
    private Context context;
    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;

    @BindView(R.id.lv_eventList)
    NonScrollListView lv_eventList;
    @BindView(R.id.tv_no_events)
    TextView tv_no_events;
    String current_month = "";
    String current_year = "";
    ArrayList<CalenderEventsList> calenderEventsListArrayList = new ArrayList<>();

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;


    public static CalendarNewFragment newInstance(String param1, String param2) {
        CalendarNewFragment fragment = new CalendarNewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getArguments() != null) {
                previous_screen_select_month = getArguments().getString(ARG_PARAM1);
                previous_screen_select_year = getArguments().getString(ARG_PARAM2);

                Logger.e("25/08 previous_screen_select_month =======> " + previous_screen_select_month + "");
                Logger.e("25/08 previous_screen_select_year =======> " + previous_screen_select_year + "");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendarnew, container, false);
        ButterKnife.bind(this, view);
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

        initHeader();

        setTypeface_Text();

        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_adapter = new CalendarAdapterCustom(getActivity(), cal_month);
        gv_calendar.setAdapter(cal_adapter);

        final int currentMonth = cal_month.get(Calendar.MONTH);
        final int currentYear = cal_month.get(Calendar.YEAR);
        if (previous_screen_select_month.equalsIgnoreCase("") || previous_screen_select_month.length() == 0) {
            current_month = currentMonth + "";
            current_year = currentYear + "";
        } else {
            current_month = previous_screen_select_month + "";
            current_year = previous_screen_select_year + "";
        }

        hScroll.post(new Runnable() {
            @Override
            public void run() {
                onDefaultSelected(Integer.parseInt(current_month));
            }
        });

        return view;

    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            MainActivity.tv_subtitle.setTypeface(tf);
            MainActivity.tv_title.setTypeface(tf);

            tv_no_events.setTypeface(tf);

            MainActivity.tv_title.setText(Common.filter("lbl_MONTHLY_CALENDER", "ar"));

            tv_no_events.setText(Common.filter("lbl_no_events", "ar"));


        } else {
            MainActivity.tv_subtitle.setTypeface(tf);
            MainActivity.tv_title.setTypeface(tf);

            tv_no_events.setTypeface(tf);

            tv_no_events.setText(Common.filter("lbl_no_events", "en"));


            MainActivity.tv_title.setText(Common.filter("lbl_MONTHLY_CALENDER", "en"));

        }

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

    private void setBackgroundMonth(int btnId) {
        ArrayList<MyHeaderTextView> arrayMonth = Utils.findAllTextView(ll_month);
        for (int i = 0; i < arrayMonth.size(); i++) {
            if (arrayMonth.get(i).getId() == btnId) {
                arrayMonth.get(i).setBackgroundResource(R.drawable.celender_mounth_bg);
                arrayMonth.get(i).setTextColor(getResources().getColor(R.color.cal_month_select));
            } else {
                arrayMonth.get(i).setBackgroundResource(0);
                arrayMonth.get(i).setTextColor(Color.WHITE);
            }
        }

        Logger.e("11/01 month " + current_month + " ");
        Logger.e("11/01 year " + current_year + " ");


        current_year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        GetEventACtionWebservice();
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

                        ArrayList<CalendarCollection> arrayList = new ArrayList<>();
                        if (jsonArraydays.length() > 0) {
                            for (int i = 0; i < jsonArraydays.length(); i++) {
                                String date = jsonArraydays.optString(i);

                                try {
                                    Logger.e("Calender Live Date ======> " + date);
                                    // Here pass the event to calendar


//                                    eventmap.put(i + "", new SimpleDateFormat("yyyy-MM-dd").parse(date));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

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
                                CalendarCollection collection = new CalendarCollection(Utils.convertDate(datetime), "#" + color);
                                arrayList.add(collection);
                                calenderEventsListArrayList.add(calenderEventsList);

                            }
                            cal_adapter.setAllEvents(arrayList);
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

    private void onDefaultSelected(int pos) {
        current_month = (pos + 1) + "";
        current_year = cal_adapter.getCurrentCal().get(Calendar.YEAR) + "";

        if (pos == 0) {
            scrollTo(tv_mon1);
            setBackgroundMonth(tv_mon1.getId());
        } else if (pos == 1) {
            scrollTo(tv_mon2);
            setBackgroundMonth(tv_mon2.getId());
        } else if (pos == 2) {
            scrollTo(tv_mon3);
            setBackgroundMonth(tv_mon3.getId());
        } else if (pos == 3) {
            scrollTo(tv_mon4);
            setBackgroundMonth(tv_mon4.getId());
        } else if (pos == 4) {
            scrollTo(tv_mon5);
            setBackgroundMonth(tv_mon5.getId());
        } else if (pos == 5) {
            scrollTo(tv_mon6);
            setBackgroundMonth(tv_mon6.getId());
        } else if (pos == 6) {
            scrollTo(tv_mon7);
            setBackgroundMonth(tv_mon7.getId());
        } else if (pos == 7) {
            scrollTo(tv_mon8);
            setBackgroundMonth(tv_mon8.getId());
        } else if (pos == 8) {
            scrollTo(tv_mon9);
            setBackgroundMonth(tv_mon9.getId());
        } else if (pos == 9) {
            scrollTo(tv_mon10);
            setBackgroundMonth(tv_mon10.getId());
        } else if (pos == 10) {
            scrollTo(tv_mon11);
            setBackgroundMonth(tv_mon11.getId());
        } else if (pos == 11) {
            scrollTo(tv_mon12);
            setBackgroundMonth(tv_mon12.getId());
        }
    }

    @OnClick(R.id.tv_mon1)
    public void onClickJan(View view) {
        current_month = 1 + "";
        current_year = cal_adapter.getCurrentCal().get(Calendar.YEAR) + "";

        setMonth(0, view);
        setBackgroundMonth(tv_mon1.getId());

    }

    @OnClick(R.id.tv_mon2)
    public void onClickFeb(View view) {
        current_month = 2 + "";
        current_year = cal_adapter.getCurrentCal().get(Calendar.YEAR) + "";

        setMonth(1, view);
        setBackgroundMonth(tv_mon2.getId());
    }

    @OnClick(R.id.tv_mon3)
    public void onClickMarch(View view) {
        current_month = 3 + "";
        current_year = cal_adapter.getCurrentCal().get(Calendar.YEAR) + "";

        setMonth(2, view);
        setBackgroundMonth(tv_mon3.getId());
    }

    @OnClick(R.id.tv_mon4)
    public void onClickApr(View view) {
        current_month = 4 + "";
        current_year = cal_adapter.getCurrentCal().get(Calendar.YEAR) + "";


        setMonth(3, view);
        setBackgroundMonth(tv_mon4.getId());
    }

    @OnClick(R.id.tv_mon5)
    public void onClickMay(View view) {
        current_month = 5 + "";
        current_year = cal_adapter.getCurrentCal().get(Calendar.YEAR) + "";

        setMonth(4, view);
        setBackgroundMonth(tv_mon5.getId());
    }

    @OnClick(R.id.tv_mon6)
    public void onClickJun(View view) {
        current_month = 6 + "";
        current_year = cal_adapter.getCurrentCal().get(Calendar.YEAR) + "";

        setMonth(5, view);
        setBackgroundMonth(tv_mon6.getId());
    }

    @OnClick(R.id.tv_mon7)
    public void onClickJuly(View view) {
        current_month = 7 + "";
        current_year = cal_adapter.getCurrentCal().get(Calendar.YEAR) + "";


        setMonth(6, view);
        setBackgroundMonth(tv_mon7.getId());
    }

    @OnClick(R.id.tv_mon8)
    public void onClickAug(View view) {
        current_month = 8 + "";
        current_year = cal_adapter.getCurrentCal().get(Calendar.YEAR) + "";

        setMonth(7, view);
        setBackgroundMonth(tv_mon8.getId());
    }

    @OnClick(R.id.tv_mon9)
    public void onClickSep(View view) {
        current_month = 9 + "";
        current_year = cal_adapter.getCurrentCal().get(Calendar.YEAR) + "";

        setMonth(8, view);
        setBackgroundMonth(tv_mon9.getId());
    }

    @OnClick(R.id.tv_mon10)
    public void onClickOct(View view) {
        current_month = 10 + "";
        current_year = cal_adapter.getCurrentCal().get(Calendar.YEAR) + "";

        setMonth(9, view);
        setBackgroundMonth(tv_mon10.getId());
    }

    @OnClick(R.id.tv_mon11)
    public void onClickNov(View view) {
        current_month = 11 + "";
        current_year = cal_adapter.getCurrentCal().get(Calendar.YEAR) + "";

        setMonth(10, view);
        setBackgroundMonth(tv_mon11.getId());
    }

    @OnClick(R.id.tv_mon12)
    public void onClickDec(View view) {
        current_month = 12 + "";
        current_year = cal_adapter.getCurrentCal().get(Calendar.YEAR) + "";

        setMonth(11, view);
        setBackgroundMonth(tv_mon12.getId());
    }

    private void setMonth(int month, View view) {
        cal_month.set(GregorianCalendar.MONTH, month);
        if (view != null) {
            scrollTo(view);
        }
        refreshCalendar();
    }

    private void scrollTo(View view) {
        int vLeft = view.getLeft();
        int vRight = view.getRight();
        int sWidth = hScroll.getWidth();
        ObjectAnimator animator = ObjectAnimator.ofInt(hScroll, "scrollX", ((vLeft + vRight - sWidth) / 2));
        animator.setDuration(100);
        animator.start();

        hScroll.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
    }

    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
//        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }
}
