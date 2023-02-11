package com.example.lab_baotrieuthai_866206_android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

        private GoogleMap mMap;

        private static final int REQUEST_CODE = 1;
        private Marker homeMarker;
        private Marker selectedMarker;
        private LatLng selectedLatLng;
        private FavoriteRoomDb favoriteRoomDb;
        private TextView label_bottom;

        List<Marker> markers = new ArrayList();

        // location with location manager and listener
        LocationManager locationManager;
        LocationListener locationListener;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            label_bottom = (TextView) findViewById(R.id.label_bottom);
            label_bottom.setVisibility(View.GONE);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    setHomeMarker(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (!hasLocationPermission())
                requestLocationPermission();
            else
                startUpdateLocation();

            // click on Maker
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    selectedMarker = marker;
                    selectedLatLng = marker.getPosition();
                    calculateDistanceBetweenPoints();
                    return false;
                }
            });

            // apply long press gesture
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    // set marker
                    setMarker(latLng);
                }

                private void setMarker(LatLng latLng) {
                    MarkerOptions options = new MarkerOptions().position(latLng)
                            .title("A");
                    markers.add(mMap.addMarker(options));
                    addFavorite(options);
                }


            });
        }

    private void addFavorite(MarkerOptions options) {
        Double lat = options.getPosition().latitude;
        Double lng = options.getPosition().longitude;
        // getting the current time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd hh:mm:ss", Locale.CANADA);
        String savedDate = sdf.format(cal.getTime());
        String name = "Favorite";

        // Insert into room db
        Favorite favorite = new Favorite(name, lat, lng, savedDate);
        favoriteRoomDb.favoriteDao().insertFavorite(favorite);
        Toast.makeText(this, "Favorite added", Toast.LENGTH_SHORT).show();
    }

        private void calculateDistanceBetweenPoints() {
            if (selectedLatLng == null || markers.size() == 1) {
                return;
            }

            int index = markers.indexOf(selectedMarker);
            LatLng prevLatLng = markers.get((index - 1 + markers.size()) % markers.size()).getPosition();
            LatLng nextLatLng = markers.get((index + 1) % markers.size()).getPosition();

            double prevDistance = SphericalUtil.computeDistanceBetween(selectedLatLng, prevLatLng);
            double nextDistance = SphericalUtil.computeDistanceBetween(selectedLatLng, nextLatLng);

            label_bottom.setVisibility(View.VISIBLE);
            label_bottom.setText(selectedMarker.getTitle() +" to " + markers.get((index - 1 + markers.size()) % markers.size()).getTitle() +": " + String.format("%.2f", prevDistance / 1000) + " km\n"
                    + selectedMarker.getTitle() + " to " + markers.get((index + 1) % markers.size()).getTitle()+": " + String.format("%.2f", nextDistance / 1000) + " km");
        }

        private void setHomeMarker(Location location) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions options = new MarkerOptions().position(userLocation)
                    .title("Lambton College")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .snippet("You are here!");
            homeMarker = mMap.addMarker(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
        }

        private void startUpdateLocation() {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
        }

        private void requestLocationPermission() {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        private boolean hasLocationPermission() {
            return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
}
