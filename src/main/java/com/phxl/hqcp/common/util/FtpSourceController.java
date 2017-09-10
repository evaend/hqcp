package com.phxl.hqcp.common.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phxl.core.base.util.FTPUtils;

/**
 * FTP资源访问类接口 <br/>
 * @date: 2016年7月13日 上午9:11:11
 * @author 黄文君
 * @version 1.0
 * @since JDK 1.6
 */
@Controller
@RequestMapping("/ftp")
public class FtpSourceController {
	public final Logger logger = LoggerFactory.getLogger(FTPUtils.class);
	
	FTPUtils service = new FTPUtils();
	
	@RequestMapping("/**/*.*")
	@ResponseBody
	public void download(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
		String uri=URLDecoder.decode(request.getRequestURI(), "UTF-8");
		logger.debug("uri: "+uri);
		String ftpBaseUri=request.getContextPath()+"/ftp";
		String filePath=uri.substring(uri.indexOf(ftpBaseUri)+ftpBaseUri.length());
		logger.debug("FTP资源访问, filePath="+filePath);
		
		Assert.notNull(filePath, "filePath不能为空!");
		FTPUtils.download(filePath, response, null);
	}
	
	@RequestMapping("/post")
	@ResponseBody
	public String postFile(){
		return "success";
	}
}
