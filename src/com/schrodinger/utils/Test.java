package com.schrodinger.utils;

import java.util.List;

import com.schrodinger.dao.SQLiteDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Test {

	public static void main(String[] args) throws Exception {
		// 与工时督查人数核对
		List<String> list = SrStream.ReaderArray(SrUtils.class.getResourceAsStream("1.txt"), "utf-8");
//				System.out.println(list);
		JSONArray arr = SQLiteDao.newInstance().queryPageList("select * from sys_user where status = '1'");
		for(Object obj : arr){
			JSONObject o = (JSONObject) obj;
			String name = (String) o.get("name");
			if(!SrUtils.contains(list, name)){
				System.out.println(name);
			}
		}
	}
}
