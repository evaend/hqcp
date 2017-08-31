package com.phxl.hqcp.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phxl.core.base.entity.Pager;
import com.phxl.core.base.exception.ValidationException;
import com.phxl.core.base.service.impl.BaseService;
import com.phxl.core.base.util.BaseUtils;
import com.phxl.core.base.util.JSONUtils;
import com.phxl.core.base.util.MD5Util;
import com.phxl.core.base.util.SHAUtil;
import com.phxl.hqcp.common.constant.CustomConst;
import com.phxl.hqcp.dao.UserInfoMapper;
import com.phxl.hqcp.entity.UserInfo;
import com.phxl.hqcp.service.UserService;

/**
 * 用户服务（隶属于机构）
 * @date	2017年3月23日 下午2:06:24
 * @author	黄文君
 * @version	1.0
 * @since	JDK 1.6
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserInfoMapper userInfoMapper;

	public int findUserNoExist(String userNo) {
	    return userInfoMapper.findUserNoExist(userNo);
	}

	/**
	 * 验证用户登录
	 * @throws JsonProcessingException 
	 */
	public Map<String,Object> checkLoginInfo(String userNo, String pwd,String token) throws JsonProcessingException {
		Map<String,Object> map = new HashMap<String,Object>();
		String result = "success";
		UserInfo userInfo = new UserInfo();
		userInfo.setUserNo(userNo);
		//由于用户账号是唯一的，所以使用账号验证用户是否存在
		UserInfo newUserInfo = this.searchEntity(userInfo);
		boolean loginSuccess = true;
		
		if(newUserInfo == null){
			loginSuccess = false;
			result = "用户名不存在";
		}else{
			String sysPwd = null;
			try{
				String[] strArray =  {newUserInfo.getPwd().toUpperCase(),token};
			    String shastr = BaseUtils.sort(strArray);//将待加密的字符组排序并组成一个字符串    
				sysPwd = SHAUtil.shaEncode(shastr);//字符串加密
			}catch(Exception e)
			{
				return null;
			}
			if( sysPwd!= null && !pwd.toUpperCase().equals(sysPwd.toUpperCase())){
				loginSuccess = false;
				result = "用户名或者密码错误";
			}else if(!newUserInfo.getAuditFstate().equals(CustomConst.AuditFstate.PASSED)){
				loginSuccess = false;
				result = "用户未审核通过";
			}else{//需要根据用户id查询所属机构的状态
				if(newUserInfo.getOrgId() != null){
					String orgFstate = this.findOrgFstateByOrgId(newUserInfo.getOrgId());
					if(StringUtils.isBlank(orgFstate)){
						loginSuccess = false;
						result = "用户所属机构状态未知";
					}else if(!orgFstate.equals(CustomConst.Fstate.USABLE)){
						loginSuccess = false;
						result = "用户所属机构已停用";
					}
				}else{
					loginSuccess = false;
					result = "用户所属机构未知";
				}
			}
		}
		map.put("loginStatus", loginSuccess);
		if(loginSuccess == true){
			map.put("userInfo", this.findUserInfoById(newUserInfo.getUserId()));
			//如果是默认密码，提示修改密码
			if((MD5Util.MD5Encrypt(CustomConst.DEFAULT_PASSWORD).toUpperCase()
					).equals(newUserInfo.getPwd().toUpperCase())){
				result = "您的密码是默认密码，请及时修改！";
			}
//			//查询当前用户的菜单权限
			JSONUtils json = new JSONUtils();
			map.put("menuList", json.toPrettyJson(this.selectUserMenu(newUserInfo.getUserId())));            
		}
		map.put("result", result);
		return map;
	}

	/**
	 * 查询用户功能菜单树
	 */
	public List<Map<String,Object>> selectUserMenu(String userId) {
		// TODO Auto-generated method stub
//		Map<String,Object> resultMap = new HashMap<String,Object>();
//		List<Map<String,Object>> resultMaps = new ArrayList<Map<String,Object>>();
//		List<Map<String,Object>> allMenus = menuService.searchMenuWithLevel(null);//查询所有模块
//		List<Map<String,Object>> userMenus = userInfoMapper.selectUserMenu(userId);
//		//解析查询结果
//		for(Map<String,Object> userMenu:userMenus){
//			String menu_id = userMenu.get("MENU_ID").toString();
//			Integer level = 0;
//			Map<String,Object> sMenu = null;
//			for(Map<String,Object> allMenu:allMenus){
//				if(menu_id.equals(allMenu.get("id").toString())){
//					level = Integer.valueOf(allMenu.get("level").toString());
//					sMenu = allMenu;
//					allMenus.remove(allMenu);
//					break;
//				}
//			}
//			resultMap = sMenu;
//			//从功能权限节点往上寻找，直到到根节点为止
//			while(level > 0){
//				menu_id = sMenu.get("parentId").toString();
//				//上级节点已经存在在拼装的结果中，直接将新节点放在父节点的submenus中
//				boolean isParentExist = isMenuExist(menu_id,resultMaps,sMenu);
//				if(isParentExist){
//					level = 0;//该节点已经找到位置，可以退出本次循环
//				}else{
//					//上级节点还未封装，将该节点从所有菜单中抠出来，组装新的节点放在结果中
//					for(Map<String,Object> allMenu:allMenus){
//						if(menu_id.equals(allMenu.get("id").toString())){
//							level = Integer.valueOf(allMenu.get("level").toString());
//							sMenu = allMenu;
//							List<Map<String,Object>> tmpMaps = new ArrayList<Map<String,Object>>();
//							tmpMaps.add(resultMap);
//							sMenu.put("subMenus", tmpMaps);
//							resultMap = sMenu;
//							allMenus.remove(allMenu);
//							break;
//						}
//					}
//				}
//			}
//			//拼到根的结点是否已在最终结果中存在，如果没有拼到根节点，可以忽略这步
//			if(Integer.valueOf(sMenu.get("level").toString()).equals(0)){
//				boolean isExist = isMenuExist(sMenu.get("id").toString(),resultMaps,null);
//				if(!isExist){//不存在，则添加；否则不用管
//					resultMaps.add(resultMap);
//				}
//			}
//		}
//		return resultMaps;
		return null;
	}
	
	/**
	 * 在已拼装的结果中查询特定节点
	 * @return
	 */
	public boolean isMenuExist(String id,List<Map<String,Object>> listMaps,Map<String,Object> aMenu){
		   boolean exist = false;
		   for(Map<String,Object> listMap : listMaps){
			   List<Map<String,Object>> subMaps = null;
			   if(listMap.get("subMenus") != null){
				   subMaps = (List<Map<String,Object>>)listMap.get("subMenus");
			   }
			   if(id.equals(listMap.get("id").toString())){
				   exist = true;
				   if(subMaps != null && aMenu != null){
					   subMaps.add(aMenu);
				   }
				   break;
				}
			   if(subMaps != null){
				   exist = isMenuExist(id,subMaps,aMenu);
			   }
		   }
		   return exist;
	}
	public String getSometh() {
		// TODO Auto-generated method stub
		return "test123456";
	}
	
	/**
	 * 判断指定机构是否运营商（服务商）
	 * @author	黄文君
	 * @date	2017年3月24日 下午9:06:35
	 * @param	orgId
	 * @return	boolean
	 */
	public boolean assertServiceOrgByOrgId(Long orgId) {
		return userInfoMapper.findServiceOrgByOrgId(orgId)>0?true:false;
	}

	/**
	 * 查询机构用户列表
	 * @author	黄文君
	 * @date	2017年3月24日 下午8:20:36
	 * @param	pager
	 * @return	List<UserInfo>
	 */
	public List<UserInfo> findOrgUserList(Pager pager) {
		return userInfoMapper.findOrgUserList(pager);
	}
	
	/**
	 * 根据用户id查询用户信息
	 * @author	黄文君
	 * @date	2017年4月6日 下午2:02:45
	 * @param userId
	 * @return
	 * @return	UserInfo
	 */
	public UserInfo findUserInfoById(String userId){
		return userInfoMapper.findUserInfoById(userId);
	}

	/**
	 * 判断用户名（登录名）是否存在
	 * @date	2017年3月24日 下午9:10:07
	 * @param	userno
	 * @param	excludeUserId
	 * @return	boolean
	 */
	public boolean existedUserno(String userno, String excludeUserId) {
		return userInfoMapper.countUserno(userno, excludeUserId)>0?true:false;
	}

	/**
	 * 查看指定机构的机构类型
	 * @author	黄文君
	 * @date	2017年3月24日 下午5:00:11
	 * @param	orgId
	 * @return	String
	 */
	public String findOrgTypeByOrgId(Long orgId) {
		return userInfoMapper.findOrgTypeByOrgId(orgId);
	}


	public String findOrgFstateByOrgId(Long orgId) {
		return userInfoMapper.findOrgFstateByOrgId(orgId);
	}
	
	/**
	 * 查询还没有机构管理员的机构列表
	 * @author	黄文君
	 * @date	2017年3月29日 下午5:21:26
	 * @return	List<Map<String,Object>>
	 */
	@Override
	public List<Map<String, Object>> findWithoutAdminOrgList(Pager pager) {
		return userInfoMapper.findWithoutAdminOrgList(pager);
	}
	
	/**
	 * 添加用户
	 * @author	黄文君
	 * @date	2017年3月30日 上午9:12:26
	 * @param	user
	 * @return	void
	 * @throws	ValidationException 
	 */
	@Override
	public void addUser(UserInfo user) throws ValidationException {
		if(insertInfo(user)>0){
			logger.debug("用户已添加");
			
			//如果添加的用户是机构管理员，把这个用户添加到该机构的核心组内
//			if(user.getUserLevel().equals(UserLevel.ORG_ADMIN)) {
//				Group group = new Group();
//				group.setOrgId(user.getOrgId());
//				group.setGroupType(GroupType.ORG_REF);
//				List<Group> gList = super.searchList(group);
//				if(CollectionUtils.isEmpty(gList)){
//					throw new ValidationException("该用户的机构（orgId: "+user.getOrgId()+"）缺少核心组！");
//				}else if(CollectionUtils.isNotEmpty(gList) && gList.size()>1){
//					throw new ValidationException("数据错误: 该用户的机构（orgId: "+user.getOrgId()+"）有多个核心组！");
//				}else{
//					GroupUser gu = new GroupUser();
//					gu.setGroupId( gList.get(0).getGroupId());
//					gu.setUserId(user.getUserId());
//					//机构的核心组添加用户
//					if(super.insertInfo(gu)>0){
//						logger.debug("用户已添加到本机构的核心组内");
//					}
//				}
//			}
		}
	}
}
