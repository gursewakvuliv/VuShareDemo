package com.vusharedemo;

import android.app.Application;
import android.util.Log;

import com.vusharesdk.exception.VuShareException;
import com.vusharesdk.vushare.VuShare;

/**
 * Created by MB0000003 on 23-Apr-18.
 */

public class TheApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
////            VuShare.getInstance().initialize(this, "");
            VuShare.getInstance().initialize(this, "gdkhddkndgdflxx2545dgfgd6546df654fdfvxf");
        } catch (VuShareException e) {
            e.printStackTrace();
            Log.wtf("error Code", e.getErrorCode());
            Log.wtf("Message", e.getMessage());
        }
    }
}
