package com.example.baidumaptest;

import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

private MapView mapView;
private BaiduMap baiduMap;

private LocationManager locationManager;

private String provider;

private boolean isFirstLocate=true; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mapView=(MapView)findViewById(R.id.map_view);
        baiduMap=mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providerList= locationManager.getProviders(true);
        if(providerList.contains(LocationManager.GPS_PROVIDER)){
        	provider=LocationManager.GPS_PROVIDER;
        }else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
        	provider=LocationManager.NETWORK_PROVIDER;
        }else{
        	Toast.makeText(this, "No location provider to use", Toast.LENGTH_SHORT).show();
        	return;
        }
        Location location=locationManager.getLastKnownLocation(provider);
        
        
        if(location !=null){
        	navigaTo(location);
        }
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
    }
    
    private void navigaTo(Location location) {
		// TODO Auto-generated method stub
		if(isFirstLocate){
			LatLng ll=new LatLng(location.getLatitude(), location.getLongitude());
			Log.d("test", location.getLatitude()+" "+location.getLongitude());
			MapStatusUpdate update=MapStatusUpdateFactory.newLatLng(ll);
			baiduMap.animateMapStatus(update);
			update=MapStatusUpdateFactory.zoomBy(16f);
			baiduMap.animateMapStatus(update);
			isFirstLocate=false;
		}
		MyLocationData.Builder locationBuilder=new MyLocationData.Builder();
		locationBuilder.latitude(location.getLatitude());
		locationBuilder.longitude(location.getLongitude());
		MyLocationData locationData=locationBuilder.build();
		baiduMap.setMyLocationData(locationData);
		
	}
    
    LocationListener locationListener=new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if(location!=null){
				navigaTo(location);
			}
		}
	};

	@Override
    protected void onDestroy(){
    	super.onDestroy();
    	baiduMap.setMyLocationEnabled(false);
    	mapView.onDestroy();
    	if(locationManager!=null){
    		locationManager.removeUpdates(locationListener);
    	}
    }
    
    @Override
    protected void onPause(){
    	super.onPause();
    	mapView.onPause();
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
    	mapView.onResume();
    }
}
