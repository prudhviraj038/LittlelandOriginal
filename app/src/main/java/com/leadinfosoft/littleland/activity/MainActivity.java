package com.leadinfosoft.littleland.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.leadinfosoft.littleland.ModelClass.HomeChildListDetailsModel;
import com.leadinfosoft.littleland.ModelClass.HomeClassListDetails;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.adapter.MenuListAdapter;
import com.leadinfosoft.littleland.fragment.CalendarNewFragment;
import com.leadinfosoft.littleland.fragment.Calender;
import com.leadinfosoft.littleland.fragment.Home;
import com.leadinfosoft.littleland.fragment.MyKidsFragment;
import com.leadinfosoft.littleland.fragment.NotificationFragment;
import com.leadinfosoft.littleland.fragment.SendMessageFragment;
import com.leadinfosoft.littleland.fragment.Teacher;
import com.leadinfosoft.littleland.fragment.ViewPost;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.onClick;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;


    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.mainView)
    CoordinatorLayout mainview;
    /* @BindView(R.id.drawer_layout)
     public static DrawerLayout drawer;*/
    @BindView(R.id.custom_toolbar)
    Toolbar custom_toolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_menu)
    ImageView iv_menu;


    public static ImageView iv_menu_bottom_left, iv_menu_bottom_right;

    public static DrawerLayout drawer;

    public static TextView tv_select_class;
    public static TextView tv_title, tv_subtitle;

    public static ImageView iv_profile_pic;

    public static ListView lv_menulist;

    public static ArrayList<String> listname = new ArrayList<>(Arrays.asList("Home", "NOTIFICATIONS", "EVENTS", "NEWS"
            , "ABOUT US", "CONTACT US", "عربى", "LOGOUT"));

    public static ArrayList<String> listname_ar = new ArrayList<>(Arrays.asList("الصفحة الرئيسية", "إخطارات", "هدف", "أخبار"
            , "معلومات عنا", "اتصل بنا", "ENGLISH", "الخروج"));

    public static Integer[] imageId = {
            R.drawable.ic_sidemenu_home,
            R.drawable.ic_sidemenu_notification,
            R.drawable.ic_sidemenu_events,
            R.drawable.ic_sidemenu_news,
            R.drawable.ic_sidemenu_about_us,
            R.drawable.ic_sidemenu_contact,
            R.drawable.ic_sidemenu_language,
            R.drawable.ic_sidemenu_logout

    };

    public static ArrayList<String> listname_parents = new ArrayList<>(Arrays.asList("HOME", "NOTIFICATIONS", "EVENTS", "NEWS", "MY KIDS"
            , "ABOUT US", "CONTACT US", "عربى", "LOGOUT"));

    public static ArrayList<String> listname_parents_ar = new ArrayList<>(Arrays.asList("الصفحة الرئيسية", "إخطارات", "هدف", "أخبار", "أطفالي"
            , "معلومات عنا", "اتصل بنا", "ENGLISH", "الخروج"));

    public static Integer[] imageId_parents = {
            R.drawable.ic_sidemenu_home,
            R.drawable.ic_sidemenu_notification,
            R.drawable.ic_sidemenu_events,
            R.drawable.ic_sidemenu_news,
            R.drawable.sidemenu_childs,
            R.drawable.ic_sidemenu_about_us,
            R.drawable.ic_sidemenu_contact,
            R.drawable.ic_sidemenu_language,
            R.drawable.ic_sidemenu_logout

    };

    Context context;
    SharedPreferencesClass sharedPreferencesClass;
    ConnectionDetector con;
    CustomDialogClass customDialogClass;

    Response_string<String> resp;
    RequestMaker req;

    ImageLoader imageLoader;
    DisplayImageOptions options;

    public static ImageView iv_profile;

    public static HashMap<String, String> englishmap = new HashMap<>();
    public static HashMap<String, String> arabicmap = new HashMap<>();

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommonFunction();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        if (sharedPreferencesClass.getLanguage_Response().equalsIgnoreCase("") || sharedPreferencesClass.getLanguage_Response().length() == 0) {

        } else {
            Common.setLanguagedefault(context, sharedPreferencesClass.getLanguage_Response());
        }

        initHeader();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        lv_menulist = (ListView) findViewById(R.id.lv_menulist);
        iv_profile = (ImageView) findViewById(R.id.iv_profile);

        iv_menu_bottom_left = (ImageView) findViewById(R.id.iv_menu_bottom_left);
        iv_menu_bottom_right = (ImageView) findViewById(R.id.iv_menu_bottom_right);


        if (sharedPreferencesClass.getDEVICE_TOKEN().equalsIgnoreCase("") || sharedPreferencesClass.getDEVICE_TOKEN().length() == 0) {
            sharedPreferencesClass.setDEVICE_TOKEN("N/A");
        }
        if (sharedPreferencesClass.getDEVICE_CODE_UUID().equalsIgnoreCase("") || sharedPreferencesClass.getDEVICE_CODE_UUID().length() == 0) {
            sharedPreferencesClass.setDEVICE_CODE_UUID(UUID.randomUUID().toString() + "");
        }

        if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase("") || sharedPreferencesClass.getUser_Type().length() == 0) {

            GetInitialWebservice();


        } else if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase(Common.USER_TYPE_TEACHER)) {

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {


                iv_menu_bottom_left.setVisibility(View.VISIBLE);
                iv_menu_bottom_right.setVisibility(View.GONE);

                MenuListAdapter menuListAdapter = new MenuListAdapter(context, listname_ar, imageId);
                lv_menulist.setAdapter(menuListAdapter);
            } else {

                iv_menu_bottom_left.setVisibility(View.GONE);
                iv_menu_bottom_right.setVisibility(View.VISIBLE);

                MenuListAdapter menuListAdapter = new MenuListAdapter(context, listname, imageId);
                lv_menulist.setAdapter(menuListAdapter);
            }


            lv_menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String text = listname.get(position);
//                Toast.makeText(MainActivity.this, "selected_menu =====> " + text + "", Toast.LENGTH_SHORT).show();
                    Logger.e("10/08 selected_menu =====> " + text);

                    if (position == 0) {
                        drawer.closeDrawer(GravityCompat.START);
                        openFragment(new Teacher());

                    } else if (position == 1) {

                        drawer.closeDrawer(GravityCompat.START);

                        openFragment(new NotificationFragment());


                    } else if (position == 2) {
                        drawer.closeDrawer(GravityCompat.START);

                        openFragment(new CalendarNewFragment());

                    } else if (position == 3) {
                        drawer.closeDrawer(GravityCompat.START);

                        openFragment(new ViewPost().newInstance("news", ""));

                    } else if (position == 4) {
                        drawer.closeDrawer(GravityCompat.START);

                        Intent i = new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(i);

                    } else if (position == 5) {
                        drawer.closeDrawer(GravityCompat.START);

                        Intent i = new Intent(MainActivity.this, ContactUsActivity.class);
                        startActivity(i);

                    } else if (position == 6) {
                        drawer.closeDrawer(GravityCompat.START);

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
                        sharedPreferencesClass.setSelected_Child_Id("");
                        sharedPreferencesClass.setSelected_Child_Name("");
                        sharedPreferencesClass.setUser_Type("");

                        sharedPreferencesClass.setUSER_DETAILS("");

                        sharedPreferencesClass.setUser_NAME("");
                        sharedPreferencesClass.setUser_profile_pic("");
                        sharedPreferencesClass.setUser_qualification("");

                        Intent i = new Intent(MainActivity.this, FirstActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }

                }
            });

        } else {

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

                iv_menu_bottom_left.setVisibility(View.VISIBLE);
                iv_menu_bottom_right.setVisibility(View.GONE);

                MenuListAdapter menuListAdapter = new MenuListAdapter(context, listname_parents_ar, imageId_parents);
                lv_menulist.setAdapter(menuListAdapter);
            } else {


                iv_menu_bottom_left.setVisibility(View.GONE);
                iv_menu_bottom_right.setVisibility(View.VISIBLE);

                MenuListAdapter menuListAdapter = new MenuListAdapter(context, listname_parents, imageId_parents);
                lv_menulist.setAdapter(menuListAdapter);
            }

            lv_menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String text = listname_parents.get(position);
//                Toast.makeText(MainActivity.this, "selected_menu =====> " + text + "", Toast.LENGTH_SHORT).show();
                    Logger.e("10/08 selected_menu =====> " + text);

                    if (position == 0) {
                        drawer.closeDrawer(GravityCompat.START);
                        openFragment(new Teacher());

                    } else if (position == 1) {

                        drawer.closeDrawer(GravityCompat.START);

                        openFragment(new NotificationFragment());


                    } else if (position == 2) {
                        drawer.closeDrawer(GravityCompat.START);

                        openFragment(new CalendarNewFragment());


                    } else if (position == 3) {
                        drawer.closeDrawer(GravityCompat.START);

                        openFragment(new ViewPost().newInstance("news", ""));

                    } else if (position == 4) {
                        drawer.closeDrawer(GravityCompat.START);

                        openFragment(new MyKidsFragment());

                    } else if (position == 5) {
                        drawer.closeDrawer(GravityCompat.START);

                        Intent i = new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(i);

                    } else if (position == 6) {
                        drawer.closeDrawer(GravityCompat.START);

                        Intent i = new Intent(MainActivity.this, ContactUsActivity.class);
                        startActivity(i);

                    } else if (position == 7) {
                        drawer.closeDrawer(GravityCompat.START);

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
                        sharedPreferencesClass.setSelected_Child_Id("");
                        sharedPreferencesClass.setSelected_Child_Name("");
                        sharedPreferencesClass.setUser_Type("");


                        sharedPreferencesClass.setUSER_DETAILS("");

                        sharedPreferencesClass.setUser_NAME("");
                        sharedPreferencesClass.setUser_profile_pic("");
                        sharedPreferencesClass.setUser_qualification("");

                        Intent i = new Intent(MainActivity.this, FirstActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }

                }
            });
        }


        if (drawer != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                public void onDrawerClosed(View view) {
                    supportInvalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    supportInvalidateOptionsMenu();
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, slideOffset);
                    mainview.setTranslationX(+slideOffset * drawerView.getWidth());
                    drawer.bringChildToFront(drawerView);
                    drawer.requestLayout();
                }
            };

            drawer.setDrawerListener(toggle);
            toggle.syncState();
            navigationView.setItemIconTintList(null);

        }
//        openFragment(new Home());


        Intent intent = getIntent();

        String fromnotification = "";
        fromnotification = intent.getStringExtra("fromnotification");

        Logger.e("28/08 Notification mainactivity fromnotification ======> " + fromnotification + "");


        String msg_type = "";
        msg_type = intent.getStringExtra("msg_type");

        Logger.e("28/08 Notification mainactivity msg_type ======> " + msg_type + "");


        if (fromnotification == null || fromnotification.equalsIgnoreCase("") || fromnotification.length() == 0) {
            openFragment(new Teacher());
        } else {
            try {
                JSONObject jsonObjectNotificationDetails = new JSONObject(msg_type);

                Logger.e("28/08 Notification mainactivity jsonObjectNotificationDetails ======> " + jsonObjectNotificationDetails + "");

                openFragment(new Teacher());

                if (jsonObjectNotificationDetails != null) {
                    String type = jsonObjectNotificationDetails.optString("msg_type");

                    if (type.equalsIgnoreCase("general")) {

                        openFragment(new Teacher());


                    } else if (type.equalsIgnoreCase("post")) {

                        Intent i = new Intent(context, ViewPostDetail.class);
                        i.putExtra("post_id", jsonObjectNotificationDetails.optString("ref_id"));
                        startActivity(i);

                        MainActivity.this.overridePendingTransition(0, 0);


//                        MainActivity.this.overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

                    } else if (type.equalsIgnoreCase("direct_message")) {

                        String to_uid = jsonObjectNotificationDetails.optString("from_uid");

                        Logger.e("28/08 notification direct_message ====>");

                        Logger.e("28/08 notification direct_message to_uid====>" + to_uid);


                        String to_name = "";

                        Fragment fragment = new SendMessageFragment().newInstance(to_uid, to_name);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(fragment.toString());

                        fragmentTransaction.commit();
                    } else if (type.equalsIgnoreCase("attendance")) {
                        openFragment(new NotificationFragment());
                    }
                } else {
                    Toast.makeText(context, Common.Technical_Problem, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        setTypeface_Text();

    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            tv_title.setTypeface(tf);
            tv_subtitle.setTypeface(tf);
            tv_select_class.setTypeface(tf);


        } else {
            tv_title.setTypeface(tf);
            tv_subtitle.setTypeface(tf);
            tv_select_class.setTypeface(tf);
        }

    }

    private void initHeader() {

        tv_select_class = (TextView) findViewById(R.id.tv_select_class);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);

        iv_profile_pic = (ImageView) findViewById(R.id.iv_profile_pic);
        iv_profile_pic.setVisibility(View.GONE);
    }

    @OnClick(R.id.iv_menu)
    public void onClickMenu() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    private void CommonFunction() {
        context = getApplicationContext();
        con = new ConnectionDetector(context);
        sharedPreferencesClass = new SharedPreferencesClass(context);
        customDialogClass = new CustomDialogClass(MainActivity.this);

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.no_image_available)
                .showImageOnFail(R.drawable.no_image_available)
                .showImageOnLoading(R.drawable.loading).build();

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

    private void openFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTrasaction = fm.beginTransaction();
        fragmentTrasaction.replace(R.id.frame, fragment);
        fragmentTrasaction.addToBackStack(fragment.toString());
        fragmentTrasaction.commit();
    }

   /* @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

            int Count = getSupportFragmentManager().getBackStackEntryCount();
//            List<Fragment> fragmentList= getSupportFragmentManager().getFragment();

            Fragment fragmentList = getSupportFragmentManager().getFragments().get(Count - 2);
            String fragmentName = fragmentList.getClass().getSimpleName();
//            Debug.e("fragmentName","fragmentName:"+fragmentName);
            getSupportFragmentManager().popBackStack();


        } else {

//            Debug.e("doubleBackToExitPressedOnce","====>>");
            if (this.doubleBackToExitPressedOnce) {
                finish();
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }
    }*/

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

                        imageLoader.displayImage(jsonObjectUser.optString("profile_pic"), MainActivity.iv_profile, options);

                        sharedPreferencesClass.setGet_Initial_Days_Array(jsonObjectSuccess.optJSONArray("days") + "");

                        if (jsonObjectUser.optString("type").equalsIgnoreCase(Common.USER_TYPE_TEACHER)) {


                            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

                                iv_menu_bottom_left.setVisibility(View.VISIBLE);
                                iv_menu_bottom_right.setVisibility(View.GONE);

                                MenuListAdapter menuListAdapter = new MenuListAdapter(context, listname_ar, imageId);
                                lv_menulist.setAdapter(menuListAdapter);
                            } else {


                                iv_menu_bottom_left.setVisibility(View.GONE);
                                iv_menu_bottom_right.setVisibility(View.VISIBLE);

                                MenuListAdapter menuListAdapter = new MenuListAdapter(context, listname, imageId);
                                lv_menulist.setAdapter(menuListAdapter);
                            }


                            lv_menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String text = listname.get(position);
//                Toast.makeText(MainActivity.this, "selected_menu =====> " + text + "", Toast.LENGTH_SHORT).show();
                                    Logger.e("10/08 selected_menu =====> " + text);

                                    if (position == 0) {
                                        drawer.closeDrawer(GravityCompat.START);
                                        openFragment(new Teacher());

                                    } else if (position == 1) {

                                        drawer.closeDrawer(GravityCompat.START);

                                        openFragment(new NotificationFragment());


                                    } else if (position == 2) {
                                        drawer.closeDrawer(GravityCompat.START);

                                        openFragment(new CalendarNewFragment());


                                    } else if (position == 3) {
                                        drawer.closeDrawer(GravityCompat.START);

                                        openFragment(new ViewPost().newInstance("news", ""));

                                    } else if (position == 4) {
                                        drawer.closeDrawer(GravityCompat.START);

                                        Intent i = new Intent(MainActivity.this, AboutUsActivity.class);
                                        startActivity(i);

                                    } else if (position == 5) {
                                        drawer.closeDrawer(GravityCompat.START);

                                        Intent i = new Intent(MainActivity.this, ContactUsActivity.class);
                                        startActivity(i);

                                    } else if (position == 6) {
                                        drawer.closeDrawer(GravityCompat.START);

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
                                        sharedPreferencesClass.setSelected_Child_Id("");
                                        sharedPreferencesClass.setSelected_Child_Name("");
                                        sharedPreferencesClass.setUser_Type("");

                                        sharedPreferencesClass.setUSER_DETAILS("");

                                        sharedPreferencesClass.setUser_NAME("");
                                        sharedPreferencesClass.setUser_profile_pic("");
                                        sharedPreferencesClass.setUser_qualification("");

                                        Intent i = new Intent(MainActivity.this, FirstActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        finish();
                                    }

                                }
                            });


                        } else {
                            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

                                iv_menu_bottom_left.setVisibility(View.VISIBLE);
                                iv_menu_bottom_right.setVisibility(View.GONE);

                                MenuListAdapter menuListAdapter = new MenuListAdapter(context, listname_parents_ar, imageId_parents);
                                lv_menulist.setAdapter(menuListAdapter);
                            } else {


                                iv_menu_bottom_left.setVisibility(View.GONE);
                                iv_menu_bottom_right.setVisibility(View.VISIBLE);

                                MenuListAdapter menuListAdapter = new MenuListAdapter(context, listname_parents, imageId_parents);
                                lv_menulist.setAdapter(menuListAdapter);
                            }

                            lv_menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String text = listname_parents.get(position);
//                Toast.makeText(MainActivity.this, "selected_menu =====> " + text + "", Toast.LENGTH_SHORT).show();
                                    Logger.e("10/08 selected_menu =====> " + text);

                                    if (position == 0) {
                                        drawer.closeDrawer(GravityCompat.START);
                                        openFragment(new Teacher());

                                    } else if (position == 1) {

                                        drawer.closeDrawer(GravityCompat.START);

                                        openFragment(new NotificationFragment());


                                    } else if (position == 2) {
                                        drawer.closeDrawer(GravityCompat.START);

                                        openFragment(new CalendarNewFragment());


                                    } else if (position == 3) {
                                        drawer.closeDrawer(GravityCompat.START);

                                        openFragment(new ViewPost().newInstance("news", ""));

                                    } else if (position == 4) {
                                        drawer.closeDrawer(GravityCompat.START);

                                        openFragment(new MyKidsFragment());

                                    } else if (position == 5) {
                                        drawer.closeDrawer(GravityCompat.START);

                                        Intent i = new Intent(MainActivity.this, AboutUsActivity.class);
                                        startActivity(i);

                                    } else if (position == 6) {
                                        drawer.closeDrawer(GravityCompat.START);

                                        Intent i = new Intent(MainActivity.this, ContactUsActivity.class);
                                        startActivity(i);

                                    } else if (position == 7) {
                                        drawer.closeDrawer(GravityCompat.START);

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
                                        sharedPreferencesClass.setSelected_Child_Id("");
                                        sharedPreferencesClass.setSelected_Child_Name("");
                                        sharedPreferencesClass.setUser_Type("");


                                        sharedPreferencesClass.setUSER_DETAILS("");

                                        sharedPreferencesClass.setUser_NAME("");
                                        sharedPreferencesClass.setUser_profile_pic("");
                                        sharedPreferencesClass.setUser_qualification("");

                                        Intent i = new Intent(MainActivity.this, FirstActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        finish();
                                    }

                                }
                            });
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
            req = new RequestMaker(resp, params, MainActivity.this);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_Get_Initial);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }

    public static void data(String result) {
        try {
            JSONObject rootjsonarr = new JSONObject(result.toString());
            JSONObject en = rootjsonarr.getJSONObject("en");
            JSONObject ar = rootjsonarr.getJSONObject("ar");

            MainActivity.englishmap.clear();
            MainActivity.arabicmap.clear();

            Iterator<String> iter = en.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                //Log.d("key",key);
                try {
                    Object value = en.get(key);
                    //Log.d("value", String.valueOf(value));

                    MainActivity.englishmap.put(key, String.valueOf(value));
                } catch (JSONException e) {
                    // Something went wrong!
                }
            }

            Iterator<String> iterar = ar.keys();
            while (iterar.hasNext()) {
                String key = iterar.next();
                //Log.d("key",key);
                try {
                    Object value = ar.get(key);
                    //Log.d("value", String.valueOf(value));

                    MainActivity.arabicmap.put(key, String.valueOf(value));
                } catch (JSONException e) {
                    // Something went wrong!
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String filter(String key, String language) {

        String value = "";
        if (language.equalsIgnoreCase("en")) {
            value = englishmap.get(key);
        } else {
            value = arabicmap.get(key);
        }
        Log.d("Keyvalue", "Key:- " + key + " Value:- " + value);


        if (value == null || value.equalsIgnoreCase("") || value.length() == 0) {
            value = key;
        }
        return value;
    }
}

