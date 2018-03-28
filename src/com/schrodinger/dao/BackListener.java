package com.schrodinger.dao;

import java.util.Calendar;

import com.schrodinger.utils.SrUtils;

import net.sf.json.JSONObject;

@SuppressWarnings("static-access")
public class BackListener {
	
	static Thread backThread;
	static long waiteTime = 0;
	static SQLiteDao dao;
	
	private static long getBackTime0() throws Exception{
		long backTime = 0;
		JSONObject json = dao.queryObjectById("1", "sys_set");
		
		int time = json.getInt("back_time");
		int type = json.getInt("back_type");
		
		switch (type) {
			case 1:
			case 2:
			case 3:
				backTime = time * SrUtils.DAY;
				break;
			case 4:
				backTime = SrUtils.differenceWeek(time) * SrUtils.DAY;
				break;
		}
		return backTime;
	}
	
	public static long getBackTime() {
		try {
			return getBackTime0() - waiteTime;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean isBackThreadAlive() {
		return backThread.isAlive();
	}
	
	public static void startListener(SQLiteDao dao){
		if(BackListener.dao == null){
			BackListener.dao = dao;
		}
		backThread = new Thread(new Runnable() {
			public void run() {
				try {
					// 1小时后启用
//					Thread.currentThread().sleep(3600000);
					while(true){
						//
						Thread.currentThread().sleep(1000);
						if(getBackTime() < 1){
							SrUtils.autoBack();
							waiteTime = 0;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		backThread.start();
		
		
		new Thread(new Runnable() {
			public void run() {
				
				try {
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
					cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
					cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
					long difference = System.currentTimeMillis() - cal.getTime().getTime();
					
					waiteTime += difference;
					
//					JSONObject json = SQLiteDao.newInstance().queryObjectById("1", "sys_set");
//					if("4".equals(json.getString("back_type"))){
//					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					while(true){
						waiteTime += 1000;
						Thread.currentThread().sleep(1000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
