package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.ModelClass.NotificationListModelClass;
import com.leadinfosoft.littleland.ModelClass.ViewOpinionModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Lead on 7/25/2017.
 */

public class ViewOpinionListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader;
    DisplayImageOptions options;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    SharedPreferencesClass sharedPreferencesClass;


    ArrayList<ViewOpinionModelClass> viewOpinionModelClassArrayList = new ArrayList<>();

    public ViewOpinionListAdapter(Context context, ArrayList<ViewOpinionModelClass> viewOpinionModelClassArrayList) {
        this.context = context;
        this.viewOpinionModelClassArrayList = viewOpinionModelClassArrayList;
        layoutInflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.no_image_available)
                .showImageOnFail(R.drawable.no_image_available)
                .showImageOnLoading(R.drawable.loading).build();

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
        return viewOpinionModelClassArrayList.size();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            convertView = layoutInflater.inflate(R.layout.custom_view_opinion_list_ar, null);

        } else {
            convertView = layoutInflater.inflate(R.layout.custom_view_opinion_list, null);

        }

       /* MyHeaderTextView tv_menu_name = (MyHeaderTextView) convertView.findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(MenuarrayList.get(i));*/

        CircularImageView iv_image;
        TextView tv_name, tv_child_name, tv_time;
        TextView tv_desc;

        iv_image = (CircularImageView) convertView.findViewById(R.id.iv_image);

        tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        tv_child_name = (TextView) convertView.findViewById(R.id.tv_child_name);
        tv_time = (TextView) convertView.findViewById(R.id.tv_time);

        tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);

        tv_name.setTypeface(tf);
        tv_child_name.setTypeface(tf);
        tv_time.setTypeface(tf);
        tv_desc.setTypeface(tf);


        imageLoader.displayImage(viewOpinionModelClassArrayList.get(position).getProfile_pic(), iv_image, options);
        tv_name.setText(viewOpinionModelClassArrayList.get(position).getName());
        tv_child_name.setText(viewOpinionModelClassArrayList.get(position).getChild_name());
        tv_time.setText(Common.RealTimeDateFormat(viewOpinionModelClassArrayList.get(position).getStamp()));

        tv_desc.setText(viewOpinionModelClassArrayList.get(position).getRemark());

        return convertView;
    }
}
