package com.schrodinger.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.schrodinger.basic.BasicService;

/** 
 * @author Bronya
 * @version 创建时间：2016-11-10
 */
@Service
public class SrWorkhoursService extends BasicService{


	public Object queryLocalHours(Map<String, String> params)throws Exception  {
		String sql = "select u.name _name,s.* from sys_workhours s left join sys_user u on u.id = s.user_id";
		return dao.queryPageList(sql, params);
	}
	
	public Object queryLocalEvections(Map<String, String> params)throws Exception  {
		String sql = "select u.name _name ,s.* from sys_evections s left join sys_user u on u.id = s.user_id";
		return dao.queryPageList(sql, params);
	}
	
}
