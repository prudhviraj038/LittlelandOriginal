package com.leadinfosoft.littleland.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.ContactListModel;
import com.leadinfosoft.littleland.ModelClass.GetParentsDetailsForParticularClassid_NewPost;
import com.leadinfosoft.littleland.ModelClass.HomeClassListDetails;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.MainActivity;
import com.leadinfosoft.littleland.activity.Parent_Teacher_List_For_Contact_Activity;
import com.leadinfosoft.littleland.activity.Parent_Teacher_List_From_New_Post_Activity;
import com.leadinfosoft.littleland.adapter.MessageContactListDetailsAdapter;
import com.leadinfosoft.littleland.adapter.MessageListAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.utill.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MessageContactListFragment extends Fragment implements View.OnClickListener {

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

    ListView lv_contact_list;
    TextView tv_no_conversions;

    ArrayList<ContactListModel> contactListModelArrayList = new ArrayList<>();
    MessageContactListDetailsAdapter messageContactListDetailsAdapter = null;

    ArrayList<HomeClassListDetails> homeClassListDetailsArrayList = new ArrayList<>();

    ArrayList<String> class_nameArrayList = new ArrayList<>();
    ArrayList<String> class_nameArrayList_ar = new ArrayList<>();

    String str_selected_class_id = "";
    String str_selected_class_name = "";


    ArrayList<GetParentsDetailsForParticularClassid_NewPost> getParentsDetailsForParticularClassid_newPostArrayList = new ArrayList<>();

    JSONArray jsonArraysuccess = null;

    String str_selected_parent_id_from_next_Screen = "";

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    public static Boolean on_back = false;


    public MessageContactListFragment() {
        // Required empty public constructor
    }


    public static MessageContactListFragment newInstance(String param1, String param2) {
        MessageContactListFragment fragment = new MessageContactListFragment();
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

        commonFunction();

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            rootView = inflater.inflate(R.layout.fragment_message_contact_list_ar, container, false);

        } else {
            rootView = inflater.inflate(R.layout.fragment_message_contact_list, container, false);


        }

        init(rootView);

        initHeader();

        setTypeface_Text();

        GetClassDetails();

        NewContactListWebService();

        if (on_back) {

        } else {
            if (mParam1 != null) {
                if (mParam1.equalsIgnoreCase("send_message")) {

                    if (homeClassListDetailsArrayList.size() > 0) {
                        if (homeClassListDetailsArrayList.size() == 1) {
                            str_selected_class_id = homeClassListDetailsArrayList.get(0).getClass_id();
                            str_selected_class_name = homeClassListDetailsArrayList.get(0).getClass_name();

                            if (sharedPreferencesClass.getUser_Type().equalsIgnoreCase(Common.USER_TYPE_TEACHER)) {
                                GetParentsByClassId_ForTeacher();

                            } else {
                                GetTeachersByClassId_ForParents();

                            }

                        } else {
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

                           /* ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, class_nameArrayList);
                            lv_class.setAdapter(adapter);*/

                                lv_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                Toast.makeText(context, "Click Class Name =====> " + homeClassListDetailsArrayList.get(position).getClass_name() + "=====> Class Id========> " + homeClassListDetailsArrayList.get(position).getClass_id(), Toast.LENGTH_SHORT).show();


                                        str_selected_class_id = homeClassListDetailsArrayList.get(position).getClass_id();
                                        str_selected_class_name = homeClassListDetailsArrayList.get(position).getClass_name();

                                        Logger.e("Select Class id from new post =======> " + str_selected_class_id + "");
                                        Logger.e("Select Class name from new post =======> " + str_selected_class_name + "");


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
                        }
                    }


                } else {

                }

            }
        }

        return rootView;
    }


    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {


            MainActivity.tv_title.setTypeface(tf);
            MainActivity.tv_subtitle.setTypeface(tf);

            tv_no_conversions.setTypeface(tf);


            MainActivity.tv_title.setText(Common.filter("lbl_Messages", "ar"));

            tv_no_conversions.setText(Common.filter("lbl_no_conversation_found", "ar"));



        } else {
            MainActivity.tv_title.setTypeface(tf);
            MainActivity.tv_subtitle.setTypeface(tf);

            tv_no_conversions.setTypeface(tf);



            MainActivity.tv_title.setText(Common.filter("lbl_Messages", "en"));

            tv_no_conversions.setText(Common.filter("lbl_no_conversation_found", "en"));


        }


    }

    private void init(View rootView) {

        lv_contact_list = (ListView) rootView.findViewById(R.id.lv_contact_list);
        tv_no_conversions= (TextView) rootView.findViewById(R.id.tv_no_conversions);
        tv_no_conversions.setVisibility(View.GONE);

        ArrayList<String> arrayList = new ArrayList<>();

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
        getActivity().findViewById(R.id.iv_bell1).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_add).setVisibility(View.VISIBLE);

        MainActivity.iv_profile_pic.setVisibility(View.GONE);


        MainActivity.tv_title.setText("Messages");
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

        getActivity().findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                  /*  ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, class_nameArrayList);
                    lv_class.setAdapter(adapter);*/

                    lv_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                Toast.makeText(context, "Click Class Name =====> " + homeClassListDetailsArrayList.get(position).getClass_name() + "=====> Class Id========> " + homeClassListDetailsArrayList.get(position).getClass_id(), Toast.LENGTH_SHORT).show();


                            str_selected_class_id = homeClassListDetailsArrayList.get(position).getClass_id();
                            str_selected_class_name = homeClassListDetailsArrayList.get(position).getClass_name();

                            Logger.e("Select Class id from new post =======> " + str_selected_class_id + "");
                            Logger.e("Select Class name from new post =======> " + str_selected_class_name + "");


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

            }
        });

        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

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


                        Intent i = new Intent(context, Parent_Teacher_List_For_Contact_Activity.class);
                        i.putExtra("teacher_parent_list_array", jsonArraysuccess + "");
                        i.putExtra("class_name", str_selected_class_name);

                        startActivityForResult(i, 123);


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


                        Intent i = new Intent(context, Parent_Teacher_List_For_Contact_Activity.class);
                        i.putExtra("teacher_parent_list_array", jsonArraysuccess + "");
                        i.putExtra("class_name", str_selected_class_name);

                        startActivityForResult(i, 123);

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

    // TODO: Rename method, update argument and hook method into UI event
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.e("22/08 requestCode ===> " + requestCode + "");
        Logger.e("22/08 resultCode ===> " + resultCode + "");


        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            str_selected_parent_id_from_next_Screen = data.getStringExtra("id");

            String name = data.getStringExtra("count");

            Logger.e("23/08 test ========> " + str_selected_parent_id_from_next_Screen + "");

            String CurrentString = str_selected_parent_id_from_next_Screen;
            String[] separated = CurrentString.split(",");

            Fragment fragment = new SendMessageFragment().newInstance(str_selected_parent_id_from_next_Screen, name);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.addToBackStack(fragment.toString());

            fragmentTransaction.commit();

          /*  FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTrasaction = fm.beginTransaction();
            fragmentTrasaction.replace(R.id.frame, fragment);
            fragmentTrasaction.addToBackStack(fragment.toString());
            fragmentTrasaction.commit();*/

        }
    }

    private void NewContactListWebService() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("NewContactListWebService Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

                        JSONArray jsonArraySuccess = jsonObject.optJSONArray("success");

                        if (jsonArraySuccess.length() > 0) {
                            contactListModelArrayList.clear();

                            lv_contact_list.setVisibility(View.VISIBLE);
                            tv_no_conversions.setVisibility(View.GONE);


                            for (int i = 0; i < jsonArraySuccess.length(); i++) {
                                JSONObject jsonObject1 = jsonArraySuccess.optJSONObject(i);

                                String to_uid = jsonObject1.optString("to_uid");
                                String from_uid = jsonObject1.optString("from_uid");
                                String message = jsonObject1.optString("message");
                                String stamp = jsonObject1.optString("stamp");
                                String name = jsonObject1.optString("name");
                                String profile_pic = jsonObject1.optString("profile_pic");
                                String unread_count = jsonObject1.optString("unread_count");

                                ContactListModel contactListModel = new ContactListModel();
                                contactListModel.setTo_uid(to_uid);
                                contactListModel.setFrom_uid(from_uid);
                                contactListModel.setMessage(message);
                                contactListModel.setStamp(stamp);
                                contactListModel.setName(name);
                                contactListModel.setProfile_pic(profile_pic);
                                contactListModel.setUnread_count(unread_count);

                                contactListModelArrayList.add(contactListModel);


                            }

                        } else {
                            contactListModelArrayList.clear();

                            lv_contact_list.setVisibility(View.GONE);
                            tv_no_conversions.setVisibility(View.VISIBLE);

                        }

                        messageContactListDetailsAdapter = new MessageContactListDetailsAdapter(context, contactListModelArrayList);
                        lv_contact_list.setAdapter(messageContactListDetailsAdapter);
                        messageContactListDetailsAdapter.notifyDataSetChanged();

                        lv_contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String to_uid = contactListModelArrayList.get(position).getTo_uid();
                                String to_name = contactListModelArrayList.get(position).getName();


                                Fragment fragment = new SendMessageFragment().newInstance(to_uid, to_name);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.frame, fragment);
                                fragmentTransaction.addToBackStack(fragment.toString());

                                fragmentTransaction.commit();
                            }
                        });


                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("action", "conversation_list"));
        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("NewContactListWebService Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_message_cms);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }
}
