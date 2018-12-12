package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a15862.mytraveldiary.DAO.CommentDAO;
import com.example.a15862.mytraveldiary.DAO.PlaceDAO;
import com.example.a15862.mytraveldiary.Entity.Comment;
import com.example.a15862.mytraveldiary.Entity.Place;
import com.example.a15862.mytraveldiary.Entity.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.stephentuso.welcome.WelcomeHelper;

public class LoginNewActivity extends AppCompatActivity {
    private EditText edtUserName;
    private EditText edtPassword;
    private TextView txtSignUpWithPassword;
    private Button btnSignWithPhone;
    private Button btnLogin;
    private FirebaseFirestore db;
    private String TAG = "LoginTT";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
//    WelcomeHelper welcomeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        db = FirebaseFirestore.getInstance();


        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignWithPhone = (Button) findViewById(R.id.btnSignWithPhone);
        txtSignUpWithPassword = (TextView) findViewById(R.id.txtSignUpWithPassword);


//        welcomeScreen = new WelcomeHelper(this, MyWelcomeActivity.class);
//        welcomeScreen.forceShow();


        btnSignWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register3Activity.class);
                startActivity(intent);
            }
        });

        txtSignUpWithPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register1Activity.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUserName.getText().toString();
                String password = edtPassword.getText().toString();
                Log.e(TAG, "btnlogin click");
                db.collection("User").whereEqualTo("username", username).whereEqualTo("password", password)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "username or password wrong", Toast.LENGTH_SHORT).show();
                        } else {
                            for (DocumentSnapshot d : queryDocumentSnapshots) {
                                User user = d.toObject(User.class);
                                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器

                                editor.putString("displayName", user.getDisplayName());

                                editor.putString("username", user.getUsername());
                                editor.putString("avatar", user.getAvatar());
//                                Log.i("avatar",user.getAvatar());
                                editor.commit();
                                Intent i = new Intent(getApplicationContext(), MapActivity.class);
                                startActivity(i);
                            }
                        }
                    }
                });
            }
        });
        getLocationPermission();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        welcomeScreen.onSaveInstanceState(outState);
    }
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
    }
}
