package com.phxl.hqcp.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.interceptor.ResResultBindingInterceptor;
import com.phxl.core.base.util.JSONUtils;
import com.phxl.core.base.util.LocalAssert;
import com.phxl.hqcp.common.constant.CustomConst.DeptParentName;
import com.phxl.hqcp.common.constant.CustomConst.DeptTypeName;
import com.phxl.hqcp.common.constant.CustomConst.DeptWorkOther;
import com.phxl.hqcp.common.constant.CustomConst.DeptWorkScope;
import com.phxl.hqcp.common.constant.CustomConst.LoginUser;
import com.phxl.hqcp.common.constant.CustomConst.LogisticsScope;
import com.phxl.hqcp.common.constant.CustomConst.LogisticsType;
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
    public Map<String, Object> searchConstrDept(@RequestParam(value = "pYear", required = false)String pYear,
                                       @RequestParam(value = "constrDeptGuid", required = false)String constrDeptGuid,
        HttpServletRequest request) throws ValidationException{
	    Long sessionOrgId = (Long)request.getSession().getAttribute(LoginUser.SESSION_USER_ORGID);
	    Assert.notNull(sessionOrgId, "会话用户机构id，不能为空!");
	    Pager pager = new Pager(false);
        pager.addQueryParam("pYear", pYear);
        if(StringUtils.isNotBlank(constrDeptGuid)){
            pager.addQueryParam("constrDeptGuid", constrDeptGuid);
        }else{
            pager.addQueryParam("orgId", sessionOrgId);
        }
        Map<String, Object> result = deptInfoService.searchConstrDept(pager);
	   /* ConstrDept constrDept = new ConstrDept();
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
	    }*/
        return result;
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

        //获取会话中的信息
        String sessionUserId = (String)session.getAttribute(LoginUser.SESSION_USERID);
        String sessionUserName = (String)session.getAttribute(LoginUser.SESSION_USERNAME);
        Long sessionOrgId = (Long)session.getAttribute(LoginUser.SESSION_USER_ORGID);
        
        ConstrDept constrDept = new ConstrDept();
        
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String body = buffer.toString();
        JsonParser parser = new JsonParser();
        JsonElement pje = parser.parse(body);
        if(pje.isJsonObject()){
            JsonObject jo = pje.getAsJsonObject();
            Set<Entry<String, JsonElement>> entrySet = jo.entrySet();
            Set<String> userSet = new HashSet<String>();
            Set<String> meetingSet = new HashSet<String>();
            if(entrySet!=null && !entrySet.isEmpty()){
                for (Iterator<Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ){
                    Entry<String, JsonElement> entry = iter.next();
                    String key = entry.getKey();
                    if(StringUtils.isNotBlank(key) && key.indexOf("fname")!=-1){
                        userSet.add(key.substring(key.length()-1));
                    }else if(StringUtils.isNotBlank(key) && key.indexOf("meetingName")!=-1){
                        meetingSet.add(key.substring(key.length()-1));
                    }
                }
            }
            
            constrDept = JSONUtils.toBean(body, ConstrDept.class);
            Assert.notNull(constrDept, "科室上报的信息，不能为空!");
            //部门基本情况
            ConstrDeptInfo deptInfo = new ConstrDeptInfo();//deptTypeName
            deptInfo.setDeptName(constrDept.getDeptName());
            if(StringUtils.isNotBlank(constrDept.getDeptTypeName())){//部门级别
                if("1".equals(constrDept.getDeptTypeName())){
                    deptInfo.setDeptTypeName(DeptTypeName.BU);
                }else if("2".equals(constrDept.getDeptTypeName())){
                    deptInfo.setDeptTypeName(DeptTypeName.KE);
                }else if("3".equals(constrDept.getDeptTypeName())){
                    deptInfo.setDeptTypeName(DeptTypeName.ZU);
                }else{
                    deptInfo.setDeptTypeName(constrDept.getDeptTypeOther());
                }
            }
            if(StringUtils.isNotBlank(constrDept.getDeptParentName())){//上级管理部门
                if("1".equals(constrDept.getDeptParentName())){
                    deptInfo.setDeptParentName(DeptParentName.YW);
                }else if("2".equals(constrDept.getDeptParentName())){
                    deptInfo.setDeptParentName(DeptParentName.HQ);
                }else if("3".equals(constrDept.getDeptParentName())){
                    deptInfo.setDeptParentName(DeptParentName.KJ);
                }else if("4".equals(constrDept.getDeptParentName())){
                    deptInfo.setDeptParentName(DeptParentName.DL);
                }else{
                    deptInfo.setDeptParentName(constrDept.getDeptParentOther());
                }
            }
            if(constrDept.getWorkScope()!=null && constrDept.getWorkScope().length>0){//部门业务管理范围
                List<Map<String, Object>> workScope = new ArrayList<Map<String,Object>>();
                for(int k=0;k<constrDept.getWorkScope().length;k++){
                    String val = constrDept.getWorkScope()[k];
                    Map<String, Object> scopeMap = new HashMap<String, Object>();
                    scopeMap.put("workScopeValue", val);
                    if("1".equals(val)){
                        scopeMap.put("workScopeName", DeptWorkScope.SB);
                    }else if("2".equals(val)){
                        scopeMap.put("workScopeName", DeptWorkScope.HC);
                    }else if("3".equals(val)){
                        scopeMap.put("workScopeName", DeptWorkScope.SJ);
                    }else if("4".equals(val)){
                        scopeMap.put("workScopeName", DeptWorkScope.YP);
                    }else if("5".equals(val)){
                        scopeMap.put("workScopeName", DeptWorkScope.BG);
                    }else if("6".equals(val)){
                        scopeMap.put("workScopeName", DeptWorkScope.ZW);
                    }else{
                        scopeMap.put("workScopeName", constrDept.getWorkScopeOther());
                    }
                    workScope.add(scopeMap);
                }
                if(workScope!=null && !workScope.isEmpty()){
                    deptInfo.setWorkScope(workScope);
                }
            }
            if(constrDept.getWorkOther()!=null && constrDept.getWorkOther().length>0){//部门承担的其它工作
                List<Map<String, Object>> workOther = new ArrayList<Map<String,Object>>();
                for(int m=0;m<constrDept.getWorkOther().length;m++){
                    String val = constrDept.getWorkOther()[m];
                    Map<String, Object> otherMap = new HashMap<String, Object>();
                    otherMap.put("workOtherValue", val);
                    if("1".equals(val)){
                        otherMap.put("workOtherName", DeptWorkOther.XZBM);
                    }else if("2".equals(val)){
                        otherMap.put("workOtherName", DeptWorkOther.YJBM);
                    }else if("3".equals(val)){
                        otherMap.put("workOtherName", constrDept.getWorkMassName());
                    }else{
                        otherMap.put("workOtherName", constrDept.getWorkOtherName());
                    }
                    workOther.add(otherMap);
                }
                if(workOther!=null && !workOther.isEmpty()){
                    deptInfo.setWorkOther(workOther);
                }
            }
            if(deptInfo!=null){
                constrDept.setDeptInfo(deptInfo);
            }
            //部门相关业务
            ConstrDeptWork deptWork = new ConstrDeptWork();
            deptWork.setEquipmentSum(constrDept.getEquipmentSum());
            deptWork.setEquipmentValue(constrDept.getEquipmentValue());
            deptWork.setAbEquipmentSum(constrDept.getAbEquipmentSum());
            deptWork.setAbEquipmentValue(constrDept.getAbEquipmentValue());
            deptWork.setConsuSum(constrDept.getConsuSum());
            deptWork.setConsuValue(constrDept.getConsuValue());
            deptWork.setHighConsuSum(constrDept.getHighConsuSum());
            deptWork.setHighConsuValue(constrDept.getHighConsuValue());
            if(constrDept.getLogisticsScope()!=null && constrDept.getLogisticsScope().length>0){//医疗器械物流管理开展范围
                List<Map<String, Object>> logisticsScopeList = new ArrayList<Map<String,Object>>();
                for(int k=0;k<constrDept.getLogisticsScope().length;k++){
                    String val = constrDept.getLogisticsScope()[k];
                    Map<String, Object> logisticsScopeMap = new HashMap<String, Object>();
                    logisticsScopeMap.put("logisticsScopeValue", val);
                    if("1".equals(val)){
                        logisticsScopeMap.put("logisticsScopeName", LogisticsScope.SBWL);
                    }else if("2".equals(val)){
                        logisticsScopeMap.put("logisticsScopeName", LogisticsScope.WSWL);
                    }else if("3".equals(val)){
                        logisticsScopeMap.put("logisticsScopeName", LogisticsScope.ZRWL);
                    }else if("4".equals(val)){
                        logisticsScopeMap.put("logisticsScopeName", LogisticsScope.XDWL);
                    }else if("5".equals(val)){
                        logisticsScopeMap.put("logisticsScopeName", LogisticsScope.SJWL);
                    }else if("6".equals(val)){
                        logisticsScopeMap.put("logisticsScopeName", LogisticsScope.FQWL);
                    }
                    logisticsScopeList.add(logisticsScopeMap);
                }
                if(logisticsScopeList!=null && !logisticsScopeList.isEmpty()){
                    deptWork.setLogisticsScopeList(logisticsScopeList);
                }
            }
            if(constrDept.getLogisticsType()!=null && constrDept.getLogisticsType().length>0){//卫生材料医疗器械物流管理模式
                List<Map<String, Object>> logisticsTypeList = new ArrayList<Map<String,Object>>();
                for(int m=0;m<constrDept.getLogisticsType().length;m++){
                    String val = constrDept.getLogisticsType()[m];
                    Map<String, Object> logisticsTypeMap = new HashMap<String, Object>();
                    logisticsTypeMap.put("logisticsTypeValue", val);
                    if("1".equals(val)){
                        logisticsTypeMap.put("logisticsTypeName", LogisticsType.WLWB);
                    }else if("2".equals(val)){
                        logisticsTypeMap.put("logisticsTypeName", LogisticsType.CKWB);
                    }else if("3".equals(val)){
                        logisticsTypeMap.put("logisticsTypeName", LogisticsType.LKC);
                    }else if("4".equals(val)){
                        logisticsTypeMap.put("logisticsTypeName", LogisticsType.DSFTG);
                    }else if("5".equals(val)){
                        logisticsTypeMap.put("logisticsTypeName", LogisticsType.EJKG);
                    }else{
                        logisticsTypeMap.put("logisticsTypeName", constrDept.getLogisticsTypeOther());
                    }
                    logisticsTypeList.add(logisticsTypeMap);
                }
                if(logisticsTypeList!=null && !logisticsTypeList.isEmpty()){
                    deptWork.setLogisticsTypeList(logisticsTypeList);
                }
            }
            if(deptWork!=null){
                constrDept.setDeptWork(deptWork);
            }
            if(userSet!=null && !userSet.isEmpty()){//循环取部门人员信息，并存入列表
               List<ConstrDeptUser> userList = new ArrayList<ConstrDeptUser>();
               for(String i:userSet){
                   ConstrDeptUser deptUser = new ConstrDeptUser();
                   deptUser.setFname((jo.get("fname-"+i)==null || jo.get("fname-"+i).isJsonNull())?"":jo.get("fname-"+i).getAsString());
                   String gender = (jo.get("gender-"+i)==null || jo.get("gender-"+i).isJsonNull())?"":jo.get("gender-"+i).getAsString();
                   if(StringUtils.isNotBlank(gender)){
                       deptUser.setGender(deptInfoService.findDictCode("GENDER", gender));
                   }
                   String birthChar = (jo.get("birthChar-"+i)==null || jo.get("birthChar-"+i).isJsonNull())?"":jo.get("birthChar-"+i).getAsString();
                   if(StringUtils.isNotBlank(birthChar)){
                       deptUser.setBirthChar(deptInfoService.findDictCode("BIRTH_CHAR", birthChar));
                   }
                   JsonArray Jarray = (jo.get("technicalTitles-"+i)==null || jo.get("technicalTitles-"+i).isJsonNull())?null:jo.get("technicalTitles-"+i).getAsJsonArray();
                   String technicalTitlesA = "";
                   String technicalTitlesB = "";
                   if(Jarray!=null && Jarray.size()>0){
                       technicalTitlesA = Jarray.get(0).getAsString();
                       technicalTitlesB = Jarray.get(1).getAsString();
                   }
                   if(StringUtils.isNotBlank(technicalTitlesA)){
                       deptUser.setTechnicalTitlesA(deptInfoService.findDictCode("TECHNICAL_TITLES_A", technicalTitlesA));
                   }
                   if(StringUtils.isNotBlank(technicalTitlesB)){
                       deptUser.setTechnicalTitlesB(deptInfoService.findDictCode("TECHNICAL_TITLES_B", technicalTitlesB));
                   }
                   deptUser.setPostName((jo.get("postName-"+i)==null || jo.get("postName-"+i).isJsonNull())?"":jo.get("postName-"+i).getAsString());
                   deptUser.setPostAge((jo.get("postAge-"+i)==null || jo.get("postAge-"+i).isJsonNull())?null:jo.get("postAge-"+i).getAsShort());
                   String highestEducation = (jo.get("highestEducation-"+i)==null || jo.get("highestEducation-"+i).isJsonNull())?"":jo.get("highestEducation-"+i).getAsString();
                   if(StringUtils.isNotBlank(highestEducation)){
                       deptUser.setHighestEducation(deptInfoService.findDictCode("XL", highestEducation));
                   }
                   String majorName = (jo.get("majorName-"+i)==null || jo.get("majorName-"+i).isJsonNull())?"":jo.get("majorName-"+i).getAsString();
                   if(StringUtils.isNotBlank(majorName)){
                       deptUser.setMajorName(deptInfoService.findDictCode("ZY", majorName));
                   }
                   userList.add(deptUser);
               }
               if(userList!=null && !userList.isEmpty()){//将部门人员信息列表存入实体类中
                   constrDept.setUserList(userList);
               }
            }
            if(meetingSet!=null && !meetingSet.isEmpty()){//循环取部门培训信息，并存入列表
                List<ConstrDeptMeeting> meetingList = new ArrayList<ConstrDeptMeeting>();
                for(String j:meetingSet){
                    ConstrDeptMeeting deptMeeting = new ConstrDeptMeeting();//
                    deptMeeting.setMeetingName((jo.get("meetingName-"+j)==null || jo.get("meetingName-"+j).isJsonNull())?"":jo.get("meetingName-"+j).getAsString());
                    String meetingType = (jo.get("meetingType-"+j)==null || jo.get("meetingType-"+j).isJsonNull())?"":jo.get("meetingType-"+j).getAsString();
                    if(StringUtils.isNotBlank(meetingType)){
                        deptMeeting.setMeetingType(deptInfoService.findDictCode("HYLX", meetingType));
                    }
                    String meetingTime = (jo.get("meetingTime-"+j)==null || jo.get("meetingTime-"+j).isJsonNull())?"":jo.get("meetingTime-"+j).getAsString();
                    if(StringUtils.isNotBlank(meetingTime)){
                        meetingTime = meetingTime.substring(0, 10);
                        deptMeeting.setMeetingTime(meetingTime);
                    }
                    deptMeeting.setMeetingAddress((jo.get("meetingAddress-"+j)==null || jo.get("meetingAddress-"+j).isJsonNull())?"":jo.get("meetingAddress-"+j).getAsString());
                    deptMeeting.setMeetingSponsor((jo.get("meetingSponsor-"+j)==null || jo.get("meetingSponsor-"+j).isJsonNull())?"":jo.get("meetingSponsor-"+j).getAsString());
                    deptMeeting.setMeetingAllUserSum((jo.get("meetingAllUserSum-"+j)==null || jo.get("meetingAllUserSum-"+j).isJsonNull())?null:jo.get("meetingAllUserSum-"+j).getAsLong());
                    deptMeeting.setMeetingDeptUserSum((jo.get("meetingDeptUserSum-"+j)==null || jo.get("meetingDeptUserSum-"+j).isJsonNull())?null:jo.get("meetingDeptUserSum-"+j).getAsLong());
                    deptMeeting.setTfRemark((jo.get("tfRemark-"+j)==null || jo.get("tfRemark-"+j).isJsonNull())?"":jo.get("tfRemark-"+j).getAsString());
                    meetingList.add(deptMeeting);
                }
                if(meetingList!=null && !meetingList.isEmpty()){//将部门培训信息列表存入实体类中
                    constrDept.setMeetingList(meetingList);
                }
            }
        }
        
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
        if(constrDept.getMeetingList()!=null && !constrDept.getMeetingList().isEmpty()){
            for(ConstrDeptMeeting meeting:constrDept.getMeetingList()){
                if(meeting!=null && StringUtils.isNotBlank(meeting.getMeetingTime()) && !constrDept.getpYear().equals(meeting.getMeetingTime().substring(0, 4))){
                    throw new ValidationException("培训时间不在上报周期内，请重新填写!");
                }
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
            @RequestParam(value = "orgId", required = false) Long orgId,
            @RequestParam(value = "pYear", required = false) String pYear,
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
        if (StringUtils.isNotBlank(pYear)) {
            pager.addQueryParam("pYear", pYear);
        }
        if (orgId!=null) {
            pager.addQueryParam("orgId", orgId);
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
	
	/**
	 * 
	 * getOrgInfoTb:(按年度查询监管机构下的机构总数、三甲机构数、二甲机构数、及同比). <br/> 
	 * 
	 * @Title: getOrgInfoTb
	 * @Description: TODO
	 * @param orgId
	 * @param ymd
	 * @param request
	 * @return
	 * @throws Exception    设定参数
	 * @return Map<String,Object>    返回类型
	 * @throws
	 */
	@ResponseBody
    @RequestMapping("/getOrgInfoTb")
    public Map<String, Object> getOrgInfoTb(
            @RequestParam(value = "ymd", required = false) String ymd,
            HttpServletRequest request) throws Exception {
        LocalAssert.notBlank(ymd, "请选择时间!");
        Pager pager = new Pager(false);
        pager.addQueryParam("orgId", request.getSession().getAttribute(LoginUser.SESSION_USER_ORGID));
        pager.addQueryParam("ymd", ymd.substring(0,4));
        Map<String, Object> result = deptInfoService.getOrgInfoTb(pager);
        return result;
    }
	
	/**
	 * 
	 * getOrgDeptInfoByGender:(按年度查询监管机构下医工人数男女比例). <br/> 
	 * 
	 * @Title: getOrgDeptInfoByGender
	 * @Description: TODO
	 * @param orgId
	 * @param ymd
	 * @param request
	 * @return
	 * @throws Exception    设定参数
	 * @return Map<String,Object>    返回类型
	 * @throws
	 */
	@ResponseBody
    @RequestMapping("/getOrgDeptInfoByGender")
    public Map<String, Object> getOrgDeptInfoByGender(
            @RequestParam(value = "ymd", required = false) String ymd,
            HttpServletRequest request) throws Exception {
        LocalAssert.notBlank(ymd, "请选择时间!");
        Pager pager = new Pager(false);
        pager.addQueryParam("ymd", ymd.substring(0,4));
        Map<String, Object> result = deptInfoService.getOrgDeptInfoByGender(pager);
        return result;
    }
	
	/**
	 * 
	 * getOrgEducation:(查询医工人员学历). <br/> 
	 * 
	 * @Title: getOrgEducation
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
    @RequestMapping("/getOrgEducation")
    public Map<String, Object> getOrgEducation(
            @RequestParam(value = "ymd", required = false) String ymd,
            HttpServletRequest request) throws Exception {
        LocalAssert.notBlank(ymd, "请选择时间!");
        Pager pager = new Pager(false);
        pager.addQueryParam("orgId", request.getSession().getAttribute(LoginUser.SESSION_USER_ORGID));
        pager.addQueryParam("ymd", ymd.substring(0,4));
        Map<String, Object> result = deptInfoService.getOrgEducation(pager);
        return result;
    }
	
	/**
	 * 
	 * getAdverseEvents:(按年度查询不良事件上报率). <br/> 
	 * 
	 * @Title: getAdverseEvents
	 * @Description: TODO
	 * @param orgId
	 * @param ymd
	 * @param request
	 * @return
	 * @throws Exception    设定参数
	 * @return Map<String,Object>    返回类型
	 * @throws
	 */
	@ResponseBody
    @RequestMapping("/getAdverseEvents")
    public Map<String, Object> getAdverseEvents(
            @RequestParam(value = "ymd", required = false) String ymd,
            HttpServletRequest request) throws Exception {
        LocalAssert.notBlank(ymd, "请选择时间!");
        Pager pager = new Pager(false);
        pager.addQueryParam("orgId", request.getSession().getAttribute(LoginUser.SESSION_USER_ORGID));
        pager.addQueryParam("ymd", ymd);
        Map<String, Object> result = deptInfoService.getAdverseEvents(pager);
        return result;
    }
	
	/**
	 * 
	 * getMaterialTraceability:(按年度查询耗材追溯分析). <br/> 
	 * 
	 * @Title: getMaterialTraceability
	 * @Description: TODO
	 * @param orgId
	 * @param ymd
	 * @param request
	 * @return
	 * @throws Exception    设定参数
	 * @return Map<String,Object>    返回类型
	 * @throws
	 */
	@ResponseBody
    @RequestMapping("/getMaterialTraceability")
    public Map<String, Object> getMaterialTraceability(
            @RequestParam(value = "ymd", required = false) String ymd,
            HttpServletRequest request) throws Exception {
        LocalAssert.notBlank(ymd, "请选择时间!");
        Pager pager = new Pager(false);
        pager.addQueryParam("orgId", request.getSession().getAttribute(LoginUser.SESSION_USER_ORGID));
        pager.addQueryParam("ymd", ymd);
        Map<String, Object> result = deptInfoService.getMaterialTraceability(pager);
        return result;
    }
	
	/**
	 * 
	 * getOrgAllLevel:(机构分布). <br/> 
	 * 
	 * @Title: getOrgAllLevel
	 * @Description: TODO
	 * @param orgId
	 * @param ymd
	 * @param request
	 * @return
	 * @throws Exception    设定参数
	 * @return Map<String,Object>    返回类型
	 * @throws
	 */
	@ResponseBody
    @RequestMapping("/getOrgAllLevel")
    public Map<String, Object> getOrgAllLevel(
            @RequestParam(value = "ymd", required = false) String ymd,
            HttpServletRequest request) throws Exception {
        LocalAssert.notBlank(ymd, "请选择时间!");
        Pager pager = new Pager(false);
        pager.addQueryParam("orgId", request.getSession().getAttribute(LoginUser.SESSION_USER_ORGID));
        pager.addQueryParam("ymd", ymd.substring(0,4));
        Map<String, Object> result = deptInfoService.getOrgAllLevel(pager);
        return result;
    }
	
}
