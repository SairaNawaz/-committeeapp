package com.apponative.committee_app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

/**
 * Created by Muhammad Waqas on 5/30/2017.
 */

public class PermissionUtils {

    public static boolean hasCameraAndWritePermission(Context context) {

        return hasCameraPermission(context) && hasWriteStoragePermission(context);
    }

    public static boolean hasCameraPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasWriteStoragePermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasReadContactPermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isPermissionRequired() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static void askReadContactsPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_CONTACTS},
                Constants.PERMISSION_REQUEST_CONTACTS);
    }

    public static void askWriteStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Constants.PERMISSION_REQUEST_EXTERNAL_STORAGE);
    }

    public static void askCameraAndWritePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Constants.PERMISSION_REQUEST_CAMERA);
    }

    public static void askWriteStoragePermission(Fragment fragment) {
        fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Constants.PERMISSION_REQUEST_EXTERNAL_STORAGE);
    }

    public static void askCameraAndWritePermission(Fragment fragment) {
        fragment.requestPermissions(
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Constants.PERMISSION_REQUEST_CAMERA);
    }
}
