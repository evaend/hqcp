package com.phxl.hqcp.dao;

import java.util.List;
import java.util.Map;

import com.phxl.core.base.entity.Pager;
import com.phxl.hqcp.entity.Formula;

public interface FormulaMapper {
	//查询指标信息审核列表
	List<Map<String, Object>> selectFormulaList(Pager<Map<String, Object>> pager);
	
	//查询建设科室信息
	Map<String, Object> selectHospitalInfo(Map<String, Object> map);
	
	//查询所有质量上报的时间段
	List<Map<String, Object>> selectAllDate(Map<String, Object> map);
	
	//查询建设科室公式记录
	List<Map<String, Object>> selectTemplateDetail(Map<String, Object> map);
	
	//查询质量指标详情
	List<Map<String, Object>> selectFormulaInfo(Map<String, Object> map);
	
	//查询质量指标列表
	List<Map<String, Object>> selectFormulaInfoList(Pager<Map<String, Object>> pager);
	
	//查询当前机构，有没有质量上报信息
	List<Formula> selectFormulaIsNull(Map<String, Object> map);
}