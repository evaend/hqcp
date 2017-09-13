package com.phxl.hqcp.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    
    /*部门基本情况*/
    private String deptName;

    private String deptTypeName;

    private String deptTypeNameOther;

    private String deptParentName;
    
    private String deptParentNameOther;

    private String[] workScope;//部门业务管理范围-多选
    
    private String workScopeOther;

    private String[] workOther;//部门承担的其它工作-多选
    
    private String workMassName;
    
    private String workOtherName;
    
    /*部门相关业务*/
    private Long equipmentSum;

    private Long equipmentValue;

    private Long abEquipmentSum;

    private Long abEquipmentValue;

    private Long consuSum;

    private Long consuValue;

    private Long highConsuSum;

    private Long highConsuValue;
    
    private String[] logisticsScope;//医疗器械物流管理开展范围-多选

    private String[] logisticsType;//卫生材料医疗器械物流管理模式-多选
    
    private String logisticsTypeOther;
    
    /*部门人员、部门培训的数量*/
    private Integer userCount;
    
    private Integer meetingCount;
    
    /*关联的实体类*/
    private ConstrDeptInfo deptInfo;//建设科室-科室基本信息
    
    private ConstrDeptWork deptWork;//建设科室-科室业务
    
    private List<ConstrDeptUser> userList;//建设科室-科室人员
    
    private List<ConstrDeptMeeting> meetingList;//建设科室-科室会议
    
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
    
    public ConstrDeptInfo getDeptInfo() {
        return deptInfo;
    }

    public void setDeptInfo(ConstrDeptInfo deptInfo) {
        this.deptInfo = deptInfo;
    }
    
    public ConstrDeptWork getDeptWork() {
        return deptWork;
    }

    public void setDeptWork(ConstrDeptWork deptWork) {
        this.deptWork = deptWork;
    }
    
    public List<ConstrDeptUser> getUserList() {
        return userList;
    }

    public void setUserList(List<ConstrDeptUser> userList) {
        this.userList = userList;
    }

    public List<ConstrDeptMeeting> getMeetingList() {
        return meetingList;
    }

    public void setMeetingList(List<ConstrDeptMeeting> meetingList) {
        this.meetingList = meetingList;
    }
    
    /*附加的属性get/set方法*/
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptTypeName() {
        return deptTypeName;
    }

    public void setDeptTypeName(String deptTypeName) {
        this.deptTypeName = deptTypeName;
    }

    public String getDeptParentName() {
        return deptParentName;
    }

    public void setDeptParentName(String deptParentName) {
        this.deptParentName = deptParentName;
    }

    public String[] getWorkScope() {
        return workScope;
    }

    public void setWorkScope(String[] workScope) {
        this.workScope = workScope;
    }

    public String getWorkScopeOther() {
        return workScopeOther;
    }

    public void setWorkScopeOther(String workScopeOther) {
        this.workScopeOther = workScopeOther;
    }

    public String[] getWorkOther() {
        return workOther;
    }

    public void setWorkOther(String[] workOther) {
        this.workOther = workOther;
    }

    public String getWorkMassName() {
        return workMassName;
    }

    public void setWorkMassName(String workMassName) {
        this.workMassName = workMassName;
    }

    public String getWorkOtherName() {
        return workOtherName;
    }

    public void setWorkOtherName(String workOtherName) {
        this.workOtherName = workOtherName;
    }

    public Long getEquipmentSum() {
        return equipmentSum;
    }

    public void setEquipmentSum(Long equipmentSum) {
        this.equipmentSum = equipmentSum;
    }

    public Long getEquipmentValue() {
        return equipmentValue;
    }

    public void setEquipmentValue(Long equipmentValue) {
        this.equipmentValue = equipmentValue;
    }

    public Long getAbEquipmentSum() {
        return abEquipmentSum;
    }

    public void setAbEquipmentSum(Long abEquipmentSum) {
        this.abEquipmentSum = abEquipmentSum;
    }

    public Long getAbEquipmentValue() {
        return abEquipmentValue;
    }

    public void setAbEquipmentValue(Long abEquipmentValue) {
        this.abEquipmentValue = abEquipmentValue;
    }

    public Long getConsuSum() {
        return consuSum;
    }

    public void setConsuSum(Long consuSum) {
        this.consuSum = consuSum;
    }

    public Long getConsuValue() {
        return consuValue;
    }

    public void setConsuValue(Long consuValue) {
        this.consuValue = consuValue;
    }

    public Long getHighConsuSum() {
        return highConsuSum;
    }

    public void setHighConsuSum(Long highConsuSum) {
        this.highConsuSum = highConsuSum;
    }

    public Long getHighConsuValue() {
        return highConsuValue;
    }

    public void setHighConsuValue(Long highConsuValue) {
        this.highConsuValue = highConsuValue;
    }

    public String[] getLogisticsScope() {
        return logisticsScope;
    }

    public void setLogisticsScope(String[] logisticsScope) {
        this.logisticsScope = logisticsScope;
    }

    public String[] getLogisticsType() {
        return logisticsType;
    }

    public void setLogisticsType(String[] logisticsType) {
        this.logisticsType = logisticsType;
    }

    public String getLogisticsTypeOther() {
        return logisticsTypeOther;
    }

    public void setLogisticsTypeOther(String logisticsTypeOther) {
        this.logisticsTypeOther = logisticsTypeOther;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getMeetingCount() {
        return meetingCount;
    }

    public void setMeetingCount(Integer meetingCount) {
        this.meetingCount = meetingCount;
    }

    public String getDeptTypeNameOther() {
        return deptTypeNameOther;
    }

    public void setDeptTypeNameOther(String deptTypeNameOther) {
        this.deptTypeNameOther = deptTypeNameOther;
    }

    public String getDeptParentNameOther() {
        return deptParentNameOther;
    }

    public void setDeptParentNameOther(String deptParentNameOther) {
        this.deptParentNameOther = deptParentNameOther;
    }
}