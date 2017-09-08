package com.phxl.hqcp.entity;

import java.util.Date;

import com.phxl.core.base.annotation.BaseSql;


@BaseSql(tableName="TB_FORMULA_TEMPLATE", resultName="com.phxl.hqcp.dao.FormulaTemplateMapper.BaseResultMap")
public class FormulaTemplate {
    private String indexGuid;

    private String indexCode;

    private String qcScopeType;

    private Long qcOrgId;

    private String pYear;

    private String pYmd;

    private Date startDate;

    private Date endDate;
    
    private String qcScopeSubType;

    public String getIndexGuid() {
        return indexGuid;
    }

    public void setIndexGuid(String indexGuid) {
        this.indexGuid = indexGuid == null ? null : indexGuid.trim();
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode == null ? null : indexCode.trim();
    }

    public String getQcScopeType() {
        return qcScopeType;
    }

    public void setQcScopeType(String qcScopeType) {
        this.qcScopeType = qcScopeType == null ? null : qcScopeType.trim();
    }

    public Long getQcOrgId() {
        return qcOrgId;
    }

    public void setQcOrgId(Long qcOrgId) {
        this.qcOrgId = qcOrgId;
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

	public String getQcScopeSubType() {
		return qcScopeSubType;
	}

	public void setQcScopeSubType(String qcScopeSubType) {
		this.qcScopeSubType = qcScopeSubType;
	}
    
}