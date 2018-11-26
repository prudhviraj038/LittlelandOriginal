package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.ModelClass.ViewPostListModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.MainActivity;
import com.leadinfosoft.littleland.activity.ViewPostDetail;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lead on 7/14/2017.
 */

public class ViewPostAdapter extends RecyclerView.Adapter<ViewPostAdapter.MyViewHolder> {

    Context context;
    boolean isEnglish;

    ImageLoader imageLoader;
    DisplayImageOptions options;

    ArrayList<ViewPostListModelClass> viewPostListModelClassArrayList = new ArrayList<>();

    SharedPreferencesClass sharedPreferencesClass;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    public ViewPostAdapter(Context context, boolean isEnglish, ArrayList<ViewPostListModelClass> viewPostListModelClassArrayList) {
        this.context = context;
        this.isEnglish = isEnglish;
        this.viewPostListModelClassArrayList = viewPostListModelClassArrayList;

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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewpost_rowlist_ar, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewpost_rowlist, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.tv_title.setTypeface(tf);
        holder.tv_time_date.setTypeface(tf_numeric);

        holder.tv_title.setText(viewPostListModelClassArrayList.get(position).getTitle());
        holder.tv_time_date.setText(Common.changeDateFormat_yyyy_mm_dd_hh_mm_ss(viewPostListModelClassArrayList.get(position).getStamp()));


        imageLoader.displayImage(viewPostListModelClassArrayList.get(position).getImage(), holder.iv_image, options);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ViewPostDetail.class);
                i.putExtra("post_id", viewPostListModelClassArrayList.get(position).getId());
                context.startActivity(i);
                ((MainActivity) context).overridePendingTransition(0, 0);


//                ((MainActivity) context).overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return viewPostListModelClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_image;
        public TextView tv_title, tv_time_date;

        public MyViewHolder(View view) {
            super(view);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_time_date = (TextView) view.findViewById(R.id.tv_time_date);
        }


    }
}