package com.phxl.hqcp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.phxl.core.base.dao.BaseMapper;
import com.phxl.core.base.entity.Pager;

public interface ConstrDeptMapper extends BaseMapper {

	/**
	 *
	 * searchConstrDept:(查询科室上报信息). <br/>
	 * @Title: searchConstrDept
	 * @Description: TODO
	 * @param pager
	 * @return    设定参数
	 * @return List<Map<String,Object>>    返回类型
	 * @throws
	 */
	List<Map<String, Object>> searchConstrDept(Pager pager);
	
	/**
	 * 
	 * searchConstrDeptCheckBox:(查询科室上报多选信息). <br/> 
	 * @Title: searchConstrDeptCheckBox
	 * @Description: TODO
	 * @param constrDeptGuid
	 * @param type
	 * @return    设定参数
	 * @return List<Map<String,Object>>    返回类型
	 * @throws
	 */
	List<Map<String, Object>> searchConstrDeptCheckBox(@Param(value = "constrDeptGuid") String constrDeptGuid, @Param(value = "type") String type);
	
}