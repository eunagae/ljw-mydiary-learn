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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.maps.MapController;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

	public static LatLng DEFAULT_GP = new LatLng(37.566500, 126.978000);// 서울

	// Minimum & maximum latitude so we can span it
	// The latitude is clamped between -80 degrees and +80 degrees inclusive
	// thus we ensure that we go beyond that number
	private double minLatitude = +81;
	private double maxLatitude = -81;

	// Minimum & maximum longitude so we can span it
	// The longitude is clamped between -180 degrees and +180 degrees inclusive
	// thus we ensure that we go beyond that number
	private double minLongitude = +181;
	private double maxLongitude = -181;

	protected GoogleMap mMap;
	private ProgressDialog progressDialog;
	private String errorString = "";
	private ImageButton searchBt;
	private GoogleMapkiUtil httpUtil;
	private AlertDialog errorDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationpicker);

		
		setUpMapIfNeeded();
		Log.d("dd", "why doesn't working!!!");
		
		//서울로 설정.
		CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(DEFAULT_GP , 16);
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
				progressDialog = ProgressDialog
						.show(LocationPicker.this,
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

		Toast.makeText(this, "Success !!!", Toast.LENGTH_SHORT).show();
		final String[] searches = searchList.toArray(new String[searchList.size()]);
		final String pickedAddr = null;
		new AlertDialog.Builder(this)
		.setItems(searches, 
			new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//pickedAddr = searches[which].toString();
			}
		})
		.setNegativeButton("취소", null)
		.show();
		
		Toast.makeText(this, pickedAddr, 0).show();
		
		adjustToPoints(searches);
	}

	/**
	 * 주어진 위치들에 적합한 줌, 이동시킴
	 * 
	 * @param mPoints
	 */
	protected void adjustToPoints(String[] results) {

		mMap.clear();

		int length = Integer.valueOf(results.length / 3);
		LatLng[] mPoints = new LatLng[length];

		for (int i = 0; i < length; i++) {
			LatLng latlng = new LatLng(Float.valueOf(results[i * 3 + 1]),
					Float.valueOf(results[i * 3 + 2]));
			mMap.addMarker(new MarkerOptions()
					.position(latlng)
					.title(results[i * 3])
					.icon(BitmapDescriptorFactory.defaultMarker(i * 360
							/ length)));

			mPoints[i] = latlng;
		}

		for (LatLng ll : mPoints) {

			// Sometimes the longitude or latitude gathering
			// did not work so skipping the point
			// doubt anybody would be at 0 0
			if (ll.latitude != 0 && ll.longitude != 0) {
				// Sets the minimum and maximum latitude so we can span and zoom
				minLatitude = (minLatitude > ll.latitude) ? ll.latitude
						: minLatitude;
				maxLatitude = (maxLatitude < ll.latitude) ? ll.latitude
						: maxLatitude;
				// Sets the minimum and maximum latitude so we can span and zoom
				minLongitude = (minLongitude > ll.longitude) ? ll.longitude
						: minLongitude;
				maxLongitude = (maxLongitude < ll.longitude) ? ll.longitude
						: maxLongitude;
			}
		}

		Log.d("dd", minLatitude + "/" + maxLatitude + "/" + minLongitude + "/"
				+ maxLongitude);

		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(new LatLngBounds(
				new LatLng(minLatitude, minLongitude), new LatLng(maxLatitude,
						maxLongitude)), 6);
		mMap.animateCamera(cu);

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
