package com.apps.bandung.bandungcoin.Model;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Rifqy on 21/04/2018.
 */

public class TModel {

    private String tnama;
    private String tdeskripsi;
    private Long timestamp;
    private String tfoto;

    public TModel(){

    }

    public TModel(String tnama, String tdeskripsi, Long timestamp, String tfoto) {
        this.tnama = tnama;
        this.tdeskripsi = tdeskripsi;
        this.timestamp = timestamp;
        this.tfoto = tfoto;
    }

    public String getTnama() {
        return tnama;
    }

    public void setTnama(String tnama) {
        this.tnama = tnama;
    }

    public String getTdeskripsi() {
        return tdeskripsi;
    }

    public void setTdeskripsi(String tdeskripsi) {
        this.tdeskripsi = tdeskripsi;
    }

    public String getTimestamp() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String a = DateFormat.format("dd-MM-yyyy", cal).toString();

        return a;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTfoto() {

        if(tfoto.equals("default")) {
            this.tfoto = "https://firebasestorage.googleapis.com/v0/b/coin-20994.appspot.com/o/Kampus%2Fdefaultprofile.jpg?alt=media&token=33bffa66-d312-4ba6-8d12-4c9fc1ec2d83";
            return tfoto;
        } else {
            return tfoto;
        }

    }

    public void setTfoto(String tfoto) {
        this.tfoto = tfoto;
    }






}
