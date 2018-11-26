package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.ViewPostDetail;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;

/**
 * Created by Lead on 7/15/2017.
 */

public class MonthAttendPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    boolean isEnglish;
    MonthAttendRecyAdapter monthAttendRecyAdapter;

    public MonthAttendPagerAdapter(Context context,boolean isEnglish) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isEnglish=isEnglish;
//        monthAttendRecyAdapter=new MonthAttendRecyAdapter(mContext,isEnglish);
        Log.e("view1111111","====>>>");
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.monthly_attend_pager_rowlist, container, false);
        RecyclerView rv_month= (RecyclerView) itemView.findViewById(R.id.rv_month);
        LinearLayoutManager llm=new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_month.setLayoutManager(llm);

        rv_month.setAdapter(monthAttendRecyAdapter);

        /*ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mResources[position]);*/

        container.addView(itemView);

        return itemView;
    }


}
