package com.phxl.hqcp.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ServiceException;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.service.impl.BaseService;
import com.phxl.core.base.util.IdentifieUtil;
import com.phxl.core.base.util.LocalAssert;
import com.phxl.hqcp.common.constant.CustomConst.DeptParentName;
import com.phxl.hqcp.common.constant.CustomConst.DeptTypeName;
import com.phxl.hqcp.dao.CallProcedureMapper;
import com.phxl.hqcp.dao.ConstrDeptMapper;
import com.phxl.hqcp.dao.QcScopeMapper;
import com.phxl.hqcp.dao.SelectScopeMapper;
import com.phxl.hqcp.dao.StaticDataMapper;
import com.phxl.hqcp.entity.ConstrDept;
import com.phxl.hqcp.entity.ConstrDeptCheckbox;
import com.phxl.hqcp.entity.ConstrDeptInfo;
import com.phxl.hqcp.entity.ConstrDeptMeeting;
import com.phxl.hqcp.entity.ConstrDeptOrg;
import com.phxl.hqcp.entity.ConstrDeptUser;
import com.phxl.hqcp.entity.ConstrDeptWork;
import com.phxl.hqcp.entity.QcScope;
import com.phxl.hqcp.service.DeptInfoService;
import com.phxl.hqcp.service.FormulaService;

@Service
public class DeptInfoServiceImpl extends BaseService implements DeptInfoService {

    @Autowired
	SelectScopeMapper selectScopeMapper;
    @Autowired
	QcScopeMapper qcScopeMapper;
    @Autowired
	CallProcedureMapper callProcedureMapper;
    @Autowired
    FormulaService formulaService;
    @Autowired
    ConstrDeptMapper constrDeptMapper;
    @Autowired
    StaticDataMapper staticDataMapper;

    @Override
    public List<Map<String, Object>> getPyearList(Pager pager) {
        return selectScopeMapper.getPyearList(pager);
    }

    @Override
    public List<Map<String, Object>> searchSelectScopeList(Pager pager) {
        return selectScopeMapper.searchSelectScopeList(pager);
    }

    @Override
    public List<Map<String, Object>> getDeptYearList(Pager pager) {
        return qcScopeMapper.getDeptYearList(pager);
    }

    @Override
    public List<Map<String, Object>> searchConstrDeptAuditList(Pager pager) throws ServiceException{
        List<Map<String, Object>> list = qcScopeMapper.searchConstrDeptAuditList(pager);
        //查询建设科室-机构概要
        if(pager.getQueryParam("orgId")!=null && pager.getQueryParam("pYear")!=null){
            ConstrDeptOrg deptOrg = new ConstrDeptOrg();
            deptOrg.setQcOrgId(Long.parseLong(pager.getQueryParam("orgId").toString()));
            deptOrg.setpYear((String)pager.getQueryParam("pYear"));
            deptOrg.setpYmd("P_YEAR");
            List<ConstrDeptOrg> orgList = super.searchList(deptOrg);//根据监管机构、上报年限查询科室机构信息
            if(orgList!=null && !orgList.isEmpty()){
                Boolean msg = false;
                for(ConstrDeptOrg deptOrginfo:orgList){
                    if(deptOrginfo!=null && StringUtils.isBlank(deptOrginfo.getConstrDeptGuid())){
                        msg = true;
                        break;
                    }
                }
                if(msg){
                    //若查询不到信息，则调用存储过程
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("qcOrgId",pager.getQueryParam("orgId"));//申请单库房id
                    params.put("pYear", pager.getQueryParam("pYear"));
                    params.put("pYmd", "P_YEAR");
                    callProcedureMapper.SP_DEPT_ORG(params);
                    List<Map> cursorList1 = params.get("ret_cursor") == null ? null : (List<Map>)params.get("ret_cursor");
                    if(cursorList1 != null && !cursorList1.isEmpty()){
                        Map map = cursorList1.get(0);
                        Integer ret = map.get("RET") == null ? 0 :Integer.parseInt( map.get("RET").toString());
                        if(ret > 0){
                            String error = map.get("ERROR") == null ? "" : (String)map.get("ERROR");
                        }else{
                            String error1 = map.get("ERROR") == null ? "" : (String)map.get("ERROR");
                            throw new ServiceException(error1);
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public String insertEditConstrDept(ConstrDept constrDept, String sessionUserId, String sessionUserName, Long sessionOrgId) throws Exception {
        Assert.notNull(constrDept, "请填写科室上报信息!");
        LocalAssert.notBlank(constrDept.getpYear(), "请选择上报周期");
        LocalAssert.notBlank(constrDept.getAuditFstate(), "上报信息状态，不能为空");
        Assert.notNull(constrDept.getSchedule(), "完成度不能为空!");
        if(StringUtils.isBlank(constrDept.getConstrDeptGuid())){//新增科室上报信息
          //查询当前机构的监管机构
          Long qcOrgId = null;
          QcScope qc = new QcScope();
          qc.setOrgId(sessionOrgId);
          qc.setQcScopeType("02");
          qc.setpYear(constrDept.getpYear());
          qc.setpYmd("P_YEAR");
          qc.setQcScopeSubType("01");
          List<QcScope> list = super.searchList(qc);
          if(list!=null && !list.isEmpty()){
              qc = list.get(0);
              if(qc!=null && qc.getQcOrgId()!=null){
                  qcOrgId = qc.getQcOrgId();
              }
          }
          Assert.notNull(qcOrgId, "监管机构，不能为空!");
          String startTime = "";
          String endTime = "";
          SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
          StringBuffer sbuf =  new StringBuffer(constrDept.getpYear());
          StringBuffer ebuf =  new StringBuffer(constrDept.getpYear());
          startTime = sbuf.append("-01-01").toString();
          endTime = ebuf.append("-12-31").toString();
          //开始新增科室上报信息
          constrDept.setConstrDeptGuid(IdentifieUtil.getGuId());
          constrDept.setQcOrgId(qcOrgId);
          constrDept.setOrgId(sessionOrgId);
          constrDept.setQcScopeType("02");
          constrDept.setpYmd("P_YEAR");
          constrDept.setStartTime(sdf.parse(startTime));
          constrDept.setEndTime(sdf.parse(endTime));
          constrDept.setCreateTime(new Date());
          constrDept.setCreateUserId(sessionUserId);
          constrDept.setCreateUserName(sessionUserName);
          constrDept.setModifyTime(new Date());
          constrDept.setModefileUserId(sessionUserId);
          constrDept.setModefileUserNmae(sessionUserName);
          super.insertInfo(constrDept);
        }else{
          //编辑科室上报信息
            ConstrDept dept = super.find(ConstrDept.class, constrDept.getConstrDeptGuid());
            dept.setAuditFstate(constrDept.getAuditFstate());
            dept.setModifyTime(new Date());
            dept.setModefileUserId(sessionUserId);
            dept.setModefileUserNmae(sessionUserName);
            dept.setSchedule(constrDept.getSchedule());
            super.updateInfo(dept);
            ConstrDeptInfo deptInfo = new ConstrDeptInfo();
            deptInfo.setConstrDeptGuid(dept.getConstrDeptGuid());
            List<ConstrDeptInfo> deptList = super.searchList(deptInfo);
            if(deptList!=null && !deptList.isEmpty()){
                for(ConstrDeptInfo dep:deptList){
                    if(dep!=null && StringUtils.isNotBlank(dep.getConstrDeptInfoGuid())){
                        //删除科室基本信息的多选
                        ConstrDeptCheckbox deptCheckbox = new ConstrDeptCheckbox();
                        deptCheckbox.setConstrDeptDetailGuid(dep.getConstrDeptInfoGuid());
                        super.deleteInfo(deptCheckbox);
                    }
                    //删除科室基本信息
                    super.deleteInfo(dep);
                }
            }
            ConstrDeptWork work = new ConstrDeptWork();
            work.setConstrDeptGuid(dept.getConstrDeptGuid());
            List<ConstrDeptWork> wList = super.searchList(work);
            if(wList!=null && !wList.isEmpty()){
                for(ConstrDeptWork dw:wList){
                    if(dw!=null && StringUtils.isNotBlank(dw.getConstrDeptWorkGuid())){
                        //删除科室业务的多选
                        ConstrDeptCheckbox deptCheckbox = new ConstrDeptCheckbox();
                        deptCheckbox.setConstrDeptDetailGuid(dw.getConstrDeptWorkGuid());
                        super.deleteInfo(deptCheckbox);
                    }
                  //删除科室业务
                    super.deleteInfo(dw);
                }
            }
            //删除科室人员
            ConstrDeptUser user = new ConstrDeptUser();
            user.setConstrDeptGuid(dept.getConstrDeptGuid());
            super.deleteInfo(user);
            //删除科室培训
            ConstrDeptMeeting meet = new ConstrDeptMeeting();
            meet.setConstrDeptGuid(dept.getConstrDeptGuid());
            super.deleteInfo(meet);
        }
        //开始新增科室基本
        if(constrDept.getDeptInfo()!=null){
            insertConstrDeptInfo(constrDept);
        }
        //开始新增科室业务
        if(constrDept.getDeptWork()!=null){
            insertConstrDeptWork(constrDept);
        }
        //开始新增科室人员
        if(constrDept.getUserList()!=null && !constrDept.getUserList().isEmpty()){
            insertConstrDeptUser(constrDept);
        }
        //开始新增科室培训
        if(constrDept.getMeetingList()!=null && !constrDept.getMeetingList().isEmpty()){
            insertConstrDeptMeeting(constrDept);
        }
        //添加质量上报信息
        formulaService.insertForMula(constrDept.getpYear(),sessionOrgId,sessionUserId, sessionUserName);
        return constrDept.getConstrDeptGuid();
    }

    private void insertConstrDeptInfo(ConstrDept constrDept) throws Exception {
        ConstrDeptInfo deptInfo = constrDept.getDeptInfo();
        deptInfo.setConstrDeptInfoGuid(IdentifieUtil.getGuId());
        deptInfo.setConstrDeptGuid(constrDept.getConstrDeptGuid());
        if(StringUtils.isNotBlank(deptInfo.getDeptName()) && deptInfo.getDeptName().length()>100){
            throw new ValidationException("科室名称不能超过100个字符!");
        }
        if(StringUtils.isNotBlank(deptInfo.getDeptTypeName()) && deptInfo.getDeptTypeName().length()>100){
            throw new ValidationException("部门级别不能超过100个字符!");
        }
        if(StringUtils.isNotBlank(deptInfo.getDeptParentName()) && deptInfo.getDeptParentName().length()>100){
            throw new ValidationException("上级管理部门不能超过100个字符!");
        }
        super.insertInfo(deptInfo);
        List<Map<String, Object>> workScope = deptInfo.getWorkScope();
        if(workScope!=null && !workScope.isEmpty()){
            for(Map<String, Object> map:workScope){
                if(map!=null && map.get("workScopeValue")!=null){
                    ConstrDeptCheckbox check = new ConstrDeptCheckbox();
                    check.setCheckboxDetailGuid(IdentifieUtil.getGuId());
                    check.setConstrDeptDetailGuid(deptInfo.getConstrDeptInfoGuid());
                    check.setFsort(Long.parseLong(map.get("workScopeValue").toString()));
                    check.setTfCode(map.get("workScopeValue").toString());
                    check.setCheckboxType("TB_CONSTR_DEPT_INFO.DEPT_WORK_SCOPE");
                    if(map.get("workScopeName")!=null){
                        check.setTfValue(map.get("workScopeName").toString());
                    }
                    super.insertInfo(check);
                }
            }
        }
        List<Map<String, Object>> workOther = deptInfo.getWorkOther();
        if(workOther!=null && !workOther.isEmpty()){
            for(Map<String, Object> map:workOther){
                if(map!=null && map.get("workOtherValue")!=null){
                    ConstrDeptCheckbox check = new ConstrDeptCheckbox();
                    check.setCheckboxDetailGuid(IdentifieUtil.getGuId());
                    check.setConstrDeptDetailGuid(deptInfo.getConstrDeptInfoGuid());
                    check.setFsort(Long.parseLong(map.get("workOtherValue").toString()));
                    check.setTfCode(map.get("workOtherValue").toString());
                    check.setCheckboxType("TB_CONSTR_DEPT_INFO.DEPT_WORK_OTHER");
                    if(map.get("workOtherName")!=null){
                        check.setTfValue(map.get("workOtherName").toString());
                    }
                    super.insertInfo(check);
                }
            }
        }
    }

    private void insertConstrDeptWork(ConstrDept constrDept) throws Exception {
        ConstrDeptWork deptWork = constrDept.getDeptWork();
        deptWork.setConstrDeptWorkGuid(IdentifieUtil.getGuId());
        deptWork.setConstrDeptGuid(constrDept.getConstrDeptGuid());
        if(deptWork.getEquipmentSum()!=null && (!deptWork.getEquipmentSum().toString().matches("^(([1-9]{1}\\d*)|([0]{1}))?$") || new BigDecimal(deptWork.getEquipmentSum()).compareTo(new BigDecimal("999999999999"))==1)){
            throw new ValidationException("医疗设备总数量必须大于或等于0的正整数，且最多不能超过12位数!");
        }
        if(deptWork.getEquipmentValue()!=null && (!deptWork.getEquipmentValue().toString().matches("^(([1-9]{1}\\d*)|([0]{1}))?$") || new BigDecimal(deptWork.getEquipmentValue()).compareTo(new BigDecimal("999999999999"))==1)){
            throw new ValidationException("医疗设备总价值必须大于或等于0的正整数，且最多不能超过12位数!");
        }
        if(deptWork.getAbEquipmentSum()!=null && (!deptWork.getAbEquipmentSum().toString().matches("^(([1-9]{1}\\d*)|([0]{1}))?$") || new BigDecimal(deptWork.getAbEquipmentSum()).compareTo(new BigDecimal("999999999999"))==1)){
            throw new ValidationException("甲乙类医疗设备总数量必须大于或等于0的正整数，且最多不能超过12位数!");
        }
        if(deptWork.getAbEquipmentValue()!=null && (!deptWork.getAbEquipmentValue().toString().matches("^(([1-9]{1}\\d*)|([0]{1}))?$") || new BigDecimal(deptWork.getAbEquipmentValue()).compareTo(new BigDecimal("999999999999"))==1)){
            throw new ValidationException("甲乙类医疗设备总价值必须大于或等于0的正整数，且最多不能超过12位数!");
        }
        if(deptWork.getConsuSum()!=null && (!deptWork.getConsuSum().toString().matches("^(([1-9]{1}\\d*)|([0]{1}))?$") || new BigDecimal(deptWork.getConsuSum()).compareTo(new BigDecimal("999999999999"))==1)){
            throw new ValidationException("医用耗材（产品）总数量必须大于或等于0的正整数，且最多不能超过12位数!");
        }
        if(deptWork.getConsuValue()!=null && (!deptWork.getConsuValue().toString().matches("^(([1-9]{1}\\d*)|([0]{1}))?$") || new BigDecimal(deptWork.getConsuValue()).compareTo(new BigDecimal("999999999999"))==1)){
            throw new ValidationException("医用耗材年度消耗总金额必须大于或等于0的正整数，且最多不能超过12位数!");
        }
        if(deptWork.getHighConsuSum()!=null && (!deptWork.getHighConsuSum().toString().matches("^(([1-9]{1}\\d*)|([0]{1}))?$") || new BigDecimal(deptWork.getHighConsuSum()).compareTo(new BigDecimal("999999999999"))==1)){
            throw new ValidationException("高值耗材（产品）总数量必须大于或等于0的正整数，且最多不能超过12位数!");
        }
        if(deptWork.getHighConsuValue()!=null && (!deptWork.getHighConsuValue().toString().matches("^(([1-9]{1}\\d*)|([0]{1}))?$") || new BigDecimal(deptWork.getHighConsuValue()).compareTo(new BigDecimal("999999999999"))==1)){
            throw new ValidationException("高值耗材（产品）总金额必须大于或等于0的正整数，且最多不能超过12位数!");
        }
        super.insertInfo(deptWork);
        List<Map<String, Object>> logisticsScopeList = deptWork.getLogisticsScopeList();
        if(logisticsScopeList!=null && !logisticsScopeList.isEmpty()){
            for(Map<String, Object> map:logisticsScopeList){
                if(map!=null && map.get("logisticsScopeValue")!=null){
                    ConstrDeptCheckbox check = new ConstrDeptCheckbox();
                    check.setCheckboxDetailGuid(IdentifieUtil.getGuId());
                    check.setConstrDeptDetailGuid(deptWork.getConstrDeptWorkGuid());
                    check.setFsort(Long.parseLong(map.get("logisticsScopeValue").toString()));
                    check.setTfCode(map.get("logisticsScopeValue").toString());
                    check.setCheckboxType("TB_CONSTR_DEPT_WORK.LOGISTICS_SCOPE");
                    if(map.get("logisticsScopeName")!=null){
                        check.setTfValue(map.get("logisticsScopeName").toString());
                    }
                    super.insertInfo(check);
                }
            }
        }
        List<Map<String, Object>> logisticsTypeList = deptWork.getLogisticsTypeList();
        if(logisticsTypeList!=null && !logisticsTypeList.isEmpty()){
            for(Map<String, Object> map:logisticsTypeList){
                if(map!=null && map.get("logisticsTypeValue")!=null){
                    ConstrDeptCheckbox check = new ConstrDeptCheckbox();
                    check.setCheckboxDetailGuid(IdentifieUtil.getGuId());
                    check.setConstrDeptDetailGuid(deptWork.getConstrDeptWorkGuid());
                    check.setFsort(Long.parseLong(map.get("logisticsTypeValue").toString()));
                    check.setTfCode(map.get("logisticsTypeValue").toString());
                    check.setCheckboxType("TB_CONSTR_DEPT_WORK.LOGISTICS_TYPE");
                    if(map.get("logisticsTypeName")!=null){
                        check.setTfValue(map.get("logisticsTypeName").toString());
                    }
                    super.insertInfo(check);
                }
            }
        }
    }

    private void insertConstrDeptUser(ConstrDept constrDept) throws Exception {
        List<ConstrDeptUser> userList = constrDept.getUserList();
        if(userList!=null && !userList.isEmpty()){
            for(ConstrDeptUser entity:userList){
                if(entity!=null){
                    entity.setConstrDeptUserGuid(IdentifieUtil.getGuId());
                    entity.setConstrDeptGuid(constrDept.getConstrDeptGuid());
                    if(StringUtils.isNotBlank(entity.getFname()) && entity.getFname().length()>15){
                        throw new ValidationException("姓名不能超过15个字符!");
                    }
                    if(StringUtils.isNotBlank(entity.getPostName()) && entity.getPostName().length()>50){
                        throw new ValidationException("岗位不能超过50个字符!");
                    }
                    if(entity.getPostAge()!=null && (!entity.getPostAge().toString().matches("^(([1-9]{1}\\d*)|([0]{1}))?$") || new BigDecimal(entity.getPostAge()).compareTo(new BigDecimal("99"))==1)){
                        throw new ValidationException("岗位工龄必须大于或等于0的正整数，且最多不能超过2位数!");
                    }
                    super.insertInfo(entity);
                }
            }
        }
    }

    private void insertConstrDeptMeeting(ConstrDept constrDept) throws Exception {
        List<ConstrDeptMeeting> meetingList = constrDept.getMeetingList();
        if(meetingList!=null && !meetingList.isEmpty()){
            for(ConstrDeptMeeting entity:meetingList){
                if(entity!=null){
                    entity.setConstrDeptMeetingGuid(IdentifieUtil.getGuId());
                    entity.setConstrDeptGuid(constrDept.getConstrDeptGuid());
                    if(StringUtils.isNotBlank(entity.getMeetingName()) && entity.getMeetingName().length()>100){
                        throw new ValidationException("会议名称不能超过100个字符!");
                    }
                    if(StringUtils.isNotBlank(entity.getMeetingTime())){
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                        entity.setMeetingDate(sdf.parse(entity.getMeetingTime()));
                    }
                    if(StringUtils.isNotBlank(entity.getMeetingAddress()) && entity.getMeetingAddress().length()>100){
                        throw new ValidationException("会议地址不能超过100个字符!");
                    }
                    if(StringUtils.isNotBlank(entity.getMeetingSponsor()) && entity.getMeetingSponsor().length()>100){
                        throw new ValidationException("主办方不能超过100个字符!");
                    }
                    if(StringUtils.isNotBlank(entity.getTfRemark()) && entity.getTfRemark().length()>250){
                        throw new ValidationException("备注不能超过250个字符!");
                    }
                    if(entity.getMeetingAllUserSum()!=null && (!entity.getMeetingAllUserSum().toString().matches("^(([1-9]{1}\\d*)|([0]{1}))?$") || new BigDecimal(entity.getMeetingAllUserSum()).compareTo(new BigDecimal("999999999999"))==1)){
                        throw new ValidationException("参会人数必须大于或等于0的正整数，且最多不能超过2位数!");
                    }
                    if(entity.getMeetingDeptUserSum()!=null && (!entity.getMeetingDeptUserSum().toString().matches("^(([1-9]{1}\\d*)|([0]{1}))?$") || new BigDecimal(entity.getMeetingDeptUserSum()).compareTo(new BigDecimal("999999999999"))==1)){
                        throw new ValidationException("科室参会人数必须大于或等于0的正整数，且最多不能超过2位数!");
                    }
                    super.insertInfo(entity);
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> searchConstrDeptUserList(Pager pager) {
        return selectScopeMapper.searchConstrDeptUserList(pager);
    }

    @Override
    public Map<String, Object> getDeptInfo(Pager pager) {
        List<Map<String, Object>> list = selectScopeMapper.getDeptInfo(pager);
        Map<String, Object> map = new HashMap<String, Object>();
        if(list!=null && !list.isEmpty()){
            Map<String, Object> deptMap = list.get(0);
            if(deptMap!=null && !deptMap.isEmpty()){
                //床位
                Map<String, Object> bedMap = new HashMap<String, Object>();
                bedMap.put("planBedSum", deptMap.get("PLAN_BED_SUM")==null?0:deptMap.get("PLAN_BED_SUM"));
                bedMap.put("tbPlanBedSum", deptMap.get("TB_PLAN_BED_SUM")==null?"0%":deptMap.get("TB_PLAN_BED_SUM"));
                map.put("bedSum", bedMap);
                //机构员工总数
                Map<String, Object> staffMap = new HashMap<String, Object>();
                staffMap.put("planStaffSum", deptMap.get("STAFF_SUM")==null?0:deptMap.get("STAFF_SUM"));
                staffMap.put("tbStaffSum", deptMap.get("TB_STAFF_SUM")==null?"0%":deptMap.get("TB_STAFF_SUM"));
                map.put("staffSum", staffMap);
                //医工人员总数
                Map<String, Object> ygMap = new HashMap<String, Object>();
                ygMap.put("planYgSum", deptMap.get("YG_NUM")==null?0:deptMap.get("YG_NUM"));
                ygMap.put("tbYgSum", deptMap.get("TB_YG_NUM")==null?"0%":deptMap.get("TB_YG_NUM"));
                map.put("ygSum", ygMap);
                //医工培训总数
                Map<String, Object> meetMap = new HashMap<String, Object>();
                meetMap.put("planMeetSum", deptMap.get("MEET_NUM")==null?0:deptMap.get("MEET_NUM"));
                meetMap.put("tbMeetSum", deptMap.get("TB_MEET_NUM")==null?"0%":deptMap.get("TB_MEET_NUM"));
                map.put("meetSum", meetMap);
            }
        }
        return map;
    }


    @Override
    public Map<String, Object> getOrgInfoTb(Pager pager) {
        List<Map<String, Object>> list = qcScopeMapper.getOrgInfoTb(pager);
        Map<String, Object> map = new HashMap<String, Object>();
        if(list!=null && !list.isEmpty()){
            Map<String, Object> orgMap = list.get(0);
            if(orgMap!=null && !orgMap.isEmpty()){
                //机构总数
                Map<String, Object> orgTotalMap = new HashMap<String, Object>();
                orgTotalMap.put("totalOrg", orgMap.get("totalOrg")==null?0:orgMap.get("totalOrg"));
                orgTotalMap.put("tbTotalOrg", orgMap.get("tbTotalOrg")==null?"0%":orgMap.get("tbTotalOrg"));
                map.put("orgSum", orgTotalMap);
                //三甲机构
                Map<String, Object> topThreeOrg = new HashMap<String, Object>();
                topThreeOrg.put("totalLevel3", orgMap.get("totalLevel3")==null?0:orgMap.get("totalLevel3"));
                topThreeOrg.put("tbTotalLevel3", orgMap.get("tbTotalLevel3")==null?"0%":orgMap.get("tbTotalLevel3"));
                map.put("topThreeOrg", topThreeOrg);
                //二甲机构
                Map<String, Object> topTwoOrg = new HashMap<String, Object>();
                topTwoOrg.put("totalLevel2", orgMap.get("totalLevel2")==null?0:orgMap.get("totalLevel2"));
                topTwoOrg.put("tbTotalLevel2", orgMap.get("tbTotalLevel2")==null?"0%":orgMap.get("tbTotalLevel2"));
                map.put("topTwoOrg", topTwoOrg);
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> getDeptUserAge(Pager pager) {
        List<Map<String, Object>> list = selectScopeMapper.getDeptUserAge(pager);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", list);
        map.put("name", "医工人员年龄情况");
        return map;
    }

    @Override
    public Map<String, Object> getDeptUserEducation(Pager pager) {
        List<Map<String, Object>> list = selectScopeMapper.getDeptUserEducation(pager);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", list);
        map.put("name", "医工人员学历情况");
        return map;
    }

    @Override
    public Map<String, Object> getOrgEducation(Pager pager) {
        List<Map<String, Object>> list = qcScopeMapper.getOrgEducation(pager);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", list);
        map.put("name", "医工人员学历情况");
        return map;
    }

    @Override
    public Map<String, Object> getDeptUserMajor(Pager pager) {
        List<Map<String, Object>> list = selectScopeMapper.getDeptUserMajor(pager);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String[] legendData = {"医工人员专业情况"};
        Map<String, Object> legendMap = new HashMap<String, Object>();
        legendMap.put("data", legendData);
        resultMap.put("legend", legendMap);
        if(list!=null && list.size()>0){
            String[] majorName = new String[list.size()];
            String[] num = new String[list.size()];
            Map<String, Object> majorMap = new HashMap<String, Object>();
            Map<String, Object> numMap = new HashMap<String, Object>();
            for(int i=0;i<list.size();i++){
                Map<String, Object> map = list.get(i);
                if(map!=null && !map.isEmpty()){
                    majorName[i] = map.get("MAJOR_NAME")==null?"":map.get("MAJOR_NAME").toString();
                    num[i] = map.get("NUM")==null?"":map.get("NUM").toString();
                }
            }
            majorMap.put("data", majorName);
            numMap.put("data", num);
            numMap.put("type", "bar");
            numMap.put("barMaxWidth", "30px");
            numMap.put("name", "专业人数");
            resultMap.put("xAxis", majorMap);
            resultMap.put("series", numMap);
        }

        return resultMap;
    }

    @Override
    public Map<String, Object> getAdverseEvents(Pager pager) {
        List<Map<String, Object>> list = qcScopeMapper.getAdverseEvents(pager);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String[] legendData = {"不良事件上报率"};
        Map<String, Object> legendMap = new HashMap<String, Object>();
        legendMap.put("data", legendData);
        resultMap.put("legend", legendMap);
        List<Map<String, Object>> adverList = new ArrayList<Map<String,Object>>();
        if(list!=null && list.size()>0){
            String[] orgName = new String[list.size()];
            String[] num = new String[list.size()];
            String[] aver = new String[list.size()];
            Map<String, Object> majorMap = new HashMap<String, Object>();
            Map<String, Object> numMap = new HashMap<String, Object>();
            Map<String, Object> lineMap = new HashMap<String, Object>();
            for(int i=0;i<list.size();i++){
                Map<String, Object> map = list.get(i);
                if(map!=null && !map.isEmpty()){
                    orgName[i] = map.get("F_ORG_NAME")==null?"":map.get("F_ORG_NAME").toString();
                    num[i] = map.get("INDEX_VALUE")==null?"":map.get("INDEX_VALUE").toString();
                    aver[i] = map.get("INDEX_VALUE_ALL")==null?"":map.get("INDEX_VALUE_ALL").toString();
                }
            }
            majorMap.put("data", orgName);
            numMap.put("data", num);
            numMap.put("type", "bar");
            numMap.put("barMaxWidth", "30px");
            numMap.put("name", "不良事件上报率");
            adverList.add(numMap);
            lineMap.put("name", "不良事件上报率");
            lineMap.put("type", "line");
            lineMap.put("showSymbol", "false");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("color", "#f46e65");
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("normal", map);
            lineMap.put("itemStyle", map1);
            lineMap.put("data", aver);
            adverList.add(lineMap);
            resultMap.put("xAxis", majorMap);
            resultMap.put("series", adverList);
        }

        return resultMap;
    }

    @Override
    public Map<String, Object> getMaterialTraceability(Pager pager) {
        List<Map<String, Object>> list = qcScopeMapper.getMaterialTraceability(pager);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String[] legendData = {"医用高值耗材溯源率"};
        Map<String, Object> legendMap = new HashMap<String, Object>();
        legendMap.put("data", legendData);
        resultMap.put("legend", legendMap);
        List<Map<String, Object>> traceList = new ArrayList<Map<String,Object>>();
        if(list!=null && list.size()>0){
            String[] orgName = new String[list.size()];
            String[] num = new String[list.size()];
            String[] aver = new String[list.size()];
            Map<String, Object> majorMap = new HashMap<String, Object>();
            Map<String, Object> numMap = new HashMap<String, Object>();
            Map<String, Object> lineMap = new HashMap<String, Object>();
            for(int i=0;i<list.size();i++){
                Map<String, Object> map = list.get(i);
                if(map!=null && !map.isEmpty()){
                    orgName[i] = map.get("F_ORG_NAME")==null?"":map.get("F_ORG_NAME").toString();
                    num[i] = map.get("INDEX_VALUE")==null?"":map.get("INDEX_VALUE").toString();
                    aver[i] = map.get("INDEX_VALUE_ALL")==null?"":map.get("INDEX_VALUE_ALL").toString();
                }
            }
            majorMap.put("data", orgName);
            numMap.put("data", num);
            numMap.put("type", "bar");
            numMap.put("barMaxWidth", "30px");
            numMap.put("name", "医用高值耗材溯源率");
            traceList.add(numMap);
            lineMap.put("name", "医用高值耗材溯源率");
            lineMap.put("type", "line");
            lineMap.put("showSymbol", "false");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("color", "#f46e65");
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("normal", map);
            lineMap.put("itemStyle", map1);
            lineMap.put("data", aver);
            traceList.add(lineMap);
            resultMap.put("xAxis", majorMap);
            resultMap.put("series", traceList);
        }

        return resultMap;
    }

    @Override
    public Map<String, Object> getOrgDeptInfoByGender(Pager pager) {
        List<Map<String, Object>> list = qcScopeMapper.getOrgDeptInfoByGender(pager);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String[] legendData = {"男","女"};
        Map<String, Object> legendMap = new HashMap<String, Object>();
        legendMap.put("data", legendData);
        resultMap.put("legend", legendMap);
        List<String> orgIdList = new ArrayList<String>();//机构Id
        if(list!=null && !list.isEmpty()){
            for(Map<String, Object> map:list){
                if(map!=null && !map.isEmpty() && map.get("ORG_ID")!=null && !orgIdList.contains(map.get("ORG_ID").toString())){
                    orgIdList.add(map.get("ORG_ID").toString());
                }
            }
        }
        
        List<String> orgList = new ArrayList<String>();//机构
        List<String> maleList = new ArrayList<String>();//男
        List<String> femaleList = new ArrayList<String>();//女
        List<Map<String, Object>> genders = new ArrayList<Map<String,Object>>();
        if(orgIdList!=null && !orgIdList.isEmpty()){
            for(String orgId:orgIdList){
                pager.addQueryParam("orgId", orgId);
                list = qcScopeMapper.getOrgDeptInfoByGender(pager);
                if(list!=null && !list.isEmpty()){
                    String man = "0";
                    String woman = "0";
                    for(Map<String, Object> map:list){
                        if(map!=null && !map.isEmpty() && map.get("ORG_ID")!=null){
                            if(!orgList.contains(map.get("ORG_NAME").toString())){
                                orgList.add((String)map.get("ORG_NAME"));
                            }
                            if(map.get("GENDER")!=null && StringUtils.isNotBlank(map.get("GENDER").toString()) && "1".equals(map.get("GENDER").toString())){//男
                                man = map.get("YG_NUM")==null?"0":map.get("YG_NUM").toString();
                            }
                            if(map.get("GENDER")!=null && StringUtils.isNotBlank(map.get("GENDER").toString()) && "2".equals(map.get("GENDER").toString())){//女
                                woman = map.get("YG_NUM")==null?"0":map.get("YG_NUM").toString();
                            }
                        }
                    }
                    maleList.add(man);
                    femaleList.add(woman);
                }
            }
        }
        Map<String, Object> orgs = new HashMap<String, Object>();
        orgs.put("data", orgList.toArray());
        resultMap.put("xAxis", orgs);
        Map<String, Object> males = new HashMap<String, Object>();
        males.put("name", "男");
        males.put("type", "bar");
        males.put("barMaxWidth", "30px");
        males.put("stack", "医工人数");
        males.put("data", maleList.toArray());
        genders.add(males);
        Map<String, Object> females = new HashMap<String, Object>();
        females.put("name", "女");
        females.put("type", "bar");
        females.put("barMaxWidth", "30px");
        females.put("stack", "医工人数");
        females.put("data", femaleList.toArray());
        genders.add(females);
        resultMap.put("series", genders);
        return resultMap;
    }

    @Override
    public Map<String, Object> getOrgAllLevel(Pager pager) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("十堰市", "0");
        map1.put("襄阳市", "0");
        map1.put("随州市", "0");
        map1.put("孝感市", "0");
        map1.put("黄冈市", "0");
        map1.put("武汉市", "0");
        map1.put("鄂州市", "0");
        map1.put("黄石市", "0");
        map1.put("咸宁市", "0");
        map1.put("荆州市", "0");
        map1.put("仙桃市", "0");
        map1.put("天门市", "0");
        map1.put("潜江市", "0");
        map1.put("荆门市", "0");
        map1.put("宜昌市", "0");
        map1.put("神农架林区", "0");
        map1.put("恩施土家族苗族自治州十堰", "0");
        pager.addQueryParam("level", "3");
        List<Map<String, Object>> threeList = qcScopeMapper.getOrgAllLevel(pager);
        Map<String, Object> level3Map = new HashMap<String, Object>();
        level3Map.putAll(map1);
        if(threeList!=null && !threeList.isEmpty()){
            for(Map<String, Object> cityMap:threeList){
                level3Map.put(cityMap.get("name").toString(), cityMap.get("value"));
            }
        }
        List<Map<String, Object>> threeMap = new ArrayList<Map<String,Object>>();
        if(level3Map!=null && !level3Map.isEmpty()){
            Set<String> set = level3Map.keySet(); //取出所有的key值
            for (String key:set) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", key);
                map.put("value", level3Map.get(key));
                threeMap.add(map);
            }
        }
        Map<String, Object> thirdMap = new HashMap<String, Object>();
        thirdMap.put("data", threeMap);
        resultMap.put("level3", thirdMap);
        pager.addQueryParam("level", "2");
        List<Map<String, Object>> twoList = qcScopeMapper.getOrgAllLevel(pager);
        Map<String, Object> level2Map = new HashMap<String, Object>();
        level2Map.putAll(map1);
        if(twoList!=null && !twoList.isEmpty()){
            for(Map<String, Object> cityMap:twoList){
                level2Map.put(cityMap.get("name").toString(), cityMap.get("value"));
            }
        }
        List<Map<String, Object>> secondMap = new ArrayList<Map<String,Object>>();
        if(level2Map!=null && !level2Map.isEmpty()){
            Set<String> set = level2Map.keySet(); //取出所有的key值
            for (String key:set) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", key);
                map.put("value", level2Map.get(key));
                secondMap.add(map);
            }
        }
        Map<String, Object> twoMap = new HashMap<String, Object>();
        twoMap.put("data", secondMap);
        resultMap.put("level2", twoMap);
        return resultMap;
    }
    
	@Override
	public Map<String, Object> searchConstrDept(Pager pager) {
		List<Map<String, Object>> list = constrDeptMapper.searchConstrDept(pager);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(list!=null && !list.isEmpty()){
			resultMap = list.get(0);
			if(resultMap!=null && !resultMap.isEmpty() && StringUtils.isNotBlank((String)resultMap.get("constrDeptGuid"))){
			    if(resultMap.get("schedule")!=null && StringUtils.isNotBlank(resultMap.get("schedule").toString())){
			        BigDecimal b1 = new BigDecimal(resultMap.get("schedule").toString());   
			        BigDecimal b2 = new BigDecimal("100");
			        if(b1.compareTo(new BigDecimal(0))==1){
			            resultMap.put("schedule", b1.multiply(b2).doubleValue());
			        }
			    }
				if(resultMap.get("deptTypeName")!=null && StringUtils.isNotBlank((String)resultMap.get("deptTypeName"))){//部门级别
					if(DeptTypeName.BU.equals((String)resultMap.get("deptTypeName"))){
						resultMap.put("deptTypeName", "1");
	                }else if(DeptTypeName.KE.equals((String)resultMap.get("deptTypeName"))){
	                	resultMap.put("deptTypeName", "2");
	                }else if(DeptTypeName.ZU.equals((String)resultMap.get("deptTypeName"))){
	                	resultMap.put("deptTypeName", "3");
	                }else{
	                    resultMap.put("deptTypeNameOther", (String)resultMap.get("deptTypeName"));
	                	resultMap.put("deptTypeName", "其他");
	                }
				}
				if(resultMap.get("deptParentName")!=null && StringUtils.isNotBlank((String)resultMap.get("deptParentName"))){//上级管理部门
					if(DeptParentName.YW.equals((String)resultMap.get("deptParentName"))){
						resultMap.put("deptParentName", "1");
	                }else if(DeptParentName.HQ.equals((String)resultMap.get("deptParentName"))){
	                	resultMap.put("deptParentName", "2");
	                }else if(DeptParentName.KJ.equals((String)resultMap.get("deptParentName"))){
	                	resultMap.put("deptParentName", "3");
	                }else if(DeptParentName.DL.equals((String)resultMap.get("deptParentName"))){
	                	resultMap.put("deptParentName", "4");
	                }else{
	                    resultMap.put("deptParentNameOther", (String)resultMap.get("deptParentName"));
	                	resultMap.put("deptParentName", "其他");
	                }
				}
				//部门业务管理范围、部门承担的其它工作
				List<Map<String, Object>> infoList = constrDeptMapper.searchConstrDeptCheckBox((String)resultMap.get("constrDeptGuid"), "info");
				if(infoList!=null && !infoList.isEmpty()){
					Set<String> workScope = new HashSet<String>();
					Set<String> workOther = new HashSet<String>();
					for(Map<String, Object> infoMap:infoList){
						if(infoMap!=null && infoMap.get("FSORT")!=null && StringUtils.isNotBlank(infoMap.get("FSORT").toString())){
							if(infoMap.get("CHECKBOX_TYPE")!=null && "TB_CONSTR_DEPT_INFO.DEPT_WORK_SCOPE".equals(infoMap.get("CHECKBOX_TYPE").toString())){
								if("7".equals(infoMap.get("FSORT").toString())){
								    workScope.add("其他");
									resultMap.put("workScopeOther", infoMap.get("TF_VALUE"));
								}else{
								    workScope.add(infoMap.get("FSORT").toString());
								}
							}
							if(infoMap.get("CHECKBOX_TYPE")!=null && "TB_CONSTR_DEPT_INFO.DEPT_WORK_OTHER".equals(infoMap.get("CHECKBOX_TYPE").toString())){
								workOther.add(infoMap.get("FSORT").toString());
								if("3".equals(infoMap.get("FSORT").toString())){
									resultMap.put("workMassName", infoMap.get("TF_VALUE"));
								}
								if("4".equals(infoMap.get("FSORT").toString())){
									resultMap.put("workOtherName", infoMap.get("TF_VALUE"));
								}
							}
						}
					}
					resultMap.put("workScope", workScope.toArray());
					resultMap.put("workOther", workOther.toArray());
				}
				//医疗器械物流管理开展范围、卫生材料医疗器械物流管理模式
				List<Map<String, Object>> workList = constrDeptMapper.searchConstrDeptCheckBox((String)resultMap.get("constrDeptGuid"), "work");
				if(workList!=null && !workList.isEmpty()){
					Set<String> logisticsScope = new HashSet<String>();
					Set<String> logisticsType = new HashSet<String>();
					for(Map<String, Object> workMap:workList){
						if(workMap!=null && workMap.get("FSORT")!=null && StringUtils.isNotBlank(workMap.get("FSORT").toString())){
							if(workMap.get("CHECKBOX_TYPE")!=null && "TB_CONSTR_DEPT_WORK.LOGISTICS_SCOPE".equals(workMap.get("CHECKBOX_TYPE").toString())){
								logisticsScope.add(workMap.get("FSORT").toString());
							}
							if(workMap.get("CHECKBOX_TYPE")!=null && "TB_CONSTR_DEPT_WORK.LOGISTICS_TYPE".equals(workMap.get("CHECKBOX_TYPE").toString())){
								if("6".equals(workMap.get("FSORT").toString())){
								    logisticsType.add("其他");
									resultMap.put("logisticsTypeOther", workMap.get("TF_VALUE"));
								}else{
								    logisticsType.add(workMap.get("FSORT").toString());
								}
							}
						}
					}
					resultMap.put("logisticsScope", logisticsScope.toArray());
					resultMap.put("logisticsType", logisticsType.toArray());
				}
				//医工人员
				ConstrDeptUser deptUser = new ConstrDeptUser();
				deptUser.setConstrDeptGuid((String)resultMap.get("constrDeptGuid"));
				List<ConstrDeptUser> users = super.searchList(deptUser);
				if(users!=null && !users.isEmpty()){
					resultMap.put("userCount", users.size());
					for(int i=0;i<users.size();i++){
						deptUser = users.get(i);
						resultMap.put("fname-"+i, deptUser.getFname());
						String gender = "";
						if(StringUtils.isNotBlank(deptUser.getGender())){
						    gender = staticDataMapper.findDictName("GENDER", deptUser.getGender());
						}
						resultMap.put("gender-"+i, gender);
						String birthChar = "";
                        if(StringUtils.isNotBlank(deptUser.getBirthChar())){
                            birthChar = staticDataMapper.findDictName("BIRTH_CHAR", deptUser.getBirthChar());
                        }
						resultMap.put("birthChar-"+i, birthChar);
						String technicalTitlesA = "";
                        if(StringUtils.isNotBlank(deptUser.getTechnicalTitlesA())){
                            technicalTitlesA = staticDataMapper.findDictName("TECHNICAL_TITLES_A", deptUser.getTechnicalTitlesA());
                        }
						String technicalTitlesB = "";
                        if(StringUtils.isNotBlank(deptUser.getTechnicalTitlesB())){
                            technicalTitlesB = staticDataMapper.findDictName("TECHNICAL_TITLES_B", deptUser.getTechnicalTitlesB());
                        }
                        String[] technicalTitles = {technicalTitlesA,technicalTitlesB};
						resultMap.put("technicalTitles-"+i, technicalTitles);
						resultMap.put("postName-"+i, deptUser.getPostName());
						resultMap.put("postAge-"+i, deptUser.getPostAge());
						String highestEducation = "";
                        if(StringUtils.isNotBlank(deptUser.getHighestEducation())){
                            highestEducation = staticDataMapper.findDictName("XL", deptUser.getHighestEducation());
                        }
						resultMap.put("highestEducation-"+i, highestEducation);
						String majorName = "";
                        if(StringUtils.isNotBlank(deptUser.getMajorName())){
                            majorName = staticDataMapper.findDictName("ZY", deptUser.getMajorName());
                        }
						resultMap.put("majorName-"+i, majorName);
					}
				}
				//部门培训
				ConstrDeptMeeting deptMeeting = new ConstrDeptMeeting();
				deptMeeting.setConstrDeptGuid((String)resultMap.get("constrDeptGuid"));
				List<ConstrDeptMeeting> meetings = super.searchList(deptMeeting);
				if(meetings!=null && !meetings.isEmpty()){
					resultMap.put("meetingCount", meetings.size());
					for(int j=0;j<meetings.size();j++){
						deptMeeting = meetings.get(j);
						resultMap.put("meetingName-"+j, deptMeeting.getMeetingName());
						String meetingType = "";
                        if(StringUtils.isNotBlank(deptMeeting.getMeetingType())){
                            meetingType = staticDataMapper.findDictName("HYLX", deptMeeting.getMeetingType());
                        }
						resultMap.put("meetingType-"+j, meetingType);
						if(deptMeeting.getMeetingDate()!=null){
							SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
							resultMap.put("meetingTime-"+j, sdf.format(deptMeeting.getMeetingDate()));
						}
						resultMap.put("meetingAddress-"+j, deptMeeting.getMeetingAddress());
						resultMap.put("meetingSponsor-"+j, deptMeeting.getMeetingSponsor());
						resultMap.put("meetingAllUserSum-"+j, deptMeeting.getMeetingAllUserSum());
						resultMap.put("meetingDeptUserSum-"+j, deptMeeting.getMeetingDeptUserSum());
						resultMap.put("tfRemark-"+j, deptMeeting.getTfRemark());
					}
				}
			}
		}
		return resultMap;
	}

    @Override
    public String findDictCode(String dictType, String dictName) {
        String code = staticDataMapper.findDictCode(dictType, dictName);
        return StringUtils.isBlank(code)?"":code;
    }

}
