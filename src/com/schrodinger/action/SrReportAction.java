package com.schrodinger.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.schrodinger.basic.BasicAction;
import com.schrodinger.service.SrReportService;

/** 
 * @author Bronya
 * @version 创建时间：2016-10-25
 */
@Controller
@RequestMapping("report")
public class SrReportAction extends BasicAction{

	@Autowired
	SrReportService service;
	
	@RequestMapping("/reportGroupPlace")
	public void reportGroupPlace(HttpServletRequest request, HttpServletResponse response) throws Exception {
		write(response, service.queryGroupPlace(getParameterMap(request)));
	}
	@RequestMapping("/reportGroupSex")
	public void reportGroupSex(HttpServletRequest request, HttpServletResponse response) throws Exception {
		write(response, service.queryGroupSex(getParameterMap(request)));
	}
	@RequestMapping("/reportMonthPlace")
	public void reportMonthPlace(HttpServletRequest request, HttpServletResponse response) throws Exception {
		write(response, service.queryMonthPlace(getParameterMap(request)));
	}
	
	@RequestMapping("/reportGroupYearUser")
	public void reportGroupYearUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		write(response, service.queryGroupYearUser(getParameterMap(request)));
	}
	
	@RequestMapping("/reportGroupUser")
	public void reportGroupUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		write(response, service.queryGroupUser(getParameterMap(request)));
	}
	
	@RequestMapping("/reportGroupMonth")
	public void reportGroupMonth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		write(response, service.queryGroupMonth(getParameterMap(request)));
	}
	
	@RequestMapping("/reportGroupSkill")
	public void reportGroupSkill(HttpServletRequest request, HttpServletResponse response) throws Exception {
		write(response, service.queryGroupSkill(getParameterMap(request)));
	}

	@RequestMapping("/reportGroupLevel")
	public void reportGroupLevel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		write(response, service.queryGroupLevel(getParameterMap(request)));
	}
	
	@RequestMapping("/reportGroupQuit")
	public void reportGroupQuit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		write(response, service.queryGroupQuit(getParameterMap(request)));
	}
	
	@RequestMapping("/reportGroupHost")
	public void reportGroupHost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		write(response, service.queryGroupHost(getParameterMap(request)));
	}
	
	@RequestMapping("/reportGroupBook")
	public void reportGroupBook(HttpServletRequest request, HttpServletResponse response) throws Exception {
		write(response, service.queryGroupBook(getParameterMap(request)));
	}
	
}

