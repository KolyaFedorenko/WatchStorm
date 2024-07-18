package com.kolyafedorenko.watchstorm.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kolyafedorenko.watchstorm.R;
import com.github.gongw.VerifyCodeView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WatchStormWebDialog extends CustomDialog {

    private VerifyCodeView dialogVerifyCodeView;
    private Button dialogButtonSaveDigitCode;
    private DatabaseReference webCodeReference;
    private String webCode;

    public WatchStormWebDialog(String login) {
        webCodeReference = FirebaseDatabase
                .getInstance()
                .getReference("WatchStormWeb")
                .child("WebCodes")
                .child(login);
    }

    @Override
    public Dialog createDialog(Activity activity, boolean cancelable, int resource) {
        return super.createDialog(activity, cancelable, resource);
    }

    @Override
    public void findViews(Dialog dialog) {
        dialogVerifyCodeView = dialog.findViewById(R.id.dialogVerifyCodeView);
        dialogButtonSaveDigitCode = dialog.findViewById(R.id.dialogButtonSaveDigitCode);
    }

    @Override
    public void useViews(Dialog dialog) {
        dialogVerifyCodeView.setOnAllFilledListener(new VerifyCodeView.OnAllFilledListener() {
            @Override
            public void onAllFilled(String text) {
                dialogButtonSaveDigitCode.setVisibility(View.VISIBLE);
                webCode = text;
            }
        });

        dialogVerifyCodeView.setOnTextChangedListener(new VerifyCodeView.OnTextChangedListener() {
            @Override
            public void onTextChanged(String text) {
                if (text.length() < 6) {
                    dialogButtonSaveDigitCode.setVisibility(View.GONE);
                }
            }
        });

        dialogButtonSaveDigitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    webCodeReference.setValue(webCode);
                    Toast.makeText(dialog.getContext(),"Your 6-digit code has been saved", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (Exception ignored) { }
            }
        });
    }
}
