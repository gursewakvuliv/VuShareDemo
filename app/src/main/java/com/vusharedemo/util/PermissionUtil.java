package com.vusharedemo.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.vusharedemo.R;

import java.util.ArrayList;

/**
 * Created by MB0000003 on 18-Apr-18.
 */

public class PermissionUtil {

    public static void checkLocationPermission(Context context, PermissionListener permissionListener){
        if (!isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            TedPermission mTedPermission = new TedPermission(context)
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            permissionListener.onPermissionGranted();
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                            permissionListener.onPermissionDenied(deniedPermissions);
                        }
                    })
                    .setDeniedMessage(context.getString(R.string.permission_denied_message))
                    .setPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION);
            mTedPermission.check();
        } else {
            permissionListener.onPermissionGranted();
        }
    }

    public static void checkWriteStoragePermission(Context context, PermissionListener permissionListener){
        if (!isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            TedPermission mTedPermission = new TedPermission(context)
                    .setPermissionListener(permissionListener)
                    .setDeniedMessage(context.getString(R.string.permission_denied_message))
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);
            mTedPermission.check();
        } else {
            permissionListener.onPermissionGranted();
        }
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        if (!isMarshmallow()) {
            return true;
        }
        if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMarshmallow() {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }
}