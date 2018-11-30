package com.example.a15862.mytraveldiary;


import android.content.Intent;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a15862.mytraveldiary.DAO.DBOP;
import com.example.a15862.mytraveldiary.Entity.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

// this is the activity for login by using firebase Authentication.
public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private User user = null;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // we use firebase authenticationUI.
        super.onCreate(savedInstanceState);
//        Intent i = new Intent(this, MapActivity.class);
//        startActivity(i);
//        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//        intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
//        startActivity(intent);
        setContentView(R.layout.activity_login);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // code is provided by firebase doc
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.PhoneBuilder().build())
                                // we allow Google and Phone for demo, we may add twitter and facebook in future.
                        )
                        .build(),
                RC_SIGN_IN);
    }

    // RC_SIGN_IN is kind of channel, which can let us know which activity is finished.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                Log.i("1st", "auth complete");
                FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
                Log.i("1st","get current user");
                db.collection("users").document(authUser.getUid()).set(authUser);
                Log.i("1st","insert finish");
                Intent i = new Intent(this, MapActivity.class);
                //i.putExtra("user",user);
                startActivity(i);

            } else {
                // Sign in failed.
                Log.e("1st", "not work");
            }
        }
    }


//    private void findUserInDB(User u) {
//        db.collection("FirstTry").whereEqualTo("userid", u.getUserid())
//                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if (queryDocumentSnapshots.isEmpty()) {
//                } else {
//                    for (DocumentSnapshot d : queryDocumentSnapshots) {
//                        user = d.toObject(User.class);
//                    }
//                }
//            }
//        });
//    }
}



//                    Log.i("TuZa", authUser.getProviderId());
//                            if (authUser.getDisplayName()!=null){
//                            Log.i("TuZ3",authUser.getDisplayName());
//                            user.setDisplayName(authUser.getDisplayName());
//                            }
//                            Log.i("TuZb", authUser.getProviderId());
//                            if (authUser.getEmail()!=null){
//                            Log.i("TuZ4",user.getEmail());
//                            user.setEmail(authUser.getEmail());
//                            }
//                            Log.i("TuZc", authUser.getProviderId());
//                            if (authUser.getPhoneNumber()!=null){
//                            Log.i("TuZ5",authUser.getPhoneNumber());
//                            user.setPhone(authUser.getPhoneNumber());
//                            }