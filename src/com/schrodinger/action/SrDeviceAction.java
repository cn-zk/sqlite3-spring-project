package com.schrodinger.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.schrodinger.basic.BasicAction;
import com.schrodinger.service.SrDeviceService;

import net.sf.json.JSONObject;

/** 
 * @author Bronya
 * @version 创建时间：2016-10-25
 */
@Controller
@RequestMapping("device")
public class SrDeviceAction extends BasicAction{

	@Autowired
	SrDeviceService service;

	@RequestMapping("/queryHostList")
	public void queryHostList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		obj.put("rows", service.queryHostList(params));
		obj.put("total", params.get("total"));
		write(response, obj);
		
	}
	
	@RequestMapping("/findHost")
	public void findUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		String id = request.getParameter("id");
		obj.put("dto", service.findHost(id));
		write(response, obj);
	}
	
	@RequestMapping("/hostUpdate")
	public void userUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		try {
			obj.put("result", service.insertOrUpdate(request.getParameter("dtos"), "sys_host"));
			obj.put("flag", "success");
		} catch (Exception e) {
			obj.put("flag", "failure");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	@RequestMapping("/hostDelete")
	public void userDelete(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		obj.put("result", service.delete(request.getParameter("ids"), "sys_host"));
		obj.put("flag", "success");
		write(response, obj);
	}
	
	@RequestMapping("/queryDisplayList")
	public void queryDisplayList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		obj.put("rows", service.queryDisplayList(params));
		obj.put("total", params.get("total"));
		write(response, obj);
	}
	
	
	@RequestMapping("/findDisplay")
	public void findDisplay(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		String id = request.getParameter("id");
		obj.put("dto", service.find("sys_display", id));
		write(response, obj);
	}
	
	@RequestMapping("/displayUpdate")
	public void displayUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		obj.put("result", service.insertOrUpdate(request.getParameter("dtos"), "sys_display"));
		obj.put("flag", "success");
		write(response, obj);
	}
	
	@RequestMapping("/displayDelete")
	public void displayDelete(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		obj.put("result", service.delete(request.getParameter("ids"), "sys_display"));
		obj.put("flag", "success");
		write(response, obj);
	}
	
	@RequestMapping("/queryNotebookList")
	public void queryNotebookList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		obj.put("rows", service.queryNotebookList(params));
		obj.put("total", params.get("total"));
		write(response, obj);
	}
	
	
	@RequestMapping("/findNotebook")
	public void findNotebook(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		String id = request.getParameter("id");
		obj.put("dto", service.findNotebook(id));
		write(response, obj);
	}
	
	@RequestMapping("/notebookUpdate")
	public void notebookUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		try {
			obj.put("result", service.insertOrUpdate(request.getParameter("dtos"), "sys_notebook"));
			obj.put("flag", "success");
		} catch (Exception e) {
			obj.put("flag", "failure");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	@RequestMapping("/notebookDelete")
	public void notebookDelete(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		obj.put("result", service.delete(request.getParameter("ids"), "sys_notebook"));
		obj.put("flag", "success");
		write(response, obj);
	}

	@RequestMapping("/queryManageList")
	public void queryManageList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		obj.put("rows", service.queryManageList(params));
		obj.put("total", params.get("total"));
		write(response, obj);
	}
	
	@RequestMapping("/findManage")
	public void findManage(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		String id = request.getParameter("id");
		obj.put("dto", service.findManage(id));
		write(response, obj);
	}
	
	@RequestMapping("/manageUpdate")
	public void manageUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		try {
			obj.put("result", service.insertOrUpdate(request.getParameter("dtos"), "sys_manage"));
			obj.put("flag", "success");
		} catch (Exception e) {
			obj.put("flag", "failure");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
}

