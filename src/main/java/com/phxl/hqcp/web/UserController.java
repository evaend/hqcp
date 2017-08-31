package com.phxl.hqcp.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ServiceException;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.interceptor.ResResultBindingInterceptor;
import com.phxl.core.base.util.IdentifieUtil;
import com.phxl.core.base.util.LocalAssert;
import com.phxl.core.base.util.MD5Util;
import com.phxl.hqcp.common.constant.CustomConst;
import com.phxl.hqcp.common.constant.CustomConst.Fstate;
import com.phxl.hqcp.common.constant.CustomConst.LoginUser;
import com.phxl.hqcp.common.constant.CustomConst.OrgType;
import com.phxl.hqcp.common.constant.CustomConst.UserLevel;
import com.phxl.hqcp.entity.UserInfo;
import com.phxl.hqcp.service.UserService;

/**
 * 【用户管理】接口
 */
@Controller
@RequestMapping("user")
public class UserController {
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	UserService userService;
	
	/**
	 * 查询各个机构管理员列表、以及本机构操作员列表（运营人员使用）
	 * @author	黄文君
	 * @date	2017年4月1日 下午15:56:56
	 * @param	searchName			模糊搜索关键字
	 * @param	pagesize			每页记录数量
	 * @param	page				当前页码
	 * @param	sidx				排序字段名称
	 * @param	sord				排序方式（desc/descend：降序； asc/ascend：升序）
	 * @param	session
	 * @return	Pager<UserInfo>
	 */
	@RequestMapping("findOrgAdminUserList")
	@ResponseBody
	public Pager<UserInfo> findOrgAdminUserList(String searchName, 
											    Integer pagesize, 
											    Integer page, 
											    String sidx, 
											    String sord,
											    HttpSession session) {
		Pager pager = new Pager();
		pager.setPageSize(pagesize);
		pager.setPageNum(page);
		
		pager.addQueryParam("searchName", searchName);
		pager.addQueryParam("sessionOrgId", session.getAttribute(LoginUser.SESSION_USER_ORGID));
		pager.addQueryParam("sessionOrgType", session.getAttribute(LoginUser.SESSION_USER_ORG_TYPE));
		pager.addQueryParam("sessionUserLevel", session.getAttribute(LoginUser.SESSISON_USER_LEVEL));
		
		if(StringUtils.isNotBlank(sidx) && StringUtils.isNotBlank(sord)){
			pager.addQueryParam("resultMapName", "com.phxl.ysy.systemModule.dao.UserInfoMapper.BaseResultMap");//设置ResultMap映射关系
			pager.addQueryParam("orderField", sidx);
			pager.addQueryParam("orderMark", "ascend".equalsIgnoreCase(sord)?"asc":"desc");
		}
		pager.setRows(userService.findOrgUserList(pager));
		return pager;
	}
	
	/**
	 * 查询当前机构的全部用户的列表
	 * @author	黄文君
	 * @date	2017年3月29日 下午6:27:53
	 * @param	searchName			模糊搜索关键字
	 * @param	excludeGroupId		排除组id
	 * @param	allowAutoPagable	是否分页
	 * @param	pagesize			每页记录数量
	 * @param	page				当前页码
	 * @param	sidx				排序字段名称
	 * @param	sord				排序方式（desc/descend：降序； asc/ascend：升序）
	 * @param	session
	 * @return	Pager<UserInfo>
	 */
	@RequestMapping("findOrgUserList")
	@ResponseBody
	public Pager<UserInfo> findOrgUserList(String searchName, 
										   String excludeGroupId, 
										   Boolean allowAutoPagable, 
										   Integer pagesize, 
										   Integer page, 
										   String sidx, 
										   String sord,
										   HttpSession session) {
		Pager pager = new Pager((Boolean)ObjectUtils.defaultIfNull(allowAutoPagable, true));
		pager.setPageSize(pagesize);
		pager.setPageNum(page);
		
		pager.addQueryParam("searchName", searchName);
		pager.addQueryParam("sessionOrgId", session.getAttribute(LoginUser.SESSION_USER_ORGID));
		pager.addQueryParam("sessionOrgType", session.getAttribute(LoginUser.SESSION_USER_ORG_TYPE));
		pager.addQueryParam("sessionUserLevel", session.getAttribute(LoginUser.SESSISON_USER_LEVEL));
		pager.addQueryParam("excludeGroupId", excludeGroupId);
		
		if(StringUtils.isNotBlank(sidx) && StringUtils.isNotBlank(sord)){
			pager.addQueryParam("resultMapName", "com.phxl.ysy.systemModule.dao.UserInfoMapper.BaseResultMap");//设置ResultMap映射关系
			pager.addQueryParam("orderField", sidx);
			pager.addQueryParam("orderMark", "ascend".equalsIgnoreCase(sord)?"asc":"desc");
		}
		pager.setRows(userService.findOrgUserList(pager));
		return pager;
	}
	
	/**
	 * 查询指定用户信息
	 * @author	黄文君
	 * @date	2017年3月23日 下午2:26:28
	 * @param	userId		用户id
	 * @return	UserInfo
	 * @throws	Exception 
	 */
	@RequestMapping("findOrgUserById")
	@ResponseBody
	public UserInfo findOrgUserById(String userId, HttpSession session) throws Exception{
		UserInfo user = userService.findUserInfoById(userId);
		Assert.notNull(user, "该用户不存在!");
		
		String sessionUserId = (String)session.getAttribute(LoginUser.SESSION_USERID);
		Long sessionOrgId = (Long)session.getAttribute(LoginUser.SESSION_USER_ORGID);
		String sessionOrgType = (String)session.getAttribute(LoginUser.SESSION_USER_ORG_TYPE);
		Long orgId = user.getOrgId();
		
		if(!OrgType.PLATFORM.equals(sessionOrgType)){//运营商（服务商）
			Assert.notNull(sessionOrgId, "会话用户不属于任何机构!");
			Assert.notNull(orgId, "指定用户不属于任何机构!");
			logger.debug("sessionOrgId="+sessionOrgId+", orgId="+orgId);
			if(sessionOrgId.longValue() != orgId.longValue()){
				throw new ServiceException("你不能查看其他机构的用户信息!");
			}
		}
		return user;
	}
	
	/**
	 * 用户管理: 查询还没有机构管理员的机构列表
	 * @author	黄文君
	 * @date	2017年3月29日 下午4:59:14
	 * @param	searchName		模糊搜索关键字
	 * @param	pageSize		每页记录数量
	 * @param	request
	 * @return	List<Map<String, Object>>
	 */
	@RequestMapping("findWithoutAdminOrgList")
	@ResponseBody
	public List<Map<String, Object>> findWithoutAdminOrgList(String searchName,Integer pageSize, HttpServletRequest request){
		request.setAttribute(ResResultBindingInterceptor.IGNORE_STD_RESULT, true);//忽略标准结果
		
		Pager pager = new Pager();
		pager.setPageSize(pageSize);
		pager.addQueryParam("searchName", searchName);
		
		return userService.findWithoutAdminOrgList(pager);
	}
	
	/**
	 * 新增用户
	 * @author	黄文君
	 * @date	2017年3月23日 下午2:27:16
	 * @param	user
	 * @return	void
	 * @throws	Exception  
	 */
	@RequestMapping("addUser")
	@ResponseBody
	public void addUser(UserInfo user, HttpSession session) throws Exception {
		//检查: 非空检查
		LocalAssert.notBlank(user.getUserNo(), "账号，不能为空!");
		LocalAssert.notBlank(user.getPwd(), "密码，不能为空!");
		LocalAssert.notBlank(user.getUserName(), "用户名称，不能为空!");
		LocalAssert.notBlank(user.getMobilePhone(), "联系电话，不能为空!");
		
		//检查: 登陆账号，不能重复
		if(userService.existedUserno(user.getUserNo(), null)){
			throw new ValidationException("该账号已经存在，不能添加!");
		}
		
		String sessionUserId = (String)session.getAttribute(LoginUser.SESSION_USERID);
		Long sessionOrgId = (Long)session.getAttribute(LoginUser.SESSION_USER_ORGID);
		String sessionOrgType = (String)session.getAttribute(LoginUser.SESSION_USER_ORG_TYPE);
		String sessionUserLevel = (String)session.getAttribute(LoginUser.SESSISON_USER_LEVEL);
		
		//新增的用户的所属机构id
		Long orgId = null;
		if(!OrgType.PLATFORM.equals(sessionOrgType)){
			//非运营机构，只能给本机构维护用户
			orgId = sessionOrgId;
		}else if(OrgType.PLATFORM.equals(sessionOrgType)){
			LocalAssert.notBlank(user.getUserLevel(), "用户级别，不能为空!");
			/*运营商（服务商）:（1）跨机构建立机构管理员；（2）本运营机构操作员。*/
			orgId = user.getUserLevel().equals(UserLevel.ORG_USER) ? sessionOrgId : user.getOrgId();
		}
		Assert.notNull(orgId, "新增用户的所属机构，未知!");
		
		String orgType = userService.findOrgTypeByOrgId(orgId);//查看指定所属机构的机构类型
		LocalAssert.notEmpty(orgType, "机构（orgId: "+orgId+"）的机构类型为空!");
		//检查: 非运营机构，不能给运营机构添加用户
		if(!OrgType.PLATFORM.equals(sessionOrgType) && OrgType.PLATFORM.equals(orgType)){
			throw new ValidationException("非运营机构，不能给运营机构添加用户!");
		}
		
		//运营商（服务商），可以建机构管理员，或自已机构的操作员
		if(OrgType.PLATFORM.equals(sessionOrgType)){
			Assert.notNull(orgId, "新增用户的机构ID，不能为空!");
			Assert.notNull(user.getUserLevel(), "用户级别，不能为空!");
			
			/**检查：一个机构只能有一个机构管理员*/
			UserInfo admin = new UserInfo();
			admin.setOrgId(orgId);
			admin.setUserLevel(UserLevel.ORG_ADMIN);
			//查询这个机构是否已经有机构管理员用户
			List<UserInfo> uList = userService.searchList(admin);
			if(CollectionUtils.isNotEmpty(uList) && uList.size()>1) {
				throw new ValidationException("数据错误: 该机构有"+uList.size()+"个机构管理员! 请联系运营人员。");
			} else if(user.getUserLevel().equals(UserLevel.ORG_ADMIN) && CollectionUtils.isNotEmpty(uList) && uList.size()==1) {
				throw new ServiceException("该机构（"+uList.get(0).getOrgName()+"）已经有管理员，不能再添加!");
			} else if(CollectionUtils.isEmpty(uList) && user.getUserLevel().equals(UserLevel.ORG_USER)){
				throw new ServiceException("该机构还没有管理员，请先添加机构管理员!");
			} else if(!sessionOrgId.equals(orgId) && !user.getUserLevel().equals(UserLevel.ORG_ADMIN) ){
				throw new ServiceException("运营商不能跨机构建非管理员级别的用户!");
			}
		}else{//其他任何人，都只能建机构操作员
			user.setUserLevel(UserLevel.ORG_USER);//用户级别：机构操作员
		}
		
		user.setUserId(IdentifieUtil.getGuId());
		user.setPwd(MD5Util.MD5Encrypt(CustomConst.DEFAULT_PASSWORD));//默认密码
		user.setOrgId(orgId);//用户的机构
		user.setOrgType(orgType);//机构类型
		user.setCreateTime(new Date());//创建时间
		user.setCreateUserid(sessionUserId);//创建人
//		user.setFstate(StringUtils.defaultIfEmpty(user.getFstate(), Fstate.USABLE));//新建用户默认启用
		
		//检查UserInfo数据项长度
		validateFieldLength(user);
		
		//添加用户
		userService.addUser(user);
	}
	
	/**
	 * 修改用户
	 * @author	黄文君
	 * @date	2017年3月23日 下午2:31:59
	 * @param	user
	 * @return	void
	 * @throws	ValidationException 
	 */
	@RequestMapping("updateUser")
	@ResponseBody
	public void updateUser(UserInfo user, HttpSession session) throws ValidationException {
		//检查: 非空检查
		LocalAssert.notBlank(user.getUserId(), "用户id，不能为空!");
		//LocalAssert.notBlank(user.getUserNo(), "账号，不能为空!");
		//LocalAssert.notBlank(user.getPwd(), "密码，不能为空!");
//		LocalAssert.notBlank(user.getFstate(), "状态，不能为空!");
		LocalAssert.notBlank(user.getUserName(), "用户名称，不能为空!");
		LocalAssert.notBlank(user.getMobilePhone(), "联系电话，不能为空!");
		
		UserInfo userInfo = userService.find(UserInfo.class, user.getUserId());
		Assert.notNull(userInfo, "用户不存在（userId: "+user.getUserId()+"）!");
		
		//检查: 登陆账号不能重复
		if(userService.existedUserno(user.getUserNo(), user.getUserId())){
			throw new ValidationException("该账号已经存在，不能添加!");
		}
		
		String sessionUserId = (String)session.getAttribute(LoginUser.SESSION_USERID);
		Long sessionOrgId = (Long)session.getAttribute(LoginUser.SESSION_USER_ORGID);
		String sessionOrgType = (String)session.getAttribute(LoginUser.SESSION_USER_ORG_TYPE);
		//只有运营商（服务商），才可能跨机构维护用户!!
		if(!OrgType.PLATFORM.equals(sessionOrgType)){
			Assert.notNull(userInfo.getOrgId(), "新增用户的机构ID，不能为空!");
			if(sessionOrgId.longValue()!=userInfo.getOrgId()){
				throw new ValidationException("非法的请求，你只能维护本机构的用户!");
			}
		}
		
		////userInfo.setUserNo(user.getUserNo());//不能修改
		userInfo.setUserName(user.getUserName());
		//userInfo.setPwd(MD5Util.MD5Encrypt(user.getPwd()));
		userInfo.setMobilePhone(user.getMobilePhone());
//		userInfo.setFstate(user.getFstate());
		userInfo.setTfRemark(user.getTfRemark());
		userInfo.setModifyTime(new Date());
		userInfo.setModifyUserid(sessionUserId);
		
		//检查UserInfo数据项长度
		validateFieldLength(user);
		
		//修改用户
		userService.updateInfoCover(userInfo);
	}
	
	/**
	 * 重置用户密码
	 * @author	黄文君
	 * @date	2017年3月23日 下午2:33:37
	 * @param	userId
	 * @return	void
	 * @throws	ValidationException 
	 */
	@RequestMapping("resetUserPwd")
	@ResponseBody
	public void resetUserPwd(String userId, HttpSession session) throws ValidationException{
		LocalAssert.notBlank(userId, "用户id，不能为空!");
		
		UserInfo userInfo = userService.find(UserInfo.class, userId);
		Assert.notNull(userInfo, "用户不存在（userId: "+userId+"）!");
		
		Long sessionOrgId = (Long)session.getAttribute(LoginUser.SESSION_USER_ORGID);//机构id
		String sessionOrgType = (String)session.getAttribute(LoginUser.SESSION_USER_ORG_TYPE);//机构类型
		String sessionUserLevel = (String)session.getAttribute(LoginUser.SESSISON_USER_LEVEL);//用户级别
		
		//只有系统管理员，才能重系统管理员的密码
		logger.debug("sessionUserLevel="+sessionUserLevel+", userLevel=" + userInfo.getUserLevel());
		if(UserLevel.SUPER_ADMIN.equals(userInfo.getUserLevel()) && !UserLevel.SUPER_ADMIN.equals(sessionUserLevel)){
			throw new ValidationException("只有系统管理员，才能重置系统管理员的密码!!");
		}
		
		//只有运营商（服务商），才可能跨机构维护用户!!
		if(!OrgType.PLATFORM.equals(sessionOrgType) && sessionOrgId.longValue()!=userInfo.getOrgId()){
			throw new ValidationException("非法的请求，你只能重置本机构用户的密码!");
		}
		
		UserInfo ui = new UserInfo();
		ui.setUserId(userId);
		ui.setPwd(MD5Util.MD5Encrypt(CustomConst.DEFAULT_PASSWORD));//用户默认密码
		userService.updateInfo(ui);
	}
	
	/**
	 * 【用户状态】启用、停用、注销
	 * @author	黄文君
	 * @date	2017年3月23日 下午2:34:59
	 * @param	userId
	 * @param	fstate
	 * @return	void
	 * @throws	ValidationException 
	 */
	@RequestMapping("updateFstate")
	@ResponseBody
	public void updateFstate(String userId, String fstate, HttpSession session) throws ValidationException{
		LocalAssert.notBlank(userId, "用户id，不能为空!");
		LocalAssert.notBlank(fstate, "状态，不能为空!");
		if(!ArrayUtils.contains(new String[]{Fstate.USABLE, Fstate.DISABLE, Fstate.REMOVED}, fstate)){
			throw new ValidationException("未知的状态值!");
		}
		
		UserInfo userInfo = userService.find(UserInfo.class, userId);
		Assert.notNull(userInfo, "用户不存在（userId: "+userId+"）!");
		
		Long sessionOrgId = (Long)session.getAttribute(LoginUser.SESSION_USER_ORGID);//机构id
		String sessionOrgType = (String)session.getAttribute(LoginUser.SESSION_USER_ORG_TYPE);//机构类型
		String sessionUserLevel = (String)session.getAttribute(LoginUser.SESSISON_USER_LEVEL);//用户级别
		
		//只有系统管理员，才能重系统管理员的密码
		logger.debug("sessionUserLevel="+sessionUserLevel+", userLevel=" + userInfo.getUserLevel());
		if(UserLevel.SUPER_ADMIN.equals(userInfo.getUserLevel()) && !UserLevel.SUPER_ADMIN.equals(sessionUserLevel)){
			throw new ValidationException("只有系统管理员，才能更新系统管理员的用户状态!!");
		}
		
		//只有运营商（服务商），才可能跨机构维护用户!!
		if(!OrgType.PLATFORM.equals(sessionOrgType) && sessionOrgId.longValue()!=userInfo.getOrgId()){
			throw new ValidationException("非法的请求，你只能重置本机构的用户密码!");
		}
		
		UserInfo ui = new UserInfo();
		ui.setUserId(userId);
//		ui.setFstate(fstate);
		userService.updateInfo(ui);
	}
	
	/**
	 * 检查UserInfo数据项长度
	 * @author	黄文君
	 * @date	2017年4月10日 下午6:17:05
	 * @param	u
	 * @throws	ValidationException
	 * @return	void
	 */
	private void validateFieldLength(UserInfo u) throws ValidationException {
		if(u.getUserNo()!=null && u.getUserNo().length()>25){
			throw new ValidationException("账号，长度不能超过25个字符!");
		}
		if(u.getUserName()!=null && u.getUserName().length()>25){
			throw new ValidationException("用户名，长度不能超过25个字符!");
		}
		if(u.getPwd()!=null && u.getPwd().length()>50){
			throw new ValidationException("密码，长度不能超过50个字符!");
		}
		if(u.getMobilePhone()!=null && u.getMobilePhone().length()>15){
			throw new ValidationException("联系电话，长度不能超过15个字符!");
		}
		if(u.getTfRemark()!=null && u.getTfRemark().length()>25){
			throw new ValidationException("备注，长度不能超过25个字符!");
		}
	}
	
	/**
	 * 用户修改自己的密码
	 */
	@RequestMapping("modifyUserPwd")
	@ResponseBody
	public void modifyUserPwd(String oldPwd,String newPwd, HttpSession session) throws ValidationException{
		LocalAssert.notBlank(oldPwd, "原始密码不能为空!");
		LocalAssert.notBlank(newPwd, "新密码不能为空!");

		
		String sessionUserId = (String)session.getAttribute(LoginUser.SESSION_USERID);//用户id
		
		UserInfo userInfo = userService.find(UserInfo.class, sessionUserId);
		
		if(userInfo == null){
			throw new ValidationException("用户不存在");
		}
		if(!(userInfo.getPwd().toUpperCase()).equals(oldPwd.toUpperCase())){
			throw new ValidationException("原密码输入错误");
		}
		
		userInfo.setPwd(newPwd);
		userInfo.setModifyTime(new Date());
		userInfo.setModifyUserid(sessionUserId);
		userService.updateInfo(userInfo);
	}
	
}
