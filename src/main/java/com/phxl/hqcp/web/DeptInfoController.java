package com.phxl.hqcp.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.ArrayUtils;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.interceptor.ResResultBindingInterceptor;
import com.phxl.core.base.util.LocalAssert;
import com.phxl.hqcp.common.constant.CustomConst.LoginUser;
import com.phxl.hqcp.entity.ConstrDept;
import com.phxl.hqcp.entity.ConstrDeptCheckbox;
import com.phxl.hqcp.entity.ConstrDeptInfo;
import com.phxl.hqcp.entity.ConstrDeptMeeting;
import com.phxl.hqcp.entity.ConstrDeptUser;
import com.phxl.hqcp.entity.ConstrDeptWork;
import com.phxl.hqcp.service.DeptInfoService;

/**
 * 手术室耗材申请
 * @author	taoyou
 */
@RequestMapping("/deptInfoController")
@Controller
public class DeptInfoController {

	@Autowired
	DeptInfoService deptInfoService;
	
	/**
	 * 
	 * getPyearList:(科室建设-查询时间列表). <br/> 
	 * 
	 * @Title: getPyearList
	 * @Description: TODO
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception    设定参数
	 * @return List<Map<String,Object>>    返回类型
	 * @throws
	 */
	@RequestMapping("/getPyearList")
    @ResponseBody
    public List<Map<String, Object>> getPyearList(HttpSession session,HttpServletRequest request) throws Exception {
        Pager pager = new Pager(false);
        pager.addQueryParam("selectScopeSubType", "01");
        pager.addQueryParam("pYmd", "P_YEAR");
        pager.addQueryParam("orgId", (Long)session.getAttribute(LoginUser.SESSION_USER_ORGID));//会话机构id
        request.setAttribute(ResResultBindingInterceptor.IGNORE_STD_RESULT, true);//忽略标准结果
        List<Map<String, Object>> list = deptInfoService.getPyearList(pager);
        return list;
    }
	
	/**
	 * 
	 * searchSelectScopeList:(科室建设-查看医院列表). <br/> 
	 * 
	 * @Title: searchSelectScopeList
	 * @Description: TODO
	 * @param page
	 * @param rows
	 * @param sortCol
	 * @param sortType
	 * @param searchName
	 * @param fstate
	 * @param pYear
	 * @param request
	 * @return    设定参数
	 * @return Pager    返回类型
	 * @throws
	 */
	@ResponseBody
    @RequestMapping("/searchSelectScopeList")
    public Pager searchSelectScopeList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pagesize", required = false) Integer rows,
            @RequestParam(value = "sidx", required = false) String sortCol,
            @RequestParam(value = "sord", required = false) String sortType,
            @RequestParam(value = "searchName", required = false)String searchName,
            @RequestParam(value = "fstate", required = false)String fstate,
            @RequestParam(value = "pYear", required = false)String pYear,
            HttpServletRequest request) {
        Pager pager = new Pager(true);
        if (page == null) {
            page = 1;
        }
        if (rows == null) {
            rows = 20;
        }
        pager.setPageNum(page);
        pager.setPageSize(rows);
        if (StringUtils.isNotBlank(sortCol)) {
            pager.addQueryParam("sortCol", sortCol);
        }
        if (StringUtils.isNotBlank(sortType)) {
            if("descend".equals(sortType)){//降序
                pager.addQueryParam("sortType", "desc");
            }
            if("ascend".equals(sortType)){//升序
                pager.addQueryParam("sortType", "asc");
            }
        }
        if (StringUtils.isNotBlank(searchName)) {
            pager.addQueryParam("searchName", searchName);
        }
        if (StringUtils.isNotBlank(pYear)) {
            pager.addQueryParam("pYear", pYear);
        }
        if (StringUtils.isNotBlank(fstate)) {
            pager.addQueryParam("fstate", fstate);
        }else{
            pager.addQueryParam("fstate", "00,10,20");
        }
        pager.addQueryParam("selectScopeSubType", "01");
        pager.addQueryParam("pYmd", "P_YEAR");
        pager.addQueryParam("qcScopeType", "02");
        pager.addQueryParam("orgId", request.getSession().getAttribute(LoginUser.SESSION_USER_ORGID));
        List<Map<String, Object>> result = deptInfoService.searchSelectScopeList(pager);
        pager.setRows(result);
        return pager;
    }
	
	/**
	 * 
	 * getDeptYearList:(科室上报-查询上报周期列表). <br/> 
	 * 
	 * @Title: getDeptYearList
	 * @Description: TODO
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception    设定参数
	 * @return List<Map<String,Object>>    返回类型
	 * @throws
	 */
	@RequestMapping("/getDeptYearList")
    @ResponseBody
    public List<Map<String, Object>> getDeptYearList(HttpSession session,HttpServletRequest request) throws Exception {
        Pager pager = new Pager(false);
        pager.addQueryParam("qcScopeSubType", "01");
        pager.addQueryParam("pYmd", "P_YEAR");
        pager.addQueryParam("qcScopeType", "02");
        pager.addQueryParam("orgId", (Long)session.getAttribute(LoginUser.SESSION_USER_ORGID));//会话机构id
        request.setAttribute(ResResultBindingInterceptor.IGNORE_STD_RESULT, true);//忽略标准结果
        List<Map<String, Object>> list = deptInfoService.getDeptYearList(pager);
        return list;
    }
	
	/**
	 * 
	 * searchConstrDept:(查询科室上报详情). <br/> 
	 * 
	 * @Title: searchConstrDept
	 * @Description: TODO
	 * @param pYear
	 * @param constrDeptGuid
	 * @param request
	 * @return    设定参数
	 * @return ConstrDept    返回类型
	 * @throws
	 */
	@ResponseBody
    @RequestMapping("/searchConstrDept")
    public ConstrDept searchConstrDept(@RequestParam(value = "pYear", required = false)String pYear,
                                       @RequestParam(value = "constrDeptGuid", required = false)String constrDeptGuid,
        HttpServletRequest request) throws ValidationException{
	    Long sessionOrgId = (Long)request.getSession().getAttribute(LoginUser.SESSION_USER_ORGID);
	    Assert.notNull(sessionOrgId, "会话用户机构id，不能为空!");
	    ConstrDept constrDept = new ConstrDept();
	    List<ConstrDept> list = new ArrayList<ConstrDept>();
	    if(StringUtils.isNotBlank(constrDeptGuid)){
	        constrDept = deptInfoService.find(ConstrDept.class, constrDeptGuid);
	        list.add(constrDept);
	    }else{
	        LocalAssert.notBlank(pYear, "请选择上报周期");
	        constrDept.setpYear(pYear);
	        constrDept.setOrgId(sessionOrgId);
	        constrDept.setQcScopeType("02");
	        constrDept.setpYmd("P_YEAR");
	        list = deptInfoService.searchList(constrDept);//查科室上报主表
	    }
	    if(list!=null && !list.isEmpty()){
	        constrDept = list.get(0);
	        if(constrDept!=null && StringUtils.isNotBlank(constrDept.getConstrDeptGuid())){
	            ConstrDeptInfo constrDeptInfo = new ConstrDeptInfo();
	            constrDeptInfo.setConstrDeptGuid(constrDept.getConstrDeptGuid());
	            List<ConstrDeptInfo> deptInfos = deptInfoService.searchList(constrDeptInfo);//查科室基本信息
	            if(deptInfos!=null && !deptInfos.isEmpty()){
	                constrDeptInfo = deptInfos.get(0);
	                if(constrDeptInfo!=null && StringUtils.isNotBlank(constrDeptInfo.getConstrDeptInfoGuid())){
	                    ConstrDeptCheckbox check = new ConstrDeptCheckbox();
	                    check.setConstrDeptDetailGuid(constrDeptInfo.getConstrDeptInfoGuid());
	                    check.setCheckboxType("DEPT_WORK_SCOPE");
	                    List<ConstrDeptCheckbox> workList = deptInfoService.searchList(check);//查询部门业务管理范围：多选
	                    if(workList!=null && !workList.isEmpty()){
	                        List<Map<String, Object>> workScopeList = new ArrayList<Map<String,Object>>();
	                        for(ConstrDeptCheckbox deptCheckbox:workList){
	                            if(deptCheckbox!=null && StringUtils.isNotBlank(deptCheckbox.getCheckboxDetailGuid())){
	                                Map<String, Object> map = new HashMap<String, Object>();
	                                map.put("workScopeValue", deptCheckbox.getFsort());
	                                map.put("workScopeName", deptCheckbox.getTfValue());
	                                workScopeList.add(map);
	                            }
	                        }
	                        if(workScopeList!=null && !workScopeList.isEmpty()){
	                            constrDeptInfo.setWorkScope(workScopeList);
	                        }
	                    }
	                    check = new ConstrDeptCheckbox();
	                    check.setConstrDeptDetailGuid(constrDeptInfo.getConstrDeptInfoGuid());
                        check.setCheckboxType("DEPT_WORK_OTHER");
                        List<ConstrDeptCheckbox> otherList = deptInfoService.searchList(check);//部门承担的其它工作：多选
                        if(otherList!=null && !otherList.isEmpty()){
                            List<Map<String, Object>> workOtherList = new ArrayList<Map<String,Object>>();
                            for(ConstrDeptCheckbox deptCheckbox:otherList){
                                if(deptCheckbox!=null && StringUtils.isNotBlank(deptCheckbox.getCheckboxDetailGuid())){
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("workOtherValue", deptCheckbox.getFsort());
                                    map.put("workOtherName", deptCheckbox.getTfValue());
                                    workOtherList.add(map);
                                }
                            }
                            if(workOtherList!=null && !workOtherList.isEmpty()){
                                constrDeptInfo.setWorkOther(workOtherList);
                            }
                        }
                        constrDept.setDeptInfo(constrDeptInfo);//设置科室基本信息
	                }
	            }
	            ConstrDeptWork constrDeptWork = new ConstrDeptWork();
	            constrDeptWork.setConstrDeptGuid(constrDept.getConstrDeptGuid());
                List<ConstrDeptWork> deptWorks = deptInfoService.searchList(constrDeptWork);//查科室业务信息
                if(deptWorks!=null && !deptWorks.isEmpty()){
                    constrDeptWork = deptWorks.get(0);
                    if(constrDeptWork!=null && StringUtils.isNotBlank(constrDeptWork.getConstrDeptWorkGuid())){
                        ConstrDeptCheckbox check = new ConstrDeptCheckbox();
                        check.setConstrDeptDetailGuid(constrDeptWork.getConstrDeptWorkGuid());
                        check.setCheckboxType("LOGISTICS_SCOPE");
                        List<ConstrDeptCheckbox> scopeList = deptInfoService.searchList(check);//医疗器械物流管理开展范围：多选
                        if(scopeList!=null && !scopeList.isEmpty()){
                            List<Map<String, Object>> logisticsScopeList = new ArrayList<Map<String,Object>>();
                            for(ConstrDeptCheckbox deptCheckbox:scopeList){
                                if(deptCheckbox!=null && StringUtils.isNotBlank(deptCheckbox.getCheckboxDetailGuid())){
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("logisticsScopeValue", deptCheckbox.getFsort());
                                    map.put("logisticsScopeName", deptCheckbox.getTfValue());
                                    logisticsScopeList.add(map);
                                }
                            }
                            if(logisticsScopeList!=null && !logisticsScopeList.isEmpty()){
                                constrDeptWork.setLogisticsScopeList(logisticsScopeList);
                            }
                        }
                        check = new ConstrDeptCheckbox();
                        check.setConstrDeptDetailGuid(constrDeptWork.getConstrDeptWorkGuid());
                        check.setCheckboxType("LOGISTICS_TYPE");
                        List<ConstrDeptCheckbox> typeList = deptInfoService.searchList(check);//卫生材料医疗器械物流管理模式：多选
                        if(typeList!=null && !typeList.isEmpty()){
                            List<Map<String, Object>> logisticsTypeList = new ArrayList<Map<String,Object>>();
                            for(ConstrDeptCheckbox deptCheckbox:typeList){
                                if(deptCheckbox!=null && StringUtils.isNotBlank(deptCheckbox.getCheckboxDetailGuid())){
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("logisticsTypeValue", deptCheckbox.getFsort());
                                    map.put("logisticsTypeName", deptCheckbox.getTfValue());
                                    logisticsTypeList.add(map);
                                }
                            }
                            if(logisticsTypeList!=null && !logisticsTypeList.isEmpty()){
                                constrDeptWork.setLogisticsTypeList(logisticsTypeList);
                            }
                        }
                        constrDept.setDeptWork(constrDeptWork);
                    }
                }
                ConstrDeptUser deptUser = new ConstrDeptUser();
                deptUser.setConstrDeptGuid(constrDept.getConstrDeptGuid());
                List<ConstrDeptUser> userList = deptInfoService.searchList(deptUser);//查建设科室-科室人员
                if(userList!=null && !userList.isEmpty()){
                    constrDept.setUserList(userList);
                }
                ConstrDeptMeeting deptMeeting = new ConstrDeptMeeting();
                deptMeeting.setConstrDeptGuid(constrDept.getConstrDeptGuid());
                List<ConstrDeptMeeting> meetingList = deptInfoService.searchList(deptMeeting);//建设科室-科室会议
                if(meetingList!=null && !meetingList.isEmpty()){
                    for(ConstrDeptMeeting meeting:meetingList){
                        if(meeting!=null && StringUtils.isNotBlank(meeting.getConstrDeptMeetingGuid()) && meeting.getMeetingDate()!=null){
                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
                            String meetingTime=sdf.format(meeting.getMeetingDate());
                            meeting.setMeetingTime(meetingTime);
                        }
                    }
                    constrDept.setMeetingList(meetingList);
                }
	        }
	    }else{
	        constrDept = new ConstrDept();
	    }
        return constrDept;
    }
	
	/**
	 * 
	 * insertEditConstrDept:(新增或编辑科室上报信息). <br/> 
	 * 
	 * @Title: insertEditConstrDept
	 * @Description: TODO
	 * @param request
	 * @param session
	 * @throws Exception    设定参数
	 * @return void    返回类型
	 * @throws
	 */
	@ResponseBody
    @RequestMapping("/insertEditConstrDept")
    public void insertEditConstrDept(
            HttpServletRequest request,
            HttpSession session) throws Exception{

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));//配置项:默认日期格式
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//配置项:忽略未知属性
        
        //获取会话中的信息
        String sessionUserId = (String)session.getAttribute(LoginUser.SESSION_USERID);
        String sessionUserName = (String)session.getAttribute(LoginUser.SESSION_USERNAME);
        Long sessionOrgId = (Long)session.getAttribute(LoginUser.SESSION_USER_ORGID);
        
        //科室上报信息
        ConstrDept constrDept = objectMapper.readValue(request.getReader(), ConstrDept.class);
        Assert.notNull(constrDept, "请填写科室上报信息!");
        LocalAssert.notBlank(constrDept.getpYear(), "请选择上报周期");
        Assert.notNull(constrDept.getSchedule(), "完成度不能为空!");
        if(constrDept.getSchedule().compareTo(new BigDecimal(0))==0){
            throw new ValidationException("完成度为0，请填写上报信息!");
        }
        if(StringUtils.isNotBlank(constrDept.getAuditFstate()) && ("10".equals(constrDept.getAuditFstate()) || "20".equals(constrDept.getAuditFstate()))){
            throw new ValidationException("已提交过的数据不可编辑!");
        }
        if(StringUtils.isBlank(constrDept.getConstrDeptGuid())){
            //新增科室上报,验证当前机构同一个上报周期是否已经上报过数据
            ConstrDept entity = new ConstrDept();
            entity.setOrgId(sessionOrgId);
            entity.setQcScopeType("02");
            entity.setpYear(constrDept.getpYear());
            entity.setpYmd("P_YEAR");
            List<ConstrDept> list = deptInfoService.searchList(entity);
            if(list!=null && !list.isEmpty()){//当前机构同一个上报周期已经上报过数据,则不可重复上报
                throw new ValidationException("改机构已上报过"+constrDept.getpYear()+"年的数据，不可重复上报!");
            }
        }
        //新增或编辑科室上报信息
        deptInfoService.insertEditConstrDept(constrDept,sessionUserId,sessionUserName,sessionOrgId);
    }

	/**
	 * 
	 * searchConstrDeptAuditList:(查询科室审核列表). <br/> 
	 * 
	 * @Title: searchConstrDeptAuditList
	 * @Description: TODO
	 * @param page
	 * @param rows
	 * @param sortCol
	 * @param sortType
	 * @param orgName
	 * @param fstate
	 * @param pYear
	 * @param request
	 * @return    设定参数
	 * @return Pager    返回类型
	 * @throws
	 */
	@ResponseBody
    @RequestMapping("/searchConstrDeptAuditList")
    public Pager searchConstrDeptAuditList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pagesize", required = false) Integer rows,
            @RequestParam(value = "sidx", required = false) String sortCol,
            @RequestParam(value = "sord", required = false) String sortType,
            @RequestParam(value = "orgName", required = false)String orgName,
            @RequestParam(value = "fstate", required = false)String fstate,
            @RequestParam(value = "pYear", required = false)String pYear,
            HttpServletRequest request) throws Exception{
	    LocalAssert.notBlank(pYear, "上报时间，不能为空");
        Pager pager = new Pager(true);
        if (page == null) {
            page = 1;
        }
        if (rows == null) {
            rows = 20;
        }
        pager.setPageNum(page);
        pager.setPageSize(rows);
        if (StringUtils.isNotBlank(sortCol)) {
            pager.addQueryParam("sortCol", sortCol);
        }
        if (StringUtils.isNotBlank(sortType)) {
            if("descend".equals(sortType)){//降序
                pager.addQueryParam("sortType", "desc");
            }
            if("ascend".equals(sortType)){//升序
                pager.addQueryParam("sortType", "asc");
            }
        }
        if (StringUtils.isNotBlank(orgName)) {
            pager.addQueryParam("orgName", orgName);
        }
        if (StringUtils.isNotBlank(pYear)) {
            pager.addQueryParam("pYear", pYear);
        }
        if (StringUtils.isNotBlank(fstate)) {
            pager.addQueryParam("fstate", fstate);
        }else{
            pager.addQueryParam("fstate", "00,10,20");
        }
        pager.addQueryParam("qcScopeSubType", "01");
        pager.addQueryParam("pYmd", "P_YEAR");
        pager.addQueryParam("qcScopeType", "02");
        pager.addQueryParam("orgId", request.getSession().getAttribute(LoginUser.SESSION_USER_ORGID));
        List<Map<String, Object>> result = deptInfoService.searchConstrDeptAuditList(pager);
        pager.setRows(result);
        return pager;
    }
	
	/**
	 * 
	 * updateConstrDept:(审核科室上报信息). <br/> 
	 * 
	 * @Title: updateConstrDept
	 * @Description: TODO
	 * @param constrDeptGuid
	 * @param auditFstate
	 * @param request
	 * @throws Exception    设定参数
	 * @return void    返回类型
	 * @throws
	 */
	@RequestMapping("/updateConstrDept")
    @ResponseBody
    public void updateConstrDept(@RequestParam(value = "constrDeptGuid", required = false)String constrDeptGuid,
                                 @RequestParam(value = "auditFstate", required = false)String auditFstate,
        HttpServletRequest request) throws Exception {
	    LocalAssert.notBlank(constrDeptGuid, "科室上报信息主键，不能为空!");
	    LocalAssert.notBlank(auditFstate, "审核状态，不能为空!");
	    ConstrDept entity = deptInfoService.find(ConstrDept.class, constrDeptGuid);
	    Assert.notNull(entity, "科室上报信息（constrDeptGuid: " + constrDeptGuid + "），不存在");
	    if(entity.getSchedule().compareTo(new BigDecimal(1))!=0){
            throw new ValidationException("科室上报信息完成度未达到100%，信息不完整，无法通过审核!");
        }
	    if(!"10".equals(entity.getAuditFstate())){
	        throw new ValidationException("科室上报信息未提交，无法通过审核!");
	    }
	    //获取会话中的信息
        String sessionUserId = (String)request.getSession().getAttribute(LoginUser.SESSION_USERID);
        String sessionUserName = (String)request.getSession().getAttribute(LoginUser.SESSION_USERNAME);
        entity.setAuditId(sessionUserId);
        entity.setAuditName(sessionUserName);
        entity.setAuditFstate(auditFstate);
        entity.setAuditTime(new Date());
        entity.setModifyTime(new Date());
        entity.setModefileUserId(sessionUserId);
        entity.setModefileUserNmae(sessionUserName);
        deptInfoService.updateInfo(entity);
    }
	
	/**
	 * 
	 * searchConstrDeptUserList:(科室人员信息列表——科室建设详情). <br/> 
	 * 
	 * @Title: searchConstrDeptUserList
	 * @Description: TODO
	 * @param page
	 * @param rows
	 * @param sortCol
	 * @param sortType
	 * @param constrDeptGuid
	 * @param request
	 * @return    设定参数
	 * @return Pager    返回类型
	 * @throws
	 */
	@ResponseBody
    @RequestMapping("/searchConstrDeptUserList")
    public Pager searchConstrDeptUserList(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pagesize", required = false) Integer rows,
            @RequestParam(value = "sidx", required = false) String sortCol,
            @RequestParam(value = "sord", required = false) String sortType,
            @RequestParam(value = "constrDeptGuid", required = false)String constrDeptGuid,
            HttpServletRequest request) {
        Pager pager = new Pager(true);
        if (page == null) {
            page = 1;
        }
        if (rows == null) {
            rows = 5;
        }
        pager.setPageNum(page);
        pager.setPageSize(rows);
        if (StringUtils.isNotBlank(sortCol)) {
            pager.addQueryParam("sortCol", sortCol);
        }
        if (StringUtils.isNotBlank(sortType)) {
            if("descend".equals(sortType)){//降序
                pager.addQueryParam("sortType", "desc");
            }
            if("ascend".equals(sortType)){//升序
                pager.addQueryParam("sortType", "asc");
            }
        }
        if (StringUtils.isNotBlank(constrDeptGuid)) {
            pager.addQueryParam("constrDeptGuid", constrDeptGuid);
        }
        List<Map<String, Object>> result = deptInfoService.searchConstrDeptUserList(pager);
        pager.setRows(result);
        return pager;
    }
	
	/**
	 * 
	 * getDeptInfo:(按年度查询机构的床位数、机构员工总数、医工人员总数、医工培训总数). <br/> 
	 * 
	 * @Title: getDeptInfo
	 * @Description: TODO
	 * @param orgId
	 * @param pYear
	 * @param constrDeptGuid
	 * @param request
	 * @return
	 * @throws Exception    设定参数
	 * @return Map<String,Object>    返回类型
	 * @throws
	 */
	@ResponseBody
    @RequestMapping("/getDeptInfo")
    public Map<String, Object> getDeptInfo(
            @RequestParam(value = "orgId", required = false) Long orgId,
            @RequestParam(value = "pYear", required = false) String pYear,
            @RequestParam(value = "constrDeptGuid", required = false)String constrDeptGuid,
            HttpServletRequest request) throws Exception {
	    LocalAssert.notBlank(pYear, "请选择时间!");
        Assert.notNull(orgId, "机构ID，不能为空");
        Pager pager = new Pager(false);
        pager.addQueryParam("orgId", orgId);
        pager.addQueryParam("pYear", pYear);
        if (StringUtils.isNotBlank(constrDeptGuid)) {
            pager.addQueryParam("constrDeptGuid", constrDeptGuid);
        }
        Map<String, Object> result = deptInfoService.getDeptInfo(pager);
        return result;
    }
	
	/**
	 * 
	 * getDeptUserAge:(按年度查询医工人员年龄情况（科室建设）). <br/> 
	 * 
	 * @Title: getDeptUserAge
	 * @Description: TODO
	 * @param orgId
	 * @param pYear
	 * @param constrDeptGuid
	 * @param request
	 * @return
	 * @throws Exception    设定参数
	 * @return Map<String,Object>    返回类型
	 * @throws
	 */
	@ResponseBody
    @RequestMapping("/getDeptUserAge")
    public Map<String, Object> getDeptUserAge(
            @RequestParam(value = "orgId", required = false) Long orgId,
            @RequestParam(value = "pYear", required = false) String pYear,
            @RequestParam(value = "constrDeptGuid", required = false)String constrDeptGuid,
            HttpServletRequest request) throws Exception {
        LocalAssert.notBlank(pYear, "请选择时间!");
        Assert.notNull(orgId, "机构ID，不能为空");
        Pager pager = new Pager(false);
        pager.addQueryParam("orgId", orgId);
        pager.addQueryParam("pYear", pYear);
        if (StringUtils.isNotBlank(constrDeptGuid)) {
            pager.addQueryParam("constrDeptGuid", constrDeptGuid);
        }
        Map<String, Object> result = deptInfoService.getDeptUserAge(pager);
        return result;
    }
	
	/**
	 * 
	 * getDeptUserEducation:(按年度查询医工人员学历情况（科室建设）). <br/> 
	 * 
	 * @Title: getDeptUserEducation
	 * @Description: TODO
	 * @param orgId
	 * @param pYear
	 * @param constrDeptGuid
	 * @param request
	 * @return
	 * @throws Exception    设定参数
	 * @return Map<String,Object>    返回类型
	 * @throws
	 */
	@ResponseBody
    @RequestMapping("/getDeptUserEducation")
    public Map<String, Object> getDeptUserEducation(
            @RequestParam(value = "orgId", required = false) Long orgId,
            @RequestParam(value = "pYear", required = false) String pYear,
            @RequestParam(value = "constrDeptGuid", required = false)String constrDeptGuid,
            HttpServletRequest request) throws Exception {
        LocalAssert.notBlank(pYear, "请选择时间!");
        Assert.notNull(orgId, "机构ID，不能为空");
        Pager pager = new Pager(false);
        pager.addQueryParam("orgId", orgId);
        pager.addQueryParam("pYear", pYear);
        if (StringUtils.isNotBlank(constrDeptGuid)) {
            pager.addQueryParam("constrDeptGuid", constrDeptGuid);
        }
        Map<String, Object> result = deptInfoService.getDeptUserEducation(pager);
        return result;
    }
	
	/**
	 * 
	 * getDeptUserMajor:(按年度查询医工人员专业情况（科室建设）). <br/> 
	 * 
	 * @Title: getDeptUserMajor
	 * @Description: TODO
	 * @param orgId
	 * @param pYear
	 * @param constrDeptGuid
	 * @param request
	 * @return
	 * @throws Exception    设定参数
	 * @return Map<String,Object>    返回类型
	 * @throws
	 */
	@ResponseBody
    @RequestMapping("/getDeptUserMajor")
    public Map<String, Object> getDeptUserMajor(
            @RequestParam(value = "orgId", required = false) Long orgId,
            @RequestParam(value = "pYear", required = false) String pYear,
            @RequestParam(value = "constrDeptGuid", required = false)String constrDeptGuid,
            HttpServletRequest request) throws Exception {
        LocalAssert.notBlank(pYear, "请选择时间!");
        Assert.notNull(orgId, "机构ID，不能为空");
        Pager pager = new Pager(false);
        pager.addQueryParam("orgId", orgId);
        pager.addQueryParam("pYear", pYear);
        if (StringUtils.isNotBlank(constrDeptGuid)) {
            pager.addQueryParam("constrDeptGuid", constrDeptGuid);
        }
        Map<String, Object> result = deptInfoService.getDeptUserMajor(pager);
        return result;
    }
	
}
