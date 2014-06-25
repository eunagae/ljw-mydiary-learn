package com.example.kr.hkit.mydiary.write;

import com.example.kr.hkit.mydiary.R;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class WriteDiary extends Activity {
	Button dayPickerBtn;
	Button datePickerBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar();
		ab.hide();
		setContentView(R.layout.write_diary);
		
		dayPickerBtn = (Button) findViewById(R.id.write_diary_daypicker_btn);
		datePickerBtn = (Button) findViewById(R.id.write_diary_datepicker_btn);
		
		
	}
}
