package com.schrodinger.action;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.schrodinger.basic.BasicAction;
import com.schrodinger.service.SrExportService;
import com.schrodinger.utils.SrUtils;

/**
 * @author Bronya
 * @version 创建时间：2016-10-25
 */
@Controller
@RequestMapping("exp")
public class SrExportAction extends BasicAction {

	@Autowired
	private SrExportService exp;

	@SuppressWarnings("deprecation")
	@RequestMapping("/downloadUser")
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "人员分组通讯录_" + SrUtils.currentDate("yyyymmdd");
		OutputStream os = new BufferedOutputStream(response.getOutputStream());
		try {
			response.setContentType("application/octet-stream");
			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) { // IE浏览器
				fileName = URLEncoder.encode(fileName + ".xls", "UTF-8");
			} else {
				fileName = URLDecoder.decode(fileName + ".xls");// 其他浏览器
			}
			response.setHeader("Content-disposition",
					"attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1")); // 指定下载的文件名
			exp.exportData(os);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

}
