package com.phxl.hqcp.entity;

import java.util.Date;

import com.phxl.core.base.annotation.BaseSql;

@BaseSql(tableName="TD_STATIC_INFO", resultName="com.phxl.hqcp.dao.StaticInfoMapper.BaseResultMap")
public class StaticInfo {
    private String staticId;

    private String staticType;

    private String parentStaticId;

    private Long orgId;

    private String createUserid;

    private Date createTime;

    private Date modifyTime;

    private String tfTable;

    private String tfClo;

    private String tfComment;

    private String tfTableClo;

    private String fsort;

    public String getStaticId() {
        return staticId;
    }

    public void setStaticId(String staticId) {
        this.staticId = staticId == null ? null : staticId.trim();
    }

    public String getStaticType() {
        return staticType;
    }

    public void setStaticType(String staticType) {
        this.staticType = staticType == null ? null : staticType.trim();
    }

    public String getParentStaticId() {
        return parentStaticId;
    }

    public void setParentStaticId(String parentStaticId) {
        this.parentStaticId = parentStaticId == null ? null : parentStaticId.trim();
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid == null ? null : createUserid.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getTfTable() {
        return tfTable;
    }

    public void setTfTable(String tfTable) {
        this.tfTable = tfTable == null ? null : tfTable.trim();
    }

    public String getTfClo() {
        return tfClo;
    }

    public void setTfClo(String tfClo) {
        this.tfClo = tfClo == null ? null : tfClo.trim();
    }

    public String getTfComment() {
        return tfComment;
    }

    public void setTfComment(String tfComment) {
        this.tfComment = tfComment == null ? null : tfComment.trim();
    }

    public String getTfTableClo() {
        return tfTableClo;
    }

    public void setTfTableClo(String tfTableClo) {
        this.tfTableClo = tfTableClo == null ? null : tfTableClo.trim();
    }

    public String getFsort() {
        return fsort;
    }

    public void setFsort(String fsort) {
        this.fsort = fsort == null ? null : fsort.trim();
    }
}