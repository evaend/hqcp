package com.phxl.hqcp.web;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.interceptor.ResResultBindingInterceptor;
import com.phxl.core.base.util.JSONUtils;
import com.phxl.core.base.util.LocalAssert;
import com.phxl.hqcp.common.constant.CustomConst.AuditFstate;
import com.phxl.hqcp.common.constant.CustomConst.LoginUser;
import com.phxl.hqcp.common.constant.CustomConst.OrgType;
import com.phxl.hqcp.entity.OrgInfo;
import com.phxl.hqcp.service.OrgInfoService;

/** 
 * 
 * 2017年8月31日 下午9:53:24
 * @author 陶悠
 *
 */
@Controller
@RequestMapping("/orgController")
public class OrgInfoController{
    
    @Autowired
    private OrgInfoService orgInfoService;
    
    /**
     * 
     * findAllOrgList:(查找机构列表). <br/>
     */
    @ResponseBody
    @RequestMapping("/findAllOrgList")
    public Pager findAllOrgList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pagesize", required = false) Integer pagesize,
            @RequestParam(value = "orgType", required = false)String orgType,
            @RequestParam(value = "orgId", required = false)Long orgId,
            @RequestParam(value = "qcOrgId", required = false)Long qcOrgId,
            @RequestParam(value = "hospitalLevel", required = false)String hospitalLevel,
            @RequestParam(value = "hospitalProperty", required = false)String hospitalProperty,
            @RequestParam(value = "planBedSum", required = false)String planBedSum,
            @RequestParam(value = "staffSum", required = false)String staffSum,
            @RequestParam(value = "pYear", required = false)String pYear,
            HttpServletRequest request) {
        Pager pager = new Pager(true);
        pager.setPageNum(page == null?1:page);
        pager.setPageSize(pagesize == null?15:pagesize);
        pager.addQueryParam("orgId", orgId);
        pager.addQueryParam("orgType", orgType);
        pager.addQueryParam("qcOrgId", qcOrgId);
        pager.addQueryParam("hospitalLevel", hospitalLevel);
        pager.addQueryParam("hospitalProperty", hospitalProperty);
        pager.addQueryParam("planBedSum", planBedSum);
        pager.addQueryParam("staffSum", staffSum);
        pager.addQueryParam("pYear", pYear);

        List<Map> result = orgInfoService.searchOrgInfoList(pager);
        pager.setRows(result);
        return pager;
    }
    
    /**
     * searchOrgInfo:(查询机构详细信息). <br/> 
     */
    @ResponseBody
    @RequestMapping("/searchOrgInfo")
    public Map<String, Object> searchOrgInfo(@RequestParam(value = "orgId", required = false)Long orgId, 
    	@RequestParam(value = "pYear", required = false)String pYear,
        HttpServletRequest request) throws JsonProcessingException{
        request.setAttribute(ResResultBindingInterceptor.IGNORE_STD_RESULT, true);//忽略标准结果
        Pager pager1 = new Pager(false);
        pager1.addQueryParam("orgId", orgId);
        pager1.addQueryParam("pYear", pYear);
        List<Map> resultMap = orgInfoService.searchOrgInfoList(pager1);       
        return resultMap.get(0);
    }
    
    /**
     * 
     * addUpdateOrgInfo:(新增编辑机构信息). <br/> 
     */
    @ResponseBody
    @RequestMapping(value={"/addUpdateOrgInfo"})
    public void addUpdateOrgInfo(HttpServletRequest request) throws Exception{
    	
    	ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));//配置项:默认日期格式
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//配置项:忽略未知属性
		OrgInfo orginfo = objectMapper.readValue(request.getReader(), OrgInfo.class);
		Assert.notNull(orginfo, "请传机构信息!");
		
        String userid = (String)request.getSession().getAttribute(LoginUser.SESSION_USERID);
        String username = (String)request.getSession().getAttribute(LoginUser.SESSION_USERNAME);
        
		LocalAssert.notBlank(orginfo.getpYear(), "请选择信息统计时间");
		LocalAssert.notBlank(orginfo.getHospitalLevel(), "请选择医院等级");
		LocalAssert.notBlank(orginfo.getHospitalProperty(), "请选择医院性质");
		LocalAssert.notBlank(orginfo.getHospitalTeaching(), "请选择医院教学类型");
		LocalAssert.notBlank(orginfo.getHospitalType(), "请选择医院类型");
		Assert.notNull(orginfo.getPlanBedSum(), "请输入医院编制床位数");
		Assert.notNull(orginfo.getActualBedSum(), "请输入医院开放床位数");
		Assert.notNull(orginfo.getStaffSum(), "请输入职工总数");
		String orgAddress = orginfo.getTfProvince() + orginfo.getTfCity() + orginfo.getTfDistrict();
		LocalAssert.notBlank(orgAddress, "请选择地址");
		 //若机构组织代码或机构附件没有提交，则状态是待审核；否则是已审核
		 if(StringUtils.isBlank(orginfo.getOrgCode()) 
				 || StringUtils.isBlank(orginfo.getTfAccessory())){
			 orginfo.setAuditFstate(AuditFstate.AWAIT_AUDIT);
		 }else{
			 orginfo.setAuditFstate(AuditFstate.AWAIT_AUDIT);
		 }
		 validOrgInfoLength(orginfo);
		 
		if(orginfo.getOrgId() == null){//新增
//			目前新增机构只有医院
			LocalAssert.notBlank(orginfo.getOrgName(), "请输入医院名称");
			LocalAssert.notBlank(orginfo.getOrgAlias(), "请输入医院简称");
			if(0 != orgInfoService.findOrgNameExist(orginfo.getOrgName(),null)){
	             throw new ValidationException("机构名称已存在!");
	         }
			 if(StringUtils.isNotBlank(orginfo.getOrgCode()) && 0 != orgInfoService.findOrgCodeExist(orginfo.getOrgCode(),null)){
	             throw new ValidationException("机构代码已存在!");
	         }
			 orginfo.setOrgType(OrgType.HOSPITAL);//只有医院
			 orginfo.setCreateUserId(userid);
			 orginfo.setCreateUserName(username);
			 orginfo.setCreateTime(new Date());
	         orgInfoService.addUpdateOrgInfo(orginfo,null);
		}else{//编辑
			OrgInfo ooi = orgInfoService.find(OrgInfo.class, orginfo.getOrgId());
			Assert.notNull(ooi, "该机构不存在!");
			 if(StringUtils.isNotBlank(orginfo.getOrgCode()) && 0 != orgInfoService.findOrgCodeExist(orginfo.getOrgCode(),orginfo.getOrgId())){
	             throw new ValidationException("机构代码已存在!");
	         }
			ooi.setModefileUserId(userid);
			ooi.setModefileUserNmae(username);
			ooi.setModifyTime(new Date());
			ooi.setAuditFstate(orginfo.getAuditFstate());
			ooi.setTfProvince(orginfo.getTfProvince());
			ooi.setTfCity(orginfo.getTfCity());
			ooi.setTfDistrict(orginfo.getTfDistrict());
			orgInfoService.addUpdateOrgInfo(orginfo,ooi);//ooi原机构
		}
    }
    
    /**
     * 验证机构字段长度
     * @param orgInfo
     * @throws ValidationException 
     */
    public void validOrgInfoLength(OrgInfo orginfo) throws ValidationException{
         if(orginfo.getOrgName()!=null && orginfo.getOrgName().length()>100){
             throw new ValidationException("机构名称长度不能大于100个字符!");
         }      
         if(orginfo.getOrgAlias()!=null && orginfo.getOrgAlias().length()>50){
             throw new ValidationException("简称长度不能大于50个字符!");
         }
         if(orginfo.getOrgCode()!=null && orginfo.getOrgCode().length()>25){
             throw new ValidationException("机构代码长度不能大于25个字符!");
         }
         if(orginfo.getLxr()!=null && orginfo.getLxr().length()>25){
             throw new ValidationException("联系人长度不能大于25个字符!");
         }
         if(orginfo.getLxdh()!=null && orginfo.getLxdh().length()>25){
             throw new ValidationException("联系电话长度不能大于25个字符!");
         }
    }
    
    /**
     * 【数据字典】查询所有机构，做下拉框
     */
    @RequestMapping("/findOrgs")
	@ResponseBody
	public List<Map<String, Object>> findOrgs(String searchName, String orgType,
											  String orgId,
									 			   HttpServletRequest request) {
		request.setAttribute(ResResultBindingInterceptor.IGNORE_STD_RESULT, true);//忽略标准结果
		Pager<Map<String, Object>> pager = new Pager<Map<String, Object>>(false);
		
		pager.addQueryParam("searchName", searchName);
		pager.addQueryParam("orgId", orgId);
		pager.addQueryParam("orgTypes", StringUtils.isBlank(orgType) ? new String[]{OrgType.HOSPITAL}:new String[]{orgType});
		
		return orgInfoService.findOrgs(pager);
	}
}
  