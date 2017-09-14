package com.phxl.hqcp.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.util.DateUtils;
import com.phxl.hqcp.common.constant.CustomConst.LoginUser;
import com.phxl.hqcp.common.util.ExportUtil;
import com.phxl.hqcp.entity.OrgInfo;
import com.phxl.hqcp.service.FormulaService;
@Controller
@RequestMapping("/formulaController")
public class FormulaController {
	@Autowired
	FormulaService formulaService;
	@Autowired
	HttpSession session;
	
	/**
	 * 查询当前质控机构的质量管理信息
	 * @param orgName 被监管的机构ID
	 * @param auditFstate 质量上报的状态
	 * @param pYear 年份
	 * @param page 当前页
	 * @param pagesize 每页条数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectFormulaList")
	public Pager<Map<String, Object>> selectFormulaList(
			@RequestParam(value="searchName",required = false ) String orgName,
			@RequestParam(value="fstate",required = false) String auditFstate,
			@RequestParam(value="pYear",required = false) String pYear,
			@RequestParam(value="fstateType",required = false) Integer fstateType,
			@RequestParam(value="page",required = false) Integer page,
			@RequestParam(value="pagesize",required = false) Integer pagesize,
			HttpServletRequest request) {
		Assert.notNull(pYear, "当前没有选择时间！");
		Pager<Map<String, Object>> pager = new Pager<Map<String,Object>>(true);
		pager.setPageSize(pagesize == null ? 15 : pagesize);
		pager.setPageNum(page == null ? 1 : page);
		pager.addQueryParam("orgName", orgName);
		//设置审核状态参数
		if (fstateType!=null) {
			if (auditFstate==null) {
				pager.addQueryParam("auditFstate", "20");
			}else {
				pager.addQueryParam("auditFstate", auditFstate);
			}
		}else{
			if (auditFstate==null) {
				pager.addQueryParam("auditFstate", "10");
			}else {
				pager.addQueryParam("auditFstate", auditFstate);
			}
		}
		
		if (pYear!=null) {
			pager.addQueryParam("pYear", pYear.trim().substring(0, 4));
		}
		char c = pYear.trim().charAt(4);
		if (c == '1') {
			pager.addQueryParam("month", pYear.trim().substring(0, 4)+"-3-15");
		}else{
			pager.addQueryParam("month", pYear.trim().substring(0, 4)+"-9-15");
		}
		List<Map<String, Object>> list = formulaService.selectFormulaList(pager);
		
		//计算百分比
		for (Map<String, Object> map : list) {
			double schedule = Double.valueOf(map.get("indexValue").toString()) / Double.valueOf(map.get("indexCount").toString());
			map.put("schedule", schedule);
			String month = map.get("startTime").toString().trim();
			char m = month.charAt(6);
			if (m == '1' ) {
				map.put("pYearValue", map.get("pYear").toString().trim()+"1");
				map.put("pYearText", map.get("pYear").toString().trim()+"上半年");
			}else {
				map.put("pYearValue", map.get("pYear").toString().trim()+"2");
				map.put("pYearText", map.get("pYear").toString().trim()+"下半年");
			}
		}
		pager.setRows(list);
		return pager;
	}
	
	/**
	 * 查询所有质量上报的时间段
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectAllDate")
	public List<Map<String,Object>> selectAllDate(HttpServletRequest request,HttpSession session){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = formulaService.selectAllDate(map);
		//查询所有质量上报的时间段 
		List<Map<String, Object>> dateList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map2 : list) {
			Map<String, Object> dateMap = new HashMap<String, Object>();
			//获取当前日期的第五个字符（月份）
			char month = map2.get("startDate").toString().trim().charAt(5);
			if (month == '1') {
				dateMap.put("value", map2.get("pYear").toString().trim()+"1");
				dateMap.put("text", map2.get("pYear").toString().trim()+"上半年");
			}else{
				dateMap.put("value", map2.get("pYear").toString().trim()+"2");
				dateMap.put("text", map2.get("pYear").toString().trim()+"下半年");
			}
			dateList.add(dateMap);
		}
		return dateList;
	}
	
	/**
	 * 查询建设科室公式记录 
	 * @param month 当前选择时间段
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectTemplateDetail")
	public List<Map<String, Object>> selectTemplateDetail(
			@RequestParam(value = "pYear", required = false ) String pYear,
			HttpServletRequest request) throws ValidationException{
		if (StringUtils.isBlank(pYear)) {
			throw new ValidationException("时间不允许为空");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		//获取当前日期的第五个字符（上半年、下半年）
		char c = pYear.trim().charAt(4);
		if (c == '1') {
			map.put("month", pYear.trim().substring(0, 4)+"-3-15");
		}else{
			map.put("month", pYear.trim().substring(0, 4)+"-9-15");
		}
		
		List<Map<String, Object>> list = formulaService.selectTemplateDetail(map);
		return list;
	}
	
	/**
	 * 查询当前选择的医院机构的当前选择类型的公式明细，以及同级机构，同省机构的平均值(按所选时间排序，选择哪个时间段，哪个排前面)
	 * @param pYear 当前选择的时间（如2016下半年）
	 * @param indexValue 当前选择类型（如医学工程人员配置水平）
	 * @param orgId 当前选择机构
	 * @param page 当前页
	 * @param pagesize 每页条数
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectAllYearFornla")
	public Map<String, Object> selectAllYearFornla(
			@RequestParam(value="pYear",required = false ) String pYear,
			@RequestParam(value="indexValue",required = false ) String indexValue,
			@RequestParam(value="orgId",required = false ) String orgId,
			@RequestParam(value="page",required=false) Integer page,
			@RequestParam(value="pagesize",required=false) Integer pagesize,
			HttpServletRequest request) throws ValidationException{
		if (StringUtils.isBlank(pYear)) {
			throw new ValidationException("时间不允许为空");
		}
		if (StringUtils.isBlank(indexValue)) {
			throw new ValidationException("指标类型不允许为空");
		}
		if (StringUtils.isBlank(orgId)) {
			throw new ValidationException("机构ID不允许为空");
		}
		//返回的数据
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//参数
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("indexPCode", indexValue);
		map.put("orgId", orgId);
		
		//当前医院的以往指标信息 以及 同级、同省指标信息
		List<Map<String, Object>> selectFormulaInfo = formulaService.selectFormulaInfo(map);
		
		//横坐标
		Map<String, Object> xAxis = new HashMap<String, Object>();
		//纵坐标
		Map<String, Object> legend = new HashMap<String, Object>();
		
		String [] xAxisData = new String [selectFormulaInfo.size()];
		String [] legendData = {"时间","同级医院平均水平","全省医院平均配置水平对比"};

		//柱状图
		String [] seriesDate = new String [selectFormulaInfo.size()];
		//线型（同级医院）
		String [] seriesLevel = new String [selectFormulaInfo.size()];
		//线型（同级医院）
		String [] seriesAll = new String [selectFormulaInfo.size()];
				
		for (int i = 0 ; i < selectFormulaInfo.size() ; i++) {
			Map<String, Object> map2 = selectFormulaInfo.get(i);
			char m = map2.get("ymd").toString().trim().charAt(4);
			//获取横坐标的值(如果为上半年)
			if (m == '1') {
				xAxisData[i] = map2.get("pYear").toString().trim()+"上半年";
			}else{
				xAxisData[i] = map2.get("pYear").toString().trim()+"下半年";
			}
			seriesDate[i] = map2.get("indexValue")==null ? "0" : map2.get("indexValue").toString();
			seriesLevel[i] = map2.get("indexValueLevel")==null ? "0" : map2.get("indexValueLevel").toString();
			seriesAll[i] = map2.get("indexValueAll")==null ? "0" : map2.get("indexValueAll").toString();			
		}
		xAxis.put("data", xAxisData);
		legend.put("data",legendData);
		
		Map<String, Object> itemStyle = new HashMap<String, Object>();
		Map<String, Object> color = new HashMap<String, Object>();
		color.put("color", "#d73435");
		itemStyle.put("normal", color);
		
		//时间（柱）
		Map<String, Object> series1 = new HashMap<String, Object>();
		series1.put("name","时间");
		series1.put("type","bar");
		series1.put("data",seriesDate);
		series1.put("barMaxWidth","30px");
		//同级（线）
		Map<String, Object> series2 = new HashMap<String, Object>();
		series2.put("name","同级医院平均水平");
		series2.put("type","line");
		series2.put("data",seriesLevel);
		series2.put("showSymbol",false);
		series2.put("itemStyle",itemStyle);
		
		//全省（线）
		Map<String, Object> series3 = new HashMap<String, Object>();
		series3.put("name","全省医院平均配置水平对比");
		series3.put("type","line");
		series3.put("data",seriesAll);
		series3.put("showSymbol",false);
		series3.put("itemStyle",itemStyle);
		//值
		List<Map<String, Object>> series = new ArrayList<Map<String,Object>>();
		series.add(series1);
		series.add(series2);
		series.add(series3);
		
		resultMap.put("xAxis", xAxis);
		resultMap.put("legend", legend);
		resultMap.put("series", series);
		
		Pager<Map<String, Object>> pager = new Pager<Map<String,Object>>(true);
		pager.setPageSize(pagesize == null ? 5 : pagesize);
		pager.setPageNum(page == null ? 1 : page);
		pager.addQueryParam("ymd", pYear);
		pager.addQueryParam("indexPCode", indexValue);

		Map<String, Object> titleMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> selectFormulaInfoList = formulaService.selectFormulaInfoList(pager);
		if (selectFormulaInfoList.size()<1) {
			
		}else {
			titleMap.put("qcNameFz", selectFormulaInfoList.get(0).get("qcNameFz"));
			titleMap.put("qcNameFm", selectFormulaInfoList.get(0).get("qcNameFm"));
			titleMap.put("qcNameZb", selectFormulaInfoList.get(0).get("qcNameZb"));
		}
		pager.setRows(selectFormulaInfoList);
		
		resultMap.put("pager", pager);
		resultMap.put("titleMap", titleMap);
		return resultMap;
	}
	
	
	/**
	 * 查询当前选择时间的医院机构的当前选择类型的公式明细，同省机构的平均值(按所选时间排序，选择哪个时间段，哪个排前面)
	 * @param pYear 当前选择的时间（如2016下半年）
	 * @param indexValue 当前选择类型（如医学工程人员配置水平）
	 * @param page 当前页
	 * @param pagesize 每页条数
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectAllFornla")
	public Map<String, Object> selectAllFornla(
			@RequestParam(value="pYear",required = false ) String pYear,
			@RequestParam(value="indexValue",required = false ) String indexValue,
			@RequestParam(value="page",required=false) Integer page,
			@RequestParam(value="pagesize",required=false) Integer pagesize,
			HttpServletRequest request) throws ValidationException{
		if (StringUtils.isBlank(pYear)) {
			throw new ValidationException("时间不允许为空");
		}
		if (StringUtils.isBlank(indexValue)) {
			throw new ValidationException("指标类型不允许为空");
		}
		//返回的数据
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Pager<Map<String, Object>> pager1 = new Pager<Map<String,Object>>(false);
		pager1.setPageSize(pagesize == null ? 5 : pagesize);
		pager1.setPageNum(page == null ? 1 : page);
		pager1.addQueryParam("ymd", pYear);
		pager1.addQueryParam("indexPCode", indexValue);
		
		//当前医院的以往指标信息 以及 同级、同省指标信息
		List<Map<String, Object>> selectFormulaInfo = formulaService.selectFormulaInfoList(pager1);
		
		Map<String, Object> titleMap = new HashMap<String, Object>();
		
		if (selectFormulaInfo.size()<1) {
			
		}else {
			titleMap.put("qcNameFz", selectFormulaInfo.get(0).get("qcNameFz"));
			titleMap.put("qcNameFm", selectFormulaInfo.get(0).get("qcNameFm"));
			titleMap.put("qcNameZb", selectFormulaInfo.get(0).get("qcNameZb"));
		}
		
		//横坐标
		Map<String, Object> xAxis = new HashMap<String, Object>();
		//纵坐标
		Map<String, Object> legend = new HashMap<String, Object>();
		
		String [] xAxisData = new String [selectFormulaInfo.size()];
		String [] legendData = {titleMap.get("qcNameFz").toString(),"全省医院平均配置水平对比"};

		//柱状图
		String [] seriesDate = new String [selectFormulaInfo.size()];
		
		//线型（同级医院）
		String [] seriesAll = new String [selectFormulaInfo.size()];
				
		for (int i = 0 ; i < selectFormulaInfo.size() ; i++) {
			Map<String, Object> map2 = selectFormulaInfo.get(i);
			//获取横坐标的值(医院名称)
			xAxisData[i] = map2.get("fOrgName").toString();
			seriesDate[i] = map2.get("indexValue").toString();
			seriesAll[i] = map2.get("indexValueAll").toString();			
		}
		xAxis.put("data", xAxisData);
		legend.put("data",legendData);
		
		Map<String, Object> itemStyle = new HashMap<String, Object>();
		Map<String, Object> color = new HashMap<String, Object>();
		color.put("color", "#d73435");
		itemStyle.put("normal", color);
		
		//时间（柱）
		Map<String, Object> series1 = new HashMap<String, Object>();
		series1.put("name",titleMap.get("qcNameFz"));
		series1.put("type","bar");
		series1.put("data",seriesDate);
		series1.put("barMaxWidth","30px");
		
		//全省（线）
		Map<String, Object> series3 = new HashMap<String, Object>();
		series3.put("name","全省医院平均配置水平对比");
		series3.put("type","line");
		series3.put("data",seriesAll);
		series3.put("showSymbol",false);
		series3.put("itemStyle",itemStyle);
		//值
		List<Map<String, Object>> series = new ArrayList<Map<String,Object>>();
		series.add(series1);
		series.add(series3);
		
		resultMap.put("xAxis", xAxis);
		resultMap.put("legend", legend);
		resultMap.put("series", series);
		
		Pager<Map<String, Object>> pager = new Pager<Map<String,Object>>(true);
		pager.setPageSize(pagesize == null ? 5 : pagesize);
		pager.setPageNum(page == null ? 1 : page);
		pager.addQueryParam("ymd", pYear);
		pager.addQueryParam("indexPCode", indexValue);

		List<Map<String, Object>> selectFormulaInfoList = formulaService.selectFormulaInfoList(pager);
		
		pager.setRows(selectFormulaInfoList);
		
		resultMap.put("pager", pager);
		resultMap.put("titleMap", titleMap);
		return resultMap;
	}
	
	/**
	 * 导出质量指标详情
	 * @param pYear 当前选择的时间（如2016下半年）
	 * @param indexValue 当前选择类型（如医学工程人员配置水平）
	 */
	@ResponseBody
	@RequestMapping("/exporAllYearFornla")
	public void exporAllYearFornla(
			@RequestParam(value="pYear",required = false ) String pYear,
			@RequestParam(value="indexValue",required = false ) String indexValue,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		Pager<Map<String, Object>> pager = new Pager<Map<String,Object>>(false);
		if (StringUtils.isBlank(pYear)) {
			throw new ValidationException("时间不允许为空");
		}
		if (StringUtils.isBlank(indexValue)) {
			throw new ValidationException("指标类型不允许为空");
		}
		pager.addQueryParam("ymd", pYear);
		pager.addQueryParam("indexPCode", indexValue);

		List<Map<String, Object>> selectFormulaInfoList = formulaService.selectFormulaInfoList(pager);
		
		final String qcNameFz = selectFormulaInfoList.get(0).get("qcNameFz").toString();
		final String qcNameFm = selectFormulaInfoList.get(0).get("qcNameFm").toString();
		final String qcNameZb = selectFormulaInfoList.get(0).get("qcNameZb").toString();
		
		String date = "";
		char m = pYear.trim().charAt(4);
		if (m == '1') {
			date = pYear.substring(0,4).toString().trim()+"上半年";
		}else{
			date = pYear.substring(0,4).toString().trim()+"下半年";
		}

		// 导出的excel列头，和数据库查询的结果一致
		List<String> fieldName = Arrays.asList("pYear" , "fOrgName", "hospitalLevel", "hospitalType", "numeratorValue", 
				"denominatorValue", "indexValue", "indexValueLevel", "indexValueAll");
		Map<String,String> ApplyDetailExcelMap = new HashMap<String, String>() {{  
	        put("pYear", "时间");  
	        put("fOrgName", "医院名称"); 
	        put("hospitalLevel", "医院等级");  
	        put("hospitalType", "医院性质");
	        put("numeratorValue", qcNameFz);
	        put("denominatorValue", qcNameFm); 
	        put("indexValue", qcNameZb); 
	        put("indexValueLevel", "同级医院平均配置水平对比");
	        put("indexValueAll", "全省医院平均配置水平对比");
	    }};

		String nowDay = DateUtils.DateToStr(new Date(), "yyyy-MM-dd");
		String fileName = nowDay + "-质量指标详情";
		Map<String, String> format = new HashMap<String, String>();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> detail: selectFormulaInfoList){
			Map<String, Object> r = new HashMap<String, Object>();
			Map<String, Object> entity = (Map<String, Object>) detail;
			r.put("pYear", entity.get("pYear")==null ? "" : entity.get("pYear").toString());  
			r.put("fOrgName", entity.get("fOrgName")==null ? "" : entity.get("fOrgName").toString());  
			r.put("hospitalLevel", entity.get("hospitalLevel")==null ? "" : entity.get("hospitalLevel").toString());   
			r.put("hospitalType", entity.get("hospitalType")==null ? "" : entity.get("hospitalType").toString());  
			r.put("numeratorValue", entity.get("numeratorValue")==null ? "" : entity.get("numeratorValue").toString());  
			r.put("denominatorValue", entity.get("denominatorValue")==null ? "" : entity.get("denominatorValue").toString());  
			r.put("indexValue", entity.get("indexValue")==null ? "" : entity.get("indexValue").toString());   
			r.put("indexValueLevel", entity.get("indexValueLevel")==null ? "" : entity.get("indexValueLevel").toString());  
			r.put("indexValueAll", entity.get("indexValueAll")==null ? "" : entity.get("indexValueAll").toString());  
			dataList.add(r);
		}
		List<String> conditionBefore = null;
		conditionBefore = Arrays.asList(
				""+ qcNameZb
				+ ",时间：,"+ date
				);
		
		ExportUtil.exportExcel(response, fieldName, dataList, format, "质 控 列 表 详 情", conditionBefore,
				null, fileName, ApplyDetailExcelMap);	
		
	}
	
	/**
	 * 添加质量上报信息
	 * @param orgId 要上报的机构
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping("/insertForMula")
	public String insertForMula(
			@RequestParam(value="orgId",required = false) Long orgId,
			HttpServletRequest request){
		String result = "error";
		Assert.notNull(orgId, "请选择机构");
		Assert.notNull(formulaService.find(OrgInfo.class, orgId), "当前机构不存在");
		String createUserId = "111"; //session.getAttribute(LoginUser.SESSION_USERID).toString();
		String createUserName = "222"; //session.getAttribute(LoginUser.SESSION_USERNAME).toString();
		
		//添加质量上报信息
		formulaService.insertForMula(orgId, createUserId, createUserName);
		result = "success";
		return result;
	}*/
}
