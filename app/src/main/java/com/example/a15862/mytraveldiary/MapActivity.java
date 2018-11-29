package com.example.a15862.mytraveldiary;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.a15862.mytraveldiary.Entity.Place;
import com.example.a15862.mytraveldiary.ServiceImps.SearchServicesImp;
import com.example.a15862.mytraveldiary.Services.SearchServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, placeInfoReceiver {

    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private final int radius = 50;
    private static final double RAD = Math.PI / 180.0;
    private static final double EARTH_RADIUS = 6378137;
    private List<Place> nearbyPlaces;
    Set<String> existed;
    boolean googleInfoGet = false;
    boolean databaseInfoGet = false;
    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int NEARBY_ZOOM = 18;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    SearchServices searchServices;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // The sliding menu
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nearbyPlaces = new ArrayList<>();
        searchServices = new SearchServicesImp(this);
        super.onCreate(savedInstanceState);
        Log.i("myMap", "oncreate");
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_map);
        // Construct a GeoDataClient.
        //mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        //mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /* sliding menu
         * The listed items in the menu are temporary and the clicking function is not implemented
         */
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ImageView mAvatar = findViewById(R.id.imgProfile);

        NavigationView navView = findViewById(R.id.nav_view);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        // do something corresponding to the item selected
                        // functions will be implemented after intergrating other parts
                        onOptionsItemSelected(menuItem);

                        return true;
                    }
                });


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        // Create new marker when user pin on the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng point) {
                mMap.clear();
                // Use YelpAPI with parameters.
                try {
                    FirebaseFirestore db =  FirebaseFirestore.getInstance();
                    db.collection("Place").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot ds:queryDocumentSnapshots){
                                Place p = ds.toObject(Place.class);
                                LatLng loc = p.getLocation();
                                if(getDistance(loc.longitude,loc.latitude,point.longitude,point.latitude)<=radius){
                                    if(existed.add(p.getPid())){
                                        nearbyPlaces.add(p);
                                    }
                                }
                            }
                        }
                    });
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, NEARBY_ZOOM));
                    searchServices.searchLocation(point, radius);
                } catch (IOException e) {
                    Log.e("Exception: %s", e.getMessage());
                } catch (Exception e) {
                    Log.e("Exception: %s", e.getMessage());
                }
            }
        });

        // Pop a dialog when clicking on the marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                Intent intent = new Intent(MapActivity.this, ClickExistActivity.class);
//                Intent intent = new Intent(MapActivity.this, ClickNotExistActivity.class);
                Intent intent = new Intent(MapActivity.this, AddDiaryActivity.class);
                intent.putExtra("CurrentLatitude", mLastKnownLocation.getLatitude());
                intent.putExtra("CurrentLongitude", mLastKnownLocation.getLongitude());
                startActivity(intent);

                return false;
            }
        });
    }


    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("Current Place")
                                    .snippet("Latitude: " + latLng.latitude + " Longitude: " + latLng.longitude));
                            marker.showInfoWindow();
                        } else {
                            Log.d("", "Current location is null. Using defaults.");
                            Log.e("", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

//    public void draw(JSONObject res) {
//        try {
//            //The candidate is the value of the key "Results" in JSON
//            //for each location in result , get the longitue and latitude and the place name,use marker to draw them
//            JSONArray array = res.getJSONArray("results");
//            for (int i = 0; i < array.length(); i++) {
//                System.out.println(i);
//                JSONObject cur = array.getJSONObject(i);
//                JSONObject location = cur.getJSONObject("geometry").getJSONObject("location");
//                LatLng loc = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
//                Marker marker = mMap.addMarker(new MarkerOptions().position(loc).title(cur.getString("name")));
//                marker.showInfoWindow();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void receive(JSONObject res) throws JSONException {
        googleInfoGet = true;
        try {
            //The candidate is the value of the key "Results" in JSON
            //for each location in result , get the longitue and latitude and the place name,use marker to draw them
            JSONArray array = res.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject cur = array.getJSONObject(i);
                String pid = cur.getString("place_id");
                if(!existed.add(pid)) continue;
                String placeName = cur.getString("name");
                JSONObject location = cur.getJSONObject("geometry").getJSONObject("location");
                LatLng placeLoc = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
                String vicinity = cur.getString("vicinity");
                Place p = new Place(placeLoc,placeName,vicinity,pid);
                if(cur.has("photos")){
                    JSONObject photos = cur.getJSONArray("photos").getJSONObject(0);
                    p.setPhotoPath(photos.getString("html_attributions"));
                }
                nearbyPlaces.add(p);
                Log.i("JingNew",p.print());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.goText:
                //showDiaryFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static double getDistance(double lng1, double lat1, double lng2, double lat2)
    {
        double radLat1 = lat1*RAD;
        double radLat2 = lat2*RAD;
        double a = radLat1 - radLat2;
        double b = (lng1 - lng2)*RAD;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}
