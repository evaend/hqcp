package com.phxl.hqcp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.phxl.core.base.entity.Pager;
import com.phxl.hqcp.entity.StaticData;
import com.phxl.hqcp.entity.StaticInfo;

public interface StaticDataMapper {
	/**
	 * 查询基础数据信息
	 */
	List<Map<String, Object>> searchStaticData(Pager<?> pager);

	/**
	 * 批量增加数据字典内容
	 */
	void batchInsertFromStaticInfo(@Param("sourceStaticId")String sourceStaticId, @Param("newSInfo")StaticInfo newSInfo);

	/**
	 * 【数据字典】根据字典类型查询数据字典内容查询
	 */
	List<Map<String, Object>> searchStaticDataByStaticId(Pager<?> pager);
	
	/**
	 * 【数据字典】查询某个分类里的字典编码是否重复
	 */
	int countStaticData(StaticData staticData);

	/**
	 * 【数据字典】根据staticid删除数据字典
	 */
	void deleteSdByStaticId(String staticId);

	/**
	 * 查询基础数据信息
	 */
	List<Map<String, Object>> privateData(Pager pager1);
	
	/**
	 * 过滤出字典中不存在的常量
	 * @author	黄文君
	 * @date	2017年6月2日 上午10:02:49
	 * @param	dictName		字典项标识
	 * @param	names			值列表
	 * @return	String
	 */
	String[] filterNoExistNames(@Param("dictCode")String dictName, @Param("names")Object[] names);
	
	/**
	 * 翻译某一个具体字典码
	 * @author	黄文君
	 * @date	2017年6月22日 下午1:51:14
	 * @param	dictType
	 * @param	dictCode
	 * @return	String
	 */
	String findDictName(@Param("dictType")String dictType, @Param("dictCode")String dictCode);
	
	/**
	 * 过滤出字典中不存在的Code
	 */
	String[] filterNoExistCodes(@Param("dictCode")String dictName, @Param("names")Object[] names);
	
}