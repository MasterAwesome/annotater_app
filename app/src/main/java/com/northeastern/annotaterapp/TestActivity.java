package com.northeastern.annotaterapp;

import static com.northeastern.annotaterapp.utils.PermissionUtils.requestRecordAudioPermission;

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
    private Button record;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestRecordAudioPermission(this);
        initComponents();
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
                status.setText(getResources().getString(R.string.listening));
                break;
            case FAILURE:
            default:
                status.setText(getResources().getString(R.string.failure_def));
        }
    }
}