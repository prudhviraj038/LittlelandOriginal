package com.leadinfosoft.littleland.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.leadinfosoft.littleland.ModelClass.GetParentsDetailsForParticularClassid_NewPost;
import com.leadinfosoft.littleland.ModelClass.NotificationListModelClass;
import com.leadinfosoft.littleland.ModelClass.ViewPostListModelClass;
import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.activity.MainActivity;
import com.leadinfosoft.littleland.activity.ViewPostDetail;
import com.leadinfosoft.littleland.adapter.NotificationListAdapter;
import com.leadinfosoft.littleland.adapter.ViewPostAdapter;
import com.leadinfosoft.littleland.utill.SharedPreferencesClass;
import com.leadinfosoft.littleland.utill.Utils;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.leadinfosoft.littleland.widget.MyTextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    ListView lv_notifications;
    TextView tv_no_notifications;
    boolean isEnglish;


    Context context;

    Response_string<String> resp;
    RequestMaker req;

    ConnectionDetector con;
    SharedPreferencesClass sharedPreferencesClass;
    CustomDialogClass customDialogClass;

    NotificationListAdapter notificationListAdapter = null;

    String str_offset = "0";

    ArrayList<NotificationListModelClass> notificationListModelClassArrayList = new ArrayList<>();

    String fontPath = "";
    Typeface tf;

    String fontPath_numeric = "";
    Typeface tf_numeric;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
            rootView = inflater.inflate(R.layout.fragment_notifications_ar, container, false);
            isEnglish = false;


        } else {
            rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
            isEnglish = true;


        }

        initHeader();

        init(rootView);

        setTypeface_Text();

        GetNotificationListWebService();

        return rootView;
    }

    private void init(View rootView) {

        lv_notifications = (ListView) rootView.findViewById(R.id.lv_notifications);

        tv_no_notifications = (TextView) rootView.findViewById(R.id.tv_no_notifications);
        tv_no_notifications.setVisibility(View.GONE);

    }

    private void openFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTrasaction = fm.beginTransaction();
        fragmentTrasaction.replace(R.id.frame, fragment);
        fragmentTrasaction.addToBackStack(fragment.toString());
        fragmentTrasaction.commit();
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

    private void initHeader() {

        getActivity().findViewById(R.id.rl_withlogo).setVisibility(View.GONE);
        getActivity().findViewById(R.id.rl_withtitle).setVisibility(View.VISIBLE);


        getActivity().findViewById(R.id.iv_add).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.iv_bell1).setVisibility(View.INVISIBLE);

        MainActivity.iv_profile_pic.setVisibility(View.GONE);


        if (isEnglish) {
            ((MyHeaderTextView) getActivity().findViewById(R.id.tv_title)).setText("NOTIFICATIONS");
            ((MyTextView) getActivity().findViewById(R.id.tv_subtitle)).setVisibility(View.GONE);
        } else {
            ((MyHeaderTextView) getActivity().findViewById(R.id.tv_title)).setText("رأي آخر");
            ((MyTextView) getActivity().findViewById(R.id.tv_subtitle)).setVisibility(View.GONE);

        }

        getActivity().findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    private void setTypeface_Text() {

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {

            MainActivity.tv_title.setTypeface(tf);
            MainActivity.tv_subtitle.setTypeface(tf);


            tv_no_notifications.setTypeface(tf);

            tv_no_notifications.setText(Common.filter("lbl_no_notifications", "ar"));

            MainActivity.tv_title.setText(Common.filter("lbl_notifications", "ar"));


        } else {
            MainActivity.tv_title.setTypeface(tf);
            MainActivity.tv_subtitle.setTypeface(tf);


            tv_no_notifications.setTypeface(tf);

            tv_no_notifications.setText(Common.filter("lbl_no_notifications", "en"));

            MainActivity.tv_title.setText(Common.filter("lbl_notifications", "en"));


        }

    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void GetNotificationListWebService() {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("GetNotificationListWebService Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();


                        JSONArray jsonArraysuccess = jsonObject.optJSONArray("success");

                        if (jsonArraysuccess.length() > 0) {

                            tv_no_notifications.setVisibility(View.GONE);


                            notificationListModelClassArrayList.clear();
                            for (int i = 0; i < jsonArraysuccess.length(); i++) {
                                JSONObject jsonObject1 = jsonArraysuccess.optJSONObject(i);


                                String id = jsonObject1.optString("id");
                                String title = jsonObject1.optString("title");
                                String body = jsonObject1.optString("body");
                                String image = jsonObject1.optString("image");
                                String type = jsonObject1.optString("type");
                                String ref_id = jsonObject1.optString("ref_id");
                                String stamp = jsonObject1.optString("stamp");


                                NotificationListModelClass notificationListModelClass = new NotificationListModelClass();
                                notificationListModelClass.setId(id);
                                notificationListModelClass.setTitle(title);
                                notificationListModelClass.setBody(body);
                                notificationListModelClass.setImage(image);
                                notificationListModelClass.setType(type);
                                notificationListModelClass.setRef_id(ref_id);
                                notificationListModelClass.setStamp(stamp);

                                notificationListModelClassArrayList.add(notificationListModelClass);


                            }

                        } else {
                            notificationListModelClassArrayList.clear();

                            tv_no_notifications.setVisibility(View.VISIBLE);


                        }

                        notificationListAdapter = new NotificationListAdapter(context, notificationListModelClassArrayList);
                        lv_notifications.setAdapter(notificationListAdapter);
                        notificationListAdapter.notifyDataSetChanged();
                        lv_notifications.setEmptyView(tv_no_notifications);

                        lv_notifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (notificationListModelClassArrayList.get(position).getType().equalsIgnoreCase("post")) {
                                    Intent i = new Intent(context, ViewPostDetail.class);
                                    i.putExtra("post_id", notificationListModelClassArrayList.get(position).getRef_id());
                                    startActivity(i);
                                    getActivity().overridePendingTransition(0, 0);

//                                    getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                                } else if (notificationListModelClassArrayList.get(position).getType().equalsIgnoreCase("direct_message")) {
                                    String to_uid = notificationListModelClassArrayList.get(position).getRef_id();
                                    String to_name = "";

                                    Fragment fragment = new SendMessageFragment().newInstance(to_uid, to_name);
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.frame, fragment);
                                    fragmentTransaction.addToBackStack(fragment.toString());

                                    fragmentTransaction.commit();
                                } else if (notificationListModelClassArrayList.get(position).getType().equalsIgnoreCase("attendance")) {

                                    GetAbsentDialog(notificationListModelClassArrayList.get(position).getRef_id());

                                }
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


        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("offset", str_offset));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("GetNotificationListWebService Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_notifications);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }

    }

    private void GetAbsentDialog(final String ref_id) {

        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        if (sharedPreferencesClass.getSelected_Language().equalsIgnoreCase(Common.Selected_Language_AR)) {
            dialog.setContentView(R.layout.dialog_reason_for_absent_ar);

        } else {
            dialog.setContentView(R.layout.dialog_reason_for_absent);

        }

        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        final EditText et_reason = (EditText) dialog.findViewById(R.id.et_reason);
        TextView tv_submit = (TextView) dialog.findViewById(R.id.tv_submit);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_reason.getText().toString().equalsIgnoreCase("") || et_reason.getText().toString().length() == 0) {
                    et_reason.setError("Please Enter Reason");
                } else {

                    AttendanceReasonWebService(ref_id, et_reason.getText().toString());

                    dialog.dismiss();

                }

            }
        });

        dialog.show();
    }

    private void AttendanceReasonWebService(String ref_id, String reason) {
        resp = new Response_string<String>() {
            @Override
            public void OnRead_response(String result) {

                try {
                    Logger.e("AttendanceReasonWebService Webservice Response" + result + "");

                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.optString("error");

                    if (error.equalsIgnoreCase("") || error.length() == 0) {
//                            Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

                        Toast.makeText(context, jsonObject.optString("success"), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ArrayList<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("uid", sharedPreferencesClass.getUSER_Id()));
        params.add(new BasicNameValuePair("attendance_id", ref_id));
        params.add(new BasicNameValuePair("remark", reason));
        params.add(new BasicNameValuePair("lan", sharedPreferencesClass.getSelected_Language()));


        Logger.e("AttendanceReasonWebService Webservice Params =====> " + params + "");

        if (con.isConnectingToInternet()) {
            req = new RequestMaker(resp, params, context);
            req.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Common.URL_attendance_reason);
        } else {
            Toast.makeText(context, Common.InternetConnection, Toast.LENGTH_SHORT).show();
        }
    }

}
