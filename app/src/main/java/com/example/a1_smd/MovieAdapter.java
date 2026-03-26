package com.example.a1_smd;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> movieList;
    private final OnMovieClickListener listener;
    private final Context context;

    public interface OnMovieClickListener {
        void onBookClick(Movie movie);
    }

    public MovieAdapter(Context context, List<Movie> movieList, OnMovieClickListener listener) {
        this.context = context;
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.imgPoster.setImageResource(movie.getPosterResId());
        holder.tvMovieTitle.setText(movie.getName());
        holder.tvMovieDetails.setText(movie.getGenreWithDuration());

        if (movie.isNowShowing()) {
            holder.btnBookOrStatus.setText(context.getString(R.string.book_seats_text));
            holder.btnBookOrStatus.setEnabled(true);
            holder.btnBookOrStatus.setOnClickListener(v -> listener.onBookClick(movie));
            holder.btnBookOrStatus.setAlpha(1.0f);
        } else {
            holder.btnBookOrStatus.setText(context.getString(R.string.comingSoonText));
            // Keep it enabled to be clickable to trigger navigation if needed, 
            // but the logic inside SeatSelectionFragment will handle disabled seats
            // Or we just don't handle clicks if it's strictly "Coming Soon" disables everything
            holder.btnBookOrStatus.setOnClickListener(v -> listener.onBookClick(movie));
            
            // As per requirements: "Seats must not be clickable. Instead of original buttons display Coming Soon (disabled button)"
            // Wait, the requirement says "Instead of the original buttons, display: 1. Coming Soon (disabled button) 2. Watch Trailer"
            // This behavior is for SeatSelectionFragment.
            // But here on the Home screen in Coming soon, maybe it's still "Book Seats" to view the details/trailer, or we just let it be book seats. 
            // Actually, we can just say "View Details" or keep it "Book Seats".
            // Let's keep it clickable here so user can go to SeatSelectionFragment. 
            holder.btnBookOrStatus.setText(context.getString(R.string.book_seats_text));
            holder.btnBookOrStatus.setOnClickListener(v -> listener.onBookClick(movie));
        }

        holder.btnTrailer.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvMovieTitle;
        TextView tvMovieDetails;
        Button btnBookOrStatus;
        Button btnTrailer;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvMovieDetails = itemView.findViewById(R.id.tvMovieDetails);
            btnBookOrStatus = itemView.findViewById(R.id.btnBookOrStatus);
            btnTrailer = itemView.findViewById(R.id.btnTrailer);
        }
    }
}
