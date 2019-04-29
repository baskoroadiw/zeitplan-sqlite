package org.d3ifcool.zeitplannew;

import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.d3ifcool.zeitplannew.data.JadwalContract;
import org.d3ifcool.zeitplannew.reminder.AlarmScheduler;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import maes.tech.intentanim.CustomIntent;

public class AddActivity extends AppCompatActivity {

//    private long currentId;

    //constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    private Toolbar mtoolbar;
    private Spinner spinnerHari;
    private EditText editTextMatakuliah, editTextDosen, editTextRuangan, editTextWaktu, editTextWaktuSelesai;
    private String mataKuliah, dosen, ruangan, hari, mDate, mTime, mTimeEnd;
    private long mRepeatTime;
    private TimePicker timePicker;
    private int mYear, mMonth, mHour, mMinute, mDay, mHourEnd, mMinuteEnd;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

//        getCurrentId();

        //initialize View
        spinnerHari = findViewById(R.id.spinnerHari);
        editTextMatakuliah = findViewById(R.id.editText_matakuliah);
        editTextDosen = findViewById(R.id.editText_dosen);
        editTextRuangan = findViewById(R.id.editText_ruangan);
        editTextWaktu = findViewById(R.id.editText_waktu);
        editTextWaktuSelesai = findViewById(R.id.editText_waktuSelesai);

        //setup matakuliah
        editTextMatakuliah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mataKuliah = s.toString().trim();
                editTextMatakuliah.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //initialize Default values
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        mTime = mHour + ":" + mMinute;
        mTimeEnd = mHourEnd + ":" + mMinuteEnd;

        //Toolbar
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle(R.string.title_activity_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);

        //get Date
        mDate = mDay + "/" +mMonth+ "/" +mYear;
        Log.i("DATE NOW",mDate);

        //get Time
        editTextWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(AddActivity.this, R.style.DialogTheme,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;
                        String fixMinute, fixHour;
                        if (minute < 10) {
                            fixMinute = "0" + minute;
                        } else {
                            fixMinute = String.valueOf(minute);
                        }
                        if (hourOfDay < 10) {
                            fixHour = "0" +hourOfDay;
                        }else{
                            fixHour = String.valueOf(hourOfDay);
                        }
                        mTime = fixHour + ":" + fixMinute;
                        editTextWaktu.setText(mTime);
                    }
                },mHour,mMinute,true);
                tpd.show();
            }
        });

        editTextWaktuSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(AddActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHourEnd = hourOfDay;
                        mMinuteEnd = minute;
                        String fixMinute, fixHour;
                        if (minute < 10) {
                            fixMinute = "0" + minute;
                        } else {
                            fixMinute = String.valueOf(minute);
                        }
                        if (hourOfDay < 10) {
                            fixHour = "0" +hourOfDay;
                        }else{
                            fixHour = String.valueOf(hourOfDay);
                        }
                        mTimeEnd = fixHour + ":" + fixMinute;
                        editTextWaktuSelesai.setText(mTimeEnd);
                    }
                },mHourEnd,mMinuteEnd,true);
                tpd.show();
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this,"up-to-bottom");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_reminder,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_reminder:
                hari = spinnerHari.getSelectedItem().toString().trim();
                mataKuliah = editTextMatakuliah.getText().toString().trim();
                ruangan = editTextRuangan.getText().toString().trim();
                dosen = editTextDosen.getText().toString().trim();
                String cekjam = editTextWaktu.getText().toString().trim();
                String cekjamSelesai = editTextWaktuSelesai.getText().toString().trim();
                if (TextUtils.isEmpty(hari)||TextUtils.isEmpty(mataKuliah)||TextUtils.isEmpty(ruangan)||TextUtils.isEmpty(dosen)||
                TextUtils.isEmpty(cekjam)||TextUtils.isEmpty(cekjamSelesai)){

                    Toasty.warning(this, "Data tidak lengkap",Toasty.LENGTH_SHORT).show();
                }else{
                    saveSchedule();
                    finish();
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSchedule() {

        //get TextView
        hari = spinnerHari.getSelectedItem().toString().trim();
        mataKuliah = editTextMatakuliah.getText().toString().trim();
        ruangan = editTextRuangan.getText().toString().trim();
        dosen = editTextDosen.getText().toString().trim();
        String cekjam = editTextWaktu.getText().toString().trim();
        String cekjamSelesai = editTextWaktuSelesai.getText().toString().trim();

        ContentValues values = new ContentValues();

        values.put(JadwalContract.JadwalEntry.COLUMN_HARI,hari);
        values.put(JadwalContract.JadwalEntry.COLUMN_MATAKULIAH,mataKuliah);
        values.put(JadwalContract.JadwalEntry.COLUMN_DOSEN,dosen);
        values.put(JadwalContract.JadwalEntry.COLUMN_RUANGAN,ruangan);
        values.put(JadwalContract.JadwalEntry.COLUMN_TANGGAL,mDate);
        values.put(JadwalContract.JadwalEntry.COLUMN_WAKTU,mTime);
        values.put(JadwalContract.JadwalEntry.COLUMN_WAKTU_SELESAI,mTimeEnd);

        //set Tanggal untuk notifikasi
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        long selectedTimestamp =  mCalendar.getTimeInMillis();

        mRepeatTime = 1*milWeek;

            Uri newUri = getContentResolver().insert(JadwalContract.JadwalEntry.CONTENT_URI,values);

            if (newUri == null){
                Toast.makeText(this, "Error Saving Schedule", Toast.LENGTH_SHORT).show();
            }

        new AlarmScheduler().setRepeatAlarm(this,selectedTimestamp, JadwalContract.JadwalEntry.CONTENT_URI,mRepeatTime);

        Toasty.success(this,"Schedule Saved",Toasty.LENGTH_SHORT).show();
    }

//    private void getCurrentId(){
//        String [] projection = {JadwalContract.JadwalEntry._ID};
//        Cursor cursor = getContentResolver().query(JadwalContract.JadwalEntry.CONTENT_URI,projection,null,null,null);
//        if (cursor.moveToNext()){
//            currentId = cursor.getLong(cursor.getColumnIndex(JadwalContract.JadwalEntry._ID)) + 1;
//        }else{
//            return;
//        }
//        cursor.close();
//        Log.i("Current ID",String.valueOf(currentId));
//    }
}
