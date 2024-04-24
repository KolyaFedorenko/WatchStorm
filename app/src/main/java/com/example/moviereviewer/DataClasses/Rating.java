package com.example.moviereviewer.DataClasses;

public class Rating {
    private int plotRating;
    private int castRating;
    private int visualRating;
    private int compositeRating;
    private int usersAverageRating;

    public Rating(int plotRating, int castRating, int visualRating, int usersAverageRating) {
        this.plotRating = plotRating;
        this.castRating = castRating;
        this.visualRating = visualRating;
        this.compositeRating = Math.round((plotRating * 1.0f + castRating * 1.0f + visualRating * 1.0f) / 3);
        this.usersAverageRating = usersAverageRating;
    }

    public int getPlotRating() {
        return plotRating;
    }

    public void setPlotRating(int plotRating) {
        this.plotRating = plotRating;
    }

    public int getCastRating() {
        return castRating;
    }

    public void setCastRating(int castRating) {
        this.castRating = castRating;
    }

    public int getVisualRating() {
        return visualRating;
    }

    public void setVisualRating(int visualRating) {
        this.visualRating = visualRating;
    }

    public int getCompositeRating() {
        return compositeRating;
    }

    public void setCompositeRating(int compositeRating) {
        this.compositeRating = compositeRating;
    }

    public int getUsersAverageRating() {
        return usersAverageRating;
    }

    public void setUsersAverageRating(int usersAverageRating) {
        this.usersAverageRating = usersAverageRating;
    }
}
