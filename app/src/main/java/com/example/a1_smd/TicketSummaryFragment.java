package com.example.a1_smd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class TicketSummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_summary, container, false);

        // Read all booking data from arguments
        String movieName = "";
        int posterResId = R.drawable.movie_poster_1;
        String theaterName = getString(R.string.theater_name);
        String hallNumber = getString(R.string.hall_name);
        String date = getString(R.string.show_date);
        String time = getString(R.string.show_time);
        int seatCount = 0;
        int ticketPrice = 0;
        double snacksTotal = 0.0;
        ArrayList<String> snacksList = new ArrayList<>();
        ArrayList<String> selectedSeats = new ArrayList<>();

        if (getArguments() != null) {
            movieName = getArguments().getString("movieName", "");
            posterResId = getArguments().getInt("posterResId", R.drawable.movie_poster_1);
            theaterName = getArguments().getString("theaterName", theaterName);
            hallNumber = getArguments().getString("hallNumber", hallNumber);
            date = getArguments().getString("bookingDate", date);
            time = getArguments().getString("bookingTime", time);
            seatCount = getArguments().getInt("selectedSeatCount", 0);
            ticketPrice = getArguments().getInt("ticketPrice", 0);
            snacksTotal = getArguments().getDouble("snacksTotal", 0.0);
            snacksList = getArguments().getStringArrayList("snacksList");
            selectedSeats = getArguments().getStringArrayList("selectedSeats");
            if (snacksList == null) snacksList = new ArrayList<>();
            if (selectedSeats == null) selectedSeats = new ArrayList<>();
        }

        // Save to SharedPreferences
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("BookingPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("movieName", movieName);
        editor.putInt("seatCount", seatCount);
        editor.putInt("totalPrice", ticketPrice);
        editor.apply();

        // Bind movie info
        TextView tvMovieName = view.findViewById(R.id.tvMovieName);
        tvMovieName.setText(movieName);

        ImageView ivMoviePoster = view.findViewById(R.id.ivMoviePoster);
        ivMoviePoster.setImageResource(posterResId);

        view.findViewById(R.id.tvScreen);
        view.findViewById(R.id.tvAudioFormat);

        TextView tvTheaterName = view.findViewById(R.id.tvTheaterName);
        tvTheaterName.setText(theaterName);

        TextView tvHallNumber = view.findViewById(R.id.tvHallNumber);
        tvHallNumber.setText(hallNumber);

        TextView tvDate = view.findViewById(R.id.tvDate);
        tvDate.setText(date);

        TextView tvTime = view.findViewById(R.id.tvTime);
        tvTime.setText(time);

        // Tickets list
        LinearLayout layoutTicketsList = view.findViewById(R.id.layoutTicketsList);
        if (!selectedSeats.isEmpty()) {
            int pricePerSeat = seatCount > 0 ? ticketPrice / seatCount : 0;
            for (String seat : selectedSeats) {
                layoutTicketsList.addView(
                        makeRow(seat, pricePerSeat + " PKR"));
            }
        } else {
            layoutTicketsList.addView(makeRow(seatCount + " Ticket(s)", ticketPrice + " PKR"));
        }

        // Snacks list
        LinearLayout layoutSnacksList = view.findViewById(R.id.layoutSnacksList);
        if (!snacksList.isEmpty()) {
            for (String snack : snacksList) {
                String[] parts = snack.split("\\(");
                String namePart = parts[0].trim();
                String pricePart = parts.length > 1
                        ? parts[1].replace(")", "").replace("PKR ", "").trim() + " PKR"
                        : "";
                layoutSnacksList.addView(makeRow(namePart, pricePart));
            }
        } else {
            TextView tvNone = new TextView(requireContext());
            tvNone.setText(getString(R.string.no_snacks_selected));
            tvNone.setTextColor(getResources().getColor(R.color.secondary_text));
            layoutSnacksList.addView(tvNone);
        }

        // Total
        TextView tvFinalTotal = view.findViewById(R.id.tvFinalTotal);
        int total = (int) (ticketPrice + snacksTotal);
        tvFinalTotal.setText(total + " PKR");

        // Back button
        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // Menu button (top right) — placeholder
        view.findViewById(R.id.btnMenuTop).setOnClickListener(v -> { /* no-op placeholder */ });

        // Send Ticket button
        final String finalMovieName = movieName;
        final String finalTheaterName = theaterName;
        final String finalHallNumber = hallNumber;
        final String finalDate = date;
        final String finalTime = time;
        final ArrayList<String> finalSelectedSeats = selectedSeats;
        final ArrayList<String> finalSnacksList = snacksList;
        final int finalTicketPrice = ticketPrice;
        final double finalSnacksTotal = snacksTotal;

        Button btnSendTicket = view.findViewById(R.id.btnSendTicket);
        btnSendTicket.setOnClickListener(v -> {
            String ticketDetails = formatTicketDetails(finalMovieName, finalTheaterName,
                    finalHallNumber, finalDate, finalTime, finalSelectedSeats,
                    finalTicketPrice, finalSnacksList, finalSnacksTotal);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "CineFAST Booking Details");
            intent.putExtra(Intent.EXTRA_TEXT, ticketDetails);
            startActivity(Intent.createChooser(intent, "Share Ticket Via"));
        });

        return view;
    }

    /** Creates a two-column row (label on left, value on right) for tickets/snacks lists. */
    private RelativeLayout makeRow(String label, String value) {
        RelativeLayout row = new RelativeLayout(requireContext());
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setPadding(0, 8, 0, 8);

        TextView tvLabel = new TextView(requireContext());
        tvLabel.setText(label);
        tvLabel.setTextColor(getResources().getColor(R.color.secondary_text));
        tvLabel.setTextSize(14);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_START);
        tvLabel.setLayoutParams(lp1);

        TextView tvValue = new TextView(requireContext());
        tvValue.setText(value);
        tvValue.setTextColor(getResources().getColor(R.color.white));
        tvValue.setTextSize(14);
        tvValue.setTypeface(null, android.graphics.Typeface.BOLD);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_END);
        tvValue.setLayoutParams(lp2);

        row.addView(tvLabel);
        row.addView(tvValue);
        return row;
    }

    private String formatTicketDetails(String movieName, String theaterName, String hallNumber,
                                       String date, String time, ArrayList<String> seats,
                                       int ticketPrice, ArrayList<String> snacksList,
                                       double snacksTotal) {
        StringBuilder sb = new StringBuilder();
        sb.append("Movie: ").append(movieName).append("\n");
        sb.append("Theater: ").append(theaterName).append("\n");
        sb.append("Hall: ").append(hallNumber).append("\n");
        sb.append("Date: ").append(date).append("\n");
        sb.append("Time: ").append(time).append("\n");
        sb.append("Seats: ");
        if (seats != null && !seats.isEmpty()) {
            sb.append(String.join(", ", seats));
        } else {
            sb.append("N/A");
        }
        sb.append("\n");
        sb.append("Ticket Price: ").append(ticketPrice).append(" PKR\n");
        sb.append("Snacks: ");
        if (snacksList != null && !snacksList.isEmpty()) {
            sb.append(String.join(", ", snacksList));
        } else {
            sb.append("None");
        }
        sb.append("\n");
        sb.append("Snacks Total: ").append(snacksTotal).append(" PKR\n");
        sb.append("Total: ").append((int) (ticketPrice + snacksTotal)).append(" PKR\n");
        return sb.toString();
    }
}
