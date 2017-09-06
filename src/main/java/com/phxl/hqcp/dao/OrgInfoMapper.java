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
     * 
     * @Title: findOrgNameOrOrgCodeExist
     * @Description: TODO
     * @param orgValue
     * @param type
     * @return    设定参数
     * @return int    返回类型
     * @throws
     */
    int findOrgNameOrOrgCodeExist(@Param(value = "orgValue") String orgValue, @Param(value = "type") String type,@Param(value = "orgId") Long orgId);

    /**
     * 
     * searchOrgInfoList:(查询机构信息列表). <br/> 
     * 
     * @Title: searchOrgInfoList
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return List<Map>    返回类型
     * @throws
     */
    List<Map> searchOrgInfoList(Pager pager);

    /**
     * 
     * searchParentOrgInfoList:(查找上级机构列表). <br/> 
     * 
     * @Title: searchParentOrgInfoList
     * @Description: TODO
     * @param orgId
     * @return    设定参数
     * @return List<OrgInfo>    返回类型
     * @throws
     */
    List<Map<String, Object>> searchParentOrgInfoList(Pager<?> pager);
    
    /**
     * 查询机构列表（联想下拉搜索）
     * @author	黄文君
     * @date	2017年4月25日 下午2:48:25
     * @param	pager
     * @return	List<Map<String,Object>>
     */
    List<Map<String, Object>> findOrgListForSelector(Pager<?> pager);
    
}