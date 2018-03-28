package com.schrodinger.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.schrodinger.basic.BasicService;

import net.sf.json.JSONArray;

/** 
 * @author Bronya
 * @version 创建时间：2016-11-10
 */
@Service
public class SrSyssetService extends BasicService{

	public JSONArray queryLookupList(Map<String, String> params) throws Exception {
		return dao.queryPageList("sys_lookup", params);
	}
	
	public JSONArray queryLookupChilds(Map<String, String> params) throws Exception {
		return dao.queryPageList("sys_lookup_tl", params);
	}
	
	@Override
	public int delete(Map<String, String> params) throws Exception {

		String t = params.get("t");
		if(t != null){
			params.put("table", "sys_lookup_tl");
		}else{
			params.put("table", "sys_lookup");
			String[] ids = params.get("ids").split(",");
			StringBuffer sql = new StringBuffer("select * from sys_lookup_tl where lookup_id in (");
			for(String id : ids){
				sql.append("'"+id+"',");
			}
			sql.setLength(sql.length()-1);
			sql.append(")");
			params.put("child", sql.toString());
		}
		return super.delete(params);
	}
	
}
