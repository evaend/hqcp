package com.phxl.hqcp.entity;

import java.util.Date;

import com.phxl.core.base.annotation.BaseSql;
@BaseSql(tableName = "TD_ORG_INFO", resultName = "com.phxl.hqcp.dao.OrgInfoMapper.BaseResultMap")
public class OrgInfo {
    private Long orgId;

    private String orgName;

    private String orgCode;

    private String fqun;

    private String orgAlias;

    private String tfProvince;

    private String tfCity;

    private String tfDistrict;

    private String orgType;

    private String flag;

    private Long parentId;

    private String lxr;

    private String lxdh;

    private String tfAccessory;

    private String tfLogo;

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

    private String auditOrgCode;

    private String auditTfAccessory;
    
    //每年机构可能会变化的信息，重新上报
    private String hospitalLevel;
    private String hospitalType;
    private String hospitalProperty;
    private String hospitalTeaching;
    private Long planBedSum;
    private Long staffSum;
    private Long actualBedSum;
    private String pYear;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public String getFqun() {
        return fqun;
    }

    public void setFqun(String fqun) {
        this.fqun = fqun == null ? null : fqun.trim();
    }

    public String getOrgAlias() {
        return orgAlias;
    }

    public void setOrgAlias(String orgAlias) {
        this.orgAlias = orgAlias == null ? null : orgAlias.trim();
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

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType == null ? null : orgType.trim();
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr == null ? null : lxr.trim();
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh == null ? null : lxdh.trim();
    }

    public String getTfAccessory() {
        return tfAccessory;
    }

    public void setTfAccessory(String tfAccessory) {
        this.tfAccessory = tfAccessory == null ? null : tfAccessory.trim();
    }

    public String getTfLogo() {
        return tfLogo;
    }

    public void setTfLogo(String tfLogo) {
        this.tfLogo = tfLogo == null ? null : tfLogo.trim();
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

    public String getAuditOrgCode() {
        return auditOrgCode;
    }

    public void setAuditOrgCode(String auditOrgCode) {
        this.auditOrgCode = auditOrgCode == null ? null : auditOrgCode.trim();
    }

    public String getAuditTfAccessory() {
        return auditTfAccessory;
    }

    public void setAuditTfAccessory(String auditTfAccessory) {
        this.auditTfAccessory = auditTfAccessory == null ? null : auditTfAccessory.trim();
    }

	public String getHospitalLevel() {
		return hospitalLevel;
	}

	public void setHospitalLevel(String hospitalLevel) {
		this.hospitalLevel = hospitalLevel;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getHospitalProperty() {
		return hospitalProperty;
	}

	public void setHospitalProperty(String hospitalProperty) {
		this.hospitalProperty = hospitalProperty;
	}

	public String getHospitalTeaching() {
		return hospitalTeaching;
	}

	public void setHospitalTeaching(String hospitalTeaching) {
		this.hospitalTeaching = hospitalTeaching;
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
		this.pYear = pYear;
	}
}