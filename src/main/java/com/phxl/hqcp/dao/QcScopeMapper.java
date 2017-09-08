package com.phxl.hqcp.dao;

import java.util.List;
import java.util.Map;
import com.phxl.core.base.dao.BaseMapper;
import com.phxl.core.base.entity.Pager;

public interface QcScopeMapper extends BaseMapper {
    
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
     * searchConstrDeptAuditList:(科室信息审核列表). <br/> 
     * 
     * @Title: searchConstrDeptAuditList
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     */
    List<Map<String, Object>> searchConstrDeptAuditList(Pager pager);
    
    /**
     * 
     * getOrgInfoTb:(按年度查询监管机构下的机构总数、三甲机构数、二甲机构数、及同比). <br/>
     * 
     * @Title: getOrgInfoTb
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     */
    List<Map<String, Object>> getOrgInfoTb(Pager pager);
    
    /**
     * 
     * getOrgDeptInfo:(按年度查询监管机构下医工人数男女比例). <br/> 
     * 
     * @Title: getOrgDeptInfo
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     */
    List<Map<String, Object>> getOrgDeptInfoByGender(Pager pager);
    
    /**
     * 
     * getOrgEducation:(查询医工人员学历). <br/> 
     * 
     * @Title: getOrgEducation
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     */
    List<Map<String, Object>> getOrgEducation(Pager pager);
    
    /**
     * 
     * getAdverseEvents:(按年度查询不良事件上报率). <br/> 
     * 
     * @Title: getAdverseEvents
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     */
    List<Map<String, Object>> getAdverseEvents(Pager pager);
    
    /**
     * 
     * getMaterialTraceability:(按年度查询耗材追溯分析). <br/> 
     * 
     * @Title: getMaterialTraceability
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     */
    List<Map<String, Object>> getMaterialTraceability(Pager pager);
   
}