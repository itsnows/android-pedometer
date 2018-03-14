package com.pedometerlibrary.data.source;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pedometerlibrary.data.bean.PedometerStep;
import com.pedometerlibrary.data.bean.PedometerStepPart;
import com.pedometerlibrary.data.db.PedometerContentProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/12 14:50
 * <p>
 * 计步器数据管理
 */
public class PedometerDataManager implements PedometerDataSource {
    private static final String TAG = PedometerDataManager.class.getSimpleName();
    private Context context;

    public PedometerDataManager(Context context) {
        this.context = context;
    }

    @Override
    public void putStep(@NonNull String date, @NonNull PedometerStep pedometerStep, @NonNull GetSourceCallback<String> callback) {
        Cursor cursor = null;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PedometerPersistenceContract.StepEntry.COLUMN_NAME_ID, pedometerStep.getId());
            contentValues.put(PedometerPersistenceContract.StepEntry.COLUMN_NAME_STEP, pedometerStep.getStep());
            contentValues.put(PedometerPersistenceContract.StepEntry.COLUMN_NAME_DATE, pedometerStep.getDate());
            contentValues.put(PedometerPersistenceContract.StepEntry.COLUMN_NAME_DISTANCE, pedometerStep.getDistance());
            contentValues.put(PedometerPersistenceContract.StepEntry.COLUMN_NAME_CALORIE, pedometerStep.getCalorie());
            contentValues.put(PedometerPersistenceContract.StepEntry.COLUMN_NAME_SYNC_DATE, pedometerStep.getSyncDate());
            contentValues.put(PedometerPersistenceContract.StepEntry.COLUMN_NAME_SYNC_DEVICE, pedometerStep.getSyncDevice());
            contentValues.put(PedometerPersistenceContract.StepEntry.COLUMN_NAME_CREATED_BY, pedometerStep.getCreatedBy());
            contentValues.put(PedometerPersistenceContract.StepEntry.COLUMN_NAME_CREATED_DATE, pedometerStep.getCreatedDate());
            contentValues.put(PedometerPersistenceContract.StepEntry.COLUMN_NAME_LAST_MODIFIED_BY, pedometerStep.getLastModifiedBy());
            contentValues.put(PedometerPersistenceContract.StepEntry.COLUMN_NAME_LAST_MODIFIED_DATE, pedometerStep.getLastModifiedDate());
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = Uri.fromParts(PedometerContentProvider.SCHEME, PedometerContentProvider.AUTHORITY + PedometerContentProvider.SEPARATOR + PedometerContentProvider.STEP_PATH, null);
            String selection = PedometerPersistenceContract.StepEntry.COLUMN_NAME_DATE + "=?";
            String[] selectionArgs = new String[]{date};
            cursor = contentResolver.query(uri, null, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToNext()) {
                contentResolver.update(uri, contentValues, selection, selectionArgs);
                Log.d(TAG, "Step update");
            } else {
                contentResolver.insert(uri, contentValues);
                Log.d(TAG, "Step insert");
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onDataNotAvailable(e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void getStep(@NonNull String date, @NonNull GetSourceCallback<PedometerStep> callback) {
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = Uri.fromParts(PedometerContentProvider.SCHEME, PedometerContentProvider.AUTHORITY + PedometerContentProvider.SEPARATOR + PedometerContentProvider.STEP_PATH, null);
            String selection = PedometerPersistenceContract.StepEntry.COLUMN_NAME_DATE + "=?";
            String[] selectionArgs = new String[]{date};
            cursor = contentResolver.query(uri, null, selection, selectionArgs, null);
            while (cursor.moveToNext()) {
                Long id = cursor.getLong(cursor.getColumnIndex(PedometerPersistenceContract.StepEntry.COLUMN_NAME_ID));
                Integer step = cursor.getInt(cursor.getColumnIndex(PedometerPersistenceContract.StepEntry.COLUMN_NAME_STEP));
                date = cursor.getString(cursor.getColumnIndex(PedometerPersistenceContract.StepEntry.COLUMN_NAME_DATE));
                Double distance = cursor.getDouble(cursor.getColumnIndex(PedometerPersistenceContract.StepEntry.COLUMN_NAME_DISTANCE));
                Double calorie = cursor.getDouble(cursor.getColumnIndex(PedometerPersistenceContract.StepEntry.COLUMN_NAME_CALORIE));
                String syncDate = cursor.getString(cursor.getColumnIndex(PedometerPersistenceContract.StepEntry.COLUMN_NAME_SYNC_DATE));
                String syncDevice = cursor.getString(cursor.getColumnIndex(PedometerPersistenceContract.StepEntry.COLUMN_NAME_SYNC_DEVICE));
                String createdBy = cursor.getString(cursor.getColumnIndex(PedometerPersistenceContract.StepEntry.COLUMN_NAME_CREATED_BY));
                String createdDate = cursor.getString(cursor.getColumnIndex(PedometerPersistenceContract.StepEntry.COLUMN_NAME_CREATED_DATE));
                String lastModifiedBy = cursor.getString(cursor.getColumnIndex(PedometerPersistenceContract.StepEntry.COLUMN_NAME_LAST_MODIFIED_BY));
                String lastModifiedDate = cursor.getString(cursor.getColumnIndex(PedometerPersistenceContract.StepEntry.COLUMN_NAME_LAST_MODIFIED_DATE));
                callback.onDataLoaded(new PedometerStep(id, step, date, distance, calorie, syncDate, syncDevice, createdBy, createdDate, lastModifiedBy, lastModifiedDate));
                return;
            }
            callback.onDataNotAvailable("暂无数据");
        } catch (Exception e) {
            e.printStackTrace();
            callback.onDataNotAvailable(e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void removeStep(@NonNull String date, @NonNull GetSourceCallback<String> callback) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = Uri.fromParts(PedometerContentProvider.SCHEME, PedometerContentProvider.AUTHORITY + PedometerContentProvider.SEPARATOR + PedometerContentProvider.STEP_PATH, null);
            String selection = PedometerPersistenceContract.StepEntry.COLUMN_NAME_DATE + "=?";
            String[] selectionArgs = new String[]{date};
            contentResolver.delete(uri, selection, selectionArgs);
            callback.onDataLoaded("删除数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            callback.onDataNotAvailable(e.getMessage());
        }
    }

    @Override
    public void putStepPart(@NonNull Long stepId, @NonNull PedometerStepPart pedometerStepPart, @NonNull GetSourceCallback<String> callback) {
        Cursor cursor = null;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_ID, pedometerStepPart.getId());
            contentValues.put(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_STEP_ID, pedometerStepPart.getId());
            contentValues.put(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_STEP, pedometerStepPart.getStep());
            contentValues.put(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_DISTANCE, pedometerStepPart.getDistance());
            contentValues.put(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_CALORIE, pedometerStepPart.getCalorie());
            contentValues.put(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_FLAG, pedometerStepPart.getCalorie());
            contentValues.put(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_SYNC_DATE, pedometerStepPart.getFlag());
            contentValues.put(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_SYNC_DEVICE, pedometerStepPart.getSyncDevice());
            contentValues.put(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_CREATED_BY, pedometerStepPart.getCreatedBy());
            contentValues.put(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_CREATED_DATE, pedometerStepPart.getCreatedDate());
            contentValues.put(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_LAST_MODIFIED_BY, pedometerStepPart.getLastModifiedBy());
            contentValues.put(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_LAST_MODIFIED_DATE, pedometerStepPart.getLastModifiedDate());
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = Uri.fromParts(PedometerContentProvider.SCHEME, PedometerContentProvider.AUTHORITY + PedometerContentProvider.SEPARATOR + PedometerContentProvider.STEP_PATH, null);
            String selection = PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_STEP_ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(stepId)};
            cursor = contentResolver.query(uri, null, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToNext()) {
                contentResolver.update(uri, contentValues, selection, selectionArgs);
                Log.d(TAG, "Step update");
            } else {
                contentResolver.insert(uri, contentValues);
                Log.d(TAG, "Step insert");
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onDataNotAvailable(e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void getStepPart(@NonNull Long stepId, @NonNull GetSourceCallback<List<PedometerStepPart>> callback) {
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = Uri.fromParts(PedometerContentProvider.SCHEME, PedometerContentProvider.AUTHORITY + PedometerContentProvider.SEPARATOR + PedometerContentProvider.STEP_PATH, null);
            String selection = PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_STEP_ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(stepId)};
            cursor = contentResolver.query(uri, null, selection, selectionArgs, null);
            List<PedometerStepPart> pedometerStepParts = new ArrayList<>(0);
            while (cursor.moveToNext()) {
                Long id = cursor.getLong(cursor.getColumnIndex(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_ID));
                stepId = cursor.getLong(cursor.getColumnIndex(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_ID));
                Integer step = cursor.getInt(cursor.getColumnIndex(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_STEP));
                Double distance = cursor.getDouble(cursor.getColumnIndex(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_DISTANCE));
                Double calorie = cursor.getDouble(cursor.getColumnIndex(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_CALORIE));
                Integer flag = cursor.getInt(cursor.getColumnIndex(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_FLAG));
                String syncDate = cursor.getString(cursor.getColumnIndex(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_SYNC_DATE));
                String syncDevice = cursor.getString(cursor.getColumnIndex(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_SYNC_DEVICE));
                String createdBy = cursor.getString(cursor.getColumnIndex(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_CREATED_BY));
                String createdDate = cursor.getString(cursor.getColumnIndex(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_CREATED_DATE));
                String lastModifiedBy = cursor.getString(cursor.getColumnIndex(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_LAST_MODIFIED_BY));
                String lastModifiedDate = cursor.getString(cursor.getColumnIndex(PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_LAST_MODIFIED_DATE));
                pedometerStepParts.add(new PedometerStepPart(id, stepId, step, distance, calorie, flag, syncDate, syncDevice, createdBy, createdDate, lastModifiedBy, lastModifiedDate));
            }
            callback.onDataLoaded(pedometerStepParts);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onDataNotAvailable(e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void removeStepPart(@NonNull Long stepId, @NonNull GetSourceCallback<String> callback) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = Uri.fromParts(PedometerContentProvider.SCHEME, PedometerContentProvider.AUTHORITY + PedometerContentProvider.SEPARATOR + PedometerContentProvider.STEP_PATH, null);
            String selection = PedometerPersistenceContract.StepPartEntry.COLUMN_NAME_STEP_ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(stepId)};
            contentResolver.delete(uri, selection, selectionArgs);
            callback.onDataLoaded("删除数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            callback.onDataNotAvailable(e.getMessage());
        }
    }

}
