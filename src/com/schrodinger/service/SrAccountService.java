package com.schrodinger.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.schrodinger.basic.BasicService;

import net.sf.json.JSONObject;

@Service
public class SrAccountService extends BasicService{
	
	public Object queryAccountList(Map<String, String> params) throws Exception{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select t.*,\n");
		sbSql.append("       (select t1.name from sys_role t1 where t1.id = t.pc_role_id) as pc_role_name,\n");
		sbSql.append("       (select t1.name from sys_role t1 where t1.id = t.pe_role_id) as pe_role_name\n");
		sbSql.append("  from sys_account t"); 

		return dao.queryPageList(sbSql.toString(), params);
	}

	public JSONObject loginAccount(String login, String pwd) throws Exception{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT a.*,\n");
		sbSql.append("       r.name AS role_name\n");
		sbSql.append("  FROM sys_account a\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       sys_role r ON r.id = a.pc_role_id\n");
		sbSql.append(" WHERE a.login = '"+login+"' and a.pwd='"+pwd+"'"); 

		JSONObject obj = dao.getJdbc().queryById(sbSql.toString());
		return obj;
	}

	public Object findAccount(String id) throws Exception {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT a.*,\n");
		sbSql.append("       r.name AS _pc_role_name,\n");
		sbSql.append("       r1.name AS _pe_role_name\n");
		sbSql.append("  FROM sys_account a\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       sys_role r ON r.id = a.pc_role_id\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       sys_role r1 ON r1.id = a.pe_role_id"); 

		return dao.find(sbSql.toString(), id);
	}

	public JSONObject findUser(String name) throws Exception {
		return dao.queryObjectByField("sys_user", "name", name);
	}
	
}
