package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.ModelClass.ContactListModel;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Lead on 7/25/2017.
 */

public class MessageContactListDetailsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;

    ArrayList<ContactListModel> contactListModelArrayList = new ArrayList<>();

    ImageLoader imageLoader;
    DisplayImageOptions options;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    SharedPreferencesClass sharedPreferencesClass;

    public MessageContactListDetailsAdapter(Context context, ArrayList<ContactListModel> contactListModelArrayList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.contactListModelArrayList = contactListModelArrayList;

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
        return contactListModelArrayList.size();
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


        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR))
        {
            convertView = layoutInflater.inflate(R.layout.custom_contact_list_ar, null);

        }
        else {
            convertView = layoutInflater.inflate(R.layout.custom_contact_list, null);

        }

       /* MyHeaderTextView tv_menu_name = (MyHeaderTextView) convertView.findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(MenuarrayList.get(i));*/

        CircularImageView iv_image;
        TextView tv_name, tv_date;
        TextView tv_desc, tv_count;

        iv_image = (CircularImageView) convertView.findViewById(R.id.iv_image);
        tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        tv_date = (TextView) convertView.findViewById(R.id.tv_date);
        tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
        tv_count = (TextView) convertView.findViewById(R.id.tv_count);

        tv_name.setTypeface(tf);
        tv_date.setTypeface(tf_numeric);
        tv_desc.setTypeface(tf);
        tv_count.setTypeface(tf_numeric);


        if (contactListModelArrayList.get(position).getUnread_count().equalsIgnoreCase("0")) {
            tv_count.setVisibility(View.GONE);

        } else {
            tv_count.setVisibility(View.VISIBLE);

        }


        imageLoader.displayImage(contactListModelArrayList.get(position).getProfile_pic(), iv_image, options);

        tv_name.setText(contactListModelArrayList.get(position).getName());
        tv_date.setText(Common.RealTimeDateFormat(contactListModelArrayList.get(position).getStamp()));
        tv_desc.setText(contactListModelArrayList.get(position).getMessage());
        tv_count.setText(contactListModelArrayList.get(position).getUnread_count());


        return convertView;
    }
}
