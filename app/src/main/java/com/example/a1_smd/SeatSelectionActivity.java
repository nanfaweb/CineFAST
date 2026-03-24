package com.example.a1_smd;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class SeatSelectionActivity extends AppCompatActivity {

    private static final int ROWS = 8;
    private static final int COLS = 10; // 4 left + 2 gap + 4 right
    private static final int GAP_COL_START = 4;
    private static final int GAP_COL_END = 5;
    private static final int PRICE_PER_SEAT = 1000;

    private static final int STATE_AVAILABLE = 0;
    private static final int STATE_SELECTED = 1;
    private static final int STATE_BOOKED = 2;
    private static final int STATE_GAP = -1;
    private static final int STATE_HIDDEN = -2; // For corner seats

    private int[][] seatState = new int[ROWS][COLS];
    private View[][] seatViews = new View[ROWS][COLS];
    private int selectedCount = 0;
    private ArrayList<String> selectedSeatNames = new ArrayList<>();

    private TextView tvSeatInfo;
    private Button btnProceedSnacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        String movieName = getIntent().getStringExtra("MOVIE_NAME");
        int posterResourceId = getIntent().getIntExtra("POSTER_RESOURCE_ID", R.drawable.movie_poster_1);
        String theaterName = getString(R.string.theater_name);
        String hallNumber = getString(R.string.hall_name);
        String date = getString(R.string.show_date);
        String time = getString(R.string.show_time);
        TextView tvMovieTitle = findViewById(R.id.tvMovieTitle);
        if (movieName != null) {
            tvMovieTitle.setText(movieName);
        }

        tvSeatInfo = findViewById(R.id.tvSeatInfo);
        btnProceedSnacks = findViewById(R.id.btnProceedSnacks);
        Button btnBookSeats = findViewById(R.id.btnBookSeats);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        initSeatStates();

        GridLayout seatGrid = findViewById(R.id.seatGrid);
        seatGrid.setColumnCount(COLS);
        seatGrid.setRowCount(ROWS);
        buildSeatGrid(seatGrid);
        updateSeatInfo();

        btnBookSeats.setOnClickListener(v -> {
            if (selectedCount == 0) {
                Toast.makeText(this, R.string.select_at_least_one_seat, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(SeatSelectionActivity.this, BookingDetailsActivity.class);
                intent.putExtra("MOVIE_NAME", tvMovieTitle.getText().toString());
                intent.putExtra("POSTER_RESOURCE_ID", posterResourceId);
                intent.putExtra("SELECTED_SEAT_COUNT", selectedCount);
                intent.putExtra("TICKET_PRICE", selectedCount * PRICE_PER_SEAT);
                intent.putExtra("SNACKS_TOTAL", 0.0);
                intent.putStringArrayListExtra("SNACKS_LIST", new ArrayList<>());
                intent.putStringArrayListExtra("SELECTED_SEATS", selectedSeatNames);

                intent.putExtra("THEATER_NAME", theaterName);
                intent.putExtra("HALL_NUMBER", hallNumber);
                intent.putExtra("BOOKING_DATE", date);
                intent.putExtra("BOOKING_TIME", time);
                startActivity(intent);
            }
        });

        btnProceedSnacks.setOnClickListener(v -> {
            Intent intent = new Intent(SeatSelectionActivity.this, SnacksActivity.class);
            intent.putExtra("MOVIE_NAME", tvMovieTitle.getText().toString());
            intent.putExtra("POSTER_RESOURCE_ID", posterResourceId); // Forward poster resource ID
            intent.putExtra("SELECTED_SEAT_COUNT", selectedCount);
            intent.putExtra("TOTAL_PRICE", selectedCount * PRICE_PER_SEAT);
            intent.putStringArrayListExtra("SELECTED_SEATS", selectedSeatNames);
            startActivity(intent);
        });
    }

    private void initSeatStates() {

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (c >= GAP_COL_START && c <= GAP_COL_END) {
                    seatState[r][c] = STATE_GAP;
                } else if ((r == 0 || r == ROWS - 1) && (c == 0 || c == COLS - 1)) {
                    seatState[r][c] = STATE_HIDDEN;
                } else {
                    seatState[r][c] = STATE_AVAILABLE;
                }
            }
        }

        bookSeat(1, 0);
        bookSeat(1, 8);
        bookSeat(1, 9);

        bookSeat(4, 1);
        bookSeat(4, 2);

        bookSeat(5, 1);
        bookSeat(5, 2);

        bookSeat(6, 7);
        bookSeat(6, 8);
        bookSeat(6, 9);
    }

    private void bookSeat(int row, int col) {
        if (seatState[row][col] != STATE_GAP && seatState[row][col] != STATE_HIDDEN) {
            seatState[row][col] = STATE_BOOKED;
        }
    }

    private void buildSeatGrid(GridLayout grid) {
        int seatSize = (int) (32 * getResources().getDisplayMetrics().density);
        int seatMargin = (int) (3 * getResources().getDisplayMetrics().density);
        int gapWidth = (int) (8 * getResources().getDisplayMetrics().density);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (seatState[r][c] == STATE_GAP) {
                    View spacer = new View(this);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = gapWidth;
                    params.height = seatSize;
                    params.rowSpec = GridLayout.spec(r);
                    params.columnSpec = GridLayout.spec(c);
                    spacer.setLayoutParams(params);
                    grid.addView(spacer);
                    seatViews[r][c] = spacer;
                    continue;
                }

                if (seatState[r][c] == STATE_HIDDEN) {
                    View hidden = new View(this);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = seatSize;
                    params.height = seatSize;
                    params.setMargins(seatMargin, seatMargin, seatMargin, seatMargin);
                    params.rowSpec = GridLayout.spec(r);
                    params.columnSpec = GridLayout.spec(c);
                    hidden.setVisibility(View.INVISIBLE);
                    hidden.setLayoutParams(params);
                    grid.addView(hidden);
                    seatViews[r][c] = hidden;
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

                applySeatBackground(seat, seatState[r][c]);

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
        String seatName = getSeatName(row, col);

        if (seatState[row][col] == STATE_AVAILABLE) {
            seatState[row][col] = STATE_SELECTED;
            selectedCount++;
            selectedSeatNames.add(seatName);
        } else if (seatState[row][col] == STATE_SELECTED) {
            seatState[row][col] = STATE_AVAILABLE;
            selectedCount--;
            selectedSeatNames.remove(seatName);
        }
        applySeatBackground(seatViews[row][col], seatState[row][col]);
        updateSeatInfo();
    }

    private String getSeatName(int row, int col) {
        char rowChar = (char) ('A' + row);
        int seatNum;

        if (col < GAP_COL_START) {
            seatNum = col + 1;
        } else {
            seatNum = col - 1;
        }

        return "Row " + rowChar + ", Seat " + seatNum;
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

        btnProceedSnacks.setEnabled(selectedCount > 0);
        btnProceedSnacks.setAlpha(selectedCount > 0 ? 1.0f : 0.5f);

        if (selectedCount > 0) {
            btnProceedSnacks.setBackgroundResource(R.drawable.btn_filled_white_red_border);
        } else {
            btnProceedSnacks.setBackgroundResource(R.drawable.btn_outline_white);
        }
    }
}