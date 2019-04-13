package org.d3ifcool.zeitplannew.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public final class JadwalContract {
    private JadwalContract() {}

    public static final String CONTENT_AUTHORITY = "org.d3ifcool.zeitplannew";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ZEITPLAN = "zeitplan-path";

    public static final class JadwalEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_ZEITPLAN);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ZEITPLAN;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ZEITPLAN;

        public final static String TABLE_NAME = "jadwal";

        public final static String _ID = BaseColumns._ID;

        public static final String COLUMN_HARI = "hari";
        public static final String COLUMN_MATAKULIAH = "matakuliah";
        public static final String COLUMN_DOSEN = "dosen";
        public static final String COLUMN_RUANGAN = "ruangan";
        public static final String COLUMN_TANGGAL = "tanggal";
        public static final String COLUMN_WAKTU = "waktu";
        public static final String COLUMN_WAKTU_SELESAI = "waktuselesai";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }
}
