package com.vusharedemo.ui.activity;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.vusharedemo.R;
import com.vusharedemo.util.PermissionUtil;
import com.vusharesdk.callbacks.ScanCallback;
import com.vusharesdk.callbacks.UniversalCallback;
import com.vusharesdk.controller.SharedPrefController;
import com.vusharesdk.model.UserModel;
import com.vusharesdk.vushare.VuShare;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        setUserDetail();
        findViewById(R.id.btnShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createServer();
            }
        });

        findViewById(R.id.btnJoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
    }

    private void setUserDetail() {
        SharedPrefController.setUserName(this, "Test");
        SharedPrefController.setPhoneNo(this, "32686566456");
        SharedPrefController.setStatus(this, "feeling good");
        SharedPrefController.setGender(this, "Male");
    }

    private void createServer() {
        toggleProgressVisibility(true);
        VuShare.getInstance().createServer(MainActivity.this, SharedPrefController.getUserName(MainActivity.this), new UniversalCallback<String>() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(MainActivity.this, "Server Created", Toast.LENGTH_SHORT).show();
                register();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                toggleProgressVisibility(false);
            }
        });
    }

    private void register() {
        UserModel userModel = new UserModel();
        userModel.setPhoneNo(SharedPrefController.getPhoneNo(this));
        userModel.setStatus(SharedPrefController.getStatus(this));
        userModel.setGender(SharedPrefController.getGender(this));
        userModel.setName(SharedPrefController.getUserName(this));
        VuShare.getInstance().register(VuShare.getInstance().getServerHost(), userModel, new UniversalCallback<UserModel>() {
            @Override
            public void onSuccess(UserModel s) {
                // now you can add feeds
                Toast.makeText(MainActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                toggleProgressVisibility(false);
                startActivity(new Intent(MainActivity.this, FeedActivity.class));
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MainActivity.this, "Register failure", Toast.LENGTH_SHORT).show();
                toggleProgressVisibility(false);
            }
        });
    }


    private void checkPermission() {
        PermissionUtil.checkLocationPermission(MainActivity.this, new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                getWifiNetworkList();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        });
    }


    private void getWifiNetworkList() {
        VuShare.getInstance().getAvailableHostNetworks(this, new ScanCallback.WifiNetworkScan() {
            @Override
            public void scanSuccess(List<ScanResult> scanResultList) {
                toggleProgressVisibility(true);
                Toast.makeText(MainActivity.this, "connect To Host", Toast.LENGTH_SHORT).show();
                connectToHost(scanResultList.get(0).SSID, scanResultList.get(0).BSSID);
            }

            @Override
            public void scanFailure() {
                toggleProgressVisibility(false);
                Toast.makeText(MainActivity.this, "No Network Available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connectToHost(String ssid, String bssid) {
        VuShare.getInstance().connectToWifi(MainActivity.this, ssid, bssid, new ScanCallback.WifiConnection() {
            @Override
            public void onConnectionEstablished() {
                ping();
            }

            @Override
            public void onConnectionError() {
                toggleProgressVisibility(false);
                Toast.makeText(MainActivity.this, "Cannot connect to " + ssid, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ping() {
        VuShare.getInstance().ping(VuShare.getInstance().getServerHost(), MainActivity.this, new UniversalCallback<UserModel>() {
            @Override
            public void onSuccess(UserModel serverModel) {
                Toast.makeText(MainActivity.this, "Ping success", Toast.LENGTH_SHORT).show();
                register();
            }

            @Override
            public void onFailure(String message) {
                toggleProgressVisibility(false);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        VuShare.getInstance().resume(this);
    }

    private void toggleProgressVisibility(boolean show){
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}