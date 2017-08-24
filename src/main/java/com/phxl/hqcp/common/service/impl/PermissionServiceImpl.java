package com.phxl.hqcp.common.service.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.phxl.core.base.annotation.RequiredPermission;
import com.phxl.core.base.util.SysContent;
import com.phxl.hqcp.common.constant.CustomConst;
import com.phxl.hqcp.common.constant.CustomConst.LoginUser;

/**
 * 接口访问控制
 * 2017年4月13日 上午11:16:56
 * @author 陶悠
 *
 */
@Component
public class PermissionServiceImpl
 {
	private Log logInfo = LogFactory.getLog(getClass());   
	
	//前置方法a
	public void permissionBefore(JoinPoint point) {
		try{
			HttpServletRequest request = SysContent.getRequest();
			HttpServletResponse response = SysContent.getResponse();
			HttpSession session = request.getSession();
			if (request == null || response == null || point == null || session == null) {
				response.setStatus(CustomConst.ResponseStatus.PERMISSIONREFUSE);//非法访问
				return;
			}

			//方法名
			String method = point.getSignature().getName();
			//获取到这个类上面的方法全名
		     Method meths[] = point.getSignature().getDeclaringType().getMethods();
		     //获取到这个类上面的方法上面的注释
		     RequiredPermission requiredPermission = null;
			 for (Method m : meths) {
					if(m.getName().equals(method)){
						Annotation[] annotations = m.getAnnotations();
					    for(Annotation annotation : annotations){
					        if(annotation.annotationType() == RequiredPermission.class){
					        	requiredPermission = m.getAnnotation(RequiredPermission.class);
					        }
					    }
					    break;
					}
				}	
			//接口存在权限注解
			if(requiredPermission != null){
				String requiredP = requiredPermission.value();
				if(StringUtils.isNotBlank(requiredP)){
					/**
					 * 从session中取出当前用户的权限列表，根据接口的注解判断是否有权限
					 */
					if(session.getAttribute(LoginUser.CUR_USER_MENULIST) != null){
						String userMenuList = (String)session.getAttribute(LoginUser.CUR_USER_MENULIST);
						if(userMenuList.indexOf(requiredP) == -1){//没有对应的权限
							response.setStatus(CustomConst.ResponseStatus.PERMISSIONREFUSE);//非法访问
							return;
						}
					}					
				}
			}
		}catch(Exception e){
			logInfo.error("权限控制方法出错："+e.getMessage());
		}
	}

}

