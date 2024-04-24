package com.example.moviereviewer.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviereviewer.Adapters.FoundMovieAdapter;
import com.example.moviereviewer.DataClasses.MoviesFromDatabase;
import com.example.moviereviewer.DataClasses.Result;
import com.example.moviereviewer.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SearchDialog extends CustomDialog {

    public interface OnMovieSelectedListener{
        void onMovieSelected(Result result);
    }
    private OnMovieSelectedListener listener;

    private EditText dialogEditSearch;
    private ConstraintLayout dialogConstraintSearch;
    private RecyclerView dialogRecyclerFoundMovies;

    private FoundMovieAdapter adapter;
    private ArrayList<Result> results;
    private String movieToSearch;

    private Dialog dialog;
    private Context context;

    public SearchDialog(String movieToSearch) {
        this.movieToSearch = movieToSearch;
    }

    @Override
    public Dialog createDialog(Activity activity, boolean cancelable, int resource) {
        Dialog dialog = super.createDialog(activity, cancelable, resource);
        this.dialog = dialog;
        this.context = dialog.getContext();
        return dialog;
    }

    @Override
    public void findViews(Dialog dialog) {
        dialogEditSearch = dialog.findViewById(R.id.dialogEditSearch);
        dialogConstraintSearch = dialog.findViewById(R.id.dialogConstraintSearch);
        dialogRecyclerFoundMovies = dialog.findViewById(R.id.dialogRecyclerFoundMovies);
    }

    @Override
    public void useViews(Dialog dialog) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        dialogRecyclerFoundMovies.setLayoutManager(layoutManager);
        results = new ArrayList<>();

        dialogEditSearch.setText(movieToSearch);

        dialogConstraintSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResultsFromDatabase(dialogEditSearch.getText().toString());
            }
        });
    }

    public void getResultsFromDatabase(String movieToSearch) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String formattedTitle = movieToSearch.replaceAll("\\s", "%20");
        String url = "https://api.themoviedb.org/3/search/multi?" +
                "api_key=15d2ea6d0dc1d476efbca3eba2b9bbfb&query=" + formattedTitle;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    results.clear();
                    Gson gson = new Gson();
                    MoviesFromDatabase moviesFromDatabase = gson.fromJson(response, MoviesFromDatabase.class);
                    showResultsInRecyclerView(moviesFromDatabase);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Cannot get movies from database", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "JSON doesn't received", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void showResultsInRecyclerView(MoviesFromDatabase moviesFromDatabase) {
        for (Result result: moviesFromDatabase.getResults()) {
            if (result.getMediaType().equals("movie") || result.getMediaType().equals("tv")) {
                if (result.getReleaseDate() != null) {
                    if (result.getReleaseDate().length() > 4) {
                        if (result.getPosterPath() != null) results.add(result);
                    }
                } else if (result.getFirstAirDate() != null) {
                    if (result.getFirstAirDate().length() > 4) {
                        if (result.getPosterPath() != null) results.add(result);
                    }
                }
            }
        }
        if (dialogRecyclerFoundMovies.getAdapter() == null) {
            adapter = new FoundMovieAdapter(context, results);
            adapter.setListener(new FoundMovieAdapter.OnMovieSelectedListener() {
                @Override
                public void onMovieSelected(Result result) {
                    if (listener != null) listener.onMovieSelected(result);
                    dialog.dismiss();
                }
            });
            dialogRecyclerFoundMovies.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        dialogRecyclerFoundMovies.setVisibility(View.VISIBLE);
    }

    public void setListener(OnMovieSelectedListener listener) {
        this.listener = listener;
    }
}
