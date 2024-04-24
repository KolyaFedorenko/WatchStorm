package com.example.moviereviewer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.moviereviewer.PasswordHasher;
import com.example.moviereviewer.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordDialog extends CustomDialog {

    private EditText dialogEditNewPassword, dialogConfirmNewPassword;
    private Button dialogButtonChangePassword;
    private DatabaseReference passwordReference;

    public ChangePasswordDialog(String login){
        passwordReference = FirebaseDatabase
                .getInstance()
                .getReference("WatchStorm")
                .child(login)
                .child("Data")
                .child("password");
    }

    @Override
    public Dialog createDialog(Activity activity, boolean cancelable, int resource) {
        return super.createDialog(activity, cancelable, resource);
    }

    @Override
    public void findViews(Dialog dialog) {
        dialogEditNewPassword = dialog.findViewById(R.id.dialogEditNewPassword);
        dialogConfirmNewPassword = dialog.findViewById(R.id.dialogEditConfirmPassword);
        dialogButtonChangePassword = dialog.findViewById(R.id.dialogButtonChangePassword);
    }

    @Override
    public void useViews(Dialog dialog) {

        dialogConfirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String newPassword = dialogEditNewPassword.getText().toString();
                String passwordConfirmation = dialogConfirmNewPassword.getText().toString();
                if (newPassword.equals(passwordConfirmation)) {
                    dialogButtonChangePassword.setVisibility(View.VISIBLE);
                } else {
                    dialogButtonChangePassword.setVisibility(View.GONE);
                }
            }
        });

        dialogButtonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String hashedPassword = PasswordHasher.generatePasswordHash(dialogEditNewPassword.getText().toString());
                    passwordReference.setValue(hashedPassword);
                    dialog.dismiss();
                } catch (Exception ignored) { }
            }
        });
    }
}
