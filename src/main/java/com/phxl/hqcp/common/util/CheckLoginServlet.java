package com.phxl.hqcp.common.util;

import java.io.IOException;
import java.util.Queue;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.phxl.core.base.util.BaseUtils;
import com.phxl.core.base.util.SHAUtil;
import com.phxl.core.base.util.SysContent;
import com.phxl.core.base.util.SystemConfig;
import com.phxl.hqcp.common.constant.CustomConst;
import com.phxl.hqcp.common.constant.CustomConst.LoginUser;

/**
 * 除了特意要放过的，如果请求里有session且token正确，直接访问链接，否则到登录页面
 * 
 * @author taoyou
 *
 */

public class CheckLoginServlet implements Filter {

	// 白名单，可以写入数据库
	String[] Reg = {
	};

	/**
	 * 实现过滤方法
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		SysContent.setCurrentLocal(request, response);

		String targetURL = request.getRequestURI();
		String contextPath = request.getContextPath();
		String serviceName = request.getServerName();

		// String JSESSIONID = request.getSession().getId();
		// Cookie cookie = new Cookie("JSESSIONID",
		// JSESSIONID);//保存session并传回到前端，便于下一次验证
		// cookie.setDomain("");//留着跨域验证
		// cookie.setPath("/");
		// response.addCookie(cookie);
		filterChain.doFilter(request, response);
		return;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	public String updateSessionToken(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] strArray = { String.valueOf(System.currentTimeMillis()), String.valueOf(Math.random()) };
			String shastr = BaseUtils.sort(strArray);// 将待加密的字符组排序并组成一个字符串
			String newToken = SHAUtil.shaEncode(shastr);// 字符串加密
			Cookie cookieToken = new Cookie("token", newToken);// 新的token返回到token
			cookieToken.setPath("/");
			response.addCookie(cookieToken);
			return newToken;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
