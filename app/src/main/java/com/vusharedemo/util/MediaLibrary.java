package com.vusharedemo.util;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;

import com.vusharesdk.model.EntityMediaDetail;
import com.vusharesdk.utility.Constants;

import java.util.ArrayList;

/**
 * Created by MB0000003 on 14-Nov-17.
 */

public class MediaLibrary {
    private static final MediaLibrary ourInstance = new MediaLibrary();

    public static MediaLibrary getInstance() {
        return ourInstance;
    }

    private MediaLibrary() {
    }

    public ArrayList<EntityMediaDetail> getAllVideoMediaDetails(Context context) {

        ArrayList<EntityMediaDetail> fileModelList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            return fileModelList;
        }

        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        };

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;


        Uri queryUri = MediaStore.Files.getContentUri("external");

        ContentResolver cr = context.getContentResolver();

        Cursor cursor = cr.query(queryUri, projection, selection, null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");

        if (cursor != null) {

            for (int i = 0; i < cursor.getCount(); i++) {

                cursor.moveToPosition(i);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String path = cursor.getString(dataColumnIndex);

                EntityMediaDetail mediaDetail = new EntityMediaDetail();
                mediaDetail.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE)));

                long videoDuration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                mediaDetail.setDuration(videoDuration);
                mediaDetail.setId(cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)));
                mediaDetail.setPath(path);
                mediaDetail.setType(Constants.TYPE_VIDEO);
                fileModelList.add(mediaDetail);
            }
            cursor.close();
        }
        return fileModelList;
    }
}