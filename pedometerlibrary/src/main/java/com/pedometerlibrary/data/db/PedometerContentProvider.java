package com.pedometerlibrary.data.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pedometerlibrary.data.source.PedometerPersistenceContract;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2017/9/21 9:29
 * <p>
 * PedometerContentProvider
 */

public class PedometerContentProvider extends ContentProvider {
    private static final String TAG = PedometerContentProvider.class.getSimpleName();
    private static final String AUTHORITY = "com.pedometerlibrary.PedometerContentProvider";
    private static final int STEP_PATH = 0;
    private static final int STEPINFO_PATH = 1;
    private UriMatcher uriMatcher;
    private PedometerDatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "step", STEP_PATH);
        uriMatcher.addURI(AUTHORITY, "stepPartdcx", STEPINFO_PATH);
        dbHelper = new PedometerDatabaseHelper(getContext());
        return false;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.v(TAG, "PedometerContentProvider low memory");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case STEP_PATH:
                cursor = db.query(PedometerPersistenceContract.StepEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
                break;
            case STEPINFO_PATH:
                cursor = db.query(PedometerPersistenceContract.StepPartEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String type = null;
        switch (uriMatcher.match(uri)) {
            case STEP_PATH:
                type = "vnd.android.cursor.dir/step";
                break;
            case STEPINFO_PATH:
                type = "vnd.android.cursor.dir/stepinfo";
                break;
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri insertUri = null;
        long id = -1;
        switch (uriMatcher.match(uri)) {
            case STEP_PATH:
                id = db.insert(PedometerPersistenceContract.StepEntry.TABLE_NAME, null, contentValues);
                insertUri = ContentUris.withAppendedId(uri, id);
                break;
            case STEPINFO_PATH:
                id = db.insert(PedometerPersistenceContract.StepPartEntry.TABLE_NAME, null, contentValues);
                insertUri = ContentUris.withAppendedId(uri, id);
                break;
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
        return insertUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case STEP_PATH:
                count = db.delete(PedometerPersistenceContract.StepEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case STEPINFO_PATH:
                count = db.delete(PedometerPersistenceContract.StepPartEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case STEP_PATH:
                count = db.update(PedometerPersistenceContract.StepEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case STEPINFO_PATH:
                count = db.update(PedometerPersistenceContract.StepPartEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
        return count;
    }
}
