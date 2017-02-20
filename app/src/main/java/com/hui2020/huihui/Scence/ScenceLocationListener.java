package com.hui2020.huihui.Scence;

import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

/**
 * Created by FD-GHOST on 2016/12/31.
 */

public class ScenceLocationListener implements BDLocationListener {
    private TextView targetLatitude, targetLongitude;
    private TextView textView;
    float[] results;

    public ScenceLocationListener(TextView targetLatitude, TextView targetLongitude, TextView textView) {
        this.targetLatitude = targetLatitude;
        this.targetLongitude = targetLongitude;
        this.textView = textView;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        //Receive Location
        Log.e("ScenceLocation", "Baidu");
        Double currentLatitude = location.getLatitude();
        Double currentLongitude = location.getLongitude();
        results = new float[1];
        Log.e("ScenDistance",targetLatitude.getText().toString()+"      "+targetLongitude.getText().toString());
        double latitude = Double.parseDouble(targetLatitude.getText().toString());
        double longitude = Double.parseDouble(targetLongitude.getText().toString());
        if(latitude !=0 && longitude!=0) {
            Location.distanceBetween(latitude, longitude, currentLatitude, currentLongitude, results);
            textView.setText("距离" + ((int) (results[0] * 0.3048) / 10000) + "米   ");
            Log.e("ScenDistance", ((int) (results[0] * 0.3048) / 10000) + "");
        }
    }

    public int getDistance(){
        return ((int) (results[0] * 0.3048) / 10000);
    }
}