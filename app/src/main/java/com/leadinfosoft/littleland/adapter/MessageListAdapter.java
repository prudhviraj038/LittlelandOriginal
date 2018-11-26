package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.ModelClass.MessageListModel;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.MyImageViewActivity;
import com.leadinfosoft.littleland.activity.MyPlayerActivity;
import com.leadinfosoft.littleland.activity.MyPlayerWebViewActivity;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Lead on 7/25/2017.
 */

public class MessageListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;

    ArrayList<MessageListModel> messageListModelArrayList = new ArrayList<>();

    ImageLoader imageLoader;
    DisplayImageOptions options;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    SharedPreferencesClass sharedPreferencesClass;


    public MessageListAdapter(Context context, ArrayList<MessageListModel> messageListModelArrayList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.messageListModelArrayList = messageListModelArrayList;

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

    public void add(MessageListModel messageListModel) {
        messageListModelArrayList.add(messageListModel);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        Logger.e("24/08 Adapter messageListModelArrayList size ======> " + messageListModelArrayList.size() + "");
        return messageListModelArrayList.size();
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
            convertView = layoutInflater.inflate(R.layout.custom_message_list_ar, null);

        } else {
            convertView = layoutInflater.inflate(R.layout.custom_message_list, null);

        }

       /* MyHeaderTextView tv_menu_name = (MyHeaderTextView) convertView.findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(MenuarrayList.get(i));*/

        LinearLayout ll_my_message_view = (LinearLayout) convertView.findViewById(R.id.ll_my_message_view);
        LinearLayout ll_my_attachment_view = (LinearLayout) convertView.findViewById(R.id.ll_my_attachment_view);

        LinearLayout ll_opposite_message_view = (LinearLayout) convertView.findViewById(R.id.ll_opposite_message_view);
        LinearLayout ll_opposite_attachment_view = (LinearLayout) convertView.findViewById(R.id.ll_opposite_attachment_view);

        CircularImageView iv_my_imageview_1 = (CircularImageView) convertView.findViewById(R.id.iv_my_imageview_1);
        CircularImageView iv_my_imageview_2 = (CircularImageView) convertView.findViewById(R.id.iv_my_imageview_2);
        CircularImageView iv_opposite_image_1 = (CircularImageView) convertView.findViewById(R.id.iv_opposite_image_1);
        CircularImageView iv_opposite_image_2 = (CircularImageView) convertView.findViewById(R.id.iv_opposite_image_2);

        TextView tv_my_message_desc = (TextView) convertView.findViewById(R.id.tv_my_message_desc);
        TextView tv_opposite_message_desc = (TextView) convertView.findViewById(R.id.tv_opposite_message_desc);

        RoundedImageView iv_my_image = (RoundedImageView) convertView.findViewById(R.id.iv_my_image);
        RoundedImageView iv_opposite_image = (RoundedImageView) convertView.findViewById(R.id.iv_opposite_image);


        ImageView iv_my_video_image = (ImageView) convertView.findViewById(R.id.iv_my_video_image);

        ImageView iv_opposite_video_image = (ImageView) convertView.findViewById(R.id.iv_opposite_video_image);

        TextView tv_my_date_1 = (TextView) convertView.findViewById(R.id.tv_my_date_1);
        TextView tv_my_date_2 = (TextView) convertView.findViewById(R.id.tv_my_date_2);
        TextView tv_opposite_date_1 = (TextView) convertView.findViewById(R.id.tv_opposite_date_1);
        TextView tv_opposite_date_2 = (TextView) convertView.findViewById(R.id.tv_opposite_date_2);

        tv_my_date_1.setTypeface(tf_numeric);
        tv_my_date_2.setTypeface(tf_numeric);
        tv_opposite_date_1.setTypeface(tf_numeric);
        tv_opposite_date_1.setTypeface(tf_numeric);

        tv_my_message_desc.setTypeface(tf);
        tv_opposite_message_desc.setTypeface(tf);



        Logger.e("24/08 adapter arraylist size ======> " + messageListModelArrayList.size() + "");

        Logger.e("24/08 adapter my value ======> " + messageListModelArrayList.get(position).getBody());

        TextView tv_my_text_with_image = (TextView) convertView.findViewById(R.id.tv_my_text_with_image);
        tv_my_text_with_image.setVisibility(View.GONE);

        TextView tv_opposite_text_with_image = (TextView) convertView.findViewById(R.id.tv_opposite_text_with_image);
        tv_opposite_text_with_image.setVisibility(View.GONE);

        tv_my_text_with_image.setTypeface(tf);
        tv_opposite_text_with_image.setTypeface(tf);



        if (messageListModelArrayList.get(position).getIs_sent().equalsIgnoreCase("0")) {
            ll_my_attachment_view.setVisibility(View.VISIBLE);
            ll_my_message_view.setVisibility(View.VISIBLE);
            ll_opposite_attachment_view.setVisibility(View.GONE);
            ll_opposite_message_view.setVisibility(View.GONE);

            tv_my_message_desc.setText(messageListModelArrayList.get(position).getBody());
            tv_my_text_with_image.setText(messageListModelArrayList.get(position).getBody());

            imageLoader.displayImage(messageListModelArrayList.get(position).getTo_profile_pic(), iv_my_imageview_1, options);
            imageLoader.displayImage(messageListModelArrayList.get(position).getTo_profile_pic(), iv_my_imageview_2, options);

            tv_my_date_1.setText(Common.changeDateFormat_yyyy_mm_dd_hh_mm_ss(messageListModelArrayList.get(position).getStamp()));
            tv_my_date_2.setText(Common.changeDateFormat_yyyy_mm_dd_hh_mm_ss(messageListModelArrayList.get(position).getStamp()));

            if (messageListModelArrayList.get(position).getBody().equalsIgnoreCase("") || messageListModelArrayList.get(position).getBody().length() == 0) {
                iv_my_imageview_1.setVisibility(View.GONE);
                tv_my_message_desc.setVisibility(View.GONE);
                tv_my_date_1.setVisibility(View.GONE);

                ll_my_message_view.setVisibility(View.GONE);

            } else {
                iv_my_imageview_1.setVisibility(View.VISIBLE);
                tv_my_message_desc.setVisibility(View.VISIBLE);
                tv_my_date_1.setVisibility(View.VISIBLE);

                ll_my_message_view.setVisibility(View.VISIBLE);


            }

            try {


                if (messageListModelArrayList.get(position).getAttachment().equalsIgnoreCase("false")) {
                    iv_my_imageview_2.setVisibility(View.GONE);
                    iv_my_image.setVisibility(View.GONE);
                    iv_my_video_image.setVisibility(View.GONE);
                    tv_my_date_2.setVisibility(View.GONE);

                    ll_my_attachment_view.setVisibility(View.GONE);
                } else {
                    JSONObject jsonObjectattachment = new JSONObject(messageListModelArrayList.get(position).getAttachment());

                    String path = "";
                    String media_type = "";

                    if (jsonObjectattachment != null) {

                        Logger.e("24/08 my attachemtn =======> " + jsonObjectattachment + "");

                        ll_my_message_view.setVisibility(View.GONE);


                        path = jsonObjectattachment.optString("path");
                        media_type = jsonObjectattachment.optString("media_type");

                        Logger.e("24/08 on the spot file path ====> " + path + "");
                        Logger.e("24/08 on the spot or not ====> " + messageListModelArrayList.get(position).getOnthespot() + "");


                        if (messageListModelArrayList.get(position).getBody().equalsIgnoreCase("") || messageListModelArrayList.get(position).getBody().length() == 0) {
                            tv_my_text_with_image.setVisibility(View.GONE);
                        } else {
                            tv_my_text_with_image.setVisibility(View.VISIBLE);

                        }


                        iv_my_imageview_2.setVisibility(View.VISIBLE);


                        ll_my_attachment_view.setVisibility(View.VISIBLE);

                        if (media_type.equalsIgnoreCase("image")) {
                            iv_my_image.setVisibility(View.VISIBLE);
                            iv_my_video_image.setVisibility(View.GONE);
                            tv_my_date_2.setVisibility(View.VISIBLE);


                        } else {
                            iv_my_image.setVisibility(View.VISIBLE);
                            iv_my_video_image.setVisibility(View.VISIBLE);
                            tv_my_date_2.setVisibility(View.VISIBLE);

                        }


                        if (messageListModelArrayList.get(position).getOnthespot()) {

                            if (media_type.equalsIgnoreCase("image")) {

                                File imgFile = new File(path);

                                if (imgFile.exists()) {

                                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                                    iv_my_image.setImageBitmap(myBitmap);

                                }

                            } else {
                                iv_my_image.setVisibility(View.VISIBLE);
                                iv_my_video_image.setVisibility(View.VISIBLE);
                                tv_my_date_2.setVisibility(View.VISIBLE);
                            }


                        } else {

                            if (media_type.equalsIgnoreCase("image")) {

                                imageLoader.displayImage(path, iv_my_image, options);
                            } else {
                                iv_my_image.setVisibility(View.VISIBLE);
                                iv_my_video_image.setVisibility(View.VISIBLE);
                                tv_my_date_2.setVisibility(View.VISIBLE);
                            }
                        }


                    } else {
                        iv_my_imageview_2.setVisibility(View.GONE);
                        iv_my_image.setVisibility(View.GONE);
                        iv_my_video_image.setVisibility(View.GONE);
                        tv_my_date_2.setVisibility(View.GONE);

                    }

                    final String finalPath = path;
                    iv_my_video_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*Intent i = new Intent(context, MyPlayerActivity.class);
                            i.putExtra("video_url", finalPath);
                            context.startActivity(i);*/

                            Intent i = new Intent(context, MyPlayerWebViewActivity.class);
                            i.putExtra("video_url", finalPath);
                            context.startActivity(i);


                        }
                    });

                    iv_my_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Logger.e("30/08 Click iv_my_image ======> " + messageListModelArrayList.get(position).getOnthespot());

                            if (messageListModelArrayList.get(position).getOnthespot()) {

                                Intent i = new Intent(context, MyImageViewActivity.class);
                                i.putExtra("image_url", finalPath);
                                i.putExtra("type", "Onthespot");
                                context.startActivity(i);
                            } else {
                                Intent i = new Intent(context, MyImageViewActivity.class);
                                i.putExtra("image_url", finalPath);
                                context.startActivity(i);
                            }


                        }
                    });


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            ll_my_attachment_view.setVisibility(View.GONE);
            ll_my_message_view.setVisibility(View.GONE);
            ll_opposite_attachment_view.setVisibility(View.VISIBLE);
            ll_opposite_message_view.setVisibility(View.VISIBLE);

            tv_opposite_message_desc.setText(messageListModelArrayList.get(position).getBody());
            tv_opposite_text_with_image.setText(messageListModelArrayList.get(position).getBody());


            imageLoader.displayImage(messageListModelArrayList.get(position).getFrom_profile_pic(), iv_opposite_image_1, options);
            imageLoader.displayImage(messageListModelArrayList.get(position).getFrom_profile_pic(), iv_opposite_image_2, options);

            tv_opposite_date_1.setText(Common.changeDateFormat_yyyy_mm_dd_hh_mm_ss(messageListModelArrayList.get(position).getStamp()));
            tv_opposite_date_2.setText(Common.changeDateFormat_yyyy_mm_dd_hh_mm_ss(messageListModelArrayList.get(position).getStamp()));


            if (messageListModelArrayList.get(position).getBody().equalsIgnoreCase("") || messageListModelArrayList.get(position).getBody().length() == 0) {
                iv_opposite_image_1.setVisibility(View.GONE);
                tv_opposite_message_desc.setVisibility(View.GONE);
                tv_opposite_date_1.setVisibility(View.GONE);

                ll_opposite_message_view.setVisibility(View.GONE);


            } else {
                iv_opposite_image_1.setVisibility(View.VISIBLE);
                tv_opposite_message_desc.setVisibility(View.VISIBLE);
                tv_opposite_date_1.setVisibility(View.VISIBLE);

                ll_opposite_message_view.setVisibility(View.VISIBLE);


            }

            try {


                if (messageListModelArrayList.get(position).getAttachment().equalsIgnoreCase("false")) {
                    iv_opposite_image_2.setVisibility(View.GONE);
                    iv_opposite_image.setVisibility(View.GONE);
                    iv_opposite_video_image.setVisibility(View.GONE);
                    tv_opposite_date_2.setVisibility(View.GONE);

                    ll_opposite_attachment_view.setVisibility(View.GONE);


                } else {
                    JSONObject jsonObjectattachment = new JSONObject(messageListModelArrayList.get(position).getAttachment());

                    if (jsonObjectattachment != null) {

                        Logger.e("24/08 opposite attachemtn =======> " + jsonObjectattachment + "");

                        ll_opposite_message_view.setVisibility(View.GONE);


                        String path = jsonObjectattachment.optString("path");
                        String media_type = jsonObjectattachment.optString("media_type");

                        iv_opposite_image_2.setVisibility(View.VISIBLE);
                        tv_opposite_date_2.setVisibility(View.VISIBLE);

                        if (messageListModelArrayList.get(position).getBody().equalsIgnoreCase("") || messageListModelArrayList.get(position).getBody().length() == 0) {
                            tv_opposite_text_with_image.setVisibility(View.GONE);
                        } else {
                            tv_opposite_text_with_image.setVisibility(View.VISIBLE);

                        }

                        ll_opposite_attachment_view.setVisibility(View.VISIBLE);


                        if (media_type.equalsIgnoreCase("image")) {
                            iv_opposite_image.setVisibility(View.VISIBLE);
                            iv_opposite_video_image.setVisibility(View.GONE);

                        } else {
                            iv_opposite_image.setVisibility(View.VISIBLE);
                            iv_opposite_video_image.setVisibility(View.VISIBLE);
                        }


                        if (messageListModelArrayList.get(position).getOnthespot()) {


                            if (media_type.equalsIgnoreCase("image")) {
                                File imgFile = new File(path);

                                if (imgFile.exists()) {

                                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                                    iv_opposite_image.setImageBitmap(myBitmap);

                                }
                            } else {
                                iv_opposite_image.setVisibility(View.VISIBLE);
                                iv_opposite_video_image.setVisibility(View.VISIBLE);
                            }


                        } else {

                            if (media_type.equalsIgnoreCase("image")) {
                                imageLoader.displayImage(path, iv_opposite_image, options);
                            } else {
                                iv_opposite_image.setVisibility(View.VISIBLE);
                                iv_opposite_video_image.setVisibility(View.VISIBLE);
                            }


                        }

                        final String finalPath = path;
                        iv_opposite_video_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /*Intent i = new Intent(context, MyPlayerActivity.class);
                                i.putExtra("video_url", finalPath);
                                context.startActivity(i);*/

                                Intent i = new Intent(context, MyPlayerWebViewActivity.class);
                                i.putExtra("video_url", finalPath);
                                context.startActivity(i);


                            }
                        });

                        iv_opposite_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Logger.e("30/08 Click iv_my_image ======> " + messageListModelArrayList.get(position).getOnthespot());

                                if (messageListModelArrayList.get(position).getOnthespot()) {

                                    Intent i = new Intent(context, MyImageViewActivity.class);
                                    i.putExtra("image_url", finalPath);
                                    i.putExtra("type", "Onthespot");

                                    context.startActivity(i);
                                } else {
                                    Intent i = new Intent(context, MyImageViewActivity.class);
                                    i.putExtra("image_url", finalPath);
                                    context.startActivity(i);
                                }

                            }
                        });


                    } else {
                        iv_opposite_image_2.setVisibility(View.GONE);
                        iv_opposite_image.setVisibility(View.GONE);
                        iv_opposite_video_image.setVisibility(View.GONE);
                        tv_opposite_date_2.setVisibility(View.GONE);

                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return convertView;
    }
}
