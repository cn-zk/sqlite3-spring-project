package com.schrodinger.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.schrodinger.basic.BasicService;

import net.sf.json.JSONArray;

/** 
 * @author Bronya
 * @version 创建时间：2016-11-11
 */
@Service
public class SrSysserverService extends BasicService{
	
	public JSONArray queryLookupComboData(Map<String, String> params) throws Exception {
		return dao.queryPageList(
				"select t.lookup_code as value, t.lookup_name as text, t1.lookup_type from sys_lookup_tl t left join sys_lookup t1 on t.lookup_id = t1.id", params);
	}

	public JSONArray queryData(Map<String, String> params) throws Exception {
		String sql = "select "+ params.get("column") + " from " + params.get("table");
		String where = params.get("where");
		if(where != null){
			sql += " where 1=1 "+ where;
			sql = sql.replace("#eq", "=");
		}
		return dao.queryPageList(sql.toString(), params);
	}
	
}
