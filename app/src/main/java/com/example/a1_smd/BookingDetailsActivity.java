package com.example.a1_smd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class BookingDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        String movieName = getIntent().getStringExtra("MOVIE_NAME");
        int posterResourceId = getIntent().getIntExtra("POSTER_RESOURCE_ID", R.drawable.movie_poster_1);
        String theaterName = getIntent().getStringExtra("THEATER_NAME");
        String hallNumber = getIntent().getStringExtra("HALL_NUMBER");
        String date = getIntent().getStringExtra("BOOKING_DATE");
        String time = getIntent().getStringExtra("BOOKING_TIME");
        int seatCount = getIntent().getIntExtra("SELECTED_SEAT_COUNT", 0);
        int ticketPrice = getIntent().getIntExtra("TICKET_PRICE", 0);
        double snacksTotal = getIntent().getDoubleExtra("SNACKS_TOTAL", 0.0);
        ArrayList<String> snacksList = getIntent().getStringArrayListExtra("SNACKS_LIST");
        ArrayList<String> selectedSeats = getIntent().getStringArrayListExtra("SELECTED_SEATS");

        TextView tvMovieName = findViewById(R.id.tvMovieName);
        tvMovieName.setText(movieName);

        ImageView ivMoviePoster = findViewById(R.id.ivMoviePoster);
        ivMoviePoster.setImageResource(posterResourceId);

        TextView tvScreen = findViewById(R.id.tvScreen);
        tvScreen.setText(getString(R.string.screen_number));

        TextView tvAudioFormat = findViewById(R.id.tvAudioFormat);
        tvAudioFormat.setText(getString(R.string.audio_format_dolby_atmos));

        TextView tvTheaterName = findViewById(R.id.tvTheaterName);
        tvTheaterName.setText(theaterName != null ? theaterName : getString(R.string.theater_name));

        TextView tvHallNumber = findViewById(R.id.tvHallNumber);
        tvHallNumber.setText(hallNumber != null ? hallNumber : getString(R.string.hall_name));

        TextView tvDate = findViewById(R.id.tvDate);
        tvDate.setText(date != null ? date : getString(R.string.show_date));

        TextView tvTime = findViewById(R.id.tvTime);
        tvTime.setText(time != null ? time : getString(R.string.show_time));

        LinearLayout layoutTicketsList = findViewById(R.id.layoutTicketsList);
        if (selectedSeats != null && !selectedSeats.isEmpty()) {
            for (String seat : selectedSeats) {
                android.widget.RelativeLayout row = new android.widget.RelativeLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                row.setPadding(0, 8, 0, 8);

                TextView tvSeat = new TextView(this);
                tvSeat.setText(seat);
                tvSeat.setTextColor(getResources().getColor(R.color.secondary_text));
                tvSeat.setTextSize(14);

                android.widget.RelativeLayout.LayoutParams paramsSeat = new android.widget.RelativeLayout.LayoutParams(
                        android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                        android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsSeat.addRule(android.widget.RelativeLayout.ALIGN_PARENT_START);
                tvSeat.setLayoutParams(paramsSeat);

                TextView tvSeatPrice = new TextView(this);
                tvSeatPrice.setText(ticketPrice / seatCount + " PKR");
                tvSeatPrice.setTextColor(getResources().getColor(R.color.white));
                tvSeatPrice.setTextSize(14);
                tvSeatPrice.setTypeface(null, android.graphics.Typeface.BOLD);

                android.widget.RelativeLayout.LayoutParams paramsSeatPrice = new android.widget.RelativeLayout.LayoutParams(
                        android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                        android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsSeatPrice.addRule(android.widget.RelativeLayout.ALIGN_PARENT_END);
                tvSeatPrice.setLayoutParams(paramsSeatPrice);

                row.addView(tvSeat);
                row.addView(tvSeatPrice);
                layoutTicketsList.addView(row);
            }
        } else {
            android.widget.RelativeLayout row = new android.widget.RelativeLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            row.setPadding(0, 8, 0, 8);

            TextView tvSeats = new TextView(this);
            tvSeats.setText(seatCount + " Ticket(s)");
            tvSeats.setTextColor(getResources().getColor(R.color.secondary_text));
            tvSeats.setTextSize(14);

            android.widget.RelativeLayout.LayoutParams paramsSeats = new android.widget.RelativeLayout.LayoutParams(
                    android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsSeats.addRule(android.widget.RelativeLayout.ALIGN_PARENT_START);
            tvSeats.setLayoutParams(paramsSeats);

            TextView tvTicketPriceText = new TextView(this);
            tvTicketPriceText.setText(ticketPrice + " PKR");
            tvTicketPriceText.setTextColor(getResources().getColor(R.color.white));
            tvTicketPriceText.setTextSize(14);
            tvTicketPriceText.setTypeface(null, android.graphics.Typeface.BOLD);

            android.widget.RelativeLayout.LayoutParams priceParams = new android.widget.RelativeLayout.LayoutParams(
                    android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
            priceParams.addRule(android.widget.RelativeLayout.ALIGN_PARENT_END);
            tvTicketPriceText.setLayoutParams(priceParams);

            row.addView(tvSeats);
            row.addView(tvTicketPriceText);
            layoutTicketsList.addView(row);
        }

        LinearLayout layoutSnacksList = findViewById(R.id.layoutSnacksList);
        if (snacksList != null && !snacksList.isEmpty()) {
            for (String snack : snacksList) {
                String[] parts = snack.split("\\(");
                String namePart = parts[0].trim();
                String pricePart = "";
                if (parts.length > 1) {
                    pricePart = parts[1].replace(")", "").replace("PKR ", "").trim();
                }

                android.widget.RelativeLayout row = new android.widget.RelativeLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                row.setPadding(0, 8, 0, 8);

                TextView tvName = new TextView(this);
                tvName.setText(namePart);
                tvName.setTextColor(getResources().getColor(R.color.secondary_text));
                tvName.setTextSize(14);

                android.widget.RelativeLayout.LayoutParams paramsName = new android.widget.RelativeLayout.LayoutParams(
                        android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                        android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsName.addRule(android.widget.RelativeLayout.ALIGN_PARENT_START);
                tvName.setLayoutParams(paramsName);

                TextView tvPrice = new TextView(this);
                tvPrice.setText(pricePart + " PKR");
                tvPrice.setTextColor(getResources().getColor(R.color.white));
                tvPrice.setTextSize(14);
                tvPrice.setTypeface(null, android.graphics.Typeface.BOLD);

                android.widget.RelativeLayout.LayoutParams paramsPrice = new android.widget.RelativeLayout.LayoutParams(
                        android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                        android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsPrice.addRule(android.widget.RelativeLayout.ALIGN_PARENT_END);
                tvPrice.setLayoutParams(paramsPrice);

                row.addView(tvName);
                row.addView(tvPrice);
                layoutSnacksList.addView(row);
            }
        } else {
            TextView tvNone = new TextView(this);
            tvNone.setText(getString(R.string.no_snacks_selected));
            tvNone.setTextColor(getResources().getColor(R.color.secondary_text));
            layoutSnacksList.addView(tvNone);
        }

        TextView tvFinalTotal = findViewById(R.id.tvFinalTotal);
        int total = (int) (ticketPrice + snacksTotal);
        tvFinalTotal.setText(total + " PKR");

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> {
            Toast.makeText(this, "Menu opened", Toast.LENGTH_SHORT).show();
        });

        Button btnSendTicket = findViewById(R.id.btnSendTicket);
        btnSendTicket.setOnClickListener(v -> {
            String ticketDetails = formatTicketDetails(movieName, theaterName, hallNumber, date, time, selectedSeats, ticketPrice, snacksList, snacksTotal);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "CineFAST Booking Details");
            intent.putExtra(Intent.EXTRA_TEXT, ticketDetails);
            startActivity(Intent.createChooser(intent, "Share Ticket Via"));
        });
    }

    private String formatTicketDetails(String movieName, String theaterName, String hallNumber, String date, String time, ArrayList<String> selectedSeats, int ticketPrice, ArrayList<String> snacksList, double snacksTotal) {
        StringBuilder sb = new StringBuilder();
        sb.append("Movie: ").append(movieName).append("\n");
        sb.append("Theater: ").append(theaterName).append("\n");
        sb.append("Hall: ").append(hallNumber).append("\n");
        sb.append("Date: ").append(date).append("\n");
        sb.append("Time: ").append(time).append("\n");
        sb.append("Seats: ");
        if (selectedSeats != null && !selectedSeats.isEmpty()) {
            sb.append(String.join(", ", selectedSeats));
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
        sb.append("Total: ").append((int)(ticketPrice + snacksTotal)).append(" PKR\n");
        return sb.toString();
    }
}