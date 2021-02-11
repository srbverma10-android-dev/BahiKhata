package com.sourabhverma.bahikhata;

public class ALL_DATA_FOR_RECORDS {

    private String Credit;
    private String Debit;
    private String Date_And_Time;
    private String Customer_Name;

    public String getCredit() {
        return Credit;
    }

    public void setCredit(String credit) {
        Credit = credit;
    }

    public String getDebit() {
        return Debit;
    }

    public void setDebit(String debit) {
        Debit = debit;
    }

    public String getDate_And_Time() {
        return Date_And_Time;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public String getCustomer_Device_ID() {
        return Customer_Device_ID;
    }

    private String Customer_Device_ID;

    public ALL_DATA_FOR_RECORDS(String credit, String debit, String date_And_Time, String customer_Name, String customer_Device_ID) {
        Credit = credit;
        Debit = debit;
        Date_And_Time = date_And_Time;
        Customer_Name = customer_Name;
        Customer_Device_ID = customer_Device_ID;
    }
}
