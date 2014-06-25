package com.example.kr.hkit.mydiary.write;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.example.kr.hkit.mydiary.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class WriteDiary extends Activity {
	Button dayPickerBtn;
	Button datePickerBtn;
	
	//날짜
	int mYear, mMonth, mDay, mHour, mMinute;

	
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
			
		case R.id.write_diary_imgpicker_btn:
			intent = new Intent(WriteDiary.this, ImagePicker.class);
			startActivity(intent);
			break;
			
		}
		
	}
	

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
