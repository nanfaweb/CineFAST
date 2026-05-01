package com.example.a1_smd;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyBookingsFragment extends Fragment implements BookingAdapter.OnCancelClickListener {

    private RecyclerView rvMyBookings;
    private BookingAdapter adapter;
    private List<Booking> bookingList;
    private DatabaseReference mDatabase;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bookings, container, false);

        rvMyBookings = view.findViewById(R.id.rvMyBookings);
        rvMyBookings.setLayoutManager(new LinearLayoutManager(getContext()));

        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(getContext(), bookingList, this);
        rvMyBookings.setAdapter(adapter);

        ImageButton btnMenu = view.findViewById(R.id.btnMenuBookings);
        btnMenu.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference("bookings").child(userId);
            loadBookings();
        } else {
            Toast.makeText(getContext(), "Please log in to view bookings", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void loadBookings() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Booking booking = dataSnapshot.getValue(Booking.class);
                    if (booking != null) {
                        booking.setId(dataSnapshot.getKey());
                        bookingList.add(booking);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load bookings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCancelClick(Booking booking) {
        new AlertDialog.Builder(getContext())
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes", (dialog, which) -> validateAndCancelBooking(booking))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void validateAndCancelBooking(Booking booking) {
        String dateTimeStr = booking.getDateTime(); // Expected format: "15.02.2026 21:00"
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

        try {
            Date bookingDate = sdf.parse(dateTimeStr);
            Date currentDate = new Date();

            if (bookingDate != null && bookingDate.before(currentDate)) {
                Toast.makeText(getContext(), "Cannot cancel past bookings", Toast.LENGTH_SHORT).show();
            } else {
                // Future booking, allow cancellation
                mDatabase.child(booking.getId()).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Booking Cancelled Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to cancel booking", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error parsing date", Toast.LENGTH_SHORT).show();
        }
    }
}
