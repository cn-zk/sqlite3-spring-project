package com.schrodinger.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.schrodinger.basic.BasicAction;
import com.schrodinger.service.SrFlagService;

import net.sf.json.JSONObject;

/** 
 * @author Bronya
 * @version 创建时间：2016-10-25
 */
@Controller
@RequestMapping("flag")
public class SrFlagAction extends BasicAction{

	@Autowired
	private SrFlagService service;

	@RequestMapping("/queryFlag")
	public void queryUserFlag(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		obj.put("rows", service.queryUserFlag(params));
		obj.put("total", params.get("total"));
		write(response, obj);
	}
	
	@RequestMapping("/queryFlagMin")
	public void queryFlagMin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		obj.put("rows", service.queryFlagMin(params));
		obj.put("total", params.get("total"));
		write(response, obj);
	}
	
	@RequestMapping("/findFlag")
	public void findUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		String id = request.getParameter("id");
		obj.put("dto", service.find("sys_flag", id));
		write(response, obj);
	}
	
	@RequestMapping("/flagUpdate")
	public void userUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		obj.put("result", service.insertOrUpdate(request.getParameter("dtos"), "sys_flag"));
		obj.put("flag", "success");
		write(response, obj);
	}
	
	@RequestMapping("/flagDelete")
	public void userDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("result", service.delete(request.getParameter("ids"), "sys_flag"));
		obj.put("flag", "success");
		write(response, obj);
	}
}

