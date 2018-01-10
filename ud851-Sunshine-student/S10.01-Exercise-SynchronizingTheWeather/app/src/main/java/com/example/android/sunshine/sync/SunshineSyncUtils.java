package com.example.android.sunshine.sync;


import android.content.Context;
import android.content.Intent;

// COMPLETED (9) Create a class called SunshineSyncUtils
public class SunshineSyncUtils {

    //  COMPLETED (10) Create a public static void method called startImmediateSync
    public static void startImmediateSync(final Context context) {
    //  COMPLETED (11) Within that method, start the SunshineSyncIntentService
        Intent intentService = new Intent(context, SunshineSyncIntentService.class);
        context.startService(intentService);
    }

}