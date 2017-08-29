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
   
}