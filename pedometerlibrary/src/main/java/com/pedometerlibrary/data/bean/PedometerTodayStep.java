package com.pedometerlibrary.data.bean;

import java.util.List;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/13 12:04
 * <p>
 * PedometerTodayStep
 */
public class PedometerTodayStep {

    private PedometerStep step;

    private List<PedometerStepPart> stepParts;

    public PedometerTodayStep() {
    }

    public PedometerTodayStep(PedometerStep step, List<PedometerStepPart> stepParts) {
        this.step = step;
        this.stepParts = stepParts;
    }

    public PedometerStep getStep() {
        return step;
    }

    public void setStep(PedometerStep step) {
        this.step = step;
    }

    public List<PedometerStepPart> getStepParts() {
        return stepParts;
    }

    public void setStepParts(List<PedometerStepPart> stepParts) {
        this.stepParts = stepParts;
    }

    @Override
    public String toString() {
        return "PedometerTodayStep{" +
                "step=" + step +
                ", stepParts=" + stepParts +
                '}';
    }
}
