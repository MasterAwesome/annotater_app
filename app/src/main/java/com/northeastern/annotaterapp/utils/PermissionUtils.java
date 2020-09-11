package com.northeastern.annotaterapp.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;

public final class PermissionUtils {
    private static final int RECORD_REQUEST_CODE = 101;

    public static void requestRecordAudioPermission(AppCompatActivity activity) {
        String requiredPermission = Manifest.permission.RECORD_AUDIO;

        // If the user previously denied this permission then show a message explaining why
        // this permission is needed
        if (activity.checkCallingOrSelfPermission(requiredPermission)
                == PackageManager.PERMISSION_DENIED) {
            activity.requestPermissions(new String[] {requiredPermission}, RECORD_REQUEST_CODE);
        }
    }
}
