package com.pedometerlibrary.data.bean;

import java.io.Serializable;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/12 15:09
 * <p>
 * 计步器记步部分
 */
public class PedometerStepPart implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 记步ID
     */
    private Long stepId;

    /**
     * 步数
     */
    private Integer step;

    /**
     * 里程 单位：（米）
     */
    private Double distance;

    /**
     * 卡路里
     */
    private Double calorie;

    /**
     * 标记
     * 0：记录中
     * 1：记录完成
     */
    private Integer flag;

    /**
     * 同步时间
     */
    private String syncDate;

    /**
     * 记步设备
     */
    private String syncDevice;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private String createdDate;

    /**
     * 修改人
     */
    private String lastModifiedBy;

    /**
     * 修改时间
     */
    private String lastModifiedDate;

    public PedometerStepPart() {
    }

    public PedometerStepPart(Long id, Long stepId, Integer step, Double distance, Double calorie, Integer flag, String syncDate, String syncDevice, String createdBy, String createdDate, String lastModifiedBy, String lastModifiedDate) {
        this.id = id;
        this.stepId = stepId;
        this.step = step;
        this.distance = distance;
        this.calorie = calorie;
        this.flag = flag;
        this.syncDate = syncDate;
        this.syncDevice = syncDevice;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStepId() {
        return stepId;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getCalorie() {
        return calorie;
    }

    public void setCalorie(Double calorie) {
        this.calorie = calorie;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(String syncDate) {
        this.syncDate = syncDate;
    }

    public String getSyncDevice() {
        return syncDevice;
    }

    public void setSyncDevice(String syncDevice) {
        this.syncDevice = syncDevice;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {
        return "PedometerStepPart{" +
                "id=" + id +
                ", stepId=" + stepId +
                ", step=" + step +
                ", distance=" + distance +
                ", calorie=" + calorie +
                ", flag=" + flag +
                ", syncDate='" + syncDate + '\'' +
                ", syncDevice='" + syncDevice + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", lastModifiedDate='" + lastModifiedDate + '\'' +
                '}';
    }
}
