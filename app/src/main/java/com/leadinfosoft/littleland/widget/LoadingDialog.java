package com.leadinfosoft.littleland.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.leadinfosoft.littleland.R;

import butterknife.ButterKnife;

/**
 * Created by Lead on 7/24/2017.
 */

public class LoadingDialog extends ProgressDialog {
    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }
   /* @BindView(R.id.avi_loader)
    AVLoadingIndicatorView avi_loader;*/

   /* public LodingDialog(@NonNull Context context) {
        super(context);
    }

    public LodingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected LodingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loading_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setProgressStyle(ProgressDialog.STYLE_SPINNER);
        setProgress(0);
        setMax(100);
        setCancelable(true);
        ButterKnife.bind(this);

    }

}