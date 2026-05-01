package com.example.a1_smd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final Context context;
    private final List<Booking> bookingList;
    private final OnCancelClickListener cancelClickListener;

    public interface OnCancelClickListener {
        void onCancelClick(Booking booking);
    }

    public BookingAdapter(Context context, List<Booking> bookingList, OnCancelClickListener cancelClickListener) {
        this.context = context;
        this.bookingList = bookingList;
        this.cancelClickListener = cancelClickListener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvBookingMovieName.setText(booking.getMovieName());
        holder.tvBookingDateTime.setText(booking.getDateTime());
        holder.tvBookingSeats.setText(booking.getSeats() + " Ticket(s)");

        // Resolve drawable string to ID
        int resId = context.getResources().getIdentifier(booking.getPosterDrawable(), "drawable", context.getPackageName());
        if (resId != 0) {
            holder.ivBookingPoster.setImageResource(resId);
        } else {
            holder.ivBookingPoster.setImageResource(R.drawable.movie_poster_1); // fallback
        }

        holder.btnCancelBooking.setOnClickListener(v -> cancelClickListener.onCancelClick(booking));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBookingPoster;
        TextView tvBookingMovieName, tvBookingDateTime, tvBookingSeats;
        ImageButton btnCancelBooking;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBookingPoster = itemView.findViewById(R.id.ivBookingPoster);
            tvBookingMovieName = itemView.findViewById(R.id.tvBookingMovieName);
            tvBookingDateTime = itemView.findViewById(R.id.tvBookingDateTime);
            tvBookingSeats = itemView.findViewById(R.id.tvBookingSeats);
            btnCancelBooking = itemView.findViewById(R.id.btnCancelBooking);
        }
    }
}
