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

package com.northeastern.annotaterapp;

import static com.northeastern.annotaterapp.Constants.INTERVAL_TIME;
import static com.northeastern.annotaterapp.utils.PermissionUtils.requestRecordAudioPermission;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.northeastern.annotaterapp.workers.ListenWorker;
import java.util.concurrent.TimeUnit;

/**
 * This is the default activity. It has a toggle and allows the user to turn the worker on or off.
 */
public class SettingsActivity extends AppCompatActivity {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private PeriodicWorkRequest work;
    private TableLayout dbDisplay;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);

        requestRecordAudioPermission(this);

        work = new PeriodicWorkRequest.Builder(ListenWorker.class, INTERVAL_TIME, TimeUnit.MINUTES)
                       .addTag("asker")
                       .build();

        initComponents();
        initDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalRows = dbDisplay.getChildCount();
        dbDisplay.removeViews(1, totalRows - 1);
        initDatabase();
    }

    private void setupPeriodicWorker() {
        mSharedPreferences.edit().putBoolean("worker_enabled", true).apply();
        WorkManager.getInstance(this).enqueue(work);
    }

    private void teardownPeriodicWorker() {
        mSharedPreferences.edit().putBoolean("worker_enabled", false).apply();
        WorkManager.getInstance(this).cancelAllWorkByTag("asker");
    }

    private void initDatabase() {
        SQLiteDatabase main = this.openOrCreateDatabase("main", MODE_PRIVATE, null);
        main.execSQL("CREATE TABLE IF NOT EXISTS log(TIMESTAMP VARCHAR, ACTIVITY VARCHAR)");
        Cursor cursor = main.rawQuery("SELECT * from log", null);
        populateTable(cursor);
        main.close();
    }

    private void populateTable(Cursor cursor) {
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                TableRow baseTableRow = new TableRow(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                params.weight = 1;
                params.gravity = Gravity.START;

                TextView timestamp = new TextView(this);
                timestamp.setLayoutParams(params);
                String timestampString =
                        cursor.getString(cursor.getColumnIndexOrThrow("TIMESTAMP"));
                timestamp.setText(timestampString);

                TextView activity = new TextView(this);
                activity.setLayoutParams(params);
                String activityString = cursor.getString(cursor.getColumnIndexOrThrow("ACTIVITY"));
                activity.setText(activityString);

                baseTableRow.addView(timestamp);
                baseTableRow.addView(activity);

                dbDisplay.addView(baseTableRow);
                Log.d(LOG_TAG, "View added to table: " + timestampString + "\t" + activityString);
                cursor.moveToNext();
            }
        }
    }

    private void initComponents() {
        SwitchCompat enable = findViewById(R.id.enable);
        enable.setOnCheckedChangeListener(new AskServiceCheckedListener());
        if (mSharedPreferences.getBoolean("worker_enabled", false))
            enable.setChecked(true);
        dbDisplay = findViewById(R.id.sqlDisplay);
    }

    private class AskServiceCheckedListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(LOG_TAG, "Switch is " + (isChecked ? "enabled" : "disabled"));

            if (isChecked) {
                setupPeriodicWorker();
            } else {
                teardownPeriodicWorker();
            }
        }
    }
}
