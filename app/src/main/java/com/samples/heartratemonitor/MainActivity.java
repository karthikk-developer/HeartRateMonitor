package com.samples.heartratemonitor;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private Button mBtConnect;
    private BluetoothLeService mBluetoothService;
    private boolean isBound;
    private ServiceConnection mServiceConnection;
    private TextView tvDeviceInfo;
    private TextView tvHeartRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        tvDeviceInfo = findViewById(R.id.tv_device_info);
        tvHeartRate = findViewById(R.id.tv_heart_rate);
        mBtConnect = findViewById(R.id.bt_connect);
        mBtConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(i, ScanActivity.SCAN_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanActivity.SCAN_REQUEST) {
            if (resultCode == RESULT_OK) {
                //bind to service and initiate device connection
                String connectAddress = data.getStringExtra(ScanActivity.DEVICE_ADDRESS);
                String deviceName = data.getStringExtra(ScanActivity.DEVICE_NAME);
                tvDeviceInfo.setText("Name:" + deviceName + "\n" + connectAddress);
                if (connectAddress != null && !connectAddress.isEmpty()) {
                    Intent i = new Intent(this, BluetoothLeService.class);
                    mServiceConnection = new ServiceConnection() {
                        @Override
                        public void onServiceConnected(ComponentName name, IBinder service) {
                            mBluetoothService = ((BluetoothLeService.LocalBinder) service).getService();
                            isBound = true;
                        }

                        @Override
                        public void onServiceDisconnected(ComponentName name) {
                            isBound = false;
                        }
                    };

                    bindService(i, mServiceConnection, BIND_AUTO_CREATE);
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onStop() {
        if (isBound && mServiceConnection != null) {
            unbindService(mServiceConnection);
        }
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeviceEvent(DeviceEvent event) {
    }


}
