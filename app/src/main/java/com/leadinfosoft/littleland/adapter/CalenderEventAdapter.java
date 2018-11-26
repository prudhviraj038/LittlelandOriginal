package com.leadinfosoft.littleland.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.Constant;

import java.util.ArrayList;

/**
 * Created by Lead on 7/13/2017.
 */

public class CalenderEventAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>
{

  Context context;
    private ArrayList<String> items;
    boolean isEnglish;
    private final int MEETING = 0, HOLIDAY = 1,FESTIVAL=2;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView countryText;
        public TextView popText;

        public MyViewHolder(View view) {
            super(view);

        }
    }

    public CalenderEventAdapter(Context context) {
       this.context=context;
    }

    public CalenderEventAdapter(Context context, ArrayList<String> items,boolean isEnglish) {
        this.context=context;
        this.items=items;
        this.isEnglish=isEnglish;
    }

    @Override
    public int getItemViewType(int position) {

        if(items.get(position).compareTo(Constant.MEETING)==0)
        {
            return MEETING;
        }
        else if (items.get(position).compareTo(Constant.HOLIDAY)==0)
        {
            return HOLIDAY;
        }
        else if (items.get(position).compareTo(Constant.FESTIVAL)==0)
        {
            return FESTIVAL;
        }
        else
        {
            return -1;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case MEETING:
                ViewHolder1 vh1 = (ViewHolder1) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case HOLIDAY:
                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
                configureViewHolder2(vh2);

                /*vh2.getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("11111111","======>>>");
                    }
                });*/


                break;
            default:
                ViewHolder2 vh3 = (ViewHolder2) viewHolder;
                configureViewHolder2(vh3);
                break;
        }

    }

    private void configureViewHolder1(ViewHolder1 vh1, int position) {
        /*User user = (User) items.get(position);
        if (user != null) {
            vh1.getLabel1().setText("Name: " + user.getName());
            vh1.getLabel2().setText("Hometown: " + user.getArea());
        }*/
    }

    private void configureViewHolder2(ViewHolder2 vh2) {
//        vh2.getImageView().setImageResource(R.drawable.chat_wallpaper_two);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case MEETING:
                View v1;
                if (isEnglish)
                {
                    v1= inflater.inflate(R.layout.calender_meeting_rowlist, parent, false);
                }
                else
                {
                    v1= inflater.inflate(R.layout.calender_meeting_rowlist_ar, parent, false);
                }

                viewHolder = new ViewHolder1(v1);
                break;
            case HOLIDAY:
                View v2;
                if (isEnglish)
                {
                    v2 = inflater.inflate(R.layout.calender_holiday_rowlist, parent, false);
                }
                else
                {
                    v2 = inflater.inflate(R.layout.calender_holiday_rowlist_ar, parent, false);
                }

                viewHolder = new ViewHolder2(v2);
                break;

            /*case FESTIVAL:
                View v2 = inflater.inflate(R.layout.layout_viewholder2, viewGroup, false);
                viewHolder = new ViewHolder2(v2);
                break;*/
            default:
                View v3;
                if (isEnglish)
                {
                    v3 = inflater.inflate(R.layout.calender_festival_rowlist, parent, false);
                }
                else
                {
                    v3 = inflater.inflate(R.layout.calender_festival_rowlist_ar, parent, false);
                }

                viewHolder = new ViewHolder2(v3);
//                View v = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
//                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                break;
        }
        return viewHolder;
        /*View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calender_meeting_rowlist,parent, false);
        return new MyViewHolder(v);*/
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        private TextView mTitle;
        public ViewHolder1(View itemView) {
            super(itemView);
//            mTitle = (TextView) itemView.findViewById(R.id.titleTextView);
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        private TextView mTitle;
        public ViewHolder2(View itemView) {
            super(itemView);
//            mTitle = (TextView) itemView.findViewById(R.id.titleTextView);
        }
    }

    public static class ViewHolder3 extends RecyclerView.ViewHolder {
        private TextView mTitle;
        public ViewHolder3(View itemView) {
            super(itemView);
//            mTitle = (TextView) itemView.findViewById(R.id.titleTextView);
        }
    }
}
