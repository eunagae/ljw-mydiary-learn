package kr.hkit.mydiary.write;

import com.example.kr.hkit.mydiary.R;
import com.google.android.gms.maps.MapView;
import com.google.android.maps.MapActivity;
import android.os.Bundle;

public class LocationPicker extends MapActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationpicker); 
		//MapView myMap = (MapView) findViewById(R.id.locationpicker_mapview);
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
