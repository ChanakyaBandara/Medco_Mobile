package com.sylabs.medco;

public class Drug {
    private String DrID, DrName, DrMan, DrStr, DrDes, DrImg, Dose;

    public Drug() {
    }

    public Drug(String drID, String drName, String drMan, String drStr, String drDes, String drImg, String dose) {
        DrID = drID;
        DrName = drName;
        DrMan = drMan;
        DrStr = drStr;
        DrDes = drDes;
        DrImg = drImg;
        Dose = dose;
    }

    public String getDrID() {
        return DrID;
    }

    public void setDrID(String drID) {
        DrID = drID;
    }

    public String getDrName() {
        return DrName;
    }

    public void setDrName(String drName) {
        DrName = drName;
    }

    public String getDrMan() {
        return DrMan;
    }

    public void setDrMan(String drMan) {
        DrMan = drMan;
    }

    public String getDrStr() {
        return DrStr;
    }

    public void setDrStr(String drStr) {
        DrStr = drStr;
    }

    public String getDrDes() {
        return DrDes;
    }

    public void setDrDes(String drDes) {
        DrDes = drDes;
    }

    public String getDrImg() {
        return DrImg;
    }

    public void setDrImg(String drImg) {
        DrImg = drImg;
    }

    public String getDose() {
        return Dose;
    }

    public void setDose(String dose) {
        Dose = dose;
    }
}
