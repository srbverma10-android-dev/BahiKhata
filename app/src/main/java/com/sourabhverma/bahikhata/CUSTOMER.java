package com.sourabhverma.bahikhata;

public class CUSTOMER {

    public CUSTOMER(String NAME, String PHONE_NUMBER, String DEVICE_ADDRESS) {
        this.NAME = NAME;
        this.PHONE_NUMBER = PHONE_NUMBER;
        this.DEVICE_ADDRESS = DEVICE_ADDRESS;
    }

    private String NAME;

    public String getNAME() {
        return NAME;
    }



    public String getPHONE_NUMBER() {
        return PHONE_NUMBER;
    }


    public String getDEVICE_ADDRESS() {
        return DEVICE_ADDRESS;
    }



    private String PHONE_NUMBER;
    private String DEVICE_ADDRESS;

}
