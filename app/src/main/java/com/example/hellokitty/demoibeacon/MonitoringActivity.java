package com.example.hellokitty.demoibeacon;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.altbeacon.beacon.BeaconManager;



public class MonitoringActivity extends AppCompatActivity {
    protected static final String TAG = "MonitoringActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

// ứng dụng dùng android v6 trở lên cần bật định vị
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Ứng dụng cần bật định vụ");
                builder.setMessage("Vui lòng bật định vị để có thể phát hiện beacon");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @TargetApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_COARSE_LOCATION);
                    }

                });
                builder.show();
            }
        }
        Intent myIntent = new Intent(this, RangingActivity.class);
        this.startActivity(myIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Đã bật định vị");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Ứng dụng cần bật định vụ");
                    builder.setMessage("Vui lòng bật định vị để có thể phát hiện beacon");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    // chạy RangingActivity
    public void onRangingClicked(View view) {
        Intent myIntent = new Intent(this, RangingActivity.class);
        this.startActivity(myIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BeaconApplication) this.getApplicationContext()).setMonitoringActivity(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BeaconApplication) this.getApplicationContext()).setMonitoringActivity(null);
    }
}
