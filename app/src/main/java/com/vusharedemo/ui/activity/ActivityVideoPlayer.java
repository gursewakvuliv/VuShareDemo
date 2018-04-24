package com.vusharedemo.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.vusharedemo.R;

/**
 * Created by MB0000003 on 24-Apr-18.
 */

public class ActivityVideoPlayer extends AppCompatActivity {

    private VideoView player;
    public static final String PATH = "path";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        player = findViewById(R.id.video_view);
        player.setVideoURI(Uri.parse(getIntent().getStringExtra(PATH)));
        player.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                player.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stopPlayback();
    }
}
