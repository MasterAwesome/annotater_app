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

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import com.northeastern.annotaterapp.ICallback;
import com.northeastern.annotaterapp.tagger.IAskDefault;

public class IAskRecorderImpl extends IAskDefault {
    private static final String LOG_TAG = IAskRecorderImpl.class.getSimpleName();
    private final SpeechRecognizer speechRecognizer;

    public IAskRecorderImpl(Context ctx, ICallback callback) {
        super(ctx, callback);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(ctx);
        speechRecognizer.setRecognitionListener(new Listener(callback));
    }

    @Override
    public void getOneSentence() {
        Log.d(LOG_TAG, "getOneSentence");

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE, "en_IN"); // clearly I have an indian accent :(
        callback.handleCallback(ICallback.StatusCode.IN_PROGRESS, null);
        speechRecognizer.startListening(intent);
    }

    @Override
    public void stopListening() {
        speechRecognizer.destroy();
    }
}
