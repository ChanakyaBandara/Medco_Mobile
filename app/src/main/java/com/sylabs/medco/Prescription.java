package com.sylabs.medco;

import java.io.Serializable;

public class Prescription implements Serializable {
    private String prID;
    private String Date;
    private String Doc_Name;
    private String QRcode;

    public Prescription() {
    }

    public Prescription(String prID, String date, String doc_Name, String QRcode) {
        this.prID = prID;
        Date = date;
        Doc_Name = doc_Name;
        this.QRcode = QRcode;
    }

    public String getPrID() {
        return prID;
    }

    public void setPrID(String prID) {
        this.prID = prID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDoc_Name() {
        return Doc_Name;
    }

    public void setDoc_Name(String doc_Name) {
        Doc_Name = doc_Name;
    }

    public String getQRcode() {
        return QRcode;
    }

    public void setQRcode(String QRcode) {
        this.QRcode = QRcode;
    }
}
