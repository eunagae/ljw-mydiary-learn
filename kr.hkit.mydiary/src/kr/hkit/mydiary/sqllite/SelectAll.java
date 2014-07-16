package kr.hkit.mydiary.sqllite;

public class SelectAll {
	private int id;
	private String title;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private String content;
	private String picpath1, picpath2, picpath3, picpath4, picpath5, picpath6;
	private String mp3Path, mp3Title, mp3Singer;
	private int mp3AlbumArtID;
	private String addr, latitude, longtude;
	private String url;
	
	public SelectAll(int id, String title, int year, int month, int day,
			int hour, int minute, String content, String picpath1,
			String picpath2, String picpath3, String picpath4, String picpath5,
			String picpath6, String mp3Path, String addr, String latitude,
			String longtude, String url) {
		super();
		this.id = id;
		this.title = title;
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.content = content;
		this.picpath1 = picpath1;
		this.picpath2 = picpath2;
		this.picpath3 = picpath3;
		this.picpath4 = picpath4;
		this.picpath5 = picpath5;
		this.picpath6 = picpath6;
		this.mp3Path = mp3Path;
		this.addr = addr;
		this.latitude = latitude;
		this.longtude = longtude;
		this.url = url;
	}
	
	
	public String getMp3Title() {
		return mp3Title;
	}


	public void setMp3Title(String mp3Title) {
		this.mp3Title = mp3Title;
	}


	public String getMp3Singer() {
		return mp3Singer;
	}


	public void setMp3Singer(String mp3Singer) {
		this.mp3Singer = mp3Singer;
	}


	public int getMp3AlbumArtID() {
		return mp3AlbumArtID;
	}


	public void setMp3AlbumArtID(int mp3AlbumArtID) {
		this.mp3AlbumArtID = mp3AlbumArtID;
	}


	public SelectAll(int id, String title, int year, int month, int day,
			int hour, int minute, String content, String picpath1,
			String picpath2, String picpath3, String picpath4, String picpath5,
			String picpath6, String mp3Path, String mp3Title, String mp3Singer,
			int mp3AlbumArtID, String addr, String latitude, String longtude,
			String url) {
		super();
		this.id = id;
		this.title = title;
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.content = content;
		this.picpath1 = picpath1;
		this.picpath2 = picpath2;
		this.picpath3 = picpath3;
		this.picpath4 = picpath4;
		this.picpath5 = picpath5;
		this.picpath6 = picpath6;
		this.mp3Path = mp3Path;
		this.mp3Title = mp3Title;
		this.mp3Singer = mp3Singer;
		this.mp3AlbumArtID = mp3AlbumArtID;
		this.addr = addr;
		this.latitude = latitude;
		this.longtude = longtude;
		this.url = url;
	}
	public SelectAll() {
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
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPicpath1() {
		return picpath1;
	}
	public void setPicpath1(String picpath1) {
		this.picpath1 = picpath1;
	}
	public String getPicpath2() {
		return picpath2;
	}
	public void setPicpath2(String picpath2) {
		this.picpath2 = picpath2;
	}
	public String getPicpath3() {
		return picpath3;
	}
	public void setPicpath3(String picpath3) {
		this.picpath3 = picpath3;
	}
	public String getPicpath4() {
		return picpath4;
	}
	public void setPicpath4(String picpath4) {
		this.picpath4 = picpath4;
	}
	public String getPicpath5() {
		return picpath5;
	}
	public void setPicpath5(String picpath5) {
		this.picpath5 = picpath5;
	}
	public String getPicpath6() {
		return picpath6;
	}
	public void setPicpath6(String picpath6) {
		this.picpath6 = picpath6;
	}
	public String getMp3Path() {
		return mp3Path;
	}
	public void setMp3Path(String mp3Path) {
		this.mp3Path = mp3Path;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return String
				.format("SelectAll [id=%s, title=%s, year=%s, month=%s, day=%s, hour=%s, minute=%s, content=%s, picpath1=%s, picpath2=%s, picpath3=%s, picpath4=%s, picpath5=%s, picpath6=%s, mp3Path=%s, mp3Title=%s, mp3Singer=%s, mp3AlbumArtID=%s, addr=%s, latitude=%s, longtude=%s, url=%s]",
						id, title, year, month, day, hour, minute, content,
						picpath1, picpath2, picpath3, picpath4, picpath5,
						picpath6, mp3Path, mp3Title, mp3Singer, mp3AlbumArtID,
						addr, latitude, longtude, url);
	}
	
	
	
}
