package com.example.chung.nhacvieccanhan.model;

/**
 * Created by Chung on 10/16/2017.
 */

public class ThoiGianLap {
    private int id;
    private String SoThoiGianLap;

    public ThoiGianLap(int id, String soThoiGianLap) {
        this.id = id;
        SoThoiGianLap = soThoiGianLap;
    }

    public ThoiGianLap(String soThoiGianLap) {
        SoThoiGianLap = soThoiGianLap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSoThoiGianLap() {
        return SoThoiGianLap;
    }

    public void setSoThoiGianLap(String soThoiGianLap) {
        SoThoiGianLap = soThoiGianLap;
    }
}
