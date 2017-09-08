/** 
 * Project Name:basicDataModule 
 * File Name:OrgInfoService.java 
 * Package Name:com.phxl.ysy.basicDataModule.service 
 * Date:2017年3月22日下午6:32:17 
 * Copyright (c) 2017, PHXL All Rights Reserved. 
 * 
*/  
  
package com.phxl.hqcp.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;
import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.service.IBaseService;
import com.phxl.hqcp.entity.OrgInfo;

/** 
 * ClassName:OrgInfoService <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年3月22日 下午6:32:17 <br/> 
 * @author   fenghui 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
public interface OrgInfoService extends IBaseService{

    /**
     * 
     * findOrgNameExist:(查询机构名称是否唯一，根据机构主表和待审核的机构注册表查询). <br/> 
     * 
     * @Title: findOrgNameExist
     * @Description: TODO
     * @param orgName
     * @return    设定参数
     * @return int    返回类型
     * @throws
     */
    int findOrgNameExist(String orgName,Long orgId);
    
    /**
     * 
     * findOrgCodeExist:(查询组织机构代码是否唯一，根据机构主表和待审核的机构注册表查询). <br/> 
     * 
     * @Title: findOrgCodeExist
     * @Description: TODO
     * @param orgCode
     * @return    设定参数
     * @return int    返回类型
     * @throws
     */
    int findOrgCodeExist(String orgCode,Long orgId);
    
    /**
     * 
     * checkFileSize:(检查上传文件的大小). <br/> 
     * 
     * @Title: checkFileSize
     * @Description: TODO
     * @param file
     * @return    设定参数
     * @return Boolean    返回类型
     * @throws
     */
    boolean checkFileSize(MultipartFile file, long maxKbyteNum);

    /**
     * 
     * searchOrgInfoList:(查询机构列表). <br/>
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
    List<Map<String, Object>> searchParentOrgInfoList(Pager pager);
    
    /**
     * 【数据字典】查询所有机构，做下拉框
     */
	List<Map<String, Object>> findOrgs(Pager<Map<String, Object>> pager);

	void addUpdateOrgInfo(OrgInfo orginfo, OrgInfo oldOrg) throws Exception;
    
}
  