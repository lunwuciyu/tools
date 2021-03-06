package util;

import datetime.DateTimeUtil;
import string.StringUtil;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * 验证输入是否合法
 * <p></p>
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2011-11-29
 * @modify by reason:{方法名}:{原因}
 */
public class Verify {
	private final static String invalidChars = "\"%&'*,/;<>?[\\]^`“”-";
	
	/**
	 * 字母，数字与下划线
	 * @param str
	 * @return
	 */
	public static boolean isName(String str){
		return str.matches("\\w*");
	}
	
	/**
	 * null或空字符串
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str){
		return str == null || str.isEmpty();
	}
	
	public static boolean isMobilePhone(String str){
		return str.matches("^1\\d{10}$");
	}
	
	public static boolean isBranchTelephoneNum(String str){
		return str.matches("^\\d{4}$");
	}
	
	public static boolean isEmail(String str){
		if(StringUtil.isNullOrEmpty(str)){
			return false;
		}
		
		Pattern emailer = Pattern.compile("^(\\w+)([\\-+\\.][\\w]+)*@(\\w[\\-\\w]*\\.){1,5}([A-Za-z]){2,6}$");
		java.util.regex.Matcher matcher = emailer.matcher(str);
		return matcher.find();
		//return str.matches("/^[a-zA-Z0-9_\\.]+@[a-zA-Z0-9-]+[\\.a-zA-Z]+$/");
	}
	
	/**
	 * 是否为端口
	 * @param str
	 * @return
	 */
	public static boolean isPort(String str){
		try{
			int i=Integer.parseInt(str);
			return i>0&&i<=65535;
		}catch(Exception ex){
			
		}
		return false;
	}
	
	/**
	 * 中文，单词，空格.-
	 * @param str
	 * @return
	 */
	public static boolean isDeviceName(String str){
		for(int i=0;i<str.length();i++){
			if("\"%&'*,/;<>?[\\]^`“”".indexOf(str.charAt(i))!=-1){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 中文，单词，空格.-
	 * @return
	 */
	public static boolean isUserName(String str){
		for(int i=0;i<str.length();i++){
			if(invalidChars.indexOf(str.charAt(i))!=-1){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Ip地址
	 * @param str
	 * @return
	 */
	public static boolean isIpAddr(String str){
		try{
			int length = str.length();
			if(str.charAt(0) == '.' || str.charAt(length-1) == '.'){
				return false;
			}
			String[] array = str.split("\\.");
			if(array.length != 4) return false;
			for(String s: array){
				int i = Integer.parseInt(s);
				if(i<0||i>255) return false;
			}
			return true;
		}catch(Exception ex){
		}
		return false;
	}
	
	/**
	 * Ip地址
	 * @param str
	 * @return
	 */
	public static boolean isFirstNotZeroIpAddr(String str){
		boolean isIp = false;
		isIp = isIpAddr(str);
		if(isIp){
			if(Integer.parseInt(str.split("\\.")[0])==0){
				isIp = false;
			}
		}
		return isIp;
	}
	
	/**
	 * 长度
	 * @param str
	 * @param length
	 * @return
	 */
	public static boolean isLength(String str, int length){
		str = str.replaceAll("[^\\x00-\\xff]", "**"); // 将非标准字符替换成两个标准字符,如中文字符
		return str.length()<=length;
	}
	
	/**
	 * 最大值
	 * @param Int
	 * @param value
	 * @return
	 */
	public static boolean isMax(Integer Int, int value){
		return Int.intValue()<=value;
	}
	
	/**
	 * 最小值
	 * @param Int
	 * @param value
	 * @return
	 */
	public static boolean isLessZero(Integer Int){
		return Int.intValue() >= 0;
	}
	
	/**
	 * 编号
	 * @param str
	 * @return
	 */
	public static boolean isIndexCode(String str){
		return str.matches("[0-9a-zA-Z_]*");
	}
	
	/**
	 * 验证序列号 与前台验证一致 中文，单词，空格.-
	 * 可以为空或""
	 * @param str
	 * @return
	 */
	public static boolean isSerialNo(String str){
		String serialNoInvalidChars = "\"%&'*,/;<>?[\\]^`“”+()$#@\\!=|@{}";
		if (StringUtil.isNotNullOrEmpty(str)) {
			for(int i=0;i<str.length();i++){
				if(serialNoInvalidChars.indexOf(str.charAt(i))!=-1){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 判断日期格式： 2099-10-10
	 * @param str
	 * @param length
	 * @return
	 */
	public static boolean isDate(Date date, String dateString){
		String expireTimeString = DateTimeUtil.getFormatDateTime(date,
				DateTimeUtil.DATA_FORMAT_yyyy_MM_dd);
		String eL = "[0-9]{4}-[0-9]{2}-[0-9]{2}";		
		Pattern p = Pattern.compile(eL);	 
		String invalidChars = "\"%&'*,/;<>?[\\]^`“”_"; // 非法字符
		// 日期格式开头与结尾不能为中划线
		if("-".indexOf(dateString.charAt(0))!=-1 || "-".indexOf(dateString.charAt(dateString.length()-1))!=-1){
			return false;
		}
		int num = 0; //统计中划线的个数
		for(int i=0;i<dateString.length();i++){
			if(invalidChars.indexOf(dateString.charAt(i))!=-1){
				return false;
			}
			if("-".indexOf(dateString.charAt(i))!=-1){
				num++;
			}
		}
		if(num > 2){  //日期格式如果多于两个中划线，则判定非法
			return false;
		}
		java.util.regex.Matcher m = p.matcher(expireTimeString);		
		boolean dateFlag = m.matches();
		return dateFlag;
	}
}