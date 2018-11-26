package com.leadinfosoft.littleland.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.Utils;

/**
 * Created by Lead on 7/13/2017.
 */

public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
//        setMyTypeFace(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setMyTypeFace(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setMyTypeFace(context);
    }

    public void setMyTypeFace(Context context)
    {
        if (Utils.userSelectedLang(context))
        {
            Typeface customFont = Typeface.createFromAsset(context.getAssets(),"fonts/Stone Sans Regular.ttf");
            setTypeface(customFont);
            setHintTextColor(context.getResources().getColor(R.color.hint));
        }
        else
        {
            Typeface customFont = Typeface.createFromAsset(context.getAssets(),"fonts/GE_Flow_Regular.otf");
            setTypeface(customFont);
            setHintTextColor(context.getResources().getColor(R.color.hint));
        }

    }

}