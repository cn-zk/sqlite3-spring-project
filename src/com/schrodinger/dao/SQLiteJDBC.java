package com.schrodinger.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Map;

import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author  Bronya
 * @version 2016-10-27 下午4:04:05
 */
public class SQLiteJDBC {

	public static final String NULL = "[null]";

	private Connection conn = null;
	
	private static SQLiteJDBC jdbc;
	
	/**
	 * @throws Exception 
	 * 
	 */
	private SQLiteJDBC() throws Exception {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+SQLSet.get("path"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(conn != null){
			System.out.println("Opened database successfully");
		}else{
			System.exit(0);
		}
		
		// 预先初始化
//		SystemDatabase.newInstance();
	}
	
	/**
	 * 
	 * @return
	 */
	public static SQLiteJDBC newInstance(){
		if(jdbc == null){
			try {
				jdbc = new SQLiteJDBC();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jdbc;
	}

	

	/**
	 * 分页查询
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public JSONArray queryPage(String sql ,Map<String, String> params) throws Exception {
		try {
			if(params != null && params.containsKey("page") && params.containsKey("rows")){
				
				int page = Integer.parseInt(params.get("page"));
				int rows = Integer.parseInt(params.get("rows"));
				
				params.put("total", queryCount(sql)+"");
				StringBuffer sbSql = new StringBuffer();
				sbSql.append(sql);
				sbSql.append(" limit "+ rows+" offset "+((page-1) * rows));
				sql = sbSql.toString();
			}
		} catch (NumberFormatException e) {}
		
		Statement stat = conn.createStatement();
		ResultSet res = stat.executeQuery(sql);
		ResultSetMetaData resd = res.getMetaData();
		
		JSONArray arr = new JSONArray();
		
		while(res.next()){
			JSONObject obj = new JSONObject();
			for(int i = 1 ; i <= resd.getColumnCount() ; i ++){
				obj.put(resd.getColumnName(i).toLowerCase(), res.getString(i));
			}
			arr.add(obj);
		}
		res.close();
		return arr;
	}
	
	/**
	 * 获得合计
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int queryCount(String sql) throws Exception{
		if(SrUtils.emptyId()){
			throw new Exception("session 失效");
		}
		sql = "select count(0) c from ("+sql+")";
		Statement stat = conn.createStatement();
		ResultSet res = stat.executeQuery(sql);    
		if(res.next()){      
		   return res.getInt(1);
		}
		return 0;
	}
	
	/**
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int update(String sql) throws Exception{
		Statement stat = conn.createStatement();
		int i = stat.executeUpdate(sql);
		stat.close();
		return i;
	}

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public JSONObject queryById(String sql) throws Exception {
		Statement stat = conn.createStatement();
		ResultSet res = stat.executeQuery(sql);
		ResultSetMetaData resd = res.getMetaData();
		
		JSONObject obj = null;
		if (res.next()) {
			obj = new JSONObject();
			for(int i = 1 ; i <= resd.getColumnCount() ; i ++){
				obj.put(resd.getColumnName(i).toLowerCase(), res.getString(i));
			}
		}
		res.close();
		stat.close();
		return obj;
	}

	/**
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public JSONArray queryPragma(String tableName) throws Exception {
		Statement stat = conn.createStatement();
		ResultSet res = stat.executeQuery("pragma table_info ('"+tableName+"')");
		
		JSONArray obj = new JSONArray();
		while (res.next()) {
			obj.add(res.getString("name"));
		}
		res.close();
		stat.close();
		return obj;
	}
}