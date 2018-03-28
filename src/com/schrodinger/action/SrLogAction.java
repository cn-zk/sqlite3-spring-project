package com.schrodinger.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.schrodinger.basic.BasicAction;
import com.schrodinger.service.SrLogService;
import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("log")
public class SrLogAction extends BasicAction{

	@Autowired
	SrLogService service;
	
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		try {
			obj.put("rows", service.queryList(params));
			obj.put("total", params.get("total"));
		} catch (Exception e) {
			obj.put("rows", new JSONArray());
			obj.put("total", 0);
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	@RequestMapping("/dels")
	public void userDelete(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("result", service.delete(request.getParameter("ids"), "sys_log"));
			obj.put("flag", "success");
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			obj.put("flag", "error");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
}
