package com.schrodinger.basic;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.schrodinger.utils.SrUtils;

public class BasicAction {

	public Map<String,String> getParameterMap(HttpServletRequest request){
		Map<?, ?> map = request.getParameterMap();
		Map<String, String> pms = new HashMap<String, String>(map.size());
		
		Iterator<?> iter = map.entrySet().iterator();
		while(iter.hasNext()){
			Entry<?,?> en = (Entry<?, ?>) iter.next();
			if(en.getValue() != null){
				String[] vals = (String[]) en.getValue();
				pms.put((String) en.getKey(), vals.length == 1 ? vals[0] : null);
			}
		}
		
		return pms;
	}
	
	public void write(HttpServletResponse response, Object obj){
		print(response, obj);
	}
	public void print(HttpServletResponse response, Object obj){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		Writer w = null;
		try {
			w = response.getWriter();
			w.write(obj.toString());
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			try {
				if(w != null)
				w.write("{\"error\":\""+e.getMessage()+"\"}");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
