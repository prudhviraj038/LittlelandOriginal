package com.leadinfosoft.littleland.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.leadinfosoft.littleland.BuildConfig;
import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.FilePath;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.MySSLSocketFactory;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.RequestMakerBg;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.HomeChildListDetailsModel;
import com.leadinfosoft.littleland.ModelClass.HomeClassListDetails;
import com.leadinfosoft.littleland.ModelClass.MyKidsListModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.AboutUsActivity;
import com.leadinfosoft.littleland.activity.ContactUsActivity;
import com.leadinfosoft.littleland.activity.DailyAttendance;
import com.leadinfosoft.littleland.activity.FirstActivity;
import com.leadinfosoft.littleland.activity.MainActivity;
import com.leadinfosoft.littleland.activity.MonthlyAttendanceActivity;
import com.leadinfosoft.littleland.activity.WeekAttendanceActivity;
import com.leadinfosoft.littleland.adapter.MenuListAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.utill.Utils;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Teacher extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    @BindView(R.id.compactcalendar_view)
    CompactCalendarView compactCalendarView;

    boolean isopenpost = false;

    private OnFragmentInteractionListener mListener;

    Context context;

    Response_string<String> resp;
    RequestMaker req;

    RequestMakerBg requestMakerBg;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    ImageLoader imageLoader;
    DisplayImageOptions options;

    MyHeaderTextView tv_day, tv_month;


    ArrayList<HomeClassListDetails> homeClassListDetailsArrayList = new ArrayList<>();

    ArrayList<String> class_nameArrayList = new ArrayList<>();
    ArrayList<String> class_nameArrayList_ar = new ArrayList<>();

    ArrayList<HomeChildListDetailsModel> homeChildListDetailsModelArrayList = new ArrayList<>();

    ArrayList<String> child_nameArrayList = new ArrayList<>();
    ArrayList<String> child_nameArrayList_ar = new ArrayList<>();

    LinearLayout ll_parents_view_calender, ll_teacher_view_calender;
    LinearLayout ll_teacher_take_attendence;

    RelativeLayout rl_teacher, rl_parents;

    LinearLayout ll_teacher_manage_post, ll_teacher_manage_sub_post;
    LinearLayout ll_parent_manage_post, ll_parent_manage_sub_post;

    LinearLayout ll_parents_manage_attendence_parent, ll_parents_manage_attendence_sub_post;
    LinearLayout ll_teacher_manage_attendence_parent, ll_teacher_manage_attendence_sub_post;

    LinearLayout ll_teacher_event_calender_parent;
    LinearLayout ll_parents_event_calender_parent;

    LinearLayout ll_parents_event_calender_sub_post, ll_teacher_event_calender_sub_post;


    Animation animShow, animHide;

    LinearLayout ll_teacher_new_Post, ll_parent_new_Post;

    LinearLayout ll_parents_view_monthly_report, ll_parents_view_weekly_report;

    LinearLayout ll_teacher_view_monthly_report, ll_teacher_view_weekly_report;

    LinearLayout ll_teacher_view_post, ll_parents_view_post;

    LinearLayout ll_teacher_send_message, ll_parents_send_message;

    LinearLayout ll_teacher_conversations, ll_parents_conversations;

    LinearLayout ll_teacher_direct_message, ll_teacher_direct_message_sub_post;

    LinearLayout ll_parents_direct_message, ll_parents_direct_message_sub_post;

    ImageView iv_parents_manage_arrow, iv_parents_attandence_arrow, iv_parents_events_arrow, iv_parents_direct_arrow;

    ImageView iv_teacher_manage_arrow, iv_teacher_attandence_arrow, iv_teacher_events_arrow, iv_teacher_direct_arrow;

    RelativeLayout rl_curentdate;

    String selected_month = "";
    String selected_year = "";

    ImageView iv_user_image;
    MyHeaderTextView tv_parents_name, tv_parents_educ, tv_name, tv_educ;

    final int CAMERA_CAPTURE = 81;
    //captured picture uri
    private Uri picUri;

    final int PIC_CROP = 83;

    final int PICK_IMAGE_REQUEST = 82;

    private Bitmap bitmapgallery;

    String image = "";

    private String selectedFilePath = "";

    MyHeaderTextView tv_parent_viewpost, tv_parent_lbl_viewpost, tv_parents_subpost_view_posts, tv_parent_attendance, tv_parent_lbl_attandence, tv_parent_events, tv_parent_lbl_events, tv_parent_direct, tv_parent_lbl_direct, tv_attendence_parent_sub_post_view_monthly_report, tv_attendence_parent_sub_post_view_weekly_report, tv_events_parent_sub_post_view_calender,
            tv_direct_parent_sub_post_send_message, tv_direct_parent_sub_post_conversion;

    MyHeaderTextView tv_teacher_manage_post, tv_teacher_lbl_manage_post, tv_teacher_attandence, tv_teacher_lbl_attandence,
            tv_teacher_events, tv_teacher_lbl_events, tv_teacher_direct, tv_teacher_lbl_direct;

    MyHeaderTextView tv_teacher_manage_post_new_post, tv_teacher_manage_post_view_post, tv_teacher_attandence_take_attandence, tv_teacher_attandence_view_monthly_report, tv_teacher_attandence_view_weekly_report, tv_teacher_events_view_calender,
            tv_teacher_direct_conversions, tv_teacher_direct_send_message;


    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    int serverResponseCode = 0;
    boolean doubleBackToExitPressedOnce = false;


    public Teacher() {
        // Required empty public constructor
    }


    public static Teacher newInstance(String param1, String param2) {
        Teacher fragment = new Teacher();
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
            rootView = inflater.inflate(R.layout.fragment_teacher_ar, container, false);


        } else {
            rootView = inflater.inflate(R.layout.fragment_teacher, container, false);


        }

        init(rootView);

        LoadDrawerMenu();

        ButterKnife.bind(this, rootView);
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        String givenDateString = "Tue Apr 25 16:08:28 GMT+05:30 2017";
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        tv_day.setText(day + "");
        tv_month.setText(getMonth(month));

        selected_month = String.valueOf(month);
        selected_year = year + "";


        try {
            Date mDate = sdf.parse(givenDateString);
            long timeInMilliseconds = mDate.getTime();
          /*  Event ev1 = new Event(Color.GREEN, timeInMilliseconds, "Some extra data that I want to store.");
            compactCalendarView.addEvent(ev1);
            System.out.println("Date in milli :: " + timeInMilliseconds);

//            int month = 8 - 1;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2017);
            calendar.set(Calendar.DAY_OF_MONTH, 05);
            calendar.set(Calendar.MONTH, 8);

            Event ev2 = new Event(Color.RED, calendar.getTimeInMillis(), "Some extra data that I want to store.");
            compactCalendarView.addEvent(ev2);


            calendar.set(Calendar.YEAR, 2017);
            calendar.set(Calendar.DAY_OF_MONTH, 8);
            calendar.set(Calendar.MONTH, 7);

            Event ev3 = new Event(Color.RED, calendar.getTimeInMillis(), "Some extra data that I want to store.");
            compactCalendarView.addEvent(ev3);

            Logger.e("calender =======> " + calendar.getTimeInMillis() + "");*/
            //1501891200000

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT

        initHeader();


        if (sharedPreferencesClass.getDEVICE_TOKEN().equalsIgnoreCase("") || sharedPreferencesClass.getDEVICE_TOKEN().length() == 0) {
            sharedPreferencesClass.setDEVICE_TOKEN("N/A");
        }
        if (sharedPreferencesClass.getDEVICE_CODE_UUID().equalsIgnoreCase("") || sharedPreferencesClass.getDEVICE_CODE_UUID().length() == 0) {
            sharedPreferencesClass.setDEVICE_CODE_UUID(UUID.randomUUID().toString() + "");
        }

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {


                Calendar calendar = Calendar.getInstance();

                calendar.setTime(dateClicked);

                Logger.e("Click To Calender" + "Day was clicked: " + dateClicked.getDate() + "");
                Logger.e("Click To Calender" + "Month was clicked: " + dateClicked.getMonth() + "");

                tv_day.setText(dateClicked.getDate() + "");

                tv_month.setText(getMonth(dateClicked.getMonth()));

                selected_month = calendar.get(Calendar.MONTH) + "";
                selected_year = calendar.get(Calendar.YEAR) + "";

                Fragment fragment = new CalendarNewFragment().newInstance(calendar.get(Calendar.MONTH) + "", calendar.get(Calendar.YEAR) + "");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(fragment.toString());

                fragmentTransaction.commit();

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Logger.e("Scroll" + "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });


        if (sharedPreferencesClass.getLanguage_Response().equalsIgnoreCase("") || sharedPreferencesClass.getLanguage_Response().length() == 0) {

        } else {
            Common.data(sharedPreferencesClass.getLanguage_Response());
        }

        setTypeface_Text();

        GetInitialWebservice();

        GetWordsWebService();

        animShow = AnimationUtils.loadAnimation(context, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(context, R.anim.view_hide);
        return rootView;
    }

    private void LoadDrawerMenu() {

        if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase(Common.USER_TYPE_TEACHER)) {

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

                MainActivity.iv_menu_bottom_left.setVisibility(View.VISIBLE);
                MainActivity.iv_menu_bottom_right.setVisibility(View.GONE);


                MenuListAdapter menuListAdapter = new MenuListAdapter(context, MainActivity.listname_ar, MainActivity.imageId);
                MainActivity.lv_menulist.setAdapter(menuListAdapter);
            } else {

                MainActivity.iv_menu_bottom_left.setVisibility(View.GONE);
                MainActivity.iv_menu_bottom_right.setVisibility(View.VISIBLE);

                MenuListAdapter menuListAdapter = new MenuListAdapter(context, MainActivity.listname, MainActivity.imageId);
                MainActivity.lv_menulist.setAdapter(menuListAdapter);
            }


            MainActivity.lv_menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String text = MainActivity.listname.get(position);
//                Toast.makeText(MainActivity.this, "selected_menu =====> " + text + "", Toast.LENGTH_SHORT).show();
                    Logger.e("10/08 selected_menu =====> " + text);

                    if (position == 0) {
                        MainActivity.drawer.closeDrawer(GravityCompat.START);
                        openFragment(new Teacher());

                    } else if (position == 1) {

                        MainActivity.drawer.closeDrawer(GravityCompat.START);

                        openFragment(new NotificationFragment());


                    } else if (position == 2) {
                        MainActivity.drawer.closeDrawer(GravityCompat.START);

                        openFragment(new CalendarNewFragment());

                    } else if (position == 3) {
                        MainActivity.drawer.closeDrawer(GravityCompat.START);

                        openFragment(new ViewPost().newInstance("news", ""));

                    } else if (position == 4) {
                        MainActivity.drawer.closeDrawer(GravityCompat.START);

                        Intent i = new Intent(context, AboutUsActivity.class);
                        startActivity(i);

                    } else if (position == 5) {
                        MainActivity.drawer.closeDrawer(GravityCompat.START);

                        Intent i = new Intent(context, ContactUsActivity.class);
                        startActivity(i);

                    } else if (position == 6) {
                        MainActivity.drawer.closeDrawer(GravityCompat.START);

                        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

                            sharedPreferencesClass.setSelected_Language(Common.Selected_Language_EN);

                        } else {
                            sharedPreferencesClass.setSelected_Language(Common.Selected_Language_AR);

                        }

                        openFragment(new Teacher());

                    } else if (position == 7) {

                        sharedPreferencesClass.setIs_Login("false");
                        sharedPreferencesClass.setSelected_Class_Id("");
                        sharedPreferencesClass.setSelected_Class_Name("");
                        sharedPreferencesClass.setSelected_Class_Name_ar("");

                        sharedPreferencesClass.setSelected_Child_Id("");
                        sharedPreferencesClass.setSelected_Child_Name("");
                        sharedPreferencesClass.setUser_Type("");

                        sharedPreferencesClass.setUSER_DETAILS("");

                        sharedPreferencesClass.setUser_NAME("");
                        sharedPreferencesClass.setUser_profile_pic("");
                        sharedPreferencesClass.setUser_qualification("");

                        Intent i = new Intent(context, FirstActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getActivity().startActivity(i);
                        getActivity().finish();
                    }

                }
            });

        } else {

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

                MainActivity.iv_menu_bottom_left.setVisibility(View.VISIBLE);
                MainActivity.iv_menu_bottom_right.setVisibility(View.GONE);


                MenuListAdapter menuListAdapter = new MenuListAdapter(context, MainActivity.listname_parents_ar, MainActivity.imageId_parents);
                MainActivity.lv_menulist.setAdapter(menuListAdapter);
            } else {

                MainActivity.iv_menu_bottom_left.setVisibility(View.GONE);
                MainActivity.iv_menu_bottom_right.setVisibility(View.VISIBLE);


                MenuListAdapter menuListAdapter = new MenuListAdapter(context, MainActivity.listname_parents, MainActivity.imageId_parents);
                MainActivity.lv_menulist.setAdapter(menuListAdapter);
            }

            MainActivity.lv_menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String text = MainActivity.listname_parents.get(position);
//                Toast.makeText(MainActivity.this, "selected_menu =====> " + text + "", Toast.LENGTH_SHORT).show();
                    Logger.e("10/08 selected_menu =====> " + text);

                    if (position == 0) {
                        MainActivity.drawer.closeDrawer(GravityCompat.START);
                        openFragment(new Teacher());

                    } else if (position == 1) {

                        MainActivity.drawer.closeDrawer(GravityCompat.START);

                        openFragment(new NotificationFragment());


                    } else if (position == 2) {
                        MainActivity.drawer.closeDrawer(GravityCompat.START);

                        openFragment(new CalendarNewFragment());


                    } else if (position == 3) {
                        MainActivity.drawer.closeDrawer(GravityCompat.START);

                        openFragment(new ViewPost().newInstance("news", ""));

                    } else if (position == 4) {
                        MainActivity.drawer.closeDrawer(GravityCompat.START);

                        openFragment(new MyKidsFragment());

                    } else if (position == 5) {
                        MainActivity.drawer.closeDrawer(GravityCompat.START);

                        Intent i = new Intent(context, AboutUsActivity.class);
                        startActivity(i);

                    } else if (position == 6) {
                        MainActivity.drawer.closeDrawer(GravityCompat.START);

                        Intent i = new Intent(context, ContactUsActivity.class);
                        startActivity(i);

                    } else if (position == 7) {
                        MainActivity.drawer.closeDrawer(GravityCompat.START);

                        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

                            sharedPreferencesClass.setSelected_Language(Common.Selected_Language_EN);

                        } else {
                            sharedPreferencesClass.setSelected_Language(Common.Selected_Language_AR);

                        }

                        openFragment(new Teacher());

                    } else if (position == 8) {

                        sharedPreferencesClass.setIs_Login("false");
                        sharedPreferencesClass.setSelected_Class_Id("");
                        sharedPreferencesClass.setSelected_Class_Name("");
                        sharedPreferencesClass.setSelected_Class_Name_ar("");

                        sharedPreferencesClass.setSelected_Child_Id("");
                        sharedPreferencesClass.setSelected_Child_Name("");
                        sharedPreferencesClass.setUser_Type("");


                        sharedPreferencesClass.setUSER_DETAILS("");

                        sharedPreferencesClass.setUser_NAME("");
                        sharedPreferencesClass.setUser_profile_pic("");
                        sharedPreferencesClass.setUser_qualification("");

                        Intent i = new Intent(context, FirstActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        getActivity().finish();
                    }

                }
            });
        }

    }


    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            MainActivity.tv_select_class.setTypeface(tf);
            tv_day.setTypeface(tf);
            tv_month.setTypeface(tf);

            tv_parent_viewpost.setTypeface(tf);
            tv_parent_lbl_viewpost.setTypeface(tf);
            tv_parents_subpost_view_posts.setTypeface(tf);
            tv_parent_attendance.setTypeface(tf);

            tv_parent_lbl_attandence.setTypeface(tf);
            tv_parent_events.setTypeface(tf);
            tv_parent_lbl_events.setTypeface(tf);
            tv_parent_direct.setTypeface(tf);


            tv_parent_lbl_direct.setTypeface(tf);
            tv_attendence_parent_sub_post_view_monthly_report.setTypeface(tf);
            tv_attendence_parent_sub_post_view_weekly_report.setTypeface(tf);
            tv_events_parent_sub_post_view_calender.setTypeface(tf);
            tv_direct_parent_sub_post_send_message.setTypeface(tf);
            tv_direct_parent_sub_post_conversion.setTypeface(tf);


            tv_teacher_manage_post.setTypeface(tf);
            tv_teacher_lbl_manage_post.setTypeface(tf);
            tv_teacher_attandence.setTypeface(tf);
            tv_teacher_lbl_attandence.setTypeface(tf);
            tv_teacher_events.setTypeface(tf);
            tv_teacher_lbl_events.setTypeface(tf);
            tv_teacher_direct.setTypeface(tf);
            tv_teacher_lbl_direct.setTypeface(tf);

            tv_teacher_manage_post_new_post.setTypeface(tf);
            tv_teacher_manage_post_view_post.setTypeface(tf);
            tv_teacher_attandence_take_attandence.setTypeface(tf);
            tv_teacher_attandence_view_monthly_report.setTypeface(tf);
            tv_teacher_attandence_view_weekly_report.setTypeface(tf);
            tv_teacher_events_view_calender.setTypeface(tf);
            tv_teacher_direct_conversions.setTypeface(tf);
            tv_teacher_direct_send_message.setTypeface(tf);


            tv_teacher_manage_post.setText(Common.filter("lbl_manage_post", "ar"));
            tv_teacher_lbl_manage_post.setText(Common.filter("lbl_manage_post_you_can_send_post_to_direct_parents", "ar"));

            tv_teacher_attandence.setText(Common.filter("lbl_attendance", "ar"));
            tv_teacher_lbl_attandence.setText(Common.filter("lbl_attendance_student_attendence_report", "ar"));

            tv_teacher_events.setText(Common.filter("lbl_events", "ar"));
            tv_teacher_lbl_events.setText(Common.filter("lbl_events_check_events_for_little_land", "ar"));

            tv_teacher_direct.setText(Common.filter("lbl_direct", "ar"));
            tv_teacher_lbl_direct.setText(Common.filter("lbl_direct_send_direct_message", "ar"));

            tv_teacher_manage_post_new_post.setText(Common.filter("lbl_new_post", "ar"));
            tv_teacher_manage_post_view_post.setText(Common.filter("lbl_view_post", "ar"));

            tv_teacher_attandence_take_attandence.setText(Common.filter("lbl_take_attendence", "ar"));
            tv_teacher_attandence_view_monthly_report.setText(Common.filter("lbl_view_monthly_report", "ar"));

            tv_teacher_attandence_view_weekly_report.setText(Common.filter("lbl_view_weekly_report", "ar"));
            tv_teacher_events_view_calender.setText(Common.filter("lbl_view_calender", "ar"));
            tv_teacher_direct_conversions.setText(Common.filter("lbl_conversions", "ar"));
            tv_teacher_direct_send_message.setText(Common.filter("lbl_send_message", "ar"));

            tv_parent_viewpost.setText(Common.filter("lbl_view_post", "ar"));
            tv_parent_lbl_viewpost.setText(Common.filter("lbl_view_post_you_can_view_post_of_childs", "ar"));

            tv_parents_subpost_view_posts.setText(Common.filter("lbl_view_posts", "ar"));
            tv_parent_attendance.setText(Common.filter("lbl_attandence", "ar"));

            tv_parent_lbl_attandence.setText(Common.filter("lbl_student_attendence_report", "ar"));

            tv_parent_events.setText(Common.filter("lbl_events", "ar"));

            tv_parent_lbl_events.setText(Common.filter("lbl_events_check_event_for_littleland", "ar"));

            tv_parent_direct.setText(Common.filter("lbl_direct", "ar"));

            tv_parent_lbl_direct.setText(Common.filter("lbl_direct_send_direct_message", "ar"));
            tv_attendence_parent_sub_post_view_monthly_report.setText(Common.filter("lbl_view_monthly_report", "ar"));
            tv_attendence_parent_sub_post_view_weekly_report.setText(Common.filter("lbl_view_weekly_report", "ar"));
            tv_events_parent_sub_post_view_calender.setText(Common.filter("lbl_view_calender", "ar"));

            tv_direct_parent_sub_post_send_message.setText(Common.filter("lbl_send_message", "ar"));
            tv_direct_parent_sub_post_conversion.setText(Common.filter("lbl_conversions", "ar"));


        } else {
            MainActivity.tv_select_class.setTypeface(tf);
            tv_day.setTypeface(tf);
            tv_month.setTypeface(tf);

            tv_parent_viewpost.setTypeface(tf);
            tv_parent_lbl_viewpost.setTypeface(tf);
            tv_parents_subpost_view_posts.setTypeface(tf);
            tv_parent_attendance.setTypeface(tf);

            tv_parent_lbl_attandence.setTypeface(tf);
            tv_parent_events.setTypeface(tf);
            tv_parent_lbl_events.setTypeface(tf);
            tv_parent_direct.setTypeface(tf);


            tv_parent_lbl_direct.setTypeface(tf);
            tv_attendence_parent_sub_post_view_monthly_report.setTypeface(tf);
            tv_attendence_parent_sub_post_view_weekly_report.setTypeface(tf);
            tv_events_parent_sub_post_view_calender.setTypeface(tf);
            tv_direct_parent_sub_post_send_message.setTypeface(tf);
            tv_direct_parent_sub_post_conversion.setTypeface(tf);


            tv_teacher_manage_post.setTypeface(tf);
            tv_teacher_lbl_manage_post.setTypeface(tf);
            tv_teacher_attandence.setTypeface(tf);
            tv_teacher_lbl_attandence.setTypeface(tf);
            tv_teacher_events.setTypeface(tf);
            tv_teacher_lbl_events.setTypeface(tf);
            tv_teacher_direct.setTypeface(tf);
            tv_teacher_lbl_direct.setTypeface(tf);

            tv_teacher_manage_post_new_post.setTypeface(tf);
            tv_teacher_manage_post_view_post.setTypeface(tf);
            tv_teacher_attandence_take_attandence.setTypeface(tf);
            tv_teacher_attandence_view_monthly_report.setTypeface(tf);
            tv_teacher_attandence_view_weekly_report.setTypeface(tf);
            tv_teacher_events_view_calender.setTypeface(tf);
            tv_teacher_direct_conversions.setTypeface(tf);
            tv_teacher_direct_send_message.setTypeface(tf);


            tv_teacher_manage_post.setText(Common.filter("lbl_manage_post", "en"));
            tv_teacher_lbl_manage_post.setText(Common.filter("lbl_manage_post_you_can_send_post_to_direct_parents", "en"));

            tv_teacher_attandence.setText(Common.filter("lbl_attendance", "en"));
            tv_teacher_lbl_attandence.setText(Common.filter("lbl_attendance_student_attendence_report", "en"));

            tv_teacher_events.setText(Common.filter("lbl_events", "en"));
            tv_teacher_lbl_events.setText(Common.filter("lbl_events_check_events_for_little_land", "en"));

            tv_teacher_direct.setText(Common.filter("lbl_direct", "en"));
            tv_teacher_lbl_direct.setText(Common.filter("lbl_direct_send_direct_message", "en"));

            tv_teacher_manage_post_new_post.setText(Common.filter("lbl_new_post", "en"));
            tv_teacher_manage_post_view_post.setText(Common.filter("lbl_view_post", "en"));

            tv_teacher_attandence_take_attandence.setText(Common.filter("lbl_take_attendence", "en"));
            tv_teacher_attandence_view_monthly_report.setText(Common.filter("lbl_view_monthly_report", "en"));

            tv_teacher_attandence_view_weekly_report.setText(Common.filter("lbl_view_weekly_report", "en"));
            tv_teacher_events_view_calender.setText(Common.filter("lbl_view_calender", "en"));
            tv_teacher_direct_conversions.setText(Common.filter("lbl_conversions", "en"));
            tv_teacher_direct_send_message.setText(Common.filter("lbl_send_message", "en"));

            tv_parent_viewpost.setText(Common.filter("lbl_view_post", "en"));
            tv_parent_lbl_viewpost.setText(Common.filter("lbl_view_post_you_can_view_post_of_childs", "en"));

            tv_parents_subpost_view_posts.setText(Common.filter("lbl_view_posts", "en"));
            tv_parent_attendance.setText(Common.filter("lbl_attandence", "en"));

            tv_parent_lbl_attandence.setText(Common.filter("lbl_student_attendence_report", "en"));

            tv_parent_events.setText(Common.filter("lbl_events", "en"));

            tv_parent_lbl_events.setText(Common.filter("lbl_events_check_event_for_littleland", "en"));

            tv_parent_direct.setText(Common.filter("lbl_direct", "en"));

            tv_parent_lbl_direct.setText(Common.filter("lbl_direct_send_direct_message", "en"));
            tv_attendence_parent_sub_post_view_monthly_report.setText(Common.filter("lbl_view_monthly_report", "en"));
            tv_attendence_parent_sub_post_view_weekly_report.setText(Common.filter("lbl_view_weekly_report", "en"));
            tv_events_parent_sub_post_view_calender.setText(Common.filter("lbl_view_calender", "en"));

            tv_direct_parent_sub_post_send_message.setText(Common.filter("lbl_send_message", "en"));
            tv_direct_parent_sub_post_conversion.setText(Common.filter("lbl_conversions", "en"));

        }

        tv_day.setTypeface(tf_numeric);
//        tv_month.setTypeface(tf_numeric);

    }

    private void init(View rootView) {

        rl_curentdate = (RelativeLayout) rootView.findViewById(R.id.rl_curentdate);
        rl_curentdate.setOnClickListener(this);

        rl_teacher = (RelativeLayout) rootView.findViewById(R.id.rl_teacher);
        rl_parents = (RelativeLayout) rootView.findViewById(R.id.rl_parents);


        ll_teacher_manage_post = (LinearLayout) rootView.findViewById(R.id.ll_teacher_manage_post);
        ll_teacher_manage_post.setOnClickListener(this);

        ll_teacher_manage_sub_post = (LinearLayout) rootView.findViewById(R.id.ll_teacher_manage_sub_post);

        ll_parent_manage_post = (LinearLayout) rootView.findViewById(R.id.ll_parent_manage_post);
        ll_parent_manage_post.setOnClickListener(this);

        ll_parent_manage_sub_post = (LinearLayout) rootView.findViewById(R.id.ll_parent_manage_sub_post);

        if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase("teacher")) {

            rl_teacher.setVisibility(View.VISIBLE);
            rl_parents.setVisibility(View.GONE);

        } else {
            rl_teacher.setVisibility(View.GONE);
            rl_parents.setVisibility(View.VISIBLE);
        }

        tv_day = (MyHeaderTextView) rootView.findViewById(R.id.tv_day);
        tv_month = (MyHeaderTextView) rootView.findViewById(R.id.tv_month);

        ll_parents_view_calender = (LinearLayout) rootView.findViewById(R.id.ll_parents_view_calender);
        ll_parents_view_calender.setOnClickListener(this);

        ll_teacher_view_calender = (LinearLayout) rootView.findViewById(R.id.ll_teacher_view_calender);
        ll_teacher_view_calender.setOnClickListener(this);

        ll_teacher_take_attendence = (LinearLayout) rootView.findViewById(R.id.ll_teacher_take_attendence);
        ll_teacher_take_attendence.setOnClickListener(this);

        ll_parents_manage_attendence_parent = (LinearLayout) rootView.findViewById(R.id.ll_parents_manage_attendence_parent);
        ll_parents_manage_attendence_parent.setOnClickListener(this);

        ll_parents_manage_attendence_sub_post = (LinearLayout) rootView.findViewById(R.id.ll_parents_manage_attendence_sub_post);

        ll_teacher_manage_attendence_parent = (LinearLayout) rootView.findViewById(R.id.ll_teacher_manage_attendence_parent);
        ll_teacher_manage_attendence_parent.setOnClickListener(this);


        ll_teacher_manage_attendence_sub_post = (LinearLayout) rootView.findViewById(R.id.ll_teacher_manage_attendence_sub_post);

        ll_teacher_event_calender_parent = (LinearLayout) rootView.findViewById(R.id.ll_teacher_event_calender_parent);
        ll_teacher_event_calender_parent.setOnClickListener(this);


        ll_parents_event_calender_parent = (LinearLayout) rootView.findViewById(R.id.ll_parents_event_calender_parent);
        ll_parents_event_calender_parent.setOnClickListener(this);


        ll_parents_event_calender_sub_post = (LinearLayout) rootView.findViewById(R.id.ll_parents_event_calender_sub_post);
        ll_teacher_event_calender_sub_post = (LinearLayout) rootView.findViewById(R.id.ll_teacher_event_calender_sub_post);

        ll_teacher_new_Post = (LinearLayout) rootView.findViewById(R.id.ll_teacher_new_Post);
        ll_teacher_new_Post.setOnClickListener(this);

        ll_parent_new_Post = (LinearLayout) rootView.findViewById(R.id.ll_parent_new_Post);
        ll_parent_new_Post.setOnClickListener(this);

        ll_parents_view_monthly_report = (LinearLayout) rootView.findViewById(R.id.ll_parents_view_monthly_report);
        ll_parents_view_monthly_report.setOnClickListener(this);

        ll_parents_view_weekly_report = (LinearLayout) rootView.findViewById(R.id.ll_parents_view_weekly_report);
        ll_parents_view_weekly_report.setOnClickListener(this);


        ll_teacher_view_monthly_report = (LinearLayout) rootView.findViewById(R.id.ll_teacher_view_monthly_report);
        ll_teacher_view_monthly_report.setOnClickListener(this);


        ll_teacher_view_weekly_report = (LinearLayout) rootView.findViewById(R.id.ll_teacher_view_weekly_report);
        ll_teacher_view_weekly_report.setOnClickListener(this);

        ll_teacher_view_post = (LinearLayout) rootView.findViewById(R.id.ll_teacher_view_post);
        ll_teacher_view_post.setOnClickListener(this);


        ll_parents_view_post = (LinearLayout) rootView.findViewById(R.id.ll_parents_view_post);
        ll_parents_view_post.setOnClickListener(this);

        ll_teacher_send_message = (LinearLayout) rootView.findViewById(R.id.ll_teacher_send_message);
        ll_teacher_send_message.setOnClickListener(this);

        ll_parents_send_message = (LinearLayout) rootView.findViewById(R.id.ll_parents_send_message);
        ll_parents_send_message.setOnClickListener(this);

        ll_teacher_conversations = (LinearLayout) rootView.findViewById(R.id.ll_teacher_conversations);
        ll_teacher_conversations.setOnClickListener(this);

        ll_parents_conversations = (LinearLayout) rootView.findViewById(R.id.ll_parents_conversations);
        ll_parents_conversations.setOnClickListener(this);

        ll_teacher_direct_message = (LinearLayout) rootView.findViewById(R.id.ll_teacher_direct_message);
        ll_teacher_direct_message.setOnClickListener(this);

        ll_teacher_direct_message_sub_post = (LinearLayout) rootView.findViewById(R.id.ll_teacher_direct_message_sub_post);


        ll_parents_direct_message = (LinearLayout) rootView.findViewById(R.id.ll_parents_direct_message);
        ll_parents_direct_message.setOnClickListener(this);

        ll_parents_direct_message_sub_post = (LinearLayout) rootView.findViewById(R.id.ll_parents_direct_message_sub_post);

        iv_parents_manage_arrow = (ImageView) rootView.findViewById(R.id.iv_parents_manage_arrow);
        iv_parents_attandence_arrow = (ImageView) rootView.findViewById(R.id.iv_parents_attandence_arrow);
        iv_parents_events_arrow = (ImageView) rootView.findViewById(R.id.iv_parents_events_arrow);
        iv_parents_direct_arrow = (ImageView) rootView.findViewById(R.id.iv_parents_direct_arrow);

        iv_teacher_manage_arrow = (ImageView) rootView.findViewById(R.id.iv_teacher_manage_arrow);
        iv_teacher_attandence_arrow = (ImageView) rootView.findViewById(R.id.iv_teacher_attandence_arrow);
        iv_teacher_events_arrow = (ImageView) rootView.findViewById(R.id.iv_teacher_events_arrow);
        iv_teacher_direct_arrow = (ImageView) rootView.findViewById(R.id.iv_teacher_direct_arrow);


        iv_user_image = (ImageView) rootView.findViewById(R.id.iv_user_image);
        tv_parents_name = (MyHeaderTextView) rootView.findViewById(R.id.tv_parents_name);
        tv_parents_educ = (MyHeaderTextView) rootView.findViewById(R.id.tv_parents_educ);


        tv_name = (MyHeaderTextView) rootView.findViewById(R.id.tv_name);
        tv_educ = (MyHeaderTextView) rootView.findViewById(R.id.tv_educ);

        imageLoader.displayImage(sharedPreferencesClass.getUser_profile_pic(), iv_user_image, options);

        imageLoader.displayImage(sharedPreferencesClass.getUser_profile_pic(), MainActivity.iv_profile, options);

        tv_parents_name.setText(sharedPreferencesClass.getUser_NAME());
        tv_parents_educ.setText(sharedPreferencesClass.getUser_qualification());

        tv_name.setText(sharedPreferencesClass.getUser_NAME());
        tv_educ.setText(sharedPreferencesClass.getUser_qualification());


        tv_parent_viewpost = (MyHeaderTextView) rootView.findViewById(R.id.tv_parent_viewpost);
        tv_parent_lbl_viewpost = (MyHeaderTextView) rootView.findViewById(R.id.tv_parent_lbl_viewpost);
        tv_parents_subpost_view_posts = (MyHeaderTextView) rootView.findViewById(R.id.tv_parents_subpost_view_posts);
        tv_parent_attendance = (MyHeaderTextView) rootView.findViewById(R.id.tv_parent_attendance);
        tv_parent_lbl_attandence = (MyHeaderTextView) rootView.findViewById(R.id.tv_parent_lbl_attandence);
        tv_parent_events = (MyHeaderTextView) rootView.findViewById(R.id.tv_parent_events);
        tv_parent_lbl_events = (MyHeaderTextView) rootView.findViewById(R.id.tv_parent_lbl_events);
        tv_parent_direct = (MyHeaderTextView) rootView.findViewById(R.id.tv_parent_direct);
        tv_parent_lbl_direct = (MyHeaderTextView) rootView.findViewById(R.id.tv_parent_lbl_direct);
        tv_attendence_parent_sub_post_view_monthly_report = (MyHeaderTextView) rootView.findViewById(R.id.tv_attendence_parent_sub_post_view_monthly_report);
        tv_attendence_parent_sub_post_view_weekly_report = (MyHeaderTextView) rootView.findViewById(R.id.tv_attendence_parent_sub_post_view_weekly_report);
        tv_events_parent_sub_post_view_calender = (MyHeaderTextView) rootView.findViewById(R.id.tv_events_parent_sub_post_view_calender);
        tv_direct_parent_sub_post_send_message = (MyHeaderTextView) rootView.findViewById(R.id.tv_direct_parent_sub_post_send_message);
        tv_direct_parent_sub_post_conversion = (MyHeaderTextView) rootView.findViewById(R.id.tv_direct_parent_sub_post_conversion);

        tv_teacher_manage_post = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_manage_post);
        tv_teacher_lbl_manage_post = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_lbl_manage_post);
        tv_teacher_attandence = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_attandence);
        tv_teacher_lbl_attandence = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_lbl_attandence);
        tv_teacher_events = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_events);

        tv_teacher_lbl_events = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_lbl_events);
        tv_teacher_direct = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_direct);
        tv_teacher_lbl_direct = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_lbl_direct);

        tv_teacher_manage_post_new_post = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_manage_post_new_post);
        tv_teacher_manage_post_view_post = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_manage_post_view_post);
        tv_teacher_attandence_take_attandence = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_attandence_take_attandence);
        tv_teacher_attandence_view_monthly_report = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_attandence_view_monthly_report);
        tv_teacher_attandence_view_weekly_report = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_attandence_view_weekly_report);
        tv_teacher_events_view_calender = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_events_view_calender);
        tv_teacher_direct_conversions = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_direct_conversions);
        tv_teacher_direct_send_message = (MyHeaderTextView) rootView.findViewById(R.id.tv_teacher_direct_send_message);


    }

    private void commonFunction() {
        context = getActivity();
        con = new ConnectionDetector(context);
        sharedPreferencesClass = new SharedPreferencesClass(context);
        customDialogClass = new CustomDialogClass((Activity) context);

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.no_image_available)
                .showImageOnFail(R.drawable.no_image_available)
                .showImageOnLoading(R.drawable.loading).build();

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

   /* @OnClick(R.id.rl_post)
    public void onClickPost() {
        Log.e("1111111111111", "==========>>");
        if (isopenpost) {
            rl_post_sub.setVisibility(View.GONE);
            isopenpost = false;
        } else {
            rl_post_sub.setVisibility(View.VISIBLE);
            isopenpost = true;
        }

//        openFragment(new Calender());


    }*/

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
        getActivity().findViewById(R.id.iv_orang).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.iv_star).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.iv_applogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.rl_spiner).setVisibility(View.VISIBLE);

        MainActivity.iv_profile_pic.setVisibility(View.GONE);


        getActivity().findViewById(R.id.iv_bell).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new NotificationFragment());
            }
        });

        if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase("teacher")) {
            if (sharedPreferencesClass.getSelected_Class_Name().equalsIgnoreCase("") || sharedPreferencesClass.getSelected_Class_Name().length() == 0) {
                MainActivity.tv_select_class.setText("Select Class");

            } else {

                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Class_Name_ar());


                } else {
                    MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Class_Name());


                }


//                MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Class_Name());

            }
        } else {
            if (sharedPreferencesClass.getSelected_Child_Name().equalsIgnoreCase("") || sharedPreferencesClass.getSelected_Child_Name().length() == 0) {
                MainActivity.tv_select_class.setText("Select Child");

            } else {
                MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Child_Name());

            }
        }


        getActivity().findViewById(R.id.rl_spiner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase("teacher")) {
                    final Dialog dialog = new Dialog(context);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before

                    if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                        dialog.setContentView(R.layout.custom_class_list_ar);

                    } else {
                        dialog.setContentView(R.layout.custom_class_list);

                    }

                    dialog.setTitle("Title...");

                    // set the custom dialog components - text, image and button

                    ListView lv_class = (ListView) dialog.findViewById(R.id.lv_class);

                    if (homeClassListDetailsArrayList.size() > 0) {

                        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, class_nameArrayList_ar);
                            lv_class.setAdapter(adapter);
                        } else {
                            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, class_nameArrayList);
                            lv_class.setAdapter(adapter);
                        }


                        lv_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                Toast.makeText(context, "Click Class Name =====> " + homeClassListDetailsArrayList.get(position).getClass_name() + "=====> Class Id========> " + homeClassListDetailsArrayList.get(position).getClass_id(), Toast.LENGTH_SHORT).show();

                                sharedPreferencesClass.setSelected_Class_Id(homeClassListDetailsArrayList.get(position).getClass_id());
                                sharedPreferencesClass.setSelected_Class_Name(homeClassListDetailsArrayList.get(position).getClass_name());
                                sharedPreferencesClass.setSelected_Class_Name_ar(homeClassListDetailsArrayList.get(position).getClass_name_ar());

                                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                                    MainActivity.tv_select_class.setText(homeClassListDetailsArrayList.get(position).getClass_name_ar());

                                } else {
                                    MainActivity.tv_select_class.setText(homeClassListDetailsArrayList.get(position).getClass_name());

                                }
//                                MainActivity.tv_select_class.setText(homeClassListDetailsArrayList.get(position).getClass_name());

                                dialog.dismiss();
                            }
                        });

                    }


                    dialog.show();
                } else if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase("parent")) {
                    final Dialog dialog = new Dialog(context);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before

                    if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                        dialog.setContentView(R.layout.dialog_custom_child_list_ar);

                    } else {
                        dialog.setContentView(R.layout.dialog_custom_child_list);

                    }

                    dialog.setTitle("Title...");

                    // set the custom dialog components - text, image and button

                    ListView lv_child = (ListView) dialog.findViewById(R.id.lv_child);

                    if (homeChildListDetailsModelArrayList.size() > 0) {

                        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, child_nameArrayList);
                            lv_child.setAdapter(adapter);
                        } else {
                            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, child_nameArrayList);
                            lv_child.setAdapter(adapter);
                        }


                        lv_child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                Toast.makeText(context, "Click Class Name =====> " + homeClassListDetailsArrayList.get(position).getClass_name() + "=====> Class Id========> " + homeClassListDetailsArrayList.get(position).getClass_id(), Toast.LENGTH_SHORT).show();

                                sharedPreferencesClass.setSelected_Child_Id(homeChildListDetailsModelArrayList.get(position).getUid());
                                sharedPreferencesClass.setSelected_Child_Name(homeChildListDetailsModelArrayList.get(position).getFname() + " " + homeChildListDetailsModelArrayList.get(position).getLname());

                                sharedPreferencesClass.setSelected_Class_Id(homeChildListDetailsModelArrayList.get(position).getClass_id());
                                sharedPreferencesClass.setSelected_Class_Name(homeChildListDetailsModelArrayList.get(position).getClass_name());
                                sharedPreferencesClass.setSelected_Class_Name_ar(homeChildListDetailsModelArrayList.get(position).getClass_name_ar());


                                MainActivity.tv_select_class.setText(homeChildListDetailsModelArrayList.get(position).getFname() + " " + homeChildListDetailsModelArrayList.get(position).getLname());

                                dialog.dismiss();
                            }
                        });

                    }


                    dialog.show();
                }
            }
        });


        MainActivity.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPermission();

            }
        });

        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);


    }

    private void setPermission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (!Common.hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, PERMISSION_ALL);
        } else {
            pickImage();

        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void pickImage() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            dialog.setContentView(R.layout.dialog_image_chooser_camera_or_gallery_ar);

        } else {
            dialog.setContentView(R.layout.dialog_image_chooser_camera_or_gallery);

        }


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        LinearLayout ll_main = (LinearLayout) dialog.findViewById(R.id.ll_main);
        ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ImageView iv_new_camera = (ImageView) dialog.findViewById(R.id.iv_new_camera);
        iv_new_camera.setVisibility(View.VISIBLE);


        ImageView iv_new_gallery = (ImageView) dialog
                .findViewById(R.id.iv_new_gallery);
        iv_new_gallery.setVisibility(View.VISIBLE);

        iv_new_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    dispatchTakePictureIntent();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*try {
                    //use standard intent to capture an image

                    Random r = new Random();
                    int i1 = r.nextInt(80 - 65) + 65;

                    String image_name = "/" + "picture" + i1 + "" + ".jpg";

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + image_name*//*"/picture.jpg"*//*;
                    File imageFile = new File(imageFilePath);
                    picUri = Uri.fromFile(imageFile); // convert path to Uri
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                    startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
                } catch (Exception e) {
                    e.printStackTrace();
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }*/

                dialog.dismiss();
            }
        });

        iv_new_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                dialog.dismiss();


            }
        });

        dialog.show();

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        selectedFilePath = "file:" + image.getAbsolutePath();
        Logger.e("12/01 mCurrentPhotoPath ----->" + selectedFilePath + "");


        return image;
    }

    private void dispatchTakePictureIntent() throws IOException {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            photoFile = createImageFile();

            Logger.e("12/01 photoFile ------>" + photoFile + "");

            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Uri photoURI = Uri.fromFile(createImageFile());
                Uri photoURI = FileProvider.getUriForFile(context,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
            }
        }
    }

  /*  @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    @Override
    public void onClick(View v) {
        if (v == ll_teacher_view_calender) {
            openFragment(new CalendarNewFragment());


        } else if (v == ll_parents_view_calender) {
            openFragment(new CalendarNewFragment());


        } else if (v == ll_teacher_take_attendence) {
            startActivity(new Intent(getActivity(), DailyAttendance.class));
            getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        } else if (v == ll_parent_manage_post) {

            if (ll_parent_manage_sub_post.isShown()) {

//                ll_parent_manage_sub_post.setVisibility(View.GONE);

//                ll_parent_manage_sub_post.startAnimation(animHide);
                ll_parent_manage_sub_post.setVisibility(View.GONE);


                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_parents_manage_arrow.setImageResource(R.drawable.message_left_aero_orange);

                } else {
                    iv_parents_manage_arrow.setImageResource(R.drawable.orange_right);

                }

//                iv_parents_manage_arrow.setImageResource(R.drawable.orange_right);


            } else {
               /* Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);

                ll_parent_manage_sub_post.setVisibility(View.VISIBLE);
                ll_parent_manage_sub_post.startAnimation(slideUp);*/


//                ll_parent_manage_sub_post.startAnimation(animShow);

                iv_parents_manage_arrow.setImageResource(R.drawable.orange_down);

                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_parents_attandence_arrow.setImageResource(R.drawable.purple_left);
                    iv_parents_events_arrow.setImageResource(R.drawable.green_left);
                    iv_parents_direct_arrow.setImageResource(R.drawable.purple_left);
                } else {
                    iv_parents_attandence_arrow.setImageResource(R.drawable.purple_right);
                    iv_parents_events_arrow.setImageResource(R.drawable.green_right);
                    iv_parents_direct_arrow.setImageResource(R.drawable.purple_right);
                }


                ll_parent_manage_sub_post.setVisibility(View.VISIBLE);


                ll_parents_manage_attendence_sub_post.setVisibility(View.GONE);
                ll_parents_event_calender_sub_post.setVisibility(View.GONE);
                ll_parents_direct_message_sub_post.setVisibility(View.GONE);


//                ll_parent_manage_sub_post.setVisibility(View.VISIBLE);

            }

        } else if (v == ll_teacher_manage_post) {
            if (ll_teacher_manage_sub_post.isShown()) {

//                ll_teacher_manage_sub_post.startAnimation(animHide);
                ll_teacher_manage_sub_post.setVisibility(View.GONE);

                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_teacher_manage_arrow.setImageResource(R.drawable.message_left_aero_orange);

                } else {
                    iv_teacher_manage_arrow.setImageResource(R.drawable.orange_right);

                }

//                iv_teacher_manage_arrow.setImageResource(R.drawable.orange_right);

//                ll_teacher_manage_sub_post.setVisibility(View.GONE);
            } else {

                ll_teacher_manage_sub_post.setVisibility(View.VISIBLE);
//                ll_teacher_manage_sub_post.startAnimation(animShow);

                iv_teacher_manage_arrow.setImageResource(R.drawable.orange_down);

                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_teacher_attandence_arrow.setImageResource(R.drawable.purple_left);
                    iv_teacher_events_arrow.setImageResource(R.drawable.green_left);
                    iv_teacher_direct_arrow.setImageResource(R.drawable.purple_left);
                } else {
                    iv_teacher_attandence_arrow.setImageResource(R.drawable.purple_right);
                    iv_teacher_events_arrow.setImageResource(R.drawable.green_right);
                    iv_teacher_direct_arrow.setImageResource(R.drawable.purple_right);
                }


                ll_teacher_manage_attendence_sub_post.setVisibility(View.GONE);
                ll_teacher_event_calender_sub_post.setVisibility(View.GONE);
                ll_teacher_direct_message_sub_post.setVisibility(View.GONE);


//                ll_teacher_manage_sub_post.setVisibility(View.VISIBLE);

            }
        } else if (v == ll_teacher_manage_attendence_parent) {
            if (ll_teacher_manage_attendence_sub_post.isShown()) {
                ll_teacher_manage_attendence_sub_post.setVisibility(View.GONE);

                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_teacher_attandence_arrow.setImageResource(R.drawable.purple_left);


                } else {
                    iv_teacher_attandence_arrow.setImageResource(R.drawable.purple_right);


                }


            } else {
                ll_teacher_manage_attendence_sub_post.setVisibility(View.VISIBLE);

                ll_teacher_manage_sub_post.setVisibility(View.GONE);
                ll_teacher_event_calender_sub_post.setVisibility(View.GONE);
                ll_teacher_direct_message_sub_post.setVisibility(View.GONE);

                iv_teacher_attandence_arrow.setImageResource(R.drawable.purple_down);

                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_teacher_manage_arrow.setImageResource(R.drawable.message_left_aero_orange);
                    iv_teacher_events_arrow.setImageResource(R.drawable.green_left);
                    iv_teacher_direct_arrow.setImageResource(R.drawable.purple_left);
                } else {
                    iv_teacher_manage_arrow.setImageResource(R.drawable.orange_right);
                    iv_teacher_events_arrow.setImageResource(R.drawable.green_right);
                    iv_teacher_direct_arrow.setImageResource(R.drawable.purple_right);
                }


            }
        } else if (v == ll_parents_manage_attendence_parent) {
            if (ll_parents_manage_attendence_sub_post.isShown()) {
                ll_parents_manage_attendence_sub_post.setVisibility(View.GONE);


                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_parents_attandence_arrow.setImageResource(R.drawable.purple_left);


                } else {
                    iv_parents_attandence_arrow.setImageResource(R.drawable.purple_right);


                }

//                iv_parents_attandence_arrow.setImageResource(R.drawable.purple_right);


            } else {
                ll_parents_manage_attendence_sub_post.setVisibility(View.VISIBLE);

                iv_parents_attandence_arrow.setImageResource(R.drawable.purple_down);


                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_parents_manage_arrow.setImageResource(R.drawable.message_left_aero_orange);
                    iv_parents_events_arrow.setImageResource(R.drawable.green_left);
                    iv_parents_direct_arrow.setImageResource(R.drawable.purple_left);

                } else {
                    iv_parents_manage_arrow.setImageResource(R.drawable.orange_right);
                    iv_parents_events_arrow.setImageResource(R.drawable.green_right);
                    iv_parents_direct_arrow.setImageResource(R.drawable.purple_right);

                }


                ll_parent_manage_sub_post.setVisibility(View.GONE);
                ll_parents_event_calender_sub_post.setVisibility(View.GONE);
                ll_parents_direct_message_sub_post.setVisibility(View.GONE);


            }
        } else if (v == ll_teacher_event_calender_parent) {
            if (ll_teacher_event_calender_sub_post.isShown()) {
                ll_teacher_event_calender_sub_post.setVisibility(View.GONE);


                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_teacher_events_arrow.setImageResource(R.drawable.green_left);


                } else {
                    iv_teacher_events_arrow.setImageResource(R.drawable.green_right);


                }


//                iv_teacher_events_arrow.setImageResource(R.drawable.green_right);


            } else {
                ll_teacher_event_calender_sub_post.setVisibility(View.VISIBLE);

                iv_teacher_events_arrow.setImageResource(R.drawable.green_down);

                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_teacher_attandence_arrow.setImageResource(R.drawable.purple_left);
                    iv_teacher_manage_arrow.setImageResource(R.drawable.message_left_aero_orange);
                    iv_teacher_direct_arrow.setImageResource(R.drawable.purple_left);

                } else {
                    iv_teacher_attandence_arrow.setImageResource(R.drawable.purple_right);
                    iv_teacher_manage_arrow.setImageResource(R.drawable.orange_right);
                    iv_teacher_direct_arrow.setImageResource(R.drawable.purple_right);

                }


                ll_teacher_manage_attendence_sub_post.setVisibility(View.GONE);
                ll_teacher_manage_sub_post.setVisibility(View.GONE);
                ll_teacher_direct_message_sub_post.setVisibility(View.GONE);


            }
        } else if (v == ll_parents_event_calender_parent) {
            if (ll_parents_event_calender_sub_post.isShown()) {
                ll_parents_event_calender_sub_post.setVisibility(View.GONE);


                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_parents_events_arrow.setImageResource(R.drawable.green_left);


                } else {
                    iv_parents_events_arrow.setImageResource(R.drawable.green_right);


                }

//                iv_parents_events_arrow.setImageResource(R.drawable.green_right);


            } else {
                ll_parents_event_calender_sub_post.setVisibility(View.VISIBLE);

                iv_parents_events_arrow.setImageResource(R.drawable.green_down);


                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_parents_attandence_arrow.setImageResource(R.drawable.purple_left);
                    iv_parents_manage_arrow.setImageResource(R.drawable.message_left_aero_orange);
                    iv_parents_direct_arrow.setImageResource(R.drawable.purple_left);
                } else {
                    iv_parents_attandence_arrow.setImageResource(R.drawable.purple_right);
                    iv_parents_manage_arrow.setImageResource(R.drawable.orange_right);
                    iv_parents_direct_arrow.setImageResource(R.drawable.purple_right);

                }


                ll_parents_manage_attendence_sub_post.setVisibility(View.GONE);
                ll_parent_manage_sub_post.setVisibility(View.GONE);
                ll_parents_direct_message_sub_post.setVisibility(View.GONE);


            }
        } else if (v == ll_teacher_new_Post) {
            openFragment(new NewPostFragment());
        } else if (v == ll_parent_new_Post) {
            openFragment(new NewPostFragment());
        } else if (v == ll_parents_view_monthly_report) {
            Intent i = new Intent(context, MonthlyAttendanceActivity.class);
            startActivity(i);
        } else if (v == ll_teacher_view_monthly_report) {
            Intent i = new Intent(context, MonthlyAttendanceActivity.class);
            startActivity(i);
        } else if (v == ll_parents_view_weekly_report) {
            Intent i = new Intent(context, WeekAttendanceActivity.class);
            startActivity(i);
        } else if (v == ll_teacher_view_weekly_report) {
            Intent i = new Intent(context, WeekAttendanceActivity.class);
            startActivity(i);
        } else if (v == ll_parents_view_post) {

            openFragment(new ViewPost().newInstance("viewpost", ""));


        } else if (v == ll_teacher_view_post) {
            openFragment(new ViewPost().newInstance("viewpost", ""));


        } else if (v == ll_teacher_send_message) {

            MessageContactListFragment.on_back = false;
            Fragment fragment = new MessageContactListFragment().newInstance("send_message", "");
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.addToBackStack(fragment.toString());

            fragmentTransaction.commit();

//            openFragment(new MessageContactListFragment());

        } else if (v == ll_parents_send_message) {

            MessageContactListFragment.on_back = false;

            Fragment fragment = new MessageContactListFragment().newInstance("send_message", "");
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.addToBackStack(fragment.toString());

            fragmentTransaction.commit();

//            openFragment(new MessageContactListFragment());

        } else if (v == ll_teacher_conversations) {
            MessageContactListFragment.on_back = false;

            openFragment(new MessageContactListFragment());

        } else if (v == ll_parents_conversations) {
            MessageContactListFragment.on_back = false;

            openFragment(new MessageContactListFragment());

        } else if (v == ll_teacher_direct_message) {
            if (ll_teacher_direct_message_sub_post.isShown()) {
                ll_teacher_direct_message_sub_post.setVisibility(View.GONE);


                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_teacher_direct_arrow.setImageResource(R.drawable.purple_left);


                } else {
                    iv_teacher_direct_arrow.setImageResource(R.drawable.purple_right);


                }

//                iv_teacher_direct_arrow.setImageResource(R.drawable.purple_right);


            } else {
                ll_teacher_direct_message_sub_post.setVisibility(View.VISIBLE);

                iv_teacher_direct_arrow.setImageResource(R.drawable.purple_down);


                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_teacher_events_arrow.setImageResource(R.drawable.green_left);
                    iv_teacher_attandence_arrow.setImageResource(R.drawable.purple_left);
                    iv_teacher_manage_arrow.setImageResource(R.drawable.message_left_aero_orange);
                } else {
                    iv_teacher_events_arrow.setImageResource(R.drawable.green_right);
                    iv_teacher_attandence_arrow.setImageResource(R.drawable.purple_right);
                    iv_teacher_manage_arrow.setImageResource(R.drawable.orange_right);

                }


                ll_teacher_manage_attendence_sub_post.setVisibility(View.GONE);
                ll_teacher_manage_sub_post.setVisibility(View.GONE);
                ll_teacher_event_calender_sub_post.setVisibility(View.GONE);


            }
        } else if (v == ll_parents_direct_message) {

            if (ll_parents_direct_message_sub_post.isShown()) {
                ll_parents_direct_message_sub_post.setVisibility(View.GONE);


                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_parents_direct_arrow.setImageResource(R.drawable.purple_left);


                } else {
                    iv_parents_direct_arrow.setImageResource(R.drawable.purple_right);


                }

//                iv_parents_direct_arrow.setImageResource(R.drawable.purple_right);


            } else {
                ll_parents_direct_message_sub_post.setVisibility(View.VISIBLE);

                iv_parents_direct_arrow.setImageResource(R.drawable.purple_down);

                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    iv_parents_events_arrow.setImageResource(R.drawable.green_left);
                    iv_parents_attandence_arrow.setImageResource(R.drawable.purple_left);
                    iv_parents_manage_arrow.setImageResource(R.drawable.message_left_aero_orange);
                } else {
                    iv_parents_events_arrow.setImageResource(R.drawable.green_right);
                    iv_parents_attandence_arrow.setImageResource(R.drawable.purple_right);
                    iv_parents_manage_arrow.setImageResource(R.drawable.orange_right);

                }


                ll_parents_manage_attendence_sub_post.setVisibility(View.GONE);
                ll_parent_manage_sub_post.setVisibility(View.GONE);
                ll_parents_event_calender_sub_post.setVisibility(View.GONE);


            }

        } else if (v == rl_curentdate) {
            Fragment fragment = new CalendarNewFragment().newInstance(selected_month, selected_year);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.addToBackStack(fragment.toString());

            fragmentTransaction.commit();
        }


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

                        sharedPreferencesClass.setUser_profile_pic(jsonObjectUser.optString("profile_pic"));


                        imageLoader.displayImage(jsonObjectUser.optString("profile_pic"), iv_user_image, options);

                        imageLoader.displayImage(jsonObjectUser.optString("profile_pic"), MainActivity.iv_profile, options);


                        sharedPreferencesClass.setUser_NAME(jsonObjectUser.optString("fname") + " " + jsonObjectUser.optString("lname"));

                        sharedPreferencesClass.setUser_qualification(jsonObjectUser.optString("qualification"));

                        tv_parents_name.setText(jsonObjectUser.optString("fname") + " " + jsonObjectUser.optString("lname"));

                        tv_parents_educ.setText(jsonObjectUser.optString("qualification"));

                        tv_name.setText(jsonObjectUser.optString("fname") + " " + jsonObjectUser.optString("lname"));

                        tv_educ.setText(jsonObjectUser.optString("qualification"));

                        sharedPreferencesClass.setGet_Initial_Days_Array(jsonObjectSuccess.optJSONArray("days") + "");

                        String str_class = jsonObjectUser.optString("class");

                        String str_childs = jsonObjectUser.optString("childs");

                        if (str_class.equalsIgnoreCase("false")) {

                        } else {
                            JSONArray jsonArrayClass = jsonObjectUser.optJSONArray("class");
                            sharedPreferencesClass.setGet_Initial_Class_Details_Array(jsonArrayClass + "");

                            if (jsonArrayClass.length() > 0) {
                                homeClassListDetailsArrayList.clear();
                                class_nameArrayList.clear();
                                class_nameArrayList_ar.clear();

                                for (int i = 0; i < jsonArrayClass.length(); i++) {
                                    JSONObject jsonObject1 = jsonArrayClass.optJSONObject(i);

//                                    "class_id":"18",
//                                            "class_name":"Blue Bells L2",
//                                            "class_name_ar":"Blue Bells L2"

                                    String class_id = jsonObject1.optString("id");
                                    String class_name = jsonObject1.optString("class_name");
                                    String class_name_ar = jsonObject1.optString("class_name_ar");

                                    HomeClassListDetails homeClassListDetails = new HomeClassListDetails();
                                    homeClassListDetails.setClass_id(class_id);
                                    homeClassListDetails.setClass_name(class_name);
                                    homeClassListDetails.setClass_name_ar(class_name_ar);
                                    homeClassListDetailsArrayList.add(homeClassListDetails);

                                    class_nameArrayList.add(class_name);
                                    class_nameArrayList_ar.add(class_name_ar);


                                }

                                if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase("teacher")) {
                                    if (sharedPreferencesClass.getSelected_Class_Name().equalsIgnoreCase("") || sharedPreferencesClass.getSelected_Class_Name().length() == 0) {

                                        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                                            MainActivity.tv_select_class.setText(homeClassListDetailsArrayList.get(0).getClass_name_ar());


                                        } else {
                                            MainActivity.tv_select_class.setText(homeClassListDetailsArrayList.get(0).getClass_name());


                                        }


//                                        MainActivity.tv_select_class.setText(homeClassListDetailsArrayList.get(0).getClass_name());

                                        sharedPreferencesClass.setSelected_Class_Id(homeClassListDetailsArrayList.get(0).getClass_id());
                                        sharedPreferencesClass.setSelected_Class_Name(homeClassListDetailsArrayList.get(0).getClass_name());
                                        sharedPreferencesClass.setSelected_Class_Name_ar(homeClassListDetailsArrayList.get(0).getClass_name_ar());


                                    } else {

                                        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                                            MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Class_Name_ar());


                                        } else {
                                            MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Class_Name());


                                        }


//                                        MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Class_Name());

                                    }
                                } else {
                                    if (sharedPreferencesClass.getSelected_Child_Name().equalsIgnoreCase("") || sharedPreferencesClass.getSelected_Child_Name().length() == 0) {

                                        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                                            MainActivity.tv_select_class.setText(Common.filter("lbl_Select_Child", "ar"));
                                        } else {
                                            MainActivity.tv_select_class.setText(Common.filter("lbl_Select_Child", "en"));

                                        }

//                                        MainActivity.tv_select_class.setText("Select Child");//lbl_Select_Child

                                    } else {
                                        MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Child_Name());

                                    }
                                }

                            } else {
                                homeClassListDetailsArrayList.clear();
                                class_nameArrayList.clear();
                                class_nameArrayList_ar.clear();


                            }

                        }


                        if (str_childs.equalsIgnoreCase("false")) {
                            sharedPreferencesClass.setGet_Initial_Child_Details_Array("false");

                        } else {
                            JSONArray jsonArrayChilds = jsonObjectUser.optJSONArray("childs");
                            sharedPreferencesClass.setGet_Initial_Child_Details_Array(jsonArrayChilds + "");

                            if (jsonArrayChilds.length() > 0) {
                                homeChildListDetailsModelArrayList.clear();
                                child_nameArrayList.clear();
                                child_nameArrayList_ar.clear();

                                for (int i = 0; i < jsonArrayChilds.length(); i++) {
                                    JSONObject jsonObject1 = jsonArrayChilds.optJSONObject(i);

                                    String uid = jsonObject1.optString("uid");
                                    String fname = jsonObject1.optString("fname");
                                    String lname = jsonObject1.optString("lname");
                                    String profile_pic = jsonObject1.optString("profile_pic");
                                    String type = jsonObject1.optString("type");
                                    String branch_id = jsonObject1.optString("branch_id");
                                    String branch_name = jsonObject1.optString("branch_name");
                                    String branch_name_ar = jsonObject1.optString("branch_name_ar");
                                    String class_id = jsonObject1.optString("class_id");
                                    String class_name = jsonObject1.optString("class_name");
                                    String class_name_ar = jsonObject1.optString("class_name_ar");

                                    HomeChildListDetailsModel homeChildListDetailsModel = new HomeChildListDetailsModel();
                                    homeChildListDetailsModel.setUid(uid);
                                    homeChildListDetailsModel.setFname(fname);
                                    homeChildListDetailsModel.setLname(lname);
                                    homeChildListDetailsModel.setProfile_pic(profile_pic);
                                    homeChildListDetailsModel.setType(type);
                                    homeChildListDetailsModel.setBranch_id(branch_id);
                                    homeChildListDetailsModel.setBranch_name(branch_name);
                                    homeChildListDetailsModel.setBranch_name_ar(branch_name_ar);
                                    homeChildListDetailsModel.setClass_id(class_id);
                                    homeChildListDetailsModel.setClass_name(class_name);
                                    homeChildListDetailsModel.setClass_name_ar(class_name_ar);

                                    homeChildListDetailsModelArrayList.add(homeChildListDetailsModel);

                                    child_nameArrayList.add(fname + " " + lname);
//                                    class_nameArrayList_ar.add(class_name_ar);


                                }

                                if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase("teacher")) {
                                    if (sharedPreferencesClass.getSelected_Class_Name().equalsIgnoreCase("") || sharedPreferencesClass.getSelected_Class_Name().length() == 0) {


                                        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                                            MainActivity.tv_select_class.setText(homeClassListDetailsArrayList.get(0).getClass_name_ar());


                                        } else {
                                            MainActivity.tv_select_class.setText(homeClassListDetailsArrayList.get(0).getClass_name());


                                        }

//                                        MainActivity.tv_select_class.setText(homeClassListDetailsArrayList.get(0).getClass_name());

                                        sharedPreferencesClass.setSelected_Class_Id(homeClassListDetailsArrayList.get(0).getClass_id());
                                        sharedPreferencesClass.setSelected_Class_Name(homeClassListDetailsArrayList.get(0).getClass_name());
                                        sharedPreferencesClass.setSelected_Class_Name_ar(homeClassListDetailsArrayList.get(0).getClass_name_ar());


                                    } else {


                                        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                                            MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Class_Name_ar());


                                        } else {
                                            MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Class_Name());


                                        }

//                                        MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Class_Name());

                                    }
                                } else {
                                    if (sharedPreferencesClass.getSelected_Child_Name().equalsIgnoreCase("") || sharedPreferencesClass.getSelected_Child_Name().length() == 0) {
                                        MainActivity.tv_select_class.setText(homeChildListDetailsModelArrayList.get(0).getFname() + " " + homeChildListDetailsModelArrayList.get(0).getLname());

                                        sharedPreferencesClass.setSelected_Child_Id(homeChildListDetailsModelArrayList.get(0).getUid());
                                        sharedPreferencesClass.setSelected_Child_Name(homeChildListDetailsModelArrayList.get(0).getFname() + " " + homeChildListDetailsModelArrayList.get(0).getLname());

                                        sharedPreferencesClass.setSelected_Class_Id(homeChildListDetailsModelArrayList.get(0).getClass_id());
                                        sharedPreferencesClass.setSelected_Class_Name(homeChildListDetailsModelArrayList.get(0).getClass_name());
                                        sharedPreferencesClass.setSelected_Class_Name_ar(homeChildListDetailsModelArrayList.get(0).getClass_name_ar());


                                    } else {
                                        MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Child_Name());

                                    }
                                }

                            } else {
                                homeChildListDetailsModelArrayList.clear();
                                child_nameArrayList.clear();
                                child_nameArrayList_ar.clear();
                            }

                        }

                        JSONArray jsonArrayDays = jsonObjectSuccess.optJSONArray("days");

                        if (jsonArrayDays.length() > 0) {
                            for (int i = 0; i < jsonArrayDays.length(); i++) {
                                String date = jsonArrayDays.optString(i);

                                //2017-08-02

                                String givenDateString = date;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date mDate = sdf.parse(givenDateString);
                                    long timeInMilliseconds = mDate.getTime();
                                    System.out.println("Date in milli :: " + timeInMilliseconds);
                                    Logger.e("Date in MilliSecond From Api =======> " + timeInMilliseconds);

                                    Event ev2 = new Event(Color.WHITE, timeInMilliseconds, "");
                                    compactCalendarView.addEvent(ev2);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.e("22/08 requestCode ===> " + requestCode + "");
        Logger.e("22/08 resultCode ===> " + resultCode + "");

        if (resultCode == Activity.RESULT_OK) {


            if (requestCode == CAMERA_CAPTURE) {


                Logger.e("12/01 CAMERA_CAPTURE mCurrentPhotoPath ----> " + selectedFilePath + "");

                Uri imageUri = Uri.parse(selectedFilePath);
                File file = new File(imageUri.getPath());

                Logger.e("12/01 imageUri getPath -----> " + imageUri.getPath() + "");

                try {
                    InputStream ims = new FileInputStream(file);
//                    ivPreview.setImageBitmap(BitmapFactory.decodeStream(ims));
                } catch (FileNotFoundException e) {
                    return;
                }

                if (file.exists()) {

                    Logger.e("12/01 file exists");
                }
//Do something
                else {
                    Logger.e("12/01 file not exists");

                    return;
                }

                selectedFilePath = imageUri.getPath();

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    Logger.e("23/08 file name ======> " + selectedFilePath);

                    Logger.e("23/08 file name ======> " + selectedFilePath);

                    InputStream ims = null;
                    try {
                        ims = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Bitmap bitmap = BitmapFactory.decodeStream(ims);

                    new Rotation_Check(bitmap, imageUri).execute();

                   /* if (con.isConnectingToInternet()) {
                        UploadProfilePicWebService uploadFileWebService = new UploadProfilePicWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }*/

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }

                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(context,
                        new String[]{imageUri.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {

                                Logger.e("12/01 onScanCompleted ------> " + "Done");

//                                Toast.makeText(context, "onScanCompleted", Toast.LENGTH_SHORT).show();

                            }
                        });


//get the Uri for the captured image
               /* Uri uri = picUri;

                if (picUri == null) {
                    //no data present
                    return;
                }

//            Toast.makeText(context, "picUri ====>" + picUri + "", Toast.LENGTH_SHORT).show();

                selectedFilePath = FilePath.getPath(context, uri);
                Logger.e("28/08 Camera ====> " + "Selected Camera File Path:" + selectedFilePath);

                File file = new File(String.valueOf(selectedFilePath));
                if (file.exists()) {


                }
//Do something
                else {
                    return;
                }

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    Logger.e("23/08 file name ======> " + selectedFilePath);



                    if (con.isConnectingToInternet()) {
                        UploadProfilePicWebService uploadFileWebService = new UploadProfilePicWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }*/
            } else if (requestCode == PIC_CROP) {
                Bundle extras = data.getExtras();
//get the cropped bitmap
                Bitmap thePic = (Bitmap) extras.get("data");

                bitmapgallery = thePic;

                if (bitmapgallery == null) {
                    // set the toast for select image
                } else {
                    image = getStringImage(bitmapgallery);
                }

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(context, selectedFileUri);
                Logger.e("23/08  ====> " + "Selected Gallery File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    if (con.isConnectingToInternet()) {
                        UploadProfilePicWebService uploadFileWebService = new UploadProfilePicWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }


                    Logger.e("23/08 file name ======> " + selectedFilePath);

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == PICK_IMAGE_REQUEST) {

                if (data == null) {
                    //no data present
                    return;
                }

                picUri = data.getData();

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(context, selectedFileUri);
                Logger.e("23/08  ====> " + "Selected Gallery File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    Bitmap myBitmap = BitmapFactory.decodeFile(selectedFilePath);

                   /* MainActivity.iv_profile.setImageBitmap(myBitmap);

                    iv_user_image.setImageBitmap(myBitmap);*/

                    if (con.isConnectingToInternet()) {
                        UploadProfilePicWebService uploadFileWebService = new UploadProfilePicWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }

                    Logger.e("23/08 file name ======> " + selectedFilePath);

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }

//            performCrop();
            }
        }
    }

    class Rotation_Check extends AsyncTask<Void, Void, Void> {

        Bitmap bitmap_new;
        Uri imageUri_new;

        public Rotation_Check(Bitmap bitmap, Uri imageUri) {

            this.bitmap_new = bitmap;
            this.imageUri_new = imageUri;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            customDialogClass.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //Record method

            try {


                Bitmap bitmap_rotate = rotateImageIfRequired(bitmap_new, context, imageUri_new);
                Logger.e("29/01 bitmap_rotate ----> " + bitmap_rotate + "");

                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(selectedFilePath);
                    bitmap_rotate.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            if (con.isConnectingToInternet()) {
                UploadProfilePicWebService uploadFileWebService = new UploadProfilePicWebService();
                uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
            }

        }
    }


    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

      /*  if (selectedImage.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {*/
        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Logger.e("orientation: %s" + orientation);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
//        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void performCrop() {
        try {
//call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
//set crop properties
            cropIntent.putExtra("crop", "true");
//indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
//indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
//retrieve data on return
            cropIntent.putExtra("return-data", true);
//start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    public String getStringImage(Bitmap bmp) {
        String encodedImage = "";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);


        return encodedImage;
    }


    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    // To animate view slide out from top to bottom
    public void slideToBottom(LinearLayout view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    // To animate view slide out from bottom to top
    public void slideToTop(LinearLayout view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, -view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    public void GetWordsWebService() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetWordsWebService Webservice Response" + result + "");

                    Common.data(result);

                    setTypeface_Text();

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
            requestMakerBg = new RequestMakerBg(resp, params, context);
            requestMakerBg.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_words);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }

    }

    class UploadProfilePicWebService extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialogClass.show();

        }

        @Override
        protected String doInBackground(String... params) {


            String method = "POST";
            String reply1 = null;
            try {

                InputStream inStream = null;
//            HttpClient client = new DefaultHttpClient();

                HttpClient client = getNewHttpClient();


                HttpResponse response = null;

                if (method == "POST") {
                    HttpPost post = new HttpPost(Common.URL_save_pic);

//                    post.setHeader("Content-type", "application/x-www-form-urlencoded");
                    post.setHeader("Accept", "application/json");
                    // post.setHeader(Servicelist.AUTHORIZATION_HEADER,Servicelist.AUTHORIZATION_VAL);

                    MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                    try {
                        reqEntity.addPart("uid", new StringBody(new String(sharedPreferencesClass.getUSER_Id())));
                        reqEntity.addPart("lan", new StringBody(new String(sharedPreferencesClass.getSelected_Language())));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        customDialogClass.dismiss();

                    }

                    try {
                        reqEntity.addPart("file", new FileBody(new File(selectedFilePath)));

                        Logger.e("30/08 my Profile pic Upload Pic params ======> " + reqEntity + "");

                        post.setEntity(reqEntity);

                        response = client.execute(post);


                    } catch (Exception e) {
                        e.printStackTrace();
                        customDialogClass.dismiss();

                    }

                    /*UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                            arguments_values, "UTF-8");
                    post.setEntity(entity);
                    response = client.execute(post);*/
                }

                inStream = response.getEntity().getContent();
                StringBuffer sb = new StringBuffer();
                int chr;
                while ((chr = inStream.read()) != -1) {
                    sb.append((char) chr);
                }
                reply1 = sb.toString();

            } catch (Exception e) {

                try {
                    customDialogClass.dismiss();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
                return null;
            }
            return reply1;

            /*String s = "";

            int i = 0;
            while (i <= 50) {
                try {
                    Thread.sleep(50);
                    publishProgress(i);
                    i++;
                } catch (Exception e) {
                    Log.i("makemachine", e.getMessage());
                }
            }

            try {
//                HttpClient httpclient = new DefaultHttpClient();

                HttpClient httpclient = getNewHttpClient();

                HttpPost httppost = new HttpPost(Common.URL_save_pic);
//            MultipartEntity reqEntity = new MultipartEntity();

                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);


                try {
                    reqEntity.addPart("uid", new StringBody(new String(sharedPreferencesClass.getUSER_Id())));
                    reqEntity.addPart("lan", new StringBody(new String(sharedPreferencesClass.getSelected_Language())));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    customDialogClass.dismiss();

                }

                try {
                    reqEntity.addPart("file", new FileBody(new File(selectedFilePath)));

                    Logger.e("30/08 my Profile pic Upload Pic params ======> " + reqEntity + "");

                    httppost.setEntity(reqEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                    customDialogClass.dismiss();

                }
                HttpResponse response = null;
                try {
                    response = httpclient.execute(httppost);
                } catch (IOException e) {
                    e.printStackTrace();
                    customDialogClass.dismiss();

                }

                HttpEntity resEntity = null;

                try {
                    resEntity = response.getEntity();
                    httpclient.getConnectionManager().shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                    customDialogClass.dismiss();

                }

                try {
                    return EntityUtils.toString(resEntity);
                } catch (IOException e) {
                    e.printStackTrace();
                    customDialogClass.dismiss();

                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
*/



         /*   HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(Common.URL_save_pic);

            HttpResponse response = null;

            try {
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                entity.addPart("file", new FileBody(new File(selectedFilePath)));
                entity.addPart("uid", new StringBody(sharedPreferencesClass.getUSER_Id()));


                httpPost.setEntity(entity);

                response=httpClient.execute(httpPost, localContext);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return String.valueOf(response);*/

//            logClass.Log(context, "Read Response", s + "");

//            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            Logger.e("23/08 progress values======>" + values.length + "");
            Logger.e("23/08 progress values======>" + values + "");

            Logger.e("23/08 progress values 1111111111======>" + (values[0] * 2) + "%" + "");
            Logger.e("23/08 progress values 2222222222======>" + values[0] + "");


         /*   _percentField.setText((values[0] * 2) + "%");
            _percentField.setTextSize(values[0]);*/

        }

        @Override
        protected void onPostExecute(String result) {
            Logger.e("23/08 file upload response ==========>" + result + "");

            if (customDialogClass.isShowing()) {
                customDialogClass.dismiss();

            }

            try {

                if (result != null) {
                    Logger.e("23/08 not null ======> " + "");
                } else {
                    return;
                }

                String myJSON = result;

                JSONObject jsonObject = new JSONObject(myJSON);

                String error = jsonObject.optString("error");

                if (error.equalsIgnoreCase("") || error.length() == 0) {

                    String success = jsonObject.optString("success");
                    Toast.makeText(context, success, Toast.LENGTH_SHORT).show();

                    JSONObject jsonObjectuser = jsonObject.optJSONObject("user");

                    String profile_pic = jsonObjectuser.optString("profile_pic");
                    sharedPreferencesClass.setUser_profile_pic(profile_pic);


                    sharedPreferencesClass.setUSER_DETAILS(jsonObjectuser + "");

                    sharedPreferencesClass.setUSER_Id(jsonObjectuser.optString("uid"));
                    sharedPreferencesClass.setUser_Type(jsonObjectuser.optString("type"));

                    imageLoader.displayImage(profile_pic, iv_user_image, options);
                    imageLoader.displayImage(profile_pic, MainActivity.iv_profile, options);


                } else {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    // handle back button

                    if (Teacher.this.doubleBackToExitPressedOnce) {
                        getActivity().finish();
                    }

                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(context, "Press again to exit", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);

                    return true;

                }

                return false;
            }
        });


    }
}
