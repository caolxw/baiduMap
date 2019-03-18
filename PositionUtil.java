package example.util;

import example.entry.Gps;

/*
 * ��ͼ����ת��
 * WGS84	����ͨ������ϵ
 * GCJ02	��������ϵ	��Ѷ
 * BD-09	�ٶ�����ϵ
 */

/*
 * public class Gps {
		private String ip;
		private double wgLat;	//γ��
		private double wgLon;	//����
	}
 */
public class PositionUtil {
	
	public static double pi = Math.PI;
	public static double a = 6378245.0;
	public static double ee = 0.00669342162296594323;
	public static double x_pi = pi * 3000.0 / 180.0;
	
	/*
	 * WGS84 to GCJ02
	 * 
	 * @param wglon
	 * @param wglat
	 * @return Gps
	 */
	public static Gps gps84_to_gcj02(double wglon, double wglat) {
		if (OutOfChina(wglon,wglat)) {
			return null;
		}
		double dLat = transformLat(wglon - 105, wglat - 35);
		double dLon = transformLon(wglon - 105, wglat - 35);
		double radLat = wglat / 180 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtmagic = Math.sqrt(magic);
		dLat = (dLat * 180) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
		dLon = (dLon * 180) / (a / sqrtmagic * Math.cos(radLat) * pi);
		double mglat = wglat + dLat;
		double mglon = wglon + dLon;
		return new Gps(mglon, mglat);
		
	}
	
	/*
	 * GCJ02 to BD-09
	 * 
	 * @param gcLon
	 * @param gclat
	 * @return
	 */
	public static Gps gcj02_to_bd09(double gclon, double gclat) {
		double x = gclon;
		double y = gclat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
		double theta = Math.atan2(y,x) + 0.000003 * Math.cos(x * x_pi);
		
		double bdlon = z * Math.cos(theta) + 0.0065;
		double bdlat = z * Math.sin(theta) + 0.006;
		
		return new Gps(bdlon, bdlat);
	}
	
	/*
	 * WGS84 to BD09
	 * 
	 * @param bdlon
	 * @param bdlat
	 * @return
	 */
	public static Gps gps84_to_bd09(double wglon, double wglat) {
		Gps gps = PositionUtil.gps84_to_gcj02(wglon,wglat);
		//System.out.println(gps);
		if (gps == null) {
			gps = new Gps(wglon, wglat);
		}else {
			gps = PositionUtil.gcj02_to_bd09(gps.getWgLon(), gps.getWgLat());
		}
		
		//System.out.println(gps);
		return gps;
				
	}
	
	private static double transformLon(double x, double y) {
		// TODO Auto-generated method stub
		double ret = 300.0 + 2.0 * x + 3.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * pi) + 300 * Math.sin(x / 30 * pi)) * 2 / 3;
		return ret;
	}

	private static double transformLat(double x, double y) {
		// TODO Auto-generated method stub
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static boolean OutOfChina(double lon, double lat) {
		// TODO Auto-generated method stub
		
		if (lon < 72.004 || lon > 137.8347) {
			return true;
		}
		if (lat < 0.8293 || lat > 55.8271) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(Math.PI);
		System.out.println(x_pi);
		
		/*
		 * �������ݣ�
		 * ����Ӣ�ۼ��
		 * �ٶ�����ϵ��116.404043,39.911012
		 * ��������ϵ����Ѷ�ߵ£���116.397700,39.904560
		 * ���ʱ�׼����ϵ��116.3914166741,39.9032629520
		 */
		
		Gps gps = PositionUtil.gps84_to_gcj02(116.3914166741,39.9032629520);
		System.out.println(gps);
		gps = PositionUtil.gcj02_to_bd09(gps.getWgLon(), gps.getWgLat());
		System.out.println(gps);
		
		gps = PositionUtil.gps84_to_bd09(116.3914166741,39.9032629520);
		System.out.println(gps);
	}
}
