package com.leadinfosoft.littleland.utill;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.leadinfosoft.littleland.widget.MyHeaderTextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lead on 7/17/2017.
 */

public class Utils {


    public static boolean userSelectedLang(Context context)
    {
        SharedPreferencesClass sharedPreferencesClass=new SharedPreferencesClass(context);
        return sharedPreferencesClass.getUserSelectLang();

    }
    public static ArrayList<MyHeaderTextView> findAllTextView(ViewGroup viewGroup) {
        ArrayList<MyHeaderTextView> array = new ArrayList<MyHeaderTextView>();

        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup)
                findAllTextView((ViewGroup) view);
            else if (view instanceof MyHeaderTextView) {
                MyHeaderTextView edittext = (MyHeaderTextView) view;
                array.add(edittext);

            }
        }
        return array;
    }
    public static String convertDate(String dateStr){
        //2017-09-01 11:30:00
        DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fromFormat.setLenient(false);
        //2017-09-01
        DateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
        toFormat.setLenient(false);

        Date date = null;
        try {
            date = fromFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(toFormat.format(date));
        return toFormat.format(date);
    }
}
