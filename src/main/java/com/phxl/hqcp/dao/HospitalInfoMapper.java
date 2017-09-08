package com.phxl.hqcp.dao;

import com.phxl.hqcp.entity.HospitalInfo;

public interface HospitalInfoMapper {
    int deleteByPrimaryKey(Long orgId);

    int insert(HospitalInfo record);

    int insertSelective(HospitalInfo record);

    HospitalInfo selectByPrimaryKey(Long orgId);

    int updateByPrimaryKeySelective(HospitalInfo record);

    int updateByPrimaryKey(HospitalInfo record);
}