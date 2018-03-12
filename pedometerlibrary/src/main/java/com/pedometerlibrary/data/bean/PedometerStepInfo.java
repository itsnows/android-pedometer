package com.pedometerlibrary.data.bean;

import java.io.Serializable;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/12 15:09
 * <p>
 * PedometerStepInfo
 */
public class PedometerStepInfo implements Serializable {

    /**
     * 记步详情主键
     */
    private Long id;

    /**
     * 记步外键
     */
    private Long stepId;

    /**
     * 步数
     */
    private Integer step;

    /**
     * 日期
     */
    private String date;

    /**
     * 里程 单位：（米）
     */
    private Double distance;

    /**
     * 卡路里
     */
    private Double calorie;

    /**
     * 记步设备
     */
    private String device;

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

    public PedometerStepInfo() {
    }

    public PedometerStepInfo(Long id, Long stepId, Integer step, String date, Double distance, Double calorie, String device, Integer flag, String syncDate, String createdBy, String createdDate, String lastModifiedBy, String lastModifiedDate) {
        this.id = id;
        this.stepId = stepId;
        this.step = step;
        this.date = date;
        this.distance = distance;
        this.calorie = calorie;
        this.device = device;
        this.flag = flag;
        this.syncDate = syncDate;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
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
        return "PedometerStepInfo{" +
                "id=" + id +
                ", stepId=" + stepId +
                ", step=" + step +
                ", date='" + date + '\'' +
                ", distance=" + distance +
                ", calorie=" + calorie +
                ", device='" + device + '\'' +
                ", flag=" + flag +
                ", syncDate='" + syncDate + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", lastModifiedDate='" + lastModifiedDate + '\'' +
                '}';
    }
}
