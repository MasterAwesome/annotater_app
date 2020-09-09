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
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_IN"); // clearly I have an indian accent :(
        callback.handleCallback(ICallback.StatusCode.IN_PROGRESS, null);
        speechRecognizer.startListening(intent);
    }


    @Override
    public void stopListening() {
        speechRecognizer.destroy();
    }


}
