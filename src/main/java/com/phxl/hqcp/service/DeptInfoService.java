package com.phxl.hqcp.service;

import java.util.List;
import java.util.Map;
import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.service.IBaseService;
import com.phxl.hqcp.entity.ConstrDept;

public interface DeptInfoService extends IBaseService{
	
	/**
	 * 
	 * getPyearList:(科室建设-时间下拉框). <br/> 
	 * 
	 * @Title: getPyearList
	 * @Description: TODO
	 * @param pager
	 * @return    设定参数
	 * @return List<Map<String,Object>>    返回类型
	 * @throws
	 */
	List<Map<String, Object>> getPyearList(Pager pager);

	/**
	 * 
	 * searchSelectScopeList:(科室建设-医院列表). <br/> 
	 * 
	 * @Title: searchSelectScopeList
	 * @Description: TODO
	 * @param pager
	 * @return    设定参数
	 * @return List<Map<String,Object>>    返回类型
	 * @throws
	 */
    List<Map<String, Object>> searchSelectScopeList(Pager pager);
    
    /**
     * 
     * getDeptYearList:(科室上报-查询上报周期列表). <br/> 
     * 
     * @Title: getDeptYearList
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     */
    List<Map<String, Object>> getDeptYearList(Pager pager);

    /**
     * 
     * insertEditConstrDept:(新增或编辑科室上报信息). <br/> 
     * 
     * @Title: insertEditConstrDept
     * @Description: TODO
     * @param constrDept
     * @param sessionUserId
     * @param sessionUserName
     * @param sessionOrgId
     * @throws Exception    设定参数
     * @return void    返回类型
     * @throws
     */
    public void insertEditConstrDept(ConstrDept constrDept, String sessionUserId, String sessionUserName, Long sessionOrgId) throws Exception;

    /**
     * 
     * searchConstrDeptAuditList:(查询科室信息审核列表). <br/> 
     * 
     * @Title: searchConstrDeptAuditList
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     */
    List<Map<String, Object>> searchConstrDeptAuditList(Pager pager);
	
}
