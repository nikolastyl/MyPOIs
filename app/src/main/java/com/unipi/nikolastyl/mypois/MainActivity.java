package com.unipi.nikolastyl.mypois;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener{
    LocationManager locationManager;
    SQLiteDatabase db;
    String currLocation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //db connection-execSQL
        db = openOrCreateDatabase("POIs.db", MODE_PRIVATE, null);
        db.execSQL("Create table if not exists POIS(" +
                "title TEXT," +
                "location TEXT PRIMARY KEY," +
                "timestamp TEXT," +
                "description TEXT," +
                "category TEXT)");
        //permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,this);




    }

    public void addNew(View view){ //add new button onClick
        Intent intent = new Intent(this,AddNewPOIs.class);
        intent.putExtra("CUR_LOC", currLocation);
        locationManager.removeUpdates(this);
        startActivity(intent);
    }


    public void seeAll(View view){ //see All button onClick
        Intent intent = new Intent(this,SeeAll.class);
        startActivity(intent);

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {//Current Location
        currLocation=location.getLatitude()+","+location.getLongitude();
    }
}