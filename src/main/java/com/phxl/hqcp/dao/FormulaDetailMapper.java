package com.phxl.hqcp.dao;

import java.util.List;
import java.util.Map;

import com.phxl.core.base.entity.Pager;
import com.phxl.hqcp.entity.FormulaDetail;
import com.phxl.hqcp.entity.FormulaTemplateDetail;

public interface FormulaDetailMapper {
	//查询当前质量上报信息
	List<Map<String, Object>> selectFormulaDetail(Pager<Map<String, Object>> pager);
	
	//质量上报
	void updateFormulaDetail(Map<String, Object> map);
}