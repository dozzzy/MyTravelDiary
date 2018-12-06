package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

import com.example.a15862.mytraveldiary.DAO.CommentDAO;
import com.example.a15862.mytraveldiary.DAO.FollowshipDAO;
import com.example.a15862.mytraveldiary.DAO.PlaceDAO;
import com.example.a15862.mytraveldiary.DAO.UserDAO;
import com.example.a15862.mytraveldiary.Entity.Comment;
import com.example.a15862.mytraveldiary.Entity.Place;
import com.example.a15862.mytraveldiary.Entity.User;
import com.example.a15862.mytraveldiary.ServiceImps.SearchServicesImp;
import com.example.a15862.mytraveldiary.Services.SearchServices;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, placeInfoReceiver, NavigationView.OnNavigationItemSelectedListener {
    private final Map<String,Set<String>> getKeyWords = new HashMap<>();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private final int radius = 50;
    private static final double RAD = Math.PI / 180.0;
    private static final double EARTH_RADIUS = 6378137;
    private List<Place> nearbyPlaces;
    private Set<String> existed;
    private Map<String, Place> findPlaceByName;
    private Map<Marker,Place> marked = new HashMap<>();
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

    private TextView txtUsername;
    private TextView txtDisplayName;

    private ImageView imgAvater;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nearbyPlaces = new ArrayList<>();
        existed = new HashSet<>();
        searchServices = new SearchServicesImp(this);
        findPlaceByName = new HashMap<>();
        super.onCreate(savedInstanceState);

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



        // Floating buttons
        final com.getbase.floatingactionbutton.FloatingActionButton actionA = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, ClickNotExistActivity.class);
                Bundle b = new Bundle();
                b.putDouble("Longitude", mLastKnownLocation.getLongitude());
                b.putDouble("Latitude", mLastKnownLocation.getLatitude());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        final com.getbase.floatingactionbutton.FloatingActionButton actionB = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()), NEARBY_ZOOM));
            }
        });


        // DrawerLayout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        SharedPreferences load = getSharedPreferences("user",Context.MODE_PRIVATE);
        String displayName=load.getString("displayName", "DEFAULT");
        String username=load.getString("username","DEFAULT");
        String avatar=load.getString("avatar","DEFAULT");
        View headerView = navigationView.getHeaderView(0);

        txtUsername=(TextView)headerView.findViewById(R.id.txtUsername);
        txtDisplayName=(TextView)headerView.findViewById(R.id.txtDisplayName);
        imgAvater=(ImageView)headerView.findViewById(R.id.imgAvater);
        txtUsername.setText(username);
        txtDisplayName.setText(displayName);
        Log.i("avatar",avatar);
        if (!avatar.equals("DEFAULT")){
            Picasso.get().load(avatar).into(imgAvater);
        }



        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                // TODO: Handle the error.
                Log.e("error",e.getMessage());
            } catch (GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
                Log.e("error",e.getMessage());
            }
            return true;
        }
        if (id == R.id.add_friends) {
            Intent intent = new Intent(MapActivity.this, AddFriendsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_diary) {
            Intent intent = new Intent(MapActivity.this, ViewAllDiaryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_friends) {
            Intent intent = new Intent(MapActivity.this, ViewAllFriendsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        getKeyWords.put("route",new HashSet<String>(Arrays.asList(new String[]{"route"})));
        getKeyWords.put("bus station",new HashSet<String>(Arrays.asList(new String[]{"bus_station"})));
        getKeyWords.put("store",new HashSet<String>(Arrays.asList(new String[]{"store"})));
        getKeyWords.put("railway station",new HashSet<String>(Arrays.asList(new String[]{"transit_station"})));
        getKeyWords.put("education",new HashSet<String>(Arrays.asList(new String[]{"school","university"})));
        getKeyWords.put("health",new HashSet<String>(Arrays.asList(new String[]{"health","doctor","gym"})));
        getKeyWords.put("point_of_interest",new HashSet<String>(Arrays.asList(new String[]{"point_of_interest"})));

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


        UiSettings uiSettings = mMap.getUiSettings();
//        uiSettings.setZoomControlsEnabled(true);
//        uiSettings.setMyLocationButtonEnabled(true);


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
                b.putDouble("Latitude", latLng.latitude);
                b.putDouble("Longitude", latLng.longitude);
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
        mMap.clear();
        label1:
        for (Place p : nearbyPlaces) {
            List<String> cats = p.getCatagory();
            for(String cat:cats){
                for(String key:getKeyWords.keySet()){
                    if(getKeyWords.get(key).contains(cat)){
                        // here the key is one of the predefined catagories
                        // route, food, store, education, bus station, railway station, health, point_of_interest

                        Marker marker = null;
                        if(key == "route") {
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(p.getLatitude(), p.getLongitude()))
                                    .title(p.getPlaceName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.route)));
                        }
                        if(key == "food") {
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(p.getLatitude(), p.getLongitude()))
                                    .title(p.getPlaceName())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.food)));
                        }
                        if(key == "store") {
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(p.getLatitude(), p.getLongitude()))
                                    .title(p.getPlaceName())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.store)));
                        }
                        if(key == "education") {
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(p.getLatitude(), p.getLongitude()))
                                    .title(p.getPlaceName())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.education)));
                        }
                        if(key == "bus station") {
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(p.getLatitude(), p.getLongitude()))
                                    .title(p.getPlaceName())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
                        }
                        if(key == "railway station") {
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(p.getLatitude(), p.getLongitude()))
                                    .title(p.getPlaceName())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.railway)));
                        }
                        if(key == "health") {
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(p.getLatitude(), p.getLongitude()))
                                    .title(p.getPlaceName())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.health)));
                        }
                        if(key == "point_of_interest") {
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(p.getLatitude(), p.getLongitude()))
                                    .title(p.getPlaceName())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.interest)));
                        }
                        marker.showInfoWindow();
                        continue label1;
                    }
                }
            }
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(p.getLatitude(), p.getLongitude()))
                    .title(p.getPlaceName()));
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
                placeName = placeName.replaceAll("/", "_");
                JSONObject location = cur.getJSONObject("geometry").getJSONObject("location");
                LatLng placeLoc = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
                String vicinity = cur.getString("vicinity");
                Place p = new Place(placeLoc, placeName, vicinity, pid);
                searchServices.getComment(pid);
                if (cur.has("photos")) {
                    JSONObject photos = cur.getJSONArray("photos").getJSONObject(0);
                    p.setPhotoPath(photos.getString("html_attributions"));
                }
                if (cur.has("types")) {
                    JSONArray ja = cur.getJSONArray("types");
                    List<String> cat = new ArrayList<>();
                    for (int j = 0; j < ja.length(); j++) {
                        cat.add(ja.getString(j));
                    }
                    p.setCatagory(cat);
                }
                nearbyPlaces.add(p);
                findPlaceByName.put(p.getPlaceName(), p);
                placeDAO.addPlace(p);
            }
            draw();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveComment(JSONObject res) throws JSONException {
        JSONObject total = res.getJSONObject("result");
        JSONArray arr =total.getJSONArray("reviews");
        String placeName = total.getString("name");
        for(int i = 0;i<arr.length();i++){
            JSONObject cur = arr.getJSONObject(i);
            String userName = cur.getString("author_name");
            String text = cur.getString("text");
            Comment comment = new Comment(userName,placeName,text);
            CommentDAO cd = new CommentDAO();
            cd.addComment(comment,1);
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



    public void undoFilter(){
        for(Marker m : marked.keySet()){
            m.setVisible(true);
        }
    }

    public void doFilter(String keyWords){
        Set<String> validWord = getKeyWords.get(keyWords);
        label1:
        for(Marker m:marked.keySet()){
            Place p = marked.get(m);
            List<String> cats = p.getCatagory();
            for(String cat:cats){
                if(validWord.contains(cat)) continue label1;
            }
            m.setVisible(false);
        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                Place place = PlaceAutocomplete.getPlace(this, data);
//                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                Status status = PlaceAutocomplete.getStatus(this, data);
//                // TODO: Handle the error.
//                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
