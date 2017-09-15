package com.phxl.hqcp.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.hqcp.common.constant.CustomConst.LoginUser;
import com.phxl.hqcp.entity.Formula;
import com.phxl.hqcp.entity.FormulaDetail;
import com.phxl.hqcp.service.FormulaDetailService;
import com.phxl.hqcp.service.FormulaService;

@Controller
@RequestMapping("/formulaDetailController")
public class FormulaDetailController {
	@Autowired
	FormulaService formulaService;
	@Autowired
	FormulaDetailService formulaDetailService;
	@Autowired
	HttpSession session;
	
	/**
	 * 查询当前质量上报信息
	 * @param pYear 上报年份
	 * @param orgId 机构ID
	 */
	@ResponseBody
	@RequestMapping("/selectFormulaDetail")
	public List<Map<String, Object>> selectFormulaDetail(
			@RequestParam(value="pYear",required=false)String pYear,
			@RequestParam(value="orgId",required=false)String orgId,
			HttpServletRequest request) throws ValidationException{
		if (StringUtils.isBlank(pYear)) {
			throw new ValidationException("时间不允许为空");
		}
		
		Pager<Map<String, Object>> pager = new Pager<Map<String,Object>>(false);
		//如果当前没有选择时间，默认最接近现在的时间
		if (StringUtils.isBlank(pYear)) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = formulaService.selectAllDate(map);
			Map<String, Object> dateMap = list.get(0);
			//获取当前日期的第五个字符（月份）
			char m = dateMap.get("startDate").toString().trim().charAt(5);
			if (m == '1') {
				pYear = dateMap.get("pYear").toString().trim()+"1";
			}else{
				pYear = dateMap.get("pYear").toString().trim()+"2";
			}
		}
		
		//获取当前日期的第五个字符（上、下） 半年
		char month = pYear.trim().charAt(4);
		if (month == '1') {
			pager.addQueryParam("month", pYear.substring(0, 4)+"-3-15");
		}else{
			pager.addQueryParam("month", pYear.substring(0, 4)+"-9-15");
		}
		
		if (StringUtils.isBlank(orgId)) {
			pager.addQueryParam("orgId", session.getAttribute(LoginUser.SESSION_USER_ORGID));
		}else {
			pager.addQueryParam("orgId", orgId);
		}
		
		List<Map<String, Object>> list = formulaDetailService.selectFormulaDetail(pager);
		
		if (list != null && list.size() > 0) {
			
		}else {
			formulaService.insertFormula(pYear, orgId);
			list = formulaDetailService.selectFormulaDetail(pager);
		}
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		for (Map<String, Object> map : list) {
			Boolean flag = null;
			//如果为医学工程人员（医疗设备、医用耗材管理和工程技术人员）配置水平  或者  医学工程人员业务培训率
			if ( (map.get("indexPCode").toString().trim().equals("05INDEX")) ||(map.get("indexPCode").toString().trim().equals("06INDEX")) ) {
				flag = true;
			}else{
				flag = false;
			}
			Integer fsort = Integer.valueOf(map.get("fsort").toString());
			String title = map.get("indexDefine").toString();
			String indexDetailGuid = map.get("indexDetailGuid")==null ? "" : map.get("indexDetailGuid").toString();
			String [] tips = {"定义："+map.get("indexDefine") , "意义："+map.get("indexMeaning"),
					"公式："+map.get("indexHelp")};

			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("label",map.get("numberName")==null ? "" :  map.get("numberName").toString());
			map1.put("value",map.get("numeratorValue")==null ? "" : map.get("numeratorValue").toString());
			map1.put("required", false);
			map1.put("key",map.get("numeratorPCode")==null ? "" :map.get("numeratorPCode").toString());
			map1.put("readonly",flag);
			
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("label",map.get("denominatorName")==null ? "" : map.get("denominatorName").toString());
			map2.put("value",map.get("denominatorValue")==null ? "" : map.get("denominatorValue").toString());
			map2.put("required",map.get("numberName")==null ? "" : false);
			map2.put("key",map.get("denominatorPCode")==null ? "" : map.get("denominatorPCode").toString());
			map2.put("readonly",flag);
			
			Map<String, Object> map3 = new HashMap<String, Object>();
			map3.put("label",map.get("indexName")==null ? "" : map.get("indexName").toString());
			map3.put("value",map.get("indexValue")==null ? "" : map.get("indexValue").toString());
			map3.put("required", false);
			map3.put("key",map.get("indexPCode")==null ? "" : map.get("indexPCode").toString());
			map3.put("readonly",flag);
			
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			mapList.add(map1);
			mapList.add(map2);
			mapList.add(map3);
			
			Map<String, Object> labelMap = new HashMap<String, Object>();
			labelMap.put("fsort", fsort);
			labelMap.put("title", title);
			labelMap.put("indexDetailGuid", indexDetailGuid);
			labelMap.put("tips", tips);
			labelMap.put("mapList", mapList);
			resultList.add(labelMap);
		}
		
		return resultList;
	}
	
	/**
	 * 质量上报
	 * @param isCommit 是提交还是暂存(1提交，2暂存)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateFormulaDetail")
	public String updateFormulaDetail(
			HttpServletRequest request , HttpSession session
			) throws JsonParseException, JsonMappingException, IOException, ValidationException{
		String result = "error";
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));//配置项:默认日期格式
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//配置项:忽略未知属性
		
		Map<String, List<Object>> formulaDetails = mapper.readValue(request.getReader(), Map.class);
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Integer isCommit = null;
		//获取要修改的值
		for (String str : formulaDetails.keySet()) {
			List<Object> ob = formulaDetails.get(str);
			Map<String, Object> map = new HashMap<String, Object>();
			if (str.trim().equals("isCommit")) {
				isCommit = Integer.valueOf(ob.get(0).toString());
			}else if (str.trim().equals("pYear")) {
				
			} else {
				map.put("guid", str);
				map.put("numeratorValue",ob.get(0));
				map.put("denominatorValue",ob.get(1));
				map.put("indexValue",BigDecimal.valueOf(Double.valueOf( ob.get(0).toString())/ 
						Double.valueOf(ob.get(1).toString()) ) );
				list.add(map);
			}
		}
		
		formulaDetailService.updateFormulaDetail(list, isCommit);
		
		result = "success";
		return result;
	}
	
	/**
	 * 审核指标信息
	 * @param indexGuid 公式记录主表GUID
	 * @param isTransat 是否通过（0通过，1不通过）
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateFormulaFstate")
	public String updateFormulaFstate(
			@RequestParam(value="indexGuid",required=false)String indexGuid,
			@RequestParam(value="isTransat",required=false)Integer isTransat,
			HttpServletRequest request) throws ValidationException {
		String result = "error";
		Assert.notNull(indexGuid, "质量上报ID不允许为空");
		
		Formula formula = formulaService.find(Formula.class, indexGuid);
		Assert.notNull(formula, "该指标信息审核不存在");
		if (!formula.getAuditFstate().equals("10")) {
			throw new ValidationException("该指标已经审核过了，不允许重复审核");
		}else{
			//通过审核
			if (isTransat==0) {
				formula.setAuditFstate("20");
				formulaService.updateInfo(formula);
			}
		}
		
		result = "success";
		return result;
	}
	
}
