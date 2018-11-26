package com.leadinfosoft.littleland.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.adapter.CalenderEventAdapter;
import com.leadinfosoft.littleland.fragment.Home;
import com.leadinfosoft.littleland.utill.Constant;
import com.leadinfosoft.littleland.utill.Utils;
import com.leadinfosoft.littleland.widget.CalendarView;
import com.leadinfosoft.littleland.widget.LoadingDialog;
import com.leadinfosoft.littleland.widget.MyHeaderTextView;
import com.leadinfosoft.littleland.widget.MyTextView;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalenderActivity extends AppCompatActivity {

    @BindView(R.id.rv_event)
    RecyclerView rv_event;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_title)
    MyHeaderTextView tv_title;
    @BindView(R.id.tv_subtitle)
    MyTextView tv_subtitle;

    HashMap<String,Date> eventmap=new HashMap<String,Date>();
    Context context;
    boolean isEnglish;

    @BindView(R.id.calendar_view)
    CalendarView calendar_view;
    LoadingDialog loadingDialog;

    @BindView(R.id.ll_data)
    LinearLayout ll_data;
    @BindView(R.id.ll_loder)
    LinearLayout ll_loder;
    @BindView(R.id.avi_loader)
    AVLoadingIndicatorView avi_loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        Log.e("=====>>>>","===>>"+CalenderActivity.this.isFinishing());
        ButterKnife.bind(this);
        context =CalenderActivity.this;

        avi_loader.show();

        calendar_view.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run() {
                avi_loader.smoothToHide();
                ll_loder.setVisibility(View.GONE);
                ll_data.setVisibility(View.VISIBLE);

            }
        }, 3000);

        initHeader();
        rv_event.setNestedScrollingEnabled(false);

        try {
            eventmap.put("meeting",new SimpleDateFormat("yyyy-MM-dd").parse("2017-07-10"));
            eventmap.put("festival",new SimpleDateFormat("yyyy-MM-dd").parse("2017-07-23"));
            eventmap.put("holiday",new SimpleDateFormat("yyyy-MM-dd").parse("2017-07-17"));


        } catch (ParseException e) {
            e.printStackTrace();
        }


        getSampleArrayList();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_event.setLayoutManager(llm);
        CalenderEventAdapter calenderEventAdapter=new CalenderEventAdapter(CalenderActivity.this, getSampleArrayList(),isEnglish);
        rv_event.setAdapter(calenderEventAdapter);

    }

    private void init() {

        Log.e("sgdhdgajd","====>>>");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume","===>>>");

    }

    @OnClick(R.id.iv_back)
   public void onClickBack()
   {
       finish();
       this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
   }

    private ArrayList<String> getSampleArrayList() {
        ArrayList<String> items = new ArrayList<>();

        items.add(Constant.MEETING);
        items.add(Constant.FESTIVAL);
        items.add(Constant.FESTIVAL);
        items.add(Constant.MEETING);
        items.add(Constant.MEETING);
        return items;
    }

    private void initHeader() {
    if (Utils.userSelectedLang(CalenderActivity.this))
    {
        tv_title.setText("Monthly Calendar");
        tv_subtitle.setText("Class - Twin Toe");
        isEnglish=true;
    }
    else
    {
        tv_title.setText("التقويم الشهري");
        tv_subtitle.setText("فئة - التوأم تو");
        isEnglish=false;
    }

    }



    private class PrepareAdapter1 extends AsyncTask<Void,Void,Void > {

        LoadingDialog loadingDialog;

        public PrepareAdapter1(Context context)
        {
            loadingDialog=new LoadingDialog(context);
            loadingDialog.show();
            Log.e("PrepareAdapter1","====>>>");



        }
        @Override
        protected void onPreExecute() {


        }

        @Override
        protected Void doInBackground(Void... params) {
            boolean isshow=false;
            try {
                Log.e("doInBackground","====>>>");

                Thread.sleep(10000);


            } catch (Exception e) {
                Log.e("aaaaaaaa","===>>"+e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void param) {
            Log.e("onPostExecute","====>>>");
            loadingDialog.dismiss();
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
