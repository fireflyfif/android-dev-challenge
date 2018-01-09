/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
//      COMPLETED (21) Implement LoaderManager.LoaderCallbacks<Cursor>

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    //  COMPLETED (18) Create a String array containing the names of the desired data columns from our ContentProvider
    public static final String[] DETAIL_FORECAST_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };
    //  COMPLETED (19) Create constant int values representing each column name's position above
    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_HUMIDITY = 3;
    public static final int INDEX_WEATHER_PRESSURE = 4;
    public static final int INDEX_WEATHER_WIND = 5;
    public static final int INDEX_WEATHER_DEGREES = 6;
    public static final int INDEX_WEATHER_CONDITION_ID = 7;

    //  COMPLETED (20) Create a constant int to identify our loader used in DetailActivity
    private static final int ID_DETAIL_LOADER = 2;

    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

    //  COMPLETED (15) Declare a private Uri field called mUri
    private Uri mUri;

//  COMPLETED (10) Remove the mWeatherDisplay TextView declaration

    //  COMPLETED (11) Declare TextViews for the date, description, high, low, humidity, wind, and pressure
    private TextView mDate, mDescription, mHighTemp, mLowTemp, mHumidity, mWind, mPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//      COMPLETED (12) Remove mWeatherDisplay TextView

//      COMPLETED (13) Find each of the TextViews by ID
        mDate = (TextView) findViewById(R.id.date);
        mDescription = (TextView) findViewById(R.id.weather_description);
        mHighTemp = (TextView) findViewById(R.id.high_temp);
        mLowTemp = (TextView) findViewById(R.id.low_temp);
        mHumidity = (TextView) findViewById(R.id.humidity);
        mWind = (TextView) findViewById(R.id.wind);
        mPressure = (TextView) findViewById(R.id.pressure);

//      COMPLETED (14) Remove the code that checks for extra text
        //      COMPLETED (16) Use getData to get a reference to the URI passed with this Activity's Intent
        mUri = getIntent().getData();

//      COMPLETED (17) Throw a NullPointerException if that URI is null
        if (mUri == null) {
            throw new NullPointerException("URI is null " + mUri);
        }

//      COMPLETED (35) Initialize the loader for DetailActivity
        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

    //  COMPLETED (22) Override onCreateLoader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // COMPLETED (23) If the loader requested is our detail loader, return the appropriate CursorLoader
        switch (id) {
            case ID_DETAIL_LOADER:

                return new CursorLoader(this,
                        mUri,
                        DETAIL_FORECAST_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    //  COMPLETED (24) Override onLoadFinished
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Why is this needed?
        boolean cursorHasValidDate = false;

        if (data != null && data.moveToFirst()) {

            cursorHasValidDate = true;
        }

        //      COMPLETED (25) Check before doing anything that the Cursor has valid data
        if (!cursorHasValidDate) {
            return;
        }

        //      COMPLETED (26) Display a readable data string
        long dateIndex = data.getLong(INDEX_WEATHER_DATE);
        String dateString = SunshineDateUtils.getFriendlyDateString(this, dateIndex, true);
        mDate.setText(dateString);

        //      COMPLETED (27) Display the weather description (using SunshineWeatherUtils)
        int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
        String weatherDescription = SunshineWeatherUtils.getStringForWeatherCondition(this, weatherId);
        mDescription.setText(weatherDescription);

        //      COMPLETED (28) Display the high temperature
        double highTemp = data.getDouble(INDEX_WEATHER_MAX_TEMP);
        String highTempString = SunshineWeatherUtils.formatTemperature(this, highTemp);
        mHighTemp.setText(highTempString);

        //      COMPLETED (29) Display the low temperature
        double lowTemp = data.getDouble(INDEX_WEATHER_MIN_TEMP);
        String lowTempString = SunshineWeatherUtils.formatTemperature(this, lowTemp);
        mLowTemp.setText(lowTempString);

        //      COMPLETED (30) Display the humidity
        float humidityIndex = data.getFloat(INDEX_WEATHER_HUMIDITY);
        String humidityString = getString(R.string.format_humidity, humidityIndex);
        mHumidity.setText(humidityString);

        //      COMPLETED (31) Display the wind speed and direction
        float windSpeed = data.getFloat(INDEX_WEATHER_WIND);
        float windDirection = data.getFloat(INDEX_WEATHER_DEGREES);
        String windSpeedDirectionString = SunshineWeatherUtils.getFormattedWind(this,
                windSpeed, windDirection);
        mWind.setText(windSpeedDirectionString);

        //      COMPLETED (32) Display the pressure
        float pressureIndex = data.getFloat(INDEX_WEATHER_PRESSURE);
        String pressureString = getString(R.string.format_pressure, pressureIndex);
        mPressure.setText(pressureString);

        //      COMPLETED (33) Store a forecast summary in mForecastSummary
        mForecastSummary = String.format("%s - %s - %s%s", dateString, weatherDescription,
                highTempString, lowTempString);

    }

    //  COMPLETED (34) Override onLoaderReset, but don't do anything in it yet
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}