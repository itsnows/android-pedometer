package com.pedometerlibrary.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author: SXF
 * E-mail: xue.com.fei@outlook.com
 * CreatedTime: 2018/3/12 15:09
 * <p>
 * 计步器记步
 */
public class PedometerStep implements Serializable {

    /**
     *ID
     */
    private Long id;

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
     * 同步时间
     */
    private String syncDate;

    /**
     * 同步设备
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

    public PedometerStep() {
    }

    public PedometerStep(Long id, Integer step, String date, Double distance, Double calorie, String syncDate, String syncDevice, String createdBy, String createdDate, String lastModifiedBy, String lastModifiedDate) {
        this.id = id;
        this.step = step;
        this.date = date;
        this.distance = distance;
        this.calorie = calorie;
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
        return "PedometerStep{" +
                "id=" + id +
                ", step=" + step +
                ", date='" + date + '\'' +
                ", distance=" + distance +
                ", calorie=" + calorie +
                ", syncDate='" + syncDate + '\'' +
                ", syncDevice='" + syncDevice + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", lastModifiedDate='" + lastModifiedDate + '\'' +
                '}';
    }
}
