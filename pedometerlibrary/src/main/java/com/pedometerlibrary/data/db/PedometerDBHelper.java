package com.pedometerlibrary.data.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pedometerlibrary.data.source.PedometerPersistenceContract;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/12 15:12
 * <p>
 * 记步器数据库助手
 */
public class PedometerDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pedometer.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * 记步表
     */
    private static final String SQL_CREATE_STEP = "CREATE TABLE IF NOT EXISTS " +
            PedometerPersistenceContract.StepEntry.TABLE_NAME + " (" +
            PedometerPersistenceContract.StepEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
            PedometerPersistenceContract.StepEntry.COLUMN_NAME_STEP + " INTEGER," +
            PedometerPersistenceContract.StepEntry.COLUMN_NAME_DATE + " TEXT," +
            PedometerPersistenceContract.StepEntry.COLUMN_NAME_DISTANCE + " DOUBLE," +
            PedometerPersistenceContract.StepEntry.COLUMN_NAME_CALORIE + " DOUBLE," +
            PedometerPersistenceContract.StepEntry.COLUMN_NAME_SYNC_DATE + " TEXT," +
            PedometerPersistenceContract.StepEntry.COLUMN_NAME_SYNC_DEVICE + " TEXT," +
            PedometerPersistenceContract.StepEntry.COLUMN_NAME_CREATED_BY + " VARCHER(50)," +
            PedometerPersistenceContract.StepEntry.COLUMN_NAME_CREATED_DATE + " TEXT," +
            PedometerPersistenceContract.StepEntry.COLUMN_NAME_LAST_MODIFIED_BY + " VARCHER(50)," +
            PedometerPersistenceContract.StepEntry.COLUMN_NAME_LAST_MODIFIED_DATE + " TEXT)";

    /**
     * 记步部分表
     */
    private static final String SQL_CREATE_STEP_SPART = "CREATE TABLE IF NOT EXISTS " +
            PedometerPersistenceContract.StepPartEntry.TABLE_NAME + " (" +
            PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
            PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_STEP_ID + " INTEGER," +
            PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_STEP + " INTEGER," +
            PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_DISTANCE + " DOUBLE," +
            PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_CALORIE + " DOUBLE," +
            PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_FLAG + " INTEGER," +
            PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_SYNC_DATE + " TEXT," +
            PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_SYNC_DEVICE + " TEXT," +
            PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_CREATED_BY + " VARCHER(50)," +
            PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_CREATED_DATE + " TEXT," +
            PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_LAST_MODIFIED_BY + " VARCHER(50)," +
            PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_LAST_MODIFIED_DATE + " TEXT)";

    public PedometerDBHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public PedometerDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this(context, name, factory, version, null);
    }

    public PedometerDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STEP);
        db.execSQL(SQL_CREATE_STEP_SPART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PedometerPersistenceContract.StepEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PedometerPersistenceContract.StepPartEntry.TABLE_NAME);
    }
}
