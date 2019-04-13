package org.d3ifcool.zeitplannew.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JadwalDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "zeitplan.db";

    private static final int DATABASE_VERSION = 1;

    public JadwalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ZEITPLAN_TABLE = "CREATE TABLE " +
                JadwalContract.JadwalEntry.TABLE_NAME + " (" +
                JadwalContract.JadwalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                JadwalContract.JadwalEntry.COLUMN_HARI + " TEXT, " +
                JadwalContract.JadwalEntry.COLUMN_MATAKULIAH + " TEXT, " +
                JadwalContract.JadwalEntry.COLUMN_DOSEN + " TEXT, " +
                JadwalContract.JadwalEntry.COLUMN_RUANGAN + " TEXT, " +
                JadwalContract.JadwalEntry.COLUMN_TANGGAL + " TEXT, " +
                JadwalContract.JadwalEntry.COLUMN_WAKTU + " TEXT, " +
                JadwalContract.JadwalEntry.COLUMN_WAKTU_SELESAI + " TEXT, " +
                JadwalContract.JadwalEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_ZEITPLAN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + JadwalContract.JadwalEntry.TABLE_NAME);
        onCreate(db);
    }
}
