package com.waitingmyself.common.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author 李旭光
 * @version 2009-8-11
 */
public class EncryptUtil {
	
	public static char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 * 对输入字符串进行SHA1加密
	 * 
	 * @param str
	 *            输入串
	 * @return 加密后的字符串
	 */
	public static String getSHA1Value(String str) {
		StringBuffer strResult = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());
			byte[] encryptedBytes = md.digest();

			String stmp;
			for (int n = 0; n < encryptedBytes.length; n++) {
				stmp = (Integer.toHexString(encryptedBytes[n] & 0XFF));
				if (stmp.length() == 1) {
					strResult.append("0");
					strResult.append(stmp);
				} else {
					strResult.append(stmp);
				}
			}
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strResult.toString();
	}

	/**
	 * 对输入字符串进行MD5加密
	 * 
	 * @param str
	 *            输入串
	 * @return 加密后的字符串
	 */
	public static String getMD5Value(String str) {
		StringBuffer strResult = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] encryptedBytes = md.digest();

			String stmp;
			for (int n = 0; n < encryptedBytes.length; n++) {
				stmp = (Integer.toHexString(encryptedBytes[n] & 0XFF));
				if (stmp.length() == 1) {
					strResult.append("0");
					strResult.append(stmp);
				} else {
					strResult.append(stmp);
				}
			}
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strResult.toString();
	}

	/**
	 *  文件的摘要加密
	 *  2009-05-06
	 * @param path
	 * @return
	 */
	public static String getMD5ValueFromFile(String path) {
		StringBuffer result = new StringBuffer("");
		try {
			InputStream fis;
			fis = new FileInputStream(path);
			byte[] buffer = new byte[1024];
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			byte b[] = md5.digest();
			for (int i = 0; i < b.length; i++) {
				result.append(HEX_CHAR[(b[i] & 0xf0) >>> 4]);
				result.append(HEX_CHAR[b[i] & 0x0f]);
			}
		} catch (Exception e) {
			return null;
		}
		return result.toString();
	}
	
	/**
	 *  文件的摘要加密
	 *  2009-05-06
	 * @param path
	 * @return
	 */
	public static String getSHA1ValueFromFile(String path) {
		StringBuffer result = new StringBuffer("");
		try {
			InputStream fis;
			fis = new FileInputStream(path);
			byte[] buffer = new byte[1024];
			MessageDigest md5 = MessageDigest.getInstance("SHA-1");
			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			byte b[] = md5.digest();
			for (int i = 0; i < b.length; i++) {
				result.append(HEX_CHAR[(b[i] & 0xf0) >>> 4]);
				result.append(HEX_CHAR[b[i] & 0x0f]);
			}
		} catch (Exception e) {
			return null;
		}
		return result.toString();
	}
}
