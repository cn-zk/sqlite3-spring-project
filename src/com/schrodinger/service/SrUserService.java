package com.schrodinger.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.schrodinger.basic.BasicService;
import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** 
 * @author Bronya
 * @version 创建时间：2016-11-10
 */
@Service
public class SrUserService extends BasicService{

	public int updateDel(Map<String,String> parameter) throws Exception {
		String ids = parameter.get("ids");
		String del = parameter.get("del");
		if(del == null){
			del = "1";
		}
		JSONArray arr = new JSONArray();
		for(String id : ids.split(",")){
			JSONObject obj = new JSONObject();
			obj.put("id", id);
			obj.put("is_del", del);
			arr.add(obj);
		}
		return insertOrUpdate(arr, "sys_user");
	}

	public JSONArray queryUserList(Map<String, String> params) throws Exception {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT t.*,\n");
		sbSql.append("       t1.name _group_name,\n");
		sbSql.append("       m.display_id,\n");
		sbSql.append("       m.host_id,\n");
		sbSql.append("       m.book_id,\n");
		sbSql.append("       n.belong as nbelong,\n");
		sbSql.append("       m.keyboard,\n");
		sbSql.append("       m.mouse,\n");
		sbSql.append("       m.package,\n");
		sbSql.append("       (\n");
		sbSql.append("           SELECT count(0) c\n");
		sbSql.append("             FROM sys_flag f\n");
		sbSql.append("            WHERE f.user_id = t.id AND\n");
		sbSql.append("                  strftime('%Y', DATE(month) ) = strftime('%Y', 'now', 'localtime')\n");
		sbSql.append("       )\n");
		sbSql.append("       flag\n");
		sbSql.append("  FROM sys_user t\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       sys_user t1 ON t.group_id = t1.id\n");
		sbSql.append("       left join\n");
		sbSql.append("       sys_manage m on m.user_id = t.id"); 
		sbSql.append("       left join\n");
		sbSql.append("       sys_notebook n on m.book_id = n.id"); 

		return dao.queryPageList(sbSql.toString(), params);
	}
	
	public JSONObject findUser(String id) throws Exception {
		return this.find("select t.*, t1.name _group_name from sys_user t left join sys_user t1 on t.group_id = t1.id", id);
	}

	public JSONArray findUserItem(String user_id) throws Exception {
		return dao.queryPageList("select item_code, count(month) as month from (select item_code, month from sys_workhours where user_id = '"+user_id+"' group by item_code, month) group by item_code");
	}

	public JSONArray findUserWorkTen(String user_id) throws Exception {
		return dao.queryPageList("SELECT sum(ot) ot,sum(leave) leave, max(ot) ot1,max(leave) leave1, count(c) c FROM ( SELECT sum(ot_days) ot,sum(leave_days) leave, 1 c FROM sys_workhours WHERE user_id = '"+user_id+"' AND strftime('%Y-%m', DATE(month ||'-01') ) <= strftime('%Y-%m', 'now', 'localtime', '-1 month') AND strftime('%Y-%m', DATE(month || '-01') ) >= strftime('%Y-%m', 'now', 'localtime', '-12 month') GROUP BY MONTH) GROUP BY C");
	}
	
	public JSONArray findUserWorkPat(String user_id) throws Exception {
		return dao.queryPageList("SELECT sum(ifnull(ot_days, 0)) ot,sum(ifnull(leave_days, 0)) leave FROM sys_workhours WHERE user_id = '"+user_id+"' AND strftime('%Y-%m', DATE(month ||'-01') ) = strftime('%Y-%m', 'now', 'localtime', '-1 month') GROUP BY USER_ID");
	}

	public JSONArray queryUserOtLeList(String user_id) throws Exception {
		return dao.queryPageList("SELECT sum(ifnull(ot_days, 0)) ot,sum(ifnull(leave_days, 0)) le, month FROM sys_workhours WHERE user_id = '"+user_id+"' AND strftime('%Y-%m', DATE(month ||'-01') ) <= strftime('%Y-%m', 'now', 'localtime', '-1 month') AND strftime('%Y-%m', DATE(month || '-01') ) >= strftime('%Y-%m', 'now', 'localtime', '-12 month') GROUP BY MONTH ORDER BY MONTH DESC");
	}

	/**
	 * 人员工作图
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public JSONArray findUserView(Map<String, String> map) throws Exception {
		
		String month = map.get("month");
		String group_id = map.get("group_id");
		String user_id = map.get("user_id");
		
		if(SrUtils.isEmpty(month)){
			month = "6";
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select t.id,t.name,\n");
		sql.append("       w.ot_days,\n");
		sql.append("       w.leave_days,\n");
		sql.append("       n.item_code\n");
		sql.append("  from sys_user t\n");
		sql.append("       left join\n");
		sql.append("       (\n");
		sql.append("           select sum(ot_days) ot_days,\n");
		sql.append("                  sum(leave_days) leave_days,\n");
		sql.append("                  user_id\n");
		sql.append("             from sys_workhours\n");
		sql.append("            where strftime('%Y-%m', date(month || '-01') ) >= strftime('%Y-%m', 'now', 'localtime', '-"+month+" month')\n");
		sql.append("            group by user_id\n");
		sql.append("       )\n");
		sql.append("       w on w.user_id = t.id\n");
		sql.append("       left join\n");
		sql.append("       (\n");
		sql.append("           select user_id,\n");
		sql.append("                  count(item_code) item_code\n");
		sql.append("             from (\n");
		sql.append("                      select user_id,\n");
		sql.append("                             item_code\n");
		sql.append("                        from sys_workhours\n");
		sql.append("                       where strftime('%Y-%m', date(month || '-01') ) >= strftime('%Y-%m', 'now', 'localtime', '-"+month+" month')\n");
		sql.append("                       group by user_id,\n");
		sql.append("                                item_code\n");
		sql.append("                  )\n");
		sql.append("            group by user_id\n");
		sql.append("       )\n");
		sql.append("       n on n.user_id = t.id\n");
		sql.append(" where t.status = '1' and\n");
		sql.append("       t.entry not null \n");
		if(!SrUtils.isEmpty(group_id)){
			sql.append("      and t.group_id='"+group_id+"'");
		}
		if(!SrUtils.isEmpty(user_id)){
			sql.append("      and t.id='"+user_id+"'");
		}
		sql.append(" order by ot_days desc "); 

		return dao.queryPageList(sql.toString());
	}
	
	/**
	 * 出差统计
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public JSONArray findUserRate(Map<String, String> map) throws Exception {
		
		String n = map.get("month");
		String group_id = map.get("group_id");
		String user_id = map.get("user_id");
		String is = map.get("is");
		
		if(SrUtils.isEmpty(n)){
			n = "3";
		}
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT u.id, u.name,\n");
		sbSql.append("       ifnull(t.rate, 0) as rate,\n");
		sbSql.append("       round(ifnull(t.ave, 0)) as ave,\n");
		sbSql.append("       round(ifnull(t.days, 0)) as days,\n");
		sbSql.append("		(\n");
		sbSql.append("    	SELECT count(0)\n");
		sbSql.append("      	FROM sys_evections\n");
		sbSql.append("     	WHERE user_id = u.id AND\n");
		sbSql.append("           strftime('%Y-%m', DATE(month || '-01') ) >= strftime('%Y-%m', 'now', 'localtime', '-1 month')\n");
		sbSql.append("		)\n");
		sbSql.append("		AS be"); 
		sbSql.append("  FROM sys_user u\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       (\n");
		sbSql.append("			select ev.user_id,\n");
		sbSql.append("                 sum(ev.days) / (count(0) * 31) * 100 AS rate,\n");
		sbSql.append("                 sum(ev.days) / "+n+" AS ave,\n");
		sbSql.append("                 max(ev.days) AS days\n");
		sbSql.append("            FROM (\n");
		sbSql.append("           SELECT user_id, month, sum(days) as days\n");
		sbSql.append("             FROM sys_evections\n");
		sbSql.append("            GROUP BY month, user_id\n");
		sbSql.append("           ) ev\n");
		sbSql.append("           WHERE strftime('%Y-%m', DATE(ev.month || '-01') ) >= strftime('%Y-%m', 'now', 'localtime', '-"+n+" month')\n");
		sbSql.append("           GROUP BY ev.user_id"); 
		sbSql.append("       )\n");
		sbSql.append("       t ON u.id = t.user_id\n");
		sbSql.append(" where u.status = '1' \n");
		if(!SrUtils.isEmpty(group_id)){
			sbSql.append("      AND u.group_id='"+group_id+"'");
		}
		if(!SrUtils.isEmpty(user_id)){
			sbSql.append("      AND u.id='"+user_id+"'");
		}
		if("1".equals(is)){
			sbSql.append("      AND ifnull(t.rate, 0)!=0");
		}
		sbSql.append(" ORDER BY t.rate desc"); 
		return dao.queryPageList(sbSql.toString());
	}
	
	/**
	 * 出差统计详细页
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public JSONArray findUserRateList(Map<String, String> map) throws Exception {

		String user_id = map.get("user_id");
		String n = map.get("month");
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT u.name,\n");
		sbSql.append("       ev.month,\n");
		sbSql.append("       sum(ev.days) as days,\n");
		sbSql.append("       GROUP_CONCAT(lp.lookup_name) as place\n");
		sbSql.append("  FROM (\n");
		sbSql.append("           SELECT month,\n");
		sbSql.append("                  user_id,\n");
		sbSql.append("                  place,\n");
		sbSql.append("                  sum(days) AS days\n");
		sbSql.append("             FROM sys_evections\n");
		sbSql.append("            GROUP BY month,\n");
		sbSql.append("                     user_id,\n");
		sbSql.append("                     place\n");
		sbSql.append("       )\n");
		sbSql.append("       ev\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       sys_user u ON u.id = ev.user_id\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       (\n");
		sbSql.append("           SELECT lookup_code,\n");
		sbSql.append("                  lookup_name\n");
		sbSql.append("             FROM sys_lookup l\n");
		sbSql.append("                  LEFT JOIN\n");
		sbSql.append("                  sys_lookup_tl t ON t.lookup_id = l.id\n");
		sbSql.append("            WHERE lookup_type = 'WH_CCDD'\n");
		sbSql.append("       )\n");
		sbSql.append("       lp ON lp.lookup_code = ev.place\n");
		sbSql.append(" WHERE ev.user_id = '"+user_id+"' AND\n");
		sbSql.append("       strftime('%Y-%m', DATE(ev.month || '-01') ) >= strftime('%Y-%m', 'now', 'localtime', '-"+n+" month')\n");
		sbSql.append(" GROUP BY ev.month,\n");
		sbSql.append("          ev.user_id"); 
		sbSql.append(" ORDER BY ev.month desc"); 

		return dao.queryPageList(sbSql.toString());
	}

	public JSONArray queryGroupView(Map<String, String> map) throws Exception {
		String year = map.get("year");
		if(SrUtils.isEmpty(year)){
			year = new SimpleDateFormat("YYYY").format(new Date());
		}
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT u.id,\n");
		sbSql.append("       u.name,\n");
		sbSql.append("       g.en,\n");
		sbSql.append("       g.out,\n");
		sbSql.append("       g.[in],\n");
		sbSql.append("       g.rot\n");
		sbSql.append("  FROM sys_user u\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       (\n");
		sbSql.append("           SELECT find_id,\n");
		sbSql.append("                  max(CASE type WHEN 'sys_user.group' THEN find_val ELSE 0 END) AS en,\n");
		sbSql.append("                  max(CASE type WHEN 'sys_user.group_out' THEN find_val ELSE 0 END) AS out,\n");
		sbSql.append("                  max(CASE type WHEN 'sys_user.group_in' THEN find_val ELSE 0 END) AS [in],\n");
		sbSql.append("                  max(CASE type WHEN 'sys_user.group_rout' THEN find_val ELSE 0 END) AS rot\n");
		sbSql.append("             FROM sys_snapshot\n");
		sbSql.append("            WHERE (type = 'sys_user.group' OR\n");
		sbSql.append("                   type = 'sys_user.group_out' OR\n");
		sbSql.append("                   type = 'sys_user.group_in' OR\n");
		sbSql.append("                   type = 'sys_user.group_rout') AND\n");
		sbSql.append("                  year = '"+year+"'\n");
		sbSql.append("            GROUP BY find_id\n");
		sbSql.append("       )\n");
		sbSql.append("       g ON g.find_id = u.id\n");
		sbSql.append(" WHERE u.is_group = '1' AND\n");
		sbSql.append("       (u.quit is null or strftime('%Y', u.quit) > '"+year+"') and g.en is not null"); 

		return dao.queryPageList(sbSql.toString());
	}

	public Object findUserEvec(String user_id) throws Exception {
		return dao.queryPageList("select l, count(0) c from (select t.lookup_name l, e.month from sys_evections e left join (select t.lookup_code, t.lookup_name from sys_lookup_tl t left join sys_lookup l on l.id = t.lookup_id where l.lookup_type = 'WH_CCDD') t on t.lookup_code = e.place where e.user_id = '"+user_id+"' group by t.lookup_name ,e.month) group by l");
	}

}
