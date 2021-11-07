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

package com.northeastern.annotaterapp.tagger.speech;

import static com.northeastern.annotaterapp.ICallback.StatusCode.FAILURE;
import static com.northeastern.annotaterapp.ICallback.StatusCode.SUCCESS;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import com.northeastern.annotaterapp.ICallback;
import java.util.ArrayList;

class Listener implements RecognitionListener {
    private static final String LOG_TAG = Listener.class.getSimpleName();
    private final ICallback callback;

    public Listener(ICallback callback) { this.callback = callback; }

    public void onReadyForSpeech(Bundle params) { Log.v(LOG_TAG, "onReadyForSpeech"); }

    public void onBeginningOfSpeech() { Log.v(LOG_TAG, "onBeginningOfSpeech"); }

    public void onRmsChanged(float rmsdB) {}

    public void onBufferReceived(byte[] buffer) { Log.v(LOG_TAG, "onBufferReceived"); }

    public void onEndOfSpeech() { Log.v(LOG_TAG, "onEndofSpeech"); }

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

    public void onPartialResults(Bundle partialResults) { Log.d(LOG_TAG, "onPartialResults"); }

    public void onEvent(int eventType, Bundle params) { Log.d(LOG_TAG, "onEvent " + eventType); }
}
