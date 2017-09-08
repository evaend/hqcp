package com.phxl.hqcp.service;

import java.util.List;
import java.util.Map;
import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ServiceException;
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
    List<Map<String, Object>> searchConstrDeptAuditList(Pager pager) throws ServiceException;

    /**
     * 
     * searchConstrDeptUserList:(科室人员列表——科室建设详情). <br/> 
     * 
     * @Title: searchConstrDeptUserList
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     */
    List<Map<String, Object>> searchConstrDeptUserList(Pager pager);

    /**
     * 
     * getDeptInfo:(按年度查询机构的床位数、机构员工总数、医工人员总数、医工培训总数). <br/> 
     * 
     * @Title: getDeptInfo
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     */
    Map<String, Object> getDeptInfo(Pager pager);

    /**
     * 
     * getDeptUserAge:(按年度查询医工人员年龄情况（科室建设）). <br/> 
     * 
     * @Title: getDeptUserAge
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return Map<String,Object>    返回类型
     * @throws
     */
    Map<String, Object> getDeptUserAge(Pager pager);

    /**
     * 
     * getDeptUserEducation:(按年度查询医工人员学历情况（科室建设）). <br/> 
     * 
     * @Title: getDeptUserEducation
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return Map<String,Object>    返回类型
     * @throws
     */
    Map<String, Object> getDeptUserEducation(Pager pager);
    
    /**
     * 
     * getDeptUserMajor:(按年度查询医工人员专业情况（科室建设）). <br/> 
     * 
     * @Title: getDeptUserMajor
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return Map<String,Object>    返回类型
     * @throws
     */
    Map<String, Object> getDeptUserMajor(Pager pager);
    
    /**
     * 
     * getOrgInfoTb:(按年度查询监管机构下的机构总数、三甲机构数、二甲机构数、及同比). <br/> 
     * 
     * @Title: getOrgInfoTb
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return Map<String,Object>    返回类型
     * @throws
     */
    Map<String, Object> getOrgInfoTb(Pager pager);
    
    /**
     * 
     * getOrgDeptInfoByGender:(按年度查询监管机构下医工人数男女比例). <br/> 
     * 
     * @Title: getOrgDeptInfoByGender
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return Map<String,Object>    返回类型
     * @throws
     */
    Map<String, Object> getOrgDeptInfoByGender(Pager pager);
    
    /**
     * 
     * getOrgEducation:(查询医工人员学历). <br/> 
     * 
     * @Title: getOrgEducation
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return Map<String,Object>    返回类型
     * @throws
     */
    Map<String, Object> getOrgEducation(Pager pager);
    
    /**
     * 
     * getAdverseEvents:(按年度查询不良事件上报率). <br/> 
     * 
     * @Title: getAdverseEvents
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return Map<String,Object>    返回类型
     * @throws
     */
    Map<String, Object> getAdverseEvents(Pager pager);
    
    /**
     * 
     * getMaterialTraceability:(按年度查询耗材追溯分析). <br/> 
     * 
     * @Title: getMaterialTraceability
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return Map<String,Object>    返回类型
     * @throws
     */
    Map<String, Object> getMaterialTraceability(Pager pager);
	
}
