package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
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

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.a15862.mytraveldiary.DAO.PlaceDAO;
import com.example.a15862.mytraveldiary.DAO.UserDAO;
import com.example.a15862.mytraveldiary.Entity.Place;
import com.example.a15862.mytraveldiary.Entity.User;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, placeInfoReceiver, AdapterView.OnItemClickListener{

    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private final int radius = 50;
    private static final double RAD = Math.PI / 180.0;
    private static final double EARTH_RADIUS = 6378137;
    private List<Place> nearbyPlaces;
    private Set<String> existed;
    private Map<String, Place> findPlaceByName;
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



    // DrawerLayout and adapter
    private DrawerLayout drawer_layout;
    private ListView list_left_drawer;
    private ArrayAdapter<String> adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nearbyPlaces = new ArrayList<>();
        existed = new HashSet<>();
        searchServices = new SearchServicesImp(this);
        findPlaceByName = new HashMap<>();
        super.onCreate(savedInstanceState);
        User testUser=new User();
        SharedPreferences load = getSharedPreferences("user",Context.MODE_PRIVATE);
        testUser.setDisplayName(load.getString("displayName", "DEFAULT"));
        testUser.setUsername(load.getString("username","DEFAULT"));
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


        // DrawerLayout and adapter
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        list_left_drawer = (ListView) findViewById(R.id.list_left_drawer);

        String[] left_menu = {"View Diary","Friends","Settings","Logout"};

        adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_expandable_list_item_1,left_menu);

        list_left_drawer.setAdapter(adapter);
        list_left_drawer.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(adapter.getItem(position) == "View Diary" ){
            Intent intent = new Intent(MapActivity.this, ViewAllDiaryActivity.class);
            startActivity(intent);
        }
        drawer_layout.closeDrawer(list_left_drawer);
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
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, NEARBY_ZOOM));
                Log.i("Info", String.valueOf(point.latitude) + "," + String.valueOf(point.longitude));
                // Use YelpAPI with parameters.
                try {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Place").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                Place p = ds.toObject(Place.class);
                                Log.i("Check", String.valueOf(p.getComments() == null));
                                LatLng loc = new LatLng(p.getLatitude(), p.getLongitude());
                                if (getDistance(loc.longitude, loc.latitude, point.longitude, point.latitude) <= radius) {
                                    if (existed.add(p.getPid())) {
                                        nearbyPlaces.add(p);
                                        findPlaceByName.put(p.getPlaceName(), p);
                                    }
                                }
                            }
                            try {
                                searchServices.searchLocation(point, radius);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("Exception: %s", e.getMessage());
                }
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
//                mMap.clear();
//                Marker marker = mMap.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .title("Touch to add a diary")
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.question_32)));
//                marker.showInfoWindow();

                DialogFragment dialog = new ConfirmFragment();
                Bundle b = new Bundle();
                b.putDouble("Latitude",latLng.latitude);
                b.putDouble("Longitude",latLng.longitude);
                dialog.setArguments(b);
                dialog.show(getSupportFragmentManager(), "ConfirmFragment");
            }
        });

        // Pop a dialog when clicking on the marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                Intent intent = new Intent(MapActivity.this, ClickExistActivity.class);
//                Intent intent = new Intent(MapActivity.this, ClickNotExistActivity.class);
                Intent intent = new Intent(MapActivity.this, ClickExistActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("Place", findPlaceByName.get(marker.getTitle()));
                intent.putExtras(b);
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

    public void draw() {
        for (Place p : nearbyPlaces) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(p.getLatitude(), p.getLongitude())).title(p.getPlaceName()));
            marker.showInfoWindow();
        }
    }

    @Override
    public void receive(JSONObject res) throws JSONException {
        PlaceDAO placeDAO = new PlaceDAO();
        try {
            //The candidate is the value of the key "Results" in JSON
            //for each location in result , get the longitue and latitude and the place name,use marker to draw them
            JSONArray array = res.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject cur = array.getJSONObject(i);
                String pid = cur.getString("place_id");
                if (!existed.add(pid)) continue;
                String placeName = cur.getString("name");
                placeName = placeName.replaceAll("/","_");
                JSONObject location = cur.getJSONObject("geometry").getJSONObject("location");
                LatLng placeLoc = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
                String vicinity = cur.getString("vicinity");
                Place p = new Place(placeLoc, placeName, vicinity, pid);
                if (cur.has("photos")) {
                    JSONObject photos = cur.getJSONArray("photos").getJSONObject(0);
                    p.setPhotoPath(photos.getString("html_attributions"));
                }
                if(cur.has("types")){
                    JSONArray ja = cur.getJSONArray("types");
                    List<String> cat = new ArrayList<>();
                    for(int j = 0;j<ja.length();j++){
                        cat.add(ja.getString(j));
                    }
                    p.setCatagoty(cat);
                }
                nearbyPlaces.add(p);
                findPlaceByName.put(p.getPlaceName(), p);
                placeDAO.addPlace(p);
            }
            draw();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = lat1 * RAD;
        double radLat2 = lat2 * RAD;
        double a = radLat1 - radLat2;
        double b = (lng1 - lng2) * RAD;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}
