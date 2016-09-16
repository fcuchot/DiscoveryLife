package fr.intech.discoverylife.Activities;

import fr.intech.discoverylife.Databases.DBHandler;
import android.content.Intent;
import fr.intech.discoverylife.Classes.Landmark;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import fr.intech.discoverylife.Databases.DBHandler;
import fr.intech.discoverylife.R;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    private static final String TAG = MapsActivity.class.getSimpleName();

    DBHandler connection = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fillDbWithData();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }


    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }




    @Override
    public void onMapReady(GoogleMap map) {

        mGoogleMap = map;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                                             @Override
                                             public void onMapClick(LatLng point) {

                                                 MarkerOptions marker = new MarkerOptions()
                                                         .position(new LatLng(point.latitude, point.longitude))
                                                         .title("New Marker").snippet("test");
                                                 mGoogleMap.addMarker(marker);
                                                 Intent myIntent = new Intent(MapsActivity.this, FillMarkerInfoActivity.class);
//                                                                             myIntent.putExtra("key", value); //Optional parameters
                                                 myIntent.putExtra("Lat", point.latitude);
                                                 myIntent.putExtra("Lng", point.longitude);
                                                 MapsActivity.this.startActivity(myIntent);
                                             }

                                         });
        //get all landmarks for this user
        List<Landmark> userLandmark = connection.getAllLandmarks(1);
        if (userLandmark.size() != 0) {
            for (int i = 0; i < userLandmark.size(); i++) {
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(
                        userLandmark.get(i).getLatitude(), userLandmark.get(i).getLongitude())).title(userLandmark.get(i).getTitle()).snippet(userLandmark.get(i).getDescription()));
            }
        }


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.866667, 2.333333), 10));
              }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d(TAG, "------ onConnected First step -------");

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "------ onConnected will wall location update -------");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        Log.d(TAG, "old location" + location);
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d(TAG, "new location" + latLng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        Log.d(TAG, "-------------------------------------------------------------- Début -----------------------------------------------------------");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "1er étape");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "2ème étape");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            Log.d(TAG, "return false");
            return false;
        } else {
            Log.d(TAG, "return true");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void fillDbWithData(){

        List<Landmark> listOfFakeLandmark = new ArrayList<Landmark>();

        listOfFakeLandmark.add(new Landmark("Arc de Triomphe","ceci est un test", 1, 48.8738f, 2.295f));
        listOfFakeLandmark.add(new Landmark("Chapelle expiatoire","ceci est un test", 1, 48.8737f, 2.3227f));
        listOfFakeLandmark.add(new Landmark("Conciergerie","ceci est un test", 1, 48.8558f, 2.346f));
        listOfFakeLandmark.add(new Landmark("Palais-Royal","ceci est un test", 1, 48.865f, 2.3377f));
        listOfFakeLandmark.add(new Landmark("Hôtel de Béthune-Sully","ceci est un test", 1, 48.8547f, 2.3639f));
        listOfFakeLandmark.add(new Landmark("Musée des Plans-Reliefs","ceci est un test", 1, 48.8565f, 2.3127f));
        listOfFakeLandmark.add(new Landmark("Panthéon","ceci est un test", 1, 48.8463f, 2.3461f));
        listOfFakeLandmark.add(new Landmark("Sainte-Chapelle","ceci est un test", 1, 48.8554f, 2.345f));
        listOfFakeLandmark.add(new Landmark("Notre-Dame de Paris","ceci est un test", 1, 48.853f, 2.35f));
        listOfFakeLandmark.add(new Landmark("Château de Champs-sur-Marne","ceci est un test", 1, 48.8536f, 2.604f));

        for(int i = 0; i < listOfFakeLandmark.size(); i++){
            connection.addLandmark(listOfFakeLandmark.get(i));
        }
    }
}