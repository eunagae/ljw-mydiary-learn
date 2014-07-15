package kr.hkit.mydiary.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DiaryDbHelper extends SQLiteOpenHelper {
	public final static String DB_NAME = "EngWord.db";
	public final static int DB_VERSION = 1;
	public final static String TAG = "SQLiteOpenHelper";
	
	public DiaryDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		Log.d(TAG, "WordDbHelper()");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table diary ( "
				+ "_id integer primary key autoincrement, "
				+ "title text, "
				+ "year int, "
				+ "month int, "
				+ "day int, "
				+ "hour int, "
				+ "minute int, "
				+ "content text, "
				+ "picpath1 text, "
				+ "picpath2 text, "
				+ "picpath3 text, "
				+ "picpath4 text, "
				+ "picpath5 text, "
				+ "picpath6 text, "
				+ "mp3path text, "
				+ "addr text, "
				+ "latitude text, "
				+ "longtude text, "
				+ "url text"
				+ ");";
		Log.d(TAG, "onCreate() "+ sql);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "onUpgrade() oldVersion = " + oldVersion + " newVersion = " + newVersion);
		db.execSQL("drop table if exists diary");
		onCreate(db);
	}

}
