package com.schrodinger.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.schrodinger.basic.BasicAction;
import com.schrodinger.service.SrSnapshotService;
import com.schrodinger.service.SrUserService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Bronya
 * @version 创建时间：2016-10-25
 */
@Controller
@RequestMapping("user")
public class SrUserAction extends BasicAction {

	@Autowired
	private SrUserService service;

	@Autowired
	private SrSnapshotService snapshot;

	@RequestMapping("/queryUserList")
	public void queryUserList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		try {
			obj.put("rows", service.queryUserList(params));
			obj.put("total", params.get("total"));
		} catch (Exception e) {
			obj.put("rows", new JSONArray());
			obj.put("total", 0);
			obj.put("error", e.getMessage());
		}
		write(response, obj);
	}

	@RequestMapping("/findUser")
	public void findUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		String id = request.getParameter("id");
		obj.put("dto", service.findUser(id));
		write(response, obj);
	}

	@RequestMapping("/userUpdate")
	public void userUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("result", service.insertOrUpdate(request.getParameter("dtos"), "sys_user"));
		// TODO sync snapshot.
		snapshot.syncSnapshot();
		obj.put("flag", "success");
		write(response, obj);
	}

	@RequestMapping("/userDelete")
	public void userDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		String ids = request.getParameter("ids");
		obj.put("result", service.delete(ids, "sys_user"));
		service.delete("user_id", ids, "sys_manage");
		service.delete("user_id", ids, "sys_workhours");
		service.delete("user_id", ids, "sys_evections");
		// TODO sync snapshot.
		snapshot.syncSnapshot();
		obj.put("flag", "success");

		write(response, obj);
	}

	@RequestMapping("/userBack")
	public void userBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("result", service.updateDel(getParameterMap(request)));
		// TODO sync snapshot.
		snapshot.syncSnapshot();
		obj.put("flag", "success");

		write(response, obj);
	}

	@RequestMapping("/findUserWork")
	public void findUserWork(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("ten", service.findUserWorkTen(request.getParameter("user_id")));
		obj.put("pat", service.findUserWorkPat(request.getParameter("user_id")));
		obj.put("flag", "success");

		write(response, obj);
	}

	@RequestMapping("/findUserItem")
	public void findUserItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("result", service.findUserItem(request.getParameter("user_id")));
		obj.put("flag", "success");
		write(response, obj);
	}

	@RequestMapping("/findUserEvec")
	public void findUserEvec(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("result", service.findUserEvec(request.getParameter("user_id")));
		obj.put("flag", "success");

		write(response, obj);
	}

	@RequestMapping("/queryUserOtLe")
	public void queryUserOtLe(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("rows", service.queryUserOtLeList(request.getParameter("user_id")));
		write(response, obj);
	}

	@RequestMapping("/findUserView")
	public void findUserView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("view", service.findUserView(getParameterMap(request)));
		obj.put("flag", "success");
		write(response, obj);
	}

	@RequestMapping("/findUserRate")
	public void findUserRate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("view", service.findUserRate(getParameterMap(request)));
		obj.put("flag", "success");

		write(response, obj);
	}

	@RequestMapping("/findUserRateList")
	public void findUserRateList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		Map<String, String> params = getParameterMap(request);
		obj.put("rows", service.findUserRateList(getParameterMap(request)));
		obj.put("total", params.get("total"));
		write(response, obj);
	}

	@RequestMapping("/queryGroupView")
	public void queryGroupView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("list", service.queryGroupView(getParameterMap(request)));
		obj.put("flag", "success");
		write(response, obj);
	}

}
