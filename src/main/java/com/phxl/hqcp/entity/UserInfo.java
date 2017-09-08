package com.phxl.hqcp.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phxl.core.base.annotation.BaseSql;
@BaseSql(tableName="TS_USER_INFO", resultName="com.phxl.hqcp.dao.UserInfoMapper.BaseResultMap")
public class UserInfo {
    private String userId;

    private String userNo;

    @JsonIgnore
    private String pwd;

    private String userName;

    private String sortNo;

    private String userLevel;

    private String mobilePhone;

    private String userAddress;

    private String tfFlag;

    private String ftypeUser;

    private Long orgId;

    private String auditId;

    private String auditName;

    private Date auditTime;

    private String auditFstate;

    private String tfRemark;
    
    private String orgType;

    private Date modifyTime;

    private Date createTime;

    private String createUserid;

    private String modifyUserid;
    
    private String orgName;
    
    private String confirmPwd;
    private String auditOrgCode;
    private String auditTfAccessory;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo == null ? null : userNo.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo == null ? null : sortNo.trim();
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel == null ? null : userLevel.trim();
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress == null ? null : userAddress.trim();
    }

    public String getTfFlag() {
        return tfFlag;
    }

    public void setTfFlag(String tfFlag) {
        this.tfFlag = tfFlag == null ? null : tfFlag.trim();
    }

    public String getFtypeUser() {
        return ftypeUser;
    }

    public void setFtypeUser(String ftypeUser) {
        this.ftypeUser = ftypeUser == null ? null : ftypeUser.trim();
    }

    public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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

    public String getTfRemark() {
        return tfRemark;
    }

    public void setTfRemark(String tfRemark) {
        this.tfRemark = tfRemark == null ? null : tfRemark.trim();
    }

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserid() {
		return createUserid;
	}

	public void setCreateUserid(String createUserid) {
		this.createUserid = createUserid;
	}

	public String getModifyUserid() {
		return modifyUserid;
	}

	public void setModifyUserid(String modifyUserid) {
		this.modifyUserid = modifyUserid;
	}

	public String getConfirmPwd() {
		return confirmPwd;
	}

	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}

	public String getAuditOrgCode() {
		return auditOrgCode;
	}

	public void setAuditOrgCode(String auditOrgCode) {
		this.auditOrgCode = auditOrgCode;
	}

	public String getAuditTfAccessory() {
		return auditTfAccessory;
	}

	public void setAuditTfAccessory(String auditTfAccessory) {
		this.auditTfAccessory = auditTfAccessory;
	}
	
}