package com.example.kr.hkit.mydiary.write;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kr.hkit.mydiary.R;

public class ImagePicker extends Activity {
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;

	private Uri mImageCaptureUri;
	private ArrayList<PictureInfo> picList = new ArrayList<PictureInfo>();
	private ArrayList<String> picPathList = new ArrayList<String>();
	private int pictureCount;

	//

	public int getPictureCount() {
		return pictureCount;
	}

	public void setPictureCount(int pictureCount) {
		this.pictureCount = pictureCount;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagepicker);
		
		for(int i =0; i<6; i++){
			PictureInfo info = new PictureInfo(null,"");
			picList.add(info);
			picPathList.add(null);
		}
		
		picList.get(0).setPicture((ImageView) findViewById(R.id.imagepicker_ImageView1));
		picList.get(1).setPicture((ImageView) findViewById(R.id.imagepicker_ImageView2));
		picList.get(2).setPicture((ImageView) findViewById(R.id.imagepicker_ImageView3));
		picList.get(3).setPicture((ImageView) findViewById(R.id.imagepicker_ImageView4));
		picList.get(4).setPicture((ImageView) findViewById(R.id.imagepicker_ImageView5));
		picList.get(5).setPicture((ImageView) findViewById(R.id.imagepicker_ImageView6));
		
		// 이미 등록된 사진이 있는경우 화면에 다시 띄워줌
		Intent intent  = getIntent();
		if(intent.getStringArrayListExtra("PicPathFromWD") != null){
			picPathList = intent.getStringArrayListExtra("PicPathFromWD");
			for(int i=0; i<picPathList.size(); i++){
				
				picList.get(i).setPicturePath(picPathList.get(i));
				Log.d("imagepicker", picPathList.get(i)+" ");
				
				BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
				
                Uri uri = Uri.fromFile(new File(picList.get(i).getPicturePath()));
            	Bitmap bmp = BitmapFactory.decodeFile(uri.getPath());
            	
            	picList.get(i).getPicture().setImageBitmap(bmp);
			}  
		}		
	}
	
	public String getRealPathFromURI(Uri contentUri) {
		 
        String[] proj = { MediaStore.Images.Media.DATA };
        
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        Uri uri = Uri.fromFile(new File(path));
        
        Log.d("imagepicker", "getRealPathFromURI(), path : " + uri.toString());        
        
        cursor.close();
        return path;
    }


	/**
	 * 카메라에서 이미지 가져오기
	 */
	private void doTakePhotoAction() {
		/*
		 * 참고 해볼곳 http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
		 * http://stackoverflow
		 * .com/questions/1050297/how-to-get-the-url-of-the-captured-image
		 * http://www.damonkohler.com/2009/02/android-recipes.html
		 * http://www.firstclown.us/tag/android/
		 */

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// 임시로 사용할 파일의 경로를 생성
		String url = "tmp_" + String.valueOf(System.currentTimeMillis())+ ".jpg";
		mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
		intent.putExtra("path", mImageCaptureUri.getPath());

		startActivityForResult(intent, PICK_FROM_CAMERA);
	}

	/**
	 * 앨범에서 이미지 가져오기
	 */
	private void doTakeAlbumAction() {
		// 앨범 호출
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, PICK_FROM_ALBUM);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
		
			return;
		}
		
		// 임시 이미지만든거랑 경로를 받아옴.
		final Bundle extras = data.getExtras();

		switch (requestCode) {
		/*
		 * // 임시 파일 삭제 File f = new File(mImageCaptureUri.getPath());
		 * if(f.exists()) { f.delete(); }
		 */
		case PICK_FROM_ALBUM: {
			
			// Cursor -> String -> Uri 클래스로 값을 넘기면서 이미지 파일의 경로를 얻는다.
		    // 그냥 intent.getString() 할 경우 SDCard내의 이미지 경로가 얻어지지 않는다.
			// 절대 경로를 얻음.
		      Cursor c = getContentResolver().query(data.getData(), null, null, null, null);
		      c.moveToNext();
		      String picturePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
		      Uri uri = Uri.fromFile(new File(picturePath));
		      c.close();
		      
		      // 얻어낸 경로로 비트맵 생성
		      Bitmap bmp = BitmapFactory.decodeFile(uri.getPath());
			
				picList.get(getPictureCount()).getPicture().setImageBitmap(bmp);	//imageView에 사진 띄움
				picList.get(getPictureCount()).setPicturePath(picturePath);						//사진경로.
				Toast.makeText(ImagePicker.this, picturePath, 0).show();
				break;
			
		}

		case PICK_FROM_CAMERA: {
			Bitmap photo = extras.getParcelable("data");
			//pictureLists.get(getPictureCount()).setImageBitmap(photo);

			break;
		}

		
		}// end of switch
	}// end of method onActivityResult()

	public void mOnClick(View v) {
		
		// 각각의 이미지뷰를 따로 추가, 삭제 해줘야 하므로 클릭시
		// 클릭했는 값을 받아옴
		// public void onClick(DialogInterface dialog, int which) { --->which가
		// 그건줄 알았는데
		// 이상한값이 튀어나와서 따로처리함
		if (v.getId() == R.id.imagepicker_ImageView1)
			setPictureCount(0);
		if (v.getId() == R.id.imagepicker_ImageView2)
			setPictureCount(1);
		if (v.getId() == R.id.imagepicker_ImageView3)
			setPictureCount(2);
		if (v.getId() == R.id.imagepicker_ImageView4)
			setPictureCount(3);
		if (v.getId() == R.id.imagepicker_ImageView5)
			setPictureCount(4);
		if (v.getId() == R.id.imagepicker_ImageView6)
			setPictureCount(5);

		// 확인 버튼
		if (v.getId() == R.id.imagepicker_submit_btn) {
			Intent intent = new Intent();
			
			for(int i =0; i<picList.size(); i++){
				
					picPathList.set(i, picList.get(i).getPicturePath());
					
			}
			
			intent.putStringArrayListExtra("PicturesPath", picPathList);
			
			this.setResult(RESULT_OK, intent);
			finish();
			return;
		}

		DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				doTakePhotoAction();
			}
		};

		DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				doTakeAlbumAction();
			}
		};

		DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				picList.get(getPictureCount()).getPicture().setImageBitmap(null);
				picList.get(getPictureCount()).setPicturePath("");
			}
		};

		new AlertDialog.Builder(this).setTitle("업로드할 이미지 선택")
				// .setPositiveButton("사진촬영", cameraListener)
				.setNeutralButton("사진 가져오기", albumListener)
				.setNegativeButton("삭제", deleteListener).show();
	}// end of method mOnClick

}
