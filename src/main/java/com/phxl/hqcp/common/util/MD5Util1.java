package com.phxl.hqcp.common.util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util1 {
	
	public static String MD5Encrypt(String inStr) {  
		MessageDigest md = null;  
	    String outStr = null;  
	    try {
	    	md = MessageDigest.getInstance("MD5");         //可以选中其他的算法如SHA   
		    byte[] digest = md.digest(inStr.getBytes());       
		    //返回的是byet[]，要转化为String存储比较方便  
		    outStr = bytetoString(digest);  
	    }catch (NoSuchAlgorithmException nsae) {   
		     nsae.printStackTrace();  
	    }  
	    return outStr; 
	}
	
	public static String bytetoString(byte[] digest) {  
	    String str = "";  
	    String tempStr = "";  
	    for (int i = 0; i < digest.length; i++) {   
		    tempStr = (Integer.toHexString(digest[i] & 0xff));   
		    if (tempStr.length() == 1) {    
		    	str = str + "0" + tempStr;   
		    } 
		    else {    
		    	str = str + tempStr;   
		    }  
	    }  
	    return str.toLowerCase();
	}
	 // 测试主函数  
    public static void main(String args[]) {  
        String s = new String("999999");  
        System.out.println("原始：" + s);  
        System.out.println("加密的：" + MD5Encrypt(s).toUpperCase());  
        System.out.println("解密的：" + bytetoString(s.getBytes()));  
    }  
	
}
