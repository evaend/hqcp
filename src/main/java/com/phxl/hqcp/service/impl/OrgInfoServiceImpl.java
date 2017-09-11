/** 
 * Project Name:basicDataModule 
 * File Name:OrgInfoServiceImpl.java 
 * Package Name:com.phxl.ysy.basicDataModule.service.impl 
 * Date:2017年3月22日下午6:35:45 
 * Copyright (c) 2017, PHXL All Rights Reserved. 
 * 
*/  
  
package com.phxl.hqcp.service.impl;

import java.io.InputStream;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.service.impl.BaseService;
import com.phxl.core.base.util.DateUtils;
import com.phxl.core.base.util.FTPUtils;
import com.phxl.core.base.util.IdentifieUtil;
import com.phxl.core.base.util.PinyinUtil;
import com.phxl.core.base.util.SystemConfig;
import com.phxl.hqcp.common.constant.CustomConst.AuditFstate;
import com.phxl.hqcp.common.util.Base64FileHelper;
import com.phxl.hqcp.dao.OrgInfoMapper;
import com.phxl.hqcp.entity.ConstrDeptOrg;
import com.phxl.hqcp.entity.HospitalInfo;
import com.phxl.hqcp.entity.OrgInfo;
import com.phxl.hqcp.entity.QcScope;
import com.phxl.hqcp.entity.SelectScope;
import com.phxl.hqcp.service.OrgInfoService;

/** 
 * 
 * 2017年8月31日 下午10:34:09
 * @author 陶悠
 *
 */
@Service
public class OrgInfoServiceImpl extends BaseService implements OrgInfoService{
    
    @Autowired
    private OrgInfoMapper orgInfoMapper;
 
    public int findOrgNameExist(String orgName,Long orgId) {
        return orgInfoMapper.findOrgNameOrOrgCodeExist(orgName,"orgName",orgId);
    }

    public int findOrgCodeExist(String orgCode,Long orgId) {
        return orgInfoMapper.findOrgNameOrOrgCodeExist(orgCode,"orgCode",orgId);
    }

    public boolean checkFileSize(MultipartFile file, long maxKbyteNum) {
        boolean msg = false;
        long fileLength = file.getSize();
        long filesize = fileLength / 1024;//kb
        if (filesize <= maxKbyteNum) {
            msg = true;
        }
        return msg;
    }

    public List<Map> searchOrgInfoList(Pager pager) {
        List<Map> list = orgInfoMapper.searchOrgInfoList(pager);
        if(list!=null && !list.isEmpty()){
          //循环获取附件信息
        }
        return list;
    }

    public List<Map<String, Object>> searchParentOrgInfoList(Pager pager) {
        return orgInfoMapper.searchParentOrgInfoList(pager);
    }
    
    /**
     * 【数据字典】查询所有机构，做下拉框
     */
	@Override
	public List<Map<String, Object>> findOrgs(Pager<Map<String, Object>> pager) {
		return orgInfoMapper.findOrgListForSelector(pager);
	}

	@Override
	public void addUpdateOrgInfo(OrgInfo orginfo, OrgInfo oldOrg) throws Exception {
		if(oldOrg == null){
			if(StringUtils.isNotBlank(orginfo.getTfAccessory())){
				uploadAccessory(orginfo);
			}
			 //新增机构信息
	        Long numMax = super.funcMax(OrgInfo.class, "orgId");//获取orgId字段的最大值
	        numMax = numMax==null?(long)1:numMax+1;
	        orginfo.setOrgId(numMax);
	        orginfo.setFqun(PinyinUtil.getFirstSpell(orginfo.getOrgName()));
			this.insertInfo(orginfo);
			//新增医院信息
			makeHospitalInfo(orginfo);		
			//新增上报信息
			makeConstrDeptOrg(orginfo);
		}else{
			if(StringUtils.isNotBlank(orginfo.getTfAccessory()) 
					&& StringUtils.isBlank(oldOrg.getTfAccessory())){//本次有上传且之前没有上传过
				uploadAccessory(orginfo);
				oldOrg.setTfAccessory(orginfo.getTfAccessory());
			}
//			else{//只能提交新的上报信息
//				//新增上报信息
//				makeConstrDeptOrg(orginfo);
//			}
			this.updateInfo(oldOrg);
			//编辑医院信息
			makeHospitalInfo(orginfo);		
			//编辑或新增上报信息
			ConstrDeptOrg constrDeptOrg = new ConstrDeptOrg();
			constrDeptOrg.setOrgId(orginfo.getOrgId());
			constrDeptOrg.setpYear(orginfo.getpYear());
			ConstrDeptOrg constrDeptOrg1 = this.searchEntity(constrDeptOrg);
			if(constrDeptOrg1 == null){
				makeConstrDeptOrg(orginfo);
			}
		}	
	}
	
	public void makeHospitalInfo(OrgInfo orginfo){
		HospitalInfo hospitalInfo = new HospitalInfo();
		hospitalInfo.setOrgId(orginfo.getOrgId());
		hospitalInfo.setHospitalLevel(orginfo.getHospitalLevel());
		hospitalInfo.setHospitalProperty(orginfo.getHospitalProperty());
		hospitalInfo.setHospitalTeaching(orginfo.getHospitalTeaching());
		hospitalInfo.setPlanBedSum(orginfo.getPlanBedSum());
		hospitalInfo.setActualBedSum(orginfo.getActualBedSum());
		hospitalInfo.setStaffSum(orginfo.getStaffSum());
		hospitalInfo.setHospitalType(orginfo.getHospitalType());
		HospitalInfo hi = this.find(HospitalInfo.class, orginfo.getOrgId());
		if(hi == null){
			this.insertInfo(hospitalInfo);
		}else{
			this.updateInfo(hospitalInfo);
		}
	}
	
	public void makeConstrDeptOrg(OrgInfo orginfo) throws ParseException{
		//新增机构上报信息之前，先把监管范围和选择范围增加进去
		QcScope qcScope = insertQcScope(orginfo);
		insertSelectScope(orginfo);
		ConstrDeptOrg constrDeptOrg = new ConstrDeptOrg();
		constrDeptOrg.setConstrDeptOrgGuid(IdentifieUtil.getGuId());
		constrDeptOrg.setpYear(orginfo.getpYear());
		constrDeptOrg.setpYmd("P_YEAR");
		constrDeptOrg.setAuditFstate(AuditFstate.PASSED);
		constrDeptOrg.setQcOrgId(qcScope.getQcOrgId());//现在先写死
//		constrDeptOrg.setQcOrgId(findQcOrgId(orginfo.getOrgId(),orginfo.getpYear()));
		constrDeptOrg.setOrgId(orginfo.getOrgId());
		constrDeptOrg.setHospitalLevel(orginfo.getHospitalLevel());
		constrDeptOrg.setHospitalProperty(orginfo.getHospitalProperty());
		constrDeptOrg.setHospitalTeaching(orginfo.getHospitalTeaching());
		constrDeptOrg.setPlanBedSum(orginfo.getPlanBedSum());
		constrDeptOrg.setActualBedSum(orginfo.getActualBedSum());
		constrDeptOrg.setStaffSum(orginfo.getStaffSum());
		constrDeptOrg.setHospitalType(orginfo.getHospitalType());;
		constrDeptOrg.setStartTime(qcScope.getStartDate());
		constrDeptOrg.setEndTime(qcScope.getEndDate());
		constrDeptOrg.setQcScopeType(qcScope.getQcScopeType());
		constrDeptOrg.setTfProvince(orginfo.getTfProvince());
		constrDeptOrg.setTfCity(orginfo.getTfCity());
		constrDeptOrg.setTfDistrict(orginfo.getTfDistrict());
		this.insertInfo(constrDeptOrg);
	}
	
	private void insertSelectScope(OrgInfo orginfo) throws ParseException {
		SelectScope selectScope = new SelectScope();
		selectScope.setSelectScopeGuid(IdentifieUtil.getGuId());
		selectScope.setOrgId(orginfo.getOrgId());
		selectScope.setSelectOrgId(Long.valueOf(10001));
		selectScope.setpYear(orginfo.getpYear());
		selectScope.setpYmd("P_YEAR");
		selectScope.setSelectScopeSubType("01");
		selectScope.setStartDate(DateUtils.convertDate(orginfo.getpYear()+"01-01", "yyyy-MM-dd"));
		selectScope.setEndDate(DateUtils.convertDate(orginfo.getpYear()+"12-31", "yyyy-MM-dd"));
		this.insertInfo(selectScope);
	}

	private QcScope insertQcScope(OrgInfo orginfo) throws ParseException {
		QcScope qcScope = new QcScope();
		qcScope.setQcScopeGuid(IdentifieUtil.getGuId());
		qcScope.setOrgId(orginfo.getOrgId());
		qcScope.setQcOrgId(Long.valueOf(10001));
		qcScope.setQcScopeType("02");
		qcScope.setpYear(orginfo.getpYear());
		qcScope.setpYmd("P_YEAR");
		qcScope.setQcScopeSubType("01");
		qcScope.setStartDate(DateUtils.convertDate(orginfo.getpYear()+"01-01", "yyyy-MM-dd"));
		qcScope.setEndDate(DateUtils.convertDate(orginfo.getpYear()+"12-31", "yyyy-MM-dd"));
		this.insertInfo(qcScope);
		return qcScope;
	}

	public void uploadAccessory(OrgInfo orginfo) throws Exception{
		String var0 =  String.valueOf(orginfo.getOrgId());
		String timestamp = DateUtils.format(new Date(), "yyMMddHHmmssSSS");
		//定位存储路径
		StringBuffer filePath = new StringBuffer();
		String directory = SystemConfig.getProperty("resource.ftp.hqcpFile.organization.hospital");//目录位置
		directory = MessageFormat.format(directory, var0);
		filePath.append(directory).append(timestamp);
		//base64格式文件转成数据流
		InputStream file = Base64FileHelper.decodeFile(orginfo.getTfAccessory(), filePath);
		//新附件，上传ftp存储
		FTPUtils.upload(directory, FilenameUtils.getName(filePath.toString()), file);
		//确定文件保存位置
		orginfo.setTfAccessory(filePath.toString());
	}
	
	public Long findQcOrgId(Long orgId,String pYear){
		Long qcOrgId =null;
		QcScope qc = new QcScope();
        qc.setOrgId(orgId);
        qc.setQcScopeType("02");
        qc.setpYear(pYear);
        qc.setpYmd("P_YEAR");
        qc.setQcScopeSubType("01");
        List<QcScope> list = super.searchList(qc);
        if(list!=null && !list.isEmpty()){
            qc = list.get(0);
            if(qc!=null && qc.getQcOrgId()!=null){
                qcOrgId = qc.getQcOrgId();
            }
        }
        return qcOrgId;
	}

}
  