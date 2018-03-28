package com.schrodinger.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.schrodinger.basic.BasicService;

@Service
public class SrRoleService extends BasicService{
	
	public Object queryAccountList(Map<String, String> params) throws Exception{
		return dao.queryPageList("select t.* from sys_role t", params);
	}
	
	public Object queryRoleMenu(String id) throws Exception{
		return dao.queryPageList("select t.* from role_menu t where role_id ='"+id+"'");
	}

	public void delete(String roleId) throws Exception {
		dao.delete("role_id", roleId, "role_menu");
	}

	public int saveRoelAccount(String roleId, String dtos) throws Exception {
		delete(roleId);
		int i = insertOrUpdate(dtos, "role_menu");
//		dao.log(SrUtils.TYPE_UPDATE, "role_menu", "#{\"update\":"+i+",\"id\":\""+roleId+"\"}");
		return i;
	}

}
