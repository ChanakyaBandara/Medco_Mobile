package com.sylabs.medco;

public class PreOder {
    private String oder_id, oder_date, reference, Cost, PHID, PID, Ph_name;

    public PreOder() {

    }

    public PreOder(String oder_id, String oder_date, String reference, String cost, String PHID, String PID, String ph_name) {
        this.oder_id = oder_id;
        this.oder_date = oder_date;
        this.reference = reference;
        this.Cost = cost;
        this.PHID = PHID;
        this.PID = PID;
        this.Ph_name = ph_name;
    }

    public String getOder_id() {
        return oder_id;
    }

    public void setOder_id(String oder_id) {
        this.oder_id = oder_id;
    }

    public String getOder_date() {
        return oder_date;
    }

    public void setOder_date(String oder_date) {
        this.oder_date = oder_date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        this.Cost = cost;
    }

    public String getPHID() {
        return PHID;
    }

    public void setPHID(String PHID) {
        this.PHID = PHID;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getPh_name() {
        return Ph_name;
    }

    public void setPh_name(String ph_name) {
        this.Ph_name = ph_name;
    }
}
