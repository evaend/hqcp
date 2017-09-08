package com.phxl.hqcp.dao;

import java.util.List;

import com.phxl.hqcp.entity.FormulaTemplateDetail;

public interface FormulaTemplateDetailMapper {
	//查询所有公式明细
	List<FormulaTemplateDetail> selectFormulaDetailCount(String indexGuid);
}