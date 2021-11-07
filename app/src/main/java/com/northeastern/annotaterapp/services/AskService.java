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

package com.northeastern.annotaterapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.northeastern.annotaterapp.ICallback;
import com.northeastern.annotaterapp.R;
import com.northeastern.annotaterapp.SettingsActivity;
import com.northeastern.annotaterapp.tagger.IAskDefault;
import com.northeastern.annotaterapp.tagger.speech.IAskRecorderImpl;
import com.northeastern.annotaterapp.utils.LogTableUtils;
import com.northeastern.annotaterapp.validators.ActivityValidator;
import com.northeastern.annotaterapp.validators.IValidator;

/**
 * This service is what does all the hardwork. Uses {@link IAskRecorderImpl} internally and is
 * currently being called by {@link androidx.work.PeriodicWorkRequest} which is implemented by
 * {@link com.northeastern.annotaterapp.workers.ListenWorker}. The schedule for this is in {@link
 * com.northeastern.annotaterapp.SettingsActivity}. This starts recording waits for callback saves
 * to the database and then kills itself.
 */
public class AskService extends Service implements ICallback {
    private static final String LOG_TAG = AskService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 1335;
    private final IAskDefault mAskImpl = new IAskRecorderImpl(this, this);
    private final IValidator validator = new ActivityValidator();
    private int mMaxTry = 3;

    @Override
    public void handleCallback(StatusCode statusCode, String data) {
        if (statusCode == StatusCode.SUCCESS) {
            if (!validator.isValid(data)) {
                Log.d(LOG_TAG, "Invalid activity!");
                if (--mMaxTry > 0)
                    startSpeechEngine();
                else
                    stopSelf(); // 3 failed attempts.
            } else {
                mMaxTry = 3;
                Log.d(LOG_TAG, "Valid data [" + data + "] obtained");
                LogTableUtils.updateTable(this, data);
                Toast.makeText(this, "You said [" + data + "]. I'll ask you again in 15minutes!",
                             Toast.LENGTH_LONG)
                        .show();
                stopSelf();
            }
        } else if (statusCode == StatusCode.IN_PROGRESS) {
            Log.d(LOG_TAG, "listening...");
        } else if (data.equals("7") && statusCode == StatusCode.FAILURE && (--mMaxTry) > 0) {
            Log.d(LOG_TAG, "Error 7 (NO_INPUT) retrying!");
            startSpeechEngine();
        }
    }

    private void startSpeechEngine() {
        if (mMaxTry != 3) {
            Log.d(LOG_TAG, "Retrying " + (3 - mMaxTry) + " out of 3 times");
        }

        mAskImpl.getOneSentence();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(LOG_TAG, "Service started!");
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        settingsIntent.setAction(Intent.ACTION_MAIN);
        settingsIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, settingsIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                                                .setContentTitle("Activity recording has started!")
                                                .setContentText("detecting now..")
                                                .setContentIntent(pendingIntent)
                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                .setAutoCancel(true)
                                                .setDeleteIntent(pendingIntent)
                                                .build();

            startForeground(NOTIFICATION_ID, notification);
        } else {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setContentTitle("Activity recording has started!")
                            .setContentText("detecting now..")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true)
                            .setDeleteIntent(pendingIntent)
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setAutoCancel(true);

            Notification notification = builder.build();

            startForeground(NOTIFICATION_ID, notification);
        }
        startSpeechEngine();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");
        mAskImpl.stopListening();
        super.onDestroy();
    }
}
