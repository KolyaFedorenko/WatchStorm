package com.example.moviereviewer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviereviewer.DataClasses.Movie;
import com.example.moviereviewer.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<Movie> movies;
    private Context context;
    private String login;
    private static final String PREFS_FILE = "WatchStorm";
    private static final String PREF_LOGIN = "Login";

    public FavoriteMovieAdapter(Context context, List<Movie> movies){
        this.movies = movies;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.login = getLogin(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.favorite_movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie favoriteMovie = movies.get(position);
        if (favoriteMovie.getImagePath().contains("w500")){
            Glide.with(context)
                    .load(favoriteMovie.getImagePath())
                    .centerCrop()
                    .into(holder.rvFavoriteMovieImage);
        }
        else {
            Glide.with(context)
                    .load(FirebaseStorage.getInstance().getReference()
                            .child(login + "/Images/" + favoriteMovie.getImagePath()))
                    .centerCrop()
                    .into(holder.rvFavoriteMovieImage);
        }
        holder.rvFavoriteMovieTitle.setText(favoriteMovie.getTitle());
        holder.rvFavoriteMovieDescription.setText(favoriteMovie.getDescription());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final ShapeableImageView rvFavoriteMovieImage;
        public final TextView rvFavoriteMovieTitle;
        public final TextView rvFavoriteMovieDescription;
        public ViewHolder(View view){
            super(view);
            rvFavoriteMovieImage = view.findViewById(R.id.rvFavoriteMovieImage);
            rvFavoriteMovieTitle = view.findViewById(R.id.rvFavoriteMovieTitle);
            rvFavoriteMovieDescription = view.findViewById(R.id.rvFavoriteMovieDescription);
        }
    }

    private String getLogin(Context context){
        return context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
                .getString(PREF_LOGIN, "Login");
    }
}
