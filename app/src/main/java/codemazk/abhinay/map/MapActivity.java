package codemazk.abhinay.map;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity {
    private GoogleMap mMap;
    // GPSTracker class
    GPSTracker gps;
    double longitude,latitude,lati,longi;
    String carname,carnbr,time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            lati = Double.parseDouble(extras.getString("lati"));
            longi = Double.parseDouble(extras.getString("longi"));
            carname = extras.getString("carname");
            carnbr = extras.getString("carnbr");
            time = extras.getString("time");


        }

       /* MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);*/

        gps = new GPSTracker(MapActivity.this);




        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude   = gps.getLongitude();

            // \n is for new line
          //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            setUpMapIfNeeded();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
       final LatLng current = new LatLng(latitude, longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 16));
       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create class object

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 16));

            }
        });
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create class object

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+lati+","+longi+"")); startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

 /*  private void setUpMapIfNeeded() {
        if (mMap != null) {
            return;
        }
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mMap == null) {
            return;
        }
        // Initialize map options. For example:
         mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }*/
 private void setUpMapIfNeeded() {
     if (mMap != null) {
         return;
     }
     mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
     if (mMap == null) {
         return;
     }
     // Initialize map options. For example:
     // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

     final LatLng carPos = new LatLng(lati, longi);

     //  String address=getAddress(this, 28.5192, 77.2130);
     Marker car = mMap.addMarker(new MarkerOptions()
             .position(carPos)
             .title(carname)
             .snippet(carnbr)
             .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
     // String address=getAddress(this,28.6700, 77.4200);

     Geocoder geocoder = new Geocoder(this, Locale.getDefault());
     String result = null;
     try {
         List<Address> addressList = geocoder.getFromLocation(
                 latitude,longitude, 1);
         if (addressList != null && addressList.size() > 0) {
             Address address = addressList.get(0);
             StringBuilder sb = new StringBuilder();
             for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                 sb.append(address.getAddressLine(i)).append("\n");//adress
             }
             sb.append(address.getLocality()).append("\n");//village

             sb.append(address.getPostalCode()).append("\n");
             sb.append(address.getCountryName());
             sb.append(address.getAdminArea()).append("\n"); //state

             sb.append(address.getSubAdminArea()).append("\n");//district

             sb.append(address.getSubLocality()).append("\n");

             result = sb.toString();
         }
     } catch (IOException e) {
         // Log.e(TAG, "Unable connect to Geocoder", e);
     }
    // new AlertDialog.Builder(this).setMessage(result).create().show();

    // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
     final LatLng curntpos = new LatLng(latitude, longitude);
     Marker curnt = mMap.addMarker(new MarkerOptions()
             .position(curntpos)
             .title(result)
             .snippet("Your are here")
            );


 }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
