package com.schrodinger.basic;

import java.util.Iterator;
import java.util.Map;

import com.schrodinger.dao.SQLiteDao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** 
 * @author Bronya
 * @version 创建时间：2016-10-27
 */
public class BasicService {

	protected SQLiteDao dao;
	
	public BasicService() {
		dao = SQLiteDao.newInstance();
	}
	
	public int delete(String ids, String table) throws Exception {
		return delete("id", ids, table);
	}
	
	public int delete(String field, String ids, String table) throws Exception {
		int i=0;
		for(String id : ids.split(",")){
			dao.delete(field, id, table);
			++i;
		}
		return i;
	}
	
	public int delete(Map<String,String> params) throws Exception {
		String ids = params.get("ids");
		String table = params.get("table");
		String child = params.get("child");
		if(child != null){
			if(dao.queryCount(child) > 0){
				throw new Exception("请先删除字表记录！");
			}
		}
		int i=0;
		for(String id : ids.split(",")){
			dao.delete(id, table);
			++i;
		}
		return i;
	}

	public int insertOrUpdate(Object obj, String table) throws Exception {
		JSONArray arr = null ;
		if(obj instanceof String){
			// is string.
			String str = (String) obj;
			if(str.length() < 1){
				return 0;
			}
			if(str.indexOf("[") == 0){
				arr = JSONArray.fromObject(str);
			}else{
				arr = new JSONArray();
				arr.add(JSONObject.fromObject(str));
			}
		}else{
			// is JSONArray
			arr = (JSONArray) obj;
		}
		
		if(arr.size() < 1){
			return 0;
		}
		
		if(arr.size() == 1){
			return dao.insertOrUpdate(table, arr.get(0));
		}else{
			return dao.insertOrUpdate(table, arr);
		}
	}
	
	
	public JSONObject find(String table, String id) throws Exception {
		return dao.find(table, id);
	}
	
	public JSONObject merge(JSONArray arr, String key , String val){
		JSONObject obj = new JSONObject();
		Iterator<?> i = arr.iterator();
		while(i.hasNext()){
			JSONObject o  = (JSONObject) i.next();
			obj.put(o.get(key), o.get(val));
		}
		return obj;
	}
	
}
