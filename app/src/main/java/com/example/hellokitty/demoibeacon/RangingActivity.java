package com.example.hellokitty.demoibeacon;


import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import android.widget.Toast;

import org.altbeacon.beacon.*;
import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


public class RangingActivity extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    static private List<Beacon> arrListBeacon = new ArrayList<org.altbeacon.beacon.Beacon>();
    Toast toast = null;
    RelativeLayout messageblue;
    RelativeLayout messageblueon;
    LinearLayout layoutTruong;
    LinearLayout layoutTrong;
    LinearLayout layoutThucTap;
    ListView listtrangtrong;
    BeaconAdapter adapterbeacon;
    MaterialProgressBar thanhchay;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    MenuItem btnpause;
    MenuItem btnblue;
    int checkbtn = 1;
    int checkbtnblue = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_information);
        verifyBluetooth();//kiem tra thiet bi ho tro khong
        beaconManager.bind(this);
        messageblueon = findViewById(R.id.messageblueon);
        messageblue = findViewById(R.id.messageblue);
        layoutTruong = findViewById(R.id.layoutTruong);
        layoutThucTap = findViewById(R.id.layoutThucTap);
        layoutTrong = findViewById(R.id.layoutTrong);
        listtrangtrong = findViewById(R.id.listtrangtrong);
        thanhchay = findViewById(R.id.thanhchay);
        adapterbeacon = new BeaconAdapter(RangingActivity.this, arrListBeacon);
        listtrangtrong.setAdapter(adapterbeacon);
        beaconManager.setForegroundScanPeriod(300); // thời gian làm mới list
    }

    // 2 nút quét/tạm dừng và bật/tắt bluetooth
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        btnpause = menu.findItem(R.id.btnpause);
        btnblue = menu.findItem(R.id.btnbluetooth);
        if (!mBluetoothAdapter.isEnabled()) {
            btnpause.setTitle("Bật bluetooth");
            btnpause.setIcon(R.drawable.iconplay);
            checkbtn = 2;
            checkbtnblue = 2;
            messageblue.setVisibility(View.VISIBLE);
            messageblueon.setVisibility(View.GONE);
            thanhchay.setVisibility(View.GONE);
            btnblue.setIcon(R.drawable.onblue);
            btnblue.setTitle("Bật bluetooth");
        }
        return super.onCreateOptionsMenu(menu);
    }


    // xử lý khi nhấn vào nút quét/tạm dừng và bật/tắt bluetooth
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnpause:
                if (checkbtn == 1) {
                    checkbtn = 2;
                    item.setIcon(R.drawable.iconplay);
                    item.setTitle("Quét");
                    onPause();
                    toast.makeText(this, "Tạm dừng", Toast.LENGTH_SHORT).show();
                    thanhchay.setVisibility(View.GONE);
                } else {
                    if (mBluetoothAdapter.isEnabled()) {
                        checkbtn = 1;
                        item.setIcon(R.drawable.iconpause);
                        item.setTitle("Tạm dừng");
                        onResume();
                        toast.makeText(this, "Tiếp tục quét", Toast.LENGTH_SHORT).show();
                        thanhchay.setVisibility(View.VISIBLE);
                    } else {
                        toast.makeText(this, "Chưa bật bluetooth", Toast.LENGTH_SHORT).show();
                    }
                }
                break;


            case R.id.btnbluetooth:
                if (checkbtnblue == 1) {
                    checkbtnblue = 2;
                    item.setIcon(R.drawable.onblue);
                    item.setTitle("Tắt bluetooth");
                    mBluetoothAdapter.disable();
                    messageblue.setVisibility(View.VISIBLE);
                    messageblueon.setVisibility(View.GONE);
                    thanhchay.setVisibility(View.GONE);
                    checkbtn = 2;
                    btnpause.setIcon(R.drawable.iconplay);
                    btnpause.setTitle("Quét");
                    onPause();
                } else {
                    item.setIcon(R.drawable.disblue);
                    item.setTitle("Bật bluetooth");
                    checkbtnblue = 1;
                    mBluetoothAdapter.enable();
                    messageblue.setVisibility(View.GONE);
                    messageblueon.setVisibility(View.VISIBLE);
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    // dừng quét
    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this))
            beaconManager.setBackgroundMode(true);
    }

    // tiếp tục quét
    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<org.altbeacon.beacon.Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    arrListBeacon.clear();
                    arrListBeacon.addAll(beacons);
                    showListBeacon();
                }
            }

        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }


    Beacon beacon;

    private void showListBeacon() {
        runOnUiThread(new Runnable() {
            public void run() {
                beacon = null;
                if (beacon == null) {
                    beacon = arrListBeacon.get(0);
                }

// nhận beacon gần nhất để show
                for (int i = 0; i < arrListBeacon.size(); ++i) {
                    if (beacon.getDistance() > arrListBeacon.get(i).getDistance()) {
                        beacon = arrListBeacon.get(i);
                    }
                }

                if (beacon.getId1().toUuid().toString().equals("57427cda-0000-48df-8ae9-21167167e74d") && beacon.getDistance() < 1) {
                    layoutThucTap.setVisibility(View.GONE);
                    layoutTruong.setVisibility(View.VISIBLE);
                    layoutTrong.setVisibility(View.GONE);
                } else if (beacon.getId1().toUuid().toString().equals("00000000-4ec9-1001-b000-001c4dcf9430") && beacon.getDistance() < 1) {
                    layoutThucTap.setVisibility(View.VISIBLE);
                    layoutTruong.setVisibility(View.GONE);
                    layoutTrong.setVisibility(View.GONE);
                } else {
                    layoutThucTap.setVisibility(View.GONE);
                    layoutTruong.setVisibility(View.GONE);
                    layoutTrong.setVisibility(View.VISIBLE);
                    adapterbeacon.notifyDataSetChanged();
                }


            }
        });
    }

    // kiểm tra thiết bị có hỗ trợ bluetooth không.
    private void verifyBluetooth() {

        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            }
        } catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thiết bị không hỗ trợ");
            builder.setMessage("Thiết bị không hỗ trợ, vui lòng mua thiết bị khác.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                    System.exit(0);
                }
            });
            builder.show();
        }
    }
}
