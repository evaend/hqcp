package com.phxl.hqcp.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.service.impl.BaseService;
import com.phxl.core.base.util.IdentifieUtil;
import com.phxl.hqcp.common.constant.CustomConst.LoginUser;
import com.phxl.hqcp.dao.FormulaDetailMapper;
import com.phxl.hqcp.dao.FormulaMapper;
import com.phxl.hqcp.dao.FormulaTemplateDetailMapper;
import com.phxl.hqcp.dao.FormulaTemplateMapper;
import com.phxl.hqcp.entity.Formula;
import com.phxl.hqcp.entity.FormulaDetail;
import com.phxl.hqcp.entity.FormulaTemplate;
import com.phxl.hqcp.entity.FormulaTemplateDetail;
import com.phxl.hqcp.service.FormulaService;

@Service
public class FormulaServiceImpl extends BaseService implements FormulaService{
	@Autowired
	FormulaMapper formualMapper;
	@Autowired
	FormulaDetailMapper formulaDetailMapper;
	@Autowired
	FormulaTemplateMapper formulaTemplateMapper;
	@Autowired
	FormulaTemplateDetailMapper formulaTemplateDetailMapper;
	@Autowired
	HttpSession session;
	
	//查询指标信息审核列表
	public List<Map<String, Object>> selectFormulaList(
			Pager<Map<String, Object>> pager) {
		return formualMapper.selectFormulaList(pager);
	}
	
	//添加质量上报信息
	public void insertForMula(String pYear,Long orgId, 
			String createUserId, String createUserName) {
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("orgId", orgId);
		requestMap.put("pYear", pYear);
		List<Formula> list = formualMapper.selectFormulaIsNull(requestMap);
		
		//查询条件（当前选择时间）
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("pYear", pYear);
		
		List<FormulaTemplate> templates = formulaTemplateMapper.selectYearFormulaTemplate(map1);
		//如果当前建设科室的机构，没有质量上报信息，则添加质量上报信息
		if (list!=null && list.size()==templates.size()) {
			//循环所有的模板主表
			for (int j = 0; j < list.size(); j++) {
				//模板主表信息
				Formula formula = list.get(j);
				this.updateFormulaDetailForTemplate(formula,orgId.toString());
			}
		}
		else if (list==null || list.size()!=templates.size()) {
			//如果没有数据，则全部添加
			if (list.size()==0) {
				//循环所有的模板主表
				for (int j = 0; j < templates.size(); j++) {
					//模板主表信息
					FormulaTemplate formulaTemplate = templates.get(j);
					this.insertFormulaDetailForTemplate(formulaTemplate, orgId.toString(), pYear);
				}
			}
			//如果数据不全，则一部分修改，一部分添加
			else if(templates.size()>list.size()){
				//循环所有的模板主表
				for (int j = 0; j < templates.size(); j++) {
					//模板主表信息
					FormulaTemplate formulaTemplate = templates.get(j);
					Formula formula1 = list.get(0);
					//如果当前质量上报信息比模板少
					if (formulaTemplate.getStartDate()!=formula1.getStartTime()) {
						this.insertFormulaDetailForTemplate(formulaTemplate, orgId.toString(), pYear);
					}else{
						this.updateFormulaDetailForTemplate(formula1, orgId.toString());
					}
				}
			}
		}
			
	}

	//查询所有质量上报的时间段
	public List<Map<String, Object>> selectAllDate(Map<String, Object> map) {
		return formualMapper.selectAllDate(map);
	}

	//查询建设科室公式记录
	public List<Map<String, Object>> selectTemplateDetail(
			Map<String, Object> map) {
		return formualMapper.selectTemplateDetail(map);
	}

	//查询质量指标详情
	public List<Map<String, Object>> selectFormulaInfo(Pager<Map<String, Object>> map) {
		return formualMapper.selectFormulaInfo(map);
	}

	//查询质量指标列表
	public List<Map<String, Object>> selectFormulaInfoList(Pager<Map<String, Object>> pager){
		return formualMapper.selectFormulaInfoList(pager);
	}

	//添加质量上报信息
	public void insertFormula(String pYear, String orgId) throws ValidationException {
		Map<String, Object> map1 = new HashMap<String, Object>();
		char c = pYear.trim().charAt(4);
		if (c == '1') {
			map1.put("month", pYear.trim().substring(0, 4)+"-3-15");
		}else{
			map1.put("month", pYear.trim().substring(0, 4)+"-9-15");
		}
		List<FormulaTemplate> templates = formulaTemplateMapper.selectAllFormulaTemplate(map1);
		if (templates==null) {
			throw new ValidationException("没有当前选择时间的质量上报的模板");
		}else if (templates.size()==0) {
			throw new ValidationException("没有当前选择时间的质量上报的模板");
		}
		//如果有质量上报模板，则添加质量上报信息
		FormulaTemplate formulaTemplate = templates.get(0);
		this.insertFormulaDetailForTemplate(formulaTemplate,orgId,pYear);
	}
	
	
	//添加质量上报
	public void insertFormulaDetailForTemplate(FormulaTemplate formulaTemplate,String orgId,String pYear){

		Formula formula = new Formula();
		formula.setIndexGuid(IdentifieUtil.getGuId());
		formula.setIndexName(formulaTemplate.getIndexCode());
		formula.setQcOrgId(formulaTemplate.getQcOrgId());
		formula.setOrgId(Long.valueOf(orgId));
		formula.setQcScopeType("02");
		formula.setStartTime(formulaTemplate.getStartDate());
		formula.setEndTime(formulaTemplate.getEndDate());
		formula.setpYear(formulaTemplate.getpYear());
		formula.setpYmd("P_HALF_YEAR");
		formula.setAuditFstate("00");
		formula.setCreateTime(new Date());
		formula.setCreateUserId(session.getAttribute(LoginUser.SESSION_USERID).toString());
		formula.setCreateUserName(session.getAttribute(LoginUser.SESSION_USERNAME).toString());
		//添加质量上报主信息
		insertInfo(formula);
		
		//查询科室建设信息
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startDate", formulaTemplate.getStartDate());
		map.put("endDate", formulaTemplate.getEndDate());
		map.put("orgId", orgId);
		map.put("pYear", pYear.trim().substring(0, 4));
		
		Map<String, Object> hospitalInfo = formualMapper.selectHospitalInfo(map);
		
		List<FormulaTemplateDetail> FormulaTemplateDetailList = formulaTemplateDetailMapper.selectFormulaDetailCount(formulaTemplate.getIndexGuid());
		
		for (int i = 0; i < FormulaTemplateDetailList.size(); i++) {
			//模板明细
			FormulaTemplateDetail formulaTemplateDetail = FormulaTemplateDetailList.get(i);
			//添加明细
			FormulaDetail formulaDetail = new FormulaDetail();
			formulaDetail.setIndexDetailGuid(IdentifieUtil.getGuId());
			formulaDetail.setIndexGuid(formula.getIndexGuid());
			formulaDetail.setFsort(formulaTemplateDetail.getFsort());
			formulaDetail.setIndexDefine(formulaTemplateDetail.getIndexDefine());
			formulaDetail.setIndexMeaning(formulaTemplateDetail.getIndexMeaning());
			formulaDetail.setIndexHelp(formulaTemplateDetail.getIndexHelp());
			formulaDetail.setIndexName(formulaTemplateDetail.getIndexName());
			formulaDetail.setIndexPCode(formulaTemplateDetail.getIndexPCode());
			formulaDetail.setNumeratorPCode(formulaTemplateDetail.getNumeratorPCode());
			formulaDetail.setDenominatorPCode(formulaTemplateDetail.getDenominatorPCode());
			formulaDetail.setGroupId(formulaTemplateDetail.getGroupId());
			formulaDetail.setGroupName(formulaTemplateDetail.getGroupName());
			formulaDetail.setGroupSort(formulaTemplateDetail.getGroupSort());
			formulaDetail.setTfRemark(formulaTemplateDetail.getTfRemark());
			//添加   医学工程人员（医疗设备、医用耗材管理和工程技术人员）配置水平
			if (hospitalInfo!=null) {
				if (formulaTemplateDetail.getIndexPCode().equals("05INDEX")) {
					formulaDetail.setNumeratorValue(hospitalInfo.get("staffSum")==null ? 0 : Long.valueOf(hospitalInfo.get("staffSum").toString()));
					formulaDetail.setDenominatorValue(hospitalInfo.get("planBedSum")==null ? 0 : Long.valueOf(hospitalInfo.get("planBedSum").toString()));
					if (hospitalInfo.get("staffSum")!=null && hospitalInfo.get("planBedSum")!=null) {
						formulaDetail.setIndexValue(BigDecimal.valueOf( Double.valueOf(hospitalInfo.get("staffSum").toString()) /
							Double.valueOf(hospitalInfo.get("planBedSum").toString()) ) );
					}
				}
				//添加  医学工程人员业务培训率
				if (formulaTemplateDetail.getIndexPCode().equals("06INDEX")) {
					formulaDetail.setNumeratorValue(hospitalInfo.get("staffSum")==null ? 0 : Long.valueOf(hospitalInfo.get("staffSum").toString()));
					formulaDetail.setDenominatorValue(hospitalInfo.get("planBedSum")==null ? 0 : Long.valueOf(hospitalInfo.get("planBedSum").toString()));
					if (hospitalInfo.get("staffSum")!=null && hospitalInfo.get("planBedSum")!=null) {
						formulaDetail.setIndexValue(BigDecimal.valueOf( Double.valueOf(hospitalInfo.get("staffSum").toString()) /
							Double.valueOf(hospitalInfo.get("planBedSum").toString()) ) );
					}
				}
				System.out.println(hospitalInfo.get("staffSum"));
				System.out.println(hospitalInfo.get("planBedSum"));
			}
			//添加质量上报明细
			insertInfo(formulaDetail);
		}
	}
	
	public void updateFormulaDetailForTemplate(Formula formula,String orgId) {
		if (!formula.getAuditFstate().equals("20")) {
			//查询科室建设信息
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("startDate", formula.getStartTime());
			map.put("endDate", formula.getEndTime());
			map.put("orgId", orgId);
			map.put("pYear", formula.getpYear());
				
			Map<String, Object> hospitalInfo = formualMapper.selectHospitalInfo(map);
				
			List<FormulaDetail> FormulaDetailList = formulaDetailMapper.selectFormulaDetailList(formula.getIndexGuid());
			if (FormulaDetailList != null) {
				for (int i = 0; i < FormulaDetailList.size(); i++) {
					//模板明细
					FormulaDetail formulaDetail = FormulaDetailList.get(i);
					if (hospitalInfo!=null) {
						//修改   医学工程人员（医疗设备、医用耗材管理和工程技术人员）配置水平
						if (formulaDetail.getIndexPCode().equals("05INDEX")) {
							formulaDetail.setNumeratorValue(hospitalInfo.get("staffSum")==null ? 0 : Long.valueOf(hospitalInfo.get("staffSum").toString()));
							formulaDetail.setDenominatorValue(hospitalInfo.get("planBedSum")==null ? 0 : Long.valueOf(hospitalInfo.get("planBedSum").toString()));
							if (hospitalInfo.get("staffSum")!=null && hospitalInfo.get("planBedSum")!=null) {
								formulaDetail.setIndexValue(BigDecimal.valueOf( Double.valueOf(hospitalInfo.get("staffSum").toString()) /
									Double.valueOf(hospitalInfo.get("planBedSum").toString()) ) );
							}
						}
						//添加  医学工程人员业务培训率
						if (formulaDetail.getIndexPCode().equals("06INDEX")) {
							formulaDetail.setNumeratorValue(hospitalInfo.get("staffSum")==null ? 0 : Long.valueOf(hospitalInfo.get("staffSum").toString()));
							formulaDetail.setDenominatorValue(hospitalInfo.get("planBedSum")==null ? 0 : Long.valueOf(hospitalInfo.get("planBedSum").toString()));
							if (hospitalInfo.get("staffSum")!=null && hospitalInfo.get("planBedSum")!=null) {
								formulaDetail.setIndexValue(BigDecimal.valueOf( Double.valueOf(hospitalInfo.get("staffSum").toString()) /
									Double.valueOf(hospitalInfo.get("planBedSum").toString()) ) );
							}
						}
					}
					
					//添加质量上报明细
					updateInfo(formulaDetail);
				}
			}
		}
	}
}
