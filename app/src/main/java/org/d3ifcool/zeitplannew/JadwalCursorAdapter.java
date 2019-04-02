package org.d3ifcool.zeitplannew;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.d3ifcool.zeitplannew.data.JadwalContract;

public class JadwalCursorAdapter extends CursorAdapter {

    TextView tvMataKuliah, tvRuangan, tvWaktu;

    public JadwalCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listitem_jadwal, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        tvMataKuliah = view.findViewById(R.id.tv_mata_kuliah);
        tvRuangan = view.findViewById(R.id.tv_ruangan);
        tvWaktu = view.findViewById(R.id.tv_waktu);

        int mataKuliahColumnIndex = cursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_MATAKULIAH);
        int ruanganColumnIndex = cursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_RUANGAN);
        int waktuColumnIndex = cursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_WAKTU);

        String mataKuliah = cursor.getString(mataKuliahColumnIndex);
        String ruangan = cursor.getString(ruanganColumnIndex);
        String waktu = cursor.getString(waktuColumnIndex);


        if (ruangan != null){
            tvRuangan.setText(ruangan);
        }else{
            tvRuangan.setText("Ruangan belum diset");
        }

        if (waktu != null){
            tvWaktu.setText(waktu);
        }else{
            tvWaktu.setText("Waktu belum di set");
        }

        tvMataKuliah.setText(mataKuliah);
    }
}
