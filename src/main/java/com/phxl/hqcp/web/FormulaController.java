package com.phxl.hqcp.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phxl.core.base.entity.Pager;
import com.phxl.hqcp.common.constant.CustomConst.LoginUser;
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
			@RequestParam(value="orgName",required = false ) String orgName,
			@RequestParam(value="auditFstate",required = false) String auditFstate,
			@RequestParam(value="pYear",required = false) String pYear,
			@RequestParam(value="fstateType",required = false) Integer fstateType,
			@RequestParam(value="page",required = false) Integer page,
			@RequestParam(value="pagesize",required = false) Integer pagesize,
			HttpServletRequest request) {
		Pager<Map<String, Object>> pager = new Pager<Map<String,Object>>(true);
		pager.setPageSize(pagesize == null ? 15 : pagesize);
		pager.setPageNum(page == null ? 1 : page);
//		pager.addQueryParam("orgId", session.getAttribute(LoginUser.SESSION_USER_ORGID));
		pager.addQueryParam("orgId", 10001);
		pager.addQueryParam("orgName", orgName);
		//设置审核状态参数
		if (fstateType!=null) {
			if (StringUtils.isBlank(auditFstate)) {
				pager.addQueryParam("auditFstate", "20");
			}else {
				pager.addQueryParam("auditFstate", auditFstate);
			}
		}else{
			if (StringUtils.isBlank(auditFstate)) {
				pager.addQueryParam("auditFstate", "10");
			}else {
				pager.addQueryParam("auditFstate", auditFstate);
			}
		}
		
		if (pYear!=null) {
			pager.addQueryParam("pYear", pYear.trim().substring(0, 4));
		}
		List<Map<String, Object>> list = formulaService.selectFormulaList(pager);
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
			@RequestParam(value = "yearMonth", required = false ) String yearMonth,
			HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		//获取当前日期的第五个字符（上半年、下半年）
		char c = yearMonth.trim().charAt(5);
		if (c == '1') {
			map.put("month", yearMonth.trim().substring(0, 5)+"-3-15");
		}else{
			map.put("month", yearMonth.trim().substring(0, 5)+"-9-15");
		}
		
		List<Map<String, Object>> list = formulaService.selectTemplateDetail(map);
		return list;
	}
	
	/**
	 * 查询当前选择的医院机构的当前选择类型的公式明细，以及同级机构，同省机构的平均值(按所选时间排序，选择哪个时间段，哪个排前面)
	 * @param yearMonth 当前选择的时间（如2016下半年）
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
			@RequestParam(value="yearMonth",required = false ) String yearMonth,
			@RequestParam(value="indexValue",required = false ) String indexValue,
			@RequestParam(value="orgId",required = false ) String orgId,
			@RequestParam(value="page",required=false) Integer page,
			@RequestParam(value="pagesize",required=false) Integer pagesize,
			HttpServletRequest request){
		//返回的数据
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//柱状图
		Map<String, Object> majorSeries = new HashMap<String, Object>();
		
		//参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("qcOrgId", 10001);
//		map.put("qcOrgId", session.getAttribute(LoginUser.SESSION_USER_ORGID));
		
		map.put("indexPCode", indexValue);
		map.put("orgId", orgId);
//		map.put("ymd", yearMonth);
		
		//当前医院的以往指标信息 以及 同级、同省指标信息
		List<Map<String, Object>> selectFormulaInfo = formulaService.selectFormulaInfo(map);
		
		//横坐标
		Map<String, Object> xAxis = new HashMap<String, Object>();
		//纵坐标
		Map<String, Object> legend = new HashMap<String, Object>();
		
		String [] xAxisData = new String [selectFormulaInfo.size()];
		String [] legendData = {"时间","同级医院平均水平","全省医院平均配置水平对比"};

//		//柱状图
//		String seriesName = "时间";
//		String type = "bar";
		String [] seriesDate = new String [selectFormulaInfo.size()];
//		//线型（同级医院）
//		String seriesName1 = "同级医院平均水平";
//		String type1 = "line";
		String [] seriesLevel = new String [selectFormulaInfo.size()];
//		//线型（同级医院）
//		String seriesName2 = "全省医院平均水平";
//		String type2 = "line";
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
			seriesDate[i] = map2.get("indexValue").toString();
			seriesLevel[i] = map2.get("indexValueLevel").toString();
			seriesAll[i] = map2.get("indexValueAll").toString();			
		}
		xAxis.put("data", xAxisData);
		legend.put("data",legendData);
		
		//时间（柱）
		Map<String, Object> series1 = new HashMap<String, Object>();
		series1.put("name","时间");
		series1.put("type","bar");
		series1.put("data",seriesDate);
		//同级（线）
		Map<String, Object> series2 = new HashMap<String, Object>();
		series2.put("name","同级医院平均水平");
		series2.put("type","line");
		series2.put("data",seriesLevel);
		//全省（线）
		Map<String, Object> series3 = new HashMap<String, Object>();
		series3.put("name","全省医院平均配置水平对比");
		series3.put("type","line");
		series3.put("data",seriesAll);
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
		pager.addQueryParam("ymd", yearMonth);
		pager.addQueryParam("indexPCode", indexValue);
//		pager.addQueryParam("qcOrgId", session.getAttribute(LoginUser.SESSION_USER_ORGID));
		pager.addQueryParam("qcOrgId", 10001);
		
		List<Map<String, Object>> selectFormulaInfoList = formulaService.selectFormulaInfoList(pager);
		pager.setRows(selectFormulaInfoList);
		
		resultMap.put("pager", pager);
		return resultMap;
	}
	
}
