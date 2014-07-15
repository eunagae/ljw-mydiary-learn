package kr.hkit.mydiary;

import java.util.ArrayList;
import java.util.zip.Inflater;

import kr.hkit.mydiary.sqllite.SelectForList;

import com.example.kr.hkit.mydiary.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DiaryListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	ArrayList<SelectForList> listInfo;
	int layout;
	
	private TextView title;
	private TextView subtitle;
	private ImageView img;
	
	
	
	public DiaryListAdapter(Context mContext, ArrayList<SelectForList> listInfo, int layout) {
		this.mContext = mContext;
		this.listInfo = listInfo;
		this.layout = layout;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return listInfo.size();
	}

	@Override
	public Object getItem(int position) {
		return listInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
        if (convertView == null){
        	convertView = 
        }
		return null;
	}

}
