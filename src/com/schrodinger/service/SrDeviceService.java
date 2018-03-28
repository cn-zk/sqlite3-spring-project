package com.schrodinger.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.schrodinger.basic.BasicService;

import net.sf.json.JSONArray;

/** 
 * @author Bronya
 * @version 创建时间：2016-12-6
 */
@Service
public class SrDeviceService extends BasicService {

	public JSONArray queryHostList(Map<String, String> params) throws Exception {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT h.*,\n");
		sbSql.append("       u.name AS _name,\n");
		sbSql.append("       u.status AS _status\n");
		sbSql.append("  FROM sys_host h\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       sys_manage m ON m.host_id = h.id\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       sys_user u ON u.id = m.user_id"); 

		return dao.queryPageList(sbSql.toString(), params);
	}
	
	public JSONArray queryDisplayList(Map<String, String> params) throws Exception {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT d.*,\n");
		sbSql.append("       u.name AS _name,\n");
		sbSql.append("       u.status AS _status\n");
		sbSql.append("  FROM sys_display d\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       sys_manage m ON m.display_id = d.id\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       sys_user u ON u.id = m.user_id"); 
		
		return dao.queryPageList(sbSql.toString(), params);
	}

	public JSONArray queryNotebookList(Map<String, String> params) throws Exception {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT n.*,\n");
		sbSql.append("       u.name AS _name,\n");
		sbSql.append("       u.status AS _status\n");
		sbSql.append("  FROM sys_notebook n\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       sys_manage m ON m.book_id = n.id\n");
		sbSql.append("       LEFT JOIN\n");
		sbSql.append("       sys_user u ON u.id = m.user_id"); 
		
		return dao.queryPageList(sbSql.toString(), params);
	}
	
	public JSONArray queryManageList(Map<String, String> params) throws Exception {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select u.id as user_id,\n");
		sbSql.append("       u.name as _name,\n");
		sbSql.append("       u.login_name as _login_name,\n");
		sbSql.append("       u.entry as _entry,\n");
		sbSql.append("       u.is_del as _is_del,\n");
		sbSql.append("       u.status as _status,\n");
		sbSql.append("       m.id,\n");
		sbSql.append("       m.display_id,\n");
		sbSql.append("       d.display_no as _display_no,\n");
		sbSql.append("       m.host_id,\n");
		sbSql.append("       h.host_no as _host_no,\n");
		sbSql.append("       h.belong as _belongh,\n");
		sbSql.append("       h.memory as _memory,\n");
		sbSql.append("       h.disk as _disk,\n");
		sbSql.append("       m.book_id,\n");
		sbSql.append("       n.book_no as _book_no,\n");
		sbSql.append("       n.belong as _belongb,\n");
		sbSql.append("       n.memory as _memoryb,\n");
		sbSql.append("       n.disk as _diskb,\n");
		sbSql.append("       m.keyboard,\n");
		sbSql.append("       m.mouse,\n");
		sbSql.append("       m.package,\n");
		sbSql.append("       m.remark\n");
		sbSql.append("  from sys_user u\n");
		sbSql.append("       left join\n");
		sbSql.append("       sys_manage m on m.user_id = u.id\n");
		sbSql.append("       left join\n");
		sbSql.append("       sys_host h on h.id = m.host_id\n");
		sbSql.append("       left join\n");
		sbSql.append("       sys_display d on d.id = m.display_id\n");
		sbSql.append("       left join\n");
		sbSql.append("       sys_notebook n on n.id = m.book_id"); 
		return dao.queryPageList(sbSql.toString(), params);
	}

	public Object findHost(String id) throws Exception {
		return super.find("sys_host", id);
	}

	public Object findNotebook(String id) throws Exception {
		return super.find("sys_notebook", id);
	}

	public Object findManage(String id) throws Exception {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select u.id as user_id,\n");
		sbSql.append("       u.name as _name,\n");
		sbSql.append("       m.id,\n");
		sbSql.append("       m.display_id,\n");
		sbSql.append("       d.display_no as _display_no,\n");
		sbSql.append("       m.host_id,\n");
		sbSql.append("       h.host_no as _host_no,\n");
		sbSql.append("       m.book_id,\n");
		sbSql.append("       n.book_no as _book_no,\n");
		sbSql.append("       m.keyboard,\n");
		sbSql.append("       m.mouse,\n");
		sbSql.append("       m.package,\n");
		sbSql.append("       m.remark\n");
		sbSql.append("  from sys_user u\n");
		sbSql.append("       left join\n");
		sbSql.append("       sys_manage m on m.user_id = u.id\n");
		sbSql.append("       left join\n");
		sbSql.append("       sys_host h on h.id = m.host_id\n");
		sbSql.append("       left join\n");
		sbSql.append("       sys_display d on d.id = m.display_id\n");
		sbSql.append("       left join\n");
		sbSql.append("       sys_notebook n on n.id = m.book_id"); 
		return dao.queryObjectByField(sbSql.toString(), "user_id", id);
	}

}
