package com.leadinfosoft.littleland.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.leadinfosoft.littleland.BuildConfig;
import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.FilePath;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.GetParentsDetailsForParticularClassid_NewPost;
import com.leadinfosoft.littleland.ModelClass.HomeChildListDetailsModel;
import com.leadinfosoft.littleland.ModelClass.HomeClassListDetails;
import com.leadinfosoft.littleland.ModelClass.UploadMulipleFileNewPost;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.DailyAttendance;
import com.leadinfosoft.littleland.activity.MainActivity;
import com.leadinfosoft.littleland.activity.Parent_Teacher_List_From_New_Post_Activity;
import com.leadinfosoft.littleland.adapter.UploadMultipleFileListNewPostAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.utill.Util;
import com.leadinfosoft.littleland.utill.Utils;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.vincent.videocompressor.VideoCompress;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class NewPostFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    Context context;

    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    EditText et_title, et_desc;
    TextView btn_submit;

    LinearLayout ll_teacher_spinner, ll_parent_spinner;

    TextView tv_select_class;

    TextView tv_select_parents, tv_select_teachers;

    TextView tv_lbl_upload_pics;

    RecyclerView rv_image_video_list;

    LinearLayout ll_take_file;

    CheckBox ch_remark;

    ArrayList<HomeClassListDetails> homeClassListDetailsArrayList = new ArrayList<>();

    ArrayList<String> class_nameArrayList = new ArrayList<>();
    ArrayList<String> class_nameArrayList_ar = new ArrayList<>();

    String str_selected_class_id = "";
    String str_selected_class_name = "";
    String str_selected_class_name_ar = "";


    ArrayList<GetParentsDetailsForParticularClassid_NewPost> getParentsDetailsForParticularClassid_newPostArrayList = new ArrayList<>();

    JSONArray jsonArraysuccess = null;

    String str_selected_parent_id_from_next_Screen = "";

    final int CAMERA_CAPTURE = 1;
    //captured picture uri
    private Uri picUri = null;

    final int PIC_CROP = 3;

    final int PICK_IMAGE_REQUEST = 2;

    static final int REQUEST_IMAGE_CAPTURE = 5;

    static final int REQUEST_VIDEO_CAPTURE = 21;

    private Bitmap bitmapgallery;

    String image = "";

    private String selectedFilePath;

    private static final int REQUEST_TAKE_GALLERY_VIDEO = 56;

    public static String str_file_type = "";

    String final_file_name = "";
    String final_file_type = "";


    AVLoadingIndicatorView avi_load_file;

    ArrayList<UploadMulipleFileNewPost> uploadMulipleFileNewPostArrayList = new ArrayList<>();

    UploadMulipleFileNewPost uploadMulipleFileNewPostModel = null;

    String str_is_opinion = "0";

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    String mCurrentPhotoPath = "";

    private String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();


    private long startTime, endTime;

    private File compressedImage;


    public NewPostFragment() {
        // Required empty public constructor
    }


    public static NewPostFragment newInstance(String param1, String param2) {
        NewPostFragment fragment = new NewPostFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        commonFunction();

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            rootView = inflater.inflate(R.layout.fragment_new_post_ar, container, false);

        } else {
            rootView = inflater.inflate(R.layout.fragment_new_post, container, false);


        }

        init(rootView);

        initHeader();

        setTypeface_Text();

        GetClassDetails();

        uploadMulipleFileNewPostArrayList.clear();

        if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase(Common.USER_TYPE_TEACHER)) {
            GetParentsByClassId_ForTeacher();

        } else {
            GetTeachersByClassId_ForParents();

        }

        return rootView;
    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            tv_select_class.setTypeface(tf);
            tv_select_parents.setTypeface(tf);
            tv_select_teachers.setTypeface(tf);
            tv_lbl_upload_pics.setTypeface(tf);

            MainActivity.tv_title.setTypeface(tf);
            MainActivity.tv_subtitle.setTypeface(tf);

            et_desc.setTypeface(tf);
            et_title.setTypeface(tf);
            ch_remark.setTypeface(tf);
            btn_submit.setTypeface(tf);


            et_title.setHint(Common.filter("lbl_message_title", "ar").toUpperCase());
            et_desc.setHint(Common.filter("lbl_message", "ar").toUpperCase());


            MainActivity.tv_title.setText(Common.filter("lbl_new_post", "ar"));
            tv_lbl_upload_pics.setText(Common.filter("lbl_upload_yours_photos_and_video", "ar"));
            ch_remark.setText(Common.filter("lbl_ask_for_opinion", "ar"));
            btn_submit.setText(Common.filter("lbl_submit", "ar"));

        } else {
            tv_select_class.setTypeface(tf);
            tv_select_parents.setTypeface(tf);
            tv_select_teachers.setTypeface(tf);
            tv_lbl_upload_pics.setTypeface(tf);

            MainActivity.tv_title.setTypeface(tf);
            MainActivity.tv_subtitle.setTypeface(tf);

            et_desc.setTypeface(tf);
            et_title.setTypeface(tf);
            ch_remark.setTypeface(tf);
            btn_submit.setTypeface(tf);


            et_title.setHint(Common.filter("lbl_message_title", "en").toUpperCase());
            et_desc.setHint(Common.filter("lbl_message", "en").toUpperCase());


            MainActivity.tv_title.setText(Common.filter("lbl_new_post", "en"));
            tv_lbl_upload_pics.setText(Common.filter("lbl_upload_yours_photos_and_video", "en"));
            ch_remark.setText(Common.filter("lbl_ask_for_opinion", "en"));
            btn_submit.setText(Common.filter("lbl_submit", "en"));
        }

        tv_select_teachers.setTypeface(tf_numeric);
        tv_select_parents.setTypeface(tf_numeric);

    }

    private void GetClassDetails() {

        if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase(Common.USER_TYPE_TEACHER)) {
            JSONArray jsonArrayClass = null;
            try {
                jsonArrayClass = new JSONArray(sharedPreferencesClass.getGet_Initial_Class_Details_Array());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sharedPreferencesClass.setGet_Initial_Class_Details_Array(jsonArrayClass + "");

            if (jsonArrayClass.length() > 0) {
                homeClassListDetailsArrayList.clear();
                class_nameArrayList.clear();
                class_nameArrayList_ar.clear();

                for (int i = 0; i < jsonArrayClass.length(); i++) {
                    JSONObject jsonObject1 = jsonArrayClass.optJSONObject(i);

//                                    "class_id":"18",
//                                            "class_name":"Blue Bells L2",
//                                            "class_name_ar":"Blue Bells L2"

                    String class_id = jsonObject1.optString("id");
                    String class_name = jsonObject1.optString("class_name");
                    String class_name_ar = jsonObject1.optString("class_name_ar");

                    HomeClassListDetails homeClassListDetails = new HomeClassListDetails();
                    homeClassListDetails.setClass_id(class_id);
                    homeClassListDetails.setClass_name(class_name);
                    homeClassListDetails.setClass_name_ar(class_name_ar);
                    homeClassListDetailsArrayList.add(homeClassListDetails);

                    class_nameArrayList.add(class_name);
                    class_nameArrayList_ar.add(class_name_ar);


                }


            } else {
                homeClassListDetailsArrayList.clear();
                class_nameArrayList.clear();
                class_nameArrayList_ar.clear();


            }
        } else {
            //Parents Class Details
            JSONArray jsonArrayClass = null;
            try {
                jsonArrayClass = new JSONArray(sharedPreferencesClass.getGet_Initial_Child_Details_Array());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sharedPreferencesClass.setGet_Initial_Class_Details_Array(jsonArrayClass + "");

            if (jsonArrayClass.length() > 0) {
                homeClassListDetailsArrayList.clear();
                class_nameArrayList.clear();
                class_nameArrayList_ar.clear();

                for (int i = 0; i < jsonArrayClass.length(); i++) {

                    JSONObject jsonObject1 = jsonArrayClass.optJSONObject(i);

//                                    "class_id":"18",
//                                            "class_name":"Blue Bells L2",
//                                            "class_name_ar":"Blue Bells L2"

                    String class_id = jsonObject1.optString("class_id");
                    String class_name = jsonObject1.optString("class_name");
                    String class_name_ar = jsonObject1.optString("class_name_ar");

                    HomeClassListDetails homeClassListDetails = new HomeClassListDetails();
                    homeClassListDetails.setClass_id(class_id);
                    homeClassListDetails.setClass_name(class_name);
                    homeClassListDetails.setClass_name_ar(class_name_ar);
                    homeClassListDetailsArrayList.add(homeClassListDetails);

                    class_nameArrayList.add(class_name);
                    class_nameArrayList_ar.add(class_name_ar);


                }


            } else {
                homeClassListDetailsArrayList.clear();
                class_nameArrayList.clear();
                class_nameArrayList_ar.clear();


            }
        }

    }

    private void init(View rootView) {

        et_title = (EditText) rootView.findViewById(R.id.et_title);
        et_desc = (EditText) rootView.findViewById(R.id.et_desc);

        btn_submit = (TextView) rootView.findViewById(R.id.btn_submit);

        tv_select_class = (TextView) rootView.findViewById(R.id.tv_select_class);
        tv_select_class.setOnClickListener(this);

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            tv_select_class.setText(sharedPreferencesClass.getSelected_Class_Name_ar());

        } else {
            tv_select_class.setText(sharedPreferencesClass.getSelected_Class_Name());

        }


        str_selected_class_name = sharedPreferencesClass.getSelected_Class_Name();
        str_selected_class_name_ar = sharedPreferencesClass.getSelected_Class_Name_ar();
        str_selected_class_id = sharedPreferencesClass.getSelected_Class_Id();

        tv_select_parents = (TextView) rootView.findViewById(R.id.tv_select_parents);
        tv_select_parents.setOnClickListener(this);

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            tv_select_parents.setText(Common.filter("lbl_Select_Parents", "ar") + " (" + "0" + ")");

        } else {
            tv_select_parents.setText(Common.filter("lbl_Select_Parents", "en") + " (" + "0" + ")");

        }


        tv_select_teachers = (TextView) rootView.findViewById(R.id.tv_select_teachers);
        tv_select_teachers.setOnClickListener(this);

        tv_lbl_upload_pics = (TextView) rootView.findViewById(R.id.tv_lbl_upload_pics);
        tv_lbl_upload_pics.setOnClickListener(this);

        rv_image_video_list = (RecyclerView) rootView.findViewById(R.id.rv_image_video_list);
        rv_image_video_list.setVisibility(View.GONE);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_image_video_list.setLayoutManager(llm);

        ch_remark = (CheckBox) rootView.findViewById(R.id.ch_remark);

        ch_remark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // perform logic

                    str_is_opinion = "1";
                } else {
                    str_is_opinion = "0";


                }

            }
        });

        ll_take_file = (LinearLayout) rootView.findViewById(R.id.ll_take_file);
        ll_take_file.setOnClickListener(this);

        avi_load_file = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi_load_file);
        avi_load_file.setVisibility(View.GONE);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetFileName();

                if (isValid()) {
                    NewPostWebService();

                }
            }
        });

        ll_teacher_spinner = (LinearLayout) rootView.findViewById(R.id.ll_teacher_spinner);
        ll_parent_spinner = (LinearLayout) rootView.findViewById(R.id.ll_parent_spinner);

        if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase(Common.USER_TYPE_TEACHER)) {
            ll_teacher_spinner.setVisibility(View.VISIBLE);
            ll_parent_spinner.setVisibility(View.GONE);

        } else {
            ll_teacher_spinner.setVisibility(View.GONE);
            ll_parent_spinner.setVisibility(View.VISIBLE);
        }

    }

    private void GetFileName() {


        if (uploadMulipleFileNewPostArrayList.size() > 0) {
            rv_image_video_list.setVisibility(View.VISIBLE);

            final_file_name = "";
            final_file_type = "";

            for (int i = 0; i < uploadMulipleFileNewPostArrayList.size(); i++) {
                final_file_name += uploadMulipleFileNewPostArrayList.get(i).getName() + ",";

                final_file_type += uploadMulipleFileNewPostArrayList.get(i).getFile_type() + ",";

            }

            final_file_name = final_file_name.replaceAll(",$", "");

            if (final_file_name.endsWith(",")) {
                final_file_name = final_file_name.substring(0, final_file_name.length() - 1);
            }

            final_file_type = final_file_type.replaceAll(",$", "");

            if (final_file_type.endsWith(",")) {
                final_file_type = final_file_type.substring(0, final_file_type.length() - 1);
            }


        } else {
            rv_image_video_list.setVisibility(View.GONE);

        }
    }

    private boolean isValid() {

        Boolean is_valid = true;

        /*if (str_selected_class_id.equalsIgnoreCase("") || str_selected_class_id.length() == 0) {
            tv_select_class.setError("Please Select Class");
            is_valid = false;
        } else if (!str_selected_class_id.equalsIgnoreCase("") || str_selected_class_id.length() != 0) {
            tv_select_class.setError(null);
            is_valid = true;

        } else*/
        if (et_title.getText().toString().equalsIgnoreCase("") || et_title.getText().toString().length() == 0) {

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                et_title.setError(Common.filter("error_Please_Enter_Title", "ar"));

            } else {
                et_title.setError(Common.filter("error_Please_Enter_Title", "en"));


            }

            is_valid = false;
        } else if (et_desc.getText().toString().equalsIgnoreCase("") || et_desc.getText().toString().length() == 0) {

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                et_desc.setError(Common.filter("error_Please_Enter_Description", "ar"));

            } else {
                et_desc.setError(Common.filter("error_Please_Enter_Description", "en"));


            }


            is_valid = false;
        }


        return is_valid;
    }

    private void commonFunction() {
        context = getActivity();
        con = new ConnectionDetector(context);
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

        MainActivity.iv_profile_pic.setVisibility(View.GONE);


        MainActivity.tv_title.setText("NEW POST");
        MainActivity.tv_subtitle.setVisibility(View.VISIBLE);
        MainActivity.tv_subtitle.setTextColor(getActivity().getResources().getColor(R.color.techer_parpal));


        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

        } else {
            MainActivity.tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


        }

//        MainActivity.tv_subtitle.setText("CLASS - " + sharedPreferencesClass.getSelected_Class_Name());

        getActivity().findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
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
    @Override
    public void onClick(View v) {

        if (v == tv_select_class) {

            final Dialog dialog = new Dialog(context);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before


            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                dialog.setContentView(R.layout.custom_class_list_ar);

            } else {
                dialog.setContentView(R.layout.custom_class_list);

            }

            dialog.setTitle("Title...");

            // set the custom dialog components - text, image and button

            ListView lv_class = (ListView) dialog.findViewById(R.id.lv_class);

            if (homeClassListDetailsArrayList.size() > 0) {


                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, class_nameArrayList_ar);
                    lv_class.setAdapter(adapter);
                } else {
                    ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, class_nameArrayList);
                    lv_class.setAdapter(adapter);
                }


                lv_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                Toast.makeText(context, "Click Class Name =====> " + homeClassListDetailsArrayList.get(position).getClass_name() + "=====> Class Id========> " + homeClassListDetailsArrayList.get(position).getClass_id(), Toast.LENGTH_SHORT).show();


                        str_selected_class_id = homeClassListDetailsArrayList.get(position).getClass_id();
                        str_selected_class_name = homeClassListDetailsArrayList.get(position).getClass_name();
                        str_selected_class_name_ar = homeClassListDetailsArrayList.get(position).getClass_name_ar();


                        Logger.e("Select Class id from new post =======> " + str_selected_class_id + "");
                        Logger.e("Select Class name from new post =======> " + str_selected_class_name + "");

                        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                            tv_select_class.setText(str_selected_class_name_ar);
                        } else {
                            tv_select_class.setText(str_selected_class_name);
                        }

//                        tv_select_class.setText(str_selected_class_name);

                        if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase(Common.USER_TYPE_TEACHER)) {
                            GetParentsByClassId_ForTeacher();

                        } else {
                            GetTeachersByClassId_ForParents();

                        }


                        dialog.dismiss();
                    }
                });

            }


            dialog.show();
        } else if (v == tv_select_parents) {

            if (str_selected_class_id.equalsIgnoreCase("") || str_selected_class_id.length() == 0) {
                Toast.makeText(context, "Please Select Class", Toast.LENGTH_SHORT).show();
                return;
            }

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                tv_select_parents.setText(Common.filter("lbl_Select_Parents", "ar") + " (" + "0" + ")");

            } else {
                tv_select_parents.setText(Common.filter("lbl_Select_Parents", "en") + " (" + "0" + ")");

            }

            str_selected_parent_id_from_next_Screen = "";

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                Intent i = new Intent(context, Parent_Teacher_List_From_New_Post_Activity.class);
                i.putExtra("teacher_parent_list_array", jsonArraysuccess + "");
                i.putExtra("class_name", str_selected_class_name_ar);
                startActivityForResult(i, 121);
            } else {
                Intent i = new Intent(context, Parent_Teacher_List_From_New_Post_Activity.class);
                i.putExtra("teacher_parent_list_array", jsonArraysuccess + "");
                i.putExtra("class_name", str_selected_class_name);
                startActivityForResult(i, 121);
            }


        } else if (v == tv_select_teachers) {

            if (str_selected_class_id.equalsIgnoreCase("") || str_selected_class_id.length() == 0) {
                Toast.makeText(context, "Please Select Class", Toast.LENGTH_SHORT).show();
                return;
            }


            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                tv_select_parents.setText(Common.filter("lbl_Select_Parents", "ar") + " (" + "0" + ")");

            } else {
                tv_select_parents.setText(Common.filter("lbl_Select_Parents", "en") + " (" + "0" + ")");

            }


            str_selected_parent_id_from_next_Screen = "";

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                Intent i = new Intent(context, Parent_Teacher_List_From_New_Post_Activity.class);
                i.putExtra("teacher_parent_list_array", jsonArraysuccess + "");
                i.putExtra("class_name", str_selected_class_name_ar);

                startActivityForResult(i, 121);
            } else {
                Intent i = new Intent(context, Parent_Teacher_List_From_New_Post_Activity.class);
                i.putExtra("teacher_parent_list_array", jsonArraysuccess + "");
                i.putExtra("class_name", str_selected_class_name);

                startActivityForResult(i, 121);
            }


        } else if (v == ll_take_file) {
            setPermission();
//            pickImage();
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
//                dispatchTakePictureFromCameraIntent();
                try {
                    dispatchTakePictureIntent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                try {
                    dispatchTakeVideoIntent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

//            picUri = Uri.fromFile(imageFile); // convert path to Uri


           /* picUri = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    imageFile);*/

//            picUri = FileProvider.getUriForFile(context, getActivity().getPackageName(), imageFile);

            picUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".FileProvider", imageFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
            startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Logger.e("12/01 mCurrentPhotoPath ----->" + mCurrentPhotoPath + "");


        return image;
    }

    private void dispatchTakePictureIntent() throws IOException {

        str_file_type = "image";

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            photoFile = createImageFile();

            Logger.e("12/01 photoFile ------>" + photoFile + "");

            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Uri photoURI = Uri.fromFile(createImageFile());
                Uri photoURI = FileProvider.getUriForFile(context,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
            }
        }
    }

    private void dispatchTakeVideoIntent() throws IOException {

        str_file_type = "video";

     /*   Random r = new Random();
        int i1 = r.nextInt(80 - 65) + 65;

        String image_name = "/" + "picture" + i1 + "" + ".mp4";

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + image_name*//*"/picture.jpg"*//*;
        File imageFile = new File(imageFilePath);

//        picUri = Uri.fromFile(imageFile); // convert path to Uri

        picUri = FileProvider.getUriForFile(context,
                BuildConfig.APPLICATION_ID + ".provider",
                imageFile);


        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);*/


        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File videoFile = null;

            videoFile = createVideoFile();

            Logger.e("12/01 videoFile ------>" + videoFile + "");

            // Continue only if the File was successfully created
            if (videoFile != null) {
//                Uri photoURI = Uri.fromFile(createImageFile());
                Uri videoURI = FileProvider.getUriForFile(context,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createVideoFile());
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        }


    }


    private File createVideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "mp4_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Logger.e("12/01 mCurrentPhotoPath ----->" + mCurrentPhotoPath + "");


        return image;
    }


    private void SelectVideoFromGallery() {

        str_file_type = "video";

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);

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


    public void GetParentsByClassId_ForTeacher() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetParentsByClassId_ForTeacher Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();


                        jsonArraysuccess = jsonObject.optJSONArray("success");

                        if (jsonArraysuccess.length() > 0) {

                            getParentsDetailsForParticularClassid_newPostArrayList.clear();

                            for (int i = 0; i < jsonArraysuccess.length(); i++) {
                                JSONObject jsonObject1 = jsonArraysuccess.optJSONObject(i);

                                String uid = jsonObject1.optString("uid");
                                String fname = jsonObject1.optString("uid");
                                String lname = jsonObject1.optString("uid");
                                String profile_pic = jsonObject1.optString("uid");
                                String type = jsonObject1.optString("uid");

                                GetParentsDetailsForParticularClassid_NewPost getParentsDetailsForParticularClassid_newPost = new GetParentsDetailsForParticularClassid_NewPost();
                                getParentsDetailsForParticularClassid_newPost.setUid(uid);
                                getParentsDetailsForParticularClassid_newPost.setFname(fname);
                                getParentsDetailsForParticularClassid_newPost.setLname(lname);
                                getParentsDetailsForParticularClassid_newPost.setProfile_pic(profile_pic);
                                getParentsDetailsForParticularClassid_newPost.setType(type);

                                getParentsDetailsForParticularClassid_newPostArrayList.add(getParentsDetailsForParticularClassid_newPost);


                            }


                        } else {
                            //length 0
                            getParentsDetailsForParticularClassid_newPostArrayList.clear();

                        }

                        Logger.e("getParentsDetailsForParticularClassid_newPostArrayList size===========> " + getParentsDetailsForParticularClassid_newPostArrayList.size() + "");


                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();


        params.add(new BasicNameValuePair("action", "get_parents_by_class_id"));
        params.add(new BasicNameValuePair("class_id", str_selected_class_id));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("GetParentsByClassId_ForTeacher Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_Get_Data);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }

    }

    public void GetTeachersByClassId_ForParents() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetTeachersByClassId_ForParents Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

                        jsonArraysuccess = jsonObject.optJSONArray("success");

                        if (jsonArraysuccess.length() > 0) {

                            getParentsDetailsForParticularClassid_newPostArrayList.clear();

                            for (int i = 0; i < jsonArraysuccess.length(); i++) {
                                JSONObject jsonObject1 = jsonArraysuccess.optJSONObject(i);

                                String uid = jsonObject1.optString("uid");
                                String fname = jsonObject1.optString("uid");
                                String lname = jsonObject1.optString("uid");
                                String profile_pic = jsonObject1.optString("uid");
                                String type = jsonObject1.optString("uid");

                                GetParentsDetailsForParticularClassid_NewPost getParentsDetailsForParticularClassid_newPost = new GetParentsDetailsForParticularClassid_NewPost();
                                getParentsDetailsForParticularClassid_newPost.setUid(uid);
                                getParentsDetailsForParticularClassid_newPost.setFname(fname);
                                getParentsDetailsForParticularClassid_newPost.setLname(lname);
                                getParentsDetailsForParticularClassid_newPost.setProfile_pic(profile_pic);
                                getParentsDetailsForParticularClassid_newPost.setType(type);

                                getParentsDetailsForParticularClassid_newPostArrayList.add(getParentsDetailsForParticularClassid_newPost);


                            }


                        } else {
                            //length 0
                            getParentsDetailsForParticularClassid_newPostArrayList.clear();

                        }

                        Logger.e("GetTeachersByClassId_ForParents getParentsDetailsForParticularClassid_newPostArrayList size===========> " + getParentsDetailsForParticularClassid_newPostArrayList.size() + "");


                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();


        params.add(new BasicNameValuePair("action", "get_teachers_by_class_id"));
        params.add(new BasicNameValuePair("class_id", str_selected_class_id));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("GetTeachersByClassId_ForParents Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_Get_Data);
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


            if (requestCode == 121) {
                str_selected_parent_id_from_next_Screen = data.getStringExtra("id");

                String count = data.getStringExtra("count");

                Logger.e("22/08 test ========> " + str_selected_parent_id_from_next_Screen + "");

                String CurrentString = str_selected_parent_id_from_next_Screen;
                String[] separated = CurrentString.split(",");

                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    tv_select_parents.setText(Common.filter("lbl_Select_Parents", "ar") + " (" + count + "" + ")");

                } else {
                    tv_select_parents.setText(Common.filter("lbl_Select_Parents", "en") + " (" + count + "" + ")");

                }

            } else if (requestCode == CAMERA_CAPTURE) {


                Logger.e("12/01 CAMERA_CAPTURE mCurrentPhotoPath ----> " + mCurrentPhotoPath + "");

                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                File file = new File(imageUri.getPath());

                Logger.e("12/01 imageUri getPath -----> " + imageUri.getPath() + "");

          /*      BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);

                bitmap = Bitmap.createScaledBitmap(bitmap,true);*/

                try {
                    InputStream ims = new FileInputStream(file);

                    Bitmap bitmap = BitmapFactory.decodeStream(ims);

//                    ivPreview.setImageBitmap(BitmapFactory.decodeStream(ims));
                } catch (FileNotFoundException e) {
                    return;
                }

                if (file.exists()) {

                    Logger.e("12/01 file exists");
                }
//Do something
                else {
                    Logger.e("12/01 file not exists");

                    return;
                }

                selectedFilePath = imageUri.getPath();

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    Logger.e("23/08 file name ======> " + selectedFilePath);

                    InputStream ims = null;
                    try {
                        ims = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Bitmap bitmap = BitmapFactory.decodeStream(ims);

//                    Bitmap bitmap = BitmapFactory.decodeFile(selectedFilePath);
/*
                    try {


                        Bitmap bitmap_rotate = rotateImageIfRequired(bitmap, context, imageUri);
                        Logger.e("29/01 bitmap_rotate ----> " + bitmap_rotate + "");

                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(selectedFilePath);
                            bitmap_rotate.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                            // PNG is a lossless format, the compression factor (100) is ignored
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if (con.isConnectingToInternet()) {
                            UploadFileWebService uploadFileWebService = new UploadFileWebService();
                            uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    new Rotation_Check(bitmap, imageUri).execute();


                   /* if (con.isConnectingToInternet()) {
                        UploadFileWebService uploadFileWebService = new UploadFileWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }*/

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }

                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(context,
                        new String[]{imageUri.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {

                                Logger.e("12/01 onScanCompleted ------> " + "Done");

//                                Toast.makeText(context, "onScanCompleted", Toast.LENGTH_SHORT).show();

                            }
                        });


//get the Uri for the captured image

               /* Uri uri = picUri;

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
// Do something else.


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
                }*/

//            performCrop();
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

            } else if (requestCode == PICK_IMAGE_REQUEST) {

                if (data == null) {
                    //no data present
                    return;
                }

                picUri = data.getData();

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(context, selectedFileUri);
                Logger.e("23/08  ====> " + "Selected Gallery File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    Logger.e("23/08 file name ======> " + selectedFilePath);

                    customCompressImage();


                    /*if (con.isConnectingToInternet()) {
                        UploadFileWebService uploadFileWebService = new UploadFileWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }*/

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

                    String str_file_name = CompressVideo(selectedFilePath);
                    Logger.e("21/02 final file name ----> " + str_file_name);


                   /* if (con.isConnectingToInternet()) {
                        UploadFileWebService uploadFileWebService = new UploadFileWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }*/


                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
           /* Bitmap imageBitmap = (Bitmap) extras.get("data");
            Imview.setImageBitmap(imageBitmap);*/
                Toast.makeText(context, "Here " + data.getData(), Toast.LENGTH_LONG).show();
            } else if (requestCode == REQUEST_VIDEO_CAPTURE) {


                Logger.e("12/01 REQUEST_VIDEO_CAPTURE mCurrentPhotoPath ----> " + mCurrentPhotoPath + "");

                Uri videoUri = Uri.parse(mCurrentPhotoPath);
                File file = new File(videoUri.getPath());

                Logger.e("12/01 videoUri getPath -----> " + videoUri.getPath() + "");

                try {
                    InputStream ims = new FileInputStream(file);
//                    ivPreview.setImageBitmap(BitmapFactory.decodeStream(ims));
                } catch (FileNotFoundException e) {
                    return;
                }

                if (file.exists()) {

                    Logger.e("12/01 file exists");
                }
//Do something
                else {
                    Logger.e("12/01 file not exists");

                    return;
                }

                selectedFilePath = videoUri.getPath();

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
//                tvFileName.setText(selectedFilePath);

                    String str_file_name = CompressVideo(selectedFilePath);
                    Logger.e("21/02 final file name ----> " + str_file_name);

                    Logger.e("23/08 video file name ======> " + selectedFilePath);
/*
                    if (con.isConnectingToInternet()) {
                        UploadFileWebService uploadFileWebService = new UploadFileWebService();
                        uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                    }*/

                } else {
                    Toast.makeText(context, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }

                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(context,
                        new String[]{videoUri.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {

                                Logger.e("12/01 onScanCompleted ------> " + "Done");

//                                Toast.makeText(context, "onScanCompleted", Toast.LENGTH_SHORT).show();

                            }
                        });

               /* Uri uri = picUri;

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
                }*/

            }
        }
    }

    private String CompressVideo(String filename) {


       /* Configuration config = new Configuration();
        final Locale locale = config.locale;*/
        final String destPath = outputDir + File.separator + "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";


        VideoCompress.compressVideoLow(filename, destPath, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {
               /* tv_indicator.setText("Compressing..." + "\n"
                        + "Start at: " + new SimpleDateFormat("HH:mm:ss", locale).format(new Date()));
                pb_compress.setVisibility(View.VISIBLE);*/

                avi_load_file.setVisibility(View.VISIBLE);

                startTime = System.currentTimeMillis();
                Util.writeFile(context, "Start at: " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "\n");
            }

            @Override
            public void onSuccess() {
                /*String previous = tv_indicator.getText().toString();
                tv_indicator.setText(previous + "\n"
                        + "Compress Success!" + "\n"
                        + "End at: " + new SimpleDateFormat("HH:mm:ss", locale).format(new Date()));
                pb_compress.setVisibility(View.INVISIBLE);*/

                avi_load_file.setVisibility(View.GONE);


                endTime = System.currentTimeMillis();
                Util.writeFile(context, "End at: " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "\n");
                Util.writeFile(context, "Total: " + ((endTime - startTime) / 1000) + "s" + "\n");
                Util.writeFile(context);


                selectedFilePath = destPath;

                if (con.isConnectingToInternet()) {
                    UploadFileWebService uploadFileWebService = new UploadFileWebService();
                    uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFail() {
              /*  tv_indicator.setText("Compress Failed!");
                pb_compress.setVisibility(View.INVISIBLE);*/


                avi_load_file.setVisibility(View.GONE);

                Toast.makeText(context, "Fail To Upload Video", Toast.LENGTH_SHORT).show();


                endTime = System.currentTimeMillis();
                Util.writeFile(context, "Failed Compress!!!" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }

            @Override
            public void onProgress(float percent) {
//                tv_progress.setText(String.valueOf(percent) + "%");
            }
        });

        Logger.e("21/02 destPath ----> " + destPath + "");

        return destPath;

    }

    class Rotation_Check extends AsyncTask<Void, Void, Void> {

        Bitmap bitmap_new;
        Uri imageUri_new;

        public Rotation_Check(Bitmap bitmap, Uri imageUri) {

            this.bitmap_new = bitmap;
            this.imageUri_new = imageUri;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            avi_load_file.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //Record method

            try {


                Bitmap bitmap_rotate = rotateImageIfRequired(bitmap_new, context, imageUri_new);
                Logger.e("29/01 bitmap_rotate ----> " + bitmap_rotate + "");

                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(selectedFilePath);
                    bitmap_rotate.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            customCompressImage();

          /*  if (con.isConnectingToInternet()) {
                UploadFileWebService uploadFileWebService = new UploadFileWebService();
                uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
            }*/

        }
    }


    public void customCompressImage() {
        if (selectedFilePath == null || selectedFilePath.equalsIgnoreCase("") || selectedFilePath.length() == 0) {


            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {


                Toast.makeText(context, Common.filter("Please Select Image", "ar"), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, Common.filter("Please Select Image", "en"), Toast.LENGTH_SHORT).show();


            }

            return;


        } else {
            // Compress image in main thread using custom Compressor

            File imgFile = new File(selectedFilePath);
//                Uri filename = Uri.fromFile(new File(selectedFilePath));


              /*  compressedImage = new Compressor(context)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(imgFile);

                setCompressedImage();
*/

            new Compressor(context)
                    .compressToFileAsFlowable(imgFile)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            compressedImage = file;
                            Logger.e("22/02 compressedImage ======> "+ compressedImage + "");
                            setCompressedImage();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
//                                showError(throwable.getMessage());
                        }
                    });


        }
    }

    private void setCompressedImage() {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

       /* Bitmap bm = BitmapFactory.decodeFile(strPath,options);
        imageView.setImageBitmap(bm);*/

//        compressedImageView.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getAbsolutePath(),options));
//        compressedSizeTextView.setText(String.format("Size : %s", getReadableFileSize(compressedImage.length())));

//        Toast.makeText(context, "Compressed image save in " + compressedImage.getPath(), Toast.LENGTH_LONG).show();

        Logger.e("22/02 Compressor getAbsolutePath ----> " + compressedImage.getAbsolutePath());


        Logger.e("22/02 Compressor ----> " + "Compressed image save in " + compressedImage.getPath());


        selectedFilePath = compressedImage.getPath();

        if (con.isConnectingToInternet()) {
            UploadFileWebService uploadFileWebService = new UploadFileWebService();
            uploadFileWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }




    }


    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

      /*  if (selectedImage.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {*/
        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Logger.e("orientation: %s" + orientation);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
//        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    class UploadFileWebService extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            avi_load_file.setVisibility(View.VISIBLE);
            ll_take_file.setEnabled(false);
            btn_submit.setEnabled(false);
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

                        Logger.e("22/02 selectedFilePathupload server param ----> " + selectedFilePath + "");

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
//            MultipartEntity reqEntity = new MultipartEntity();
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);


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
            }
*/


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
            Logger.e("15/09 new post file upload response ==========>" + result + "");
            try {

                avi_load_file.setVisibility(View.GONE);
                ll_take_file.setEnabled(true);
                btn_submit.setEnabled(true);


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

                    String full_path = jsonObjectsuccess.optString("full_path");
                    final_file_name = jsonObjectsuccess.optString("name");
                    String thumb = jsonObjectsuccess.optString("thumb");


                    Logger.e("23/08 Response full_path ======> " + full_path + "");

                    Logger.e("23/08 Response name ======> " + final_file_name + "");

                    uploadMulipleFileNewPostModel = new UploadMulipleFileNewPost();
                    uploadMulipleFileNewPostModel.setFull_path(full_path);
                    uploadMulipleFileNewPostModel.setName(final_file_name);
                    uploadMulipleFileNewPostModel.setFile_type(str_file_type);
                    uploadMulipleFileNewPostModel.setThumb(thumb);

                    uploadMulipleFileNewPostArrayList.add(uploadMulipleFileNewPostModel);

                    Logger.e("23/08 Response uploadMulipleFileNewPostArrayList size ======> " + uploadMulipleFileNewPostArrayList.size() + "");

                    UploadMultipleFileListNewPostAdapter uploadMultipleFileListNewPostAdapter = new UploadMultipleFileListNewPostAdapter(context, true, uploadMulipleFileNewPostArrayList);
                    rv_image_video_list.setAdapter(uploadMultipleFileListNewPostAdapter);
                    uploadMultipleFileListNewPostAdapter.notifyDataSetChanged();


                    if (uploadMulipleFileNewPostArrayList.size() > 0) {
                        rv_image_video_list.setVisibility(View.VISIBLE);

                        final_file_name = "";
                        final_file_type = "";

                        for (int i = 0; i < uploadMulipleFileNewPostArrayList.size(); i++) {
                            final_file_name += uploadMulipleFileNewPostArrayList.get(i).getName() + ",";

                            final_file_type += uploadMulipleFileNewPostArrayList.get(i).getFile_type() + ",";

                        }

                        final_file_name = final_file_name.replaceAll(",$", "");

                        if (final_file_name.endsWith(",")) {
                            final_file_name = final_file_name.substring(0, final_file_name.length() - 1);
                        }

                        final_file_type = final_file_type.replaceAll(",$", "");

                        if (final_file_type.endsWith(",")) {
                            final_file_type = final_file_type.substring(0, final_file_type.length() - 1);
                        }


                    } else {
                        rv_image_video_list.setVisibility(View.GONE);

                    }


                } else {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void NewPostWebService() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    btn_submit.setEnabled(true);

                    Logger.e("NewPostWebService Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

                        Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

                        getActivity().getSupportFragmentManager().popBackStack();

                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();


      /*  action REQUIRED new_post
        type REQUIRED private
        uid REQUIRED Logged - in User 's UID
        class_id REQUIRED Class ID
        selected_uids OPTIONAL Comma separated user ids
        title REQUIRED
        description REQUIRED
        file_name OPTIONAL
        file_type OPTIONAL Required,if file_name is given:
        image or video
        sending_from REQUIRED teacher*/

        params.add(new BasicNameValuePair("action", "new_post"));

        params.add(new BasicNameValuePair("type", "private"));
        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));

        params.add(new BasicNameValuePair("class_id", str_selected_class_id));
        params.add(new BasicNameValuePair("selected_uids", str_selected_parent_id_from_next_Screen));
        params.add(new BasicNameValuePair("title", et_title.getText().toString()));
        params.add(new BasicNameValuePair("description", et_desc.getText().toString()));
        params.add(new BasicNameValuePair("file_name", final_file_name));
        params.add(new BasicNameValuePair("file_type", final_file_type));
        params.add(new BasicNameValuePair("sending_from", sharedPreferencesClass.getUser_Type()));
        params.add(new BasicNameValuePair("is_opinion", str_is_opinion));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("NewPostWebService Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {

            btn_submit.setEnabled(false);

            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_New_Post_Or_Direct_Message);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }
}
