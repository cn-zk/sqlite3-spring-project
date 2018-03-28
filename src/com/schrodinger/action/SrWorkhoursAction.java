package com.schrodinger.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.schrodinger.basic.BasicAction;
import com.schrodinger.dao.SQLSet;
import com.schrodinger.service.SystemWorkhours;
import com.schrodinger.service.SrWorkhoursService;
import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** 
 * @author Bronya
 * @version 创建时间：2016-10-28
 */
@Controller
@RequestMapping("work")
public class SrWorkhoursAction extends BasicAction{

	@Autowired
	SrWorkhoursService service;
	@Autowired
	SystemWorkhours sysquery;

	@RequestMapping("/localWH")
	public void queryLocalHours(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		try {
			obj.put("rows", service.queryLocalHours(params));
			obj.put("total", params.get("total"));
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			obj.put("rows", new JSONArray());
			obj.put("total", 0);
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	@RequestMapping("/localE")
	public void queryLocalE(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		try {
			obj.put("rows", service.queryLocalEvections(params));
			obj.put("total", params.get("total"));
		} catch (Exception e) {
			obj.put("rows", new JSONArray());
			obj.put("total", 0);
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	@RequestMapping("/workhoursDay")
	public void queryWorkhoursDay(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		params.put("#cp", SQLSet.get("company"));
		try {
			obj.put("rows", sysquery.queryStndDays(params));
			obj.put("total", params.get("total"));
		} catch (Exception e) {
			obj.put("rows", new JSONArray());
			obj.put("total", 0);
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	@RequestMapping("/workhoursSurplus")
	public void queryWorkhoursSurplus(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		params.put("#cp", SQLSet.get("company"));
		try {
			obj.put("rows", sysquery.querySurplus(params));
			obj.put("total", params.get("total"));
		} catch (Exception e) {
			obj.put("rows", new JSONArray());
			obj.put("total", 0);
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	@RequestMapping("/workhoursRet")
	public void queryWorkhoursRet(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		try {
			obj.put("rows", sysquery.queryReturn(params));
			obj.put("total", params.get("total"));
		} catch (Exception e) {
			obj.put("rows", new JSONArray());
			obj.put("total", 0);
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	@RequestMapping("/workhoursGroup")
	public void queryWorkhoursGroup(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		try {
			obj.put("rows", sysquery.queryGroup(params));
			obj.put("total", params.get("total"));
		} catch (Exception e) {
			obj.put("rows", new JSONArray());
			obj.put("total", 0);
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
}
