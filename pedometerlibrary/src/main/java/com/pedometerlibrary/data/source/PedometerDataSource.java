package com.pedometerlibrary.data.source;

import android.support.annotation.NonNull;

import com.pedometerlibrary.data.bean.PedometerStep;
import com.pedometerlibrary.data.bean.PedometerStepPart;

import java.util.List;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/12 14:47
 * <p>
 * PedometerDataSource
 */
public interface PedometerDataSource {

    void putStep(@NonNull String date, @NonNull PedometerStep pedometerStep, @NonNull GetSourceCallback<String> callback);

    void getStep(@NonNull String date, @NonNull GetSourceCallback<PedometerStep> callback);

    void removeStep(@NonNull String date, @NonNull GetSourceCallback<String> callback);

    void putStepPart(@NonNull Long stepId, @NonNull PedometerStepPart pedometerStepPart, @NonNull GetSourceCallback<String> callback);

    void getStepPart(@NonNull Long stepId, @NonNull GetSourceCallback<List<PedometerStepPart>> callback);

    void removeStepPart(@NonNull Long stepId, @NonNull GetSourceCallback<String> callback);

    interface GetSourceCallback<T> {

        void onDataLoaded(T t);

        void onDataNotAvailable(String message);
    }

}
