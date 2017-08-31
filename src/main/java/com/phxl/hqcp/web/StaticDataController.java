package com.phxl.hqcp.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.util.IdentifieUtil;
import com.phxl.core.base.util.JSONUtils;
import com.phxl.core.base.util.LocalAssert;
import com.phxl.hqcp.common.constant.CustomConst.LoginUser;
import com.phxl.hqcp.entity.StaticData;
import com.phxl.hqcp.entity.StaticInfo;
import com.phxl.hqcp.service.StaticDataService;
/**
 * 数据字典
 */
@Controller
@RequestMapping("/staticData")
public class StaticDataController {

	@Autowired
	private StaticDataService staticDataService;

	/**
	 * 查询系统中一般的公用的基础数据
	 * type=HOSPITAL_LEVEL 医院等级
	 * type=CORPORATION_TYPE 公司类型
	 * type=HOSPITAL_PROPERTY 医疗机构性质
	 * type=MI_SEND_TYPE 消息发送类型
	 * @throws JsonProcessingException 
	 * @throws ValidationException 
	 */
	@ResponseBody
	@RequestMapping(value = "/commonData")
	public List<Map<String, Object>> searchHospitalLevel(String type) throws JsonProcessingException, ValidationException {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		LocalAssert.notBlank(type,"没有基础数据类型");
		
		
		Pager pager1 = new Pager(false);
		pager1.addQueryParam("tableName", "");
		pager1.addQueryParam("filedName", type);
		//pager1.addQueryParam("comment", "医院等级");
		List<Map<String, Object>> commonDatas = staticDataService.searchStaticData(pager1);
		
		JSONUtils jsonU = new JSONUtils();
		return commonDatas;
	}
	
	/**
	 * 查询系统中的机构的私有字典属性
	 * @param orgId
	 * @param type
	 */
	@ResponseBody
	@RequestMapping(value = "/privateData")
	public List<Map<String, Object>> privateData(String orgId,String type,
			HttpSession session) throws JsonProcessingException, ValidationException {
		Map<String,Object> resultMap = new HashMap<String,Object>();

		LocalAssert.notBlank(type,"没有数据类型");
		
		//没有传入机构就会取session里的机构id
		if(StringUtils.isBlank(orgId)){
			orgId = String.valueOf((Long)session.getAttribute(LoginUser.SESSION_USER_ORGID));
		}
		
		Pager pager1 = new Pager(false);
		pager1.addQueryParam("tableName", "");
		pager1.addQueryParam("filedName", type);
		pager1.addQueryParam("orgId", orgId);
		//pager1.addQueryParam("comment", "医院等级");
		List<Map<String, Object>> commonDatas = staticDataService.privateData(pager1);
		
		JSONUtils jsonU = new JSONUtils();
		return commonDatas;
	}
	
	/**
	 * 【数据字典】查询所有的数据字典类型列表
	 * @param searchParams 类别名称、类别代码、机构名称
	 */
	@ResponseBody
	@RequestMapping(value = "/searchStaticInfo")
	public Pager searchStaticInfo(String searchParams, Integer pagesize, 
			 Integer page) {
		Pager pager = new Pager(true);
		pager.setPageSize(pagesize == null?15:pagesize);
		pager.setPageNum(page == null?1:page);
		
		pager.addQueryParam("searchParams", searchParams);
		pager.setRows(staticDataService.searchStaticInfo(pager));
		
		return pager;
	}
	
	/**
	 * 【数据字典】查询机构的所有的数据字典类型列表
	 * @param searchParams
	 */
	@ResponseBody
	@RequestMapping(value = "/searchStaticByOrgId")
	public List<Map<String,Object>> searchStaticByOrgId(String searchParams, HttpSession session) {
		Pager pager = new Pager(false);
		
		pager.addQueryParam("searchParams", searchParams);
		pager.addQueryParam("sessionOrgId", session.getAttribute(LoginUser.SESSION_USER_ORGID));

		return staticDataService.searchStaticByOrgId(pager);
	}
	
	/**
	 * 【数据字典】查询某个机构的数据字典（select下拉框，带搜索）
	 * @param searchParams
	 */
	@ResponseBody
	@RequestMapping(value = "/orgStaticInfo")
	public List<Map<String,Object>> orgStaticInfo(String searchName,String orgId, HttpSession session) {
		Pager pager = new Pager(false);
		
		pager.addQueryParam("searchName", searchName);
		//没有传入机构就会取session里的机构id
		if(StringUtils.isBlank(orgId)){
			orgId = String.valueOf((Long)session.getAttribute(LoginUser.SESSION_USER_ORGID));
		}
		pager.addQueryParam("orgId", orgId);

		return staticDataService.orgStaticInfo(pager);
	}
	
	/**
	 * 新增数据字典类型
	 */
	@ResponseBody
	@RequestMapping("/insertStaticInfo")
	public void insertStaticInfo(StaticInfo staticInfo, HttpSession session) throws ValidationException{
		if(staticInfo.getOrgId() == null){
			throw new ValidationException("所属机构，不能为空!");
		}
		if(StringUtils.isBlank(staticInfo.getParentStaticId())){
			staticInfo.setParentStaticId("0");
		}
		LocalAssert.notBlank(staticInfo.getParentStaticId(), "父节点，不能为空!");
		LocalAssert.notBlank(staticInfo.getTfClo(), "编码，不能为空!");
		LocalAssert.notBlank(staticInfo.getTfComment(), "名称，不能为空!");
		LocalAssert.notBlank(staticInfo.getFsort(), "排序号，不能为空!");
		
		validateStaticInfo(staticInfo);
		
		//检查: 同机构的机构编码不能重复
		if(staticDataService.existedTfClo(staticInfo)){
			throw new ValidationException("该机构的编码已经存在，不能添加!");
		}
		
		staticInfo.setStaticId(IdentifieUtil.getGuId());
		staticInfo.setCreateUserid((String)session.getAttribute(LoginUser.SESSION_USERID));
		staticInfo.setCreateTime(new Date());
		staticInfo.setStaticType("01");//默认类型是私有，如果需要公有，请联系dba修改

		staticDataService.insertInfo(staticInfo);
	}
	
	/**
	 * 更新数据字典类型
	 */
	@ResponseBody
	@RequestMapping("/updateStaticInfo")
	public void updateStaticInfo(StaticInfo staticInfo, HttpSession session) throws ValidationException{
		LocalAssert.notBlank(staticInfo.getStaticId(), "id，不能为空!");
		if(staticInfo.getOrgId() == null){
			throw new ValidationException("所属机构，不能为空!");
		}
		
		if(StringUtils.isBlank(staticInfo.getParentStaticId())){
			staticInfo.setParentStaticId("0");
		}
		LocalAssert.notBlank(staticInfo.getParentStaticId(), "父节点，不能为空!");
		LocalAssert.notBlank(staticInfo.getTfClo(), "编码，不能为空!");
		LocalAssert.notBlank(staticInfo.getTfComment(), "名称，不能为空!");
		LocalAssert.notBlank(staticInfo.getFsort(), "排序号，不能为空!");
		
		validateStaticInfo(staticInfo);
		
		//检查: 同机构的机构编码不能重复
		if(staticDataService.existedTfClo(staticInfo)){
			throw new ValidationException("该机构的编码已经存在，不能添加!");
		}
		
		staticInfo.setCreateUserid((String)session.getAttribute(LoginUser.SESSION_USERID));
		staticInfo.setModifyTime(new Date());

		staticDataService.updateInfo(staticInfo);
	}
	
	/**
	 * 数据字典克隆,第一种是克隆字典类型和内容，第二种是只复制字典的内容，必须选择目的类型编码
	 */
	@ResponseBody
	@RequestMapping("/copyStaticInfo")
	public void copyStaticInfo(String sourceOrgId, String sourceStaticId,
			String newOrgId,String newStaticId,HttpSession session) throws ValidationException{
	
		LocalAssert.notBlank(sourceOrgId, "源机构id，不能为空!");
		LocalAssert.notBlank(sourceStaticId, "源类型编码，不能为空!");
		LocalAssert.notBlank(newOrgId, "目的机构id，不能为空!");
		
		if(sourceStaticId.equals(newStaticId)){
			throw new ValidationException("源类型和目的类型一样，不能克隆!");
		}
		
		if(StringUtils.isBlank(newStaticId) && sourceOrgId.equals(newOrgId)){
			throw new ValidationException("同一个机构下不能存在同样的编码，请选择目的类型!");
		}
		
		StaticInfo newSInfo = new StaticInfo();
		newSInfo.setStaticId(newStaticId);//newStaticId可以为空，这时候默认会给目的机构建立一样的type
		newSInfo.setModifyTime(new Date());
		newSInfo.setCreateUserid((String)session.getAttribute(LoginUser.SESSION_USERID));
		newSInfo.setOrgId(Long.valueOf(newOrgId));
		
		staticDataService.copyStaticInfo(sourceStaticId,newSInfo);
	}
	
	/**
	 * 【数据字典】根据字典类型查询数据字典内容查询
	 * @throws ValidationException 
	 */
	@ResponseBody
	@RequestMapping(value = "/searchStaticData")
	public Pager searchStaticData(String searchParams, String staticId,Integer pagesize, 
			 Integer page) throws ValidationException {
		Pager pager = new Pager(true);
		pager.setPageSize(pagesize == null?15:pagesize);
		pager.setPageNum(page == null?1:page);
		
		LocalAssert.notBlank(staticId, "数据字典类型，不能为空!");
		pager.addQueryParam("searchParams", searchParams);
		pager.addQueryParam("staticId", staticId);
		pager.setRows(staticDataService.searchStaticDataByStaticId(pager));
		
		return pager;
	}
	
	/**
	 * 【数据字典】新增数据字典
	 */
	@ResponseBody
	@RequestMapping("/insertStaticData")
	public void insertStaticData(StaticData staticData, HttpSession session) throws ValidationException{
	
		LocalAssert.notBlank(staticData.getStaticId(), "字典分类，不能为空!");
		LocalAssert.notBlank(staticData.getTfCloCode(), "字典编码，不能为空!");
		LocalAssert.notBlank(staticData.getTfCloName(), "字典名称，不能为空!");
		
		validateDataInfo(staticData);
		
		//检查:同类型的字典code不能重复
		if(staticDataService.existedTfCloCode(staticData)){
			throw new ValidationException("该分类里已存在该编码，不能添加!");
		}
		
		staticData.setStaticDataGuid(IdentifieUtil.getGuId());
		staticData.setCreateUserid((String)session.getAttribute(LoginUser.SESSION_USERID));
		staticData.setCreateTime(new Date());

		staticDataService.insertInfo(staticData);
	}
	
	/**
	 * 【数据字典】更新数据字典
	 */
	@ResponseBody
	@RequestMapping("/updateStaticData")
	public void updateStaticData(StaticData staticData, HttpSession session) throws ValidationException{
	
		LocalAssert.notBlank(staticData.getStaticDataGuid(), "字典分类的guid，不能为空!");
		LocalAssert.notBlank(staticData.getStaticId(), "字典分类，不能为空!");
		LocalAssert.notBlank(staticData.getTfCloCode(), "字典编码，不能为空!");
		LocalAssert.notBlank(staticData.getTfCloName(), "字典名称，不能为空!");
		
		validateDataInfo(staticData);
		//检查:同类型的字典code不能重复
		if(staticDataService.existedTfCloCode(staticData)){
			throw new ValidationException("该分类里已存在该编码，不能更新!");
		}
		
		staticData.setCreateUserid((String)session.getAttribute(LoginUser.SESSION_USERID));
		staticData.setCreateTime(new Date());

		staticDataService.updateInfo(staticData);
	}
	
	private void validateStaticInfo(StaticInfo staticInfo) throws ValidationException {
		//字典类型编码
		if(staticInfo.getTfClo() != null && staticInfo.getTfClo().length() > 50){
			throw new ValidationException("字典类型的编码，长度不能大于50!");
		}
		//字典类型名称
		if(staticInfo.getTfComment() != null && staticInfo.getTfComment().length() > 50){
			throw new ValidationException("字典类型的名称，长度不能大于50!");
		}
		//字典排序字段
		if(staticInfo.getFsort() != null && staticInfo.getFsort().length() > 20){
			throw new ValidationException("字典类型的排序字段，长度不能大于20!");
		}
	}
	
	private void validateDataInfo(StaticData staticData) throws ValidationException {
		//数据字典code
		if(staticData.getTfCloCode() != null && staticData.getTfCloCode().length() > 50){
			throw new ValidationException("数据字典的code，长度不能大于50!");
		}
		//数据字典名称
		if(staticData.getTfCloName() != null && staticData.getTfCloName().length() > 50){
			throw new ValidationException("数据字典的名称，长度不能大于50!");
		}
		//数据字典备注
		if(staticData.getTfRemark() != null && staticData.getTfRemark().length() > 50){
			throw new ValidationException("数据字典的备注，长度不能大于50!");
		}
	}
}
