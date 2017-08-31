package com.phxl.hqcp.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.service.impl.BaseService;
import com.phxl.core.base.util.IdentifieUtil;
import com.phxl.hqcp.dao.StaticDataMapper;
import com.phxl.hqcp.dao.StaticInfoMapper;
import com.phxl.hqcp.entity.StaticData;
import com.phxl.hqcp.entity.StaticInfo;
import com.phxl.hqcp.service.StaticDataService;

@Service
public class StaticDataServiceImp extends BaseService implements StaticDataService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	StaticDataMapper staticDataMapper;
	@Autowired
	StaticInfoMapper staticInfoMapper;
	
	public List<Map<String, Object>> searchStaticData(Pager pager) {
		return staticDataMapper.searchStaticData(pager);
	}

	@Override
	public List<Map<String, Object>> searchStaticInfo(Pager pager) {
		return staticInfoMapper.searchStaticInfo(pager);
	}

	@Override
	public List<Map<String, Object>> searchStaticByOrgId(Pager pager) {
		List<Map<String, Object>> orgStatics = staticInfoMapper.searchStaticByOrgId(pager);
		return treeList(orgStatics,"0");
	}
	
	public List<Map<String, Object>> treeList(List<Map<String, Object>> list, String parentId) {
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();  
           for (Map<String, Object> map : list) {  
               String id = map.get("staticId").toString();  
               String pid = map.get("parentStaticId").toString();  
               if (parentId.equals(pid)) {  
            	   List<Map<String, Object>> childNode = treeList(list, id);  
            	   map.put("children", childNode);
                   children.add(map);  
               }  
           }  
           return children;  
   }

	@Override
	public boolean existedTfClo(StaticInfo staticInfo) {
		return staticInfoMapper.countStaticInfo(staticInfo)>0?true:false;
	}

	@Override
	public List<Map<String, Object>> orgStaticInfo(Pager pager) {
		return staticInfoMapper.orgStaticInfo(pager);
	}

	@Override
	public void copyStaticInfo(String sourceStaticId, StaticInfo newSInfo) throws ValidationException {

		StaticInfo sourceSInfo = this.find(StaticInfo.class, sourceStaticId);//源数据字典类型
		if(sourceSInfo == null){
			throw new ValidationException("源数据字典查询错误！");
		}
		
		//1、有选择目的类型,需要更新数据字典类型，只需要把数据字典内容复制给新的类型
		if(StringUtils.isNotBlank(newSInfo.getStaticId())){
			StaticInfo dSInfo = this.find(StaticInfo.class, newSInfo.getStaticId());//目的数据字典类型
			if(dSInfo == null){
				throw new ValidationException("目的数据字典查询错误！");
			}
			this.updateInfo(newSInfo);
			//删除原有的字典
			staticDataMapper.deleteSdByStaticId(newSInfo.getStaticId());	
		}else{//2、没有目的类型，需要新增一个字典类型，并且复制数据给新的类型
			//新增新的数据字典类型
			newSInfo.setStaticId(IdentifieUtil.getGuId());
			newSInfo.setCreateTime(new Date());
			newSInfo.setStaticType("01");//默认类型是私有，如果需要公有，请在使用时联系dba修改
			newSInfo.setTfClo(sourceSInfo.getTfClo());
			newSInfo.setTfComment(sourceSInfo.getTfComment());
			newSInfo.setTfTable(sourceSInfo.getTfTable());
			newSInfo.setTfTableClo(sourceSInfo.getTfTableClo());
			newSInfo.setParentStaticId("0");//默认的被复制的数据类型就是父节点
			this.insertInfo(newSInfo);
		}
		//批量复制数据字典内容
		staticDataMapper.batchInsertFromStaticInfo(sourceStaticId,newSInfo);
		
	}

	@Override
	public List searchStaticDataByStaticId(Pager pager) {
		return staticDataMapper.searchStaticDataByStaticId(pager);
	}

	@Override
	public boolean existedTfCloCode(StaticData staticData) {
		return staticDataMapper.countStaticData(staticData)>0?true:false;
	}

	@Override
	public List<Map<String, Object>> privateData(Pager pager1) {
		return staticDataMapper.privateData(pager1);
	}

	/**
	 * 过滤出字典中不存在的常量
	 * @author	黄文君
	 * @date	2017年6月2日 上午10:02:49
	 * @param	dictName		字典项标识
	 * @param	names			值列表
	 * @return	String
	 */
	@Override
	public String[] filterNoExistNames(String dictName, Object[] names) {
		return staticDataMapper.filterNoExistNames(dictName, names);
	}

	/**
	 * 翻译某一个具体字典码
	 * @author	黄文君
	 * @date	2017年6月22日 下午1:51:14
	 * @param	dictType
	 * @param	dictCode
	 * @return	String
	 */
	@Override
	public String findDictName(String dictType, String dictCode) {
		return staticDataMapper.findDictName(dictType, dictCode);
	}

	@Override
	public String[] filterNoExistCodes(String dictName, Object[] names) {
		return staticDataMapper.filterNoExistCodes(dictName, names);

	}
}
