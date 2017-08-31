package com.phxl.hqcp.entity;

import java.util.Date;

import com.phxl.core.base.annotation.BaseSql;
@BaseSql(tableName="TD_STATIC_DATA", resultName="com.phxl.hqcp.dao.StaticDataMapper.BaseResultMap")
public class StaticData {
	 private String staticDataGuid;

	    private String tfCloCode;

	    private String tfCloName;

	    private String createUserid;

	    private Date createTime;

	    private String tfRemark;

	    private String staticId;

	    public String getStaticDataGuid() {
	        return staticDataGuid;
	    }

	    public void setStaticDataGuid(String staticDataGuid) {
	        this.staticDataGuid = staticDataGuid;
	    }

	    public String getTfCloCode() {
	        return tfCloCode;
	    }

	    public void setTfCloCode(String tfCloCode) {
	        this.tfCloCode = tfCloCode;
	    }

	    public String getTfCloName() {
	        return tfCloName;
	    }

	    public void setTfCloName(String tfCloName) {
	        this.tfCloName = tfCloName;
	    }

	    public String getCreateUserid() {
	        return createUserid;
	    }

	    public void setCreateUserid(String createUserid) {
	        this.createUserid = createUserid;
	    }

	    public Date getCreateTime() {
	        return createTime;
	    }

	    public void setCreateTime(Date createTime) {
	        this.createTime = createTime;
	    }

	    public String getTfRemark() {
	        return tfRemark;
	    }

	    public void setTfRemark(String tfRemark) {
	        this.tfRemark = tfRemark;
	    }

	    public String getStaticId() {
	        return staticId;
	    }

	    public void setStaticId(String staticId) {
	        this.staticId = staticId;
	    }
}