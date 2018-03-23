package com.pedometerlibrary.data.source;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/12 16:03
 * <p>
 * PedometerPersistenceContract
 */
public class PedometerPersistenceContract {

    public static abstract class StepEntry {
        public static final String TABLE_NAME = "step";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_STEP = "step";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_DISTANCE = "distance";
        public static final String COLUMN_NAME_CALORIE = "calorie";
        public static final String COLUMN_NAME_SYNC_DATE = "sync_date";
        public static final String COLUMN_NAME_SYNC_DEVICE = "sync_device";
        public static final String COLUMN_NAME_CREATED_BY = "created_by";
        public static final String COLUMN_NAME_CREATED_DATE = "created_date";
        public static final String COLUMN_NAME_LAST_MODIFIED_BY = "last_modified_by";
        public static final String COLUMN_NAME_LAST_MODIFIED_DATE = "last_modified_date";
    }

    public static abstract class StepPartEntry {
        public static final String TABLE_NAME = "step_part";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_STEP_ID = "step_id";
        public static final String COLUMN_NAME_STEP = "step";
        public static final String COLUMN_NAME_DISTANCE = "distance";
        public static final String COLUMN_NAME_CALORIE = "calorie";
        public static final String COLUMN_NAME_FLAG = "flag";
        public static final String COLUMN_NAME_SYNC_DATE = "sync_date";
        public static final String COLUMN_NAME_SYNC_DEVICE = "sync_device";
        public static final String COLUMN_NAME_CREATED_BY = "created_by";
        public static final String COLUMN_NAME_CREATED_DATE = "created_date";
        public static final String COLUMN_NAME_LAST_MODIFIED_BY = "last_modified_by";
        public static final String COLUMN_NAME_LAST_MODIFIED_DATE = "last_modified_date";
    }

}
