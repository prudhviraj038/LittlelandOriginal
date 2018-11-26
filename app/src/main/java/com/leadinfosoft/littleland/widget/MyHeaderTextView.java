package com.leadinfosoft.littleland.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.Utils;

import java.text.AttributedCharacterIterator;

/**
 * Created by Lead on 7/13/2017.
 */

public class MyHeaderTextView  extends TextView {

    boolean iitalic=false;
    public MyHeaderTextView(Context context) {
        super(context);
//        setMyTypeFace(context);
    }

    public MyHeaderTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setMyAttrbute(context, attrs);
//        setMyTypeFace(context);
    }

    public MyHeaderTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setMyAttrbute(context, attrs);
//        setMyTypeFace(context);
    }

    public void setMyAttrbute(Context context, AttributeSet attrs)
    {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.MyHeaderTextView);
        try
        {
            // try to load provided date format, and fallback to default otherwise
            iitalic = ta.getBoolean(R.styleable.MyHeaderTextView_italicdemo,false);

        }
        finally
        {
            ta.recycle();
        }
    }

    public void setMyTypeFace(Context context)
    {
        if(Utils.userSelectedLang(context))
        {
            if (iitalic)
            {

                Typeface customFont = Typeface.createFromAsset(context.getAssets(),"Lato-BlackItalic.ttf");
                setTypeface(customFont);
                setHintTextColor(context.getResources().getColor(R.color.hint));
            }
            else
            {
                Typeface customFont = Typeface.createFromAsset(context.getAssets(),"Lato-Black.ttf");
                setTypeface(customFont);
                setHintTextColor(context.getResources().getColor(R.color.hint));
            }

        }
        else
        {
            Typeface customFont = Typeface.createFromAsset(context.getAssets(),"GE_SS_Two_Medium.otf");
            setTypeface(customFont);
            setHintTextColor(context.getResources().getColor(R.color.hint));
        }

    }
}