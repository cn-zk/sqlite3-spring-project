package com.schrodinger.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.schrodinger.basic.BasicAction;
import com.schrodinger.service.SrMenuService;
import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("menu")
public class SrMenuAction extends BasicAction{

	@Autowired
	SrMenuService service;
	

	@RequestMapping("/tree")
	public void tree(HttpServletRequest request, HttpServletResponse response){
		try {
			this.print(response, service.queryMenuTree());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SrUtils.printStackTrace(e);
		}
	}

	@RequestMapping("/tree2")
	public void tree2(HttpServletRequest request, HttpServletResponse response){
		try {
			this.print(response, service.queryMenuTree(super.getParameterMap(request)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SrUtils.printStackTrace(e);
		}
	}
	
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response){
		try {
			this.print(response, service.queryMenuList(super.getParameterMap(request)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SrUtils.printStackTrace(e);
		}
	}
	
	
	@RequestMapping("/save")
	public void save(HttpServletRequest request, HttpServletResponse response){
		try {
			JSONObject obj = new JSONObject();
			try {
				obj.put("result", service.insertOrUpdate(request.getParameter("dtos"), "sys_menu"));
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
			obj.put("result", service.delete(request.getParameter("ids"), "sys_menu"));
			obj.put("flag", "success");
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			obj.put("flag", "error");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
}
