package com.example.chung.nhacvieccanhan.model;

public class ThoiGianBaoThuc {
    private int id;
    private int MaLoaiCV;
    private String Ngay;
    private String ThoiGian;

    public ThoiGianBaoThuc(int id, int maLoaiCV, String ngay, String thoiGian) {
        this.id = id;
        MaLoaiCV = maLoaiCV;
        Ngay = ngay;
        ThoiGian = thoiGian;
    }

    public ThoiGianBaoThuc(int maLoaiCV, String ngay, String thoiGian) {
        MaLoaiCV = maLoaiCV;
        Ngay = ngay;
        ThoiGian = thoiGian;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaLoaiCV() {
        return MaLoaiCV;
    }

    public void setMaLoaiCV(int maLoaiCV) {
        MaLoaiCV = maLoaiCV;
    }

    public String getNgay() {
        return Ngay;
    }

    public void setNgay(String ngay) {
        Ngay = ngay;
    }

    public String getThoiGian() {
        return ThoiGian;
    }

    public void setThoiGian(String thoiGian) {
        ThoiGian = thoiGian;
    }
}
