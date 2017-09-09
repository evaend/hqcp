package com.phxl.hqcp.service;

import java.util.List;
import java.util.Map;

import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.service.IBaseService;

public interface FormulaService extends IBaseService{
	//查询指标信息审核列表
	List<Map<String, Object>> selectFormulaList(Pager<Map<String, Object>> pager);
	
	//添加质量上报信息
	public void insertForMula(Long orgId,String createUserId,String createUserName);

	//查询所有质量上报的时间段
	List<Map<String, Object>> selectAllDate(Map<String, Object> map);

	//查询建设科室公式记录
	List<Map<String, Object>> selectTemplateDetail(Map<String, Object> map);

	//查询质量指标详情
	List<Map<String, Object>> selectFormulaInfo(Map<String, Object> map);
	
	//查询质量指标列表
	List<Map<String, Object>> selectFormulaInfoList(Pager<Map<String, Object>> pager);
}
