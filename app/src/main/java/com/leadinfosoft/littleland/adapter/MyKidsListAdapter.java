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
import com.leadinfosoft.littleland.ModelClass.MyKidsListModelClass;
import com.leadinfosoft.littleland.ModelClass.NotificationListModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Lead on 7/25/2017.
 */

public class MyKidsListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader;
    DisplayImageOptions options;

    ArrayList<MyKidsListModelClass> myKidsListModelClassArrayList = new ArrayList<>();

    OnKidsImageListener mListener;

    SharedPreferencesClass sharedPreferencesClass;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    public MyKidsListAdapter(Context context, ArrayList<MyKidsListModelClass> myKidsListModelClassArrayList) {
        this.context = context;
        this.myKidsListModelClassArrayList = myKidsListModelClassArrayList;
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

    public interface OnKidsImageListener {
        void onEvent(int position, String uid);
    }

    public void setCustomEventListener(OnKidsImageListener eventListener) {
        mListener = eventListener;
    }

    @Override
    public int getCount() {
        return myKidsListModelClassArrayList.size();
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {


        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            convertView = layoutInflater.inflate(R.layout.custom_notification_list_ar, null);

        } else {
            convertView = layoutInflater.inflate(R.layout.custom_notification_list, null);

        }

       /* MyHeaderTextView tv_menu_name = (MyHeaderTextView) convertView.findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(MenuarrayList.get(i));*/

        CircularImageView iv_image;
        TextView tv_date;
        TextView tv_title;
        TextView tv_desc;

        iv_image = (CircularImageView) convertView.findViewById(R.id.iv_image);
        tv_date = (TextView) convertView.findViewById(R.id.tv_date);
        tv_date.setVisibility(View.GONE);
        tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
        tv_desc.setVisibility(View.GONE);


        tv_title.setTypeface(tf);
        tv_desc.setTypeface(tf);
        tv_date.setTypeface(tf_numeric);


        imageLoader.displayImage(myKidsListModelClassArrayList.get(position).getProfile_pic(), iv_image, options);


        tv_date.setText(Common.RealTimeDateFormat(myKidsListModelClassArrayList.get(position).getUid()));

        tv_title.setText(myKidsListModelClassArrayList.get(position).getName());

        tv_desc.setText(myKidsListModelClassArrayList.get(position).getName());

        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onEvent(position, myKidsListModelClassArrayList.get(position).getUid());
            }
        });

        return convertView;
    }
}
