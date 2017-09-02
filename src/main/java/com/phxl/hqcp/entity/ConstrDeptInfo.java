package com.phxl.hqcp.entity;

import java.util.List;
import java.util.Map;

import com.phxl.core.base.annotation.BaseSql;

@BaseSql(tableName="TB_CONSTR_DEPT_INFO", resultName="com.phxl.hqcp.dao.ConstrDeptInfoMapper.BaseResultMap")
public class ConstrDeptInfo {
    private String constrDeptInfoGuid;

    private String constrDeptGuid;

    private Long fsort;

    private String deptName;

    private String deptNameRemark;

    private String deptTypeName;

    private String deptParentName;

    private String deptWorkScope;

    private String deptWorkOther;
    
    private List<Map<String, Object>> workScope;
    
    private List<Map<String, Object>> workOther;

    public String getConstrDeptInfoGuid() {
        return constrDeptInfoGuid;
    }

    public void setConstrDeptInfoGuid(String constrDeptInfoGuid) {
        this.constrDeptInfoGuid = constrDeptInfoGuid == null ? null : constrDeptInfoGuid.trim();
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    public String getDeptNameRemark() {
        return deptNameRemark;
    }

    public void setDeptNameRemark(String deptNameRemark) {
        this.deptNameRemark = deptNameRemark == null ? null : deptNameRemark.trim();
    }

    public String getDeptTypeName() {
        return deptTypeName;
    }

    public void setDeptTypeName(String deptTypeName) {
        this.deptTypeName = deptTypeName == null ? null : deptTypeName.trim();
    }

    public String getDeptParentName() {
        return deptParentName;
    }

    public void setDeptParentName(String deptParentName) {
        this.deptParentName = deptParentName == null ? null : deptParentName.trim();
    }

    public String getDeptWorkScope() {
        return deptWorkScope;
    }

    public void setDeptWorkScope(String deptWorkScope) {
        this.deptWorkScope = deptWorkScope == null ? null : deptWorkScope.trim();
    }

    public String getDeptWorkOther() {
        return deptWorkOther;
    }

    public void setDeptWorkOther(String deptWorkOther) {
        this.deptWorkOther = deptWorkOther == null ? null : deptWorkOther.trim();
    }
    
    public List<Map<String, Object>> getWorkScope() {
        return workScope;
    }

    public void setWorkScope(List<Map<String, Object>> workScope) {
        this.workScope = workScope;
    }

    public List<Map<String, Object>> getWorkOther() {
        return workOther;
    }

    public void setWorkOther(List<Map<String, Object>> workOther) {
        this.workOther = workOther;
    }
}