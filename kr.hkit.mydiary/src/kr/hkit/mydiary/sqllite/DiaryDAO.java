package kr.hkit.mydiary.sqllite;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DiaryDAO {
	private DiaryDbHelper helper;
	private SQLiteDatabase db;
	
	private DiaryDAO(Context context){
		helper = new DiaryDbHelper(context);
		db = helper.getWritableDatabase();
		
	}
	
	public static DiaryDAO open(Context context){
		return new DiaryDAO(context);
	}
	
	public void close(){
		helper.close();
	}
	
	public boolean insert(String title, 
			int year, int month, int day, int hour, int minute, 
			String content,
			String picpath1, String picpath2, String picpath3, String picpath4, String picpath5, String picpath6,
			String mp3path, String mp3Title, String mp3Singer, int mp3AlbumArtID,
			String addr, String latitude, String longtude, String url){
/*		String sql = "insert into dic values(null, '"+ eng + "', '"+han+"');";
		try{
			db.execSQL(sql);
		}catch(SQLException e){
			Log.d("WordDAO", "insert Error " + e.getMessage());
			return false;
		}
		return true;*/
		ContentValues row = new ContentValues();
		
		row.put("title", title);
		row.put("year", year);
		row.put("month", month);
		row.put("day", day);
		row.put("hour", hour);
		row.put("minute", minute);
		row.put("content", content);
		row.put("picpath1", picpath1);
		row.put("picpath2", picpath2);
		row.put("picpath3", picpath3);
		row.put("picpath4", picpath4);
		row.put("picpath5", picpath5);
		row.put("picpath6", picpath6);
		row.put("mp3path", mp3path);
		row.put("mp3title", mp3Title);
		row.put("mp3singer", mp3Singer);
		row.put("mp3albumartid", mp3AlbumArtID);
		row.put("addr", addr);
		row.put("latitude", latitude);
		row.put("longtude", longtude);
		row.put("url", url);
		long res = db.insert("diary", null, row);
		if (res == -1) return false;
		else return true;
	}
	
	public boolean deleteAll(){
/*		String sql = "delete from dic;";
		try{
			db.execSQL(sql);
		}catch(SQLException e){
			Log.d("WordDAO", "deleteAll Error " + e.getMessage());
			return false;
		}
		return true;*/
		db.delete("dic", null, null);
		return true;
	}
	
	public boolean delete(int id){
/*		String sql = "delete from dic where _id="+id+";";
		try{
			db.execSQL(sql);
		}catch(SQLException e){
			Log.d("WordDAO", "delete Error " + e.getMessage());
			return false;
		}
		return true;*/
		String[] args = {String.valueOf(id)};
		db.delete("dic", "_id=?", args);
		return true;
	}
	
	public ArrayList<SelectForList> selectForList(){
		String sql = "Select _id, title, content, picpath1 from diary;";
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<SelectForList> listinfo = new ArrayList<SelectForList>();
		
		while(cursor.moveToNext()){
			int id = cursor.getInt(0);
			String title = cursor.getString(1);
			String subtitle = cursor.getString(2);
			String picpath = cursor.getString(3);
			
			if(subtitle.length() > 20){
				subtitle.subSequence(0, 19);
			}
			listinfo.add(new SelectForList(id, title, subtitle, picpath));
		}
		cursor.close();
		return listinfo;
	}
	
	// 흠흠 개판이구만 디비공부점 해야할듯
	public SelectAll selectAll(int _id){
		
		String sql = "Select _id, title, year,	month, day, hour, minute,	content, " +
				"picpath1,	picpath2, picpath3, picpath4,	picpath5, picpath6, " +
				"mp3path, mp3title, mp3singer, mp3albumartid," +
				" addr, latitude, longtude,url  " +
				"from diary " +
				"where _id ="+_id+ ";" ;
		
		Cursor cursor = db.rawQuery(sql, null);
		SelectAll diaryInfo = new SelectAll();
		
		while(cursor.moveToNext()){
			int id = cursor.getInt(0);
			String title = cursor.getString(1);
			int year = cursor.getInt(2);
			int month = cursor.getInt(3);
			int day = cursor.getInt(4);
			int hour = cursor.getInt(5);
			int minute = cursor.getInt(6);
			String content = cursor.getString(7);
			String picpath1 = cursor.getString(8);
			String picpath2 = cursor.getString(9);
			String picpath3 = cursor.getString(10);
			String picpath4 = cursor.getString(11);
			String picpath5 = cursor.getString(12);
			String picpath6 = cursor.getString(13);
			String mp3Path = cursor.getString(14);
			String mp3Title = cursor.getString(15);
			String mp3Singer = cursor.getString(16);
			int mp3AlbumArtID = cursor.getInt(17);
			String addr = cursor.getString(18); 
			String latitude = cursor.getString(19);
			String longtude = cursor.getString(20);
			String url = cursor.getString(21);
			
			diaryInfo = new SelectAll(id, title,
					year, month, day, hour, minute, 
					content, 
					picpath1, picpath2, picpath3, picpath4, picpath5, picpath6, 
					mp3Path, mp3Title, mp3Singer, mp3AlbumArtID,
					addr, latitude, longtude, 
					url);
			}
		cursor.close();
		return diaryInfo;
		
	}
	
	public boolean update(int id, String eng, String han){
		String sql = "update dic set han='"+han+"', eng = '"+eng+"' where _id ="+id+";";
		try{
			db.execSQL(sql);
		}catch(SQLException e){
			Log.d("WordDAO", "update Error " + e.getMessage());
			return false;
		}
		return true;
	}
	
}

