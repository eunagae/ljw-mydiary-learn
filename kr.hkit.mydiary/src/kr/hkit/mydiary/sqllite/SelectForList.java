package kr.hkit.mydiary.sqllite;

public class SelectForList {
	private int id;
	private String title;
	private String subtitle;
	private String picPath;
	
	public SelectForList(int id, String title, String subtitle, String picPath) {
		super();
		this.id = id;
		this.title = title;
		this.subtitle = subtitle;
		this.picPath = picPath;
	}
	public SelectForList() {
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
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	@Override
	public String toString() {
		return String.format(
				"SelectForList [id=%s, title=%s, subtitle=%s, picPath=%s]", id,
				title, subtitle, picPath);
	}
	
	
	
}
