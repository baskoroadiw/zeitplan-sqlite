package org.d3ifcool.zeitplannew.data;

public class Jadwal {

    private String matakuliah, ruangan, dosen, waktu;

    public Jadwal(String matakuliah, String ruangan, String dosen, String waktu) {
        this.matakuliah = matakuliah;
        this.ruangan = ruangan;
        this.dosen = dosen;
        this.waktu = waktu;
    }

    public String getMatakuliah() {
        return matakuliah;
    }

    public String getRuangan() {
        return ruangan;
    }

    public String getDosen() {
        return dosen;
    }

    public String getWaktu() {
        return waktu;
    }
}
