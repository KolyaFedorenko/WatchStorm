package com.example.moviereviewer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.moviereviewer.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountDeletingDialog extends CustomDialog {

    public interface OnAccountDeleteListener{
        void onAccountDelete();
    }
    private OnAccountDeleteListener listener;

    private ConstraintLayout constraintDeleteAccount;
    private EditText dialogEditDeletingUser;
    private String login;
    private boolean deletionConfirmed = false;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("WatchStorm");

    public AccountDeletingDialog(String login){
        this.login = login;
    }

    @Override
    public Dialog createDialog(Activity activity, boolean cancelable, int resource) {
        return super.createDialog(activity, cancelable, resource);
    }

    @Override
    public void findViews(Dialog dialog) {
        constraintDeleteAccount = dialog.findViewById(R.id.constraintDeleteAccount);
        dialogEditDeletingUser = dialog.findViewById(R.id.dialogEditDeletingUser);
    }

    @Override
    public void useViews(Dialog dialog) {
        Context context = dialog.getContext();
        constraintDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogEditDeletingUser.getVisibility() == View.GONE){
                    dialogEditDeletingUser.setVisibility(View.VISIBLE);
                } else {
                    dialogEditDeletingUser.setVisibility(View.GONE);
                }
            }
        });

        dialogEditDeletingUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(login)){
                    dialogEditDeletingUser.setBackground(context.getDrawable(R.drawable.rounded_button_black));
                    dialogEditDeletingUser.setTextColor(context.getColor(R.color.black));
                    dialogEditDeletingUser.setFocusable(false);
                    dialogEditDeletingUser.setClickable(true);
                    dialogEditDeletingUser.setInputType(InputType.TYPE_NULL);
                    deletionConfirmed = true;
                }
            }
        });

        dialogEditDeletingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deletionConfirmed && listener != null) {
                    databaseReference.child(login).removeValue();
                    listener.onAccountDelete();
                    dialog.dismiss();
                }
            }
        });
    }

    public void setOnAccountDeleteListener(OnAccountDeleteListener listener){
        this.listener = listener;
    }
}
