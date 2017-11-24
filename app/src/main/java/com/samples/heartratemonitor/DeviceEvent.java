package com.samples.heartratemonitor;

/**
 * Created by karthiknew on 23/11/17.
 */

public class DeviceEvent {
    public int type;
    public String data;

    public final static int GATT_DISCONNECTED = 0;
    public final static int GATT_CONNECTED = 1;
    public final static int GATT_SERVICES_DISCOVERED = 2;
    public final static int DATA_AVAILABLE = 3;
    public final static int MESSAGE = 4;

}
