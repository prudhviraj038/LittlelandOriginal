package com.leadinfosoft.littleland.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.leadinfosoft.littleland.R;
import com.leadinfosoft.littleland.utill.Utils;

/**
 * Created by Lead on 7/13/2017.
 */

public class MyEditText extends EditText {
    public MyEditText(Context context) {
        super(context);
//        setMyTypeFace(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setMyTypeFace(context);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setMyTypeFace(context);
    }
    public void setMyTypeFace(Context context)
    {
        if (Utils.userSelectedLang(context))
        {
            Typeface customFont = Typeface.createFromAsset(context.getAssets(),"Lato-Black.ttf");
            setTypeface(customFont);
            setHintTextColor(context.getResources().getColor(R.color.hint));
        }
        else
        {
            Typeface customFont = Typeface.createFromAsset(context.getAssets(),"GE_SS_Two_Medium.otf");
            setTypeface(customFont);
            setHintTextColor(context.getResources().getColor(R.color.hint));
        }

    }

}

