package com.northeastern.annotaterapp;

import static com.northeastern.annotaterapp.Constants.INTERVAL_TIME;

import android.content.Context;
import android.content.Intent;
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
import com.northeastern.annotaterapp.services.AskService;
import com.northeastern.annotaterapp.workers.ListenWorker;
import java.util.concurrent.TimeUnit;

/**
 * This is the default activity. It has a toggle and allows the user to turn the worker on or off.
 */
public class SettingsActivity extends AppCompatActivity {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private PeriodicWorkRequest work;
    private TableLayout dbDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TestActivity.requestRecordAudioPermission(this);
        work = new PeriodicWorkRequest.Builder(ListenWorker.class, INTERVAL_TIME, TimeUnit.MINUTES)
                       .addTag("asker")
                       .build();

        initComponents();

        initDatabase();
    }

    private void setupPeriodicWorker() { WorkManager.getInstance(this).enqueue(work); }

    private void teardownPeriodicWorker() {
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
                params.gravity = Gravity.LEFT;

                TextView timestamp = new TextView(this);
                timestamp.setLayoutParams(params);
                String timestampString = cursor.getString(cursor.getColumnIndex("TIMESTAMP"));
                timestamp.setText(timestampString);

                TextView activity = new TextView(this);
                activity.setLayoutParams(params);
                String activityString = cursor.getString(cursor.getColumnIndex("ACTIVITY"));
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
        enable.setOnCheckedChangeListener(new AskServiceCheckedListener(this));
        dbDisplay = findViewById(R.id.sqlDisplay);
    }

    private class AskServiceCheckedListener implements CompoundButton.OnCheckedChangeListener {
        private final Context ctx;

        public AskServiceCheckedListener(Context ctx) { this.ctx = ctx; }

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