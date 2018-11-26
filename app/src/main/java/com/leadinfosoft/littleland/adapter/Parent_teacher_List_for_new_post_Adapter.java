package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.ModelClass.GetParentsDetailsForParticularClassid_NewPost;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Lead on 7/25/2017.
 */

public class Parent_teacher_List_for_new_post_Adapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;

    ArrayList<GetParentsDetailsForParticularClassid_NewPost> getParentsDetailsForParticularClassid_newPostArrayList = new ArrayList<>();
    private ArrayList<GetParentsDetailsForParticularClassid_NewPost> arraylist;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    SharedPreferencesClass sharedPreferencesClass;

    public interface TheListener {
        public void somethingHappened();
    }

    private TheListener mTheListener;

    public void setTheListener(TheListener listen) {
        mTheListener = listen;
    }

    public void reportSomethingChanged() {
        if (mTheListener != null) {
            mTheListener.somethingHappened();
        }
    }

    public Parent_teacher_List_for_new_post_Adapter(Context context, ArrayList<GetParentsDetailsForParticularClassid_NewPost> getParentsDetailsForParticularClassid_newPostArrayList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.getParentsDetailsForParticularClassid_newPostArrayList = getParentsDetailsForParticularClassid_newPostArrayList;
        this.arraylist = new ArrayList<GetParentsDetailsForParticularClassid_NewPost>();
        this.arraylist.addAll(getParentsDetailsForParticularClassid_newPostArrayList);

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
        return getParentsDetailsForParticularClassid_newPostArrayList.size();
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
            convertView = layoutInflater.inflate(R.layout.custom_parent_teacher_list_for_new_post_ar, null);

        } else {
            convertView = layoutInflater.inflate(R.layout.custom_parent_teacher_list_for_new_post, null);

        }


        CheckBox ch_name = (CheckBox) convertView.findViewById(R.id.ch_name);

        TextView tv_child_name = (TextView) convertView.findViewById(R.id.tv_child_name);

        ch_name.setTypeface(tf);
        tv_child_name.setTypeface(tf);


        ch_name.setText(getParentsDetailsForParticularClassid_newPostArrayList.get(position).getFname() + " " + getParentsDetailsForParticularClassid_newPostArrayList.get(position).getLname());

        tv_child_name.setText(getParentsDetailsForParticularClassid_newPostArrayList.get(position).getChild_name());

        if (getParentsDetailsForParticularClassid_newPostArrayList.get(position).getSelected()) {
            ch_name.setChecked(true);
        } else {
            ch_name.setChecked(false);

        }

        final GetParentsDetailsForParticularClassid_NewPost getParentsDetailsForParticularClassid_newPost = getParentsDetailsForParticularClassid_newPostArrayList.get(position);

        ch_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // perform logic
                    getParentsDetailsForParticularClassid_newPost.setSelected(true);

                    reportSomethingChanged();


                } else {
                    getParentsDetailsForParticularClassid_newPost.setSelected(false);

                    reportSomethingChanged();

                }
            }
        });


        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        getParentsDetailsForParticularClassid_newPostArrayList.clear();
        if (charText.length() == 0) {
            getParentsDetailsForParticularClassid_newPostArrayList.addAll(arraylist);
        } else {
            for (GetParentsDetailsForParticularClassid_NewPost wp : arraylist) {
                if (wp.getFname().toLowerCase(Locale.getDefault()).contains(charText)) {
                    getParentsDetailsForParticularClassid_newPostArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
