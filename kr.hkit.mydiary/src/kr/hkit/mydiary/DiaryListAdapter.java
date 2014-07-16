package kr.hkit.mydiary;

import java.io.File;
import java.util.ArrayList;

import kr.hkit.mydiary.sqllite.SelectForList;

import com.example.kr.hkit.mydiary.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
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

	public DiaryListAdapter(Context mContext,
			ArrayList<SelectForList> listInfo, int layout) {
		this.mContext = mContext;
		this.listInfo = listInfo;
		this.layout = layout;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

		if (convertView == null) {
			convertView = inflater.inflate(layout, parent, false);
		}

		// 목록 사진설정. 일기에 저장된 사진이 있으면 그 이미지를 띄워주고, 없으면 디폴트 이미지 띄워줌.
		img = (ImageView) convertView.findViewById(R.id.diary_list_img);

		
		if (listInfo.get(position).getPicPath()==null) {
			img.setImageResource(R.drawable.default_img);
		} else {
			String picPath = listInfo.get(position).getPicPath().toString();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1/4;

			Uri uri = Uri.fromFile(new File(picPath));
			Bitmap bmp = BitmapFactory.decodeFile(uri.getPath());

			img.setImageBitmap(bmp);
		}
		
		title = (TextView) convertView.findViewById(R.id.diary_list_title);
		subtitle = (TextView) convertView.findViewById(R.id.diary_list_subtitle);
		if(listInfo.get(position).getTitle()==null){
			title.setText("무제");		//@string 로 고치자
		}else{
			title.setText(listInfo.get(position).getTitle().toString());
		}
		
		subtitle.setText(listInfo.get(position).getSubtitle().toString());
		return convertView;
	}

}
