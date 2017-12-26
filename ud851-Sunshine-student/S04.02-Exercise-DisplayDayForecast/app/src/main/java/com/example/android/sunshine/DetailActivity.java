package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    private String mForecast;
    private TextView weatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        weatherData = (TextView) findViewById(R.id.weather_data_displayed);

        // COMPLETED (2) Display the weather forecast that was passed from MainActivity
        Intent intentThatWasPassed = getIntent();
        if (intentThatWasPassed != null) {
            if (intentThatWasPassed.hasExtra(Intent.EXTRA_PACKAGE_NAME)) {
                mForecast = intentThatWasPassed.getStringExtra(Intent.EXTRA_PACKAGE_NAME);
                weatherData.setText(mForecast);
            }

        }
    }
}