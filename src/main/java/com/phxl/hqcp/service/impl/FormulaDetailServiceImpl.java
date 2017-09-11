package com.phxl.hqcp.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.service.impl.BaseService;
import com.phxl.hqcp.dao.FormulaDetailMapper;
import com.phxl.hqcp.entity.Formula;
import com.phxl.hqcp.entity.FormulaDetail;
import com.phxl.hqcp.service.FormulaDetailService;

@Service
public class FormulaDetailServiceImpl extends BaseService implements FormulaDetailService {
	@Autowired
	FormulaDetailMapper formulaDetailMapper;
	
	//查询当前质量上报信息
	public List<Map<String, Object>> selectFormulaDetail(
			Pager<Map<String, Object>> pager) {
		return formulaDetailMapper.selectFormulaDetail(pager);
	}

	//质量上报
	public void updateFormulaDetail(FormulaDetail[] formulaDetailList,Integer isCommit) {
		//修改质量上报信息
		for (FormulaDetail formulaDetail : formulaDetailList) {
			updateInfo(formulaDetail);
		}
		
		//获得质量上报主表
		Formula formula = find(Formula.class, formulaDetailList [0].getIndexGuid());
		if (isCommit==0) {
			formula.setAuditFstate("10");
		}else{
			formula.setAuditFstate("00");
		}
		//修改主表信息
		updateInfo(formula);
				
	}

}
