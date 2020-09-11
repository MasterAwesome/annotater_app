package com.northeastern.annotaterapp.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;

public final class PermissionUtils {
    public static void requestRecordAudioPermission(AppCompatActivity activity) {
        String requiredPermission = Manifest.permission.RECORD_AUDIO;

        // If the user previously denied this permission then show a message explaining why
        // this permission is needed
        if (activity.checkCallingOrSelfPermission(requiredPermission)
                == PackageManager.PERMISSION_DENIED) {
            activity.requestPermissions(new String[] {requiredPermission}, 101);
        }
    }
}
