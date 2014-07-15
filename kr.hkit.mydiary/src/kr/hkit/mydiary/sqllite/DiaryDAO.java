package kr.hkit.mydiary.sqllite;

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
			String mp3path, String addr, String latitude, String longtude, String url){
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
	
	public Cursor selectAll(){
		String sql = "Select * from dic;";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor !=null) cursor.moveToFirst();
		return cursor;
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


















