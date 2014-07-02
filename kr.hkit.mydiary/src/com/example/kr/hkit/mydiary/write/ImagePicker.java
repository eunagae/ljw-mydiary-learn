package com.example.kr.hkit.mydiary.write;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kr.hkit.mydiary.R;

public class ImagePicker extends Activity {
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;

	
	private Uri mImageCaptureUri;
	private LinkedList<ImageView>  pictureLists  = new LinkedList<ImageView>();
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

		
		pictureLists.add((ImageView) findViewById(R.id.imagepicker_ImageView1));
		pictureLists.add((ImageView) findViewById(R.id.imagepicker_ImageView2));
		pictureLists.add((ImageView) findViewById(R.id.imagepicker_ImageView3));
		pictureLists.add((ImageView) findViewById(R.id.imagepicker_ImageView4));
		pictureLists.add((ImageView) findViewById(R.id.imagepicker_ImageView5));
		pictureLists.add((ImageView) findViewById(R.id.imagepicker_ImageView6));
		
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

		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				mImageCaptureUri);
		intent.putExtra("path", mImageCaptureUri.getPath());
		
		// 특정기기에서 사진을 저장못하는 문제가 있어 다음을 주석처리 합니다.
		// intent.putExtra("return-data", true);
		startActivityForResult(intent, PICK_FROM_CAMERA);
	}

	/**
	 * 앨범에서 이미지 가져오기
	 */
	private void doTakeAlbumAction() {
		// 앨범 호출
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		// intent.putExtra("path", mImageCaptureUri.getPath());
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
			String picturePath = data.getData().getPath();
			pictureLists.get(getPictureCount()).setImageURI(data.getData());
			//addPictureCount();
			Toast.makeText(ImagePicker.this, picturePath, 0).show();
			break;
		}

		case PICK_FROM_CAMERA: {
			Bitmap photo = extras.getParcelable("data");
			pictureLists.get(0).setImageBitmap(photo);
			
			break;
		}
		}// end of switch
	}// end of method onActivityResult()

	public void mOnClick(View v) {

		DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				doTakePhotoAction();
			}
		};

		DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				setPictureCount(which);
				doTakeAlbumAction();
			}
		};

		DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(ImagePicker.this, Integer.toString(which), 0).show();
				pictureLists.get(which).setImageBitmap(null);
			}
		};

		new AlertDialog.Builder(this).setTitle("업로드할 이미지 선택")
				// .setPositiveButton("사진촬영", cameraListener)
				.setNeutralButton("사진 가져오기", albumListener)
				.setNegativeButton("삭제", deleteListener)
				.show();
	}// end of method mOnClick

}
