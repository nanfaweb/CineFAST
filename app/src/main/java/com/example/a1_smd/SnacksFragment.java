package com.example.a1_smd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class SnacksFragment extends Fragment {

    private String movieName;
    private int posterResId;
    private int selectedSeatCount;
    private int ticketPrice;
    private ArrayList<String> selectedSeats;

    private List<Snack> snackList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snacks, container, false);

        // Read arguments passed from MainActivity
        if (getArguments() != null) {
            movieName = getArguments().getString("movieName", "");
            posterResId = getArguments().getInt("posterResId", R.drawable.movie_poster_1);
            selectedSeatCount = getArguments().getInt("selectedSeatCount", 0);
            ticketPrice = getArguments().getInt("totalPrice", 0);
            selectedSeats = getArguments().getStringArrayList("selectedSeats");
        }

        // Populate snack data using Snack model class
        snackList.add(new Snack("Popcorn", "Large / Buttered", 250, R.drawable.popcorn_img));
        snackList.add(new Snack("Nachos", "With Cheese Dip", 270, R.drawable.nachos_img));
        snackList.add(new Snack("Soft Drink", "Large / Any Flavor", 100, R.drawable.softdrink_img));
        snackList.add(new Snack("Candy Mix", "Assorted Candies", 120, R.drawable.candymix_img));

        // Set up custom ListView with SnackAdapter
        ListView lvSnacks = view.findViewById(R.id.lvSnacks);
        SnackAdapter adapter = new SnackAdapter(requireContext(), snackList);
        lvSnacks.setAdapter(adapter);

        // Back button
        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // Confirm button — gather quantities and navigate to TicketSummaryFragment
        Button btnConfirm = view.findViewById(R.id.btnConfirmSnacks);
        btnConfirm.setOnClickListener(v -> {

            double snacksTotal = 0;
            ArrayList<String> snackDetails = new ArrayList<>();

            for (Snack snack : snackList) {
                if (snack.getQuantity() > 0) {
                    snacksTotal += snack.getTotalPrice();
                    snackDetails.add(snack.getQuantity() + "x " + snack.getName()
                            + " (PKR " + snack.getTotalPrice() + ")");
                }
            }

            String theaterName = getString(R.string.theater_name);
            String hallNumber = getString(R.string.hall_name);
            String date = getString(R.string.show_date);
            String time = getString(R.string.show_time);

            Bundle bookingData = new Bundle();
            bookingData.putString("movieName", movieName);
            bookingData.putInt("posterResId", posterResId);
            bookingData.putInt("selectedSeatCount", selectedSeatCount);
            bookingData.putInt("ticketPrice", ticketPrice);
            bookingData.putDouble("snacksTotal", snacksTotal);
            bookingData.putStringArrayList("snacksList", snackDetails);
            bookingData.putStringArrayList("selectedSeats", selectedSeats);
            bookingData.putString("theaterName", theaterName);
            bookingData.putString("hallNumber", hallNumber);
            bookingData.putString("bookingDate", date);
            bookingData.putString("bookingTime", time);

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showTicketSummaryFragment(bookingData);
            }
        });

        return view;
    }
}
