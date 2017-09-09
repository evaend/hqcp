/** 
 * Project Name:hqcp
 * File Name:CustomConst.java 
 * Package Name:com.phxl.hqcp.common.constant 
 * Copyright (c) 2017, PHXL All Rights Reserved. 
 * 
 */
package com.phxl.hqcp.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量定义类: 用于定义系统中用到的常量值或者码值
 * @version	1.0
 * @since	JDK 1.6
 */
public class CustomConst {

	/**
	 * 用户级别
	 * @version	1.0
	 * @since	JDK 1.6
	 */
	public static final class UserLevel {
		/**01、系统管理员*/
		public static final String SUPER_ADMIN = "01";
		/**02、机构管理员*/
		public static final String ORG_ADMIN = "02";
		/**03、机构操作员*/
		public static final String ORG_USER = "03";
	}
	
	/**
	 * 机构类型
	 * @version	1.0
	 * @since	JDK 1.6
	 */
	public static final class OrgType {
		/**01、医院*/
		public static final String HOSPITAL = "01";
		/**02、服务商*/
		public static final String SUPPLIER = "02";
		/**03、监管部门*/
		public static final String GOV_SUPERVISE = "03";
		/**09、运营商*/
		public static final String PLATFORM = "09";
	}
	
	/**
	 * 用户成功登录后存储在session里的信息
	 */
	public final static class LoginUser {
		/**用户对象*/
		public final static String SESSION_USER_INFO = "sessionUserInfo";
		/**用户ID*/
		public final static String SESSION_USERID = "sessionUserid";
		/**用户NO*/
		public final static String SESSION_USERNO = "sessionUserno";
		/**用户名称*/
		public final static String SESSION_USERNAME = "sessionUsername";
		/**用户密码*/
		public final static String SESSION_PASSWORD = "sessionPassword";
		/**用户机构ID*/
		public final static String SESSION_USER_ORGID = "sessionOrgid";
		/**用户机构名称*/
		public final static String SESSION_USER_ORGNAME = "sessionOrgname";
		/**用户机构类型*/
        public final static String SESSION_USER_ORG_TYPE = "sessionOrgType";
		/**用户级别*/
        public final static String SESSISON_USER_LEVEL = "sessisonUserLevel";
    	/**当前登录用户模块权限列表-会话*/
		public final static String CUR_USER_MENULIST = "curUserMenuList";
	}
	
	/**
	 * 启用|停用状态码（只适用: 1启用、0停用，按需使用）
	 */
	public static final class FlagCode {
		/** 启用 */
		public static final short usable = 1;
		/** 停用 */
		public static final short unusable = 0;
	}
	
	/**
	 * 状态码: 启用|停用|注销
	 * @date	2017年3月23日 下午4:27:37
	
	 * @version	1.0
	 * @since	JDK 1.6
	 */
	public static final class Fstate {
		/**01:表示启用*/
		public static final String USABLE = "01";
		/**00:表示禁用*/
		public static final String DISABLE = "00";
		/**02:注销（表示: 移除、物理删除），不可再变*/
		public static final String REMOVED = "02";
	}
	
	/**用户默认密码*/
	public static final String DEFAULT_PASSWORD = "999999";

	/**
	 * 邮件发送状态
	 * @date	2017年4月12日 上午10:28:30
	
	 * @version	1.0
	 * @since	JDK 1.6
	 */
	public static final class EmailFstate {
		/**发送成功*/
		public static final String SUCCESS = "01";
		/**发送失败*/
		public static final String FAIL = "02";
		/**终止发送*/
		public static final String CLOSED = "03";
	}
	
	/**
	 * 请求返回状态，本系统另外定义的（目前都是失败状态）
	 * 2017年4月13日 上午11:16:02
	 * @author 陶悠
	 */
	public static final class ResponseStatus {
		/**失败：未登录*/
		public static final int UNLOGIN = 999;
		/**失败：token验证失败*/
		public static final int TOKENFAIL = 998;
		/**失败：非法接口访问*/
		public static final int PERMISSIONREFUSE = 997;
	}
	
	/**
	 * 是否标识（01是、00否）
	 * @date	2017年5月5日 下午4:27:25
	
	 * @version	1.0
	 * @since	JDK 1.6
	 */
	public static final class YesOrNo {
		/**01、是*/
		public static final String YES = "01";
		/**00、否*/
		public static final String NO = "00";
	}
	
	/**
	 * 审核状态 (00、保存 01、待审核，02、审核通过，03、审核不通过)
	 * @version	1.0
	 * @since	JDK 1.6
	 */
	public static final class AuditFstate {
		/**00、保存 */
		public static final String DRAFT = "00";
		/**01、待审核*/
		public static final String AWAIT_AUDIT = "10";
		/**02、审核通过*/
		public static final String PASSED = "20";
		/**03、审核不通过*/
		public static final String NO_PASS = "30";
	}
	
	/**
	 * 审核类型
	 * @version	1.0
	 * @since	JDK 1.6
	 */
	public static final class AuditType {
		/**00 、用户注册*/
		public static final String USER_REGISTER = "00";
		/**01、科室上报数据*/
		public static final String DEPT_REPORT = "01";
		/**02、质量上报数据*/
		public static final String QUALITY_REPORT = "02";
	}
	
	/**
	 * 字典项编码定义
	 * @version	1.0
	 * @since	JDK 1.6
	 */
	public static final class DictName {
		/**订单状态*/
		public static final String ORDER_FSTATE = "ORDER_FSTATE";
		/**单位*/
		public static final String UNIT = "UNIT";
		/**订单状态*/
		public static final String ORDER_TYPE = "ORDER_TYPE";
	}
	
	/**
     * 部门级别
     * @version 1.0
     * @since   JDK 1.6
     */
	public static final class DeptTypeName {
        /**处/部*/
        public static final String BU = "处/部";
        /**科*/
        public static final String KE = "科";
        /**组*/
        public static final String ZU = "组";
    }
	
	/**
     * 上级管理部门
     * @version 1.0
     * @since   JDK 1.6
     */
	public static final class DeptParentName {
        /**医务*/
        public static final String YW = "医务";
        /**后勤*/
        public static final String HQ = "后勤";
        /**科教*/
        public static final String KJ = "科教";
        /**独立运行*/
        public static final String DL = "独立运行";
    }
	
	/**
     * 部门业务管理范围
     * @version 1.0
     * @since   JDK 1.6
     */
	public static final class DeptWorkScope {
        /**设备*/
        public static final String SB = "设备";
        /**耗材*/
        public static final String HC = "耗材";
        /**试剂*/
        public static final String SJ = "试剂";
        /**药品*/
        public static final String YP = "药品";
        /**办公用品*/
        public static final String BG = "办公用品";
        /**总务*/
        public static final String ZW = "总务";
    }
	
	/**
     * 部门承担的其它工作
     * @version 1.0
     * @since   JDK 1.6
     */
	public static final class DeptWorkOther {
        /**本省、市卫生行政部门：医疗器械管理质控中心*/
        public static final String XZBM = "本省、市卫生行政部门:医疗器械管理质控中心";
        /**本省、市药监部门：医疗器械临床实验基地*/
        public static final String YJBM = "本省、市药监部门:医疗器械临床实验基地";
        /**本省、市质量技术监督部门：计量与测试技术合作*/
        public static final String JDBM = "本省、市质量技术监督部门:计量与测试技术合作";
    }
	
	/**
     * 医疗器械物流管理开展范围
     * @version 1.0
     * @since   JDK 1.6
     */
    public static final class LogisticsScope {
        /**医疗设备物流*/
        public static final String SBWL = "医疗设备物流";
        /**普通卫生材料物流*/
        public static final String WSWL = "普通卫生材料物流";
        /**植入、介入类耗材物流*/
        public static final String ZRWL = "植入、介入类耗材物流";
        /**消毒供应材料物流*/
        public static final String XDWL = "消毒供应材料物流";
        /**试剂物流*/
        public static final String SJWL = "试剂物流";
        /**医疗废弃物物流*/
        public static final String FQWL = "医疗废弃物物流";
    }
    
    /**
     * 卫生材料医疗器械物流管理模式
     * @version 1.0
     * @since   JDK 1.6
     */
    public static final class LogisticsType {
        /**物流管理外包*/
        public static final String WLWB = "物流管理外包";
        /**仓库外包*/
        public static final String CKWB = "仓库外包";
        /**零库存管理*/
        public static final String LKC = "零库存管理";
        /**第三方托管*/
        public static final String DSFTG = "第三方托管";
        /**二级库管*/
        public static final String EJKG = "二级库管";
    }
    
}
