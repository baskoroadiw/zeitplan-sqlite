package org.d3ifcool.zeitplannew;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import org.d3ifcool.zeitplannew.data.JadwalContract;

import maes.tech.intentanim.CustomIntent;

public class ScheduleActivity extends AppCompatActivity {

    String hari;
    TextView tvJudul;
    DetailAdapter mAdapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        hari = getIntent().getStringExtra("hari");
        tvJudul = findViewById(R.id.tv_judul_detail);
        tvJudul.setText("Jadwal Hari " + hari);

        recyclerView = findViewById(R.id.recyclerViewDetail);

        showData();
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this,"right-to-left");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showData();
    }

    private void showData(){
        String[] projection = {
                JadwalContract.JadwalEntry._ID,
                JadwalContract.JadwalEntry.COLUMN_WAKTU,
                JadwalContract.JadwalEntry.COLUMN_WAKTU_SELESAI,
                JadwalContract.JadwalEntry.COLUMN_DOSEN,
                JadwalContract.JadwalEntry.COLUMN_MATAKULIAH,
                JadwalContract.JadwalEntry.COLUMN_RUANGAN
        };
        String selection = JadwalContract.JadwalEntry.COLUMN_HARI + "=?";
        String[] selectionArgs = {hari};

        Cursor cursor = getContentResolver().query(JadwalContract.JadwalEntry.CONTENT_URI, projection, selection, selectionArgs, null);

        if (cursor.getCount()==0){
            finish();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new DetailAdapter(this,cursor);
        recyclerView.setAdapter(mAdapter);
    }
}