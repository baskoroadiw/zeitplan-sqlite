package org.d3ifcool.zeitplannew;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.d3ifcool.zeitplannew.data.Jadwal;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    Context mContext;
    ArrayList<Jadwal> listJadwal;

    public DetailAdapter(Context mContext, ArrayList<Jadwal> listJadwal) {
        this.mContext = mContext;
        this.listJadwal = listJadwal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_detailjadwal,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Jadwal jadwal = listJadwal.get(i);

        viewHolder.detailMatkul.setText(jadwal.getMatakuliah());
        viewHolder.detailRuangan.setText(jadwal.getRuangan());
        viewHolder.detailDosen.setText(jadwal.getDosen());
        viewHolder.detailWaktu.setText(jadwal.getWaktu());
    }

    @Override
    public int getItemCount() {
        return listJadwal.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        final TextView detailDosen, detailWaktu, detailMatkul, detailRuangan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            detailDosen = itemView.findViewById(R.id.tv_detail_dosen);
            detailWaktu = itemView.findViewById(R.id.tv_detail_waktu);
            detailMatkul = itemView.findViewById(R.id.tv_detail_matkul);
            detailRuangan = itemView.findViewById(R.id.tv_detail_ruangan);
        }
    }
}
