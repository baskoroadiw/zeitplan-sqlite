package org.d3ifcool.zeitplannew;

import android.content.ContentUris;
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

    private TextView tvToday,tvTime;
    private FloatingActionButton fabAdd;
    private JadwalDbHelper helper;
    private JadwalCursorAdapter mCursorAdapter;
    private Calendar mCalendar;
    private String mataKuliah = "";
    private int mYear, mMonth, mHour, mMinute, mDay;
    ListView listViewJadwal;
    View emptyView;

    private static final int ZEITPLAN_LOADER = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        tvToday = view.findViewById(R.id.textViewToday);
        fabAdd = view.findViewById(R.id.fab);
        helper = new JadwalDbHelper(appContext);

        listViewJadwal = view.findViewById(R.id.listView);
        emptyView = view.findViewById(R.id.empty_view);
        listViewJadwal.setEmptyView(emptyView);

        mCursorAdapter = new JadwalCursorAdapter(appContext, null);
        listViewJadwal.setAdapter(mCursorAdapter);

        listViewJadwal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(appContext, AddActivity.class);

                Uri currentZeitplanUri = ContentUris.withAppendedId(JadwalContract.JadwalEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentZeitplanUri);
                startActivity(intent);
            }
        });

        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMataKuliah();
            }
        });

        LoaderManager.getInstance(this).initLoader(ZEITPLAN_LOADER, null, this);

        today();

        return view;
    }

    private void addMataKuliah() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.Theme_AppCompat_Light_Dialog_MinWidth);
        builder.setTitle("Tambahkan Mata Kuliah");

        final EditText input = new EditText(appContext);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(input.getText().toString().trim())){
                    return;
                }

                mataKuliah = input.getText().toString();
                ContentValues values = new ContentValues();

                values.put(JadwalContract.JadwalEntry.COLUMN_MATAKULIAH, mataKuliah);

                Uri newUri = appContext.getContentResolver().insert(JadwalContract.JadwalEntry.CONTENT_URI, values);

                restartLoader();


                if (newUri == null) {
                    Toast.makeText(appContext, "Setting Reminder Title failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(appContext, "Mata Kuliah Ditambahkan", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void today(){
        Date date = Calendar.getInstance().getTime();
        String hariIni = (String) DateFormat.format("EEEE", date); // Thursday
        String tanggal = (String) DateFormat.format("d",   date); // 20
        String monthNumber  = (String) DateFormat.format("M",   date); // 06
        String year         = (String) DateFormat.format("yyyy", date); // 2013

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
        String[] projection = {
                JadwalContract.JadwalEntry._ID,
                JadwalContract.JadwalEntry.COLUMN_HARI,
                JadwalContract.JadwalEntry.COLUMN_MATAKULIAH,
                JadwalContract.JadwalEntry.COLUMN_KELAS,
                JadwalContract.JadwalEntry.COLUMN_DOSEN,
                JadwalContract.JadwalEntry.COLUMN_RUANGAN,
                JadwalContract.JadwalEntry.COLUMN_TANGGAL,
                JadwalContract.JadwalEntry.COLUMN_WAKTU,
                JadwalContract.JadwalEntry.COLUMN_TIMESTAMP
        };
        return new CursorLoader(getActivity(), JadwalContract.JadwalEntry.CONTENT_URI,projection,null,null,null);
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
}
