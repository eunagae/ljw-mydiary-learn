package kr.hkit.mydiary.write;

import android.widget.ImageView;

public class PictureInfo {
	private ImageView picture;
	private String picturePath;
	
	public PictureInfo(ImageView picture, String picturePath) {
		super();
		this.picture = picture;
		this.picturePath = picturePath;
	}
	public PictureInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ImageView getPicture() {
		return picture;
	}
	public void setPicture(ImageView picture) {
		this.picture = picture;
	}
	public String getPicturePath() {
		return picturePath;
	}
	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}
	
	
}
