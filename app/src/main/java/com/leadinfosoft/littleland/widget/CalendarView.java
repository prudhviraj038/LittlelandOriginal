package com.leadinfosoft.littleland.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Lead on 7/13/2017.
 */

public class CalendarView extends LinearLayout {

    // for logging
    private static final String LOGTAG = "Calendar View";

    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance();

    //event handling
    private EventHandler eventHandler = null;

    // internal components
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    private MyHeaderTextView txtDate;
    private GridView grid;

    private MyHeaderTextView tv_mon1;
    private MyHeaderTextView tv_mon2;
    private MyHeaderTextView tv_mon3;
    private MyHeaderTextView tv_mon4;
    private MyHeaderTextView tv_mon5;
    private MyHeaderTextView tv_mon6;
    private MyHeaderTextView tv_mon7;
    private MyHeaderTextView tv_mon8;
    private MyHeaderTextView tv_mon9;
    private MyHeaderTextView tv_mon10;
    private MyHeaderTextView tv_mon11;
    private MyHeaderTextView tv_mon12;

    private MyHeaderTextView tv_week1;
    private MyHeaderTextView tv_week2;
    private MyHeaderTextView tv_week3;
    private MyHeaderTextView tv_week4;
    private MyHeaderTextView tv_week5;
    private MyHeaderTextView tv_week6;
    private MyHeaderTextView tv_week7;

    HorizontalScrollView hsv;

    // seasons' rainbow
    /*int[] rainbow = new int[] {
            R.color.summer,
            R.color.fall,
            R.color.winter,
            R.color.spring
    };*/

    // month-season association (northern hemisphere, sorry australia :)


    HashMap<String, Date> userevents = new HashMap<String, Date>();

    int useryear;
    int usermounth;


    public CalendarView(Context context) {
        super(context);
        initControl(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        loadDateFormat(attrs);
        initControl(context);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        loadDateFormat(attrs);
        initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);


        assignUiElements(context);
        assignClickHandlers();

        updateCalendar();
    }

    private void loadDateFormat(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        } finally {
            ta.recycle();
        }
    }

    private void assignUiElements(Context context) {
        // layout is inflated, assign local variables to components
        header = (LinearLayout) findViewById(R.id.calendar_header);
        btnPrev = (ImageView) findViewById(R.id.calendar_prev_button);
        btnNext = (ImageView) findViewById(R.id.calendar_next_button);
        txtDate = (MyHeaderTextView) findViewById(R.id.calendar_date_display);
        grid = (GridView) findViewById(R.id.calendar_grid);
        hsv = (HorizontalScrollView) findViewById(R.id.hsv);

        tv_mon1 = (MyHeaderTextView) findViewById(R.id.tv_mon1);
        tv_mon2 = (MyHeaderTextView) findViewById(R.id.tv_mon2);
        tv_mon3 = (MyHeaderTextView) findViewById(R.id.tv_mon3);
        tv_mon4 = (MyHeaderTextView) findViewById(R.id.tv_mon4);
        tv_mon5 = (MyHeaderTextView) findViewById(R.id.tv_mon5);
        tv_mon6 = (MyHeaderTextView) findViewById(R.id.tv_mon6);
        tv_mon7 = (MyHeaderTextView) findViewById(R.id.tv_mon7);
        tv_mon8 = (MyHeaderTextView) findViewById(R.id.tv_mon8);
        tv_mon9 = (MyHeaderTextView) findViewById(R.id.tv_mon9);
        tv_mon10 = (MyHeaderTextView) findViewById(R.id.tv_mon10);
        tv_mon11 = (MyHeaderTextView) findViewById(R.id.tv_mon11);
        tv_mon12 = (MyHeaderTextView) findViewById(R.id.tv_mon12);

        tv_week1 = (MyHeaderTextView) findViewById(R.id.tv_week1);
        tv_week2 = (MyHeaderTextView) findViewById(R.id.tv_week2);
        tv_week3 = (MyHeaderTextView) findViewById(R.id.tv_week3);
        tv_week4 = (MyHeaderTextView) findViewById(R.id.tv_week4);
        tv_week5 = (MyHeaderTextView) findViewById(R.id.tv_week5);
        tv_week6 = (MyHeaderTextView) findViewById(R.id.tv_week6);
        tv_week7 = (MyHeaderTextView) findViewById(R.id.tv_week7);

        if (Utils.userSelectedLang(context)) {
            tv_mon1.setText(getResources().getString(R.string.cal_jan));
            tv_mon2.setText(getResources().getString(R.string.cal_feb));
            tv_mon3.setText(getResources().getString(R.string.cal_mar));
            tv_mon4.setText(getResources().getString(R.string.cal_apr));
            tv_mon5.setText(getResources().getString(R.string.cal_may));
            tv_mon6.setText(getResources().getString(R.string.cal_june));
            tv_mon7.setText(getResources().getString(R.string.cal_july));
            tv_mon8.setText(getResources().getString(R.string.cal_aug));
            tv_mon9.setText(getResources().getString(R.string.cal_sep));
            tv_mon10.setText(getResources().getString(R.string.cal_oct));
            tv_mon11.setText(getResources().getString(R.string.cal_nov));
            tv_mon12.setText(getResources().getString(R.string.cal_dec));

            tv_week1.setText(getResources().getString(R.string.sun));
            tv_week2.setText(getResources().getString(R.string.mon));
            tv_week3.setText(getResources().getString(R.string.tue));
            tv_week4.setText(getResources().getString(R.string.wed));
            tv_week5.setText(getResources().getString(R.string.thu));
            tv_week6.setText(getResources().getString(R.string.fri));
            tv_week7.setText(getResources().getString(R.string.sat));

        } else {
            tv_mon1.setText(getResources().getString(R.string.cal_jan_ar));
            tv_mon2.setText(getResources().getString(R.string.cal_feb_ar));
            tv_mon3.setText(getResources().getString(R.string.cal_mar_ar));
            tv_mon4.setText(getResources().getString(R.string.cal_apr_ar));
            tv_mon5.setText(getResources().getString(R.string.cal_may_ar));
            tv_mon6.setText(getResources().getString(R.string.cal_june_ar));
            tv_mon7.setText(getResources().getString(R.string.cal_july_ar));
            tv_mon8.setText(getResources().getString(R.string.cal_aug_ar));
            tv_mon9.setText(getResources().getString(R.string.cal_sep_ar));
            tv_mon10.setText(getResources().getString(R.string.cal_oct_ar));
            tv_mon11.setText(getResources().getString(R.string.cal_nov_ar));
            tv_mon12.setText(getResources().getString(R.string.cal_dec_ar));

            tv_week1.setText(getResources().getString(R.string.sun_ar));
            tv_week2.setText(getResources().getString(R.string.mon_ar));
            tv_week3.setText(getResources().getString(R.string.tue_ar));
            tv_week4.setText(getResources().getString(R.string.wed_ar));
            tv_week5.setText(getResources().getString(R.string.thu_ar));
            tv_week6.setText(getResources().getString(R.string.fri_ar));
            tv_week7.setText(getResources().getString(R.string.sat_ar));

        }


    }

    public void setMonth(int month) {
        if (month==1)
        {
            smoothScroll(tv_mon1);
            tv_mon1.performClick();
        }
        else if (month==2)
        {
            smoothScroll(tv_mon2);
            tv_mon2.performClick();

        }
        else if (month==3)
        {
            smoothScroll(tv_mon3);
            tv_mon3.performClick();

        } else if (month==4)
        {
            smoothScroll(tv_mon4);
            tv_mon4.performClick();

        } else if (month==5)
        {
            smoothScroll(tv_mon5);
            tv_mon5.performClick();

        } else if (month==6)
        {
            smoothScroll(tv_mon6);
            tv_mon6.performClick();

        } else if (month==7)
        {
            smoothScroll(tv_mon7);
            tv_mon7.performClick();

        } else if (month==8)
        {
            smoothScroll(tv_mon8);
            tv_mon8.performClick();

        } else if (month==9)
        {
            smoothScroll(tv_mon9);
            tv_mon9.performClick();

        } else if (month==10)
        {
            smoothScroll(tv_mon10);
            tv_mon10.performClick();

        } else if (month==11)
        {
            smoothScroll(tv_mon11);
            tv_mon11.performClick();

        } else if (month==12)
        {
            smoothScroll(tv_mon12);
            tv_mon12.performClick();

        }
    }


    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        // long-pressing a day
        /*grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id)
            {

            }
        });*/
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // handle long-press
                if (eventHandler != null) {
                    eventHandler.onDayLongPress((Date) parent.getItemAtPosition(position));
                }
            }
        });

        tv_mon1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                changemounthBackground(1);
                currentDate.set(Calendar.MONTH, Calendar.JANUARY);
                updateCalendar();
                if (eventHandler != null) {
                    eventHandler.onMonthChangeListener(Calendar.JANUARY);
                }

            }
        });

        tv_mon2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                changemounthBackground(2);
                currentDate.set(Calendar.MONTH, Calendar.FEBRUARY);
                updateCalendar();
                if (eventHandler != null) {
                    eventHandler.onMonthChangeListener(Calendar.FEBRUARY);
                }
//                int x, y;
//                x = tv_mon2.getLeft();
//                y = tv_mon2.getTop();
//                hsv.scrollTo(x, y);
            }
        });

        tv_mon3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                changemounthBackground(3);
                currentDate.set(Calendar.MONTH, Calendar.MARCH);
                updateCalendar();
                if (eventHandler != null) {
                    eventHandler.onMonthChangeListener(Calendar.MARCH);
                }
//                int x, y;
//                x = tv_mon3.getLeft();
//                y = tv_mon3.getTop();
//                hsv.scrollTo(x, y);
            }
        });
        tv_mon4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                changemounthBackground(4);
                currentDate.set(Calendar.MONTH, Calendar.APRIL);
                updateCalendar();
                if (eventHandler != null) {
                    eventHandler.onMonthChangeListener(Calendar.APRIL);
                }

//                int x, y;
//                x = tv_mon4.getLeft();
//                y = tv_mon4.getTop();
//                hsv.scrollTo(x, y);
            }
        });
        tv_mon5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                changemounthBackground(5);
                currentDate.set(Calendar.MONTH, Calendar.MAY);
                updateCalendar();
                if (eventHandler != null) {
                    eventHandler.onMonthChangeListener(Calendar.MAY);
                }
//                int x, y;
//                x = tv_mon5.getLeft();
//                y = tv_mon5.getTop();
//                hsv.scrollTo(x, y);
            }
        });
        tv_mon6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                changemounthBackground(6);
                currentDate.set(Calendar.MONTH, Calendar.JUNE);
                updateCalendar();
                if (eventHandler != null) {
                    eventHandler.onMonthChangeListener(Calendar.JUNE);
                }
//                int x, y;
//                x = tv_mon6.getLeft();
//                y = tv_mon6.getTop();
//                hsv.scrollTo(x, y);
            }
        });
        tv_mon7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                changemounthBackground(7);
                currentDate.set(Calendar.MONTH, Calendar.JULY);
                updateCalendar();
                if (eventHandler != null) {
                    eventHandler.onMonthChangeListener(Calendar.JULY);
                }
//                int x, y;
//                x = tv_mon7.getLeft();
//                y = tv_mon7.getTop();
//                hsv.scrollTo(x, y);
            }
        });
        tv_mon8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                changemounthBackground(8);
                currentDate.set(Calendar.MONTH, Calendar.AUGUST);
                updateCalendar();
                if (eventHandler != null) {
                    eventHandler.onMonthChangeListener(Calendar.AUGUST);
                }
//                int x, y;
//                x = tv_mon8.getLeft();
//                y = tv_mon8.getTop();
//                hsv.scrollTo(x, y);
            }
        });
        tv_mon9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                changemounthBackground(9);
                currentDate.set(Calendar.MONTH, Calendar.SEPTEMBER);
                updateCalendar();
                if (eventHandler != null) {
                    eventHandler.onMonthChangeListener(Calendar.SEPTEMBER);
                }
//                int x, y;
//                x = tv_mon9.getLeft();
//                y = tv_mon9.getTop();
//                hsv.scrollTo(x, y);
            }
        });
        tv_mon10.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                changemounthBackground(10);
                currentDate.set(Calendar.MONTH, Calendar.OCTOBER);
                updateCalendar();
                if (eventHandler != null) {
                    eventHandler.onMonthChangeListener(Calendar.OCTOBER);
                }
//                int x, y;
//                x = tv_mon10.getLeft();
//                y = tv_mon10.getTop();
//                hsv.scrollTo(x, y);
            }
        });
        tv_mon11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                changemounthBackground(11);
                currentDate.set(Calendar.MONTH, Calendar.NOVEMBER);
                updateCalendar();
                if (eventHandler != null) {
                    eventHandler.onMonthChangeListener(Calendar.NOVEMBER);
                }
//                int x, y;
//                x = tv_mon11.getLeft();
//                y = tv_mon11.getTop();
//                hsv.scrollTo(x, y);
            }
        });
        tv_mon12.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                changemounthBackground(12);
                currentDate.set(Calendar.MONTH, Calendar.DECEMBER);
                updateCalendar();
                if (eventHandler != null) {
                    eventHandler.onMonthChangeListener(Calendar.DECEMBER);
                }
//                int x, y;
//                x = tv_mon12.getLeft();
//                y = tv_mon12.getTop();
//                hsv.scrollTo(x, y);
            }
        });


    }

    public void updateCalendar() {
        updateCalendar(userevents);
    }

    public void updateCalendar(HashMap<String, Date> events) {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();
        userevents = events;
        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }


//        Log.e("celender date","===>>"+calendar.get(Calendar.MONTH)+"day ===>>"+calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events, calendar));

       /* // update title
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        txtDate.setText(sdf.format(currentDate.getTime()));*/
        /*
        change mounth bg related to season
        // set header color according to current season
        int month = currentDate.get(Calendar.MONTH);
        int season = monthSeason[month];
        int color = rainbow[season];

        header.setBackgroundColor(getResources().getColor(color));
        */
//        Log.e("aaaaaaaa","=====>>>"+currentDate.get(Calendar.MONTH));

        switch (currentDate.get(Calendar.MONTH)) {
            case 0:
                changemounthBackground(1);
                break;
            case 1:
                changemounthBackground(2);
                break;
            case 2:
                changemounthBackground(3);
                break;
            case 3:
                changemounthBackground(4);
                break;
            case 4:
                changemounthBackground(5);
                break;
            case 5:
                changemounthBackground(6);
                break;
            case 6:
                changemounthBackground(7);
                break;
            case 7:
                changemounthBackground(8);
                break;
            case 8:
                changemounthBackground(9);
                break;
            case 9:
                changemounthBackground(10);
                break;
            case 10:
                changemounthBackground(11);
                break;

            case 11:
                changemounthBackground(12);
                break;

        }

    }

    private class CalendarAdapter extends ArrayAdapter<Date> {
        // days with events
        private HashMap<String, Date> eventDays;

        // for view inflation
        private LayoutInflater inflater;
        int currentmounth;
        Calendar calendar;

        public CalendarAdapter(Context context, ArrayList<Date> days, HashMap<String, Date> eventDays) {
            super(context, R.layout.control_calendar_day, days);
            this.eventDays = eventDays;
            inflater = LayoutInflater.from(context);
            this.currentmounth = currentmounth;
        }

        public CalendarAdapter(Context context, ArrayList<Date> days, HashMap<String, Date> eventDays, Calendar calendar) {
            super(context, R.layout.control_calendar_day, days);
            this.eventDays = eventDays;
            inflater = LayoutInflater.from(context);
            this.calendar = calendar;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
//            Log.e("getview calender","mounth ===>>"+calendar.get(Calendar.MONTH)+" day ===>>"+calendar.get(Calendar.DATE)+" year ====>>"+calendar.get(Calendar.YEAR));
            // day in question
            Date date = getItem(position);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();
            int year1 = Integer.parseInt((String) DateFormat.format("yyyy", date));
            // today
            Date today = new Date();

            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.control_calendar_day, parent, false);

            MyHeaderTextView tv_date = (MyHeaderTextView) view.findViewById(R.id.tv_date);
            // if this day has an event, specify event image
            tv_date.setBackgroundResource(0);
            if (eventDays != null) {
//                Log.e("event is null","===>>");

                Iterator myVeryOwnIterator = eventDays.keySet().iterator();

                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    Date eventDate1 = (Date) eventDays.get(key);
//                    Log.e("eventDate","===>>"+eventDate1.getDate()+" event mounth===>>"+eventDate1.getMonth()+" event year===>>"+eventDate1.getYear());
//                    Log.e("Date","===>>"+day+" mounth===>>"+(month+1)+" year===>>"+year);
                    if (key.compareTo("meeting") == 0) {
                        if (eventDate1.getDate() == day &&
                                eventDate1.getMonth() == month &&
                                eventDate1.getYear() == year) {
                            // mark this day for event
                            tv_date.setBackgroundResource(R.drawable.calender_event_meeting);
                            tv_date.setTextColor(getResources().getColor(R.color.cal_set_event));
                            break;
                        }
                    } else if (key.compareTo("festival") == 0) {
                        if (eventDate1.getDate() == day &&
                                eventDate1.getMonth() == month &&
                                eventDate1.getYear() == year) {
                            // mark this day for event
                            tv_date.setBackgroundResource(R.drawable.calender_event_festival);
                            tv_date.setTextColor(getResources().getColor(R.color.cal_set_event));

                            break;
                        }
                    } else {
                        if (eventDate1.getDate() == day &&
                                eventDate1.getMonth() == month &&
                                eventDate1.getYear() == year) {
                            // mark this day for event
                            tv_date.setBackgroundResource(R.drawable.calender_event_holiday);
                            tv_date.setTextColor(getResources().getColor(R.color.cal_set_event));
                            break;
                        }
                    }

//                    Toast.makeText(ctx, "Key: "+key+" Value: "+value, Toast.LENGTH_LONG).show();
                }

                /* for (Date eventDate : eventDays)
                {
                    Log.e("eventDate","===>>"+eventDate.getMonth()+" event mounth===>>"+eventDate.getDate()+" event year===>>"+eventDate.getYear());
                    if (eventDate.getDate() == day &&
                            eventDate.getMonth() == month &&
                            eventDate.getYear() == year)
                    {
                        // mark this day for event
                        view.setBackgroundResource(R.drawable.calender_event_meeting);
                        break;
                    }
                }*/


            }

            // clear styling
//            tv_date.setTypeface(null, Typeface.NORMAL);
//            tv_date.setTextColor(Color.BLACK);


//            Log.e("mounth","===>>"+(month+1)+" today mounth===>>"+calendar.get(Calendar.MONTH)+" today year===>>"+today.getYear());
//            Log.e("year","===>>"+year1+" today mounth===>>"+calendar.get(Calendar.YEAR));

            /*if (month != today.getMonth() || year != today.getYear())
            {
                // if this day is outside current month, grey it out
                ((TextView)view).setTextColor(getResources().getColor(R.color.greyed_out));
            }
            else if (day == today.getDate())
            {
                // if it is today, set it to blue/bold
                ((TextView)view).setTypeface(null, Typeface.BOLD);
                ((TextView)view).setTextColor(getResources().getColor(R.color.today));
            }*/
            usermounth = calendar.get(Calendar.MONTH);
            useryear = calendar.get(Calendar.YEAR);
            if (usermounth == 0) {
                usermounth = 12;//set 12 for december bz it return 0 and dec mounth
            }

            if ((month + 1) != usermounth) {
                // if this day is outside current month, grey it out
                tv_date.setTextColor(getResources().getColor(R.color.greyed_out));
            } else if (day == today.getDate()) {
                // if it is today, set it to blue/bold
//                tv_date.setTypeface(null, Typeface.BOLD);
                tv_date.setTextColor(getResources().getColor(R.color.today));
            }


            // set text
            tv_date.setText(String.valueOf(date.getDate()));

            return view;
        }
    }


    private void changemounthBackground(int pos) {
        int x, y;
        switch (pos) {
            case 1:


               /* x = tv_mon1.getLeft();
                y = tv_mon1.getTop();
                hsv.scrollTo(x, y);*/
                hsv.post(new Runnable() {
                    @Override
                    public void run() {
                        int vLeft = tv_mon1.getLeft();
                        int vRight = tv_mon1.getRight();
                        int sWidth = hsv.getWidth();
                        ObjectAnimator animator = ObjectAnimator.ofInt(hsv, "scrollX", ((vLeft + vRight - sWidth) / 2));
                        animator.setDuration(100);
                        animator.start();

                        hsv.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
//                                  hsv.scrollTo(tv_mon7.getLeft(), tv_mon7.getTop());
                    }
                });

                tv_mon1.setBackgroundResource(R.drawable.celender_mounth_bg);
                tv_mon2.setBackgroundResource(0);
                tv_mon3.setBackgroundResource(0);
                tv_mon3.setBackgroundResource(0);
                tv_mon4.setBackgroundResource(0);
                tv_mon5.setBackgroundResource(0);
                tv_mon6.setBackgroundResource(0);
                tv_mon7.setBackgroundResource(0);
                tv_mon8.setBackgroundResource(0);
                tv_mon9.setBackgroundResource(0);
                tv_mon10.setBackgroundResource(0);
                tv_mon11.setBackgroundResource(0);
                tv_mon12.setBackgroundResource(0);

                tv_mon1.setTextColor(getResources().getColor(R.color.cal_month_select));
                tv_mon2.setTextColor(getResources().getColor(R.color.white));
                tv_mon3.setTextColor(getResources().getColor(R.color.white));
                tv_mon4.setTextColor(getResources().getColor(R.color.white));
                tv_mon5.setTextColor(getResources().getColor(R.color.white));
                tv_mon6.setTextColor(getResources().getColor(R.color.white));
                tv_mon7.setTextColor(getResources().getColor(R.color.white));
                tv_mon8.setTextColor(getResources().getColor(R.color.white));
                tv_mon9.setTextColor(getResources().getColor(R.color.white));
                tv_mon10.setTextColor(getResources().getColor(R.color.white));
                tv_mon11.setTextColor(getResources().getColor(R.color.white));
                tv_mon12.setTextColor(getResources().getColor(R.color.white));

                break;

            case 2:


               /* x = tv_mon2.getLeft();
                y = tv_mon2.getTop();
                hsv.scrollTo(x, y);*/

                hsv.post(new Runnable() {
                    @Override
                    public void run() {
                        int vLeft = tv_mon2.getLeft();
                        int vRight = tv_mon2.getRight();
                        int sWidth = hsv.getWidth();
                        ObjectAnimator animator = ObjectAnimator.ofInt(hsv, "scrollX", ((vLeft + vRight - sWidth) / 2));
                        animator.setDuration(100);
                        animator.start();

                        hsv.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
//                                  hsv.scrollTo(tv_mon7.getLeft(), tv_mon7.getTop());
                    }
                });

                tv_mon2.setBackgroundResource(R.drawable.celender_mounth_bg);
                tv_mon1.setBackgroundResource(0);
                tv_mon3.setBackgroundResource(0);
                tv_mon4.setBackgroundResource(0);
                tv_mon5.setBackgroundResource(0);
                tv_mon6.setBackgroundResource(0);
                tv_mon7.setBackgroundResource(0);
                tv_mon8.setBackgroundResource(0);
                tv_mon9.setBackgroundResource(0);
                tv_mon10.setBackgroundResource(0);
                tv_mon11.setBackgroundResource(0);
                tv_mon12.setBackgroundResource(0);

                tv_mon2.setTextColor(getResources().getColor(R.color.cal_month_select));
                tv_mon1.setTextColor(getResources().getColor(R.color.white));
                tv_mon3.setTextColor(getResources().getColor(R.color.white));
                tv_mon4.setTextColor(getResources().getColor(R.color.white));
                tv_mon5.setTextColor(getResources().getColor(R.color.white));
                tv_mon6.setTextColor(getResources().getColor(R.color.white));
                tv_mon7.setTextColor(getResources().getColor(R.color.white));
                tv_mon8.setTextColor(getResources().getColor(R.color.white));
                tv_mon9.setTextColor(getResources().getColor(R.color.white));
                tv_mon10.setTextColor(getResources().getColor(R.color.white));
                tv_mon11.setTextColor(getResources().getColor(R.color.white));
                tv_mon12.setTextColor(getResources().getColor(R.color.white));
                break;

            case 3:

                /*x = tv_mon3.getLeft();
                y = tv_mon3.getTop();
                hsv.scrollTo(x, y);*/

                hsv.post(new Runnable() {
                    @Override
                    public void run() {
                        int vLeft = tv_mon3.getLeft();
                        int vRight = tv_mon3.getRight();
                        int sWidth = hsv.getWidth();
                        ObjectAnimator animator = ObjectAnimator.ofInt(hsv, "scrollX", ((vLeft + vRight - sWidth) / 2));
                        animator.setDuration(100);
                        animator.start();

                        hsv.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
//                                  hsv.scrollTo(tv_mon7.getLeft(), tv_mon7.getTop());
                    }
                });

                tv_mon3.setBackgroundResource(R.drawable.celender_mounth_bg);
                tv_mon1.setBackgroundResource(0);
                tv_mon2.setBackgroundResource(0);
                tv_mon4.setBackgroundResource(0);
                tv_mon5.setBackgroundResource(0);
                tv_mon6.setBackgroundResource(0);
                tv_mon7.setBackgroundResource(0);
                tv_mon8.setBackgroundResource(0);
                tv_mon9.setBackgroundResource(0);
                tv_mon10.setBackgroundResource(0);
                tv_mon11.setBackgroundResource(0);
                tv_mon12.setBackgroundResource(0);

                tv_mon3.setTextColor(getResources().getColor(R.color.cal_month_select));
                tv_mon1.setTextColor(getResources().getColor(R.color.white));
                tv_mon2.setTextColor(getResources().getColor(R.color.white));
                tv_mon4.setTextColor(getResources().getColor(R.color.white));
                tv_mon5.setTextColor(getResources().getColor(R.color.white));
                tv_mon6.setTextColor(getResources().getColor(R.color.white));
                tv_mon7.setTextColor(getResources().getColor(R.color.white));
                tv_mon8.setTextColor(getResources().getColor(R.color.white));
                tv_mon9.setTextColor(getResources().getColor(R.color.white));
                tv_mon10.setTextColor(getResources().getColor(R.color.white));
                tv_mon11.setTextColor(getResources().getColor(R.color.white));
                tv_mon12.setTextColor(getResources().getColor(R.color.white));

                break;

            case 4:

                /*x = tv_mon4.getLeft();
                y = tv_mon4.getTop();
                hsv.scrollTo(x, y);*/

                hsv.post(new Runnable() {
                    @Override
                    public void run() {
                        int vLeft = tv_mon4.getLeft();
                        int vRight = tv_mon4.getRight();
                        int sWidth = hsv.getWidth();
                        ObjectAnimator animator = ObjectAnimator.ofInt(hsv, "scrollX", ((vLeft + vRight - sWidth) / 2));
                        animator.setDuration(100);
                        animator.start();

                        hsv.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
//                                  hsv.scrollTo(tv_mon7.getLeft(), tv_mon7.getTop());
                    }
                });

                tv_mon4.setBackgroundResource(R.drawable.celender_mounth_bg);
                tv_mon1.setBackgroundResource(0);
                tv_mon2.setBackgroundResource(0);
                tv_mon3.setBackgroundResource(0);
                tv_mon5.setBackgroundResource(0);
                tv_mon6.setBackgroundResource(0);
                tv_mon7.setBackgroundResource(0);
                tv_mon8.setBackgroundResource(0);
                tv_mon9.setBackgroundResource(0);
                tv_mon10.setBackgroundResource(0);
                tv_mon11.setBackgroundResource(0);
                tv_mon12.setBackgroundResource(0);

                tv_mon4.setTextColor(getResources().getColor(R.color.cal_month_select));
                tv_mon1.setTextColor(getResources().getColor(R.color.white));
                tv_mon2.setTextColor(getResources().getColor(R.color.white));
                tv_mon3.setTextColor(getResources().getColor(R.color.white));
                tv_mon5.setTextColor(getResources().getColor(R.color.white));
                tv_mon6.setTextColor(getResources().getColor(R.color.white));
                tv_mon7.setTextColor(getResources().getColor(R.color.white));
                tv_mon8.setTextColor(getResources().getColor(R.color.white));
                tv_mon9.setTextColor(getResources().getColor(R.color.white));
                tv_mon10.setTextColor(getResources().getColor(R.color.white));
                tv_mon11.setTextColor(getResources().getColor(R.color.white));
                tv_mon12.setTextColor(getResources().getColor(R.color.white));
                break;

            case 5:

                /*x = tv_mon5.getLeft();
                y = tv_mon5.getTop();
                hsv.scrollTo(x, y);*/

                hsv.post(new Runnable() {
                    @Override
                    public void run() {
                        int vLeft = tv_mon5.getLeft();
                        int vRight = tv_mon5.getRight();
                        int sWidth = hsv.getWidth();
                        ObjectAnimator animator = ObjectAnimator.ofInt(hsv, "scrollX", ((vLeft + vRight - sWidth) / 2));
                        animator.setDuration(100);
                        animator.start();

                        hsv.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
//                                  hsv.scrollTo(tv_mon7.getLeft(), tv_mon7.getTop());
                    }
                });

                tv_mon5.setBackgroundResource(R.drawable.celender_mounth_bg);
                tv_mon1.setBackgroundResource(0);
                tv_mon2.setBackgroundResource(0);
                tv_mon3.setBackgroundResource(0);
                tv_mon4.setBackgroundResource(0);
                tv_mon6.setBackgroundResource(0);
                tv_mon7.setBackgroundResource(0);
                tv_mon8.setBackgroundResource(0);
                tv_mon9.setBackgroundResource(0);
                tv_mon10.setBackgroundResource(0);
                tv_mon11.setBackgroundResource(0);
                tv_mon12.setBackgroundResource(0);

                tv_mon5.setTextColor(getResources().getColor(R.color.cal_month_select));
                tv_mon1.setTextColor(getResources().getColor(R.color.white));
                tv_mon2.setTextColor(getResources().getColor(R.color.white));
                tv_mon3.setTextColor(getResources().getColor(R.color.white));
                tv_mon4.setTextColor(getResources().getColor(R.color.white));
                tv_mon6.setTextColor(getResources().getColor(R.color.white));
                tv_mon7.setTextColor(getResources().getColor(R.color.white));
                tv_mon8.setTextColor(getResources().getColor(R.color.white));
                tv_mon9.setTextColor(getResources().getColor(R.color.white));
                tv_mon10.setTextColor(getResources().getColor(R.color.white));
                tv_mon11.setTextColor(getResources().getColor(R.color.white));
                tv_mon12.setTextColor(getResources().getColor(R.color.white));

                break;
            case 6:

                /*x = tv_mon6.getLeft();
                y = tv_mon6.getTop();
                hsv.scrollTo(x, y);*/

                hsv.post(new Runnable() {
                    @Override
                    public void run() {
                        int vLeft = tv_mon6.getLeft();
                        int vRight = tv_mon6.getRight();
                        int sWidth = hsv.getWidth();
                        ObjectAnimator animator = ObjectAnimator.ofInt(hsv, "scrollX", ((vLeft + vRight - sWidth) / 2));
                        animator.setDuration(100);
                        animator.start();

                        hsv.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
//                                  hsv.scrollTo(tv_mon7.getLeft(), tv_mon7.getTop());
                    }
                });

                tv_mon6.setBackgroundResource(R.drawable.celender_mounth_bg);
                tv_mon1.setBackgroundResource(0);
                tv_mon2.setBackgroundResource(0);
                tv_mon3.setBackgroundResource(0);
                tv_mon4.setBackgroundResource(0);
                tv_mon5.setBackgroundResource(0);
                tv_mon7.setBackgroundResource(0);
                tv_mon8.setBackgroundResource(0);
                tv_mon9.setBackgroundResource(0);
                tv_mon10.setBackgroundResource(0);
                tv_mon11.setBackgroundResource(0);
                tv_mon12.setBackgroundResource(0);

                tv_mon6.setTextColor(getResources().getColor(R.color.cal_month_select));
                tv_mon1.setTextColor(getResources().getColor(R.color.white));
                tv_mon2.setTextColor(getResources().getColor(R.color.white));
                tv_mon3.setTextColor(getResources().getColor(R.color.white));
                tv_mon4.setTextColor(getResources().getColor(R.color.white));
                tv_mon5.setTextColor(getResources().getColor(R.color.white));
                tv_mon7.setTextColor(getResources().getColor(R.color.white));
                tv_mon8.setTextColor(getResources().getColor(R.color.white));
                tv_mon9.setTextColor(getResources().getColor(R.color.white));
                tv_mon10.setTextColor(getResources().getColor(R.color.white));
                tv_mon11.setTextColor(getResources().getColor(R.color.white));
                tv_mon12.setTextColor(getResources().getColor(R.color.white));

                break;


            case 7:

              /*  x = tv_mon7.getLeft();
                y = tv_mon7.getTop();
                hsv.scrollTo(x, y);*/

                hsv.post(new Runnable() {
                    @Override
                    public void run() {
                        int vLeft = tv_mon7.getLeft();
                        int vRight = tv_mon7.getRight();
                        int sWidth = hsv.getWidth();
                        ObjectAnimator animator = ObjectAnimator.ofInt(hsv, "scrollX", ((vLeft + vRight - sWidth) / 2));
                        animator.setDuration(100);
                        animator.start();

                        hsv.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
//                                  hsv.scrollTo(tv_mon7.getLeft(), tv_mon7.getTop());
                    }
                });
//                Log.e("Scrolllllllllllll","===>>>");


                tv_mon7.setBackgroundResource(R.drawable.celender_mounth_bg);
                tv_mon1.setBackgroundResource(0);
                tv_mon2.setBackgroundResource(0);
                tv_mon3.setBackgroundResource(0);
                tv_mon4.setBackgroundResource(0);
                tv_mon5.setBackgroundResource(0);
                tv_mon6.setBackgroundResource(0);
                tv_mon8.setBackgroundResource(0);
                tv_mon9.setBackgroundResource(0);
                tv_mon10.setBackgroundResource(0);
                tv_mon11.setBackgroundResource(0);
                tv_mon12.setBackgroundResource(0);

                tv_mon7.setTextColor(getResources().getColor(R.color.cal_month_select));
                tv_mon1.setTextColor(getResources().getColor(R.color.white));
                tv_mon2.setTextColor(getResources().getColor(R.color.white));
                tv_mon3.setTextColor(getResources().getColor(R.color.white));
                tv_mon4.setTextColor(getResources().getColor(R.color.white));
                tv_mon5.setTextColor(getResources().getColor(R.color.white));
                tv_mon6.setTextColor(getResources().getColor(R.color.white));
                tv_mon8.setTextColor(getResources().getColor(R.color.white));
                tv_mon9.setTextColor(getResources().getColor(R.color.white));
                tv_mon10.setTextColor(getResources().getColor(R.color.white));
                tv_mon11.setTextColor(getResources().getColor(R.color.white));
                tv_mon12.setTextColor(getResources().getColor(R.color.white));

                break;

            case 8:

                /*x = tv_mon8.getLeft();
                y = tv_mon8.getTop();
                hsv.scrollTo(x, y);*/

                hsv.post(new Runnable() {
                    @Override
                    public void run() {
                        int vLeft = tv_mon8.getLeft();
                        int vRight = tv_mon8.getRight();
                        int sWidth = hsv.getWidth();
                        ObjectAnimator animator = ObjectAnimator.ofInt(hsv, "scrollX", ((vLeft + vRight - sWidth) / 2));
                        animator.setDuration(100);
                        animator.start();

                        hsv.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
//                                  hsv.scrollTo(tv_mon7.getLeft(), tv_mon7.getTop());
                    }
                });

                tv_mon8.setBackgroundResource(R.drawable.celender_mounth_bg);
                tv_mon1.setBackgroundResource(0);
                tv_mon2.setBackgroundResource(0);
                tv_mon3.setBackgroundResource(0);
                tv_mon4.setBackgroundResource(0);
                tv_mon5.setBackgroundResource(0);
                tv_mon6.setBackgroundResource(0);
                tv_mon7.setBackgroundResource(0);
                tv_mon9.setBackgroundResource(0);
                tv_mon10.setBackgroundResource(0);
                tv_mon11.setBackgroundResource(0);
                tv_mon12.setBackgroundResource(0);

                tv_mon8.setTextColor(getResources().getColor(R.color.cal_month_select));
                tv_mon1.setTextColor(getResources().getColor(R.color.white));
                tv_mon2.setTextColor(getResources().getColor(R.color.white));
                tv_mon3.setTextColor(getResources().getColor(R.color.white));
                tv_mon4.setTextColor(getResources().getColor(R.color.white));
                tv_mon5.setTextColor(getResources().getColor(R.color.white));
                tv_mon6.setTextColor(getResources().getColor(R.color.white));
                tv_mon7.setTextColor(getResources().getColor(R.color.white));
                tv_mon9.setTextColor(getResources().getColor(R.color.white));
                tv_mon10.setTextColor(getResources().getColor(R.color.white));
                tv_mon11.setTextColor(getResources().getColor(R.color.white));
                tv_mon12.setTextColor(getResources().getColor(R.color.white));

                break;
            case 9:

                /*x = tv_mon9.getLeft();
                y = tv_mon9.getTop();
                hsv.scrollTo(x, y);*/

                hsv.post(new Runnable() {
                    @Override
                    public void run() {
                        int vLeft = tv_mon9.getLeft();
                        int vRight = tv_mon9.getRight();
                        int sWidth = hsv.getWidth();
                        ObjectAnimator animator = ObjectAnimator.ofInt(hsv, "scrollX", ((vLeft + vRight - sWidth) / 2));
                        animator.setDuration(100);
                        animator.start();

                        hsv.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
//                                  hsv.scrollTo(tv_mon7.getLeft(), tv_mon7.getTop());
                    }
                });

                tv_mon9.setBackgroundResource(R.drawable.celender_mounth_bg);
                tv_mon1.setBackgroundResource(0);
                tv_mon2.setBackgroundResource(0);
                tv_mon3.setBackgroundResource(0);
                tv_mon4.setBackgroundResource(0);
                tv_mon5.setBackgroundResource(0);
                tv_mon6.setBackgroundResource(0);
                tv_mon7.setBackgroundResource(0);
                tv_mon8.setBackgroundResource(0);
                tv_mon10.setBackgroundResource(0);
                tv_mon11.setBackgroundResource(0);
                tv_mon12.setBackgroundResource(0);

                tv_mon9.setTextColor(getResources().getColor(R.color.cal_month_select));
                tv_mon1.setTextColor(getResources().getColor(R.color.white));
                tv_mon2.setTextColor(getResources().getColor(R.color.white));
                tv_mon3.setTextColor(getResources().getColor(R.color.white));
                tv_mon4.setTextColor(getResources().getColor(R.color.white));
                tv_mon5.setTextColor(getResources().getColor(R.color.white));
                tv_mon6.setTextColor(getResources().getColor(R.color.white));
                tv_mon7.setTextColor(getResources().getColor(R.color.white));
                tv_mon8.setTextColor(getResources().getColor(R.color.white));
                tv_mon10.setTextColor(getResources().getColor(R.color.white));
                tv_mon11.setTextColor(getResources().getColor(R.color.white));
                tv_mon12.setTextColor(getResources().getColor(R.color.white));

                break;
            case 10:

                /*x = tv_mon10.getLeft();
                y = tv_mon10.getTop();
                hsv.scrollTo(x, y);*/

                hsv.post(new Runnable() {
                    @Override
                    public void run() {
                        int vLeft = tv_mon10.getLeft();
                        int vRight = tv_mon10.getRight();
                        int sWidth = hsv.getWidth();
                        ObjectAnimator animator = ObjectAnimator.ofInt(hsv, "scrollX", ((vLeft + vRight - sWidth) / 2));
                        animator.setDuration(100);
                        animator.start();

                        hsv.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
//                                  hsv.scrollTo(tv_mon7.getLeft(), tv_mon7.getTop());
                    }
                });

                tv_mon10.setBackgroundResource(R.drawable.celender_mounth_bg);
                tv_mon1.setBackgroundResource(0);
                tv_mon2.setBackgroundResource(0);
                tv_mon3.setBackgroundResource(0);
                tv_mon4.setBackgroundResource(0);
                tv_mon5.setBackgroundResource(0);
                tv_mon6.setBackgroundResource(0);
                tv_mon7.setBackgroundResource(0);
                tv_mon8.setBackgroundResource(0);
                tv_mon9.setBackgroundResource(0);
                tv_mon11.setBackgroundResource(0);
                tv_mon12.setBackgroundResource(0);

                tv_mon10.setTextColor(getResources().getColor(R.color.cal_month_select));
                tv_mon1.setTextColor(getResources().getColor(R.color.white));
                tv_mon2.setTextColor(getResources().getColor(R.color.white));
                tv_mon3.setTextColor(getResources().getColor(R.color.white));
                tv_mon4.setTextColor(getResources().getColor(R.color.white));
                tv_mon5.setTextColor(getResources().getColor(R.color.white));
                tv_mon6.setTextColor(getResources().getColor(R.color.white));
                tv_mon7.setTextColor(getResources().getColor(R.color.white));
                tv_mon8.setTextColor(getResources().getColor(R.color.white));
                tv_mon9.setTextColor(getResources().getColor(R.color.white));
                tv_mon11.setTextColor(getResources().getColor(R.color.white));
                tv_mon12.setTextColor(getResources().getColor(R.color.white));

                break;
            case 11:

                /*x = tv_mon11.getLeft();
                y = tv_mon11.getTop();
                hsv.scrollTo(x, y);*/

                hsv.post(new Runnable() {
                    @Override
                    public void run() {
                        int vLeft = tv_mon11.getLeft();
                        int vRight = tv_mon11.getRight();
                        int sWidth = hsv.getWidth();
                        ObjectAnimator animator = ObjectAnimator.ofInt(hsv, "scrollX", ((vLeft + vRight - sWidth) / 2));
                        animator.setDuration(100);
                        animator.start();

                        hsv.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
//                                  hsv.scrollTo(tv_mon7.getLeft(), tv_mon7.getTop());
                    }
                });

                tv_mon11.setBackgroundResource(R.drawable.celender_mounth_bg);
                tv_mon1.setBackgroundResource(0);
                tv_mon2.setBackgroundResource(0);
                tv_mon3.setBackgroundResource(0);
                tv_mon4.setBackgroundResource(0);
                tv_mon5.setBackgroundResource(0);
                tv_mon6.setBackgroundResource(0);
                tv_mon7.setBackgroundResource(0);
                tv_mon8.setBackgroundResource(0);
                tv_mon9.setBackgroundResource(0);
                tv_mon10.setBackgroundResource(0);
                tv_mon12.setBackgroundResource(0);

                tv_mon11.setTextColor(getResources().getColor(R.color.cal_month_select));
                tv_mon1.setTextColor(getResources().getColor(R.color.white));
                tv_mon2.setTextColor(getResources().getColor(R.color.white));
                tv_mon3.setTextColor(getResources().getColor(R.color.white));
                tv_mon4.setTextColor(getResources().getColor(R.color.white));
                tv_mon5.setTextColor(getResources().getColor(R.color.white));
                tv_mon6.setTextColor(getResources().getColor(R.color.white));
                tv_mon7.setTextColor(getResources().getColor(R.color.white));
                tv_mon8.setTextColor(getResources().getColor(R.color.white));
                tv_mon9.setTextColor(getResources().getColor(R.color.white));
                tv_mon10.setTextColor(getResources().getColor(R.color.white));
                tv_mon12.setTextColor(getResources().getColor(R.color.white));

                break;
            case 12:

               /* x = tv_mon11.getLeft();
                y = tv_mon11.getTop();
                hsv.scrollTo(x, y);*/

                hsv.post(new Runnable() {
                    @Override
                    public void run() {
                        int vLeft = tv_mon12.getLeft();
                        int vRight = tv_mon12.getRight();
                        int sWidth = hsv.getWidth();
                        ObjectAnimator animator = ObjectAnimator.ofInt(hsv, "scrollX", ((vLeft + vRight - sWidth) / 2));
                        animator.setDuration(100);
                        animator.start();

                        hsv.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
//                                  hsv.scrollTo(tv_mon7.getLeft(), tv_mon7.getTop());
                    }
                });

                tv_mon12.setBackgroundResource(R.drawable.celender_mounth_bg);
                tv_mon1.setBackgroundResource(0);
                tv_mon2.setBackgroundResource(0);
                tv_mon3.setBackgroundResource(0);
                tv_mon4.setBackgroundResource(0);
                tv_mon5.setBackgroundResource(0);
                tv_mon6.setBackgroundResource(0);
                tv_mon7.setBackgroundResource(0);
                tv_mon8.setBackgroundResource(0);
                tv_mon9.setBackgroundResource(0);
                tv_mon10.setBackgroundResource(0);
                tv_mon11.setBackgroundResource(0);

                tv_mon12.setTextColor(getResources().getColor(R.color.cal_month_select));
                tv_mon1.setTextColor(getResources().getColor(R.color.white));
                tv_mon2.setTextColor(getResources().getColor(R.color.white));
                tv_mon3.setTextColor(getResources().getColor(R.color.white));
                tv_mon4.setTextColor(getResources().getColor(R.color.white));
                tv_mon5.setTextColor(getResources().getColor(R.color.white));
                tv_mon6.setTextColor(getResources().getColor(R.color.white));
                tv_mon7.setTextColor(getResources().getColor(R.color.white));
                tv_mon8.setTextColor(getResources().getColor(R.color.white));
                tv_mon9.setTextColor(getResources().getColor(R.color.white));
                tv_mon10.setTextColor(getResources().getColor(R.color.white));
                tv_mon11.setTextColor(getResources().getColor(R.color.white));

                break;

        }

    }


    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }


    public interface EventHandler {
        void onDayLongPress(Date date);

        void onMonthChangeListener(int month);
    }

    private void smoothScroll(TextView view) {


        int x, y;
        x = view.getLeft();
        y = view.getTop();
        hsv.scrollTo(x, y);


       /* int vLeft = view.getLeft();
        int vRight = view.getRight();
        int sWidth = hsv.getWidth();
        ObjectAnimator animator = ObjectAnimator.ofInt(hsv, "scrollX", ((vLeft + vRight - sWidth) / 2));
        animator.setDuration(100);
        animator.start();

        hsv.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);*/

    }
}
