package com.phxl.hqcp.entity;

import com.phxl.core.base.annotation.BaseSql;

@BaseSql(tableName = "TD_HOSPITAL_INFO", resultName = "com.phxl.hqcp.dao.HospitalInfoMapper.BaseResultMap")
public class HospitalInfo {
    private Long orgId;

    private String hospitalLevel;

    private String hospitalType;

    private String hospitalProperty;

    private String hospitalTeaching;

    private Long fsort;

    private Long planBedSum;

    private Long actualBedSum;

    private Long staffSum;

    private String tfRemark;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getHospitalLevel() {
        return hospitalLevel;
    }

    public void setHospitalLevel(String hospitalLevel) {
        this.hospitalLevel = hospitalLevel == null ? null : hospitalLevel.trim();
    }

    public String getHospitalType() {
        return hospitalType;
    }

    public void setHospitalType(String hospitalType) {
        this.hospitalType = hospitalType == null ? null : hospitalType.trim();
    }

    public String getHospitalProperty() {
        return hospitalProperty;
    }

    public void setHospitalProperty(String hospitalProperty) {
        this.hospitalProperty = hospitalProperty == null ? null : hospitalProperty.trim();
    }

    public String getHospitalTeaching() {
        return hospitalTeaching;
    }

    public void setHospitalTeaching(String hospitalTeaching) {
        this.hospitalTeaching = hospitalTeaching == null ? null : hospitalTeaching.trim();
    }

    public Long getFsort() {
        return fsort;
    }

    public void setFsort(Long fsort) {
        this.fsort = fsort;
    }

    public Long getPlanBedSum() {
        return planBedSum;
    }

    public void setPlanBedSum(Long planBedSum) {
        this.planBedSum = planBedSum;
    }

    public Long getActualBedSum() {
        return actualBedSum;
    }

    public void setActualBedSum(Long actualBedSum) {
        this.actualBedSum = actualBedSum;
    }

    public Long getStaffSum() {
        return staffSum;
    }

    public void setStaffSum(Long staffSum) {
        this.staffSum = staffSum;
    }

    public String getTfRemark() {
        return tfRemark;
    }

    public void setTfRemark(String tfRemark) {
        this.tfRemark = tfRemark == null ? null : tfRemark.trim();
    }
}