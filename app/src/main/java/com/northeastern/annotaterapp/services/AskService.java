package com.northeastern.annotaterapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.northeastern.annotaterapp.ICallback;
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
    private final IAskDefault mAskImpl = new IAskRecorderImpl(this, this);
    private final IValidator validator = new ActivityValidator();
    private int mMaxTry = 3;

    @Override
    public void handleCallback(StatusCode statusCode, String data) {
        if (statusCode == StatusCode.SUCCESS) {
            Log.d(LOG_TAG, data);
            if (!validator.isValid(data)) {
                if (--mMaxTry > 0)
                    startSpeechEngine();
                else
                    stopSelf(); // 3 failed attempts.
            } else {
                mMaxTry = 3;
                LogTableUtils.updateTable(this, data);
                stopSelf();
            }
        } else if (statusCode == StatusCode.IN_PROGRESS) {
            Log.d(LOG_TAG, "listening...");
        } else if (data.equals("7") && statusCode == StatusCode.FAILURE && (--mMaxTry) > 0) {
            Log.d(LOG_TAG, "Error 7 (NO_INPUT) retrying!");
            startSpeechEngine();
        }
    }

    private void startSpeechEngine() { mAskImpl.getOneSentence(); }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(LOG_TAG, "Service started!");
        String CHANNEL_ID = "my_channel_01";
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                                            .setContentTitle("Activity recording has started!")
                                            .setContentText("detecting now..")
                                            .build();

        startForeground(1, notification);

        startSpeechEngine();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");
        mAskImpl.stopListening();
        super.onDestroy();
    }
}
