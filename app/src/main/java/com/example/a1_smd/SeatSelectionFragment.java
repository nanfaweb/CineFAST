package com.example.a1_smd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SeatSelectionFragment extends Fragment {

    private static final int ROWS = 8;
    private static final int COLS = 10;
    private static final int GAP_COL_START = 4;
    private static final int GAP_COL_END = 5;
    private static final int PRICE_PER_SEAT = 1000;

    private static final int STATE_AVAILABLE = 0;
    private static final int STATE_SELECTED = 1;
    private static final int STATE_BOOKED = 2;
    private static final int STATE_GAP = -1;
    private static final int STATE_HIDDEN = -2;

    private int[][] seatState = new int[ROWS][COLS];
    private boolean isInitialized = false;
    private View[][] seatViews = new View[ROWS][COLS];
    private int selectedCount = 0;
    private ArrayList<String> selectedSeatNames = new ArrayList<>();

    private TextView tvSeatInfo;
    private Button btnProceedSnacks;

    private boolean isNowShowing = true;
    private String movieName = "";
    private int posterResId = R.drawable.movie_poster_1;
    private String trailerUrl = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seat_selection, container, false);

        // Read arguments
        if (getArguments() != null) {
            movieName = getArguments().getString("movieName", "");
            posterResId = getArguments().getInt("posterResId", R.drawable.movie_poster_1);
            trailerUrl = getArguments().getString("trailerUrl", "");
            isNowShowing = getArguments().getBoolean("isNowShowing", true);
        }

        // Header
        TextView tvMovieTitle = view.findViewById(R.id.tvMovieTitle);
        tvMovieTitle.setText(movieName);

        // Back button
        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // Seat info and buttons
        tvSeatInfo = view.findViewById(R.id.tvSeatInfo);
        btnProceedSnacks = view.findViewById(R.id.btnProceedSnacks);

        LinearLayout nowShowingButtons = view.findViewById(R.id.nowShowingButtons);
        LinearLayout comingSoonButtons = view.findViewById(R.id.comingSoonButtons);

        // Initialize seat states and build grid
        if (!isInitialized) {
            initSeatStates();
            isInitialized = true;
        }
        GridLayout seatGrid = view.findViewById(R.id.seatGrid);
        seatGrid.setColumnCount(COLS);
        seatGrid.setRowCount(ROWS);
        buildSeatGrid(seatGrid);

        if (isNowShowing) {
            // --- Case 1: Now Showing ---
            nowShowingButtons.setVisibility(View.VISIBLE);
            comingSoonButtons.setVisibility(View.GONE);
            updateSeatInfo();

            Button btnBookSeats = view.findViewById(R.id.btnBookSeats);
            btnBookSeats.setOnClickListener(v -> {
                if (selectedCount == 0) {
                    Toast.makeText(getContext(), R.string.select_at_least_one_seat,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.booking_confirmed,
                            Toast.LENGTH_SHORT).show();

                    Bundle bookingData = createBookingBundle(false);
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).showTicketSummaryFragment(bookingData);
                    }
                }
            });

            btnProceedSnacks.setOnClickListener(v -> {
                String posterDrawable = getArguments() != null ? getArguments().getString("posterDrawable", "") : "";
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).showSnacksFragment(
                            movieName, posterResId, posterDrawable, selectedCount,
                            selectedCount * PRICE_PER_SEAT, selectedSeatNames);
                }
            });

        } else {
            // --- Case 2: Coming Soon ---
            nowShowingButtons.setVisibility(View.GONE);
            comingSoonButtons.setVisibility(View.VISIBLE);
            tvSeatInfo.setText(R.string.comingSoonText);

            // Seats are visible but NOT clickable (no click listeners attached)

            Button btnWatchTrailer = view.findViewById(R.id.btnWatchTrailer);
            btnWatchTrailer.setOnClickListener(v -> {
                if (trailerUrl != null && !trailerUrl.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                    startActivity(intent);
                }
            });
        }

        return view;
    }

    private Bundle createBookingBundle(boolean hasSnacks) {
        String theaterName = getString(R.string.theater_name);
        String hallNumber = getString(R.string.hall_name);
        String date = getString(R.string.show_date);
        String time = getString(R.string.show_time);

        Bundle data = new Bundle();
        data.putString("movieName", movieName);
        data.putInt("posterResId", posterResId);
        data.putInt("selectedSeatCount", selectedCount);
        data.putInt("ticketPrice", selectedCount * PRICE_PER_SEAT);
        data.putDouble("snacksTotal", 0.0);
        data.putStringArrayList("snacksList", new ArrayList<>());
        data.putStringArrayList("selectedSeats", selectedSeatNames);
        data.putString("theaterName", theaterName);
        data.putString("hallNumber", hallNumber);
        data.putString("bookingDate", date);
        data.putString("bookingTime", time);
        return data;
    }

    // ---- Seat logic (same as original SeatSelectionActivity) ----

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
        float density = getResources().getDisplayMetrics().density;
        int seatSize = (int) (32 * density);
        int seatMargin = (int) (3 * density);
        int gapWidth = (int) (8 * density);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (seatState[r][c] == STATE_GAP) {
                    View spacer = new View(requireContext());
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
                    View hidden = new View(requireContext());
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

                View seat = new View(requireContext());
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = seatSize;
                params.height = seatSize;
                params.setMargins(seatMargin, seatMargin, seatMargin, seatMargin);
                params.rowSpec = GridLayout.spec(r);
                params.columnSpec = GridLayout.spec(c);
                seat.setLayoutParams(params);

                applySeatBackground(seat, seatState[r][c]);

                // Only add click listeners for Now Showing movies on available seats
                if (isNowShowing && seatState[r][c] != STATE_BOOKED) {
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
