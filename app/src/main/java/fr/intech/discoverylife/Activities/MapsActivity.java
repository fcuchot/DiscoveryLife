package fr.intech.discoverylife.Activities;

import fr.intech.discoverylife.Databases.DBHandler;
import fr.intech.discoverylife.Classes.Landmark;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import fr.intech.discoverylife.Databases.DBHandler;
import fr.intech.discoverylife.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        DBHandler connection = new DBHandler(this);
        List<Landmark> userLandmark = connection.getAllLandmarks(1);

        for (int i = 0; i < userLandmark.size(); i++){
            map.addMarker(new MarkerOptions().position(new LatLng(
                    userLandmark.get(i).getLatitude(), userLandmark.get(i).getLongitude())).title(userLandmark.get(i).getTitle()).snippet(userLandmark.get(i).getDescription()));
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.866667, 2.333333), 10));

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}