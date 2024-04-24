package com.example.moviereviewer.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.moviereviewer.Dialogs.AccountDeletingDialog;
import com.example.moviereviewer.Dialogs.ChangePasswordDialog;
import com.example.moviereviewer.Adapters.FavoriteMovieAdapter;
import com.example.moviereviewer.Dialogs.InstructionDialog;
import com.example.moviereviewer.DataClasses.Movie;
import com.example.moviereviewer.R;
import com.example.moviereviewer.Dialogs.VerificationDialog;
import com.example.moviereviewer.ViewableFragment;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements ViewableFragment {

    public interface SignOutListener{
        void onSignOut();
    }
    private SignOutListener signOutListener;

    private String login, verificationStatus;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ArrayList<Movie> favoriteMovies;
    private FavoriteMovieAdapter favoriteMovieAdapter;
    private boolean favoritesShowed = false;

    private Button buttonSingOut;
    private TextView textLogin, textTag;
    private CircleImageView imageProfile;
    private ConstraintLayout clFavoriteMovies, clDeleteMyAccount;
    private ConstraintLayout clChangePassword, clVerification, clAppUsage;
    private RecyclerView recyclerViewFavorites;
    private LottieAnimationView lottieVerifiedUser;

    public ProfileFragment(String login) {
        this.login = login;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        signOutListener = (SignOutListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("WatchStorm/" + login);
        storageReference = FirebaseStorage.getInstance().getReference(login + "/Images");
        findViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        useViews();
    }

    @Override
    public void findViews(View view) {
        buttonSingOut = view.findViewById(R.id.buttonSignOut);
        textLogin = view.findViewById(R.id.textLogin);
        textTag = view.findViewById(R.id.textTag);
        imageProfile = view.findViewById(R.id.imageProfile);
        clFavoriteMovies = view.findViewById(R.id.clFavoriteMovies);
        clDeleteMyAccount = view.findViewById(R.id.clDeleteMyAccount);
        recyclerViewFavorites = view.findViewById(R.id.recyclerViewFavorites);
        clChangePassword = view.findViewById(R.id.clChangePassword);
        clVerification = view.findViewById(R.id.clVerification);
        lottieVerifiedUser = view.findViewById(R.id.lottieVerifiedUser);
        clAppUsage = view.findViewById(R.id.clAppUsage);
    }

    @Override
    public void useViews() {
        getUserVerificationStatus();
        favoriteMovies = new ArrayList<>();
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(
                getActivity(),
                RecyclerView.HORIZONTAL,
                false));
        new PagerSnapHelper().attachToRecyclerView(recyclerViewFavorites);
        favoriteMovieAdapter = new FavoriteMovieAdapter(getActivity(), favoriteMovies);
        getFavoriteMovies();
        recyclerViewFavorites.setAdapter(favoriteMovieAdapter);


        buttonSingOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutListener.onSignOut();
            }
        });

        imageProfile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ImagePicker.Companion.with(ProfileFragment.this)
                        .cropSquare()
                        .galleryOnly()
                        .maxResultSize(500, 500)
                        .compress(1024)
                        .start();
                return false;
            }
        });

        clFavoriteMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!favoritesShowed){
                    recyclerViewFavorites.setVisibility(View.VISIBLE);
                    favoritesShowed = true;
                } else {
                    recyclerViewFavorites.setVisibility(View.GONE);
                    favoritesShowed = false;
                }
            }
        });

        clDeleteMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountDeletingDialog accountDeletingDialog = new AccountDeletingDialog(login);
                accountDeletingDialog.createDialog(getActivity(), true, R.layout.dialog_delete_account);
                accountDeletingDialog.setOnAccountDeleteListener(new AccountDeletingDialog.OnAccountDeleteListener() {
                    @Override
                    public void onAccountDelete() {
                        signOutListener.onSignOut();
                    }
                });
            }
        });

        clChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChangePasswordDialog(login)
                        .createDialog(getActivity(), true, R.layout.dialog_change_password);
            }
        });

        clVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new VerificationDialog(login, verificationStatus)
                        .createDialog(getActivity(), true, R.layout.dialog_verification);
            }
        });

        clAppUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InstructionDialog()
                        .createDialog(getActivity(), true, R.layout.dialog_instruction);
            }
        });

        textLogin.setText(login);
        Glide.with(getActivity())
                .load(FirebaseStorage.getInstance().getReference()
                .child(login + "/Images/ProfileImage"))
                .into(imageProfile);

        textTag.setText(getActivity().getString(R.string.tag, login).toLowerCase());
    }

    private void getFavoriteMovies(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (favoriteMovies.size() > 0) favoriteMovies.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Movie favoriteMovie = dataSnapshot.getValue(Movie.class);
                    favoriteMovies.add(favoriteMovie);
                }
                favoriteMovieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        };
        databaseReference
                .child("Movies")
                .orderByChild("compositeRating")
                .equalTo(100)
                .addValueEventListener(valueEventListener);
    }

    private void getUserVerificationStatus(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    verificationStatus = snapshot.child("Data").child("pathToImage").getValue(String.class);
                    if (verificationStatus.equals("verified")){
                        lottieVerifiedUser.setVisibility(View.VISIBLE);
                    } else {
                        lottieVerifiedUser.setVisibility(View.GONE);
                    }
                } catch(Exception ignored) { }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            imageProfile.setImageURI(selectedImage);
            storageReference.child("ProfileImage").putFile(selectedImage);
        }
    }
}