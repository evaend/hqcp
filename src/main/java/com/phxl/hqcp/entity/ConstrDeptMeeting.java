package com.phxl.hqcp.entity;

import java.util.Date;

import com.phxl.core.base.annotation.BaseSql;

@BaseSql(tableName="TB_CONSTR_DEPT_MEETING", resultName="com.phxl.hqcp.dao.ConstrDeptMeetingMapper.BaseResultMap")
public class ConstrDeptMeeting {
    private String constrDeptMeetingGuid;

    private String constrDeptGuid;

    private String meetingName;

    private String meetingType;

    private Date meetingDate;
    
    private String meetingAddress;

    private String meetingSponsor;

    private Long meetingAllUserSum;

    private Long meetingDeptUserSum;
    
    private String tfRemark;
    
    private String meetingTime;

    public String getConstrDeptMeetingGuid() {
        return constrDeptMeetingGuid;
    }

    public void setConstrDeptMeetingGuid(String constrDeptMeetingGuid) {
        this.constrDeptMeetingGuid = constrDeptMeetingGuid == null ? null : constrDeptMeetingGuid.trim();
    }

    public String getConstrDeptGuid() {
        return constrDeptGuid;
    }

    public void setConstrDeptGuid(String constrDeptGuid) {
        this.constrDeptGuid = constrDeptGuid == null ? null : constrDeptGuid.trim();
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName == null ? null : meetingName.trim();
    }

    public String getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType == null ? null : meetingType.trim();
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingAddress() {
        return meetingAddress;
    }

    public void setMeetingAddress(String meetingAddress) {
        this.meetingAddress = meetingAddress == null ? null : meetingAddress.trim();
    }

    public String getMeetingSponsor() {
        return meetingSponsor;
    }

    public void setMeetingSponsor(String meetingSponsor) {
        this.meetingSponsor = meetingSponsor == null ? null : meetingSponsor.trim();
    }

    public Long getMeetingAllUserSum() {
        return meetingAllUserSum;
    }

    public void setMeetingAllUserSum(Long meetingAllUserSum) {
        this.meetingAllUserSum = meetingAllUserSum;
    }

    public Long getMeetingDeptUserSum() {
        return meetingDeptUserSum;
    }

    public void setMeetingDeptUserSum(Long meetingDeptUserSum) {
        this.meetingDeptUserSum = meetingDeptUserSum;
    }
    
    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getTfRemark() {
        return tfRemark;
    }

    public void setTfRemark(String tfRemark) {
        this.tfRemark = tfRemark;
    }
}