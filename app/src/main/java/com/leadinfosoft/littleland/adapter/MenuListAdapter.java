package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Lead on 7/25/2017.
 */

public class MenuListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;

    ArrayList<String> MenuarrayList = new ArrayList<>();

    Integer[] imageId;

    SharedPreferencesClass sharedPreferencesClass;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    public MenuListAdapter(Context context, ArrayList<String> MenuarrayList, Integer[] imageId) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.MenuarrayList = MenuarrayList;
        this.imageId = imageId;

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

    @Override
    public int getCount() {
        return MenuarrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR))
        {
            convertView = layoutInflater.inflate(R.layout.menu_rowlist_ar, null);

        }
        else {
            convertView = layoutInflater.inflate(R.layout.menu_rowlist, null);

        }

        MyHeaderTextView tv_menu_name = (MyHeaderTextView) convertView.findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(MenuarrayList.get(i));

        ImageView iv_menu_image = (ImageView) convertView.findViewById(R.id.iv_menu_image);
        iv_menu_image.setImageResource(imageId[i]);

        tv_menu_name.setTypeface(tf);

        return convertView;
    }
}
