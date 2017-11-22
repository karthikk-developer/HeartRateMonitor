package com.samples.heartratemonitor;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by karthiknew on 22/11/17.
 */

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.MyViewHolder> {
    List<BluetoothDevice> mDevicesList;

    public DevicesAdapter(List<BluetoothDevice> dataList){
        this.mDevicesList =dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BluetoothDevice device = mDevicesList.get(position);
        holder.tvName.setText(device.toString());
    }

    @Override
    public int getItemCount() {
        return mDevicesList.size();
    }

    public void addDevice(BluetoothDevice device) {
        if(!mDevicesList.contains(device)) {
            mDevicesList.add(device);
        }
    }

    public void clear() {
        mDevicesList.clear();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public Button btConnect;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            btConnect = itemView.findViewById(R.id.bt_connect);
        }
    }
}
