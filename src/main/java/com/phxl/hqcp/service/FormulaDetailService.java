package com.phxl.hqcp.service;

import java.util.List;
import java.util.Map;

import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.service.IBaseService;
import com.phxl.hqcp.entity.FormulaDetail;

public interface FormulaDetailService extends IBaseService{
	//查询当前质量上报信息
	List<Map<String, Object>> selectFormulaDetail(Pager<Map<String, Object>> pager);
	
	//质量上报
	void updateFormulaDetail(List<Map<String, Object>> formulaDetailList,Integer isCommit) throws ValidationException ;

	//查询质量上报明细列表
	List<FormulaDetail> selectFormulaDetailList(String indexGuid);
}
