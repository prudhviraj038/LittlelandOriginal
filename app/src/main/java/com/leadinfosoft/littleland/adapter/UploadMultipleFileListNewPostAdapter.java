package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.ModelClass.UploadMulipleFileNewPost;
import com.leadinfosoft.littleland.ModelClass.ViewPostListModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.MainActivity;
import com.leadinfosoft.littleland.activity.MyImageViewActivity;
import com.leadinfosoft.littleland.activity.MyPlayerActivity;
import com.leadinfosoft.littleland.activity.MyPlayerWebViewActivity;
import com.leadinfosoft.littleland.activity.ViewPostDetail;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.widget.CircleTransform;
import com.leadinfosoft.littleland.widget.CropSquareTransformation;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.BlurTransformation;

/**
 * Created by Lead on 7/14/2017.
 */

public class UploadMultipleFileListNewPostAdapter extends RecyclerView.Adapter<UploadMultipleFileListNewPostAdapter.MyViewHolder> {

    Context context;
    boolean isEnglish;

    ImageLoader imageLoader;
    DisplayImageOptions options;

    ArrayList<UploadMulipleFileNewPost> uploadMulipleFileNewPostArrayList = new ArrayList<>();

    SharedPreferencesClass sharedPreferencesClass;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    public UploadMultipleFileListNewPostAdapter(Context context, boolean isEnglish, ArrayList<UploadMulipleFileNewPost> uploadMulipleFileNewPostArrayList) {
        this.context = context;
        this.isEnglish = isEnglish;
        this.uploadMulipleFileNewPostArrayList = uploadMulipleFileNewPostArrayList;

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.no_image_available)
                .showImageOnFail(R.drawable.no_image_available)
                .showImageOnLoading(R.drawable.loading).build();

       /* options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .considerExifParams(false)
                .build();*/

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
                    .inflate(R.layout.custom_upload_file_ar, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_upload_file, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        if (uploadMulipleFileNewPostArrayList.get(position).getFile_type().equalsIgnoreCase("video")) {
            holder.iv_VideoView.setVisibility(View.VISIBLE);
            holder.iv_video_view_thumb.setVisibility(View.VISIBLE);

            holder.iv_image.setVisibility(View.GONE);
            holder.tv_video_remove.setVisibility(View.VISIBLE);
            holder.tv_image_remove.setVisibility(View.GONE);

            holder.ll_main_video.setVisibility(View.VISIBLE);
            holder.ll_main_image.setVisibility(View.GONE);

            imageLoader.displayImage(uploadMulipleFileNewPostArrayList.get(position).getThumb(), holder.iv_VideoView, options);

        } else {
            holder.iv_VideoView.setVisibility(View.GONE);
            holder.iv_video_view_thumb.setVisibility(View.GONE);


            holder.iv_image.setVisibility(View.VISIBLE);

            holder.tv_video_remove.setVisibility(View.GONE);
            holder.tv_image_remove.setVisibility(View.VISIBLE);

            holder.ll_main_video.setVisibility(View.GONE);
            holder.ll_main_image.setVisibility(View.VISIBLE);

        }


       /* Glide.with(context).load(uploadMulipleFileNewPostArrayList.get(position).getFull_path()).placeholder(R.drawable.no_image_available)
                .error(R.drawable.no_image_available).into(holder.iv_image);*/

       /* Picasso.with(context).load(uploadMulipleFileNewPostArrayList.get(position).getFull_path())
                .transform(new CircleTransform(holder.iv_image)).into(holder.iv_image);*/


        imageLoader.displayImage(uploadMulipleFileNewPostArrayList.get(position).getFull_path(), holder.iv_image, options);

//        holder.iv_image.setImageBitmap(rotateBitmap(uploadMulipleFileNewPostArrayList.get(position).getFull_path()));

        holder.iv_VideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(context, MyPlayerActivity.class);
                i.putExtra("video_url", uploadMulipleFileNewPostArrayList.get(position).getFull_path());
                context.startActivity(i);*/

                Intent i = new Intent(context, MyPlayerWebViewActivity.class);
                i.putExtra("video_url", uploadMulipleFileNewPostArrayList.get(position).getFull_path());
                context.startActivity(i);


            }
        });

        holder.iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, MyImageViewActivity.class);
                i.putExtra("image_url", uploadMulipleFileNewPostArrayList.get(position).getFull_path());
                context.startActivity(i);

            }
        });


        holder.tv_image_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveData(position);
            }
        });


        holder.tv_video_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveData(position);

            }
        });

       /* MediaController mediacontroller = new MediaController(
                context);
        mediacontroller.setAnchorView(holder.VideoView);
        // Get the URL from String VideoURL
        Uri video = Uri.parse(uploadMulipleFileNewPostArrayList.get(position).getFull_path());
        holder.VideoView.setMediaController(mediacontroller);
        holder.VideoView.setVideoURI(video);*/

       /* holder.tv_title.setText(viewPostListModelClassArrayList.get(position).getTitle());
        holder.tv_time_date.setText(viewPostListModelClassArrayList.get(position).getStamp());


        imageLoader.displayImage(viewPostListModelClassArrayList.get(position).getImage(), holder.iv_image, options);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ViewPostDetail.class);
                i.putExtra("post_id", viewPostListModelClassArrayList.get(position).getId());
                context.startActivity(i);

               *//* context.startActivity(new Intent(context, ViewPostDetail.class));*//*
                ((MainActivity) context).overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return uploadMulipleFileNewPostArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

      /*  ImageView iv_image;
        public TextView tv_title, tv_time_date;*/

        ImageView iv_image;
        ImageView iv_VideoView;
        ImageView iv_video_view_thumb;
        TextView tv_image_remove, tv_video_remove;

        LinearLayout ll_main_video, ll_main_image;


        public MyViewHolder(View view) {
            super(view);
           /* iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_time_date = (TextView) view.findViewById(R.id.tv_time_date);*/

            ll_main_video = (LinearLayout) view.findViewById(R.id.ll_main_video);

            ll_main_image = (LinearLayout) view.findViewById(R.id.ll_main_image);


            tv_image_remove = (TextView) view.findViewById(R.id.tv_image_remove);
            tv_video_remove = (TextView) view.findViewById(R.id.tv_video_remove);

            tv_image_remove.setTypeface(tf);
            tv_video_remove.setTypeface(tf);

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                tv_image_remove.setText(Common.filter("lbl_remove", "ar"));
                tv_video_remove.setText(Common.filter("lbl_remove", "ar"));


            } else {
                tv_image_remove.setText(Common.filter("lbl_remove", "en"));
                tv_video_remove.setText(Common.filter("lbl_remove", "en"));


            }


            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            iv_VideoView = (ImageView) view.findViewById(R.id.iv_VideoView);
            iv_video_view_thumb = (ImageView) view.findViewById(R.id.iv_video_view_thumb);


        }


    }

    public void RemoveData(int position) {
        uploadMulipleFileNewPostArrayList.remove(position);
        notifyDataSetChanged();
    }


}