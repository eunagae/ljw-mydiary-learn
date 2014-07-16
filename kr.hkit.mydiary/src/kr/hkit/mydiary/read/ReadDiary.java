package kr.hkit.mydiary.read;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import kr.hkit.mydiary.sqllite.DiaryDAO;
import kr.hkit.mydiary.sqllite.SelectAll;
import kr.hkit.mydiary.write.MusicPic;
import kr.hkit.mydiary.write.MusicPic.MusicAdapter;

import com.example.kr.hkit.mydiary.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ReadDiary extends Activity {
	DiaryDAO dao;
	private int diaryID;
	private SelectAll diaryInfo;
	MediaPlayer player;
	boolean playState;
	
	ImageButton editDiary, deleteDiary, mp3PlayBtn, mp3StopBtn;
	
	TextView title, date, content, mp3Title, mp3Singer, addr, url;

	ImageView img1, img2, img3, img4, img5, img6, mp3AlbumArt;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.readdiary);

		getDataFromReadDiary();
		ViewByIdMustNeed();
		ViewByIdImg();
		ViewByIdMp3();
	}

	private void getDataFromReadDiary() {
		Intent intent = getIntent();
		diaryID = intent.getIntExtra("diaryID", 0);
		
		dao = DiaryDAO.open(ReadDiary.this);
		diaryInfo = new SelectAll();
		diaryInfo = dao.selectAll(diaryID);

	}

	private void ViewByIdMustNeed() {
		//필수요소
		editDiary = (ImageButton) findViewById(R.id.read_edit_btn);
		deleteDiary = (ImageButton) findViewById(R.id.read_delete_btn);
		
		//제목
		title = (TextView) findViewById(R.id.read_title_tv);
		title.setText(diaryInfo.getTitle().toString());
		
		//날짜
		date = (TextView) findViewById(R.id.read_date_tv);
		String datetime = String.format("%d.%d.%d. %d:%d", diaryInfo.getYear(),diaryInfo.getMonth(), diaryInfo.getDay(), diaryInfo.getHour(), diaryInfo.getMinute());
		date.setText(datetime);
		
		//본문
		content = (TextView) findViewById(R.id.read_content_tv);
		content.setText(diaryInfo.getContent());
		
	}
	
	// 사진 보여주기
	private void ViewByIdImg() {
		img1 = (ImageView) findViewById(R.id.read_img1);
		img2 = (ImageView) findViewById(R.id.read_img2);
		img3 = (ImageView) findViewById(R.id.read_img3);
		img4 = (ImageView) findViewById(R.id.read_img4);
		img5 = (ImageView) findViewById(R.id.read_img5);
		img6 = (ImageView) findViewById(R.id.read_img6);
		
		if(diaryInfo.getPicpath1() == null){
			img1.setVisibility(View.GONE);
			img2.setVisibility(View.GONE);
			img3.setVisibility(View.GONE);
			img4.setVisibility(View.GONE);
			img5.setVisibility(View.GONE);
			img6.setVisibility(View.GONE);
			return;
		}
			
			Bitmap bmp = getImgFromPath(diaryInfo.getPicpath1());
			img1.setImageBitmap(bmp);

		if(diaryInfo.getPicpath2() == null){
			img2.setVisibility(View.GONE);
			img3.setVisibility(View.GONE);
			img4.setVisibility(View.GONE);
			img5.setVisibility(View.GONE);
			img6.setVisibility(View.GONE);
			return;
		}
	
		bmp = getImgFromPath(diaryInfo.getPicpath2());
		img2.setImageBitmap(bmp);
		
		if(diaryInfo.getPicpath3() == null){
			img3.setVisibility(View.GONE);
			img4.setVisibility(View.GONE);
			img5.setVisibility(View.GONE);
			img6.setVisibility(View.GONE);
			return;
		}
		
		bmp = getImgFromPath(diaryInfo.getPicpath3());
		img3.setImageBitmap(bmp);
		
		if(diaryInfo.getPicpath4() == null){
			img4.setVisibility(View.GONE);
			img5.setVisibility(View.GONE);
			img6.setVisibility(View.GONE);
			return;
		}
		
		bmp = getImgFromPath(diaryInfo.getPicpath4());
		img4.setImageBitmap(bmp);
		
		if(diaryInfo.getPicpath5() == null){
			img5.setVisibility(View.GONE);
			img6.setVisibility(View.GONE);
			return;
		}
		
		bmp = getImgFromPath(diaryInfo.getPicpath5());
		img5.setImageBitmap(bmp);
		
		if(diaryInfo.getPicpath6() == null){
			img6.setVisibility(View.GONE);
			return;
		}
		
		bmp = getImgFromPath(diaryInfo.getPicpath6());
		img6.setImageBitmap(bmp);
		
	}
	
	private Bitmap getImgFromPath(String path){
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1/4;

		Uri uri = Uri.fromFile(new File(path));
		Bitmap bmp = BitmapFactory.decodeFile(uri.getPath());
		return bmp;
	}

	private void ViewByIdMp3() {
		mp3Title = (TextView) findViewById(R.id.read_mp3_title_tv);
		mp3Singer = (TextView) findViewById(R.id.read_mp3_singer_tv);
		mp3PlayBtn = (ImageButton) findViewById(R.id.read_mp3_play_btn);
		mp3AlbumArt = (ImageView) findViewById(R.id.read_albumart_img);
		mp3StopBtn = (ImageButton) findViewById(R.id.read_mp3_stop_btn);
		
		if(diaryInfo.getMp3Path() == null){
			mp3Title.setVisibility(View.GONE);
			mp3Singer.setVisibility(View.GONE);
			mp3PlayBtn.setVisibility(View.GONE);
			mp3StopBtn.setVisibility(View.GONE);
			return;
		}
	
		mp3Title.setText(diaryInfo.getMp3Title());
		mp3Singer.setText(diaryInfo.getMp3Singer());
		//앨범 아트 불러오는거 안댐
		Bitmap albumArt = MusicPic.getArtworkQuick(ReadDiary.this, diaryInfo.getMp3AlbumArtID(), 50, 50);
        mp3AlbumArt.setImageBitmap(albumArt);
        player = new MediaPlayer();
        playState = false;
        mp3PlayBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (playState ==false){
						player.setDataSource(diaryInfo.getMp3Path());
						player.prepare();
						player.start();
						playState = true;
					}else{
						player.release();
						
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
        
        mp3StopBtn.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				player.pause();
			}
		});
	}

	 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(player !=null){
			player.release();
			player = null;
		}
	}
}
