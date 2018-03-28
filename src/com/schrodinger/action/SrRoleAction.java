package com.schrodinger.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.schrodinger.basic.BasicAction;
import com.schrodinger.service.SrRoleService;
import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("role")
public class SrRoleAction extends BasicAction{

	@Autowired
	SrRoleService service;
	
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response){
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		try {
			obj.put("rows", service.queryAccountList(params));
			obj.put("total", params.get("total"));
		} catch (Exception e) {
			obj.put("rows", new JSONArray());
			obj.put("total", 0);
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	@RequestMapping("/find")
	public void find(HttpServletRequest request, HttpServletResponse response){
		try {
			this.print(response, service.find("sys_role", request.getParameter("id")));
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
		}
	}
	
	@RequestMapping("/roleMenu")
	public void roleAccount(HttpServletRequest request, HttpServletResponse response){
		try {
			this.print(response, service.queryRoleMenu(request.getParameter("id")));
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
		}
	}
	
	@RequestMapping("/save")
	public void save(HttpServletRequest request, HttpServletResponse response){
		try {
			JSONObject obj = new JSONObject();
			try {
				obj.put("result", service.insertOrUpdate(request.getParameter("dtos"), "sys_role"));
				obj.put("flag", "success");
			} catch (Exception e) {
				SrUtils.printStackTrace(e);
				obj.put("flag", "error");
				obj.put("error", e.getMessage());
			}
			this.print(response, obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SrUtils.printStackTrace(e);
		}
	}
	
	@RequestMapping("/saveRoleMenu")
	public void saveRoleAccount(HttpServletRequest request, HttpServletResponse response){
		try {
			JSONObject obj = new JSONObject();
			String roleId = request.getParameter("id");
			String dtos = request.getParameter("dtos");
			try {
				obj.put("result", service.saveRoelAccount(roleId, dtos));
				obj.put("flag", "success");
			} catch (Exception e) {
				SrUtils.printStackTrace(e);
				obj.put("flag", "error");
				obj.put("error", e.getMessage());
			}
			this.print(response, obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SrUtils.printStackTrace(e);
		}
	}
	
	@RequestMapping("/dels")
	public void userDelete(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("result", service.delete(request.getParameter("ids"), "sys_role"));
			obj.put("flag", "success");
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			obj.put("flag", "error");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
}
