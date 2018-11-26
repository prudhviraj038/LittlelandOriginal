package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.leadinfosoft.littleland.ModelClass.ViewPostMediaArrayModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.MyImageViewActivity;
import com.leadinfosoft.littleland.activity.MyPlayerActivity;
import com.leadinfosoft.littleland.activity.MyPlayerWebViewActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Lead on 7/14/2017.
 */

public class ViewPostImagePagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    ArrayList<ViewPostMediaArrayModelClass> viewPostMediaArrayModelClassArrayList = new ArrayList<>();

    ImageLoader imageLoader;
    DisplayImageOptions options;

    public ViewPostImagePagerAdapter(Context context, ArrayList<ViewPostMediaArrayModelClass> viewPostMediaArrayModelClassArrayList) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.viewPostMediaArrayModelClassArrayList = viewPostMediaArrayModelClassArrayList;

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.no_image_available)
                .showImageOnFail(R.drawable.no_image_available)
                .showImageOnLoading(R.drawable.loading).build();
    }

    @Override
    public int getCount() {
        return viewPostMediaArrayModelClassArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.viewpost_imageswitcher_rowlist, container, false);

        /*ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mResources[position]);*/

        RoundedImageView iv_image = (RoundedImageView) itemView.findViewById(R.id.iv_image);
        RoundedImageView iv_image_video = (RoundedImageView) itemView.findViewById(R.id.iv_image_video);
        ImageView iv_video_symbol = (ImageView) itemView.findViewById(R.id.iv_video_symbol);


        VideoView tv_video = (VideoView) itemView.findViewById(R.id.tv_video);

        MediaController mediacontroller = new MediaController(
                mContext);
        mediacontroller.setAnchorView(tv_video);
        // Get the URL from String VideoURL
        Uri video = Uri.parse(viewPostMediaArrayModelClassArrayList.get(position).getPath());
        tv_video.setMediaController(mediacontroller);
        tv_video.setVideoURI(video);

        if (viewPostMediaArrayModelClassArrayList.get(position).getMedia_type().equalsIgnoreCase("image")) {
            iv_image.setVisibility(View.VISIBLE);
            tv_video.setVisibility(View.GONE);
            iv_image_video.setVisibility(View.GONE);
            iv_video_symbol.setVisibility(View.GONE);

        } else {
            iv_image.setVisibility(View.GONE);
            tv_video.setVisibility(View.GONE);
            iv_image_video.setVisibility(View.VISIBLE);
            iv_video_symbol.setVisibility(View.VISIBLE);


            imageLoader.displayImage(viewPostMediaArrayModelClassArrayList.get(position).getThumb(), iv_image_video, options);


        }

        iv_image_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(mContext, MyPlayerActivity.class);
                i.putExtra("video_url", viewPostMediaArrayModelClassArrayList.get(position).getPath());
                mContext.startActivity(i);*/

                Intent i = new Intent(mContext, MyPlayerWebViewActivity.class);
                i.putExtra("video_url", viewPostMediaArrayModelClassArrayList.get(position).getPath());
                mContext.startActivity(i);
            }
        });

        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, MyImageViewActivity.class);
                i.putExtra("image_url", viewPostMediaArrayModelClassArrayList.get(position).getPath());
                mContext.startActivity(i);

            }
        });


        imageLoader.displayImage(viewPostMediaArrayModelClassArrayList.get(position).getPath(), iv_image, options);


        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
