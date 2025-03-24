package com.example.a1_smd;

public class Movie {
    private String name;
    private String genre;
    private int duration;
    private int posterResId;
    private String trailerUrl;
    private boolean isNowShowing;

    public Movie(String name, String genre, int duration, int posterResId, String trailerUrl, boolean isNowShowing) {
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.posterResId = posterResId;
        this.trailerUrl = trailerUrl;
        this.isNowShowing = isNowShowing;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public int getDuration() {
        return duration;
    }

    public int getPosterResId() {
        return posterResId;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public boolean isNowShowing() {
        return isNowShowing;
    }

    public String getGenreWithDuration() {
        return genre + " / " + duration + " min";
    }
}
