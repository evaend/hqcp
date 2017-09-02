package com.phxl.hqcp.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.service.impl.BaseService;
import com.phxl.core.base.util.IdentifieUtil;
import com.phxl.core.base.util.LocalAssert;
import com.phxl.hqcp.dao.QcScopeMapper;
import com.phxl.hqcp.dao.SelectScopeMapper;
import com.phxl.hqcp.entity.ConstrDept;
import com.phxl.hqcp.entity.ConstrDeptCheckbox;
import com.phxl.hqcp.entity.ConstrDeptInfo;
import com.phxl.hqcp.entity.ConstrDeptMeeting;
import com.phxl.hqcp.entity.ConstrDeptUser;
import com.phxl.hqcp.entity.ConstrDeptWork;
import com.phxl.hqcp.entity.QcScope;
import com.phxl.hqcp.service.DeptInfoService;

@Service
public class DeptInfoServiceImpl extends BaseService implements DeptInfoService {
	
	@Autowired
	SelectScopeMapper selectScopeMapper;
	@Autowired
	QcScopeMapper qcScopeMapper;

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
    public List<Map<String, Object>> searchConstrDeptAuditList(Pager pager) {
        return qcScopeMapper.searchConstrDeptAuditList(pager);
    }

    @Override
    public void insertEditConstrDept(ConstrDept constrDept, String sessionUserId, String sessionUserName, Long sessionOrgId) throws Exception {
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
          String startTime = "";
          String endTime = "";
          SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
          StringBuffer buf =  new StringBuffer(constrDept.getpYear());
          startTime = buf.append("-01-01").toString();
          endTime = buf.append("-12-31").toString();
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
                    check.setCheckboxType("DEPT_WORK_SCOPE");
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
                    check.setCheckboxType("DEPT_WORK_OTHER");
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
                    check.setCheckboxType("LOGISTICS_SCOPE");
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
                    check.setCheckboxType("LOGISTICS_TYPE");
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

}