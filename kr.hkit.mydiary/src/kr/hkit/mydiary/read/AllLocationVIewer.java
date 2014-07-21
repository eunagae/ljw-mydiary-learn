package kr.hkit.mydiary.read;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

import java.util.ArrayList;

import kr.hkit.mydiary.sqllite.DiaryDAO;
import kr.hkit.mydiary.sqllite.SelectForAllLocation;
import kr.hkit.mydiary.write.GoogleMapkiUtil;
import android.R.integer;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.kr.hkit.mydiary.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.MapController;

public class AllLocationVIewer extends FragmentActivity {
	ImageButton searchBtn;

	public static final int FROM_LOCATIONPIC = 3;
	public static LatLng DEFAULT_GP = new LatLng(37.566500, 126.978000);// 서울

	protected GoogleMap mMap;
	private ProgressDialog progressDialog;
	private ImageButton searchBt;
	private GoogleMapkiUtil httpUtil;
	private AlertDialog errorDialog;
	DiaryDAO dao;
	ArrayList<SelectForAllLocation> allLocationInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationpicker);
		searchBtn = (ImageButton) findViewById(R.id.locationpicker_searchBt);
		searchBtn.setVisibility(View.GONE);
		
		setUpMapIfNeeded();

		// 서울로 설정.
		CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(DEFAULT_GP, 10);
		mMap.animateCamera(cu);

		allLocationInfo = new ArrayList<SelectForAllLocation>();
		dao = DiaryDAO.open(AllLocationVIewer.this);
		allLocationInfo = dao.selectForAllLocation();

		addMaker();
		Listener();
	}

	private void Listener() {
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				Intent intent = new Intent(AllLocationVIewer.this, ReadDiary.class);
				int convertID = Integer.parseInt(marker.getId().substring(1));
				intent.putExtra("diaryID", allLocationInfo.get(convertID).getId());
			
				startActivity(intent);
				finish();
			}
		});
		
	}

		// 맵 있는지없는지 확인
		private void setUpMapIfNeeded() {
			MapController mControl;
			if (mMap == null) {
				mMap = ((SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.locationpicker_map)).getMap();

				if (mMap != null) {
					setUpMap();
				}
			}
		}

		// 로딩
		private void setUpMap() {
			Log.d("dd", "setUpMap");
			mMap.setMapType(MAP_TYPE_NORMAL);

			mMap.setMyLocationEnabled(true);
		}
		
		
	
	private void addMaker() {

		for (int i = 0; i < allLocationInfo.size(); i++) {
			LatLng latlng = new LatLng(
					Double.parseDouble(allLocationInfo.get(i).getLatitude()),
					Double.parseDouble(allLocationInfo.get(i).getLongtude()));

			mMap.addMarker(
					new MarkerOptions().position(latlng)
							
							.title(allLocationInfo.get(i).getTitle()+" / "+allLocationInfo.get(i).getAddr())
							.icon(BitmapDescriptorFactory.defaultMarker()))
					.showInfoWindow();
		}

	}
	
	

}
