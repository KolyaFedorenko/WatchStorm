package com.example.moviereviewer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviereviewer.DataClasses.Result;
import com.example.moviereviewer.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoundMovieAdapter extends RecyclerView.Adapter<FoundMovieAdapter.ViewHolder> {

    public interface OnMovieSelectedListener{
        void onMovieSelected(Result result);
    }
    private OnMovieSelectedListener listener;

    private final List<Result> results;
    private final LayoutInflater inflater;
    private final Context context;

    public FoundMovieAdapter(Context context, List<Result> results) {
        this.results = results;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.found_movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Result result = results.get(position);
        String movieInfo = "";

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500/" + result.getPosterPath())
                .into(holder.foundMovieImage);

        if (result.getMediaType().equals("movie")) {
            movieInfo = context.getString(
                    R.string.found_movie_info,
                    result.getReleaseDate().substring(0, 4),
                    "Movie"
            );
            holder.textFoundMovieTitle.setText(result.getTitle());
        } else if (result.getMediaType().equals("tv")){
            movieInfo = context.getString(
                    R.string.found_movie_info,
                    result.getFirstAirDate().substring(0, 4),
                    "TV"
            );
            holder.textFoundMovieTitle.setText(result.getName());
        }

        holder.textFoundMovieInfo.setText(movieInfo);

        holder.foundMovieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onMovieSelected(result);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final CircleImageView foundMovieImage;
        public final TextView textFoundMovieTitle, textFoundMovieInfo;
        public final ConstraintLayout foundMovieView;

        public ViewHolder(View view) {
            super(view);
            foundMovieImage = view.findViewById(R.id.foundMovieImage);
            textFoundMovieTitle = view.findViewById(R.id.textFoundMovieTitle);
            textFoundMovieInfo = view.findViewById(R.id.textFoundedMovieInfo);
            foundMovieView = view.findViewById(R.id.foundMovieView);
        }
    }

    public void setListener(OnMovieSelectedListener listener) {
        this.listener = listener;
    }
}
