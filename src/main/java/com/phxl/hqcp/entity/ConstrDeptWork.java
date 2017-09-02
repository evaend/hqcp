package com.phxl.hqcp.entity;

import java.util.List;
import java.util.Map;

import com.phxl.core.base.annotation.BaseSql;

@BaseSql(tableName="TB_CONSTR_DEPT_WORK", resultName="com.phxl.hqcp.dao.ConstrDeptWorkMapper.BaseResultMap")
public class ConstrDeptWork {
    private String constrDeptWorkGuid;

    private String constrDeptGuid;

    private Long fsort;

    private Long equipmentSum;

    private Long equipmentValue;

    private Long abEquipmentSum;

    private Long abEquipmentValue;

    private Long consuSum;

    private Long consuValue;

    private Long highConsuSum;

    private Long highConsuValue;

    private String logisticsScope;

    private String logisticsType;
    
    private List<Map<String, Object>> logisticsScopeList;
    
    private List<Map<String, Object>> logisticsTypeList;

    public String getConstrDeptWorkGuid() {
        return constrDeptWorkGuid;
    }

    public void setConstrDeptWorkGuid(String constrDeptWorkGuid) {
        this.constrDeptWorkGuid = constrDeptWorkGuid == null ? null : constrDeptWorkGuid.trim();
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

    public String getLogisticsScope() {
        return logisticsScope;
    }

    public void setLogisticsScope(String logisticsScope) {
        this.logisticsScope = logisticsScope == null ? null : logisticsScope.trim();
    }

    public String getLogisticsType() {
        return logisticsType;
    }

    public void setLogisticsType(String logisticsType) {
        this.logisticsType = logisticsType == null ? null : logisticsType.trim();
    }
    
    public List<Map<String, Object>> getLogisticsScopeList() {
        return logisticsScopeList;
    }

    public void setLogisticsScopeList(List<Map<String, Object>> logisticsScopeList) {
        this.logisticsScopeList = logisticsScopeList;
    }

    public List<Map<String, Object>> getLogisticsTypeList() {
        return logisticsTypeList;
    }

    public void setLogisticsTypeList(List<Map<String, Object>> logisticsTypeList) {
        this.logisticsTypeList = logisticsTypeList;
    }
}