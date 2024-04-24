package com.example.moviereviewer.DataClasses;

public class Movie {
    private String title;
    private String year;
    private String imagePath;
    private String description;
    private int usersAverageRating;
    private int plotRating;
    private int castRating;
    private int visualRating;
    private int compositeRating;

    public Movie(String title, String year, String imagePath, String description, Rating rating) {
        this.title = title;
        this.year = year;
        this.imagePath = imagePath;
        this.description = description;
        this.usersAverageRating = rating.getUsersAverageRating();
        this.plotRating = rating.getPlotRating();
        this.castRating = rating.getCastRating();
        this.visualRating = rating.getVisualRating();
        this.compositeRating = rating.getCompositeRating();
    }

    public Movie() { }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUsersAverageRating() {
        return usersAverageRating;
    }

    public void setUsersAverageRating(int usersAverageRating) {
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
}
