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
import com.leadinfosoft.littleland.ModelClass.ContactListModel;
import com.leadinfosoft.littleland.ModelClass.NotificationListModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Lead on 7/25/2017.
 */

public class NotificationListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader;
    DisplayImageOptions options;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    SharedPreferencesClass sharedPreferencesClass;

    ArrayList<NotificationListModelClass> notificationListModelClassArrayList = new ArrayList<>();

    public NotificationListAdapter(Context context, ArrayList<NotificationListModelClass> notificationListModelClassArrayList) {
        this.context = context;
        this.notificationListModelClassArrayList = notificationListModelClassArrayList;
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
        return notificationListModelClassArrayList.size();
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
            convertView = layoutInflater.inflate(R.layout.custom_notification_list_ar, null);

        } else {
            convertView = layoutInflater.inflate(R.layout.custom_notification_list, null);

        }

       /* MyHeaderTextView tv_menu_name = (MyHeaderTextView) convertView.findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(MenuarrayList.get(i));*/

        CircularImageView iv_image;
        TextView tv_date;
        TextView tv_desc;
        TextView tv_title;


        iv_image = (CircularImageView) convertView.findViewById(R.id.iv_image);
        tv_date = (TextView) convertView.findViewById(R.id.tv_date);
        tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);

        tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);

        tv_title.setTypeface(tf);
        tv_date.setTypeface(tf_numeric);
        tv_desc.setTypeface(tf);

        if (notificationListModelClassArrayList.get(position).getImage().equalsIgnoreCase("") || notificationListModelClassArrayList.get(position).getImage().length() == 0) {
            iv_image.setVisibility(View.GONE);
        } else {
            iv_image.setVisibility(View.VISIBLE);

            imageLoader.displayImage(notificationListModelClassArrayList.get(position).getImage(), iv_image, options);

        }


        tv_date.setText(Common.RealTimeDateFormat(notificationListModelClassArrayList.get(position).getStamp()));
        tv_title.setText(notificationListModelClassArrayList.get(position).getTitle());

        tv_desc.setText(notificationListModelClassArrayList.get(position).getBody());

        return convertView;
    }
}
