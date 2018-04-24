package com.vusharedemo.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.gun0912.tedpermission.PermissionListener;
import com.vusharedemo.R;
import com.vusharedemo.ui.adapter.AdapterFeed;
import com.vusharedemo.util.MediaLibrary;
import com.vusharedemo.util.PermissionUtil;
import com.vusharesdk.callbacks.UniversalCallback;
import com.vusharesdk.model.EntityFeedModel;
import com.vusharesdk.model.EntityHomeList;
import com.vusharesdk.model.EntityMediaDetail;
import com.vusharesdk.model.FileModel;
import com.vusharesdk.model.UserModel;
import com.vusharesdk.utility.JsonParserUtil;
import com.vusharesdk.vushare.VuShare;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MB0000003 on 24-Apr-18.
 */

public class FeedActivity extends AppCompatActivity {

    private RecyclerView rvFeed;
    private Button btnFeed;
    private List<EntityMediaDetail> videoList;
    private int position;
    private AdapterFeed adapterFeed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initViews();
        setListeners();
        extractVideos();
        getFeeds();
    }

    private void initViews() {
        rvFeed = findViewById(R.id.rvFeedList);
        rvFeed.setLayoutManager(new LinearLayoutManager(this));
        btnFeed = findViewById(R.id.btnFeed);
    }

    private void setListeners() {
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFeed();
            }
        });
    }

    private void extractVideos() {
        PermissionUtil.checkWriteStoragePermission(this, new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                videoList = MediaLibrary.getInstance().getAllVideoMediaDetails(FeedActivity.this);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }
        });
    }

    private void getFeeds() {
        VuShare.getInstance().getHomeList(VuShare.getInstance().getServerHost(), new UniversalCallback<EntityHomeList>() {
            @Override
            public void onSuccess(EntityHomeList homeList) {
                // get all feed and user list
                List<EntityFeedModel> feedList = homeList.getFeedList();
                // set adapter
                setAdapter(feedList);
            }

            @Override
            public void onFailure(String message) {

            }
        }, new UniversalCallback<EntityFeedModel>() {
            @Override
            public void onSuccess(EntityFeedModel feedModel) {
                // get new feed added
                adapterFeed.addFeed(feedModel);
            }

            @Override
            public void onFailure(String message) {

            }
        }, new UniversalCallback<List<UserModel>>() {
            @Override
            public void onSuccess(List<UserModel> userModels) {
                // get new user added
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void addFeed() {

        if (videoList == null || videoList.size() == 0) return;

        List<FileModel> fileList = new ArrayList<>();
        FileModel fileModel = new FileModel();
        fileModel.setData(new JsonParserUtil<FileModel>().getJsonString(videoList.get(position), EntityMediaDetail.class));
        fileModel.setFileId(videoList.get(position).getId() + "");
        fileModel.setHostUrl(videoList.get(position).getPath());
        fileModel.setFileUrl(videoList.get(position).getPath());
        fileModel.setType(videoList.get(position).getType());
        fileModel.setTitle(videoList.get(position).getTitle());
        fileModel.setAlbum(videoList.get(position).getAlbumName());
        fileModel.setAlbumId(videoList.get(position).getAlbumId());
        fileList.add(fileModel);
        VuShare.getInstance().addFeed(this, VuShare.getInstance().getServerHost(), fileList, new UniversalCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if (videoList.size() == position) {
                    position = -1;
                }
                position++;
            }

            @Override
            public void onFailure(String message) {
            }
        });
    }

    private void setAdapter(List<EntityFeedModel> feedList) {
        if (adapterFeed == null) {
            adapterFeed = new AdapterFeed(this, feedList);
            rvFeed.setAdapter(adapterFeed);
        }
    }
}