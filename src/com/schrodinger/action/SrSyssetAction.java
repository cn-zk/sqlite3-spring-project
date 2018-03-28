package com.schrodinger.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.schrodinger.basic.BasicAction;
import com.schrodinger.dao.SystemDatabase;
import com.schrodinger.service.SrSnapshotService;
import com.schrodinger.service.SrSyssetService;
import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Bronya
 * @version 创建时间：2016-10-28
 */
@Controller
@RequestMapping("sysset")
public class SrSyssetAction extends BasicAction{

	@Autowired
	private SrSyssetService service;
	
	@Autowired
	SrSnapshotService snapshot;

	@RequestMapping("/lookupList")
	public void queryLookupList(HttpServletRequest request, HttpServletResponse response) {

		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		try {
			obj.put("rows", service.queryLookupList(params));
			obj.put("total", params.get("total"));
		} catch (Exception e) {
			obj.put("rows", new JSONArray());
			obj.put("total", 0);
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	@RequestMapping("/lookupChilds")
	public void queryLookupChilds(HttpServletRequest request, HttpServletResponse response) {

		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		try {
			obj.put("rows", service.queryLookupChilds(params));
			obj.put("total", params.get("total"));
		} catch (Exception e) {
			obj.put("rows", new JSONArray());
			obj.put("total", 0);
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	@RequestMapping("/lookupUpdate")
	public void insertOrUpdate(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		try {
			String dtos = request.getParameter("dtos");
			String t = request.getParameter("t");
			obj.put("result", service.insertOrUpdate(dtos, t != null ? "sys_lookup_tl" : "sys_lookup"));
			obj.put("flag", "success");
		} catch (Exception e) {
			obj.put("flag", "error");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	@RequestMapping("/lookupDelete")
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("result", service.delete(getParameterMap(request)));
			obj.put("flag", "success");
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			obj.put("flag", "error");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	@RequestMapping("/syncUser")
	public void syncUser(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("result", SystemDatabase.newInstance().syncSysUser(request.getParameter("month")));
			obj.put("flag", "success");
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			obj.put("flag", "error");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	/**
	 * 工时同步
	 * @param request
	 * @param response
	 */
	@RequestMapping("/syncSysWorkhours")
	public void syncSysWorkhours(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		try {
			String id = request.getParameter("id");
			String month = request.getParameter("month");
			obj.put("result", SystemDatabase.newInstance().syncSysWorkhours(id, month));
			obj.put("flag", "success");
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			obj.put("flag", "error");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	/**
	 * 出差同步
	 * @param request
	 * @param response
	 */
	@RequestMapping("/syncSysEvection")
	public void syncSysEvection(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		try {
			String id = request.getParameter("id");
			String month = request.getParameter("month");
			
			StringBuffer err = new StringBuffer();
			
			obj.put("result", SystemDatabase.newInstance().syncSysEvection(id, month, err));
			
			//TODO sync snapshot.
			snapshot.syncSnapshot();
			
			if(err.length() > 0){
				obj.put("flag", "error");
				obj.put("error", err.toString()+" 缺失用户!");
			}else{
				obj.put("flag", "success");
			}
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			obj.put("flag", "error");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	@RequestMapping("/syncLookup")
	public void syncLookup(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("result", SystemDatabase.newInstance().syncSysLookup(request.getParameter("type")));
			obj.put("flag", "success");
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			obj.put("flag", "error");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
}
