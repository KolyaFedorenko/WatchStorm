package com.example.moviereviewer.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviereviewer.DataClasses.Movie;
import com.example.moviereviewer.Adapters.MovieAdapter;
import com.example.moviereviewer.Dialogs.MovieDialog;
import com.example.moviereviewer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MoviesFragment extends Fragment implements MovieAdapter.MovieAdapterInterface {

    private String login;
    private DatabaseReference databaseReference;
    private ArrayList<Movie> movies;
    private MovieAdapter movieAdapter;

    public MoviesFragment(String login) {
        this.login = login;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference("WatchStorm/" + login + "/Movies");
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerViewMovies = view.findViewById(R.id.recyclerViewMovies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerViewMovies.setLayoutManager(layoutManager);
        recyclerViewMovies.setNestedScrollingEnabled(false);

        movies = new ArrayList<>();
        movieAdapter = new MovieAdapter(getActivity(), movies, this);
        getMovies();
        recyclerViewMovies.setAdapter(movieAdapter);
    }

    private void getMovies(){
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (movies.size() > 0) movies.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    movies.add(movie);
                }
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        databaseReference.addValueEventListener(eventListener);
    }

    @Override
    public void onItemLongPressed(Movie movie) {
         new MovieDialog(movie).createDialog(getActivity(), true, R.layout.dialog_movie);
    }
}