package com.phxl.hqcp.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.phxl.core.base.exception.ValidationException;

/**
 * base64编码格式文件处理工具类
 * @date	2017年6月6日 下午2:44:56
 * @author	黄文君
 * @version	1.0
 * @since	JDK 1.6
 */
public class Base64FileHelper {
	
	/**
	 * base64格式文件转成数据流
	 * @author	黄文君
	 * @date	2017年6月2日 上午9:18:17
	 * @param	base64File
	 * @throws	Exception
	 * @return	InputStream
	 */
	public static InputStream decodeFile(String base64File, StringBuffer filePath) throws Exception {
		Assert.notNull(filePath, "filePath，不能为空");
		if(StringUtils.isBlank(base64File)){
			return null;
		}
		//获取文件类型
		int beginIndex = base64File.indexOf("/");
		int endIndex = base64File.indexOf(";base64,");
		String fileType = base64File.substring(beginIndex+1,endIndex);
		if(StringUtils.isNotBlank(fileType)){
		    fileType = "." + fileType;
		    filePath.append(fileType);
		}else{
		    throw new ValidationException("未知的上传文件类型!");
		}
		//获取文件
		String file = base64File;
		file = file.replaceAll("data:image/jpeg;base64,", "");  
		file = file.replaceAll("data:image/jpg;base64,", "");
		file = file.replaceAll("data:image/png;base64,", ""); 
		file = file.replaceAll("data:image/gif;base64,", ""); 
		file = file.replaceAll("data:image/bmp;base64,", "");
		file = file.replaceAll("data:application/pdf;base64,", "");
		
		byte[] buffer = new Base64().decodeBase64(file);
		//return new ByteArrayInputStream(buffer);
		return new BufferedInputStream(new ByteArrayInputStream(buffer));
	}
	
}
