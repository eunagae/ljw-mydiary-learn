package kr.hkit.mydiary.sqllite;

public class SelectForAllLocation {
	int id;
	String title;
	String addr;
	String latitude;
	String longtude;
	
	public SelectForAllLocation(int id, String title, String addr,
			String latitude, String longtude) {
		super();
		this.id = id;
		this.title = title;
		this.addr = addr;
		this.latitude = latitude;
		this.longtude = longtude;
	}
	public SelectForAllLocation() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongtude() {
		return longtude;
	}
	public void setLongtude(String longtude) {
		this.longtude = longtude;
	}
	@Override
	public String toString() {
		return String
				.format("SelectForAllLocation [id=%s, title=%s, addr=%s, latitude=%s, longtude=%s]",
						id, title, addr, latitude, longtude);
	}
	
	
	
}
