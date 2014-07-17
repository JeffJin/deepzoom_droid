package com.intellibeacons.app;

import android.app.Activity;
import android.util.Log;

/**
 * Created by jeffjin on 2014-06-14.
 */
public class LogHelper {
    public final static String LOG_ACTIVITY_TAG = "ACTIVITY_EVENT";
    public final static String LOG_SERVICE_TAG = "SERVICE_EVENT";
    public final static String LOG_GENERAL_TAG = "GENERAL";

    public static void Log(String content){
        Log.d(LOG_GENERAL_TAG, content);
    }

    public static void LogActivity(Activity activity, String content){
        String logMsg =  String.format("Activity:%s | Callbabl:%s", activity.getClass().getSimpleName(), content);
        Log.d(LOG_ACTIVITY_TAG, logMsg);
    }
}
