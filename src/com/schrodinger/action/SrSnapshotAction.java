package com.schrodinger.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.schrodinger.basic.BasicAction;
import com.schrodinger.service.SrSnapshotService;
import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONObject;

/** 
 * @author Bronya
 * @version 创建时间：2016-10-25
 */
@Controller
@RequestMapping("snapshot")
public class SrSnapshotAction extends BasicAction {

	@Autowired
	private SrSnapshotService service;
	
	/**
	 * 同步快照数据
	 * @param request
	 * @param response
	 */
	@RequestMapping("/sync")
	public void syncSnapshot(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("result", service.syncSnapshot());
			obj.put("flag", "success");
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			obj.put("flag", "error");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	/**
	 * 同步去年快照数据
	 * @param request
	 * @param response
	 */
	@RequestMapping("/sync0")
	public void syncSnapshotLast(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("result", service.syncSnapshot0(true));
			obj.put("flag", "success");
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			obj.put("flag", "error");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
	
	/**
	 * 重建快照数据
	 * @param request
	 * @param response
	 */
	@RequestMapping("/rebuild")
	public void snapshotRebuild(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("result", service.rebuild(null));
			obj.put("flag", "success");
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			obj.put("flag", "error");
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}
}
