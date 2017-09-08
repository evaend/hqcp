package com.phxl.hqcp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.phxl.core.base.entity.Pager;
import com.phxl.hqcp.entity.OrgInfo;

public interface OrgInfoMapper {
	/**
     * 
     * findOrgNameOrOrgCodeExist:(验证机构名称或组织机构代码的唯一性). <br/> 
     * @throws
     */
    int findOrgNameOrOrgCodeExist(@Param(value = "orgValue") String orgValue, @Param(value = "type") String type,@Param(value = "orgId") Long orgId);

    /**
     * 
     * searchOrgInfoList:(查询机构信息列表). <br/> 
     */
    List<Map> searchOrgInfoList(Pager pager);

    /**
     * 
     * searchParentOrgInfoList:(查找上级机构列表). <br/> 
     */
    List<Map<String, Object>> searchParentOrgInfoList(Pager<?> pager);
    
    /**
     * 查询机构列表（联想下拉搜索）
     */
    List<Map<String, Object>> findOrgListForSelector(Pager<?> pager);
    
}