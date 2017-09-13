package com.phxl.hqcp.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ValidationException;
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
	public void updateFormulaDetail(List<Map<String, Object>> formulaDetailList,Integer isCommit) throws ValidationException {
		//修改质量上报信息
		for (Map<String, Object> map : formulaDetailList) {
			formulaDetailMapper.updateFormulaDetail(map);
		}
		
		//获得质量上报主表
		FormulaDetail formulaDetail = find(FormulaDetail.class, formulaDetailList.get(0).get("guid"));
		Formula formula = find(Formula.class, formulaDetail.getIndexGuid());
		if (formula.getAuditFstate().equals("20")) {
			throw new ValidationException("当前质量上报信息已经通过审核，不允许修改！");
		}
		if (isCommit == 1) {
			formula.setAuditFstate("10");
		}else{
			formula.setAuditFstate("00");
		}
		//修改主表信息
		updateInfo(formula);
				
	}

}
