package kr.hkit.mydiary.read;

import java.io.File;
import java.util.ArrayList;

import kr.hkit.mydiary.forzoom.PhotoViewAttacher;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ImageView;

import com.example.kr.hkit.mydiary.R;

public class ImageViewer extends Activity {
	ImageView view;
	ArrayList<String> picPathList = new ArrayList<String>();
	Intent intent = null;
	int imgCount, imgMaxCount;
	Bitmap bmp;
	
	//이미지 줌에 필요한
	PhotoViewAttacher mAttcher;
	
	//이미지 전환에 필요한
	GestureDetector mDetector;
	final static int DISTANCE = 200;
	final static int VELOCITY = 300;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar();
		ab.hide();
		setContentView(R.layout.imageviewer);
		view = (ImageView) findViewById(R.id.imageviewer_view);

		intent = getIntent();
		picPathList = intent.getStringArrayListExtra("imgList");
		imgCount = intent.getIntExtra("imgID", 0);
		imgMaxCount = picPathList.size();
		
		bmp = getImgFromPath(picPathList.get(imgCount));
		view.setImageBitmap(bmp);
		
		mDetector = new GestureDetector(this, mGestureListener);
		mDetector.setIsLongpressEnabled(false);
		
		mAttcher = new PhotoViewAttacher(view);
		//mAttcher.update();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mDetector.onTouchEvent(event); 
	}

	OnGestureListener mGestureListener = new OnGestureListener() {
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,	
				float velocityY) {
			Log.d("dd", Integer.toString(imgCount));
			if (Math.abs(velocityX) > VELOCITY) {
				if (e1.getX() - e2.getX() > DISTANCE) {
					
					if(imgCount == imgMaxCount){
						imgCount = 0;
					}else{
						imgCount++;
					}
				}
				if (e2.getX() - e1.getX() > DISTANCE) {
					if(imgCount == 0){
						imgCount = imgMaxCount;
					}else{
						imgCount--;
					}
				}
			}
			Log.d("dd", Integer.toString(imgCount));
			bmp = getImgFromPath(picPathList.get(imgCount));
			view.setImageBitmap(bmp);
			return true;
		}

		public boolean onDown(MotionEvent e) {
			return false;
		}

		public void onLongPress(MotionEvent e) {
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		public void onShowPress(MotionEvent e) {
		}

		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}
	};
	

	private Bitmap getImgFromPath(String path) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1 / 4;

		Uri uri = Uri.fromFile(new File(path));
		Bitmap bmp = BitmapFactory.decodeFile(uri.getPath());
		return bmp;
	}
}
