package com.apps.bandung.bandungcoin.Model;

/**
 * Created by Rifqy on 24/03/2018.
 */

public class Post {
    private String image, nama, deskripsi;
    private Long timestamp;

    public Post(){

    }

    public Post(String image, String nama, Long timestamp, String deskripsi) {
        this.image = image;
        this.nama = nama;
        this.timestamp = timestamp;
        this.deskripsi = deskripsi;
    }

    public String getImage() {
        return image;
    }

    /*public void setImage(String image) {
        this.image = image;
    }*/

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }



}
