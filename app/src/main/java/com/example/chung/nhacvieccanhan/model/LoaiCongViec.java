package com.example.chung.nhacvieccanhan.model;

/**
 * Created by Chung on 10/16/2017.
 */

public class LoaiCongViec {
    private int id;
    private String TenLoaiCV;
    private String MoTaLoaiCV;

    public LoaiCongViec(int id, String tenLoaiCV, String moTaLoaiCV) {
        this.id = id;
        this.TenLoaiCV = tenLoaiCV;
        this.MoTaLoaiCV = moTaLoaiCV;
    }

    public LoaiCongViec(String tenLoaiCV, String moTaLoaiCV) {
        this.TenLoaiCV = tenLoaiCV;
        this.MoTaLoaiCV = moTaLoaiCV;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenLoaiCV() {
        return TenLoaiCV;
    }

    public void setTenLoaiCV(String tenLoaiCV) {
        TenLoaiCV = tenLoaiCV;
    }

    public String getMoTaLoaiCV() {
        return MoTaLoaiCV;
    }

    public void setMoTaLoaiCV(String moTaLoaiCV) {
        MoTaLoaiCV = moTaLoaiCV;
    }
}
