package kr.hkit.mydiary.write;

public class AddInfo {
	private String address;
	private String latitude;
	private String longtude;
	
	public AddInfo(String address, String latitude, String longtude) {
		super();
		this.address = address;
		this.latitude = latitude;
		this.longtude = longtude;
	}

	public AddInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
		return String.format("AddInfo [address=%s, latitude=%s, longtude=%s]",
				address, latitude, longtude);
	}
	
	
}
