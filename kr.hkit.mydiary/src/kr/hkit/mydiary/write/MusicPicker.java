package kr.hkit.mydiary.write;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.kr.hkit.mydiary.R;

public class MusicPicker extends Activity {
	ContentResolver mCr;
	TextView title;
	TextView singer;
	ListView musicList;
	ImageView albumImg;
	String mp3Path;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musicpicker);
		mCr = getContentResolver();
		title = (TextView)findViewById(R.id.musicpicker_songtitle_tv);
		singer = (TextView) findViewById(R.id.musicpicker_singer_tv);
		albumImg = (ImageView) findViewById(R.id.musicpicker_imgview);
		musicList = (ListView) findViewById(R.id.musicpicker_list);
		
		dumpQuery();
	}
	
	void dumpQuery() {
		StringBuilder result = new StringBuilder();
		Uri uri;

			uri = Audio.Media.EXTERNAL_CONTENT_URI;				
		
		
		Cursor cursor = mCr.query(uri, null, null, null, null);

		// 레코드 목록 출력
		while (cursor.moveToNext()) {
			mp3Path = getColumeValue(cursor, Audio.AudioColumns.DATA);
			title.setText(getColumeValue(cursor, MediaColumns.DISPLAY_NAME));
			singer.setText(getColumeValue(cursor, MediaColumns.TITLE));
			result.append(getColumeValue(cursor, MediaColumns.SIZE));
			result.append(getColumeValue(cursor, MediaColumns.DATE_ADDED));
			result.append(getColumeValue(cursor, MediaColumns.MIME_TYPE));
		}
		cursor.close();

		title.setText(result.toString());
	}
	
	String getColumeValue(Cursor cursor, String cname) {
		String value = cname + " : " +
			cursor.getString(cursor.getColumnIndex(cname)) + "\n";
		return value;
	}
}
