package com.phxl.hqcp.service;


import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.service.IBaseService;
import com.phxl.hqcp.entity.OrgInfo;
import com.phxl.hqcp.entity.UserInfo;

public interface UserService extends IBaseService {
	
	/**
     * 
     * findUserNoExist:(验证用户登录账号的唯一性,根据用户信息表和待审核的用户注册表验证). <br/> 
     * 
     * @Title: findUserNoExist
     * @Description: TODO
     * @param userNo
     * @return    设定参数
     * @return int    返回类型
     * @throws
     */
    int findUserNoExist(String userNo);

	/**
	 * 查询机构用户列表
	 */
	public List<UserInfo> findOrgUserList(Pager pager);

	/**
	 * 根据用户id查询用户信息
	 */
	public UserInfo findUserInfoById(String userId);
	
	/**
	 * 查看指定机构的机构类型
	 */
	public String findOrgTypeByOrgId(Long orgId);
	
	/**
	 * 查询还没有机构管理员的机构列表
	 */
	public List<Map<String, Object>> findWithoutAdminOrgList(Pager pager);
	
	/**
	 * 查询用户名（登录名）是否存在
	 */
	public boolean existedUserno(String userno, String excludeUserId);
	
	/**
	 * 判断指定机构是否运营商（服务商）
	 */
	public boolean assertServiceOrgByOrgId(Long orgId);
	
	/**
	 * 判断用户登录状态，并获取信息
	 * @param userNo
	 * @param pwd
	 * @param token
	 * @throws JsonProcessingException 
	 */
    public Map<String,Object> checkLoginInfo(String userNo, String pwd,String token) throws JsonProcessingException;
    
    /**
	 * 查看机构的状态
	 */
	public String findOrgFstateByOrgId(Long orgId);
    
    /**
     * 查询用户权限模块菜单信息，拼接成菜单树
     * @param userId
     */
    public List<Map<String,Object>> selectUserMenu(String userId);

    /**
	 * 添加用户
	 */
	public abstract void addUser(UserInfo user) throws ValidationException;

	void saveRegisterUserInfo(UserInfo user, OrgInfo orgInfo, String newTfAccessoryFile) throws Exception;

	void auditUserInfo(UserInfo userInfo) throws Exception;
   
}
