package com.schrodinger.dao;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** 
 * @author Bronya
 * @version 创建时间：2016-10-27
 */
@SuppressWarnings("unchecked")
public class SQLiteDao {
	
	private SQLiteJDBC jdbc;
	private static SQLiteDao dao;

	private SQLiteDao() {
		jdbc = SQLiteJDBC.newInstance();
		new File(SrUtils.getBackPath()).mkdirs();
	}
	
	public static SQLiteDao newInstance(){
		if(dao == null){
			dao = new SQLiteDao();
			BackListener.startListener(dao);
		}
		return dao;
	}
	
	public SQLiteJDBC getJdbc() {
		return jdbc;
	}
	
	/**
	 * 
	 * @param table
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public JSONArray queryPageList(String table, Map<String ,String> params) throws Exception{
		if(SrUtils.emptyId()){
			throw new Exception("session 失效");
		}
		return queryPageList_(table, params);
	}
	public JSONArray queryPageList_(String table, Map<String ,String> params) throws Exception{
		StringBuffer sql = new StringBuffer("select t.* from ("+ table + ") t where 1=1");
		queryParamSet(sql, params);
		return jdbc.queryPage(sql.toString(), params);
	}
	public JSONArray queryPageList_(String table) throws Exception{
		return queryPageList_(table, null);
	}
	public JSONArray queryPageList(String table) throws Exception{
		return queryPageList(table, null);
	}
	
	private void queryParamSet(StringBuffer sql , Map<String ,String> params) throws Exception{
		if(params != null){
			
			for(String key : params.keySet()){
				if(key.indexOf("#") == 0){
					String ks[] = key.substring(1).split("-");
					String qu = "like";
					String k = ks[0];
					String v = params.get(key);
					String end ;
					if(ks.length > 1){
						qu = ks[1];
					}
					if("like".equals(qu)){
						end = k + " like '%"+v+"%'";
					}else if("isnull".equals(qu)){
						end = k + " is null";
					}else if("notnull".equals(qu)){
						end = k + " not null";
					}else if("all".equals(qu)){
						StringBuffer buf = new StringBuffer("(");
						for(Object obj : queryPragma(k)){
							
							buf.append(" ");
							buf.append(obj);
							buf.append(" like '%");
							buf.append(v);
							buf.append("%' or");
							
						}
						buf.setLength(buf.length()-2);
						buf.append(")");
						end = buf.toString();
					}else if("index".equals(qu)){
						StringBuffer buf = new StringBuffer("(");
						for(Object obj : k.split("#")){
							
							buf.append(" ");
							buf.append(obj);
							buf.append(" like '%");
							buf.append(v);
							buf.append("%' or");
							
						}
						buf.setLength(buf.length()-2);
						buf.append(")");
						end = buf.toString();
					}else{
						end =  k + " = '" + v + "'";
					}
					
					sql.append(" and " + end);
				}
			}
			
			String sort = params.get("sort");
			String order = params.get("order");
			if(!SrUtils.isEmpty(sort, order)){
				sql.append(" order by "+ sort + " " + order);
			}
		}
	}
	
	
	public JSONArray queryPragma(String tableName) throws Exception{
		if(SrUtils.emptyId()){
			throw new Exception("session 失效");
		}
		return jdbc.queryPragma(tableName);
	}
	
	/**
	 * 
	 * @param obj
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public int insertOrUpdate(String table, Object obj) throws Exception{
		if(!"sys_log".equals(table) && SrUtils.emptyId()){
			throw new Exception("session 失效");
		}
		if(obj == null){
			return 0;
		}
		int i = 0;
		if(obj instanceof JSONArray){
			String id;
			JSONObject dto;
			int insert =0;
			JSONObject update = new JSONObject();
			for(Object o : (JSONArray) obj){
				dto = (JSONObject) o;
				id = (String)dto.get("id");
				if(SrUtils.isEmpty(id) || SrUtils.ID_AUTO.equals(id)){
					++ insert;
				}else{
					update.accumulate("id", id);
				}
				i = jdbc.update(toExcuteSQL(table, dto).toString());
			}
			if(insert > 0)
				log(SrUtils.TYPE_INSERT, table, "#{\"number\":\""+insert+"\"}");
			if(update.keySet().size() > 0)
				log(SrUtils.TYPE_UPDATE, table, "#"+update.toString());
		}else{
			JSONObject dto = (JSONObject) obj;
			String id = (String) dto.get("id");
			i = jdbc.update(toExcuteSQL(table, dto).toString());
			if(SrUtils.isEmpty(id) || SrUtils.ID_AUTO.equals(id)){
				log(SrUtils.TYPE_INSERT, table, "#"+obj);
			}else{
				log(SrUtils.TYPE_UPDATE, table, "#"+obj);
			}
		}
		return i;
	}
	
	public StringBuffer toExcuteSQL(String table ,JSONObject obj){
		StringBuffer sql;
		String value;

		String id = (String) obj.get("id");
		if(SrUtils.isEmpty(id) || SrUtils.ID_AUTO.equals(id)){
			// 自动ID
			if(SrUtils.ID_AUTO.equals(id)){
				obj.remove("id");
			}else{
				obj.put("id", SrUtils.getId());
			}
			sql = new StringBuffer("insert into ");
			sql.append(table);
			sql.append(" (");
			Set<String> set = (Set<String>)obj.keySet();
			String[] keys = new String[set.size()];
			int i=0;
			for(String key : set){
				if(key.startsWith("_")){
					continue;
				}
				sql.append("'"+key+"',");
				keys[i++] = key;
			}
			sql.setLength(sql.length()-1);
			sql.append(") values (");
			for(String key : keys){
				if(key == null || key.startsWith("_")){
					continue;
				}
				value = obj.getString(key);
				if(SQLiteJDBC.NULL.equals(value) || value == null){
					sql.append("null,");
				}else{
					sql.append("'"+obj.getString(key)+"',");
				}
			}
			sql.setLength(sql.length()-1);
			sql.append(")");
		}else{
			sql = new StringBuffer("update ");
			sql.append(table);
			sql.append(" set ");
			Set<String> set = (Set<String>)obj.keySet();
			for(String key : set){
				if(key.startsWith("_")){
					continue;
				}
				value = obj.getString(key);
				if("id".equals(key)){
					continue;
				}else if(SQLiteJDBC.NULL.equals(value)){
					sql.append(key + "=null,");
				}else{
					sql.append(key + "='"+value+"',");
				}
			}
			sql.setLength(sql.length()-1);
			sql.append(" where id='"+id+"'");
		}
		return sql;
	}
	
	public int delete(String id , String table) throws Exception{
		return delete("id", id, table);
	}
	
	public int delete(String field, String value , String table) throws Exception{
		String sql = "delete from "+ table +" where "+field+"='"+value+"'";
		return delete(sql);
	}
	
	public int delete(String sql) throws Exception{
		if(SrUtils.emptyId()){
			throw new Exception("session 失效");
		}
		try {
			int i=jdbc.update(sql);
			// 删除
			log(SrUtils.TYPE_DELETE, "sql",sql);
			return i;
		} catch (Exception e) {
			// 删除
			logFail(SrUtils.TYPE_DELETE, "sql", sql);
			SrUtils.printStackTrace(e);
			throw e;
		}
	}
	
	public int deleteAll(String table) throws Exception {
		if(SrUtils.emptyId()){
			throw new Exception("session 失效");
		}
		try {
			String sql = "delete from "+ table;
			int i= jdbc.update(sql);
			// 删除
			log(SrUtils.TYPE_DELETE, table,"#all");
			return i;
		} catch (Exception e) {
			SrUtils.printStackTrace(e);
			throw e;
		}
	}

	public String queryIds(String table, String field) throws Exception {
		return queryIds(table, field, null);
	}
	public String queryIds(String table, String field, Map<String,String> params) throws Exception {
		String ids = null;
		StringBuffer sql = new StringBuffer("select "+field+" from "+table +" where "+ field+" is not null");
		
		queryParamSet(sql, params);
		
		JSONArray list = jdbc.queryPage(sql.toString(), null);
		if(list.size() > 0){
			StringBuffer str = new StringBuffer("");
			Iterator<JSONObject> iter = list.iterator();
			while(iter.hasNext()){
				str.append("'"+iter.next().get(field)+"',");
			}
			str.setLength(str.length() -1);
			ids = str.toString();
		}
		return ids;
	}
	public int queryCount(String child) throws Exception {
		return jdbc.queryCount(child);
	}
	public JSONObject find(String table, String id) throws Exception {
		return queryObjectByField(table, "id", id);
	}
	public JSONObject queryObjectByField(String table, String field, Object value) throws Exception {
		return jdbc.queryById("select * from ("+table +") where "+field+"='"+value+"'");
	}
	public JSONObject queryObjectById(String id, String table) throws Exception{
		String sql = "select * from ("+table + ") where id ='"+id+"'";
		return jdbc.queryById(sql);
	}
	
	public void log(int type, String table, String text) {
		log(type, table, text, (String)SrUtils.getSession().getAttribute("id"), SrUtils.LOG_SUCCESS);
	}
	public void logFail(int type, String table, String text) {
		log(type, table, text, (String)SrUtils.getSession().getAttribute("id"), SrUtils.LOG_FAIL);
	}
	public void log(int type, String table, String text, String id, int state) {
		if("sys_log".equals(table)){
			return;
		}
		JSONObject obj = new JSONObject();
		obj.put("id", SrUtils.ID_AUTO);
		obj.put("type", type);
		obj.put("table_name", table);
		obj.put("text", text);
		obj.put("account_id", id);
		obj.put("create_date", SrUtils.currentTime());
		obj.put("state", state);
		obj.put("ip", SrUtils.getSession().getAttribute("ip"));
		try {
			insertOrUpdate("sys_log", obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SrUtils.printStackTrace(e);
		}
	}

}
