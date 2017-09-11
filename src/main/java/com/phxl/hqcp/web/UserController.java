package com.phxl.hqcp.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ServiceException;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.util.IdentifieUtil;
import com.phxl.core.base.util.LocalAssert;
import com.phxl.hqcp.common.constant.CustomConst;
import com.phxl.hqcp.common.constant.CustomConst.AuditFstate;
import com.phxl.hqcp.common.constant.CustomConst.LoginUser;
import com.phxl.hqcp.common.constant.CustomConst.OrgType;
import com.phxl.hqcp.common.util.MD5Util1;
import com.phxl.hqcp.entity.OrgInfo;
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
	 * 查询系统所有用户列表（运营人员使用）
	 */
	@RequestMapping("findAllUserList")
	@ResponseBody
	public Pager<UserInfo> findAllUserList(String searchName, 
											String auditFstate,
											    Integer pagesize, 
											    Integer page, 
											    HttpSession session) {
		Pager pager = new Pager();
		pager.setPageSize(pagesize);
		pager.setPageNum(page);
		
		pager.addQueryParam("searchName", searchName);
//		pager.addQueryParam("sessionOrgId", session.getAttribute(LoginUser.SESSION_USER_ORGID));
//		pager.addQueryParam("sessionOrgType", session.getAttribute(LoginUser.SESSION_USER_ORG_TYPE));
//		pager.addQueryParam("sessionUserLevel", session.getAttribute(LoginUser.SESSISON_USER_LEVEL));
		pager.addQueryParam("auditFstate", auditFstate);	

		pager.setRows(userService.findOrgUserList(pager));
		return pager;
	}
	
	/**
	 * 查询指定用户信息
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
			if(sessionOrgId.longValue() != orgId.longValue()){
				throw new ServiceException("你不能查看其他机构的用户信息!");
			}
		}
		return user;
	}
	
	/**
	 * 新增编辑用户
	 */
	@RequestMapping("addUpdateUser")
	@ResponseBody
	public void addUpdateUser(UserInfo user,HttpSession session) throws Exception {
		//检查: 非空检查
		LocalAssert.notBlank(user.getUserNo(), "账号，不能为空!");
		LocalAssert.notBlank(user.getUserName(), "用户名称，不能为空!");
		LocalAssert.notBlank(user.getMobilePhone(), "联系电话，不能为空!");
		Assert.notNull(user.getOrgId(), "用户的所属机构，未知!");

		String sessionUserId = (String)session.getAttribute(LoginUser.SESSION_USERID);
		Long sessionOrgId = (Long)session.getAttribute(LoginUser.SESSION_USER_ORGID);
		String sessionOrgType = (String)session.getAttribute(LoginUser.SESSION_USER_ORG_TYPE);
		String sessionUserLevel = (String)session.getAttribute(LoginUser.SESSISON_USER_LEVEL);
		
		OrgInfo orgInfo = userService.find(OrgInfo.class, user.getOrgId());//查看指定所属机构的机构类型
		LocalAssert.notNull(orgInfo, "机构不存在!");
		user.setOrgType(orgInfo.getOrgType());//机构类型
		
		
		if(StringUtils.isBlank(orgInfo.getTfAccessory()) 
				|| StringUtils.isBlank(orgInfo.getOrgCode()) ){
			throw new ValidationException("机构组织机构代码或机构附件为空，不允许新增用户，请先去机构管理维护");
		}
		
		//检查UserInfo数据项长度
		validateFieldLength(user);
		
		if(StringUtils.isBlank(user.getUserId())){//新增
			LocalAssert.notBlank(user.getPwd(), "密码，不能为空!");
			LocalAssert.notBlank(user.getConfirmPwd(), "确认密码，不能为空!");
			if(!user.getConfirmPwd().equals(user.getPwd())){
				throw new ValidationException("两遍密码输入不一致，请检查");
			}
			//检查: 登陆账号，不能重复
			if(userService.existedUserno(user.getUserNo(), null)){
				throw new ValidationException("该账号已经存在，不能添加!");
			}
			
			user.setUserId(IdentifieUtil.getGuId());
			user.setPwd(MD5Util1.MD5Encrypt(user.getPwd()));//密码加密
			user.setCreateTime(new Date());//创建时间
			user.setCreateUserid(sessionUserId);//创建人
			user.setAuditFstate(AuditFstate.PASSED);//新建用户默认审核通过
			userService.insertInfo(user);
			
		}else{//编辑
			//检查: 登陆账号不能重复
			if(userService.existedUserno(user.getUserNo(), user.getUserId())){
				throw new ValidationException("该账号已经存在，不能添加!");
			}
			user.setModifyTime(new Date());
			user.setModifyUserid(sessionUserId);
			userService.updateInfo(user);
		}
	}
	
	/**
	 * 修改用户
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
		
		//只有运营商（服务商），才可能跨机构维护用户!!
		if(!OrgType.PLATFORM.equals(sessionOrgType)){
			throw new ValidationException("只有运营机构，才能重置系统用户的密码!!");
		}
		
		UserInfo ui = new UserInfo();
		ui.setUserId(userId);
		ui.setPwd(MD5Util1.MD5Encrypt(CustomConst.DEFAULT_PASSWORD));//用户默认密码
		userService.updateInfo(ui);
	}
	
	@RequestMapping("registerUserInfo")
	@ResponseBody
	public void registerUserInfo(HttpServletRequest request,
			HttpSession session) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));//配置项:默认日期格式
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//配置项:忽略未知属性
		UserInfo user = objectMapper.readValue(request.getReader(), UserInfo.class);
		Assert.notNull(user, "请传用户注册信息!");
		
		//检查: 非空检查
		LocalAssert.notBlank(user.getUserNo(), "账号，不能为空!");
		LocalAssert.notBlank(user.getUserName(), "用户名称，不能为空!");
		LocalAssert.notBlank(user.getMobilePhone(), "联系电话，不能为空!");
		Assert.notNull(user.getOrgId(), "用户的所属机构，未知!");
		
		LocalAssert.notBlank(user.getPwd(), "密码，不能为空!");
		LocalAssert.notBlank(user.getConfirmPwd(), "确认密码，不能为空!");
		if(!user.getConfirmPwd().equals(user.getPwd())){
			throw new ValidationException("两遍密码输入不一致，请检查");
		}
		//检查: 登陆账号，不能重复
		if(userService.existedUserno(user.getUserNo(), null)){
			throw new ValidationException("该账号已经存在，不能添加!");
		}
		OrgInfo orgInfo = userService.find(OrgInfo.class, user.getOrgId());//查看指定所属机构的机构类型
		LocalAssert.notNull(orgInfo, "机构不存在!");
		user.setOrgType(orgInfo.getOrgType());//机构类型
		
		//检查UserInfo数据项长度
		validateFieldLength(user);
		user.setUserId(IdentifieUtil.getGuId());
		user.setPwd(MD5Util1.MD5Encrypt(user.getPwd()));//密码加密
		user.setCreateTime(new Date());//创建时间
		user.setAuditFstate(AuditFstate.AWAIT_AUDIT);//注册用户状态待审核
		
		String newTfAccessoryFile = null;//新附件
		if(StringUtils.isNotBlank(user.getAuditTfAccessory()) && user.getAuditTfAccessory().indexOf(";base64,")!=-1){//新附件
			newTfAccessoryFile = user.getAuditTfAccessory();//新附件
		}
		
		//没有机构组织代码和证件提交，且系统中也不存在 不容许提交注册信息
		if((newTfAccessoryFile == null && StringUtils.isBlank(orgInfo.getTfAccessory())) 
				|| (StringUtils.isBlank(user.getAuditOrgCode()) && StringUtils.isBlank(orgInfo.getOrgCode())) ){
			throw new ValidationException("机构组织代码和附件不能为空");
		}
		//添加注册用户信息
		userService.saveRegisterUserInfo(user,orgInfo, newTfAccessoryFile);		
	}
	
	@RequestMapping("auditUserInfo")
	@ResponseBody
	public void auditUserInfo(String userId,String auditFstate, HttpServletRequest request,
			HttpSession session) throws Exception {
		
		//检查: 非空检查
		LocalAssert.notBlank(userId, "请传入用户id!");
		LocalAssert.notBlank(auditFstate, "请传入审核状态");
		
		String sessionUserId = (String)session.getAttribute(LoginUser.SESSION_USERID);//用户id
		String sessionUserName = (String)session.getAttribute(LoginUser.SESSION_USERNAME);//用户名
		
		UserInfo userInfo = userService.find(UserInfo.class, userId);
		LocalAssert.notNull(userInfo, "用户不存在!");
		userInfo.setAuditFstate(auditFstate);
		userInfo.setAuditId(sessionUserId);
		userInfo.setAuditName(sessionUserName);
		userInfo.setAuditTime(new Date());
		userInfo.setCreateUserid(sessionUserId);
		userInfo.setModifyUserid(sessionUserId);
		userInfo.setModifyTime(new Date());
		//审核用户信息
		userService.auditUserInfo(userInfo);		
	}

	/**
	 * 检查UserInfo数据项长度
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
		if(u.getUserAddress()!=null && u.getUserAddress().length()>100){
			throw new ValidationException("地址，长度不能超过100个字符!");
		}
	}
	
	/**
	 * 用户修改自己的密码
	 */
	@RequestMapping("modifyUserPwd")
	@ResponseBody
	public void modifyUserPwd(String newPwd, String repeatNewPwd,HttpSession session) throws ValidationException{
		LocalAssert.notBlank(newPwd, "新密码不能为空!");
		LocalAssert.notBlank(repeatNewPwd, "重复密码不能为空!");

		if(!newPwd.equals(repeatNewPwd)){
			throw new ValidationException("两遍密码输入不一致，请检查");
		}
		
		String sessionUserId = (String)session.getAttribute(LoginUser.SESSION_USERID);//用户id
		
		UserInfo userInfo = userService.find(UserInfo.class, sessionUserId);
		
		if(userInfo == null){
			throw new ValidationException("用户不存在");
		}
		
		userInfo.setPwd(MD5Util1.MD5Encrypt(newPwd));
		userInfo.setModifyTime(new Date());
		userInfo.setModifyUserid(sessionUserId);
		userService.updateInfo(userInfo);
	}
	
}
