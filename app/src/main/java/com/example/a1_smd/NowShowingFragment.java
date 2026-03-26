package com.example.a1_smd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NowShowingFragment extends Fragment implements MovieAdapter.OnMovieClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewMovies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(
                getString(R.string.movie_1_title),
                "Crime", 175,
                R.drawable.movie_poster_1,
                "https://youtu.be/UaVTIH8mujA",
                true
        ));
        movies.add(new Movie(
                getString(R.string.movie_2_title),
                "Crime", 202,
                R.drawable.movie_poster_2,
                "https://youtu.be/9O1Iy9od7-A",
                true
        ));
        movies.add(new Movie(
                getString(R.string.movie_4_title),
                "Thriller", 106,
                R.drawable.movie_poster_4,
                "https://youtu.be/Q0eCiCinc4E",
                true
        ));

        MovieAdapter adapter = new MovieAdapter(getContext(), movies, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onBookClick(Movie movie) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showSeatSelectionFragment(movie);
        }
    }
}
