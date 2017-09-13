package com.phxl.hqcp.dao;

import java.util.List;
import java.util.Map;

import com.phxl.hqcp.entity.FormulaTemplate;

public interface FormulaTemplateMapper {
	//查询所有的质量上报模板
   List<FormulaTemplate> selectAllFormulaTemplate(Map<String, Object> map);
}