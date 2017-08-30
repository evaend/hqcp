package com.phxl.hqcp.entity;

import com.phxl.core.base.annotation.BaseSql;

@BaseSql(tableName="TB_CONSTR_DEPT_USER", resultName="com.phxl.hqcp.dao.ConstrDeptUserMapper.BaseResultMap")
public class ConstrDeptUser {
    private String constrDeptUserGuid;

    private String constrDeptGuid;

    private String fname;

    private String gender;

    private String birthChar;

    private String technicalTitlesA;

    private String technicalTitlesB;

    private String postName;

    private Short postAge;

    private String highestEducation;

    private String majorName;

    public String getConstrDeptUserGuid() {
        return constrDeptUserGuid;
    }

    public void setConstrDeptUserGuid(String constrDeptUserGuid) {
        this.constrDeptUserGuid = constrDeptUserGuid == null ? null : constrDeptUserGuid.trim();
    }

    public String getConstrDeptGuid() {
        return constrDeptGuid;
    }

    public void setConstrDeptGuid(String constrDeptGuid) {
        this.constrDeptGuid = constrDeptGuid == null ? null : constrDeptGuid.trim();
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname == null ? null : fname.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getBirthChar() {
        return birthChar;
    }

    public void setBirthChar(String birthChar) {
        this.birthChar = birthChar == null ? null : birthChar.trim();
    }

    public String getTechnicalTitlesA() {
        return technicalTitlesA;
    }

    public void setTechnicalTitlesA(String technicalTitlesA) {
        this.technicalTitlesA = technicalTitlesA == null ? null : technicalTitlesA.trim();
    }

    public String getTechnicalTitlesB() {
        return technicalTitlesB;
    }

    public void setTechnicalTitlesB(String technicalTitlesB) {
        this.technicalTitlesB = technicalTitlesB == null ? null : technicalTitlesB.trim();
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName == null ? null : postName.trim();
    }

    public Short getPostAge() {
        return postAge;
    }

    public void setPostAge(Short postAge) {
        this.postAge = postAge;
    }

    public String getHighestEducation() {
        return highestEducation;
    }

    public void setHighestEducation(String highestEducation) {
        this.highestEducation = highestEducation == null ? null : highestEducation.trim();
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName == null ? null : majorName.trim();
    }
}