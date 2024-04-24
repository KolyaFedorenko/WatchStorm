package com.example.moviereviewer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.moviereviewer.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerificationDialog extends CustomDialog {

    private TextView dialogTextAboutVerification, dialogTextRelation;
    private LinearProgressIndicator dialogProgressVerification;
    private Button dialogButtonVerify;
    private DatabaseReference databaseReference;
    private String login, userVerificationStatus;

    public VerificationDialog(String login, String userVerificationStatus) {
        this.login = login;
        this.userVerificationStatus = userVerificationStatus;
        databaseReference = FirebaseDatabase.getInstance().getReference("WatchStorm");
    }

    @Override
    public Dialog createDialog(Activity activity, boolean cancelable, int resource) {
        return super.createDialog(activity, cancelable, resource);
    }

    @Override
    public void findViews(Dialog dialog) {
        dialogTextAboutVerification = dialog.findViewById(R.id.dialogTextAboutVerification);
        dialogTextRelation = dialog.findViewById(R.id.dialogTextRelation);
        dialogProgressVerification = dialog.findViewById(R.id.dialogProgressVerification);
        dialogButtonVerify = dialog.findViewById(R.id.dialogButtonVerify);
    }

    @Override
    public void useViews(Dialog dialog) {
        Context context = dialog.getContext();
        getRatedMoviesCount(context);

        if (userVerificationStatus.equals("verified")) {
            dialogTextAboutVerification.setText(context.getString(R.string.account_already_verified));
            dialogButtonVerify.setVisibility(View.VISIBLE);
            dialogButtonVerify.setText(context.getString(R.string.remove_the_verified_status));
        }

        dialogButtonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userVerificationStatus.equals("verified")) {
                    databaseReference.child(login).child("Data").child("pathToImage").setValue("verified");
                } else {
                    databaseReference.child(login).child("Data").child("pathToImage").setValue("unverified");
                }
                dialog.dismiss();
            }
        });
    }

    private void getRatedMoviesCount(Context context) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int ratedMovies = (int) snapshot.child(login).child("Movies").getChildrenCount();
                dialogProgressVerification.setProgress(ratedMovies, true);
                dialogTextRelation.setVisibility(View.VISIBLE);
                dialogTextRelation.setText(context.getString(R.string.relation, ratedMovies));
                if (ratedMovies >= 100) dialogButtonVerify.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}
