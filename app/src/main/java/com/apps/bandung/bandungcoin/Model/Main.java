package com.apps.bandung.bandungcoin.Model;


public class Main {
    private String logo, nama, alamat;

    public Main(){

    }

    public Main(String logo, String nama, String alamat) {
        this.logo = logo;
        this.nama = nama;
        this.alamat = alamat;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
