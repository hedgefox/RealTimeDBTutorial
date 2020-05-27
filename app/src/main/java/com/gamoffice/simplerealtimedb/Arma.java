package com.gamoffice.simplerealtimedb;


import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Belal on 2/26/2017.
 */
@IgnoreExtraProperties
public class Arma {
    private String id;
    private String armaName;
    private int danno;

    public Arma() {

    }

    public Arma(String id, String armaName, int danno) {
        this.armaName = armaName;
        this.danno = danno;
        this.id = id;
    }

    public String getArmaName() {
        return armaName;
    }

    public int getDanno() {
        return danno;
    }
}