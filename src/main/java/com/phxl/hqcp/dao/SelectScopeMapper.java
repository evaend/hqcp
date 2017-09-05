package com.phxl.hqcp.dao;

import java.util.List;
import java.util.Map;

import com.phxl.core.base.dao.BaseMapper;
import com.phxl.core.base.entity.Pager;

public interface SelectScopeMapper extends BaseMapper {

    /**
     * 
     * getPyearList:(科室建设-查询时间列表). <br/> 
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
     * searchSelectScopeList:(科室建设-查看医院列表). <br/> 
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
     * searchConstrDeptUserList:(查询科室人员情况列表). <br/> 
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
    List<Map<String, Object>> getDeptInfo(Pager pager);
    
    /**
     * 
     * getDeptUserAge:(按年度查询医工人员年龄情况（科室建设）). <br/> 
     * 
     * @Title: getDeptUserAge
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     */
    List<Map<String, Object>> getDeptUserAge(Pager pager);

    /**
     * 
     * getDeptUserAge:(按年度查询医工人员学历情况（科室建设）). <br/> 
     * 
     * @Title: getDeptUserAge
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     */
    List<Map<String, Object>> getDeptUserEducation(Pager pager);
    
    /**
     * 
     * getDeptUserMajor:(按年度查询医工人员专业情况（科室建设）). <br/> 
     * 
     * @Title: getDeptUserMajor
     * @Description: TODO
     * @param pager
     * @return    设定参数
     * @return List<Map<String,Object>>    返回类型
     * @throws
     */
    List<Map<String, Object>> getDeptUserMajor(Pager pager);
    
}