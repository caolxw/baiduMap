package example.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.regexp.internal.recompile;

//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.Statement;

import example.entry.Gps;

public class SQLUntil {
	public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/mydb" + 
	"?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone = GMT";
	
	private static String user = "root";	//用户名
	private static String pwd = "";		//密码
	
	//根据显示范围查找坐标
	public static List<Gps> selectData(Gps max, Gps min) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement stat = null;
		
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, user, pwd);		
		stat = conn.createStatement();
		
		double maxlon = max.getWgLon();
		double maxlat = max.getWgLat();
		double minlon = min.getWgLon();
		double minlat = min.getWgLat();
		
		String sql = "select * from BD09 where Lon<=" + maxlon + "and Lon>=" + minlon + 
				"and Lat<=" + maxlat + "and Lat>=" + minlat;
		ResultSet rs = stat.executeQuery(sql);
		List list = new ArrayList();
		
		while (rs.next()) {
			Gps gps = new Gps(rs.getString(1), rs.getDouble(2), rs.getDouble(3));
			list.add(gps);
		}
		
		conn.close();
		stat.close();
		rs.close();
		
		return list;
	}
	
	//把数据写入数据库中
	public static void main(String[] args) throws Exception {
		File file = new File("****");				//数据文件地址
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		int n = 1;
		
		Connection conn = null;
		Statement stat = null;
		
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL, user, pwd);		
		stat = conn.createStatement();
		
		while ((line = br.readLine()) != null) {
			
		 	if (n <= 1329563) { n ++; continue; }
			Gps gps = FileUtil.getGps(line);
			String sql = "insert into BD09 values('" + gps.getIp() + "',"+ 
						gps.getWgLon() + ", " + gps.getWgLat() + ")";
			//addData(sql);
			stat.executeUpdate(sql);
		}
		
		conn.close();
		stat.close();
		System.out.println("done!");
	}
	
}
