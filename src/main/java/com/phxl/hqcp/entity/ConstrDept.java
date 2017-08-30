package com.phxl.hqcp.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.phxl.core.base.annotation.BaseSql;

@BaseSql(tableName="TB_CONSTR_DEPT", resultName="com.phxl.hqcp.dao.ConstrDeptMapper.BaseResultMap")
public class ConstrDept {
    private String constrDeptGuid;

    private Long qcOrgId;

    private Long orgId;

    private String qcScopeType;

    private String pYear;

    private String pYmd;

    private Date startTime;

    private Date endTime;

    private String auditId;

    private String auditName;

    private Date auditTime;

    private String auditFstate;

    private Date createTime;

    private String createUserId;

    private String createUserName;

    private Date modifyTime;

    private String modefileUserId;

    private String modefileUserNmae;

    private String tfRemark;

    private BigDecimal schedule;

    public String getConstrDeptGuid() {
        return constrDeptGuid;
    }

    public void setConstrDeptGuid(String constrDeptGuid) {
        this.constrDeptGuid = constrDeptGuid == null ? null : constrDeptGuid.trim();
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

    public BigDecimal getSchedule() {
        return schedule;
    }

    public void setSchedule(BigDecimal schedule) {
        this.schedule = schedule;
    }
}