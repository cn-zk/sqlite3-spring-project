package com.schrodinger.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Bronya
 * @version 创建时间：2016-10-26
 */
public class SystemDatabase {

	private static SystemDatabase sd ;
	
	private static Connection conn = null;

//	static String 
//	location = null, // "10.216.70.230",
//	database = null, // "WH", 
//	user = null, // "pt6", 
//	password = null, // "cape",
//	company = null; // "rd";
	
	static{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+SQLSet.get("location")+":1521:" + SQLSet.get("database")
					, SQLSet.get("user"), SQLSet.get("password"));
			System.out.println("jdbc:oracle:thin:@"+
					SQLSet.get("location")+":1521:" + 
					SQLSet.get("database")+", "+ 
					SQLSet.get("user") +","+ 
					SQLSet.get("password"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private SystemDatabase() throws Exception {}
	
	public static SystemDatabase newInstance() throws Exception{
		if(sd == null){
			try {
				sd = new SystemDatabase();
			} catch (Exception e) {
				throw e;
			}
		}
		return sd;
	}
	
	/**
	 * 
	 * @param type		WH_CCDD
	 * @throws Exception
	 */
	public int syncSysLookup(String type) throws Exception{
		
		JSONObject dto = SQLiteDao.newInstance().queryObjectByField("sys_lookup", "lookup_type", type);
	    if(dto == null){
	    	dto = new JSONObject();
	    	dto.put("lookup_type", type);
	    	SQLiteDao.newInstance().insertOrUpdate("sys_lookup", dto);
	    }
	    String id = dto.getString("id");
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select t.lookup_name, t.lookup_code\n");
		sbSql.append("  from sys_lookup_v t\n");
		sbSql.append(" where t.lookup_type = '"+type+"'");

		Statement stat = conn.createStatement();
		ResultSet res = stat.executeQuery(sbSql.toString());
		ResultSetMetaData resd = res.getMetaData();
		
		SQLiteDao.newInstance().delete("lookup_id", id, "sys_lookup_tl");
		int n=0;
		while(res.next()){
			JSONObject obj = new JSONObject();
			for(int i = 1 ; i <= resd.getColumnCount() ; i ++){
				obj.put(resd.getColumnName(i).toLowerCase(), res.getString(i));
			}
			obj.put("lookup_id", id);
			n++;
			SQLiteDao.newInstance().insertOrUpdate("sys_lookup_tl", obj);
		}
		res.close();
		return n;
	}
	
	/**
	 * 同步系统用户
	 * 
	 * @param ids
	 * @throws Exception
	 */
	public int syncSysUser(String dt) throws Exception{
		if(dt == null){
			throw new Exception("截止日期不能为空!");
		}
		Statement stat = conn.createStatement();
		String sql = "select id as user_id, name, login_name, status, to_char(last_update_date, 'yyyy-MM-dd') entry, '0' is_del from sys_user where login_name like '"+SQLSet.get("company")+"-%' and last_update_date >= to_date('"+dt+"', 'yyyy-MM-dd')";
		ResultSet res = stat.executeQuery(sql);
		ResultSetMetaData resd = res.getMetaData();
		int c=0;
		while(res.next()){
			String name = res.getString("name");
			JSONObject old = SQLiteDao.newInstance().queryObjectByField("sys_user", "name", name);
			JSONObject obj;
			if(old != null){
				obj = old;
				if(!SrUtils.isEmpty((String)obj.get("user_id"))){
					continue;
				}
				// -1 过滤创建时间
				for(int i = 1 ; i <= resd.getColumnCount()-1 ; i ++){
					
					String key = resd.getColumnName(i).toLowerCase();
					if("entry".equals(key)){
						continue;
					}
					obj.put(key, res.getString(i));
				}
			}else{
				obj = new JSONObject();
				for(int i = 1 ; i <= resd.getColumnCount() ; i ++){
					obj.put(resd.getColumnName(i).toLowerCase(), res.getString(i));
				}
			}
			c ++;
			SQLiteDao.newInstance().insertOrUpdate("sys_user", obj);
		}
		res.close();
		return c;
	}
	
	/**
	 * 同步系统工时
	 * 
	 * @param id
	 * @param month
	 * @return 
	 * @throws Exception
	 */
	public int syncSysWorkhours(String id, String month) throws Exception{
		if(SrUtils.isEmpty(month)){
			throw new Exception("截止日期不能为空!");
		}
		Statement stat = conn.createStatement();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select id as work_id,\n");				// 记录id
		sbSql.append("       t.submitter_id as user_id,\n");	// 用户id
		sbSql.append("       to_char(t.begin_date, 'yyyy-MM') as month,\n");		// 开始时间
		sbSql.append("       t.item_code,\n");		// 项目编号
		sbSql.append("       t.actual_days as days,\n");	// 实际工时
		sbSql.append("       t.ot_days,\n");		// 加班天数
		sbSql.append("       t.ot_detail,\n");		// 加班明细
		sbSql.append("       t.leave_days,\n");		// 请假天数
		sbSql.append("       t.leave_reason,\n");	// 请假明细
		sbSql.append("       t.work_detail\n");	// 工作内容
		sbSql.append("  from wh_workhours_info t\n");
		sbSql.append("\n");
		sbSql.append("  where 1=1 and t.submitter_code like '"+SQLSet.get("company")+"-%' and state = '3'");

		Map<String,String> map = new HashMap<String, String>();
		if(!SrUtils.isEmpty(month)){
			sbSql.append(" and to_char(t.begin_date, 'yyyy-MM') = '"+month+"'");
			map.put("#month-eq", month);
		}
		if(!SrUtils.isEmpty(id)){
			sbSql.append(" and t.submitter_id = '"+id+"'"); 
			map.put("#user_id-eq", id);
		}
		
		String ids = SQLiteDao.newInstance().queryIds("sys_workhours", "work_id", map);
		if(ids != null){
			sbSql .append(" and t.id not in ("+ ids + ")");
		}
		
		ResultSet res = stat.executeQuery(sbSql.toString());
		ResultSetMetaData resd = res.getMetaData();
		int x = 0;
		while(res.next()){
			JSONObject obj = new JSONObject();
			JSONObject dto = null;
			for(int i = 1 ; i <= resd.getColumnCount() ; i ++){
				String fd =resd.getColumnName(i).toLowerCase();
				String val = res.getString(i);
				if("user_id".equals(fd)){
					dto = SQLiteDao.newInstance().queryObjectByField("sys_user", "user_id", val);
					if(dto == null || SrUtils.isEmpty(val)){
						continue;
					}
					val = dto.getString("id");
				}
				obj.put(fd, val);
			}
			if(dto == null){
				continue;
			}
			SQLiteDao.newInstance().insertOrUpdate("sys_workhours", obj);
			x ++;
		}
		res.close();
		return x;
	}
	
	/**
	 * 同步系统出差
	 * 
	 * @param id
	 * @param month
	 * @throws Exception
	 */
	public int syncSysEvection(String id, String month, StringBuffer err) throws Exception{
		if(month == null){
			throw new Exception("截止日期不能为空!");
		}
		Statement stat = conn.createStatement();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select t.id as work_id,\n");
		sbSql.append("       t.submitter_name as user_name,\n");
		sbSql.append("       t.submitter_id as user_id,\n");
		sbSql.append("       to_char(t.begin_date, 'yyyy-MM') as month,\n");
		sbSql.append("       t.item_code,\n");
		sbSql.append("       t.evection_days as days,\n");
		sbSql.append("       t.evection_place as place\n");
		sbSql.append("  from wh_evection_info t\n");
		sbSql.append(" where t.submitter_code like '"+SQLSet.get("company")+"-%' and t.state = '3'");
		
		Map<String,String> map = new HashMap<String, String>();
		if(!SrUtils.isEmpty(month)){
			sbSql.append("   and to_char(t.begin_date, 'yyyy-MM') = '"+month+"'\n");
			map.put("#month-eq", month);
		}
		if(!SrUtils.isEmpty(id)){
			sbSql.append("   and t.submitter_id = '"+id+"'"); 
			map.put("#user_id-eq", id);
		}

		String ids = SQLiteDao.newInstance().queryIds("sys_evections", "work_id", map);
		if(ids != null){
			sbSql .append(" and id not in ("+ ids + ")");
		}
		
		ResultSet res = stat.executeQuery(sbSql.toString());
		ResultSetMetaData resd = res.getMetaData();
		int x=0;
		while(res.next()){
			JSONObject obj = new JSONObject();
			JSONObject dto = null;
			String _name = null;
			for(int i = 1 ; i <= resd.getColumnCount() ; i ++){
				String fd =resd.getColumnName(i).toLowerCase();
				String val = res.getString(i);
				if("user_name".equals(fd)){
					_name = val;
				}else
				if("user_id".equals(fd)){
					dto = SQLiteDao.newInstance().queryObjectByField("sys_user", "user_id", val);
					if(dto == null || SrUtils.isEmpty(val)){
						continue;
					}
					val = dto.getString("id");
				}
				obj.put(fd, val);
			}
			if(dto == null){
				if(err.indexOf(_name) == -1){
					err.append(_name +",");
				}
				continue;
			}
			obj.remove("user_name");
			SQLiteDao.newInstance().insertOrUpdate("sys_evections", obj);
			x ++;
		}
		res.close();
		return x;
	}

	/**
	 * 获得合计
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int queryCount(String sql) throws Exception{
		sql = "select count(0) c from ("+sql+")";
		Statement stat = conn.createStatement();
		ResultSet res = stat.executeQuery(sql);    
		if(res.next()){      
		   return res.getInt(1);
		}
		return 0;
	}
	
	/**
	 * 分页查询
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public JSONArray queryPage(String sql ,Map<String, String> params) throws Exception {
		if(params != null && params.size() > 0){
			for(Entry<String, String> entry : params.entrySet()){
				if(entry.getKey().startsWith("#")){
					sql = sql.replace(entry.getKey(), entry.getValue());
				}
			}
		}
		try {
			int page = Integer.parseInt(params.get("page"));
			int rows = Integer.parseInt(params.get("rows"));
			
			params.put("total", queryCount(sql)+"");
			
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select *\n");
			sbSql.append("  from (select a.*, rownum rn\n");
			sbSql.append("          from (select * from ("+sql+")) a\n");
			sbSql.append("         where rownum <= "+(page * rows)+")\n");
			sbSql.append(" where rn >= "+((page-1) * rows +1)); 
			
			sql = sbSql.toString();
			
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
	
//	public static void main(String[] args) throws Exception {
//		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
//		SystemDatabase sd = SystemDatabase.newInstance();
		
//		SQLiteDao.newInstance().clearTable("sys_workhours");
		
//		sd.syncSysLookup("WH_CCDD");
//		sd.syncSysUser();
//		System.out.println("workhours================");
//		sd.syncSysWorkhours(null, "2016-11");
//		System.out.println("evection================");
//		sd.syncSysEvection(null, "2016-10");
//		sd.conn.close();
//	}

}
