package kr.hkit.mydiary.write;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.example.kr.hkit.mydiary.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class WriteDiary extends Activity {
	private ArrayList<String> picturePathLists = new ArrayList<String>();
	private Button dayPickerBtn;
	private Button datePickerBtn;
	
	//날짜
	int mYear, mMonth, mDay, mHour, mMinute;

	
	
	public ArrayList<String> getPicturePathLists() {
		return picturePathLists;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar();
		ab.hide();
		setContentView(R.layout.write_diary);
		
		dayPickerBtn = (Button) findViewById(R.id.write_diary_daypicker_btn);
		datePickerBtn = (Button) findViewById(R.id.write_diary_datepicker_btn);
		
		Calendar cal = new GregorianCalendar();
		mYear = cal.get(Calendar.YEAR);
		mMonth = cal.get(Calendar.MONTH);
		mDay = cal.get(Calendar.DAY_OF_MONTH);
		mHour = cal.get(Calendar.HOUR_OF_DAY);
		mMinute = cal.get(Calendar.MINUTE);
		
		UpdateNow();
	}
	
	public void mOnClick(View v){
		Intent intent = null;
		
		switch(v.getId()){
		case R.id.write_diary_daypicker_btn:
			new DatePickerDialog(WriteDiary.this, mDateSetListener, mYear, mMonth, mDay).show();
			break;
			
		case R.id.write_diary_datepicker_btn:
			new TimePickerDialog(WriteDiary.this, mTimeSetListener, mHour, mMinute, true).show();
			break;
			
			
		//사진 불러오기. startActivityForResult()로 선택한 사진들의 경로를 되돌려받는다.
		case R.id.write_diary_imgpicker_btn:
			intent = new Intent(WriteDiary.this, ImagePicker.class);
			
			if(picturePathLists.size() != 0){
				intent.putStringArrayListExtra("PicPathFromWD", picturePathLists);
				Log.d("imagepicker", "putStringArrayListExtra");
				startActivityForResult(intent, 0);
			}else{
				startActivityForResult(intent, 3);
			}
			break;
			
		//url 링크
		case R.id.write_diary_urllink_btn:
			
			break;
		
		//음악 클릭시
		case R.id.write_diary_musicpicker_btn:
			intent = new Intent(WriteDiary.this, MusicPicker.class);
			startActivityForResult(intent, 0);
			break;
			
		//지도 클릭시
		case R.id.write_diary_locationpicker_btn:
			intent = new Intent(WriteDiary.this, LocationPicker.class);
			startActivity(intent);
			break;
		}
		
	}
	
	// ImagePicker의 사진경로값 받아오기
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
	     
	    // 수행을 제대로 한 경우
	    if(resultCode == RESULT_OK && data != null)
	    {
	    	
	        picturePathLists = data.getStringArrayListExtra("PicturesPath");
	      
	        Toast.makeText(WriteDiary.this, picturePathLists.get(0), Toast.LENGTH_LONG).show();

	    }
	    // 수행을 제대로 하지 못한 경우
	    else if(resultCode == RESULT_CANCELED)
	    {
	         Toast.makeText(this, "사진 가져오기 취소", 0).show();
	    }
	
	};

	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			UpdateNow();
		}
	};
	
	TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			UpdateNow();
		}
	};

	private void UpdateNow() {
		dayPickerBtn.setText(String.format("%d/%d/%d", mYear, mMonth+1, mDay ));
		datePickerBtn.setText(String.format("%d : %d", mHour, mMinute));
		
	}
	
}
