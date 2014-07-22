package kr.hkit.mydiary.read;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import kr.hkit.mydiary.sqllite.DiaryDAO;
import kr.hkit.mydiary.sqllite.SelectAll;
import kr.hkit.mydiary.write.MusicPic;
import kr.hkit.mydiary.write.MusicPic.MusicAdapter;
import kr.hkit.mydiary.write.WriteDiary;

import com.example.kr.hkit.mydiary.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReadDiary extends Activity {
	private static final int FROM_READDIARY = 4;
	DiaryDAO dao;
	private int diaryID;
	private SelectAll diaryInfo;
	MediaPlayer player;
	boolean playState;
	
	ImageButton mp3PlayBtn, mp3StopBtn, mapGoBtn, urlGoBtn;
	
	TextView title, date, content, mp3Title, mp3Singer, addr, url;

	ImageView img1, img2, img3, img4, img5, img6, mp3AlbumArt;
	
	View separator_img, separator_mp3, separator_addr, separator_url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.readdiary);

		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getDataFromReadDiary();
		ViewByIdMustNeed();
		ViewByIdImg();
		ViewByIdMp3();
		ViewByIdMap();
		ViewByIdURL();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.read_barmenu, menu);
				
		restoreActionBar();
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		dao = DiaryDAO.open(ReadDiary.this);
		
		switch(item.getItemId()){
		
		//글 수정
		case R.id.read_edit_barmenu:
			Intent intent = new Intent(ReadDiary.this, WriteDiary.class);
			intent.putExtra("DiaryID", diaryID);
			startActivityForResult(intent, FROM_READDIARY);
			break;
			
		//글 삭제.
		case R.id.read_delete_barmenu:
			
			new AlertDialog.Builder(this).setMessage("삭제 하겠습니까?")
								.setPositiveButton("네", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										boolean isSuccess = false;
										isSuccess = dao.delete(diaryInfo.getId());
										if(isSuccess == true){
											Toast.makeText(ReadDiary.this, "삭제 완료.", 0).show();
											dao.close();
											finish();
										}	
									}
								}).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.cancel();
										
									}
								}).show();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	 
	
	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(R.string.barname);
	}
	/**
	 *  
	 *  여기서 부터는 oncreate() 초기화.
	 *  
	 *  
	 */
	private void getDataFromReadDiary() {
		Intent intent = getIntent();
		diaryID = intent.getIntExtra("diaryID", 0);
		
		dao = DiaryDAO.open(ReadDiary.this);
		diaryInfo = new SelectAll();
		diaryInfo = dao.selectAll(diaryID);

	}

	// 제목, 날짜, 본문 무조건 입력되는것들.
	private void ViewByIdMustNeed() {
		
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
	
	public void mOnClick(View v){
		Intent intent = new Intent(ReadDiary.this, ImageViewer.class);
		
		ArrayList<String> picPathList = new ArrayList<String>();
		if(diaryInfo.getPicpath1() != null){
			picPathList.add(diaryInfo.getPicpath1());
		}
		if(diaryInfo.getPicpath2() != null){
			picPathList.add(diaryInfo.getPicpath2());
		}
		if(diaryInfo.getPicpath3() != null){
			picPathList.add(diaryInfo.getPicpath3());
		}
		if(diaryInfo.getPicpath4() != null){
			picPathList.add(diaryInfo.getPicpath4());
		}
		if(diaryInfo.getPicpath5() != null){
			picPathList.add(diaryInfo.getPicpath5());
		}
		if(diaryInfo.getPicpath6() != null){
			picPathList.add(diaryInfo.getPicpath6());
		}
		
		int imgID = 0;
		switch (v.getId()) {
		case R.id.read_img1:
			imgID = 0;
			intent.putExtra("imgID", imgID);
			break;

		case R.id.read_img2:
			imgID = 1;
			intent.putExtra("imgID", imgID);
			break;
			
		case R.id.read_img3:
			imgID = 2;
			intent.putExtra("imgID", imgID);
			break;
			
		case R.id.read_img4:
			imgID = 3;
			intent.putExtra("imgID", imgID);
			break;
			
		case R.id.read_img5:
			imgID = 4;
			intent.putExtra("imgID", imgID);
			break;
			
		case R.id.read_img6:
			imgID = 5;
			intent.putExtra("imgID", imgID);
			break;
		}
		intent.putStringArrayListExtra("imgList", picPathList);
		startActivity(intent);
	}
	
	
	// 사진 보여주기
	private void ViewByIdImg() {
		img1 = (ImageView) findViewById(R.id.read_img1);
		img2 = (ImageView) findViewById(R.id.read_img2);
		img3 = (ImageView) findViewById(R.id.read_img3);
		img4 = (ImageView) findViewById(R.id.read_img4);
		img5 = (ImageView) findViewById(R.id.read_img5);
		img6 = (ImageView) findViewById(R.id.read_img6);
		separator_img = findViewById(R.id.read_sepataor_img);
		
		if(diaryInfo.getPicpath1() == null){
			img1.setVisibility(View.GONE);
			img2.setVisibility(View.GONE);
			img3.setVisibility(View.GONE);
			img4.setVisibility(View.GONE);
			img5.setVisibility(View.GONE);
			img6.setVisibility(View.GONE);
			separator_img.setVisibility(View.GONE);
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

	//mp3 
	private void ViewByIdMp3() {
		mp3Title = (TextView) findViewById(R.id.read_mp3_title_tv);
		mp3Singer = (TextView) findViewById(R.id.read_mp3_singer_tv);
		mp3PlayBtn = (ImageButton) findViewById(R.id.read_mp3_play_btn);
		mp3AlbumArt = (ImageView) findViewById(R.id.read_albumart_img);
		mp3StopBtn = (ImageButton) findViewById(R.id.read_mp3_stop_btn);
		separator_mp3 = findViewById(R.id.read_separator_mp3);
		
		if(diaryInfo.getMp3Path() == null){
			mp3Title.setVisibility(View.GONE);
			mp3Singer.setVisibility(View.GONE);
			mp3PlayBtn.setVisibility(View.GONE);
			mp3StopBtn.setVisibility(View.GONE);
			separator_mp3.setVisibility(View.GONE);
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
						player.start();
						
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

	//map
	private void ViewByIdMap() {
		addr = (TextView) findViewById(R.id.read_addr_tv);
		mapGoBtn = (ImageButton) findViewById(R.id.read_go_map);
		separator_addr = findViewById(R.id.read_separator_addr);
		
		if(diaryInfo.getAddr() == null){
			addr.setVisibility(View.GONE);
			separator_addr.setVisibility(View.GONE);
			mapGoBtn.setVisibility(View.GONE);
			return;
		}
		
		addr.setText(diaryInfo.getAddr());
		mapGoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				double latitude = Double.parseDouble(diaryInfo.getLatitude());
				double longitude =Double.parseDouble(diaryInfo.getLongtude()); 
						
				String pos = String.format("geo:%f,%f?z=17", latitude, longitude);
				Uri uri = Uri.parse(pos);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
	}
	
	//url
	private void ViewByIdURL() {
		url = (TextView) findViewById(R.id.read_url_tv);
		separator_url = findViewById(R.id.read_separator_url);
		urlGoBtn = (ImageButton) findViewById(R.id.read_go_url);
		
		if(diaryInfo.getUrl() == null){
			url.setVisibility(View.GONE);
			urlGoBtn.setVisibility(View.GONE);
			separator_url.setVisibility(View.GONE);
			return;
		}
		url.setText(diaryInfo.getUrl());
		urlGoBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW, 
						Uri.parse(diaryInfo.getUrl()));
				startActivity(intent); 	
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
