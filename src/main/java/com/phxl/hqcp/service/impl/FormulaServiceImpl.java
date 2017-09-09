package com.phxl.hqcp.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.service.impl.BaseService;
import com.phxl.core.base.util.IdentifieUtil;
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
	
	//查询指标信息审核列表
	public List<Map<String, Object>> selectFormulaList(
			Pager<Map<String, Object>> pager) {
		return formualMapper.selectFormulaList(pager);
	}
	
	//添加质量上报信息
	public void insertForMula(Long orgId, 
			String createUserId, String createUserName) {
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("orgId", orgId);
		System.out.println(formualMapper.selectFormulaIsNull(requestMap).size());
		
		List<FormulaTemplate> templates = formulaTemplateMapper.selectAllFormulaTemplate();
		//如果当前建设科室的机构，没有质量上报信息，则添加质量上报信息
		if (formualMapper.selectFormulaIsNull(requestMap)!=null) {
			if (formualMapper.selectFormulaIsNull(requestMap).size() == 0) {
				//循环所有的模板主表
				for (int j = 0; j < formulaTemplateMapper.selectAllFormulaTemplate().size(); j++) {
					//模板主表信息
					FormulaTemplate formulaTemplate = templates.get(j);
					if (formulaTemplate.getQcScopeSubType().equals("02")) {
						Formula formula = new Formula();
						formula.setIndexGuid(IdentifieUtil.getGuId());
						formula.setIndexName(formulaTemplate.getIndexCode());
						formula.setQcOrgId(formulaTemplate.getQcOrgId());
						formula.setOrgId(orgId);
						formula.setQcScopeType("02");
						formula.setStartTime(formulaTemplate.getStartDate());
						formula.setEndTime(formulaTemplate.getEndDate());
						formula.setpYear(formulaTemplate.getpYear());
						formula.setpYmd("P_HALF_YEAR");
						formula.setAuditFstate("00");
						formula.setCreateTime(new Date());
						formula.setCreateUserId(createUserId);
						formula.setCreateUserName(createUserName);
						//添加质量上报主信息
						insertInfo(formula);
						
						//查询科室建设信息
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("startDate", formula.getStartTime());
						map.put("endDate", formula.getEndTime());
						map.put("orgId", orgId);
						map.put("pYear", formula.getpYear());
						
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
							if (i==1) {
								formulaDetail.setNumeratorValue(hospitalInfo.get("staffSum")==null ? 0 : Long.valueOf(hospitalInfo.get("staffSum").toString()));
								formulaDetail.setDenominatorValue(hospitalInfo.get("planBedSum")==null ? 0 : Long.valueOf(hospitalInfo.get("planBedSum").toString()));
								if (hospitalInfo.get("planBedSum")!=null && hospitalInfo.get("planBedSum")!=null) {
									formulaDetail.setIndexValue(BigDecimal.valueOf( Double.valueOf(hospitalInfo.get("staffSum").toString()) /
										Double.valueOf(hospitalInfo.get("planBedSum").toString()) ) );
								}else {
									formulaDetail.setIndexValue(BigDecimal.valueOf(0));
								}
							}
							//添加  医学工程人员业务培训率
							if (i==2) {
								formulaDetail.setNumeratorValue(Long.valueOf(hospitalInfo.get("meetingDeptUserSum").toString()));
								formulaDetail.setDenominatorValue(Long.valueOf(hospitalInfo.get("staffSum").toString()));
								formulaDetail.setIndexValue(BigDecimal.valueOf( Double.valueOf(hospitalInfo.get("meetingDeptUserSum").toString()) /
										Double.valueOf(hospitalInfo.get("staffSum").toString()) ) );
							}
							//添加质量上报明细
							insertInfo(formulaDetail);
						}
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
	public List<Map<String, Object>> selectFormulaInfo(Map<String, Object> map) {
		return formualMapper.selectFormulaInfo(map);
	}

	//查询质量指标列表
	public List<Map<String, Object>> selectFormulaInfoList(Pager<Map<String, Object>> pager){
		return formualMapper.selectFormulaInfoList(pager);
	}

}
