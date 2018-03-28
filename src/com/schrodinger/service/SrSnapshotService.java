package com.schrodinger.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.schrodinger.basic.BasicService;
import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONObject;

/**
 * 快照
 * @author zhangk
 */
@Service
public class SrSnapshotService extends BasicService{
	
	static String year = null;
	
	public SrSnapshotService() {
		super();
		year = new SimpleDateFormat("yyyy").format(new Date());
	}

	/**
	 * 实时更新同步当年最新的快照数据
	 * @return 
	 * @throws Exception
	 */
	public int syncSnapshot() throws Exception{
		int i = syncSnapshot0();
		String year0 = new SimpleDateFormat("yyyy").format(new Date());
		if(!year.equals(year0)){
			// 跨年备份一次去年快照
			i += syncSnapshot0(true);
			// 删除5年前出差
			i+= dao.delete("delete from sys_evections where substr(month, 1, 4) = strftime('%Y', 'now', '-5 years')");
			// 删除5年前工时
			i+= dao.delete("delete from sys_workhours where substr(month, 1, 4) = strftime('%Y', 'now', '-5 years')");
			// 删除10年前快照
			i+= dao.delete("delete from sys_snapshot where year = strftime('%Y', 'now', '-10 years')");
			// 删除 5年前的日志
			i+= dao.delete("delete from sys_log where substr(create_date, 1, 4) = strftime('%Y', 'now', '-5 years')");
			year = year0;
		}
		return i;
	}
	
	/**
	 * 跨年和本年的备份
	 * @param lastyear 去年
	 * @return
	 * @throws Exception
	 */
	public int syncSnapshot0() throws Exception{
		return syncSnapshot0(false);
	}
	public int syncSnapshot0(boolean lastyear) throws Exception{
		try {
			String f = "";
			if(lastyear){
				f = "1";
			}
			
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("INSERT INTO sys_snapshot (\n");
			sbSql.append(" type,\n");
			sbSql.append(" find_id,\n");
			sbSql.append(" find_name,\n");
			sbSql.append(" find_val,\n");
			sbSql.append(" year,\n");
			sbSql.append(" last_time \n");
			sbSql.append(")\n");
			sbSql.append("SELECT type,\n");
			sbSql.append("    find_id,\n");
			sbSql.append("    find_name,\n");
			sbSql.append("    find_val,\n");
			sbSql.append("    year,\n");
			sbSql.append("    datetime('now','localtime') as last_time\n");
			sbSql.append("FROM snapshot"+f+"_v where id is null and find_id is not null");

			int is = this.dao.getJdbc().update(sbSql.toString());

			if(is > 0){
				// 新增
				dao.log(SrUtils.TYPE_INSERT,  "sys_snapshot"+f, "#{\"numbers\":\""+is+"\"}");
			}
			
			sbSql.setLength(0);
			sbSql.append("UPDATE sys_snapshot\n");
			sbSql.append("   SET find_val = (\n");
			sbSql.append("           SELECT find_val\n");
			sbSql.append("             FROM snapshot"+f+"_v\n");
			sbSql.append("            WHERE snapshot"+f+"_v.id = sys_snapshot.id\n");
			sbSql.append("       )\n");
			sbSql.append(" WHERE sys_snapshot.id IN (\n");
			sbSql.append("    SELECT id\n");
			sbSql.append("      FROM snapshot"+f+"_v\n");
			sbSql.append("     WHERE snapshot"+f+"_v.id IS NOT NULL AND\n");
			sbSql.append("           find_val + '' != val\n");
			sbSql.append(")"); 

			
			int up = this.dao.getJdbc().update(sbSql.toString());

			if(up > 0){
				// 保存
				dao.log(SrUtils.TYPE_UPDATE, "sys_snapshot"+f,  "#{\"numbers\":\""+up+"\"}");
			}

			return is + up;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("UsSnapshotService.syncSnapshot 快照更新失败！");
		}
	}

	/**
	 * 重建快照数据
	 * @param year	重建年份
	 * @return
	 * @throws Exception
	 */
	public JSONObject rebuild(String year) throws Exception {
		if(year == null){
			// 默认重建当前年
			year = new SimpleDateFormat("YYYY").format(new Date());
		}
		JSONObject ret = new JSONObject();
		// 删除快照
		ret.put("remove", dao.delete("year", year, "sys_snapshot"));
		// 重建快照
		ret.put("rebuild", syncSnapshot());
		return ret;
	}
	
}
