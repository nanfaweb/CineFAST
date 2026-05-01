package com.example.a1_smd;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MovieJsonParser {

    public static List<Movie> loadMovies(Context context, boolean fetchNowShowing) {
        List<Movie> movieList = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("movies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String jsonString = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                boolean isNowShowing = obj.getBoolean("isNowShowing");
                
                // Filter based on the requested category
                if (isNowShowing == fetchNowShowing) {
                    String name = obj.getString("name");
                    String genre = obj.getString("genre");
                    int duration = obj.getInt("duration");
                    String trailerUrl = obj.getString("trailerUrl");
                    String drawableName = obj.getString("posterDrawable");

                    // Resolve drawable name to resource ID
                    int posterResId = context.getResources().getIdentifier(
                            drawableName, "drawable", context.getPackageName());

                    // Fallback to a default poster if not found
                    if (posterResId == 0) {
                        posterResId = R.drawable.movie_poster_1;
                    }

                    Movie movie = new Movie(name, genre, duration, posterResId, trailerUrl, isNowShowing);
                    movie.setPosterDrawable(drawableName);
                    movieList.add(movie);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieList;
    }
}
