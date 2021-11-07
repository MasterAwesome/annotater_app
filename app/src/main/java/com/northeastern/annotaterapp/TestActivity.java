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

import static com.northeastern.annotaterapp.utils.PermissionUtils.requestRecordAudioPermission;

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