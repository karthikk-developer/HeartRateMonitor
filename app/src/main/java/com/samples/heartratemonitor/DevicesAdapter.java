package com.samples.heartratemonitor;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karthiknew on 22/11/17.
 */

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.MyViewHolder> {
    List<BluetoothDevice> mDevicesList = new ArrayList<>();
    private onItemClickListener mListener;

    public interface onItemClickListener{
        void onItemClick(int position,BluetoothDevice device);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        BluetoothDevice device = mDevicesList.get(position);
        if (device.getName() != null && !device.getName().isEmpty()) {
            holder.tvName.setText(device.getName() + " - " + device.getAddress());
        } else {
            holder.tvName.setText(device.toString());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position,mDevicesList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDevicesList.size();
    }

    public void addDevice(BluetoothDevice device) {
        if (!mDevicesList.contains(device)) {
            mDevicesList.add(device);
        }
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.mListener = listener;
    }

    public void clear() {
        mDevicesList.clear();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public Button btConnect;
        public View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            btConnect = itemView.findViewById(R.id.bt_connect);
            this.itemView = itemView;
        }
    }


}
