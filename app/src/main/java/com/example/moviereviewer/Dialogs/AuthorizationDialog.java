package com.example.moviereviewer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.moviereviewer.PasswordHasher;
import com.example.moviereviewer.R;
import com.example.moviereviewer.DataClasses.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthorizationDialog extends CustomDialog {

    public interface SignInListener {
        void onSignIn(String login);
    }
    private SignInListener signInListener;

    private EditText dialogEditLogin, dialogEditPassword;
    private Button dialogButtonSignIn, dialogButtonSignUp;
    private String login, password, hashedPassword;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("WatchStorm");

    @Override
    public Dialog createDialog(Activity activity, boolean cancelable, int resource) {
        return super.createDialog(activity, cancelable, resource);
    }

    @Override
    public void findViews(Dialog dialog) {
        dialogEditLogin = dialog.findViewById(R.id.dialogEditLogin);
        dialogEditPassword = dialog.findViewById(R.id.dialogEditPassword);
        dialogButtonSignIn = dialog.findViewById(R.id.dialogButtonSignIn);
        dialogButtonSignUp = dialog.findViewById(R.id.dialogButtonSignUp);
    }

    @Override
    public void useViews(Dialog dialog) {
        dialogButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserData();
                databaseReference.child(login).child("Data")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try{
                            User receivedUser = snapshot.getValue(User.class);
                            if (receivedUser.getLogin().equals(login)
                                    && PasswordHasher
                                    .validatePassword(password, receivedUser.getPassword())) {
                                if (signInListener != null) signInListener.onSignIn(login);
                                dialog.dismiss();
                            } else {
                                showToast(dialog,"Incorrect login or password");
                            }
                        } catch (Exception ignored) {
                            showToast(dialog,"Something went wrong. Maybe you haven't registered?");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        });

        dialogButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserData();
                databaseReference.child(login).child("Data")
                        .setValue(new User(login, hashedPassword, ""));
                if (signInListener != null) signInListener.onSignIn(login);
                dialog.dismiss();
            }
        });
    }

    public void setOnSignInListener(SignInListener signInListener){
        this.signInListener = signInListener;
    }

    private void getUserData(){
        try{
            login = dialogEditLogin.getText().toString();
            password = dialogEditPassword.getText().toString();
            hashedPassword = PasswordHasher.generatePasswordHash(password);
        } catch (Exception ignored) { }
    }

    private void showToast(Dialog dialog, String text){
        Toast.makeText(dialog.getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
