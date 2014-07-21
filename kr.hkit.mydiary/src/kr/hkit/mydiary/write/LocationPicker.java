package kr.hkit.mydiary.write;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.example.kr.hkit.mydiary.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.maps.MapController;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LocationPicker extends FragmentActivity {
	public static final int FROM_LOCATIONPIC = 3;
	public static LatLng DEFAULT_GP = new LatLng(37.566500, 126.978000);// 서울

	protected GoogleMap mMap;
	private ProgressDialog progressDialog;
	private String errorString = "";
	private ImageButton searchBt;
	private GoogleMapkiUtil httpUtil;
	private AlertDialog errorDialog;
	AddInfo addinfo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationpicker);

		setUpMapIfNeeded();
		

		// 서울로 설정.
		CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(DEFAULT_GP, 16);
		mMap.animateCamera(cu);

		// 검색 버튼 숨김
		searchBt = (ImageButton) findViewById(R.id.locationpicker_searchBt);
		searchBt.setVisibility(View.VISIBLE);
		searchBt.setOnClickListener(onNameSearch);

		// httpUtil
		httpUtil = new GoogleMapkiUtil();

		errorDialog = new AlertDialog.Builder(this).setTitle("오류")
				.setMessage(errorString).setPositiveButton("확인", null).create();

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
	
	

	// 검색 아이콘 누르면 검색창 나타남.. 내위치 표시해주면서.
	private View.OnClickListener onNameSearch = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {

			final LinearLayout linear = (LinearLayout) View.inflate(
					LocationPicker.this, R.layout.locationpicker_search, null);
			TextView addrTv = (TextView) linear
					.findViewById(R.id.dialog_map_search_addr);
			Location lo = mMap.getMyLocation();
			addrTv.setText(getAddres(lo.getLatitude(), lo.getLongitude()));

			new AlertDialog.Builder(LocationPicker.this).setTitle("명칭을 입력하세요")
					.setView(linear).setPositiveButton("검색", onClickNameSearch)
					.setNegativeButton("취소", null).show();

		}
	};

	// 검색. GoogleMapkiUtil.java에 메서드 검색기능 구현.
	private DialogInterface.OnClickListener onClickNameSearch = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			AlertDialog ad = (AlertDialog) dialog;
			EditText nameEt = (EditText) ad
					.findViewById(R.id.dialog_map_search_et);
			TextView addrTv = (TextView) ad
					.findViewById(R.id.dialog_map_search_addr);

			if (nameEt.getText().length() > 0) {
				if (progressDialog != null && progressDialog.isShowing())
					return;
				progressDialog = ProgressDialog.show(LocationPicker.this,
						"Wait", "검색 중입니다");

				httpUtil.requestMapSearch(
						new ResultHandler(LocationPicker.this), nameEt
								.getText().toString(), addrTv.getText()
								.toString());

				final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(nameEt.getWindowToken(), 0);
			}
		}
	};

	// 핸들러 메시지.
	static class ResultHandler extends Handler {
		private final WeakReference<LocationPicker> mActivity;

		ResultHandler(LocationPicker activity) {
			mActivity = new WeakReference<LocationPicker>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			LocationPicker activity = mActivity.get();
			if (activity != null) {
				activity.handleMessage(msg);
			}
		}
	}

	private void handleMessage(Message msg) {
		progressDialog.dismiss();

		String result = msg.getData().getString(GoogleMapkiUtil.RESULT);
		ArrayList<String> searchList = new ArrayList<String>();

		if (result.equals(GoogleMapkiUtil.SUCCESS_RESULT)) {
			searchList = msg.getData().getStringArrayList("searchList");

		} else if (result.equals(GoogleMapkiUtil.TIMEOUT_RESULT)) {
			errorString = "네트워크 연결이 안됩니다.";
			errorDialog.setMessage(errorString);
			errorDialog.show();
			return;
		} else if (result.equals(GoogleMapkiUtil.FAIL_MAP_RESULT)) {
			errorString = "검색이 안됩니다.";
			errorDialog.setMessage(errorString);
			errorDialog.show();
			return;
		} else {
			errorString = httpUtil.stringData;
			errorDialog.setMessage(errorString);
			errorDialog.show();
			return;
		}

		final String[] searches = searchList.toArray(new String[searchList
				.size()]);
		String[] addName = new String[searches.length / 3];

		//msg는 좌표 까지 받아오는데 주소만 출력 해주어야 하므로 이름만 따로 저장,
		for (int i = 0, j = 0; j < searches.length; i++, j = j + 3) {
			addName[i] = searches[j];
		}
		
		new AlertDialog.Builder(this)
				.setItems(addName, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//5개까지 뜨도록 설정. 
						switch (which){
						case 0:
							addinfo = new AddInfo(searches[which], searches[(which + 1)], searches[(which + 2)]);
							adjustToPoints(addinfo);
							break;
						case 1:
							addinfo = new AddInfo(searches[3], searches[4], searches[5]);
							adjustToPoints(addinfo);
							break;
						case 2:
							addinfo = new AddInfo(searches[6], searches[7], searches[8]);
							adjustToPoints(addinfo);
							break;
						case 3:
							addinfo = new AddInfo(searches[9], searches[10], searches[11]);
							adjustToPoints(addinfo);
							break;
						case 4:
							addinfo = new AddInfo(searches[12], searches[13], searches[14]);
							adjustToPoints(addinfo);
							break;
						}
					}
				}).setNegativeButton("취소", null).show();
	}

	/**
	 * 주어진 위치들에 적합한 줌, 이동시킴
	 * 
	 * @param mPoints
	 */
	protected void adjustToPoints(final AddInfo addinfo) {

		mMap.clear();

		LatLng latlng = new LatLng(Double.parseDouble(addinfo.getLatitude()),
				Double.parseDouble(addinfo.getLongtude()));

		mMap.addMarker(new MarkerOptions()
				.position(latlng)
				.title(addinfo.getAddress())
				.icon(BitmapDescriptorFactory.defaultMarker())
				).showInfoWindow();

		
		CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latlng, 16);
		mMap.animateCamera(cu);
		
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			Intent intent = new Intent();
			@Override
			public void onInfoWindowClick(Marker arg0) {
				String[] addr = new String[3];
				addr[0] = addinfo.getAddress();
				addr[1] = addinfo.getLatitude();
				addr[2] = addinfo.getLongtude();
				intent.putExtra("addr", addr);
				setResult(FROM_LOCATIONPIC, intent);
				finish();
				
			}
		});
	/*	
		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			Intent intent = new Intent();
			@Override
			public boolean onMarkerClick(Marker arg0) {
				String[] addr = new String[3];
				addr[0] = addinfo.getAddress();
				addr[1] = addinfo.getLatitude();
				addr[2] = addinfo.getLongtude();
				intent.putExtra("addr", addr);
				setResult(FROM_LOCATIONPIC, intent);
				finish();
				return true;
			}
		});
		*/
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private String getAddres(double lat, double lng) {
		Geocoder gcK = new Geocoder(getApplicationContext(), Locale.KOREA);
		String res = "정보없음";
		try {
			List<Address> addresses = gcK.getFromLocation(lat, lng, 1);
			StringBuilder sb = new StringBuilder();

			if (null != addresses && addresses.size() > 0) {
				Address address = addresses.get(0);
				// sb.append(address.getCountryName()).append("/");
				// sb.append(address.getPostalCode()).append("/");
				sb.append(address.getLocality()).append("/");
				sb.append(address.getThoroughfare()).append("/");
				sb.append(address.getFeatureName());
				res = sb.toString();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
}
