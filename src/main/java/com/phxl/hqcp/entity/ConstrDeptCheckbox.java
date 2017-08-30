package com.phxl.hqcp.entity;

import com.phxl.core.base.annotation.BaseSql;

@BaseSql(tableName="TB_CONSTR_DEPT_CHECKBOX", resultName="com.phxl.hqcp.dao.ConstrDeptCheckboxMapper.BaseResultMap")
public class ConstrDeptCheckbox {
    private String checkboxDetailGuid;

    private String constrDeptDetailGuid;

    private Long fsort;

    private String tfValue;

    private String checkboxType;

    private String tfCode;

    public String getCheckboxDetailGuid() {
        return checkboxDetailGuid;
    }

    public void setCheckboxDetailGuid(String checkboxDetailGuid) {
        this.checkboxDetailGuid = checkboxDetailGuid == null ? null : checkboxDetailGuid.trim();
    }

    public String getConstrDeptDetailGuid() {
        return constrDeptDetailGuid;
    }

    public void setConstrDeptDetailGuid(String constrDeptDetailGuid) {
        this.constrDeptDetailGuid = constrDeptDetailGuid == null ? null : constrDeptDetailGuid.trim();
    }

    public Long getFsort() {
        return fsort;
    }

    public void setFsort(Long fsort) {
        this.fsort = fsort;
    }

    public String getTfValue() {
        return tfValue;
    }

    public void setTfValue(String tfValue) {
        this.tfValue = tfValue == null ? null : tfValue.trim();
    }

    public String getCheckboxType() {
        return checkboxType;
    }

    public void setCheckboxType(String checkboxType) {
        this.checkboxType = checkboxType == null ? null : checkboxType.trim();
    }

    public String getTfCode() {
        return tfCode;
    }

    public void setTfCode(String tfCode) {
        this.tfCode = tfCode == null ? null : tfCode.trim();
    }
}