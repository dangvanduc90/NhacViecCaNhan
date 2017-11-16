package com.example.chung.nhacvieccanhan.model;

/**
 * Created by Chung on 10/16/2017.
 */

public class ThoiGianLap {
    private int id;
    private int SoThoiGianLap;

    public ThoiGianLap(int id, int soThoiGianLap) {
        this.id = id;
        SoThoiGianLap = soThoiGianLap;
    }

    public ThoiGianLap(int soThoiGianLap) {
        SoThoiGianLap = soThoiGianLap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoThoiGianLap() {
        return SoThoiGianLap;
    }

    public void setSoThoiGianLap(int soThoiGianLap) {
        SoThoiGianLap = soThoiGianLap;
    }
}
