package com.sourabhverma.bahikhata;

public class TRANSACTION {

    public TRANSACTION(String date_and_time, String credit, String debit) {
        this.date_and_time = date_and_time;
        this.credit = credit;
        this.debit = debit;
    }

    public String getDate_and_time() {
        return date_and_time;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    private String date_and_time;
    private String credit,debit;

}
