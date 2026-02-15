package com.example.a1_smd;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
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

        // Get data from intent
        String movieName = getIntent().getStringExtra("MOVIE_NAME");
        int seatCount = getIntent().getIntExtra("SELECTED_SEAT_COUNT", 0);
        int ticketPrice = getIntent().getIntExtra("TICKET_PRICE", 0);
        double snacksTotal = getIntent().getDoubleExtra("SNACKS_TOTAL", 0.0);
        ArrayList<String> snacksList = getIntent().getStringArrayListExtra("SNACKS_LIST");

        // Bind UI
        TextView tvMovieName = findViewById(R.id.tvMovieName);
        tvMovieName.setText(movieName);

        TextView tvSelectSeatCount = findViewById(R.id.tvSelectSeatCount);
        tvSelectSeatCount.setText(seatCount + " Tickets");

        TextView tvTicketPrice = findViewById(R.id.tvTicketPrice);
        tvTicketPrice.setText("$" + ticketPrice);

        // Populate Snacks list dynamically
        LinearLayout layoutSnacksList = findViewById(R.id.layoutSnacksList);
        if (snacksList != null && !snacksList.isEmpty()) {
            for (String snack : snacksList) {
                // snack string format: "2x Popcorn ($17.98)"
                String[] parts = snack.split("\\(");
                String namePart = parts[0];
                String pricePart = "(" + parts[1];

                // Create a row for each snack
                android.widget.RelativeLayout row = new android.widget.RelativeLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                row.setPadding(0, 8, 0, 8);

                TextView tvName = new TextView(this);
                tvName.setText(namePart);
                tvName.setTextColor(getResources().getColor(android.R.color.white));
                tvName.setTextSize(16);

                android.widget.RelativeLayout.LayoutParams paramsName = new android.widget.RelativeLayout.LayoutParams(
                        android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                        android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsName.addRule(android.widget.RelativeLayout.ALIGN_PARENT_START);
                tvName.setLayoutParams(paramsName);

                TextView tvPrice = new TextView(this);
                tvPrice.setText(pricePart.replace("(", "").replace(")", "")); // Clean up format if desired, or keep as
                                                                              // is
                tvPrice.setTextColor(getResources().getColor(android.R.color.white));
                tvPrice.setTextSize(16);
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
            tvNone.setText("No snacks selected");
            tvNone.setTextColor(0xFF888888); // Gray
            layoutSnacksList.addView(tvNone);
        }

        // Final Total
        TextView tvFinalTotal = findViewById(R.id.tvFinalTotal);
        double total = ticketPrice + snacksTotal;
        tvFinalTotal.setText("$" + String.format("%.2f", total));

        // Buttons
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Button btnSendTicket = findViewById(R.id.btnSendTicket);
        btnSendTicket.setOnClickListener(v -> {
            Toast.makeText(this, "Ticket Sent Successfully!", Toast.LENGTH_LONG).show();
            // In a real app, this would maybe navigate to home or reset the flow
            // finishAffinity(); // Optional: close all activities
        });
    }
}
