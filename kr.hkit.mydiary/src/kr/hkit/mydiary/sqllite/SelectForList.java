package kr.hkit.mydiary.sqllite;

public class SelectForList {
	private String title;
	private String subtitle;
	private String picPath;
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
	public SelectForList(String title, String subtitle, String picPath) {
		super();
		this.title = title;
		this.subtitle = subtitle;
		this.picPath = picPath;
	}
	public SelectForList() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return String.format(
				"SelectForList [title=%s, subtitle=%s, picPath=%s]", title,
				subtitle, picPath);
	}
	
	
}
