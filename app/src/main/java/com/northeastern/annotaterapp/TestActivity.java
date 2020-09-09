package com.northeastern.annotaterapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.northeastern.annotaterapp.tagger.IAskDefault;
import com.northeastern.annotaterapp.tagger.speech.IAskRecorderImpl;

/**
 * Test activity (NOT SHIPPED) for speech recognition tests.
 */
@Deprecated
public class TestActivity extends AppCompatActivity implements ICallback {

    private static final int RECORD_REQUEST_CODE = 11;
    private Button record;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestRecordAudioPermission(this);
        initComponents();
    }

    public static void requestRecordAudioPermission(AppCompatActivity activity) {
        String requiredPermission = Manifest.permission.RECORD_AUDIO;

        // If the user previously denied this permission then show a message explaining why
        // this permission is needed
        if (activity.checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
            activity.requestPermissions(new String[]{requiredPermission}, 101);
        }
    }

    private void initComponents() {
        record = findViewById(R.id.record);
        status = findViewById(R.id.status);
        final IAskDefault recorder = new IAskRecorderImpl(this, this);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.getOneSentence();
            }
        });
    }

    @Override
    public void handleCallback(StatusCode statusCode, String data) {
        switch (statusCode) {
            case SUCCESS:
                status.setText(data);
                break;
            case IN_PROGRESS:
                status.setText("listening...");
                break;
            case FAILURE:
            default:
                status.setText("Failed to recognize something went wrong?!");
        }
    }
}