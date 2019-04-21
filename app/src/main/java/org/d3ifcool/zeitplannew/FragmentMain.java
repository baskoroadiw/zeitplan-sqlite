package org.d3ifcool.zeitplannew;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.d3ifcool.zeitplannew.data.JadwalContract;
import org.d3ifcool.zeitplannew.data.JadwalDbHelper;

import java.util.Calendar;
import java.util.Date;

public class FragmentMain extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public FragmentMain() {}

    Context appContext = MainActivity.getContextOfApplication();

    private TextView tvToday, tvDetailDosen, tvDetailRuangan, tvDetailMatkul, tvDetailWaktu, tvNoData;
    private JadwalDbHelper helper;
    private JadwalCursorAdapter mCursorAdapter;
    private Calendar mCalendar;
    private String mataKuliah = "";
    private int mYear, mMonth, mHour, mMinute, mDay;
    ListView listViewJadwal;
    String hariIni;
    String timeNow;
    View emptyView;

    private static final int ZEITPLAN_LOADER = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        tvToday = view.findViewById(R.id.textViewToday);
        helper = new JadwalDbHelper(appContext);

        listViewJadwal = view.findViewById(R.id.listView);
        emptyView = view.findViewById(R.id.empty_view);
        listViewJadwal.setEmptyView(emptyView);

        tvDetailDosen = view.findViewById(R.id.tv_detail_dosen);
        tvDetailWaktu = view.findViewById(R.id.tv_detail_waktu);
        tvDetailMatkul = view.findViewById(R.id.tv_detail_matkul);
        tvDetailRuangan = view.findViewById(R.id.tv_detail_ruangan);
        tvNoData = view.findViewById(R.id.tv_no_data);

        Date dateNow = Calendar.getInstance().getTime();
        timeNow = (String) DateFormat.format("HH:mm", dateNow);
        hariIni = (String) DateFormat.format("EEEE", dateNow); // Thursday
        if (hariIni.equalsIgnoreCase("sunday")) {
            hariIni = "Minggu";
        }else if (hariIni.equalsIgnoreCase("monday")) {
            hariIni = "Senin";
        }else if (hariIni.equalsIgnoreCase("tuesday")) {
            hariIni = "Selasa";
        }else if (hariIni.equalsIgnoreCase("wednesday")) {
            hariIni = "Rabu";
        }else if (hariIni.equalsIgnoreCase("thursday")) {
            hariIni = "Kamis";
        }else if (hariIni.equalsIgnoreCase("friday")) {
            hariIni = "Jumat";
        }else if (hariIni.equalsIgnoreCase("saturday")) {
            hariIni = "Sabtu";
        }

        mCursorAdapter = new JadwalCursorAdapter(appContext, null);
        listViewJadwal.setAdapter(mCursorAdapter);

        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        LoaderManager.getInstance(this).initLoader(ZEITPLAN_LOADER, null, this);

        today();
        nearestSchedule();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        today();
        nearestSchedule();
        restartLoader();
    }

    public void today(){
        Date date = Calendar.getInstance().getTime();
        String tanggal = (String) DateFormat.format("d",   date); // 20
        String monthNumber  = (String) DateFormat.format("M",   date); // 06
        String year         = (String) DateFormat.format("yyyy", date); // 2013

        int month = Integer.parseInt(monthNumber);
        String bulan = null;
        if (month == 1){
            bulan = "Januari";
        }else if (month == 2){
            bulan = "Februari";
        }else if (month == 3){
            bulan = "Maret";
        }else if (month == 4){
            bulan = "April";
        }else if (month == 5){
            bulan = "Mei";
        }else if (month == 6){
            bulan = "Juni";
        }else if (month == 7){
            bulan = "Juli";
        }else if (month == 8){
            bulan = "Agustus";
        }else if (month == 9){
            bulan = "September";
        }else if (month == 10){
            bulan = "Oktober";
        }else if (month == 11){
            bulan = "November";
        }else if (month == 12){
            bulan = "Desember";
        }
        String formatFix = hariIni + ", "+tanggal+" "+bulan+" "+year;
        tvToday.setText(String.valueOf(formatFix));
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String selection = JadwalContract.JadwalEntry.COLUMN_HARI + "=? AND "+
                JadwalContract.JadwalEntry.COLUMN_WAKTU + ">?";
        String [] selectionArgs = {hariIni,timeNow};
        String[] projection = {
                JadwalContract.JadwalEntry._ID,
                JadwalContract.JadwalEntry.COLUMN_HARI,
                JadwalContract.JadwalEntry.COLUMN_MATAKULIAH,
                JadwalContract.JadwalEntry.COLUMN_DOSEN,
                JadwalContract.JadwalEntry.COLUMN_RUANGAN,
                JadwalContract.JadwalEntry.COLUMN_TANGGAL,
                JadwalContract.JadwalEntry.COLUMN_WAKTU,
                JadwalContract.JadwalEntry.COLUMN_TIMESTAMP
        };
        return new CursorLoader(getActivity(), JadwalContract.JadwalEntry.CONTENT_URI,projection,selection,selectionArgs,null);
    }


    public void onLoadFinished( Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    public void restartLoader(){
        LoaderManager.getInstance(this).restartLoader(ZEITPLAN_LOADER, null, this);
    }

    public void nearestSchedule(){
        String selection = JadwalContract.JadwalEntry.COLUMN_HARI + "=? AND "+
                        JadwalContract.JadwalEntry.COLUMN_WAKTU_SELESAI + ">=?";
        String [] selectionArgs = {hariIni,timeNow};
        String orderBy = JadwalContract.JadwalEntry.COLUMN_WAKTU + " DESC";
        String[] projection = {
                JadwalContract.JadwalEntry._ID,
                JadwalContract.JadwalEntry.COLUMN_HARI,
                JadwalContract.JadwalEntry.COLUMN_MATAKULIAH,
                JadwalContract.JadwalEntry.COLUMN_DOSEN,
                JadwalContract.JadwalEntry.COLUMN_RUANGAN,
                JadwalContract.JadwalEntry.COLUMN_TANGGAL,
                JadwalContract.JadwalEntry.COLUMN_WAKTU,
                JadwalContract.JadwalEntry.COLUMN_WAKTU_SELESAI,
                JadwalContract.JadwalEntry.COLUMN_TIMESTAMP
        };
        Cursor cursor =  appContext.getContentResolver().query(JadwalContract.JadwalEntry.CONTENT_URI,projection,selection,selectionArgs,null);
        if (cursor.moveToNext()){

            tvNoData.setVisibility(View.GONE);

            String matakuliah = cursor.getString(cursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_MATAKULIAH));
            String ruangan = cursor.getString(cursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_RUANGAN));
            String dosen = cursor.getString(cursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_DOSEN));
            String waktu = cursor.getString(cursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_WAKTU));
            String waktuSelesai = cursor.getString(cursor.getColumnIndex(JadwalContract.JadwalEntry.COLUMN_WAKTU_SELESAI));
            String waktuFix = waktu + " - " +waktuSelesai;

            tvDetailMatkul.setText(matakuliah);
            tvDetailDosen.setText(dosen);
            tvDetailRuangan.setText(ruangan);
            tvDetailWaktu.setText(waktuFix);

//            Log.i("URUTAN HARI", matakuliah+ruangan+dosen+waktu + "----------------" + timeNow);
        }else{
            tvNoData.setVisibility(View.VISIBLE);
            tvDetailMatkul.setVisibility(View.GONE);
            tvDetailDosen.setVisibility(View.GONE);
            tvDetailRuangan.setVisibility(View.GONE);
            tvDetailWaktu.setVisibility(View.GONE);
        }
        cursor.close();
    }
}
