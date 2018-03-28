package com.schrodinger.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.schrodinger.basic.BasicAction;
import com.schrodinger.service.SrSysserverService;

import net.sf.json.JSONObject;

/** 
 * @author Bronya
 * @version 创建时间：2016-10-25
 */
@Controller
@RequestMapping("server")
public class SrSysserverAction extends BasicAction {

	@Autowired
	private SrSysserverService service;
	
	@RequestMapping("/lookup")
	public void sysServerLookup(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		obj.put("data", service.queryLookupComboData(params));
		write(response, obj);
	}
	
	@RequestMapping("/data")
	public void sysServerData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		obj.put("rows", service.queryData(params));
		obj.put("total", params.get("total"));
		write(response, obj);
	}
	
}
