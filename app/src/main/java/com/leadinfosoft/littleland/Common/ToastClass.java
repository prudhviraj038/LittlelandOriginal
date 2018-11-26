package com.leadinfosoft.littleland.Common;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

/**
 * Created by admin on 7/10/2017.
 */

public class ToastClass {

    private static final boolean DEBUG = true;

    public static void ToastError(Context context, String string) {

        if (DEBUG) {
            SuperToast superToast = new SuperToast(context);
            superToast.setDuration(Style.DURATION_SHORT);
            superToast.setText(string);
            superToast.setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED));
            superToast.setGravity(Gravity.CENTER);

            superToast.show();
        }


    }

    public static void ToastSuccess(Context context, String string) {

        if (DEBUG) {
            SuperToast superToast = new SuperToast(context);
            superToast.setDuration(Style.DURATION_SHORT);
            superToast.setText(string);
//            superToast.setTextColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_ORANGE));
            superToast.setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN));
            superToast.setGravity(Gravity.CENTER);
            superToast.show();
        }


    }


}
