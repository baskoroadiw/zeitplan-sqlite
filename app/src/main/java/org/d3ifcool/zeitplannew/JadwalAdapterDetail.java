package org.d3ifcool.zeitplannew;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.d3ifcool.zeitplannew.data.JadwalContract;

public class JadwalAdapterDetail extends CursorAdapter {

    TextView tvMataKuliah, tvKelas, tvWaktu, tvDosen;

    public JadwalAdapterDetail(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listitem_detailjadwal, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        tvMataKuliah = view.findViewById(R.id.tv_detail_matkul);
        tvKelas = view.findViewById(R.id.tv_detail_ruangan);
        tvWaktu = view.findViewById(R.id.tv_detail_waktu);
        tvDosen = view.findViewById(R.id.tv_detail_dosen);

        int mataKuliahColumnIndex = cursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_MATAKULIAH);
        int ruanganColumnIndex = cursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_RUANGAN);
        int waktuColumnIndex = cursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_WAKTU);
        int dosenColumnIndex = cursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_DOSEN);

        String mataKuliah = cursor.getString(mataKuliahColumnIndex);
        String ruangan = cursor.getString(ruanganColumnIndex);
        String waktu = cursor.getString(waktuColumnIndex);
        String dosen = cursor.getString(dosenColumnIndex);

        tvMataKuliah.setText(mataKuliah);
        tvKelas.setText(ruangan);
        tvWaktu.setText(waktu);
        tvDosen.setText(dosen);
    }
}
