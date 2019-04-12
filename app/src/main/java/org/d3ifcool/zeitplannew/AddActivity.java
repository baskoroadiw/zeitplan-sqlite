package org.d3ifcool.zeitplannew;

import android.app.TimePickerDialog;
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

public class AddActivity extends AppCompatActivity {

    //constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    Toolbar mtoolbar;
    private Spinner spinnerHari;
    private EditText editTextMatakuliah, editTextDosen, editTextRuangan, editTextWaktu;
    private String mataKuliah, dosen, ruangan, hari, mDate, mTime;
    private long mRepeatTime;
    TimePicker timePicker;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private Calendar mCalendar;
    private Button buttonTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //initialize View
        spinnerHari = findViewById(R.id.spinnerHari);
        editTextMatakuliah = findViewById(R.id.editText_matakuliah);
        editTextDosen = findViewById(R.id.editText_dosen);
        editTextRuangan = findViewById(R.id.editText_ruangan);
        editTextWaktu = findViewById(R.id.editText_waktu);
        buttonTime = findViewById(R.id.button);

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

        //Toolbar
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle(R.string.title_activity_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //get Date
        mDate = mDay + "/" +mMonth+ "/" +mYear;
        Log.i("DATE NOW",mDate);

        //get Time
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                tpd.setTitle("Select Time");
                tpd.show();
            }
        });

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
                saveSchedule();
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(AddActivity.this);
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

        if (TextUtils.isEmpty(mataKuliah) || TextUtils.isEmpty(hari) || TextUtils.isEmpty(dosen) || TextUtils.isEmpty(ruangan) || TextUtils.isEmpty(cekjam)) {
            Toast.makeText(this, "Isian tidak lengkap!", Toast.LENGTH_SHORT).show();
        }

        ContentValues values = new ContentValues();

        values.put(JadwalContract.JadwalEntry.COLUMN_HARI,hari);
        values.put(JadwalContract.JadwalEntry.COLUMN_MATAKULIAH,mataKuliah);
        values.put(JadwalContract.JadwalEntry.COLUMN_DOSEN,dosen);
        values.put(JadwalContract.JadwalEntry.COLUMN_RUANGAN,ruangan);
        values.put(JadwalContract.JadwalEntry.COLUMN_TANGGAL,mDate);
        values.put(JadwalContract.JadwalEntry.COLUMN_WAKTU,mTime);

        //set Tanggal untuk notifikasi
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        long selectedTimestamp =  mCalendar.getTimeInMillis();

        mRepeatTime = 1*milMinute;

            Uri newUri = getContentResolver().insert(JadwalContract.JadwalEntry.CONTENT_URI,values);

            if (newUri == null){
                Toast.makeText(this, "Error Saving Schedule", Toast.LENGTH_SHORT).show();
            }
        
        new AlarmScheduler().setRepeatAlarm(getApplicationContext(),selectedTimestamp, JadwalContract.JadwalEntry.CONTENT_URI,mRepeatTime);

        Toast.makeText(getApplicationContext(), "Schedule Saved", Toast.LENGTH_SHORT).show();
    }
}
