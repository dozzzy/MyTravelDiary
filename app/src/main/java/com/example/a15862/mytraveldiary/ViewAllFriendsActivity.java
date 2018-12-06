package com.example.a15862.mytraveldiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.a15862.mytraveldiary.DAO.FollowshipDAO;
import com.example.a15862.mytraveldiary.DAO.UserDAO;
import com.example.a15862.mytraveldiary.Entity.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAllFriendsActivity extends AppCompatActivity {
    List<User> friends = new ArrayList<>();
    List<String> friendName = null;
    FirebaseFirestore db;
    RecyclerView listFriends;
    MyCustomAdapterForFriends mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_friends);
        db = FirebaseFirestore.getInstance();
        SharedPreferences load = getSharedPreferences("user", Context.MODE_PRIVATE);
        String displayName = load.getString("displayName", "DEFAULT");
        listFriends=(RecyclerView)findViewById(R.id.listFriends);

        listFriends.setHasFixedSize(true);
        listFriends.setLayoutManager(new LinearLayoutManager(this));
        db.collection("Followship").document(displayName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                friendName = (ArrayList) documentSnapshot.getData().get("followed");
                for (String name : friendName) {
                    Log.i("Jing", name);
                    db.collection("User").whereEqualTo("username", name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Log.i("Jing","get user info");
                            for (QueryDocumentSnapshot qs : queryDocumentSnapshots) {
                                friends.add(qs.toObject(User.class));
                            }
                            mAdapter = new MyCustomAdapterForFriends(ViewAllFriendsActivity.this, friends);
                            listFriends.setAdapter(mAdapter);
                            for(User u:friends) Log.i("Jing",u.getDisplayName());
                        }
                    });
                }
            }
        });
//        for(String name : friendName){
//            Log.i("Jing",name);
//            db.collection("User").whereEqualTo("username",name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                @Override
//                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                    for(QueryDocumentSnapshot qs:queryDocumentSnapshots){
//                        friends.add(qs.toObject(User.class));
//                    }
//                }
//            });
//        }
    }
}
