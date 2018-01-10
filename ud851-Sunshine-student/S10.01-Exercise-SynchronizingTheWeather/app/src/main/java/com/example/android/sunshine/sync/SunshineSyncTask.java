package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class SunshineSyncTask {
    //  COMPLETED (1) Create a class called SunshineSyncTask
//  COMPLETED (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
    synchronized public static void syncWeather(Context context) {
    //      COMPLETED (3) Within syncWeather, fetch new weather data
        // !!! Don't understand this !!!
        try {
            /*
            The getUrl method will return the URL that we need to get the forecast JSON for the
            weather. It will decide whether to create a URL based off of the latitude and
            longitude or off of a simple location as a String
             */
            URL weatherRequestUrl = NetworkUtils.getUrl(context);

            /*
            Use the URL to retrieve the JSON
             */
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

            /*
            Parse the JSON into a list of weather values
             */
            ContentValues[] weatherValues = OpenWeatherJsonUtils
                    .getWeatherContentValuesFromJson(context, jsonWeatherResponse);
            /*
            In case where or JSON contained an error code, getWeatherContentValuesFromJson
            would have returned null. We ned to check for those cases her to prevent any
            NullPointerExceptions being thrown. We also have no reason to insert fresh data if
            there isn't any to insert
             */
            if (weatherValues != null && weatherValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver sunshineContentResolver = context.getContentResolver();
                // COMPLETED (4) If we have valid results, delete the old data and insert the new
                /*
                Delete old weather data because we don't need to keep multiple days' data
                 */
                sunshineContentResolver.delete(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        null,
                        null);

                /*
                Insert our new weather data into Sunshine's ContentProvider
                 */
                sunshineContentResolver.bulkInsert(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        weatherValues);
            }
        } catch (Exception e) {
            /*
            Server probably invalid
             */
            e.printStackTrace();
        }
    }

}