package com.example.a1_smd;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SeatSelectionActivity extends AppCompatActivity {

    private static final int ROWS = 7;
    private static final int COLS = 7; // 3 left + 1 aisle + 3 right
    private static final int AISLE_COL = 3; // column index used as aisle spacer
    private static final int PRICE_PER_SEAT = 1000;

    // Seat states
    private static final int STATE_AVAILABLE = 0;
    private static final int STATE_SELECTED = 1;
    private static final int STATE_BOOKED = 2;
    private static final int STATE_AISLE = -1;

    private int[][] seatState = new int[ROWS][COLS];
    private View[][] seatViews = new View[ROWS][COLS];
    private int selectedCount = 0;

    private TextView tvSeatInfo;
    private Button btnProceedSnacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        // Receive movie name from intent
        String movieName = getIntent().getStringExtra("MOVIE_NAME");
        TextView tvMovieTitle = findViewById(R.id.tvMovieTitle);
        if (movieName != null) {
            tvMovieTitle.setText(movieName);
        }

        tvSeatInfo = findViewById(R.id.tvSeatInfo);
        btnProceedSnacks = findViewById(R.id.btnProceedSnacks);
        Button btnBookSeats = findViewById(R.id.btnBookSeats);

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Initialize seat states
        initSeatStates();

        // Build seat grid
        GridLayout seatGrid = findViewById(R.id.seatGrid);
        buildSeatGrid(seatGrid);

        // Update info text
        updateSeatInfo();

        // Book Seats button – directly confirm booking (skip snacks)
        btnBookSeats.setOnClickListener(v -> {
            if (selectedCount == 0) {
                Toast.makeText(this, R.string.select_at_least_one_seat, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.booking_confirmed, Toast.LENGTH_SHORT).show();
                // TODO: Navigate to Ticket Summary Screen
            }
        });

        // Proceed to Snacks button – disabled until ≥1 seat selected
        btnProceedSnacks.setOnClickListener(v -> {
            Toast.makeText(this, R.string.proceed_to_snacks, Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Snacks & Drinks Screen
        });
    }

    /**
     * Pre-seed the seat grid: mark the aisle column and randomly mark some seats as
     * booked.
     */
    private void initSeatStates() {
        // All seats start as available
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (c == AISLE_COL) {
                    seatState[r][c] = STATE_AISLE;
                } else {
                    seatState[r][c] = STATE_AVAILABLE;
                }
            }
        }

        // Pre-book specific seats to match the design (red seats)
        // Row 1 (index 1): first seat left, last two right
        bookSeat(1, 0);
        bookSeat(1, 4);
        bookSeat(1, 6);

        // Row 4 (index 4): some on left, some on right
        bookSeat(4, 1);
        bookSeat(4, 2);

        // Row 5 (index 5): a few
        bookSeat(5, 1);
        bookSeat(5, 2);

        // Row 6 (index 6): bottom row
        bookSeat(6, 4);
        bookSeat(6, 5);
        bookSeat(6, 6);
    }

    private void bookSeat(int row, int col) {
        seatState[row][col] = STATE_BOOKED;
    }

    /**
     * Build the seat grid programmatically.
     */
    private void buildSeatGrid(GridLayout grid) {
        int seatSize = (int) (36 * getResources().getDisplayMetrics().density); // 36dp
        int seatMargin = (int) (4 * getResources().getDisplayMetrics().density); // 4dp
        int aisleWidth = (int) (16 * getResources().getDisplayMetrics().density); // 16dp gap

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (c == AISLE_COL) {
                    // Aisle spacer – invisible View
                    View spacer = new View(this);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = aisleWidth;
                    params.height = seatSize;
                    params.rowSpec = GridLayout.spec(r);
                    params.columnSpec = GridLayout.spec(c);
                    spacer.setLayoutParams(params);
                    grid.addView(spacer);
                    seatViews[r][c] = spacer;
                    continue;
                }

                View seat = new View(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = seatSize;
                params.height = seatSize;
                params.setMargins(seatMargin, seatMargin, seatMargin, seatMargin);
                params.rowSpec = GridLayout.spec(r);
                params.columnSpec = GridLayout.spec(c);
                seat.setLayoutParams(params);

                // Apply background based on state
                applySeatBackground(seat, seatState[r][c]);

                // Click handling
                if (seatState[r][c] != STATE_BOOKED) {
                    final int row = r;
                    final int col = c;
                    seat.setOnClickListener(v -> toggleSeat(row, col));
                }

                grid.addView(seat);
                seatViews[r][c] = seat;
            }
        }
    }

    private void toggleSeat(int row, int col) {
        if (seatState[row][col] == STATE_AVAILABLE) {
            seatState[row][col] = STATE_SELECTED;
            selectedCount++;
        } else if (seatState[row][col] == STATE_SELECTED) {
            seatState[row][col] = STATE_AVAILABLE;
            selectedCount--;
        }
        applySeatBackground(seatViews[row][col], seatState[row][col]);
        updateSeatInfo();
    }

    private void applySeatBackground(View seat, int state) {
        switch (state) {
            case STATE_SELECTED:
                seat.setBackgroundResource(R.drawable.seat_selected);
                break;
            case STATE_BOOKED:
                seat.setBackgroundResource(R.drawable.seat_booked);
                break;
            default:
                seat.setBackgroundResource(R.drawable.seat_available);
                break;
        }
    }

    private void updateSeatInfo() {
        int total = selectedCount * PRICE_PER_SEAT;
        String info = getString(R.string.seats_selected_label, selectedCount)
                + "  •  " + getString(R.string.total_label, total);
        tvSeatInfo.setText(info);

        // Enable/disable Proceed to Snacks button
        btnProceedSnacks.setEnabled(selectedCount > 0);
        btnProceedSnacks.setAlpha(selectedCount > 0 ? 1.0f : 0.5f);

        if (selectedCount > 0) {
            btnProceedSnacks.setBackgroundResource(R.drawable.btn_filled_white_red_border);
        } else {
            btnProceedSnacks.setBackgroundResource(R.drawable.btn_outline_white);
        }
    }
}
