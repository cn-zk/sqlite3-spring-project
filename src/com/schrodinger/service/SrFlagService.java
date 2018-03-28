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
public class SrFlagService extends BasicService{

	public JSONArray queryUserFlag(Map<String, String> params) throws Exception {
		return dao.queryPageList("sys_flag", params);
	}

	public JSONArray queryFlagMin(Map<String, String> params) throws Exception {
		String sql = "select f.*, s.name, s.entry, s.quit from sys_flag f left join sys_user s on s.id = f.user_id";
		return dao.queryPageList(sql, params);
	}

}
