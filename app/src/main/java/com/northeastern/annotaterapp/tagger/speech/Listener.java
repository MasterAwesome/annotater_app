package com.northeastern.annotaterapp.tagger.speech;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.northeastern.annotaterapp.ICallback;

import java.util.ArrayList;

import static com.northeastern.annotaterapp.ICallback.StatusCode.FAILURE;
import static com.northeastern.annotaterapp.ICallback.StatusCode.SUCCESS;

class Listener implements RecognitionListener {
    private static final String LOG_TAG = Listener.class.getSimpleName();
    private final ICallback callback;

    public Listener(ICallback callback) {
        this.callback = callback;
    }

    public void onReadyForSpeech(Bundle params) {
        Log.v(LOG_TAG, "onReadyForSpeech");
    }

    public void onBeginningOfSpeech() {
        Log.v(LOG_TAG, "onBeginningOfSpeech");
    }

    public void onRmsChanged(float rmsdB) {
    }

    public void onBufferReceived(byte[] buffer) {
        Log.v(LOG_TAG, "onBufferReceived");
    }

    public void onEndOfSpeech() {
        Log.v(LOG_TAG, "onEndofSpeech");
    }

    public void onError(int error) {
        Log.v(LOG_TAG, "error " + error);
        callback.handleCallback(FAILURE, String.valueOf(error));
    }

    public void onResults(Bundle results) {
        Log.d(LOG_TAG, "Speech recognition results obtained!");
        Log.v(LOG_TAG, "=====================================");

        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (data == null) {
            Log.e(LOG_TAG, "Failed to recognize anything!");
            callback.handleCallback(FAILURE, null);
        } else {
            for (int i = 0; i < data.size(); i++) {
                Log.v(LOG_TAG, (i + 1) + ". " + data.get(i));
            }
            callback.handleCallback(SUCCESS, data.get(0));

        }
        Log.v(LOG_TAG, "=====================================");
    }

    public void onPartialResults(Bundle partialResults) {
        Log.d(LOG_TAG, "onPartialResults");
    }

    public void onEvent(int eventType, Bundle params) {
        Log.d(LOG_TAG, "onEvent " + eventType);
    }
}
