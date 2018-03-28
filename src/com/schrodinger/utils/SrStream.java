package com.schrodinger.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * StreamBag
 * @date	12/01/01
 */
public class SrStream {

	// 读写速度控制在1M以内
	private static final int MAX_LENGTH = 1024 * 1024;
	
	// 默认的读写格式
	private static final String DEFAULT_FORMAT = "UTF-8";
	
	public static String ReaderString(File file) throws IOException{
		return ReaderString(file, DEFAULT_FORMAT);
	}
	/**
	 * 读文件
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String ReaderString(File file, String format) throws IOException{
		return ReaderString(file, format, false);
	}
	/**
	 * 读文件
	 * @param file		文件
	 * @param lineWarp	是否过滤换行
	 * @return
	 * @throws IOException
	 */
	public static String ReaderString(File file, String format, boolean lineWarp) throws IOException{
		return ReaderString(new FileInputStream(file), format, lineWarp);
	}
	public static String ReaderString(InputStream input) throws IOException{
		return ReaderString(input, DEFAULT_FORMAT);
	}
	public static String ReaderString(InputStream input, String format) throws IOException{
		return ReaderString(input, format, false);
	}
	/**
	 * 字符流
	 * @param input  流
	 * @param format 字符集
	 * @param lineWarp 过滤换行
	 * @return String
	 * @throws IOException
	 */
	public static String ReaderString(InputStream input, String format,
			boolean lineWarp) throws IOException{
		StringBuffer content = new StringBuffer("");
		BufferedReader read = new BufferedReader(new InputStreamReader(input, format));
		try {
			if(lineWarp){
				while(read.ready()){
					content.append(read.readLine());
				}
			}else{
				char[] c = new char[1024];
				int end = -1;
				while((end = read.read(c)) != -1){
					content.append(new String(c, 0, end));
				}
			}
		} finally {
			if (read != null)
				read.close();
		}
		return content.toString(); 
	}
	
	/**
	 * 读取数据存放到Array中
	 * @param in
	 * @param format
	 * @return List<String>
	 * @throws IOException
	 */
	public static List<String> ReaderArray(InputStream in, String format) throws IOException {
		List<String> arr = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, format));
			while (br.ready()){
				arr.add(br.readLine());
			}
		} finally {
			if (in != null)
			in.close();
		}
		return arr;
	}
	/**
	 * 读取数据存放到Map中
	 * @param in
	 * @param format
	 * @return List<String>
	 * @throws IOException
	 */
	public static Map<String,String> ReaderMap(InputStream in, String format) throws IOException {
		Map<String,String> map = new HashMap<String,String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, format));
			String line = null;
			String[] kvs = null;
			while (br.ready()){
				line = br.readLine();
				kvs = line.split("=");
				map.put(kvs[0], line.length() > 1 ? kvs[1] : null);
			}
		} finally {
			if (in != null)
				in.close();
		}
		return map;
	}
	/**
	 * Byte copy (自动根据文件大小调整拷贝速度,最大创建字节数组不超过1M)
	 * 
	 * @param path
	 * @param toPath
	 * @return
	 * @throws IOException
	 */
	public static boolean ByteCopy(String path, String toPath)
			throws IOException {
		return ByteCopy(new File(path), new File(toPath));
	}
	
	/**
	 * 字节拷贝(效率高)
	 * @param file
	 * @param toFile
	 * @return
	 * @throws IOException
	 */
	public static boolean ByteCopy(File file, File toFile)
	throws IOException {
		boolean flag = false;
		if (file.exists()) {
			toFile.getParentFile().mkdirs();
			toFile.createNewFile();
			InputStream in = new FileInputStream(file);
			OutputStream out = new FileOutputStream(toFile);
			try {
				// 文件大小小于1M
				if(file.length() < MAX_LENGTH){
					int len = new Long(file.length()).intValue();
					byte[] arr = new byte[len];
					in.read(arr, 0, len);
					out.write(arr);
					out.flush();
				}else{
					int len = -1;
					byte[] arr = new byte[MAX_LENGTH];
					while((len = in.read(arr)) != -1){
						// 每次写入最大1M的数据
						out.write(arr, 0, len);
						out.flush();
					}
				}
				flag = true;
			} finally {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			}
		}
		return flag;
	}
	
	/**
	 * 字节流读文件(自动根据文件大小调整拷贝速度,最大创建字节数组不超过1M)
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String InputString(File file) throws IOException {
		StringBuffer buf = new StringBuffer();
		if (file != null && file.exists()) {
			InputStream in = new FileInputStream(file);
			try {
				byte b[] = new byte[file.length() < MAX_LENGTH ? (int) file.length() : MAX_LENGTH];
				int end = 0;
				while ((end = in.read(b)) != -1){
					buf.append(new String(b, 0, end));
				}
			} finally {
				if (in != null)
				in.close();
			}
		}
		return buf.toString();
	}

	/**
	 * Byte流(字节数组为1024,不建议读取大文件)
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String InputString(InputStream in) throws IOException {
		StringBuffer buf = new StringBuffer();
		try {
			byte b[] = new byte[1024];
			int end = 0;
			while ((end = in.read(b)) != -1){
				buf.append(new String(b, 0, end));
			}
		} finally {
			if (in != null)
			in.close();
		}
		return buf.toString();
	}
	
	/**
	 * 
	 * @param str
	 * @param save
	 * @throws IOException
	 */
	public static void write(String str, File save) throws IOException{
		OutputStream out = new FileOutputStream(save);
		out.write(str.getBytes());
		out.flush();
		out.close();
	}
}

