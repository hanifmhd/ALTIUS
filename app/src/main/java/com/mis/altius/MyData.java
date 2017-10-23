package com.mis.altius;

/**
 * Created by Hanifmhd on 9/7/2017.
 */

public class MyData {
    private int id;
    private String nama, ttl, jenis_kelamin, alamat_rumah, kota, angkatan, no_telepon, email, perusahaan, sektor, jabatan, level_jabatan,
    alamat_kantor, kota_kantor, domisili;

    public MyData(int id, String nama, String ttl, String jenis_kelamin,String alamat_rumah, String kota, String angkatan, String no_telepon,
                  String email, String perusahaan, String sektor, String jabatan, String level_jabatan, String alamat_kantor, String kota_kantor,
                  String domisili) {
        this.id = id;
        this.nama = nama;
        this.ttl = ttl;
        this.jenis_kelamin = jenis_kelamin;
        this.alamat_rumah = alamat_rumah;
        this.kota = kota;
        this.angkatan = angkatan;
        this.no_telepon = no_telepon;
        this.email = email;
        this.perusahaan = perusahaan;
        this.sektor = sektor;
        this.jabatan = jabatan;
        this.level_jabatan = level_jabatan;
        this.alamat_kantor = alamat_kantor;
        this.kota_kantor = kota_kantor;
        this.domisili = domisili;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getAlamat_rumah() {
        return alamat_rumah;
    }

    public void setAlamat_rumah(String alamat_rumah) {
        this.alamat_rumah = alamat_rumah;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getAngkatan() {
        return angkatan;
    }

    public void setAngkatan(String angkatan) {
        this.angkatan = angkatan;
    }

    public String getNo_telepon() {
        return no_telepon;
    }

    public void setNo_telepon(String no_telepon) {
        this.no_telepon = no_telepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerusahaan() {
        return perusahaan;
    }

    public void setPerusahaan(String perusahaan) {
        this.perusahaan = perusahaan;
    }

    public String getSektor() {
        return sektor;
    }

    public void setSektor(String sektor) {
        this.sektor = sektor;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getLevel_jabatan() {
        return level_jabatan;
    }

    public void setLevel_jabatan(String level_jabatan) {
        this.level_jabatan = level_jabatan;
    }

    public String getAlamat_kantor() {
        return alamat_kantor;
    }

    public void setAlamat_kantor(String alamat_kantor) {
        this.alamat_kantor = alamat_kantor;
    }

    public String getKota_kantor() {
        return kota_kantor;
    }

    public void setKota_kantor(String kota_kantor) {
        this.kota_kantor = kota_kantor;
    }

    public String getDomisili() {
        return domisili;
    }

    public void setDomisili(String domisili) {
        this.domisili = domisili;
    }
}
