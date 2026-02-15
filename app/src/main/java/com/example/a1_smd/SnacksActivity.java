package com.example.a1_smd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class SnacksActivity extends AppCompatActivity {

    private String movieName;
    private int selectedSeatCount;
    private int ticketPrice;

    // Snack data class
    public static class SnackItem implements Serializable {
        String name;
        String description;
        int price;
        int quantity;
        int imageResId;

        public SnackItem(String name, String description, int price, int imageResId) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.imageResId = imageResId;
            this.quantity = 0;
        }
    }

    private List<SnackItem> snacks = new ArrayList<>();
    private TextView[] quantityViews = new TextView[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks);

        // Get data from previous activity
        movieName = getIntent().getStringExtra("MOVIE_NAME");

        selectedSeatCount = getIntent().getIntExtra("SELECTED_SEAT_COUNT", 0);
        ticketPrice = getIntent().getIntExtra("TOTAL_PRICE", 0);
        // Get poster resource ID from intent
        int posterResourceId = getIntent().getIntExtra("POSTER_RESOURCE_ID", R.drawable.movie_poster_1);

        // Initialize Data
        snacks.add(new SnackItem("Popcorn", "Large / Buttered", 250, R.drawable.popcorn_img));
        snacks.add(new SnackItem("Nachos", "With Cheese Dip", 270, R.drawable.nachos_img));
        snacks.add(new SnackItem("Soft Drink", "Large / Any Flavor", 100, R.drawable.softdrink_img));
        snacks.add(new SnackItem("Candy Mix", "Assorted Candies", 120, R.drawable.candymix_img));

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Bind UI
        setupSnackItem(0, R.id.snackPopcorn);
        setupSnackItem(1, R.id.snackNachos);
        setupSnackItem(2, R.id.snackDrink);
        setupSnackItem(3, R.id.snackCandy);

        Button btnConfirm = findViewById(R.id.btnConfirmSnacks);
        btnConfirm.setOnClickListener(v -> {
            Intent intent = new Intent(SnacksActivity.this, BookingDetailsActivity.class);
            intent.putExtra("MOVIE_NAME", movieName);
            intent.putExtra("POSTER_RESOURCE_ID", posterResourceId); // Forward poster resource ID
            intent.putExtra("SELECTED_SEAT_COUNT", selectedSeatCount);
            intent.putExtra("TICKET_PRICE", ticketPrice);

            // Calculate total snack price
            double totalSnacks = 0;
            ArrayList<String> snackDetails = new ArrayList<>();
            for (SnackItem item : snacks) {
                if (item.quantity > 0) {
                    totalSnacks += (item.price * item.quantity);
                    snackDetails.add(item.quantity + "x " + item.name + " (PKR " + (item.price * item.quantity) + ")");
                }
            }
            intent.putExtra("SNACKS_TOTAL", totalSnacks);
            intent.putStringArrayListExtra("SNACKS_LIST", snackDetails);

            // Pass selected seats to next activity
            ArrayList<String> selectedSeats = getIntent().getStringArrayListExtra("SELECTED_SEATS");
            intent.putStringArrayListExtra("SELECTED_SEATS", selectedSeats);

            startActivity(intent);
        });
    }

    private void setupSnackItem(int index, int includeId) {
        View view = findViewById(includeId);
        SnackItem item = snacks.get(index);

        TextView tvName = view.findViewById(R.id.snackName);
        TextView tvDesc = view.findViewById(R.id.snackDesc);
        TextView tvPrice = view.findViewById(R.id.snackPrice);
        TextView tvQty = view.findViewById(R.id.tvQuantity);
        ImageButton btnMinus = view.findViewById(R.id.btnMinus);
        ImageButton btnPlus = view.findViewById(R.id.btnPlus);
        ImageView imgSnack = view.findViewById(R.id.snackImage);

        tvName.setText(item.name);
        tvDesc.setText(item.description);
        tvPrice.setText("PKR " + item.price);
        tvQty.setText(String.valueOf(item.quantity));
        imgSnack.setImageResource(item.imageResId);

        quantityViews[index] = tvQty;

        btnMinus.setOnClickListener(v -> {
            if (item.quantity > 0) {
                item.quantity--;
                tvQty.setText(String.valueOf(item.quantity));
            }
        });

        btnPlus.setOnClickListener(v -> {
            item.quantity++;
            tvQty.setText(String.valueOf(item.quantity));
        });
    }
}
