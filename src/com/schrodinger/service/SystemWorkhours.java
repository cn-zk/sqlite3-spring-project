package com.schrodinger.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.schrodinger.basic.BasicService;
import com.schrodinger.dao.SQLSet;
import com.schrodinger.dao.SystemDatabase;
import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** 
 * @author Bronya
 * @version 创建时间：2016-10-26
 */
@Service
public class SystemWorkhours extends BasicService{

	/**
	 * 获取退回记录
	 * 
	 * @param id		用户ID
	 * @param month		年月
	 * @throws Exception
	 */
	public JSONArray queryReturn(Map<String, String> params) throws Exception{
		String month = params.get("month");
		String id = params.get("id");
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select t.submitter_name,\n");
		sbSql.append("       t.item_code,\n");
		sbSql.append("       t.item_manager,\n");
		sbSql.append("       t.work_detail,\n");
		sbSql.append("       t.return_reason,\n");
		sbSql.append("       t.retuen_date,\n");
		sbSql.append("       t.state\n");
		sbSql.append("  from wh_workhours_info t\n");
		sbSql.append(" where 1=1 ");
		sbSql.append("   and state = '4'\n");
		if(month != null){
			sbSql.append("   and to_char(t.begin_date, 'yyyy-MM') = '"+month+"'\n");
		}
		sbSql.append("   and submitter_code like '"+SQLSet.get("company")+"-%'\n");
		if(id != null){
			sbSql.append("   and t.submitter_id = '"+id+"'"); 
		}
		return SystemDatabase.newInstance().queryPage(sbSql.toString(), params);
	}
	
	/**
	 * 获取标准工时不对的记录
	 * 
	 * @param month 年月
	 * @param day	标准工时
	 * @throws Exception
	 */
	public JSONArray queryStndDays(Map<String, String> params) throws Exception{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select *\n");
		sbSql.append("  from (select t.submitter_id, t.submitter_name, sum(t.stnd_days) stnd_days\n");
		sbSql.append("          from wh_workhours_info t\n");
		sbSql.append("         where 1=1");
		sbSql.append("   and to_char(t.begin_date, 'yyyy-MM') = '#month'\n");
		sbSql.append("   and submitter_code like '#cp-%'\n");
		sbSql.append("         group by t.submitter_id,t.submitter_name)\n");
		sbSql.append(" where stnd_days <> #day"); 
		JSONArray arr = SystemDatabase.newInstance().queryPage(sbSql.toString(), params);
		
		if(arr.size() > 0){
			Iterator<?> iter = arr.iterator();
			StringBuffer ids = new StringBuffer();
			while(iter.hasNext()){
				JSONObject obj = (JSONObject) iter.next();
				ids.append("'"+obj.get("submitter_id")+"',");
			}
			ids.setLength(ids.length() -1);
			
			JSONArray us = dao.queryPageList("(select * from sys_user where user_id in ("+ids+"))");
			
			String ym = new SimpleDateFormat("yyyy-MM").format(new Date());
			
			iter = arr.iterator();
			while(iter.hasNext()){
				JSONObject obj = (JSONObject) iter.next();
				JSONObject user = SrUtils.getListObjec(us, "user_id", obj.get("submitter_id"));
				if(user != null){
					if(String.valueOf(user.get("entry")).indexOf(ym) == 0){
						obj.put("entry", user.get("entry"));
					}
				}
			}
		}
		return arr;
	}

	public JSONArray querySurplus(Map<String, String> params) throws Exception {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select\n");
		sbSql.append("    submitter_name,\n");
		sbSql.append("    item_code,\n");
		sbSql.append("    item_manager,\n");
		sbSql.append("    work_detail,\n");
		sbSql.append("    state\n");
		sbSql.append("  from wh_workhours_info\n");
		sbSql.append(" where state <> '3'\n");
		sbSql.append("   and to_char(begin_date, 'yyyy-MM') = '#month'\n");
		sbSql.append("   and submitter_name like '%(#cp)'"); 
		sbSql.append(" order by submitter_name desc"); 
		JSONArray arr = SystemDatabase.newInstance().queryPage(sbSql.toString(), params);
		
		return arr;
	}
	
	public JSONArray queryGroup(Map<String, String> params) throws Exception{

		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select u.name\n");
		sbSql.append("  from sys_user_group g\n");
		sbSql.append("  left join sys_user u\n");
		sbSql.append("    on u.id = g.user_id\n");
		sbSql.append(" where g.group_id = '#group_id'\n");
		sbSql.append("   and u.status = '1'"); 

		JSONArray list = SystemDatabase.newInstance().queryPage(sbSql.toString(), params);
		JSONArray arr = dao.queryPageList("select * from sys_user where status = '1'");
		JSONArray ret = new JSONArray();
		for(Object obj : arr){
			JSONObject o = (JSONObject) obj;
			String name = (String) o.get("name");
			if(!container(list, name)){
				ret.add(o);
			}
		}
		
		return ret;
	}
	
	
	private boolean container(JSONArray list, String s){
		Iterator<?> iter = list.iterator();
		while(iter.hasNext()){
			JSONObject o = (JSONObject) iter.next();
			String name = (String) o.get("name");
			if(s.equals(name)){
				return true;
			}
		}
		return false;
	}

}
