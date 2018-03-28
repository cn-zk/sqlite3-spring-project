package com.schrodinger.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.schrodinger.basic.BasicService;
import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONObject;

/** 
 * @author Bronya
 * @version 创建时间：2016-12-6
 */
@Service
public class SrReportService extends BasicService {

	public JSONObject queryGroupMonth(Map<String, String> params) throws Exception {
		
		String year = params.get("year");
		if(SrUtils.isEmpty(year)){
			year = new SimpleDateFormat("YYYY").format(new Date());
		}
		
		JSONObject obj = new JSONObject();
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT find_name AS m,\n");
		sbSql.append("       find_val AS c\n");
		sbSql.append("  FROM sys_snapshot\n");
		sbSql.append(" WHERE type = 'sys_user.month_entry' AND\n");
		sbSql.append("       year = '"+year+"'"); 

		obj.put("entry", merge(dao.queryPageList(sbSql.toString()), "m", "c"));
		
		sbSql = new StringBuffer();
		sbSql.append("SELECT find_name AS m,\n");
		sbSql.append("       find_val AS c\n");
		sbSql.append("  FROM sys_snapshot\n");
		sbSql.append(" WHERE type = 'sys_user.month_quit' AND\n");
		sbSql.append("       year = '"+year+"'"); 
		
		obj.put("quit", merge(dao.queryPageList(sbSql.toString()), "m", "c"));
		return obj;
	}
	
	public Object queryGroupSkill(Map<String, String> params) throws Exception {
		String year = params.get("year");
		if(SrUtils.isEmpty(year)){
			year = new SimpleDateFormat("YYYY").format(new Date());
		}
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT lookup_name AS name,\n");
		sbSql.append("       ifnull(s.find_val, 0) AS val\n");
		sbSql.append("  FROM sys_lookup_tl l\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       (\n");
		sbSql.append("           SELECT find_id,\n");
		sbSql.append("                  find_val\n");
		sbSql.append("             FROM sys_snapshot\n");
		sbSql.append("            WHERE type = 'sys_user.skill' AND\n");
		sbSql.append("                  year = '"+year+"'\n");
		sbSql.append("       )\n");
		sbSql.append("       s ON s.find_id = l.lookup_code\n");
		sbSql.append(" WHERE lookup_id = '7d38415c-712a-4dd1-a695-37619f5adfa2'"); 


		return dao.queryPageList(sbSql.toString());
	}
	
	public Object queryGroupLevel(Map<String, String> params) throws Exception {
		String year = params.get("year");
		if(SrUtils.isEmpty(year)){
			year = new SimpleDateFormat("YYYY").format(new Date());
		}
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT lookup_name AS name,\n");
		sbSql.append("       ifnull(s.find_val, 0) AS val\n");
		sbSql.append("  FROM sys_lookup_tl l\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       (\n");
		sbSql.append("           SELECT find_id,\n");
		sbSql.append("                  find_val\n");
		sbSql.append("             FROM sys_snapshot\n");
		sbSql.append("            WHERE type = 'sys_user.level' AND\n");
		sbSql.append("                  year = '"+year+"'\n");
		sbSql.append("       )\n");
		sbSql.append("       s ON s.find_id = l.lookup_code\n");
		sbSql.append(" WHERE lookup_id = '2b1b17d0-3c92-4c52-ae99-fc7ecee0b6d6'"); 

		
		return dao.queryPageList(sbSql.toString());
	}

	public Object queryGroupYearUser(Map<String, String> params) throws Exception {
		String year = params.get("year");
		if(SrUtils.isEmpty(year)){
			year = new SimpleDateFormat("YYYY").format(new Date());
		}
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT find_name AS name,\n");
		sbSql.append("       find_val AS val\n");
		sbSql.append("  FROM sys_snapshot\n");
		sbSql.append(" WHERE type = 'sys_user.entry_year' AND\n");
		sbSql.append("       year = '"+year+"'"); 

		return dao.queryPageList(sbSql.toString());
	}
	
	public Object queryGroupUser(Map<String, String> params) throws Exception {
		
		String year = params.get("year");
		if(SrUtils.isEmpty(year)){
			year = new SimpleDateFormat("YYYY").format(new Date());
		}
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT find_name AS name,\n");
		sbSql.append("       find_val AS val\n");
		sbSql.append("  FROM sys_snapshot\n");
		sbSql.append(" WHERE type = 'sys_user.user' AND\n");
		sbSql.append("       year = '"+year+"'"); 
 
		return dao.queryPageList(sbSql.toString());
	}
	
	public Object queryGroupPlace(Map<String, String> params) throws Exception {
		
		String year = params.get("year");
		if(SrUtils.isEmpty(year)){
			year = new SimpleDateFormat("YYYY").format(new Date());
		}
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT find_name AS name,\n");
		sbSql.append("       find_val AS val,\n");
		sbSql.append("       find_id AS id\n");
		sbSql.append("  FROM sys_snapshot\n");
		sbSql.append(" WHERE type = 'sys_evections.place_user' AND\n");
		sbSql.append("       year = '"+year+"' order by find_val+0 desc"); 

		return dao.queryPageList(sbSql.toString());
	}
	
	public Object queryGroupSex(Map<String, String> params) throws Exception {
		
		String year = params.get("year");
		if(SrUtils.isEmpty(year)){
			year = new SimpleDateFormat("YYYY").format(new Date());
		}
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT find_name AS name,\n");
		sbSql.append("       find_val AS val,\n");
		sbSql.append("       find_id AS id\n");
		sbSql.append("  FROM sys_snapshot\n");
		sbSql.append(" WHERE (type = 'sys_user.eve' or type = 'sys_user.sex') AND\n");
		sbSql.append("       year = '"+year+"' order by type desc"); 
		
		return dao.queryPageList(sbSql.toString());
	}
	
	public Object queryMonthPlace(Map<String, String> params) throws Exception {
		
		String year = params.get("year");
		String id = params.get("id");
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT find_id AS name,\n");
		sbSql.append("       find_val AS val,\n");
		sbSql.append("       find_name AS tit\n");
		sbSql.append("  FROM sys_snapshot\n");
		sbSql.append(" WHERE year = '"+year+"' AND\n");
		sbSql.append("       type = 'sys_evections.mp' AND\n");
		sbSql.append("       find_name = (\n");
		sbSql.append("                       SELECT t.lookup_name\n");
		sbSql.append("                         FROM sys_lookup_tl t\n");
		sbSql.append("                              LEFT JOIN\n");
		sbSql.append("                              sys_lookup l ON l.id = t.lookup_id\n");
		sbSql.append("                        WHERE t.lookup_code = '"+id+"' AND\n");
		sbSql.append("                              l.lookup_type = 'WH_CCDD'\n");
		sbSql.append("                   )"); 

		return dao.queryPageList(sbSql.toString());
	}

	public Object queryGroupQuit(Map<String, String> params) throws Exception {

		String year = params.get("year");
		if(SrUtils.isEmpty(year)){
			year = new SimpleDateFormat("YYYY").format(new Date());
		}
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT find_name AS name,\n");
		sbSql.append("       find_val AS val\n");
		sbSql.append("  FROM sys_snapshot\n");
		sbSql.append(" WHERE type = 'sys_user.quit_type' AND\n");
		sbSql.append("       year = '"+year+"' order by find_val+0 desc"); 

		return dao.queryPageList(sbSql.toString());
	}

	public Object queryGroupHost(Map<String, String> params) throws Exception {
		String year = params.get("year");
		if(SrUtils.isEmpty(year)){
			year = new SimpleDateFormat("YYYY").format(new Date());
		}
		
		JSONObject obj = new JSONObject();
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT find_name AS name,\n");
		sbSql.append("       find_val AS val\n");
		sbSql.append("  FROM sys_snapshot\n");
		sbSql.append(" WHERE type = 'device.host_en' and year = '"+year+"'\n");
		sbSql.append(" order by val+0 asc"); 
		obj.put("en", dao.queryPageList(sbSql.toString()));
		
		sbSql = new StringBuffer();
		sbSql.append("SELECT find_name AS name,\n");
		sbSql.append("       find_val AS val\n");
		sbSql.append("  FROM sys_snapshot\n");
		sbSql.append(" WHERE type = 'device.host_us' and year = '"+year+"'\n");
		sbSql.append(" order by val+0 asc"); 
		obj.put("us", dao.queryPageList(sbSql.toString()));
		return obj;
	}
	
	public Object queryGroupBook(Map<String, String> params) throws Exception {
		String year = params.get("year");
		if(SrUtils.isEmpty(year)){
			year = new SimpleDateFormat("YYYY").format(new Date());
		}
		
		JSONObject obj = new JSONObject();
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT find_name AS name,\n");
		sbSql.append("       find_val AS val\n");
		sbSql.append("  FROM sys_snapshot\n");
		sbSql.append(" WHERE type = 'device.book_en' and year = '"+year+"'\n");
		sbSql.append(" order by val+0 asc"); 
		obj.put("en", dao.queryPageList(sbSql.toString()));
		
		sbSql = new StringBuffer();
		sbSql.append("SELECT find_name AS name,\n");
		sbSql.append("       find_val AS val\n");
		sbSql.append("  FROM sys_snapshot\n");
		sbSql.append(" WHERE type = 'device.book_us' and year = '"+year+"'\n");
		sbSql.append(" order by val+0 asc"); 
		obj.put("us", dao.queryPageList(sbSql.toString()));
		return obj;
	}

}
