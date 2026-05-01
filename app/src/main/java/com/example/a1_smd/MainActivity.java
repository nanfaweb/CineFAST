package com.example.a1_smd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private SessionManager sessionManager;
    private List<Snack> selectedSnacksCache = null;

    public List<Snack> getSelectedSnacksCache() {
        return selectedSnacksCache;
    }

    public void setSelectedSnacksCache(List<Snack> snacks) {
        this.selectedSnacksCache = snacks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        Toast.makeText(this, "CineFAST", Toast.LENGTH_SHORT).show();

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navView = findViewById(R.id.navView);

        View headerView = navView.getHeaderView(0);
        TextView tvNavUserName = headerView.findViewById(R.id.tvNavUserName);
        TextView tvNavUserEmail = headerView.findViewById(R.id.tvNavUserEmail);

        if (sessionManager.isLoggedIn()) {
            tvNavUserName.setText(sessionManager.getUserName());
            tvNavUserEmail.setText(sessionManager.getUserEmail());
        }

        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                showHomeFragment();
            } else if (id == R.id.nav_my_bookings) {
                showMyBookingsFragment();
            } else if (id == R.id.nav_logout) {
                sessionManager.logout();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        if (savedInstanceState == null) {
            showHomeFragment();
            navView.setCheckedItem(R.id.nav_home);
        }
    }

    public void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void showMyBookingsFragment() {
        replaceFragment(new MyBookingsFragment(), true);
    }

    public void showHomeFragment() {
        replaceFragment(new HomeFragment(), false);
    }

    public void showSeatSelectionFragment(Movie movie) {
        setSelectedSnacksCache(null);
        Bundle args = new Bundle();
        args.putString("movieName", movie.getName());
        args.putInt("posterResId", movie.getPosterResId());
        args.putString("posterDrawable", movie.getPosterDrawable());
        args.putString("trailerUrl", movie.getTrailerUrl());
        args.putBoolean("isNowShowing", movie.isNowShowing());

        SeatSelectionFragment fragment = new SeatSelectionFragment();
        fragment.setArguments(args);
        replaceFragment(fragment, true);
    }

    public void showSnacksFragment(String movieName, int posterResId, String posterDrawable, int seatCount,
                                    int ticketPrice, ArrayList<String> selectedSeats) {
        Bundle args = new Bundle();
        args.putString("movieName", movieName);
        args.putInt("posterResId", posterResId);
        args.putString("posterDrawable", posterDrawable);
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
