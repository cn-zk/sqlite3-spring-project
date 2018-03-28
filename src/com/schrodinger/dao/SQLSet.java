package com.schrodinger.dao;

import java.io.IOException;
import java.util.Properties;

public class SQLSet {
	
	private static Properties SYS ;
	
	static{
		SYS = new Properties();
		try {
			SYS.load(SQLiteJDBC.class.getResourceAsStream("/dbset.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String get(String key){
		return SYS.getProperty(key);
	}

	public static Object getString() {
		StringBuffer buf = new StringBuffer("<table>");
		for(Object key : SYS.keySet()){
			buf.append("<tr>");
			buf.append("<th width=\"100\">");
			buf.append(key);
			buf.append(":");
			buf.append("</th>");
			buf.append("<td>");
			buf.append(SYS.getProperty((String) key));
			buf.append("</td>");
			buf.append("</tr>");
		}
		buf.append("</table>");
		return buf.toString();
	}
	
}
