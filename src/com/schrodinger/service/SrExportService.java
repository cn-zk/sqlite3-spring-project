package com.schrodinger.service;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schrodinger.basic.BasicService;
import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@SuppressWarnings("deprecation")
public class SrExportService extends BasicService{

	@Autowired
	private SrSysserverService sys ;

	private String[][] FILTER_FIELDS = new String[][]{
		new String[]{"id", "主键", "0"},
		new String[]{"name", "名称", "1"},
		new String[]{"sex", "性别", "1"},
		new String[]{"phone", "电话", "1"},
		new String[]{"login_name", "工时账号", "1"},
		new String[]{"level", "级别", "1"},
		new String[]{"skill", "技能", "1"},
		new String[]{"resource", "资源池", "0"},
		new String[]{"group_id", "分组", "1"},
		new String[]{"entry", "入职时间", "1"},
		new String[]{"ip", "IP", "0"}
	};

	private short[] bgs = new short[]{
		HSSFColor.CORAL.index,
		HSSFColor.LIME.index,
		HSSFColor.PALE_BLUE.index,
		HSSFColor.LIGHT_YELLOW.index,
		HSSFColor.GREY_25_PERCENT.index
	};
	
	private int tn;
	private HSSFCellStyle getHSSFCellStyle(HSSFWorkbook wb ,int it){
		HSSFFont font = wb.createFont();
		HSSFCellStyle cellStyle = wb.createCellStyle();
		
        font.setFontHeightInPoints((short) 11); //字体高度
        font.setColor(HSSFFont.COLOR_NORMAL); //字体颜色
        font.setFontName("微软雅黑"); //字体
//        font.setItalic(true); //是否使用斜体
//        font.setStrikeout(true); //是否使用划线

        // 设置单元格类型
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //水平布局：居中
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //水平布局：居中
        cellStyle.setWrapText(false);
        
        switch (it) {
		case 0:
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //宽度
	        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
	        tn = 0;
			break;
		case 1:
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		    cellStyle.setFillForegroundColor(bgs[tn ++]);
		    if(tn >= bgs.length){
		    	tn = 0;
		    }
			break;
		default:
			break;
		}
        
        cellStyle.setFont(font);
        return cellStyle;
	}

	private String getLookupName(JSONArray list, String code){
		if(code != null && !"null".equals(code))
		for(Object obj : list){
			JSONObject l = (JSONObject) obj;
			if(l.getString("value").equals(code)){
				return l.getString("text");
			}
		}
		return "";
	}
	
	public void exportData(OutputStream out) throws Exception{

		Map<String,String> p = new HashMap<String, String>(1);
		p.put("#lookup_type-eq", "_USER_SKILL");
		JSONArray skills = sys.queryLookupComboData(p);
		p = new HashMap<String, String>(1);
		p.put("#lookup_type-eq", "_USER_SEX");
		JSONArray sex = sys.queryLookupComboData(p);
		p = new HashMap<String, String>(1);
		p.put("#lookup_type-eq", "_USER_LEVEL");
		JSONArray levels = sys.queryLookupComboData(p);
		
		
		HSSFWorkbook wb = new HSSFWorkbook();
		
		HSSFSheet sheet = wb.createSheet("分组明细导出");
		HSSFRow title = sheet.createRow(0);
		
		String[] filter = new String[]{
			"name",
			"sex",
			"phone",
			"login_name",
			"level",
			"skill",
			"resource",
			"group_id",
			"entry",
			"email"
		};
        
		int cels=0,groupIndex = 0;
//		// set title cells.
		for(int i =0, j=0 ; i < FILTER_FIELDS.length ; i ++ ){
			String[] str = FILTER_FIELDS [i];
			if(!SrUtils.contains(filter, str[0])){
				continue;
			}
			HSSFCell cell = title.createCell(cels++);
			cell.setCellStyle(getHSSFCellStyle(wb, 0));
			cell.setCellValue(str[1]);
			if("group_id".equals(str[0])){
				groupIndex = j;
			}
			j++;
		}
		
		title.setHeightInPoints(23.25f);
		
		Map<String,String> params = new HashMap<String, String>(4);
		params.put("#is_del-eq", "0");
		params.put("#entry-notnull", "null");
		params.put("#quit-isnull", "null");
		params.put("sort", "group_id");
		params.put("order", "desc");
		JSONArray dtos = dao.queryPageList("sys_user", params);
		
		params = new HashMap<String, String>(1);
		params.put("is_grid", "1");
		
		JSONArray groups = dao.queryPageList("(select id, name from sys_user)", params);
		
		int begin = 1, end = 2, rowIndex = 1;
		String tempGrid = null;
		HSSFCellStyle cellGroupStyle = getHSSFCellStyle(wb, 1) ;
		for(Object obj : dtos){
			HSSFCellStyle defaultCell = getHSSFCellStyle(wb, 2);
			JSONObject dto = (JSONObject) obj;

			String group_id = String.valueOf(dto.get("group_id"));
			
			if(tempGrid == null){
				tempGrid = group_id;
			}else if(!tempGrid.equals(group_id)){
				cellGroupStyle = getHSSFCellStyle(wb, 1);
				
				HSSFRow row = sheet.getRow(begin);
				HSSFCell cell = row.getCell(groupIndex);
				
				String value = cell.getStringCellValue();
				int ib = 0;
				if(value.length() > 0){
					if((ib = value.indexOf("(")) != -1)
						value = value.substring(0, ib) + "小组 ("+(end-begin)+")";
					else{
						value += " ("+(end-begin)+")";
					}
				}else{
					value = " ("+(end-begin)+")";
				}
				cell.setCellValue(value);
				
				sheet.addMergedRegion(new Region(
		                 begin, //first row (0-based)       
		                (short)groupIndex, //first column  (0-based)       
		                 end-1, //last row (0-based)    
		                (short)groupIndex  //last column  (0-based)       
		        ));
			
				tempGrid = group_id;
				begin = end;
				end ++;
			}else{
				end++;
			}
			
			HSSFRow row = sheet.createRow(rowIndex ++);
			cels = 0;
			for(String[] str : FILTER_FIELDS){
				if(!SrUtils.contains(filter, str[0])){
					continue;
				}
				String value = String.valueOf(dto.get(str[0]));
				HSSFCell cell = row.createCell(cels++);
				cell.setCellStyle(defaultCell);
				if(tempGrid.equals(dto.getString("id"))){
					cell.setCellStyle(cellGroupStyle);
				}
				if("group_id".equals(str[0])){
					value = getGroupName(groups, value);
					cell.setCellStyle(cellGroupStyle);
				}else if("sex".equals(str[0])){
					value = getLookupName(sex, value);
				}else if("skill".equals(str[0])){
					value = getLookupName(skills, value);
				}else if("level".equals(str[0])){
					value = getLookupName(levels, value);
				}else if("null".equals(value)){
					value = "";
				}
				cell.setCellValue(value);
			}
		}
		
		// 收尾的分组合并
		if(end-begin > 0){
			HSSFRow row = sheet.getRow(begin);
			HSSFCell cell = row.getCell(groupIndex);
			
			String value = cell.getStringCellValue();
			int ib = 0;
			if(value.length() > 0){
				if((ib = value.indexOf("(")) != -1)
				value = value.substring(0, ib) + "小组 ("+(end-begin)+")";
				else{
					value += " ("+(end-begin)+")";
				}
			}else{
				value = " ("+(end-begin)+")";
			}
			cell.setCellValue(value);
			
			sheet.addMergedRegion(new Region(     
	                 begin, //first row (0-based)       
	                (short)groupIndex, //first column  (0-based)       
	                 end-1, //last row (0-based)    
	                (short)groupIndex  //last column  (0-based)       
	        ));
		}
		
		
		// column auto size.
		for(int i=0;i<cels;i++ ){
			sheet.autoSizeColumn((short) i);
		}
		sheet.autoSizeColumn((short) groupIndex, true);
		
		wb.write(out);
	}
	
	private String getGroupName(JSONArray groups, String id){
		if(id != null || !"null".equals(id))
		for(Object obj : groups){
			JSONObject g = (JSONObject) obj;
			if(g.getString("id").equals(id)){
				return String.valueOf(g.get("name"));
			}
		}
		return "";
	}

}
