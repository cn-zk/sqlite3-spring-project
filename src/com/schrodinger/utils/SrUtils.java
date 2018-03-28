package com.schrodinger.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.schrodinger.dao.SQLSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** 
 * @author Bronya
 * @version 创建时间：2016-10-27
 */
public class SrUtils {
	
	public static final int LOG_SUCCESS = 1;
	public static final int LOG_FAIL = 2;
	
	public static final int TYPE_LOGIN = 1;
	public static final int TYPE_INSERT = 2;
	public static final int TYPE_UPDATE = 3;
	public static final int TYPE_DELETE = 4;
	public static final String ID_AUTO = "auto";
	

	public static String[] FORMATE_SIZE = new String[]{"B", "KB", "MB", "GB", "TB" };
	
	// 一天
	public static long DAY = 86400000;
	
	/**
	 * 是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String... strs){
		int i=0;
		while(i<strs.length){
			if(isEmpty(strs[i++])){
				return true;
			}
		}
		return false;
	}
	public static boolean isEmpty(String str){
		return str == null || str.length() < 1 || "[null]".equals(str);
	}

	public static String getId() {
		return UUID.randomUUID().toString();
	}
	public static String splitBrowser(String s1){
		return s1.substring(s1.indexOf("(")+1, s1.indexOf(")"));
	}
	public static String currentTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	public static String currentDate() {
		return currentDate("yyyy-MM-dd");
	}
	public static String currentDate(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}
	public static HttpSession getSession() { 
		HttpSession session = null; 
		try { 
		    session = getRequest().getSession(); 
		} catch (Exception e) {} 
		    return session; 
		} 
		
	public static HttpServletRequest getRequest() { 
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder 
		.getRequestAttributes(); 
		return attrs.getRequest(); 
	}
	public static boolean emptyId() {
		return isEmpty((String)getSession().getAttribute("id"));
	}
	public static void printStackTrace(Exception e) {
		e.printStackTrace();
//		System.err.println(e.getMessage());
	} 
	
	public static boolean contains(List<String> list ,String s){
		Iterator<String> iter = list.iterator();
		while(iter.hasNext()){
			if(s.equals(iter.next())){
				return true;
			}
		}
		return false;
	}
	public static boolean contains(String[] list ,String s){
		for(String str : list){
			if(s.equals(str)){
				return true;
			}
		}
		return false;
	}
	
	public static String getBackPath() {
		return getBackPath(null);
	}
	public static String getBackPath(String year) {
		if(year == null){
			year = SrUtils.currentDate("yyyy");
		}
		return new File(SQLSet.get("path")).getParent()+File.separator+"back"+File.separator+year;
	}
	
	/**
	 * 自动备份
	 * @throws Exception
	 */
	public static void autoBack() throws Exception{
		String fname=SQLSet.get("dbname")+"_"+SrUtils.currentDate("MMdd")+" (自动).db";
		String newf = SrUtils.getBackPath()+File.separator+fname;
		if(new File(newf).exists()){
			return;
		}
		SrStream.ByteCopy(SQLSet.get("path"), newf);
	}
	
	/**
	 * 将File.length处理成带有容量单位的字符串 (字节~TB)
	 * 
	 * @param length	容量大小
	 * @param residue 	保留小数点后的位数
	 * @return
	 */
	public static String toCapacity(double length, int residue){
		return toCapacity(length, 0, residue);
	}
	/**
	 * 内部运算方法
	 * @param size			大小
	 * @param formatIndex	格式化下表
	 * @param residue		小数点
	 * @return
	 */
	private static String toCapacity(double size, int formatIndex, int residue){
		String ret = null;
		if(size > 1024 && formatIndex < FORMATE_SIZE.length){
			ret = toCapacity(size / 1024d, ++formatIndex, residue);
		}else{
			String l = String.valueOf(size);
			int ind = l.indexOf(".");
			if(residue <= 0)
				residue --;
			else
				ind += (residue + 1);
			if(ind < l.length()){
				l = l.substring(0, ind);
			}
			ret = l + " " + FORMATE_SIZE[formatIndex]; 
		}
		return ret ; 
	}
	
	public static String formatTime(long ms) {
		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = ms / dd;
		long hour = (ms - day * dd) / hh;
		long minute = (ms - day * dd - hour * hh) / mi;
		long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

		String strDay = day < 10 ? "0" + day : "" + day; // 天
		String strHour = hour < 10 ? "0" + hour : "" + hour;// 小时
		String strMinute = minute < 10 ? "0" + minute : "" + minute;// 分钟
		String strSecond = second < 10 ? "0" + second : "" + second;// 秒
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;// 毫秒
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

		return strDay + "天" + strHour + "时" + strMinute + "分" + strSecond + "秒";
	}
	
	public static int differenceWeek(int week) {
		Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK)-2;
        if (w < 0)
            w = 0;
        if(w >= week){
        	return 7 - (w - week);
        }
        return week - w;
	}
	
	public static int getWeek(){
		Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 2;
		return w;
	}
	
	/**
	 * 
	 * @param arr
	 * @param fd
	 * @param object
	 * @return
	 */
	public static JSONObject getListObjec(JSONArray arr, String fd, Object object){
		Iterator<?> iter = arr.iterator();
		String str = String.valueOf(object);
		while(iter.hasNext()){
			JSONObject obj = (JSONObject) iter.next();
			if(str.equals(obj.get(fd))){
				return obj;
			}
		}
		return null;
	}
	
	public static String getRemoteHost(javax.servlet.http.HttpServletRequest request){
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
	
}
