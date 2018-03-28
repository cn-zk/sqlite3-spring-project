package com.schrodinger.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.schrodinger.basic.BasicService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SrMenuService extends BasicService{
	
	public JSONArray queryMenuList(Map<String, String> params) throws Exception{
//		return dao.queryPageList("select t.*, (select text from sys_menu where id=t.pm_id) pname from sys_menu t order by pm_id, num asc", params);
		
		String rid = params.get("#role_id-eq");
		String sql = "select m.*, (select count(0) from sys_menu sm where sm.pm_id = r.menu_id) length, r.role_id from role_menu r left join sys_menu m on m.id = r.menu_id";
		if("A1".equals(rid) || "A2".equals(rid)){
			sql = "select m.*, (select count(0) from sys_menu sm where sm.pm_id = m.id) length, '"+rid+"' as role_id from sys_menu m order by num";
		}
		return dao.queryPageList(sql, params);
	}
	
	public Object queryMenuTree(Map<String, String> params) throws Exception{
		JSONArray list = queryMenuList(params);
		JSONArray ret = new JSONArray();
		
		for(Object o : list){
			JSONObject obj = (JSONObject) o;
			if(obj.getInt("length") > 0){
				params.put("#pm_id-eq", obj.getString("id"));
				obj.put("check", true);
				obj.put("children", queryMenuTree(params));
			}
			ret.add(obj);
		}
		
		return ret;
	}
	
	public Object queryMenuTree() throws Exception{
		JSONArray list = dao.queryPageList("select t.*, (select text from sys_menu where id=t.pm_id) pname from sys_menu t order by pm_id, num asc");
		
		JSONArray ret = new JSONArray();
		
		JSONObject root = list.getJSONObject(0);
		root.put("children", getChaild(list, "1"));
		ret.add(root);
		
		return ret;
	}
	
	private JSONArray getChaild(JSONArray arr, String id){
		JSONArray child = new JSONArray();
		for(Object obj : arr){
			
			JSONObject j = (JSONObject) obj;
			if(id.equals(j.getString("pm_id"))){
				j.put("children", getChaild(arr, j.getString("id")));
				child.add(j);
			}
			
		}
		
		return child;
	}

}
