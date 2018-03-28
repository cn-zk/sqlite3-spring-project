package com.schrodinger.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.schrodinger.basic.BasicService;
import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONArray;

@Service
public class SrLogService extends BasicService{
	
	public JSONArray queryList(Map<String, String> params) throws Exception{
		return dao.queryPageList("select s.*,(select t.name from sys_account t where t.id=s.account_id) as _name from sys_log s", params);
	}

	public void logLogin(String text) {
		dao.log(SrUtils.TYPE_LOGIN, "login", text);
	}
	public void logLoginFail(String text, String id) {
		dao.log(SrUtils.TYPE_LOGIN, "login", text, id, SrUtils.LOG_FAIL);
	}
	
}
