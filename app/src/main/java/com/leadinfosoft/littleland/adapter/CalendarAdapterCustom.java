package com.leadinfosoft.littleland.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leadinfosoft.littleland.ModelClass.CalendarCollection;
import com.leadinfosoft.littleland.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class CalendarAdapterCustom extends BaseAdapter {
	private Context context;

	private Calendar month;
	public GregorianCalendar pmonth;
	/**
	 * calendar instance for previous month for getting complete view
	 */
	public GregorianCalendar pmonthmaxset;
	private GregorianCalendar selectedDate;
	int firstDay;
	int maxWeeknumber;
	int maxP;
	int calMaxP;
	int lastWeekDay;
	int leftDays;
	int mnthlength;
	String itemvalue, curentDateString;
	DateFormat df;

	private ArrayList<String> items;
	public static List<String> day_string;
	private View previousView;
	private ArrayList<CalendarCollection> date_collection_arr;

	public CalendarAdapterCustom(Context context, GregorianCalendar monthCalendar) {
		date_collection_arr = new ArrayList<>();
		CalendarAdapterCustom.day_string = new ArrayList<String>();
		Locale.setDefault(Locale.US);
		month = monthCalendar;
		selectedDate = (GregorianCalendar) monthCalendar.clone();
		this.context = context;
		month.set(GregorianCalendar.DAY_OF_MONTH, 1);

		this.items = new ArrayList<String>();
		df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		curentDateString = df.format(selectedDate.getTime());
		refreshDays();

	}
	public Calendar getCurrentCal(){
		return pmonth;
	}

	public void setItems(ArrayList<String> items) {
		for (int i = 0; i != items.size(); i++) {
			if (items.get(i).length() == 1) {
				items.set(i, "0" + items.get(i));
			}
		}
		this.items = items;
	}

	public int getCount() {
		return day_string.size();
	}

	public Object getItem(int position) {
		return day_string.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new view for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		TextView dayView;
		ImageView date_icon;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.cal_item, null);

		}


		dayView = (TextView) v.findViewById(R.id.date);
		date_icon = (ImageView) v.findViewById(R.id.date_icon);

		dayView.setBackgroundResource(0);

		String[] separatedTime = day_string.get(position).split("-");


		String gridvalue = separatedTime[2].replaceFirst("^0*", "");
		if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
			dayView.setTextColor(Color.GRAY);
			dayView.setClickable(false);
			dayView.setFocusable(false);
		} else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
			dayView.setTextColor(Color.GRAY);
			dayView.setClickable(false);
			dayView.setFocusable(false);
		} else {
			// setting curent month's days in blue color.
			dayView.setTextColor(Color.BLACK);
		}


		if (day_string.get(position).equals(curentDateString)) {
//			v.setBackgroundColor(Color.CYAN);
			dayView.setTextColor(Color.BLUE);
		}


		dayView.setText(gridvalue);

		// create date string for comparison
		String date = day_string.get(position);

		if (date.length() == 1) {
			date = "0" + date;
		}
		String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
		if (monthStr.length() == 1) {
			monthStr = "0" + monthStr;
		}


		setEventView(v, position,dayView,date_icon);

		return v;
	}

	public View setSelected(View view, int pos) {
		if (previousView != null) {
		previousView.setBackgroundColor(Color.parseColor("#343434"));
		}

		view.setBackgroundColor(Color.CYAN);

		int len=day_string.size();
		if (len>pos) {
			if (day_string.get(pos).equals(curentDateString)) {

			}else{

				previousView = view;

			}

		}


		return view;
	}

	public void refreshDays() {
		// clear items

		items.clear();
		day_string.clear();
		Locale.setDefault(Locale.US);
		pmonth = (GregorianCalendar) month.clone();
		// month start day. ie; sun, mon, etc
		firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
		// finding number of weeks in current month.
		maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
		// allocating maximum row number for the gridview.
		mnthlength = maxWeeknumber * 7;
		maxP = getMaxP(); // previous month maximum day 31,30....
		calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
		/**
		 * Calendar instance for getting a complete gridview including the three
		 * month's (previous,current,next) dates.
		 */
		pmonthmaxset = (GregorianCalendar) pmonth.clone();
		/**
		 * setting the start date as previous month's required date.
		 */
		pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

		/**
		 * filling calendar gridview.
		 */
		for (int n = 0; n < mnthlength; n++) {

			itemvalue = df.format(pmonthmaxset.getTime());
			pmonthmaxset.add(GregorianCalendar.DATE, 1);
			day_string.add(itemvalue);

		}
	}

	private int getMaxP() {
		int maxP;
		if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)) {
			pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			pmonth.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		return maxP;
	}


	public void setAllEvents(ArrayList<CalendarCollection> arrayList){
		this.date_collection_arr = new ArrayList<>();
		this.date_collection_arr = arrayList;
		notifyDataSetChanged();
	}
	private void setEventView(View v, int pos, TextView txt,ImageView img){

		int len=date_collection_arr.size();
		for (int i = 0; i < len; i++) {
			CalendarCollection cal_obj=date_collection_arr.get(i);
			String date=cal_obj.date;
			int len1=day_string.size();
			if (len1>pos) {

				if (day_string.get(pos).equals(date)) {
					/*
					txt.setTextColor(Color.WHITE);*/
					ColorFilter filter1 = new PorterDuffColorFilter(
							Color.parseColor(cal_obj.color.toString()), PorterDuff.Mode.MULTIPLY);
					txt.setBackgroundResource(R.drawable.trans_circle);
					txt.getBackground().setColorFilter(filter1);
					/*ColorDrawable drawable = (ColorDrawable) v.getBackground();
					drawable.setColor(Color.parseColor(cal_obj.color.toString()));*/
//					Color.parseColor(cal_obj.color)

//					img.setImageResource(R.drawable.green_round_bg);
				}
			}}



	}


}