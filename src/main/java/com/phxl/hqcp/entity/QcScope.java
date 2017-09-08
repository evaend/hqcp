package com.phxl.hqcp.entity;

import java.util.Date;

import com.phxl.core.base.annotation.BaseSql;

@BaseSql(tableName="TD_QC_SCOPE", resultName="com.phxl.hqcp.dao.QcScopeMapper.BaseResultMap")
public class QcScope {
    private String qcScopeGuid;

    private Long qcOrgId;

    private Long orgId;

    private String qcScopeType;

    private String pYear;

    private String pYmd;

    private Date startDate;

    private Date endDate;

    private Date createTime;

    private String createUserId;

    private String createUserName;

    private Date modifyTime;

    private String modefileUserId;

    private String modefileUserNmae;

    private String tfRemark;

    private String qcScopeSubType;

    public String getQcScopeGuid() {
        return qcScopeGuid;
    }

    public void setQcScopeGuid(String qcScopeGuid) {
        this.qcScopeGuid = qcScopeGuid == null ? null : qcScopeGuid.trim();
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName == null ? null : createUserName.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModefileUserId() {
        return modefileUserId;
    }

    public void setModefileUserId(String modefileUserId) {
        this.modefileUserId = modefileUserId == null ? null : modefileUserId.trim();
    }

    public String getModefileUserNmae() {
        return modefileUserNmae;
    }

    public void setModefileUserNmae(String modefileUserNmae) {
        this.modefileUserNmae = modefileUserNmae == null ? null : modefileUserNmae.trim();
    }

    public String getTfRemark() {
        return tfRemark;
    }

    public void setTfRemark(String tfRemark) {
        this.tfRemark = tfRemark == null ? null : tfRemark.trim();
    }

    public String getQcScopeSubType() {
        return qcScopeSubType;
    }

    public void setQcScopeSubType(String qcScopeSubType) {
        this.qcScopeSubType = qcScopeSubType == null ? null : qcScopeSubType.trim();
    }
}