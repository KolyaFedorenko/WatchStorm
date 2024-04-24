package com.example.moviereviewer.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.moviereviewer.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.storage.FirebaseStorage;

import de.hdodenhof.circleimageview.CircleImageView;

public class MovieView extends ConstraintLayout {

    public interface MovieLongClickListener {
        void onMovieLongClicked();
    }
    private MovieLongClickListener movieLongClickListener;

    private CircleImageView rvMovieImage;
    private CircularProgressIndicator rvCompositeRating;
    private TextView rvMovieTitle, rvMovieYear, rvMovieDescription;
    private TextView rvVisualRatingValue, rvCastRatingValue, rvPlotRatingValue;
    private TextView rvYourAverageRatingValue, rvAudienceAverageRatingValue;
    private LinearProgressIndicator rvVisualRating, rvCastRating, rvPlotRating;
    private LinearProgressIndicator rvYourAverageRating, rvAudienceAverageRating;
    private ConstraintLayout rvConstraintDescription, rvConstraintMovie;

    private Context context;

    public MovieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeView(context, attrs);
    }

    private void initializeView(Context context, AttributeSet attrs) {
        View view = inflate(context, R.layout.movie_view, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MovieView);
        try {
            findViews(view);
            fillViewsFromAttributes(typedArray);
            useViews();
        } finally {
            typedArray.recycle();
        }
    }

    private void findViews(View view) {
        rvMovieImage = view.findViewById(R.id.rvMovieImage);
        rvCompositeRating = view.findViewById(R.id.rvCompositeRating);
        rvMovieTitle = view.findViewById(R.id.rvMovieTitle);
        rvMovieYear = view.findViewById(R.id.rvMovieYear);
        rvMovieDescription = view.findViewById(R.id.rvMovieDescription);
        rvVisualRatingValue = view.findViewById(R.id.rvVisualRatingValue);
        rvCastRatingValue = view.findViewById(R.id.rvCastRatingValue);
        rvPlotRatingValue = view.findViewById(R.id.rvPlotRatingValue);
        rvYourAverageRatingValue = view.findViewById(R.id.rvYourAverageRatingValue);
        rvAudienceAverageRatingValue = view.findViewById(R.id.rvAudienceAverageRatingValue);
        rvVisualRating = view.findViewById(R.id.rvVisualRating);
        rvCastRating = view.findViewById(R.id.rvCastRating);
        rvPlotRating = view.findViewById(R.id.rvPlotRating);
        rvYourAverageRating = view.findViewById(R.id.rvYourAverageRating);
        rvAudienceAverageRating = view.findViewById(R.id.rvAudienceAverageRating);
        rvConstraintDescription = view.findViewById(R.id.rvConstraintDescription);
        rvConstraintMovie = view.findViewById(R.id.rvConstraintMovie);
    }

    private void fillViewsFromAttributes(TypedArray typedArray) {
        String imagePath = typedArray.getString(R.styleable.MovieView_imagePath);
        int compositeRating = typedArray.getInt(R.styleable.MovieView_composite_rating, 0);
        String movieTitle = typedArray.getString(R.styleable.MovieView_movie_title);
        String movieYear = typedArray.getString(R.styleable.MovieView_movie_year);
        String movieDescription = typedArray.getString(R.styleable.MovieView_movie_description);
        int visualRating = typedArray.getInt(R.styleable.MovieView_visual_rating, 0);
        int castRating = typedArray.getInt(R.styleable.MovieView_cast_rating, 0);
        int plotRating = typedArray.getInt(R.styleable.MovieView_plot_rating, 0);
        int audienceRating = typedArray.getInt(R.styleable.MovieView_audience_rating, 0);

        setImagePath(imagePath);
        setCompositeRating(compositeRating);
        setMovieTitle(movieTitle);
        setMovieYear(movieYear);
        setMovieDescription(movieDescription);
        setVisualRating(visualRating);
        setCastRating(castRating);
        setPlotRating(plotRating);
        setAudienceRating(audienceRating);
    }

    private void useViews() {
        rvConstraintDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rvMovieDescription.getMaxLines() == 3){
                    rvMovieDescription.setMaxLines(25);
                }
                else rvMovieDescription.setMaxLines(3);
            }
        });

        rvConstraintMovie.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (movieLongClickListener != null) {
                    movieLongClickListener.onMovieLongClicked();
                }
                return false;
            }
        });
    }

    public void setImagePath(String imagePath) {
        if (imagePath != null) {
            if (imagePath.contains("w500")){
                Glide.with(context)
                        .load(imagePath)
                        .placeholder(R.color.grayAccentLight)
                        .into(rvMovieImage);
            } else {
                Glide.with(context)
                        .load(FirebaseStorage.getInstance().getReference()
                                .child("KolyaFedorenko/Images/" + imagePath))
                        .placeholder(R.color.grayAccentLight)
                        .into(rvMovieImage);
            }
        }
    }

    public void setCompositeRating(int compositeRating) {
        rvCompositeRating.setProgress(compositeRating, true);
        rvYourAverageRating.setProgress(compositeRating, true);
        rvYourAverageRatingValue.setText(String.valueOf(compositeRating));
    }

    public void setMovieTitle(String movieTitle) {
        rvMovieTitle.setText(movieTitle);
    }

    public void setMovieYear(String movieYear) {
        rvMovieYear.setText(movieYear);
    }

    public void setMovieDescription(String movieDescription) {
        rvMovieDescription.setText(movieDescription);
    }

    public void setVisualRating(int visualRating) {
        rvVisualRating.setProgress(visualRating, true);
        rvVisualRatingValue.setText(String.valueOf(visualRating));
    }

    public void setCastRating(int castRating) {
        rvCastRating.setProgress(castRating, true);
        rvCastRatingValue.setText(String.valueOf(castRating));
    }

    public void setPlotRating(int plotRating) {
        rvPlotRating.setProgress(plotRating, true);
        rvPlotRatingValue.setText(String.valueOf(plotRating));
    }

    public void setAudienceRating(int audienceRating) {
        rvAudienceAverageRating.setProgress(audienceRating, true);
        rvAudienceAverageRatingValue.setText(String.valueOf(audienceRating));
    }

    public void setMovieLongClickListener(MovieLongClickListener movieLongClickListener) {
        this.movieLongClickListener = movieLongClickListener;
    }
}
