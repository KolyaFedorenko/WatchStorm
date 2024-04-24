package com.example.moviereviewer.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviereviewer.DataClasses.Movie;
import com.example.moviereviewer.DataClasses.MoviesFromDatabase;
import com.example.moviereviewer.Dialogs.SearchDialog;
import com.example.moviereviewer.R;
import com.example.moviereviewer.DataClasses.Rating;
import com.example.moviereviewer.DataClasses.Result;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddMovieFragment extends Fragment {

    private String login;

    private EditText editMovieTitle, editMovieYear, editMovieDescription;
    private EditText editPlotRating, editCastRating, editVisualRating;
    private TextView textSelectImage;
    private Button buttonFindMovie, buttonSaveRating;
    private ConstraintLayout clImage;
    private CircleImageView ivSelectedImage;
    private ProgressBar progressBarUpload;
    private LottieAnimationView lottieAnimationUploaded;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String movieTitle, movieYear, pathToImage, description;
    private int plotRating, castRating, visualRating, audienceRating;

    private Result receivedResult;

    public AddMovieFragment(String login) {
        this.login = login;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_movie, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("WatchStorm/" + login +"/Movies");
        storageReference = FirebaseStorage.getInstance().getReference().child("WatchStorm/" + login + "/Images");
        findViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ImagePicker.Companion.with(AddMovieFragment.this)
                        .cropSquare()
                        .galleryOnly()
                        .maxResultSize(500, 500)
                        .compress(1024)
                        .start();
                return false;
            }
        });

        buttonFindMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovieFromDatabase(editMovieTitle.getText().toString());
            }
        });

        buttonSaveRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    assignVariables();
                    Rating rating = new Rating(plotRating, castRating, visualRating, audienceRating);
                    Movie movie = new Movie(movieTitle, movieYear, pathToImage, description, rating);

                    databaseReference.child(movieTitle).setValue(movie);
                    showToast("Movie " + "\"" + movieTitle + "\"" + " has been saved in database");
                    clearViews();
                } catch (Exception e) {
                    showToast("Sorry, there is an error");
                }
            }
        });
    }

    private void findViews(View view){
        editMovieTitle = view.findViewById(R.id.editMovieTitle);
        editMovieYear = view.findViewById(R.id.editMovieYear);
        editPlotRating = view.findViewById(R.id.editPlotRating);
        editCastRating = view.findViewById(R.id.editCastRating);
        editVisualRating = view.findViewById(R.id.editVisualRating);
        textSelectImage = view.findViewById(R.id.textSelectImage);
        buttonFindMovie = view.findViewById(R.id.buttonFindMovie);
        buttonSaveRating = view.findViewById(R.id.buttonSaveRating);
        clImage = view.findViewById(R.id.clImage);
        ivSelectedImage = view.findViewById(R.id.ivSelectedImage);
        progressBarUpload = view.findViewById(R.id.progressBarUpload);
        lottieAnimationUploaded = view.findViewById(R.id.lottieAnimationUploaded);
        editMovieDescription = view.findViewById(R.id.editMovieDescription);
    }

    private void showToast(String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            Uri imagePath = data.getData();
            String randomId = String.valueOf(new Random().nextInt(100000));
            ivSelectedImage.setImageURI(imagePath);
            progressBarUpload.setVisibility(View.VISIBLE);
            textSelectImage.setText(randomId);
            storageReference.child(randomId)
                    .putFile(imagePath)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            lottieAnimationUploaded.playAnimation();
                            progressBarUpload.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }

    private void getMovieFromDatabase(String movieTitle) {
        SearchDialog searchDialog = new SearchDialog(movieTitle);
        searchDialog.createDialog(getActivity(), true, R.layout.dialog_search);
        searchDialog.setListener(new SearchDialog.OnMovieSelectedListener() {
            @Override
            public void onMovieSelected(Result result) {
                setInfoInViews(result);
            }
        });
        if (!editMovieTitle.getText().toString().equals("")) {
            searchDialog.getResultsFromDatabase(editMovieTitle.getText().toString());
        }
    }

    private void setInfoInViews(Result result) {
        this.receivedResult = result;
        if (result.getMediaType().equals("movie")) {
            editMovieTitle.setText(result.getTitle());
            editMovieYear.setText(result.getReleaseDate().substring(0, 4));
        } else {
            editMovieTitle.setText(result.getName());
            editMovieYear.setText(result.getFirstAirDate().substring(0, 4));
        }
        textSelectImage.setText(result.getPosterPath().toString().substring(1));
        editMovieDescription.setText(result.getOverview());
        if (editMovieDescription.getText().length() > 300) editMovieDescription.setTextSize(12f);
        else editMovieDescription.setTextSize(14f);
    }

    private void clearViews(){
        editMovieTitle.setText("");
        editMovieYear.setText("");
        editMovieDescription.setText("");
        editPlotRating.setText("");
        editCastRating.setText("");
        editVisualRating.setText("");
        textSelectImage.setText(R.string.select_image);
        receivedResult = null;
    }

    private void assignVariables(){
        movieTitle = editMovieTitle.getText().toString();
        movieYear = editMovieYear.getText().toString();
        pathToImage = textSelectImage.getText().toString();
        description = editMovieDescription.getText().toString();
        try {
            Integer.parseInt(pathToImage);
        } catch (Exception e) {
            pathToImage = "https://image.tmdb.org/t/p/w500//" + pathToImage;
        }
        plotRating = Integer.parseInt(editPlotRating.getText().toString());
        castRating = Integer.parseInt(editCastRating.getText().toString());
        visualRating = Integer.parseInt(editVisualRating.getText().toString());
        if (receivedResult != null) audienceRating = (int) (receivedResult.getVoteAverage() * 10);
        else audienceRating = 0;
    }
}