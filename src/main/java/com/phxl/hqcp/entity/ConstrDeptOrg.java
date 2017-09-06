package com.phxl.hqcp.entity;

import java.util.Date;

import com.phxl.core.base.annotation.BaseSql;

@BaseSql(tableName="TB_CONSTR_DEPT_ORG", resultName="com.phxl.hqcp.dao.ConstrDeptOrgMapper.BaseResultMap")
public class ConstrDeptOrg {
    private String constrDeptOrgGuid;

    private String constrDeptGuid;

    private Long fsort;

    private String tfProvince;

    private String tfCity;

    private String tfDistrict;

    private String hospitalLevel;

    private String hospitalType;

    private String hospitalProperty;

    private String hospitalTeaching;

    private Long planBedSum;

    private Long staffSum;

    private Long actualBedSum;

    private String pYear;

    private String pYmd;

    private Date startTime;

    private Date endTime;

    private String auditId;

    private String auditName;

    private Date auditTime;

    private String auditFstate;

    private Long qcOrgId;

    private Long orgId;

    private String qcScopeType;

    public String getConstrDeptOrgGuid() {
        return constrDeptOrgGuid;
    }

    public void setConstrDeptOrgGuid(String constrDeptOrgGuid) {
        this.constrDeptOrgGuid = constrDeptOrgGuid == null ? null : constrDeptOrgGuid.trim();
    }

    public String getConstrDeptGuid() {
        return constrDeptGuid;
    }

    public void setConstrDeptGuid(String constrDeptGuid) {
        this.constrDeptGuid = constrDeptGuid == null ? null : constrDeptGuid.trim();
    }

    public Long getFsort() {
        return fsort;
    }

    public void setFsort(Long fsort) {
        this.fsort = fsort;
    }

    public String getTfProvince() {
        return tfProvince;
    }

    public void setTfProvince(String tfProvince) {
        this.tfProvince = tfProvince == null ? null : tfProvince.trim();
    }

    public String getTfCity() {
        return tfCity;
    }

    public void setTfCity(String tfCity) {
        this.tfCity = tfCity == null ? null : tfCity.trim();
    }

    public String getTfDistrict() {
        return tfDistrict;
    }

    public void setTfDistrict(String tfDistrict) {
        this.tfDistrict = tfDistrict == null ? null : tfDistrict.trim();
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

    public Long getPlanBedSum() {
        return planBedSum;
    }

    public void setPlanBedSum(Long planBedSum) {
        this.planBedSum = planBedSum;
    }

    public Long getStaffSum() {
        return staffSum;
    }

    public void setStaffSum(Long staffSum) {
        this.staffSum = staffSum;
    }

    public Long getActualBedSum() {
        return actualBedSum;
    }

    public void setActualBedSum(Long actualBedSum) {
        this.actualBedSum = actualBedSum;
    }

    public String getpYear() {
        return pYear;
    }

    public void setpYear(String pYear) {
        this.pYear = pYear == null ? null : pYear.trim();
    }

    public String getpYmd() {
        return pYmd;
    }

    public void setpYmd(String pYmd) {
        this.pYmd = pYmd == null ? null : pYmd.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId == null ? null : auditId.trim();
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName == null ? null : auditName.trim();
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditFstate() {
        return auditFstate;
    }

    public void setAuditFstate(String auditFstate) {
        this.auditFstate = auditFstate == null ? null : auditFstate.trim();
    }

    public Long getQcOrgId() {
        return qcOrgId;
    }

    public void setQcOrgId(Long qcOrgId) {
        this.qcOrgId = qcOrgId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getQcScopeType() {
        return qcScopeType;
    }

    public void setQcScopeType(String qcScopeType) {
        this.qcScopeType = qcScopeType == null ? null : qcScopeType.trim();
    }
}