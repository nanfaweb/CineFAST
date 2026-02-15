package com.example.a1_smd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        RadioGroup dateRadioGroup = findViewById(R.id.dateRadioGroup);

        LinearLayout movie1 = findViewById(R.id.cardMovie1);
        LinearLayout movie2 = findViewById(R.id.cardMovie2);
        LinearLayout movie3 = findViewById(R.id.cardMovie3);
        LinearLayout movie4 = findViewById(R.id.cardMovie4);
        LinearLayout movie5 = findViewById(R.id.cardMovie5);

        dateRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            movie1.setVisibility(View.VISIBLE);
            movie2.setVisibility(View.VISIBLE);
            movie3.setVisibility(View.VISIBLE);
            movie4.setVisibility(View.VISIBLE);
            movie5.setVisibility(View.VISIBLE);

            if (checkedId == R.id.radioTomorrow) {
                movie2.setVisibility(View.GONE);
                movie4.setVisibility(View.GONE);
            }
        });

        int selectedId = dateRadioGroup.getCheckedRadioButtonId();
        dateRadioGroup.check(selectedId);

        setTrailer(R.id.btnTrailerGodfather,
                "https://youtu.be/UaVTIH8mujA");

        setTrailer(R.id.btnTrailerGodfatherII,
                "https://youtu.be/9O1Iy9od7-A");

        setTrailer(R.id.btnTrailerGodfatherIII,
                "https://youtu.be/UUkG37KSWf0");

        setTrailer(R.id.btnTrailerUsualSuspects,
                "https://youtu.be/Q0eCiCinc4E");

        setTrailer(R.id.btnTrailerDemonSlayer,
                "https://youtu.be/x7uLutVRBfI");
    }

    private void setTrailer(int buttonId, String url) {
        Button button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            });
        }
    }

}