package com.leadinfosoft.littleland.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.FilePath;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.RequestMakerBg;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.MessageListModel;
import com.leadinfosoft.littleland.ModelClass.UploadMulipleFileNewPost;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.MainActivity;
import com.leadinfosoft.littleland.adapter.MessageListAdapter;
import com.leadinfosoft.littleland.adapter.UploadMultipleFileListNewPostAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.utill.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class SendMessageFragment extends Fragment implements View.OnClickListener, AbsListView.OnScrollListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1 = "";
    private String mParam2 = "";

    Context context;

    ImageLoader imageLoader;
    DisplayImageOptions options;

    Response_string<String> resp;
    RequestMaker req;

    RequestMakerBg requestMakerBg;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    ListView lv_messages;

    private String offset = "0";

    String total = "0";

    private int preLast;


    ArrayList<MessageListModel> messageListModelArrayList = new ArrayList<>();

    MessageListAdapter messageListAdapter = null;

    ImageView iv_send_message, iv_attachment;
    EditText et_message;

    LinearLayout ll_file_selected;

    ImageView iv_cancel;

    AVLoadingIndicatorView avi_load_file;
    final int CAMERA_CAPTURE = 1;
    //captured picture uri
    private Uri picUri;

    final int PIC_CROP = 3;

    final int PICK_IMAGE_REQUEST = 2;

    private Bitmap bitmapgallery;

    String image = "";

    private String selectedFilePath = "";

    private static final int REQUEST_TAKE_GALLERY_VIDEO = 56;

    static final int REQUEST_VIDEO_CAPTURE = 21;


    public static String str_file_type = "";

    String final_file_name = "";
    String final_file_type = "";
    String str_et_message = "";

    JSONArray jsonArraysuccess = null;
    JSONObject jsonObject_to_user = null;

    String final_full_path = "";

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    TextView tv_file_selected;

    public SendMessageFragment() {
        // Required empty public constructor
    }


    public static SendMessageFragment newInstance(String param1, String param2) {
        SendMessageFragment fragment = new SendMessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            Logger.e("28/08 SendMessageFragment mParam1 =======> " + mParam1 + "");
            Logger.e("28/08 SendMessageFragment mParam2 =======> " + mParam2 + "");


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        commonFunction();

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            rootView = inflater.inflate(R.layout.fragment_direct_message_ar, container, false);


        } else {
            rootView = inflater.inflate(R.layout.fragment_direct_message, container, false);


        }

        init(rootView);

        initHeader();

        offset = "0";
        total = "0";

        setTypeface_Text();

        NewGetConversionsWebService();

        return rootView;
    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            MainActivity.tv_title.setTypeface(tf);
            MainActivity.tv_subtitle.setTypeface(tf);
            et_message.setTypeface(tf);
            tv_file_selected.setTypeface(tf);

            et_message.setHint(Common.filter("lbl_write_message", "ar").toUpperCase());

        } else {
            MainActivity.tv_title.setTypeface(tf);
            MainActivity.tv_subtitle.setTypeface(tf);
            et_message.setTypeface(tf);
            tv_file_selected.setTypeface(tf);

            et_message.setHint(Common.filter("lbl_write_message", "en").toUpperCase());
        }

    }

    private void init(View rootView) {

        lv_messages = (ListView) rootView.findViewById(R.id.lv_messages);
        lv_messages.setSmoothScrollbarEnabled(true);
//        lv_messages.setOnScrollListener(this);


        iv_send_message = (ImageView) rootView.findViewById(R.id.iv_send_message);
        iv_send_message.setOnClickListener(this);

        iv_attachment = (ImageView) rootView.findViewById(R.id.iv_attachment);
        iv_attachment.setOnClickListener(this);

        et_message = (EditText) rootView.findViewById(R.id.et_message);

        tv_file_selected = (TextView) rootView.findViewById(R.id.tv_file_selected);

        avi_load_file = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi_load_file);
        avi_load_file.setVisibility(View.GONE);

        ll_file_selected = (LinearLayout) rootView.findViewById(R.id.ll_file_selected);
        ll_file_selected.setVisibility(View.GONE);


        iv_cancel = (ImageView) rootView.findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(this);

        /*et_message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, final boolean hasFocus) {
                et_message.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(et_message, InputMethodManager.SHOW_IMPLICIT);


                        if (hasFocus) {
                            Toast.makeText(context, "Call Focus true", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, "Call Focus false", Toast.LENGTH_SHORT).show();

                        }


                    }
                });
            }
        });
        et_message.requestFocus();*/


    /*    et_message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                scrollMyListViewToBottom();
            }
        });*/

        lv_messages.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);

    }

    @Override
    public void onClick(View v) {

        if (v == iv_send_message) {

            str_et_message = et_message.getText().toString();

//            if (con.isConnectingToInternet()) {
//                SendNewMessageWebService sendNewMessageWebService = new SendNewMessageWebService();
//                sendNewMessageWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            } else {
//                Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
//            }

            if (selectedFilePath.equalsIgnoreCase("") && et_message.getText().toString().equalsIgnoreCase("")) {

                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    Toast.makeText(context, Common.filter("error_Please_Enter_message_or_select_attachment", "ar"), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, Common.filter("error_Please_Enter_message_or_select_attachment", "en"), Toast.LENGTH_SHORT).show();


                }

                return;
            }

            SendMessageWebService();

            if (con.isConnectingToInternet()) {
                sendMessageFill();
            } else {
                Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
            }

            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        } else if (v == iv_attachment) {
//            pickImage();
            setPermission();


        } else if (v == iv_cancel) {
            selectedFilePath = "";

            final_file_name = "";
            final_full_path = "";
            str_file_type = "";

            iv_attachment.setVisibility(View.VISIBLE);
            ll_file_selected.setVisibility(View.GONE);

        }

    }

    private void setPermission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (!Common.hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, PERMISSION_ALL);
        } else {
            pickImage();

        }
    }

    private void pickImage() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));


        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            dialog.setContentView(R.layout.dialog_image_chooser_ar);

        } else {
            dialog.setContentView(R.layout.dialog_image_chooser);

        }


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        LinearLayout ll_main = (LinearLayout) dialog.findViewById(R.id.ll_main);
        ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
       /* tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                dispatchTakePictureFromCameraIntent();
            }
        });*/


        ImageView camera = (ImageView) dialog.findViewById(R.id.iv_camera);
        ImageView gallery = (ImageView) dialog
                .findViewById(R.id.iv_gallery);
        ImageView iv_video = (ImageView) dialog.findViewById(R.id.iv_video);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //use standard intent to capture an image
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
                    File imageFile = new File(imageFilePath);
                    picUri = Uri.fromFile(imageFile); // convert path to Uri
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                    startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
                } catch (Exception e) {
                    e.printStackTrace();
                    String errorMessage = "Whoops - your device doesn't support capturing images!";
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                str_file_type = "image";

                GalleryOrCameraDialog(dialog);

             /*   Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                dialog.dismiss();*/
            }
        });

        iv_video.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                GalleryOrCameraVideoDialog(dialog);

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void GalleryOrCameraDialog(final Dialog old_dialog) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            dialog.setContentView(R.layout.dialog_image_chooser_camera_or_gallery_ar);

        } else {
            dialog.setContentView(R.layout.dialog_image_chooser_camera_or_gallery);

        }


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        LinearLayout ll_main = (LinearLayout) dialog.findViewById(R.id.ll_main);
        ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);

        ImageView iv_new_camera = (ImageView) dialog.findViewById(R.id.iv_new_camera);
        ImageView iv_new_gallery = (ImageView) dialog
                .findViewById(R.id.iv_new_gallery);

        iv_new_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                old_dialog.dismiss();
                dispatchTakePictureFromCameraIntent();
                dialog.dismiss();
            }
        });

        iv_new_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                old_dialog.dismiss();


                str_file_type = "image";

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void GalleryOrCameraVideoDialog(final Dialog old_dialog) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            dialog.setContentView(R.layout.dialog_video_chooser_camera_or_gallery_ar);

        } else {
            dialog.setContentView(R.layout.dialog_video_chooser_camera_or_gallery);

        }


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        LinearLayout ll_main = (LinearLayout) dialog.findViewById(R.id.ll_main);
        ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);

        ImageView iv_new_camera = (ImageView) dialog.findViewById(R.id.iv_new_camera);
        ImageView iv_new_gallery = (ImageView) dialog
                .findViewById(R.id.iv_new_gallery);

        iv_new_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                old_dialog.dismiss();
                dispatchTakeVideoIntent();
                dialog.dismiss();
            }
        });

        iv_new_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                old_dialog.dismiss();

                str_file_type = "video";

                SelectVideoFromGallery();

                dialog.dismiss();


            }
        });

        dialog.show();

    }

    private void dispatchTakePictureFromCameraIntent() {

        str_file_type = "image";

        try {
            //use standard intent to capture an image

            Random r = new Random();
            int i1 = r.nextInt(80 - 65) + 65;

            String image_name = "/" + "picture" + i1 + "" + ".jpg";

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + image_name/*"/picture.jpg"*/;
            File imageFile = new File(imageFilePath);

            picUri = Uri.fromFile(imageFile); // convert path to Uri
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
            startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }

    }

    private void dispatchTakeVideoIntent() {
       /* Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }*/


        str_file_type = "video";

        Random r = new Random();
        int i1 = r.nextInt(80 - 65) + 65;

        String image_name = "/" + "picture" + i1 + "" + ".mp4";

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + image_name/*"/picture.jpg"*/;
        File imageFile = new File(imageFilePath);

        picUri = Uri.fromFile(imageFile); // convert path to Uri
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);


    }

    private void SelectVideoFromGallery() {

        str_file_type = "video";

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);

    }

    private void commonFunction() {
        context = getActivity();
        con = new ConnectionDetector(context);
        sharedPreferencesClass = new SharedPreferencesClass(context);

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.no_image_available)
                .showImageOnFail(R.drawable.no_image_available)
                .showImageOnLoading(R.drawable.loading).build();


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

   /* @OnClick(R.id.rl_post)
    public void onClickPost() {
        Log.e("1111111111111", "==========>>");
        if (isopenpost) {
            rl_post_sub.setVisibility(View.GONE);
            isopenpost = false;
        } else {
            rl_post_sub.setVisibility(View.VISIBLE);
            isopenpost = true;
        }

//        openFragment(new Calender());


    }*/

    private void openFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTrasaction = fm.beginTransaction();
        fragmentTrasaction.replace(R.id.frame, fragment);
        fragmentTrasaction.addToBackStack(fragment.toString());
        fragmentTrasaction.commit();
    }

    private void initHeader() {

        getActivity().findViewById(R.id.rl_withtitle).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.rl_withlogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_orang).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.iv_star).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.iv_applogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.rl_spiner).setVisibility(View.VISIBLE);


        getActivity().findViewById(R.id.iv_add).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.iv_bell1).setVisibility(View.INVISIBLE);

//        MainActivity.tv_title.setText(mParam2);
        MainActivity.tv_subtitle.setVisibility(View.VISIBLE);

        MainActivity.iv_profile_pic.setVisibility(View.VISIBLE);


        if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase(Common.USER_TYPE_TEACHER)) {

            if (mParam2.equalsIgnoreCase("") || mParam2.length() == 0) {
                MainActivity.tv_title.setText(R.string.app_name);
            } else {
                MainActivity.tv_title.setText(mParam2);

            }

          /*  if (mParam2.equalsIgnoreCase("") || mParam2.length()==0)
            {
                MainActivity.tv_title.setVisibility(View.GONE);
            }
            else {
                MainActivity.tv_title.setVisibility(View.VISIBLE);

            }*/

            MainActivity.tv_subtitle.setTextColor(getActivity().getResources().getColor(R.color.techer_parpal));
            MainActivity.tv_subtitle.setText("");

        } else {

            if (mParam2.equalsIgnoreCase("") || mParam2.length() == 0) {
                MainActivity.tv_title.setText(R.string.app_name);
            } else {
                MainActivity.tv_title.setText(mParam2);

            }

//            MainActivity.tv_title.setText(mParam2);

           /* if (mParam2.equalsIgnoreCase("") || mParam2.length()==0)
            {
                MainActivity.tv_title.setVisibility(View.GONE);
            }
            else {
                MainActivity.tv_title.setVisibility(View.VISIBLE);

            }*/

            MainActivity.tv_subtitle.setTextColor(getActivity().getResources().getColor(R.color.techer_parpal));
            MainActivity.tv_subtitle.setText(/*sharedPreferencesClass.getSelected_Child_Name()*/"");


        }

        getActivity().findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();

                MessageContactListFragment.on_back = true;

                final ImageView iv_back = (ImageView) getActivity().findViewById(R.id.iv_back);

                iv_back.setEnabled(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_back.setEnabled(true);
                    }
                }, 1500);
            }
        });

        if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase("teacher")) {
            if (sharedPreferencesClass.getSelected_Class_Name().equalsIgnoreCase("") || sharedPreferencesClass.getSelected_Class_Name().length() == 0) {
                MainActivity.tv_select_class.setText("Select Class");

            } else {
                MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Class_Name());

            }
        } else {
            if (sharedPreferencesClass.getSelected_Child_Name().equalsIgnoreCase("") || sharedPreferencesClass.getSelected_Child_Name().length() == 0) {
                MainActivity.tv_select_class.setText("Select Child");

            } else {
                MainActivity.tv_select_class.setText(sharedPreferencesClass.getSelected_Child_Name());

            }
        }

        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    // TODO: Rename method, update argument and hook method into UI event


    private void NewGetConversionsWebService() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("23/08 COnversionWeb Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

                        jsonArraysuccess = jsonObject.optJSONArray("success");
                        jsonObject_to_user = jsonObject.optJSONObject("to_user");

                        if (jsonObject_to_user != null) {
                            MainActivity.tv_title.setText(jsonObject_to_user.optString("to_name"));
                            MainActivity.tv_subtitle.setText(jsonObject_to_user.optString("tagline"));
                            mParam1 = jsonObject_to_user.optString("to_uid");

                            imageLoader.displayImage(jsonObject_to_user.optString("to_profile_pic"), MainActivity.iv_profile_pic, options);

                        }

                        if (jsonArraysuccess.length() > 0) {
                            messageListModelArrayList.clear();

                            for (int i = 0; i < jsonArraysuccess.length(); i++) {
                                JSONObject jsonObject1 = jsonArraysuccess.optJSONObject(i);

                                String id = jsonObject1.optString("id");
                                String from_uid = jsonObject1.optString("from_uid");
                                String from_name = jsonObject1.optString("from_name");
                                String from_profile_pic = jsonObject1.optString("from_profile_pic");
                                String to_uid = jsonObject1.optString("to_uid");
                                String to_name = jsonObject1.optString("to_name");
                                String to_profile_pic = jsonObject1.optString("to_profile_pic");

                                String body = jsonObject1.optString("body");
                                String stamp = jsonObject1.optString("stamp");
                                String attachment = jsonObject1.optString("attachment");
                                String is_sent = jsonObject1.optString("is_sent");

                                MessageListModel messageListModel = new MessageListModel();
                                messageListModel.setId(id);
                                messageListModel.setFrom_uid(from_uid);
                                messageListModel.setFrom_name(from_name);
                                messageListModel.setFrom_profile_pic(from_profile_pic);
                                messageListModel.setTo_uid(to_uid);
                                messageListModel.setTo_name(to_name);
                                messageListModel.setTo_profile_pic(to_profile_pic);
                                messageListModel.setBody(body);
                                messageListModel.setStamp(stamp);

                                messageListModel.setAttachment(attachment);
                                messageListModel.setIs_sent(is_sent);
                                messageListModel.setOnthespot(false);

                                messageListModelArrayList.add(messageListModel);


                            }
                        } else {
                            messageListModelArrayList.clear();
                        }
                        messageListAdapter = new MessageListAdapter(context, messageListModelArrayList);
                        lv_messages.setAdapter(messageListAdapter);

                        lv_messages.setSelection(Integer.parseInt(offset) - 4);

                        offset = jsonObject.optString("offset");

                        scrollMyListViewToBottom();

                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();

/*
        action REQUIRED chat_history
        uid REQUIRED Logged - in User 's UID
        to_uid REQUIRED
        OPTIONAL*/

        params.add(new BasicNameValuePair("action", "chat_history"));

        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("to_uid", mParam1));
        params.add(new BasicNameValuePair("offset", offset + ""));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("23/08 COnversionWebService Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_message_cms);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.e("22/08 requestCode ===> " + requestCode + "");
        Logger.e("22/08 resultCode ===> " + resultCode + "");

        if (resultCode == Activity.RESULT_OK) {


            if (requestCode == CAMERA_CAPTURE) {
//get the Uri for the captured image
                Uri uri = picUri;

                if (picUri == null) {
                    //no data present
                    return;
                }

//            Toast.makeText(context, "picUri ====>" + picUri + "", Toast.LENGTH_SHORT).show();

                selectedFilePath = FilePath.getPath(context, uri);
                Logger.e("28/08 Camera ====> " + "Selected Camera File Path:" + selectedFilePath);

                File file = new File(String.valueOf(selectedFilePath));
                if (file.exists()) {
                }
//Do something
                else {
                    return;
                }

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    Logger.e("23/08 file name ======> " + selectedFilePath);

                    if (con.isConnectingToInternet()) {
                        UploadFileWebService uploadFileWebService = new UploadFileWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == PIC_CROP) {
                Bundle extras = data.getExtras();
//get the cropped bitmap
                Bitmap thePic = (Bitmap) extras.get("data");

                bitmapgallery = thePic;

                if (bitmapgallery == null) {
                    // set the toast for select image
                } else {
                    image = getStringImage(bitmapgallery);
                }

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(context, selectedFileUri);
                Logger.e("23/08  ====> " + "Selected Gallery File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    if (con.isConnectingToInternet()) {
                        UploadFileWebService uploadFileWebService = new UploadFileWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }


                    Logger.e("23/08 file name ======> " + selectedFilePath);

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == PICK_IMAGE_REQUEST) {

                if (data == null) {
                    return;
                }

                picUri = data.getData();

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(context, selectedFileUri);
                Logger.e("23/08  ====> " + "Selected Gallery File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    if (con.isConnectingToInternet()) {
                        UploadFileWebService uploadFileWebService = new UploadFileWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }

                    Logger.e("23/08 file name ======> " + selectedFilePath);

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }

//            performCrop();
            } else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                if (data == null) {
                    //no data present
                    return;
                }

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(context, selectedFileUri);
                Logger.e("23/08 " + "Selected Video From Gallery File Path:======>" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    if (con.isConnectingToInternet()) {
                        UploadFileWebService uploadFileWebService = new UploadFileWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_VIDEO_CAPTURE) {
            /*Uri videoUri = intent.getData();

            Log.e("02/09 videoUri ============>", videoUri + "");

            mVideoView.setVideoURI(videoUri);*/

                Uri uri = picUri;

                Uri videoUri = data.getData();

                Logger.e("02/09 videoUri ============>" + videoUri + "");

                Logger.e("02/09 picUri ============>" + picUri + "");


//            Toast.makeText(context, "picUri ====>" + picUri + "", Toast.LENGTH_SHORT).show();

                selectedFilePath = FilePath.getPath(context, uri);
                Log.e("02/09 video path ====> ", "Selected Camera File Path:" + selectedFilePath);

                File file = new File(String.valueOf(selectedFilePath));
                if (file.exists()) {

                }
//Do something
                else {

                    return;
                }
// Do something else.
                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    Logger.e("02/09 video file name selectedFilePath======> " + selectedFilePath);

                    if (con.isConnectingToInternet()) {
                        UploadFileWebService uploadFileWebService = new UploadFileWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    private void performCrop() {
        try {
//call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
//set crop properties
            cropIntent.putExtra("crop", "true");
//indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
//indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
//retrieve data on return
            cropIntent.putExtra("return-data", true);
//start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    public String getStringImage(Bitmap bmp) {
        String encodedImage = "";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);


        return encodedImage;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView lw, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        switch (lw.getId()) {
            case R.id.lv_messages:

                // Make your calculation stuff here. You have all your
                // needed info from the parameters of this function.

                // Sample calculation to determine if the last
                // item is fully visible.
                final int lastItem = firstVisibleItem + visibleItemCount;

                if (lastItem == totalItemCount) {
                    if (preLast != lastItem) {
                        //to avoid multiple calls for last item
                        Logger.e("04/07 Last =======> " + "Last");
                        preLast = lastItem;

                        Logger.e("04/07 Last preLast =======> " + preLast + "");

                        int offset_int = Integer.parseInt(offset);
                        int total_int = Integer.parseInt(total);

                        NewGetConversionsWebService();

                       /* if (offset_int > total_int) {

                        } else {
                            NewGetConversionsWebService();
                        }*/
                    }
                }
        }
    }

    class UploadFileWebService extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            avi_load_file.setVisibility(View.VISIBLE);
            iv_attachment.setVisibility(View.GONE);
            iv_send_message.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String method = "POST";
            String reply1 = null;
            try {

                InputStream inStream = null;
//            HttpClient client = new DefaultHttpClient();

                HttpClient client = Common.getNewHttpClient();


                HttpResponse response = null;

                if (method == "POST") {
                    HttpPost post = new HttpPost(Common.URL_Upload_File);

//                    post.setHeader("Content-type", "application/x-www-form-urlencoded");
                    post.setHeader("Accept", "application/json");
                    // post.setHeader(Servicelist.AUTHORIZATION_HEADER,Servicelist.AUTHORIZATION_VAL);

                    MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                   /* try {
                        reqEntity.addPart("uid", new StringBody(new String(sharedPreferencesClass.getUSER_Id())));
                        reqEntity.addPart("lan", new StringBody(new String(sharedPreferencesClass.getSelected_Language())));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        customDialogClass.dismiss();

                    }*/

                    try {
                        reqEntity.addPart("file", new FileBody(new File(selectedFilePath)));

                        Logger.e("30/08 my Profile pic Upload Pic params ======> " + reqEntity + "");

                        post.setEntity(reqEntity);

                        response = client.execute(post);


                    } catch (Exception e) {
                        e.printStackTrace();
                        customDialogClass.dismiss();

                    }

                    /*UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                            arguments_values, "UTF-8");
                    post.setEntity(entity);
                    response = client.execute(post);*/
                }

                inStream = response.getEntity().getContent();
                StringBuffer sb = new StringBuffer();
                int chr;
                while ((chr = inStream.read()) != -1) {
                    sb.append((char) chr);
                }
                reply1 = sb.toString();

            } catch (Exception e) {

                try {
                    customDialogClass.dismiss();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                e.printStackTrace();
                return null;
            }
            return reply1;


           /* try {
                String s = "";

                int i = 0;
                while (i <= 50) {
                    try {
                        Thread.sleep(50);
                        publishProgress(i);
                        i++;
                    } catch (Exception e) {
                        Log.i("makemachine", e.getMessage());
                    }
                }

//            HttpClient httpclient = new DefaultHttpClient();

                HttpClient httpclient = Common.getNewHttpClient();

                HttpPost httppost = new HttpPost(Common.URL_Upload_File);

                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

//            MultipartEntity reqEntity = new MultipartEntity();

                reqEntity.addPart("file", new FileBody(new File(selectedFilePath)));
                httppost.setEntity(reqEntity);
                HttpResponse response = null;
                try {
                    response = httpclient.execute(httppost);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HttpEntity resEntity = response.getEntity();
                httpclient.getConnectionManager().shutdown();

                try {
                    return EntityUtils.toString(resEntity);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/


//            logClass.Log(context, "Read Response", s + "");
//            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            Logger.e("23/08 progress values======>" + values.length + "");
            Logger.e("23/08 progress values======>" + values + "");

            Logger.e("23/08 progress values 1111111111======>" + (values[0] * 2) + "%" + "");
            Logger.e("23/08 progress values 2222222222======>" + values[0] + "");


         /*   _percentField.setText((values[0] * 2) + "%");
            _percentField.setTextSize(values[0]);*/

        }

        @Override
        protected void onPostExecute(String result) {
            Logger.e("15/09 send message file upload response ==========>" + result + "");
            try {

                avi_load_file.setVisibility(View.GONE);
                iv_send_message.setEnabled(true);
                ll_file_selected.setVisibility(View.VISIBLE);


                if (result != null) {
                    Logger.e("23/08 not null ======> " + "");
                } else {
                    return;
                }

                String myJSON = result;

                JSONObject jsonObject = new JSONObject(myJSON);

                String error = jsonObject.optString("error");

                if (error.equalsIgnoreCase("") || error.length() == 0) {
                    JSONObject jsonObjectsuccess = jsonObject.optJSONObject("success");

                    final_full_path = jsonObjectsuccess.optString("full_path");
                    final_file_name = jsonObjectsuccess.optString("name");

                    Logger.e("23/08 Response full_path ======> " + final_full_path + "");

                    Logger.e("23/08 Response name ======> " + final_file_name + "");

                } else {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    selectedFilePath = "";
                    final_full_path = "";
                    final_file_name = "";

                    iv_attachment.setVisibility(View.VISIBLE);

                    iv_send_message.setEnabled(true);
                    ll_file_selected.setVisibility(View.GONE);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void SendMessageWebService() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("24/08 SendMessageWebService Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

//                        Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();


     /*   action	REQUIRED	send_message
        uid	REQUIRED	Logged-in User's UID
        to_uid	REQUIRED	Receiver's UID
        body	OPTIONAL	Message content
        file_name	OPTIONAL
        file_type	OPTIONAL	Required, if file_name is given: image or video
        body or file attachment is required. Can pass both as well.*/

        params.add(new BasicNameValuePair("action", "send_message"));

        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("to_uid", mParam1));
        params.add(new BasicNameValuePair("body", et_message.getText().toString()));
        params.add(new BasicNameValuePair("file_name", final_file_name));
        params.add(new BasicNameValuePair("file_type", str_file_type));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("24/08 SendMessageWebService Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            requestMakerBg = new RequestMakerBg(resp, params, context);
            requestMakerBg.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_message_cms);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMessageFill() {

        try {
            iv_attachment.setVisibility(View.VISIBLE);
            et_message.setText("");
            ll_file_selected.setVisibility(View.GONE);


            String id = "";
            String from_uid = "";
            String from_name = "";
            String from_profile_pic = "";
            String to_uid = "";
            String to_name = "";
            String to_profile_pic = "";

            String body = "";
            String stamp = "";
            String attachment = "";
            String is_sent = "";


            JSONObject jsonObjectUserDetails = new JSONObject(sharedPreferencesClass.getUSER_DETAILS());

            if (jsonArraysuccess.length() > 0) {
                JSONObject jsonObject1 = jsonArraysuccess.optJSONObject(0);

                id = jsonObject1.optString("id");
                from_uid = jsonObject1.optString("from_uid");
                from_name = jsonObject1.optString("from_name");
                from_profile_pic = jsonObjectUserDetails.optString("profile_pic");
                to_uid = jsonObject1.optString("to_uid");
                to_name = jsonObject1.optString("to_name");
                to_profile_pic = jsonObjectUserDetails.optString("profile_pic");

                body = jsonObject1.optString("body");
                stamp = jsonObject1.optString("stamp");
                attachment = jsonObject1.optString("attachment");
                is_sent = jsonObject1.optString("is_sent");

            } else {


                from_name = jsonObjectUserDetails.optString("fname") + " " + jsonObjectUserDetails.optString("lname");
                from_profile_pic = jsonObjectUserDetails.optString("profile_pic");
            }


            if (selectedFilePath.equalsIgnoreCase("") || selectedFilePath.length() == 0 || selectedFilePath == null) {
                attachment = "false";
            } else {

                JSONObject jsonObject11_attachment = new JSONObject();

                jsonObject11_attachment.put("path", selectedFilePath);
                jsonObject11_attachment.put("media_type", str_file_type);

                attachment = jsonObject11_attachment + "";

            }

            Logger.e("24/08 attachment ====> " + attachment + "");

            MessageListModel messageListModel = new MessageListModel();
            messageListModel.setId(id);
            messageListModel.setFrom_uid(sharedPreferencesClass.getUSER_Id());
            messageListModel.setFrom_name(from_name);
            messageListModel.setFrom_profile_pic(from_profile_pic);
            messageListModel.setTo_uid(mParam1);
            messageListModel.setTo_name(mParam2);
            messageListModel.setTo_profile_pic(to_profile_pic);
            messageListModel.setBody(str_et_message);
            messageListModel.setStamp(Common.Current_Date_Time());

            messageListModel.setAttachment(attachment);
            messageListModel.setIs_sent("1");
            messageListModel.setOnthespot(true);

//                        messageListModelArrayList.add(messageListModel);

                       /* messageListAdapter = new MessageListAdapter(context, messageListModelArrayList);
                        lv_messages.setAdapter(messageListAdapter);*/

            Logger.e("24/08 messageListAdapter.getCount ======>" + messageListAdapter.getCount() + "");

                      /*  if (messageListAdapter.getCount() > 0) {*/
            messageListAdapter.add(messageListModel);
/*

                        } else {
                            messageListAdapter = new MessageListAdapter(context, messageListModelArrayList);
                            lv_messages.setAdapter(messageListAdapter);
                        }
*/


            selectedFilePath = "";
            str_file_type = "";

            scrollMyListViewToBottom();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void scrollMyListViewToBottom() {
        lv_messages.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lv_messages.setSelection(messageListAdapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    // handle back button

                    MessageContactListFragment.on_back = true;
                    getActivity().getSupportFragmentManager().popBackStack();


                    return true;

                }

                return false;
            }
        });
    }
}
