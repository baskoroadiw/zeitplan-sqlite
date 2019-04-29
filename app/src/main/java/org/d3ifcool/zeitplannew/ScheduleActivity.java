package org.d3ifcool.zeitplannew;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import org.d3ifcool.zeitplannew.data.JadwalContract;

import maes.tech.intentanim.CustomIntent;

public class ScheduleActivity extends AppCompatActivity {

    private String hari;
    private TextView tvJudul;
    private DetailAdapter mAdapter;
    private RecyclerView recyclerView;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        hari = getIntent().getStringExtra("hari");
        tvJudul = findViewById(R.id.tv_judul_detail);
        tvJudul.setText("Jadwal Hari " + hari);

        recyclerView = findViewById(R.id.recyclerViewDetail);

        mToolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);

        showData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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