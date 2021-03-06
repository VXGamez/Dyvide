package com.vx.dyvide.controller.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.vx.dyvide.R;

public class LoadingDialog {

    private static LoadingDialog sManager;
    private Object mutex = new Object();

    private Context mContext;
    private Dialog mDialog;

    private TextView tvSubtitle;

    public static LoadingDialog getInstance(Context context) {
        if (sManager == null) {
            sManager = new LoadingDialog(context);
        }
        return sManager;
    }

    public LoadingDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);
    }

    public void cancelLoadingDialog(){
        mDialog.cancel();
    }


    public void showLoadingDialog(String message) {
        mDialog.setContentView(R.layout.dialog_loading);
        tvSubtitle = (TextView) mDialog.findViewById(R.id.dialog_subtitle);
        tvSubtitle.setTextSize(15f);
        tvSubtitle.setText(message);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }



}
