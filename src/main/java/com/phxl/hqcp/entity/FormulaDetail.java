package com.phxl.hqcp.entity;

import java.math.BigDecimal;

import com.phxl.core.base.annotation.BaseSql;

@BaseSql(tableName="TB_FORMULA_DETAIL", resultName="com.phxl.hqcp.dao.FormulaDetailMapper.BaseResultMap")
public class FormulaDetail {
    private String indexDetailGuid;

    private String indexGuid;

    private Long fsort;

    private String indexDefine;

    private String indexMeaning;

    private String indexHelp;

    private String indexName;

    private String indexPCode;

    private BigDecimal indexValue;

    private String numeratorPCode;

    private Long numeratorValue;

    private String denominatorPCode;

    private Long denominatorValue;

    private Long groupId;

    private String groupName;

    private Long groupSort;

    private String tfRemark;
    
    public String getIndexDetailGuid() {
        return indexDetailGuid;
    }

    public void setIndexDetailGuid(String indexDetailGuid) {
        this.indexDetailGuid = indexDetailGuid == null ? null : indexDetailGuid.trim();
    }

    public String getIndexGuid() {
        return indexGuid;
    }

    public void setIndexGuid(String indexGuid) {
        this.indexGuid = indexGuid == null ? null : indexGuid.trim();
    }

    public Long getFsort() {
        return fsort;
    }

    public void setFsort(Long fsort) {
        this.fsort = fsort;
    }

    public String getIndexDefine() {
        return indexDefine;
    }

    public void setIndexDefine(String indexDefine) {
        this.indexDefine = indexDefine == null ? null : indexDefine.trim();
    }

    public String getIndexMeaning() {
        return indexMeaning;
    }

    public void setIndexMeaning(String indexMeaning) {
        this.indexMeaning = indexMeaning == null ? null : indexMeaning.trim();
    }

    public String getIndexHelp() {
        return indexHelp;
    }

    public void setIndexHelp(String indexHelp) {
        this.indexHelp = indexHelp == null ? null : indexHelp.trim();
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName == null ? null : indexName.trim();
    }

    public String getIndexPCode() {
        return indexPCode;
    }

    public void setIndexPCode(String indexPCode) {
        this.indexPCode = indexPCode == null ? null : indexPCode.trim();
    }

    public BigDecimal getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(BigDecimal indexValue) {
        this.indexValue = indexValue;
    }

    public String getNumeratorPCode() {
        return numeratorPCode;
    }

    public void setNumeratorPCode(String numeratorPCode) {
        this.numeratorPCode = numeratorPCode == null ? null : numeratorPCode.trim();
    }

    public Long getNumeratorValue() {
        return numeratorValue;
    }

    public void setNumeratorValue(Long numeratorValue) {
        this.numeratorValue = numeratorValue;
    }

    public String getDenominatorPCode() {
        return denominatorPCode;
    }

    public void setDenominatorPCode(String denominatorPCode) {
        this.denominatorPCode = denominatorPCode == null ? null : denominatorPCode.trim();
    }

    public Long getDenominatorValue() {
        return denominatorValue;
    }

    public void setDenominatorValue(Long denominatorValue) {
        this.denominatorValue = denominatorValue;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public Long getGroupSort() {
        return groupSort;
    }

    public void setGroupSort(Long groupSort) {
        this.groupSort = groupSort;
    }

    public String getTfRemark() {
        return tfRemark;
    }

    public void setTfRemark(String tfRemark) {
        this.tfRemark = tfRemark == null ? null : tfRemark.trim();
    }

}