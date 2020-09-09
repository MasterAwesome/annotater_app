package com.northeastern.annotaterapp.utils;

import static com.northeastern.annotaterapp.Constants.DATE_FORMAT;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utils class that helps with database operations specifically the Log table.
 */
public final class LogTableUtils {
    private static final String LOG_TAG = LogTableUtils.class.getSimpleName();

    /**
     * Updates the log table with the given activity.
     *
     * @param context  context required for opening database.
     * @param activity the name of the activity to save.
     */
    public static void updateTable(Context context, String activity) {
        Log.d(LOG_TAG, "Pushing into the log table!");
        SQLiteDatabase db = context.openOrCreateDatabase("main", Context.MODE_PRIVATE, null);

        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String text = date.format(formatter);

        ContentValues cv = new ContentValues();
        cv.put("TIMESTAMP", text);
        cv.put("ACTIVITY", activity);
        db.insert("log", null, cv);

        db.close();
    }
}
