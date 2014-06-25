package com.example.kr.hkit.mydiary.write;

import java.io.File;
import java.util.ArrayList;

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

import com.example.kr.hkit.mydiary.R;

public class ImagePicker extends Activity{
	private static final int PICK_FROM_CAMERA = 0;
	  private static final int PICK_FROM_ALBUM = 1;
	  private static final int CROP_FROM_CAMERA = 2;
	 
	  private Uri mImageCaptureUri;
	  private ArrayList<ImageView> pictureLists = new ArrayList<ImageView>();
	 // 
	 
	  @Override
	  public void onCreate(Bundle savedInstanceState)
	  {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.imagepicker);
	 
	   // 
	    pictureLists.add((ImageView) findViewById(R.id.imagepicker_ImageView1));
	 
	    //mButton.setOnClickListener((OnClickListener) this);
	  }
	 
	  /**
	   * 카메라에서 이미지 가져오기
	   */
	  private void doTakePhotoAction()
	  {
	    /*
	     * 참고 해볼곳
	     * http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
	     * http://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured-image
	     * http://www.damonkohler.com/2009/02/android-recipes.html
	     * http://www.firstclown.us/tag/android/
	     */
	 
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	 
	    // 임시로 사용할 파일의 경로를 생성
	    String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
	    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
	 
	    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
	    // 특정기기에서 사진을 저장못하는 문제가 있어 다음을 주석처리 합니다.
	    //intent.putExtra("return-data", true);
	    startActivityForResult(intent, PICK_FROM_CAMERA);
	  }
	 
	  /**
	   * 앨범에서 이미지 가져오기
	   */
	  private void doTakeAlbumAction()
	  {
	    // 앨범 호출
	    Intent intent = new Intent(Intent.ACTION_PICK);
	    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
	    startActivityForResult(intent, PICK_FROM_ALBUM);
	  }
	 
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data)
	  {
	    if(resultCode != RESULT_OK)
	    {
	      return;
	    }
	 
	    switch(requestCode)
	    {
	
	    case CROP_FROM_CAMERA:
	      {
	        // 크롭이 된 이후의 이미지를 넘겨 받습니다.
	        // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
	        // 임시 파일을 삭제합니다.
	        final Bundle extras = data.getExtras();
	 
	        if(extras != null)
	        {
	          Bitmap photo = extras.getParcelable("data");
	          mPhotoImageView.setImageBitmap(photo);
	        }
	 
	        // 임시 파일 삭제
	        File f = new File(mImageCaptureUri.getPath());
	        if(f.exists())
	        {
	          f.delete();
	        }
	 
	        break;
	      }
	 
	    
	      case PICK_FROM_ALBUM:
	      {

	        mPhotoImageView.setImageURI(data.getData());
	        break;
	      }
	 
	      case PICK_FROM_CAMERA:
	      {
	    	  mPhotoImageView.setImageURI(data.getData());
	   
	        break;
	      }
	    }//end of switch 
	  }//end of method onActivityResult()
	 
	  public void mOnClick(View v)
	  {
		 
	    DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener()
	    {
	      @Override
	      public void onClick(DialogInterface dialog, int which)
	      {
	        doTakePhotoAction();
	      }
	    };
	 
	    DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener()
	    {
	      @Override
	      public void onClick(DialogInterface dialog, int which)
	      {
	        doTakeAlbumAction();
	      }
	    };
	 
	    DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
	    {
	      @Override
	      public void onClick(DialogInterface dialog, int which)
	      {
	        dialog.dismiss();
	      }
	    };
	 
	    new AlertDialog.Builder(this)
	      .setTitle("업로드할 이미지 선택")
	      .setPositiveButton("사진촬영", cameraListener)
	      .setNeutralButton("앨범선택", albumListener)
	      .setNegativeButton("취소", cancelListener)
	      .show();
	  }// end of method mOnClick
	  
}
