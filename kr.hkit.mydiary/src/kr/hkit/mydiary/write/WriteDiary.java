package kr.hkit.mydiary.write;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import kr.hkit.mydiary.sqllite.DiaryDAO;
import kr.hkit.mydiary.sqllite.DiaryDbHelper;

import com.example.kr.hkit.mydiary.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class WriteDiary extends Activity {
	public static final int FROM_IMAGEPIC =1;
	public static final int FROM_MUSICPIC = 2;
	public static final int FROM_LOCATIONPIC = 3;
	
	private DiaryDbHelper helper;
	private DiaryDAO dao;
	
	private ArrayList<String> picturePathLists = new ArrayList<String>();	//사진리스트.
	private String musicPath;				// mp3 파일 경로.
	private AddInfo addr;					// 주소, 경도, 위도.
	private String url;						// url.
	private Button dayPickerBtn;
	private Button datePickerBtn;
	private EditText title;
	private EditText content;
	
	String[] picPath = new String[6];
	
	
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
		
		helper = new DiaryDbHelper(this);
		dao = DiaryDAO.open(this);
		
		dayPickerBtn = (Button) findViewById(R.id.write_diary_daypicker_btn);
		datePickerBtn = (Button) findViewById(R.id.write_diary_datepicker_btn);
		title = (EditText) findViewById(R.id.write_diary_title_edit);
		content = (EditText) findViewById(R.id.write_diary_context_edit);
		
		Calendar cal = new GregorianCalendar();
		mYear = cal.get(Calendar.YEAR);
		mMonth = cal.get(Calendar.MONTH);
		mDay = cal.get(Calendar.DAY_OF_MONTH);
		mHour = cal.get(Calendar.HOUR_OF_DAY);
		mMinute = cal.get(Calendar.MINUTE);
		
		addr = new AddInfo(null, null, null);
		UpdateNow();
	}
	
	public void mOnClick(View v){
		Intent intent = null;
		
		//날짜, 시간 설정
		if(v.getId() ==  R.id.write_diary_daypicker_btn){
			new DatePickerDialog(WriteDiary.this, mDateSetListener, mYear, mMonth, mDay).show();
			return;
		}
		else if(v.getId() == R.id.write_diary_datepicker_btn){
			new TimePickerDialog(WriteDiary.this, mTimeSetListener, mHour, mMinute, true).show();
			return;
		}
			
		//사진 불러오기. startActivityForResult()로 선택한 사진들의 경로를 되돌려받는다.
		else if(v.getId() ==  R.id.write_diary_imgpicker_btn){
			intent = new Intent(WriteDiary.this, ImagePicker.class);
			
			if(picturePathLists.size() != 0){
				intent.putStringArrayListExtra("PicPathFromWD", picturePathLists);
				
				startActivityForResult(intent, 0);
			}else{
				startActivityForResult(intent, 3);
			}
			return;
		}
		//음악 클릭시
		else if(v.getId() ==  R.id.write_diary_musicpicker_btn){
			intent = new Intent(WriteDiary.this, MusicPic.class);
			startActivityForResult(intent, 0);
			return;
		}
		//지도 클릭시
		else if(v.getId() ==  R.id.write_diary_locationpicker_btn){
			intent = new Intent(WriteDiary.this, LocationPicker.class);
			startActivityForResult(intent, 0);
			return;
		
		}
		//URL 
		else if(v.getId() == R.id.write_diary_urllink_btn){
			final LinearLayout linear = (LinearLayout) View.inflate(this, R.layout.url_input, null);
			Log.d("dd", "url");
			new AlertDialog.Builder(this)
			.setTitle("URL 입력")
			.setView(linear)
			.setPositiveButton("확인",  new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					
					EditText urlEdit = (EditText) linear.findViewById(R.id.urlinput);
					url = urlEdit.getText().toString();
					Log.d("dd", url);
				};
			}
			).setNegativeButton("취소", null)
			.show();
			return;
		}
		
		//글 등록
		else if(v.getId() == R.id.write_diary_writesubmit_btn){
			boolean result;
			String str;
			
			if(validCheck() == false){
				Toast.makeText(this, "내용이 없습니다.", 0).show();
				return;
			}else{
				
				
				result = dao.insert(title.getText().toString(), 
										mYear, mMonth, mDay, mHour, mMinute, 
										content.getText().toString(), 
										picPath[0],
										picPath[1],
										picPath[2],
										picPath[3],
										picPath[4],
										picPath[5],
										musicPath, 
										addr.getAddress(), addr.getLatitude(), addr.getLongtude(), 
										url
										);
				
				str = result?"Insert Success" : "Insert Fail";
				return;
			}
		}
	}
	
	// 본문이 입력되지 않으면 글이 등록되지 않음.
	private boolean validCheck() {
		if(content.getText().toString().equals("")){
			return false;
		}
		
		if (picturePathLists.isEmpty() == false) {
			int j =0;
			for (int i = 0; i < picturePathLists.size(); i++) {
				if (picturePathLists.get(i).toString().equals("") == false) {
					picPath[j] = picturePathLists.get(i).toString();
					j++;
				}
			}
			for(int i=j; i<picPath.length; i++){
				picPath[i] = null;
			}
		}else if(picturePathLists.isEmpty()){
			for(int i = 0; i<picPath.length; i++){
				picPath[i] = null;
			}
		}
		
		
		return true;
		
	}

	// ImagePicker의 사진경로값 받아오기
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		
	    // 수행을 제대로 한 경우
		// 1. 사진
	    if(resultCode == FROM_IMAGEPIC && data != null){	    	
	        picturePathLists = data.getStringArrayListExtra("PicturesPath");
	        
	    }
	    // 2. 음악
	    else if(resultCode == FROM_MUSICPIC && data != null){
	    	musicPath = data.getStringExtra("musicPath");
	    	
	    	
	    }
	    // 3. 지도
	    else if(resultCode == FROM_LOCATIONPIC && data !=null){
	    	addr = new AddInfo(data.getStringArrayExtra("addr")[0],data.getStringArrayExtra("addr")[1],data.getStringArrayExtra("addr")[2] );
	    	
	    }
	    // 수행을 제대로 하지 못한 경우
	    else if(resultCode == RESULT_CANCELED){
	         Toast.makeText(this, "취소", 0).show();
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
