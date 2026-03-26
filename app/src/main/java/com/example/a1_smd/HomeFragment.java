package com.example.a1_smd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ImageButton btnMenu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        btnMenu = view.findViewById(R.id.btnMenu);

        HomePagerAdapter adapter = new HomePagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText(getString(R.string.now_showing_text));
                    } else {
                        tab.setText(getString(R.string.comingSoonText));
                    }
                }
        ).attach();

        btnMenu.setOnClickListener(v -> showPopupMenu(v));

        return view;
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(requireContext(), view);
        popup.getMenu().add(0, 1, 0, getString(R.string.viewLastBooking));
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 1) {
                showLastBooking();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void showLastBooking() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("BookingPrefs", Context.MODE_PRIVATE);
        String movieName = prefs.getString("movieName", null);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.lastBookingTitle));
        
        if (movieName != null) {
            int seats = prefs.getInt("seatCount", 0);
            int total = prefs.getInt("totalPrice", 0);
            
            String message = "Movie: " + movieName + "\n" +
                             "Seats: " + seats + "\n" +
                             "Total Price: " + total + " PKR";
                             
            builder.setMessage(message);
        } else {
            builder.setMessage(getString(R.string.noPreviousBooking));
        }
        
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
