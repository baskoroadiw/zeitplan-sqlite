package org.d3ifcool.zeitplannew;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.d3ifcool.zeitplannew.data.Jadwal;
import org.d3ifcool.zeitplannew.data.JadwalContract;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    Context mContext;
    Cursor mCursor;

    public DetailAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_detailjadwal,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (!mCursor.moveToPosition(i)){
            return;
        }

        int waktuColumnIndex = mCursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_WAKTU);
        int waktuSelesaiColumnIndex = mCursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_WAKTU_SELESAI);
        int dosenColumnIndex = mCursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_DOSEN);
        int matakuliahColumnIndex = mCursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_MATAKULIAH);
        int ruanganColumnIndex = mCursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_RUANGAN);

        String waktu = mCursor.getString(waktuColumnIndex);
        String waktuSelesai = mCursor.getString(waktuSelesaiColumnIndex);
        String waktuFix = waktu + " - " + waktuSelesai;
        String dosen = mCursor.getString(dosenColumnIndex);
        String matakuliah = mCursor.getString(matakuliahColumnIndex);
        String ruangan = mCursor.getString(ruanganColumnIndex);
        final long id = mCursor.getLong(mCursor.getColumnIndex(JadwalContract.JadwalEntry._ID));

        viewHolder.detailMatkul.setText(matakuliah);
        viewHolder.detailRuangan.setText(ruangan);
        viewHolder.detailWaktu.setText(waktuFix);
        viewHolder.detailDosen.setText(dosen);
        viewHolder.itemView.setTag(id);

        viewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UpdateActivity.class);

                Uri currentZeitplanUri = ContentUris.withAppendedId(JadwalContract.JadwalEntry.CONTENT_URI, id);
                intent.setData(currentZeitplanUri);

                mContext.startActivity(intent);
                CustomIntent.customType(mContext,"fadein-to-fadeout");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        final TextView detailDosen, detailWaktu, detailMatkul, detailRuangan;
        final ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            detailDosen = itemView.findViewById(R.id.tv_detail_dosen);
            detailWaktu = itemView.findViewById(R.id.tv_detail_waktu);
            detailMatkul = itemView.findViewById(R.id.tv_detail_matkul);
            detailRuangan = itemView.findViewById(R.id.tv_detail_ruangan);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
}
