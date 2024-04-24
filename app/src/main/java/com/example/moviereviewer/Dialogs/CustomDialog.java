package com.example.moviereviewer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

public abstract class CustomDialog {

    public Dialog createDialog(Activity activity, boolean cancelable, int resource) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(cancelable);
        dialog.setContentView(resource);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        findViews(dialog);
        useViews(dialog);
        dialog.show();
        return dialog;
    }

    public abstract void findViews(Dialog dialog);
    public abstract void useViews(Dialog dialog);
}
