package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.MainActivity;
import com.leadinfosoft.littleland.activity.ViewPostDetail;
import com.leadinfosoft.littleland.fragment.ViewPost;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;

/**
 * Created by Lead on 7/13/2017.
 */

public class HomeEventPageAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    SharedPreferencesClass sharedPreferencesClass;
    boolean lang;

    public HomeEventPageAdapter(Context context,boolean lang) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lang=lang;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView;
        if (lang)
        {
            itemView = mLayoutInflater.inflate(R.layout.home_page_rowlist, container, false);
        }
        else
        {
            itemView = mLayoutInflater.inflate(R.layout.home_page_rowlist_ar, container, false);
        }


        /*ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mResources[position]);*/
        MyHeaderTextView tv_more= (MyHeaderTextView) itemView.findViewById(R.id.tv_more);
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new ViewPost());
//                mContext.startActivity(new Intent(mContext, ViewPostDetail.class));
//                ((MainActivity)mContext).overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fm = ((AppCompatActivity)mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTrasaction = fm.beginTransaction();
        fragmentTrasaction.replace(R.id.frame, fragment);
        fragmentTrasaction.addToBackStack(fragment.toString());
        fragmentTrasaction.commit();
    }
}
