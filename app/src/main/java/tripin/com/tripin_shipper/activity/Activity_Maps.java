package tripin.com.tripin_shipper.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import tripin.com.tripin_shipper.R;

/**
 * Created by Android on 18/08/16.
 */
public class Activity_Maps extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    private GoogleApiClient mGoogleApiClient;
    static final LatLng VADODARA = new LatLng(22.2294841, 73.208709);
    static final LatLng RAJKOT = new LatLng(22.3039, 70.8022); // Rajkot 22.3039° N, 70.8022
    private GoogleMap map;
    private FragmentManager myFM;
    private GoogleMap googleMap;
    private FragmentActivity Activity_Maps = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booknow_map1);
       // googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment)).getMap();


         /*    ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

            }
        });*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
       /* Marker hamburg = googleMap.addMarker(new MarkerOptions().position(VADODARA)
                .title("VADODARA"));
        Marker kiel = googleMap.addMarker(new MarkerOptions()
                .position(RAJKOT)
                .title("RAJKOT")
                .snippet("TRIP IN")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_launcher)));*/

        // Move the camera instantly to hamburg with a zoom of 15.
      /*  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(VADODARA, 15));

        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);*/
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // Enabling MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);
    }


}