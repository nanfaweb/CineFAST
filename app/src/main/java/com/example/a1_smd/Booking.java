package com.example.a1_smd;

public class Booking {
    private String id;
    private String movieName;
    private int seats;
    private int totalPrice;
    private String dateTime;
    private String posterDrawable;
    private long timestamp;

    public Booking() {
        // Required empty constructor for Firebase
    }

    public Booking(String id, String movieName, int seats, int totalPrice, String dateTime, String posterDrawable, long timestamp) {
        this.id = id;
        this.movieName = movieName;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.dateTime = dateTime;
        this.posterDrawable = posterDrawable;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMovieName() { return movieName; }
    public int getSeats() { return seats; }
    public int getTotalPrice() { return totalPrice; }
    public String getDateTime() { return dateTime; }
    public String getPosterDrawable() { return posterDrawable; }
    public long getTimestamp() { return timestamp; }
}
