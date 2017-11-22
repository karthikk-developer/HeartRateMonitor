package com.samples.heartratemonitor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 5000;
    private List<BluetoothDevice> mDevicesList = new ArrayList<>();
    private DevicesAdapter mAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private RecyclerView mRecyclerView;
    private Button btScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        btScan = (Button) findViewById(R.id.bt_scan);
        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new DevicesAdapter(mDevicesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mHandler = new Handler();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BluetoothLE feature not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "BluetoothLE feature not available.", Toast.LENGTH_SHORT).show();
        }
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanDevices(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        mAdapter = new DevicesAdapter(new ArrayList<BluetoothDevice>());
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mAdapter);
        }
        //scanDevices(true);
    }

    private void scanDevices(boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    setScanBtProperties();
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        setScanBtProperties();
    }

    private void setScanBtProperties() {
        if (mScanning){
            btScan.setEnabled(false);
            btScan.setText("Scanning");
        }else{
            btScan.setText("Scan");
            btScan.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Cannot scan for bluetooth devices without permission", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addDevice(device);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    @Override
    protected void onPause() {
        super.onPause();
        scanDevices(false);
        mAdapter.clear();
    }
}
