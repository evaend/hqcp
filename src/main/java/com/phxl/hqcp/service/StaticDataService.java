package com.phxl.hqcp.service;

import java.util.List;
import java.util.Map;

import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.service.IBaseService;
import com.phxl.hqcp.entity.StaticData;
import com.phxl.hqcp.entity.StaticInfo;

public interface StaticDataService  extends IBaseService{
		
		/**
		 * 查询基础数据
		 */
		List<Map<String, Object>> searchStaticData(Pager pager);

		/**
		 * 【数据字典】查询所有的数据字典类型列表
		 */
		List<Map<String, Object>> searchStaticInfo(Pager pager);

		/**
		 * 【数据字典】查询机构的所有的数据字典类型列表
		 */
		List<Map<String, Object>> searchStaticByOrgId(Pager pager);

		/**
		 * 【数据字典】查询某个字典类型编码是否重复
		 */
		boolean existedTfClo(StaticInfo staticInfo);

		/**
		 * 【数据字典】查询某个机构的数据字典（select下拉框，带搜索）
		 */
		List<Map<String, Object>> orgStaticInfo(Pager pager);

		/**
		 * 数据字典克隆,第一种是克隆字典类型和内容，第二种是只复制字典的内容，必须选择目的类型编码
		 * @throws ValidationException 
		 */
		void copyStaticInfo(String sourceStaticId, StaticInfo newSInfo) throws ValidationException;

		/**
		 * 【数据字典】数据字典内容查询
		 */
		List searchStaticDataByStaticId(Pager pager);

		/**
		 * 【数据字典】查询某个分类里的字典编码是否重复
		 */
		boolean existedTfCloCode(StaticData staticData);

		/**
		 * 查询机构私有字典
		 */
		List<Map<String, Object>> privateData(Pager pager1);

		/**
		 * 过滤出字典中不存在的常量
		 * @author	黄文君
		 * @date	2017年6月2日 上午10:02:49
		 * @param	dictName		字典项标识
		 * @param	names			值列表
		 * @return	String[]
		 */
		public abstract String[] filterNoExistNames(String dictName, Object[] names);

		/**
		 * 翻译某一个具体字典码
		 * @author	黄文君
		 * @date	2017年6月22日 下午1:51:14
		 * @param	dictType
		 * @param	dictCode
		 * @return	String
		 */
		public abstract String findDictName(String dictType, String dictCode);
		
		/**
		 * 过滤出字典中不存在的code
		 */
		public abstract String[] filterNoExistCodes(String dictName, Object[] names);

		
}
