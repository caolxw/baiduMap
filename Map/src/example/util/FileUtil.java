package example.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import example.entry.Gps;

public class FileUtil {
	
	/*
	 *数据处理函数
	 *数据原始格式：('3ca616906e5c', '1048081,265811')
	*/
	public static Gps getGps(String str) {
		String[] strs = str.split("'");
		String key = strs[1];
		String[] values = strs[3].split(",");
		String lonString = values[0];
		String latString = values[1];
//		System.out.println(values.length);
		StringBuilder lon = new StringBuilder();
		for (int i = 0; i < lonString.length(); i++) {
			lon.append(lonString.charAt(i));
			if (i == lonString.length() - 5) {
				lon.append(".");
			}
		}
		
		StringBuilder lat = new StringBuilder();
		for (int i = 0; i < latString.length(); i++) {
			lat.append(latString.charAt(i));
			if (i == latString.length() - 5) {
				lat.append(".");
			}
		}
		Gps gps = PositionUtil.gps84_to_bd09(Double.parseDouble(lon.toString()), 
				Double.parseDouble(lat.toString()));
		gps.setIp(key);
		return gps;
	}
	
	public static void main(String[] args) throws Exception {
		File file = new File("****");
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		String line = null; 
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			break;
		}
	}
}