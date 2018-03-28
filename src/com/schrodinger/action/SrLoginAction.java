package com.schrodinger.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.schrodinger.basic.BasicAction;
import com.schrodinger.service.SrAccountService;
import com.schrodinger.service.SrLogService;
import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("login")
public class SrLoginAction extends BasicAction{

	@Autowired
	SrAccountService service;
	@Autowired
	SrLogService log;
	
	@RequestMapping("/doit")
	public ModelAndView doit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		HttpSession session = request.getSession();
		JSONObject login = (JSONObject) session.getAttribute("login");
		String path = request.getParameter("path");
//		String browser = (String) session.getAttribute("browser");
		
//		String s1 = request.getHeader("user-agent");
//		String agent = (s1.contains("Android") || s1.contains("iPhone") || s1.contains("iPad"))?"pe":"pc";
		if(login == null){
			mav.setViewName("/login.jsp");
		}else{
			mav.addObject("id", login.get("id"));
			mav.addObject("name", login.get("name"));
			mav.addObject("role_name", login.get("role_name"));
			if(path == null){
				path = "com/schrodinger/dashboard.jsp";
			}
			mav.setViewName("/"+path);
		}
		return mav;
	}
	
	@RequestMapping("/quit_")
	public ModelAndView quit_(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		request.getSession().setAttribute("login", null);
		request.getSession().setAttribute("agent", null);
		mav.setViewName("doit");
		return mav;
	}
	
	@RequestMapping("/switchPc")
	public void switchPc(HttpServletRequest request, HttpServletResponse response){
		JSONObject obj = new JSONObject();
		HttpSession session = request.getSession();
		session.setAttribute("agent", "pc");
		obj.put("flag", true);
		this.print(response, obj);
	}
	
	@RequestMapping("/login_")
	public void login_(HttpServletRequest request, HttpServletResponse response){
		JSONObject obj = new JSONObject();
		String login = request.getParameter("login");
		String pwd = request.getParameter("pwd");

		String s1 = request.getHeader("user-agent");
		String browse = SrUtils.splitBrowser(s1);
		
		try {
			JSONObject l = service.loginAccount(login, pwd);
			if(l != null){
//				boolean flag = s1.contains("Android") || s1.contains("iPhone") || s1.contains("iPad");
				HttpSession session = request.getSession();
				session.setAttribute("browser", browse);
				session.setAttribute("login", l);
				session.setAttribute("id", l.get("id"));
				session.setAttribute("ip", SrUtils.getRemoteHost(request));
				
				log.logLogin(browse+" 登陆系统");

				JSONObject user = service.findUser((String)l.get("name"));
				if(user != null){
					session.setAttribute("user_id", user.get("id"));
					session.setAttribute("user_name", user.get("name"));
					session.setAttribute("is_group", user.get("is_group"));
				}
				
				obj.put("flag", "success");
			}else{
				log.logLoginFail(browse+" 登陆系统", "["+login+"/"+pwd+"]");
				obj.put("flag", "error");
			}
			
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			obj.put("flag", "error");
			obj.put("error", e.getMessage());
		}
		this.print(response, obj);
	}

}
