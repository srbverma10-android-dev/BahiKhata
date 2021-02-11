package com.sourabhverma.bahikhata;

public class Bluetooth_Devices {

    public Bluetooth_Devices(String deviceName, String deviceAddress) {
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    private String deviceName;
    private String deviceAddress;


}
