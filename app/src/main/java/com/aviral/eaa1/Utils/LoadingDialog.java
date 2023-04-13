package com.aviral.eaa1.Utils;

import android.app.Dialog;
import android.content.Context;

import com.aviral.eaa1.R;


public class LoadingDialog {
    private Dialog dialog;
    Context context;

    public LoadingDialog(Context context) {
        this.context = context;
        init();
    }

    private void init(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.progress_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        if (dialog != null){
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
