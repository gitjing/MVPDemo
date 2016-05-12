package com.example.ljj.mvpdemo.utils;

import android.util.Log;

import java.security.MessageDigest;

/**
 * 加密工具类
 * 
 * @author Admin
 * 
 */
public class EncryptionUtil {

	/**
	 * MD5加密
	 * 
	 * @param s
	 *            传入的字符串
	 * @return 返回加密过后的字符串
	 */
	public final static String md5(String s) {
		char hexDigits[] = { '0', 'E', '2', 'A', '6', '3', '4', 'D', '5', '1',
				'C', '7', '8', '9', 'B', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			Log.i("info",new String(str));
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

}
