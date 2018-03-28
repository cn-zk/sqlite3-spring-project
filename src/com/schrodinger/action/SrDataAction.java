package com.schrodinger.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.schrodinger.basic.BasicAction;
import com.schrodinger.dao.BackListener;
import com.schrodinger.dao.SQLSet;
import com.schrodinger.service.SrDataSet;
import com.schrodinger.service.SrLogService;
import com.schrodinger.utils.SrStream;
import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("data")
public class SrDataAction extends BasicAction{

	@Autowired
	SrLogService service;
	
	@Autowired
	SrDataSet dataset;

	@RequestMapping("/view")
	public ModelAndView data(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView();
		mav.addObject("dbname", SQLSet.get("dbname"));
		mav.addObject("size", "（ "+(new File(SQLSet.get("path")).length()/1024)+" kb）");
		mav.addObject("path", SQLSet.get("path")); 
		mav.addObject("backThread", BackListener.isBackThreadAlive() ? "运行中" : "已终止"); 
		mav.addObject("backTime", dataset.getBackTimeStr());
		mav.addObject("properties", SQLSet.getString());
		
		String ct = SrUtils.currentDate("yyyy");
		
		JSONArray dirs = new JSONArray();
		File[] fs = new File(SrUtils.getBackPath()).getParentFile().listFiles();
		for(int i=fs.length-1 ; i>=0 ;i--){
			File f = fs[i];
			JSONObject obj = new JSONObject();
			obj.put("text", f.getName());
			obj.put("value", f.getName());
			if(ct.equals(f.getName())){
				obj.put("selected", true);
			}
			dirs.add(obj);
		}
		
		mav.addObject("dir", dirs.toString());
		mav.setViewName("/pc/sys/data/us_data_sett.jsp");
		return mav;
	}
	
	@RequestMapping("/dels")
	public void dels(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String name = request.getParameter("names");
		String names[] = name.split(";");
		
		int i=0;
		for(String n : names){
			if(new File(SrUtils.getBackPath()+File.separator + n).delete()){
				i ++;
			}
		}
		
		JSONObject obj = new JSONObject();
		obj.put("flag", true);
		obj.put("result", i);
		
		write(response, obj);
	}
	
	@RequestMapping("/back")
	public void back(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String fname=SQLSet.get("dbname") + "_"+SrUtils.currentDate("MMdd")+" (手动).db";
		String newf = SrUtils.getBackPath()+File.separator+fname;

		JSONObject obj = new JSONObject();
		if(new File(newf).exists()){
			obj.put("flag", "error");
			obj.put("error", fname +" 备份文件已经存在！");
		}else{
			obj.put("flag", SrStream.ByteCopy(SQLSet.get("path"), newf));
		}
		
		write(response, obj);
	}
	
	@RequestMapping("/findBackTime")
	public void find(HttpServletRequest request, HttpServletResponse response){
		try {
			this.print(response, dataset.find("sys_set", request.getParameter("id")));
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
		}
	}
	
	@RequestMapping("/saveBackTime")
	public void saveBackTime(HttpServletRequest request, HttpServletResponse response){
		try {
			JSONObject obj = new JSONObject();
			try {
				obj.put("result", dataset.insertOrUpdate(request.getParameter("dtos"), "sys_set"));
				obj.put("backTime", dataset.getBackTimeStr());
				obj.put("flag", "success");
			} catch (Exception e) {
				SrUtils.printStackTrace(e);
				obj.put("flag", "error");
				obj.put("error", e.getMessage());
			}
			this.print(response, obj);
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
		}
	}
	
	@RequestMapping("/backTime")
	public void backTime(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject obj = new JSONObject();
		obj.put("backTime", SrUtils.formatTime(BackListener.getBackTime()));
		write(response, obj);
	}
	
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String year = request.getParameter("year");
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		JSONArray arr = new JSONArray();
		File dir = new File(SrUtils.getBackPath(year));
		if(dir.exists()){
			for(File f : dir.listFiles()){
				JSONObject obj = new JSONObject();
				obj.put("name", f.getName());
				obj.put("size", f.length());
				obj.put("tc", SrUtils.toCapacity(f.length(), 2));
				obj.put("last", fmt.format(new Date(f.lastModified())));
				arr.add(obj);
			}
		}
		JSONObject obj = new JSONObject();
		obj.put("rows", arr);
		obj.put("total", arr.size());
		write(response, obj);
		
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping("/download")
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String fileName=SQLSet.get("dbname")+"_"+SrUtils.currentDate("yyyymmdd");
        File file = new File(SQLSet.get("path"));  
        if(file.exists()){  
            OutputStream os = new BufferedOutputStream(response.getOutputStream());  
            try {  
                response.setContentType("application/octet-stream");  
                if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {   //IE浏览器  
                    fileName = URLEncoder.encode(fileName + ".db", "UTF-8");    
                } else {    
                    fileName = URLDecoder.decode(fileName + ".db");//其他浏览器  
                }  
                response.setHeader("Content-disposition", "attachment; filename="  
                        + new String(fileName.getBytes("utf-8"), "ISO8859-1")); // 指定下载的文件名  
                os.write(FileUtils.readFileToByteArray(file));  
                os.flush();  
            } catch (IOException e) {  
                e.printStackTrace();  
            } finally {  
                if(os != null){  
                    os.close();  
                }  
            }  
        }  
	}
	
}
