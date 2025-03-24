package com.example.a1_smd;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            showHomeFragment();
        }
    }

    public void showHomeFragment() {
        replaceFragment(new HomeFragment(), false);
    }

    public void showSeatSelectionFragment(Movie movie) {
        Bundle args = new Bundle();
        args.putString("movieName", movie.getName());
        args.putInt("posterResId", movie.getPosterResId());
        args.putString("trailerUrl", movie.getTrailerUrl());
        args.putBoolean("isNowShowing", movie.isNowShowing());

        SeatSelectionFragment fragment = new SeatSelectionFragment();
        fragment.setArguments(args);
        replaceFragment(fragment, true);
    }

    public void showSnacksFragment(String movieName, int posterResId, int seatCount,
                                    int ticketPrice, ArrayList<String> selectedSeats) {
        Bundle args = new Bundle();
        args.putString("movieName", movieName);
        args.putInt("posterResId", posterResId);
        args.putInt("selectedSeatCount", seatCount);
        args.putInt("totalPrice", ticketPrice);
        args.putStringArrayList("selectedSeats", selectedSeats);

        SnacksFragment fragment = new SnacksFragment();
        fragment.setArguments(args);
        replaceFragment(fragment, true);
    }

    public void showTicketSummaryFragment(Bundle bookingData) {
        TicketSummaryFragment fragment = new TicketSummaryFragment();
        fragment.setArguments(bookingData);
        replaceFragment(fragment, true);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
