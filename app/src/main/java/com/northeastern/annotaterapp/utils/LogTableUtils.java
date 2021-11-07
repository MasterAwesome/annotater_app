/*
 * Copyright (C) 2020 Arvind Mukund <armu30@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.northeastern.annotaterapp.utils;

import static com.northeastern.annotaterapp.Constants.DATE_FORMAT;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

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
        String timestamp;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US))
                                .format(Calendar.getInstance().getTime());
        } else {
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            timestamp = date.format(formatter);
        }
        ContentValues cv = new ContentValues();
        cv.put("TIMESTAMP", timestamp);
        cv.put("ACTIVITY", activity.trim());
        db.insert("log", null, cv);

        db.close();
    }
}
