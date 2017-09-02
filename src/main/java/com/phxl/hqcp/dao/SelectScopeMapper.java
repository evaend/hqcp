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
    
}