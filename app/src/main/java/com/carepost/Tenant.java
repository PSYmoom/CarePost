package com.carepost;

public class Tenant {
    private int AptNum;
    private String Mail;
    private String Name;

    public Tenant() {
        //public no-arg constructor required
    }

    public Tenant(int AptNum, String Mail, String Name) {
        this.AptNum = AptNum;
        this.Mail = Mail;
        this.Name = Name;
    }

    public int getAptNum() {
        return AptNum;
    }

    public String getMail() {
        return Mail;
    }

    public String getName() {
        return Name;
    }
}
