package com.phxl.hqcp.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.phxl.core.base.entity.Pager;
import com.phxl.hqcp.entity.UserInfo;

public interface UserInfoMapper {
	/**
     * 
     * findUserNoExist:(验证用户登录账号的唯一性). <br/> 
     * 
     * @Title: findUserNoExist
     * @Description: TODO
     * @param userNo
     * @return    设定参数
     * @return int    返回类型
     * @throws
     */
    int findUserNoExist(@Param(value = "userNo") String userNo);
    
    /**
     * 查询用户的权限菜单
     * @param userId
     */
    List<Map<String,Object>> selectUserMenu(@Param(value = "userId") String userId);
	
	/**
	 * 查询机构用户列表
	 * @author	黄文君
	 * @date	2017年3月24日 下午8:20:36
	 * @param	pager
	 * @return	List<UserInfo>
	 */
	public List<UserInfo> findOrgUserList(Pager pager);
	/**
	 * 查看指定机构的机构类型
	 * @author	黄文君
	 * @date	2017年3月24日 下午4:55:22
	 * @param	userId
	 * @return	String
	 */
	public String findOrgTypeByOrgId(@Param("orgId")Long orgId);
	
	/**
	 * 判断用户名（登录名）是否存在
	 * @author	黄文君
	 * @date	2017年3月24日 下午8:13:19
	 * @param	userno
	 * @param	excludeUserId
	 * @return	int
	 */
	public int countUserno(@Param("userNo")String userno, @Param("excludeUserId")String excludeUserId);
	
	/**
	 * 判断指定机构是否运营商（服务商）
	 * @author	黄文君
	 * @date	2017年3月24日 下午9:06:35
	 * @param	orgId
	 * @return	int
	 */
	public int findServiceOrgByOrgId(@Param("orgId")Long orgId);

	/**
	 * 查看制定机构的状态
	 */
	public String findOrgFstateByOrgId(@Param("orgId")Long orgId);

	/**
	 * 查询机构对应的机构管理员
	 */
	Set<String> findManagerUserIdByOrgId(@Param("orgId")Long orgId);
	
	/**
	 * 查询还没有机构管理员的机构列表
	 * @author	黄文君
	 * @date	2017年3月29日 下午5:21:26
	 * @param	pager
	 * @return	List<Map<String,Object>>
	 */
	public List<Map<String, Object>> findWithoutAdminOrgList(Pager pager);
	
	/**
	 * 根据用户id查询用户信息
	 * @author	黄文君
	 * @date	2017年4月6日 下午2:02:45
	 * @param userId
	 * @return
	 * @return	UserInfo
	 */
	public UserInfo findUserInfoById(String userId);
}