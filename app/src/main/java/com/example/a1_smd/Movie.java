package com.example.a1_smd;

public class Movie {
    private String name;
    private String genre;
    private int duration;
    private int posterResId;
    private String posterDrawable;
    private String trailerUrl;
    private boolean isNowShowing;
    private String showDate; // New field for movie-specific date

    public Movie(String name, String genre, int duration, int posterResId, String trailerUrl, boolean isNowShowing, String showDate) {
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.posterResId = posterResId;
        this.trailerUrl = trailerUrl;
        this.isNowShowing = isNowShowing;
        this.showDate = showDate;
    }

    public String getGenreWithDuration() {
        return genre + " | " + duration + " min";
    }

    public String getName() { return name; }
    public String getGenre() { return genre; }
    public int getDuration() { return duration; }
    public int getPosterResId() { return posterResId; }
    public String getTrailerUrl() { return trailerUrl; }
    public boolean isNowShowing() { return isNowShowing; }
    public String getShowDate() { return showDate; }

    public String getPosterDrawable() { return posterDrawable; }
    public void setPosterDrawable(String posterDrawable) { this.posterDrawable = posterDrawable; }
}
