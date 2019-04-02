package org.d3ifcool.zeitplannew.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class JadwalProvider extends ContentProvider {

    public static final String LOG_TAG = JadwalProvider.class.getSimpleName();

    private static final int REMINDER = 100;

    private static final int REMINDER_ID = 101;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(JadwalContract.CONTENT_AUTHORITY, JadwalContract.PATH_ZEITPLAN, REMINDER);

        sUriMatcher.addURI(JadwalContract.CONTENT_AUTHORITY, JadwalContract.PATH_ZEITPLAN+ "/#", REMINDER_ID);

    }

    private JadwalDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new JadwalDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match){
            case REMINDER:
                cursor = database.query(JadwalContract.JadwalEntry.TABLE_NAME,projection,selection,selectionArgs, null, null, sortOrder);
                break;
            case REMINDER_ID:
                selection = JadwalContract.JadwalEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(JadwalContract.JadwalEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI "+ uri );
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                return JadwalContract.JadwalEntry.CONTENT_LIST_TYPE;
            case REMINDER_ID:
                return JadwalContract.JadwalEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                return insertReminder(uri, values);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertReminder(Uri uri, ContentValues values) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(JadwalContract.JadwalEntry.TABLE_NAME, null, values);

        if (id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                rowsDeleted = database.delete(JadwalContract.JadwalEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REMINDER_ID:
                selection = JadwalContract.JadwalEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(JadwalContract.JadwalEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                return updateReminder(uri, values, selection, selectionArgs);
            case REMINDER_ID:
                selection = JadwalContract.JadwalEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateReminder(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateReminder(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(JadwalContract.JadwalEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
