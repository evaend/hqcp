package com.phxl.hqcp.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phxl.core.base.entity.Pager;
import com.phxl.hqcp.dao.FormulaDetailMapper;
import com.phxl.hqcp.entity.FormulaDetail;
import com.phxl.hqcp.service.FormulaDetailService;

@Service
public class FormulaDetailServiceImpl implements FormulaDetailService {
	@Autowired
	FormulaDetailMapper formulaDetailMapper;
	
	//查询当前质量上报信息
	public List<Map<String, Object>> selectFormulaDetail(
			Pager<Map<String, Object>> pager) {
		return formulaDetailMapper.selectFormulaDetail(pager);
	}

}
