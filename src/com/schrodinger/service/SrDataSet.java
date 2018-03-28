package com.schrodinger.service;

import org.springframework.stereotype.Service;

import com.schrodinger.basic.BasicService;

import net.sf.json.JSONObject;

/** 
 * @author Bronya
 * @version 创建时间：2016-11-10
 */
@Service
public class SrDataSet extends BasicService{

	public String getBackTimeStr(){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select s.id, s.back_time, s.back_type, t.lookup_name\n");
		sbSql.append("  from sys_set s\n");
		sbSql.append("       left join\n");
		sbSql.append("       (\n");
		sbSql.append("           select lookup_code,\n");
		sbSql.append("                  lookup_name\n");
		sbSql.append("             from sys_lookup l\n");
		sbSql.append("                  left join\n");
		sbSql.append("                  sys_lookup_tl t on t.lookup_id = l.id\n");
		sbSql.append("            where lookup_type = '_DATA_BACK'\n");
		sbSql.append("       )\n");
		sbSql.append("       t on t.lookup_code = s.back_type\n");

		String ret = "NULL";
		try {
			JSONObject json = dao.queryObjectById("1", sbSql.toString());
			switch (json.getInt("back_type")) {
			case 1:
			case 2:
			case 3:
				ret = "每隔"+json.getString("back_time") +" "+ json.get("lookup_name");
				break;
			case 4:
				ret = json.get("lookup_name") +" "+ json.getString("back_time") +" 的11:59:59";
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
}
