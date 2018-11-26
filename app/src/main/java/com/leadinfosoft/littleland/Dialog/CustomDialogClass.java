package com.leadinfosoft.littleland.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.leadinfosoft.littleland.R;


/**
 * Created by admin on 2/28/2017.
 */

public class CustomDialogClass extends Dialog {

    public Activity c;
    public Dialog d;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialogbox_loading);

    }

}
