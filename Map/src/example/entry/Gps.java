package example.entry;

public class Gps {
	private String ip;
	private double wgLat;	//Î³¶È
	private double wgLon;	//¾­¶È
	
	public Gps(double wgLon, double wgLat) {
		this.wgLat = wgLat;
		this.wgLon = wgLon;
	}
	
	public Gps(String ip, double wgLon, double wgLat) {
		this.ip = ip;
		this.wgLon = wgLon;
		this.wgLat = wgLat;
		
	}
	public double getWgLat() {
		return wgLat;
	}

	public void setWgLat(double wgLat) {
		this.wgLat = wgLat;
	}

	public double getWgLon() {
		return wgLon;
	}

	public void setWgLon(double wgLon) {
		this.wgLon = wgLon;
	}
	
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return "Gps [ip=" + ip + ", wgLat=" + wgLat + ", wgLon=" + wgLon + "]";
	}
	
	
}
