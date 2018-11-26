package com.leadinfosoft.littleland.Common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.leadinfosoft.littleland.utill.SharedPreferencesClass;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by admin on 2/25/2017.
 */

public class Common {


    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static String Device_Type = "0";

    public static Boolean IS_DEBUG = false;

    public static String TAG = "LittleLandTag";

    public static String USER_TYPE_TEACHER = "teacher";
    public static String USER_TYPE_PARENT = "parent";

    public static String InternetConnection = "Please Check Your Internet Connection";

    public static String Technical_Problem = "Some Technical Problem Occure";

    public static String Selected_Language_EN = "en";
    public static String Selected_Language_AR = "ar";

//    public static final String URL_BASE = "http://server.mywebdemo.in/nurcery/";

    //https://dashboard.littlelandapp.com/api/doc.php

    public static final String URL_BASE = "https://dashboard.littlelandapp.com/";

    public static final String URL_HOST = URL_BASE + "api/";

    public static final String URL_Login_User = URL_HOST + "login_user.php";

    public static final String URL_Get_Initial = URL_HOST + "get_initial.php";

    public static final String URL_Event_Action = URL_HOST + "event_action.php";

    public static final String URL_attendance_daily = URL_HOST + "attendance_daily.php";

    public static final String URL_New_Post_Or_Direct_Message = URL_HOST + "post_cms.php";

    public static final String URL_Get_Data = URL_HOST + "get_data.php";

    public static final String URL_Upload_File = URL_HOST + "upload_file.php";

    public static final String URL_message_cms = URL_HOST + "message_cms.php";

    public static final String URL_attendance_report = URL_HOST + "attendance_report.php";

    public static final String URL_notifications = URL_HOST + "notifications.php";

    public static final String URL_attendance_reason = URL_HOST + "attendance_reason.php";

    public static final String URL_opinion_cms = URL_HOST + "opinion_cms.php";

    public static final String URL_contact_us = URL_HOST + "contact_us.php";

    public static final String URL_get_data = URL_HOST + "get_data.php";

    public static final String URL_save_pic = URL_HOST + "save_pic.php";

    public static final String URL_words = URL_HOST + "words.php";

    public static final String URL_video_player = URL_HOST + "video_player.php?video=";


//    http://dashboard.littlelandapp.com/api/video_player.php?video=

    public static final String Url_Static_about_us = URL_BASE + "static/about_us_en.html";
    public static final String Url_Static_about_us_ar = URL_BASE + "static/about_us_ar.html";


    public static HashMap<String, String> englishmap = new HashMap<>();
    public static HashMap<String, String> arabicmap = new HashMap<>();


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String changeDateFormat_yyyy_mm_dd(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..." + newFormat);

        return newFormat;

    }

    public static String changeDateFormat_yyyy_mm_dd_hh_mm_ss(String date) {

//        2017-08-28 16:12:42
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..." + newFormat);

        return newFormat;

    }

    public static String ConvertToDecimal(String value) {

        double angle = Double.parseDouble(value);
        DecimalFormat df = new DecimalFormat("#.000");
        String angleFormated = df.format(angle);
        System.out.println(angleFormated); //output 20.30

        return angleFormated;

    }

    public static String Current_Date_Time() {

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM--dd hh:mm:ss");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String s = dateFormatter.format(today);

        return s;

    }

    public static String Current_Date() {

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String s = dateFormatter.format(today);

        return s;

    }

    public static String RealTimeDateFormat(String datestring) {
        Calendar endDate = Calendar.getInstance();
        /*SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        cal.setTime(sdf.parse("Mon Mar 14 16:02:37 GMT 2011"));*/
        //2017-2-22 15:10:33
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        try {
//            endDate = formatter.parse(datestring);
            endDate.setTime(formatter.parse(datestring));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //milliseconds
        long different = Calendar.getInstance().getTimeInMillis() - endDate.getTimeInMillis();

        System.out.println("startDate : " + Calendar.getInstance().getTime());
        System.out.println("endDate : " + endDate.getTime());
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        Logger.e("Date Diff =====> " + String.format("%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds));
        if (elapsedDays > 0) {
            if (elapsedDays == 1) {
                return elapsedDays + " day ago";
            } else {
                return elapsedDays + " days ago";
            }
        } else if (elapsedHours > 0) {
            if (elapsedHours == 1) {
                return elapsedHours + " hr ago";
            } else {
                return elapsedHours + " hrs ago";
            }

        } else if (elapsedMinutes > 0) {
            if (elapsedMinutes == 1) {
                return elapsedMinutes + " min ago";
            } else {
                return elapsedMinutes + " mins ago";
            }
        } else {
            return "Now";
        }

    }

    public static String GetDayofMonth(String str_date) {
        String input_date_string = str_date;
        String dayFromDate = "";
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date date2;
        try {
            date2 = dateformat.parse(input_date_string);
            DateFormat dayFormate = new SimpleDateFormat("EEEE");
            dayFromDate = dayFormate.format(date2);
            Log.d("asd", "----------:: " + dayFromDate);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dayFromDate;

    }

    public static void setLanguagedefault(Context context, String result) {

        Common.data(result);

    }

    public static void data(String result) {
        try {
            JSONObject rootjsonarr = new JSONObject(result.toString());
            JSONObject en = rootjsonarr.getJSONObject("en");
            JSONObject ar = rootjsonarr.getJSONObject("ar");

            englishmap.clear();
            arabicmap.clear();

            Iterator<String> iter = en.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                //Log.d("key",key);
                try {
                    Object value = en.get(key);
                    //Log.d("value", String.valueOf(value));

                    englishmap.put(key, String.valueOf(value));
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

                    arabicmap.put(key, String.valueOf(value));
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

    public static HttpClient getNewHttpClient() {
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
}
