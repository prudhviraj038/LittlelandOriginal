package com.leadinfosoft.littleland.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leadinfosoft.littleland.Common.Common;
import com.leadinfosoft.littleland.Common.ConnectionDetector;
import com.leadinfosoft.littleland.Common.Logger;
import com.leadinfosoft.littleland.Common.RequestMaker;
import com.leadinfosoft.littleland.Common.Response_string;
import com.leadinfosoft.littleland.Dialog.CustomDialogClass;
import com.leadinfosoft.littleland.ModelClass.GetParentsDetailsForParticularClassid_NewPost;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.adapter.Contact_List_Select_Parents_Teachers_Adapter;
import com.leadinfosoft.littleland.adapter.Parent_teacher_List_for_new_post_Adapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by radhe on 8/22/2017.
 */
public class Parent_Teacher_List_For_Contact_Activity extends Activity implements View.OnClickListener {

    Context context;

    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    RelativeLayout rl_withlogo, rl_withtitle;
    ImageView iv_back, iv_bell1;
    TextView tv_title, tv_subtitle;

    String str_jsonarray_success = "";

    ArrayList<GetParentsDetailsForParticularClassid_NewPost> getParentsDetailsForParticularClassid_newPostArrayList = new ArrayList<>();

    JSONArray jsonArraysuccess = null;

    EditText et_search;
    ListView lv_list;

    TextView tv_selected, tv_selected_all, tv_clear, tv_done;

    LinearLayout ll_bottom;

    Contact_List_Select_Parents_Teachers_Adapter parent_teacher_list_for_new_post_adapter = null;

    String str_selected_parents_id = "";
    String str_selected_class_name = "";

    int count = 0;

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        commonFunction();

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            setContentView(R.layout.activity_parent_teacher_list_from_new_post_ar);
        } else {
            setContentView(R.layout.activity_parent_teacher_list_from_new_post);
        }
        initHeader();

        init();

        setTypeface_Text();

        str_jsonarray_success = getIntent().getStringExtra("teacher_parent_list_array");
        str_selected_class_name = getIntent().getStringExtra("class_name");

        if (str_selected_class_name.equalsIgnoreCase("") || str_selected_class_name.length() == 0) {

        } else {

            if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                tv_subtitle.setText(Common.filter("lbl_class", "ar") + " - " + sharedPreferencesClass.getSelected_Class_Name_ar());

            } else {
                tv_subtitle.setText(Common.filter("lbl_class", "en") + " - " + sharedPreferencesClass.getSelected_Class_Name());


            }

//            tv_subtitle.setText("Class - " + str_selected_class_name);


        }

        if (str_jsonarray_success.equalsIgnoreCase("") || str_jsonarray_success.length() == 0 || str_jsonarray_success == null) {

        } else {
            GetFieldArray();
        }


    }

    private void GetFieldArray() {
        try {
            jsonArraysuccess = new JSONArray(str_jsonarray_success);

            if (jsonArraysuccess != null) {
                if (jsonArraysuccess.length() > 0) {

                    getParentsDetailsForParticularClassid_newPostArrayList.clear();

                    for (int i = 0; i < jsonArraysuccess.length(); i++) {
                        JSONObject jsonObject1 = jsonArraysuccess.optJSONObject(i);

                        String uid = jsonObject1.optString("uid");
                        String fname = jsonObject1.optString("fname");
                        String lname = jsonObject1.optString("lname");
                        String profile_pic = jsonObject1.optString("profile_pic");
                        String type = jsonObject1.optString("type");
                        String child_name = jsonObject1.optString("child_name");

                        GetParentsDetailsForParticularClassid_NewPost getParentsDetailsForParticularClassid_newPost = new GetParentsDetailsForParticularClassid_NewPost();
                        getParentsDetailsForParticularClassid_newPost.setUid(uid);
                        getParentsDetailsForParticularClassid_newPost.setFname(fname);
                        getParentsDetailsForParticularClassid_newPost.setLname(lname);
                        getParentsDetailsForParticularClassid_newPost.setProfile_pic(profile_pic);
                        getParentsDetailsForParticularClassid_newPost.setType(type);
                        getParentsDetailsForParticularClassid_newPost.setSelected(false);
                        getParentsDetailsForParticularClassid_newPost.setChild_name(child_name);

                        getParentsDetailsForParticularClassid_newPostArrayList.add(getParentsDetailsForParticularClassid_newPost);


                    }


                } else {
                    //length 0
                    getParentsDetailsForParticularClassid_newPostArrayList.clear();

                }

                Logger.e("Activity Page getParentsDetailsForParticularClassid_newPostArrayList size===========> " + getParentsDetailsForParticularClassid_newPostArrayList.size() + "");

                parent_teacher_list_for_new_post_adapter = new Contact_List_Select_Parents_Teachers_Adapter(context, getParentsDetailsForParticularClassid_newPostArrayList);
                lv_list.setAdapter(parent_teacher_list_for_new_post_adapter);
                parent_teacher_list_for_new_post_adapter.notifyDataSetChanged();

                lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        str_selected_parents_id = getParentsDetailsForParticularClassid_newPostArrayList.get(position).getUid();


                        Intent i = getIntent();
                        i.putExtra("id", str_selected_parents_id);
                        i.putExtra("count", getParentsDetailsForParticularClassid_newPostArrayList.get(position).getFname() + " " + getParentsDetailsForParticularClassid_newPostArrayList.get(position).getLname() + "");

                        setResult(RESULT_OK, i);
                        finish();
                    }
                });

                et_search.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                        String text = et_search.getText().toString().toLowerCase(Locale.getDefault());
                        parent_teacher_list_for_new_post_adapter.filter(text);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1,
                                                  int arg2, int arg3) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                        // TODO Auto-generated method stub
                    }
                });

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void GetCalculateSelectedValue() {

        if (getParentsDetailsForParticularClassid_newPostArrayList != null) {
            if (getParentsDetailsForParticularClassid_newPostArrayList.size() > 0) {

                str_selected_parents_id = "";

                count = 0;

                for (int i = 0; i < getParentsDetailsForParticularClassid_newPostArrayList.size(); i++) {
                    if (getParentsDetailsForParticularClassid_newPostArrayList.get(i).getSelected()) {
                        count = count + 1;
                        str_selected_parents_id += getParentsDetailsForParticularClassid_newPostArrayList.get(i).getUid() + ",";

                    }
                }

                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    tv_selected.setText(Common.filter("lbl_selected", "ar") + " " + count + "");

                } else {
                    tv_selected.setText(Common.filter("lbl_selected", "en") + " " + count + "");

                }


            } else {
                //0 selected

                count = 0;

                if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
                    tv_selected.setText(Common.filter("lbl_selected", "ar") + " " + "0" + "");

                } else {
                    tv_selected.setText(Common.filter("lbl_selected", "en") + " " + "0" + "");

                }

//                tv_selected.setText("Selected " + "0" + "");

                str_selected_parents_id = "";

            }

        }


        //delete Last Comma
        str_selected_parents_id = str_selected_parents_id.replaceAll(",$", "");

        if (str_selected_parents_id.endsWith(",")) {
            str_selected_parents_id = str_selected_parents_id.substring(0, str_selected_parents_id.length() - 1);
        }

        Logger.e("22/08 str_selected_parents_id =======> " + str_selected_parents_id + "");

    }

    private void init() {

        et_search = (EditText) findViewById(R.id.et_search);
        lv_list = (ListView) findViewById(R.id.lv_list);

        tv_selected = (TextView) findViewById(R.id.tv_selected);
        tv_selected_all = (TextView) findViewById(R.id.tv_selected_all);
        tv_selected_all.setOnClickListener(this);

        tv_clear = (TextView) findViewById(R.id.tv_clear);
        tv_clear.setOnClickListener(this);


        tv_done = (TextView) findViewById(R.id.tv_done);
        tv_done.setOnClickListener(this);

        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        ll_bottom.setVisibility(View.GONE);


    }

    private void initHeader() {


        rl_withlogo = (RelativeLayout) findViewById(R.id.rl_withlogo);
        rl_withlogo.setVisibility(View.GONE);

        rl_withtitle = (RelativeLayout) findViewById(R.id.rl_withtitle);
        rl_withtitle.setVisibility(View.VISIBLE);


        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        iv_bell1 = (ImageView) findViewById(R.id.iv_bell1);
        iv_bell1.setVisibility(View.INVISIBLE);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("Select Parents");

        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        tv_subtitle.setTextColor(getResources().getColor(R.color.techer_parpal));

        tv_subtitle.setVisibility(View.VISIBLE);


//        tv_subtitle.setText("Class - " + sharedPreferencesClass.getSelected_Class_Name());


//        tv_subtitle.setText(sharedPreferencesClass.getSelected_Child_Name());


    }

    private void commonFunction() {
        context = getApplicationContext();
        con = new ConnectionDetector(context);
        sharedPreferencesClass = new SharedPreferencesClass(context);

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            fontPath = "fonts/GE_Flow_Regular.otf";
            tf = Typeface.createFromAsset(getAssets(), fontPath);
        } else {
            fontPath = "fonts/Lato-Bold.ttf";
            tf = Typeface.createFromAsset(getAssets(), fontPath);
        }

        fontPath_numeric = "fonts/Lato-Bold.ttf";
        tf_numeric = Typeface.createFromAsset(getAssets(), fontPath_numeric);

    }


    @Override
    public void onClick(View v) {

        if (v == tv_clear) {
            ClearSelectedValues();
        } else if (v == tv_selected_all) {
            SelectedAllValues();
        } else if (v == tv_done) {
            Intent i = getIntent();
            i.putExtra("id", str_selected_parents_id);
            i.putExtra("count", count + "");

            setResult(RESULT_OK, i);
            finish();
        }


    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {


            et_search.setTypeface(tf);
            tv_selected.setTypeface(tf);
            tv_selected_all.setTypeface(tf);
            tv_clear.setTypeface(tf);
            tv_done.setTypeface(tf);

            tv_title.setTypeface(tf);
            tv_subtitle.setTypeface(tf);


            et_search.setHint(Common.filter("lbl_search", "ar").toUpperCase());

            tv_selected.setText(Common.filter("lbl_selected", "ar"));
            tv_selected_all.setText(Common.filter("lbl_selected_all", "ar"));

            tv_clear.setText(Common.filter("lbl_clear", "ar"));
            tv_done.setText(Common.filter("lbl_done", "ar"));

            tv_title.setText(Common.filter("lbl_Select_Parents", "ar"));

        } else {
            et_search.setTypeface(tf);
            tv_selected.setTypeface(tf);
            tv_selected_all.setTypeface(tf);
            tv_clear.setTypeface(tf);
            tv_done.setTypeface(tf);

            tv_title.setTypeface(tf);
            tv_subtitle.setTypeface(tf);


            et_search.setHint(Common.filter("lbl_search", "en").toUpperCase());

            tv_selected.setText(Common.filter("lbl_selected", "en"));
            tv_selected_all.setText(Common.filter("lbl_selected_all", "en"));

            tv_clear.setText(Common.filter("lbl_clear", "en"));
            tv_done.setText(Common.filter("lbl_done", "en"));

            tv_title.setText(Common.filter("lbl_Select_Parents", "en"));
        }

        tv_selected.setTypeface(tf_numeric);

    }

    private void ClearSelectedValues() {

        if (getParentsDetailsForParticularClassid_newPostArrayList != null) {
            if (getParentsDetailsForParticularClassid_newPostArrayList.size() > 0) {

                for (int i = 0; i < getParentsDetailsForParticularClassid_newPostArrayList.size(); i++) {
                    getParentsDetailsForParticularClassid_newPostArrayList.get(i).setSelected(false);

                }

            } else {
                //0 selected
            }

        }

        if (parent_teacher_list_for_new_post_adapter != null) {
            parent_teacher_list_for_new_post_adapter.notifyDataSetChanged();

        }

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            tv_selected.setText(Common.filter("lbl_selected", "ar") + " " + "0" + "");

        } else {
            tv_selected.setText(Common.filter("lbl_selected", "en") + " " + "0" + "");

        }

//        tv_selected.setText("Selected 0");

        GetCalculateSelectedValue();


    }

    private void SelectedAllValues() {

        if (getParentsDetailsForParticularClassid_newPostArrayList != null) {
            if (getParentsDetailsForParticularClassid_newPostArrayList.size() > 0) {

                for (int i = 0; i < getParentsDetailsForParticularClassid_newPostArrayList.size(); i++) {
                    getParentsDetailsForParticularClassid_newPostArrayList.get(i).setSelected(true);

                }

            } else {
                //0 selected
            }

        }

        if (parent_teacher_list_for_new_post_adapter != null) {
            parent_teacher_list_for_new_post_adapter.notifyDataSetChanged();

        }


        GetCalculateSelectedValue();


    }

  /*  @Override
    public void finish() {
        super.finish();
        Intent i = getIntent();
        i.putExtra("id", "test");

        setResult(RESULT_OK, i);

    }*/
}
