package com.example.a15862.mytraveldiary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.a15862.mytraveldiary.Entity.Diary;
import com.example.a15862.mytraveldiary.Entity.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddFriendsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private EditText edtSearch;
    private Button btnSearchFriends;
    private RecyclerView searchFriendsList;
    private List<User> friendList;
    private MyCustomAdapterForFriends mAdapter;
    private String searchWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        db=FirebaseFirestore.getInstance();
        edtSearch=(EditText)findViewById(R.id.edtSearch);
        btnSearchFriends=(Button)findViewById(R.id.btnSearchFriends);
        searchFriendsList=(RecyclerView)findViewById(R.id.searchFriendsList);
        searchWord="haohui";
        friendList=new ArrayList<>();

        searchFriendsList.setHasFixedSize(true);
        searchFriendsList.setLayoutManager(new LinearLayoutManager(this));
        btnSearchFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchWord= edtSearch.getText().toString();
                Log.i("searchResult",searchWord);
                db.collection("User").orderBy("displayName").startAt(searchWord).endAt(searchWord+ "\uf8ff")
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){

                        }else {
                            Log.i("searchResult","searchFinished");
                            for(DocumentSnapshot d:queryDocumentSnapshots){
                                User user=d.toObject(User.class);
                                friendList.add(user);
                                Log.i("searchResult","add user to list");
                                Log.i("searchResult",user.getDisplayName());
                            }
                        }
                        mAdapter = new MyCustomAdapterForFriends(AddFriendsActivity.this, friendList);
                        searchFriendsList.setAdapter(mAdapter);

                        mAdapter.setOnItemClickListener(new MyCustomAdapterForFriends.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                SharedPreferences load = getSharedPreferences("user",Context.MODE_PRIVATE);
                                String username=load.getString("username","DEFAULT");

                                Bundle bundle = new Bundle();
                                bundle.putString("myUsername",username);
                                bundle.putString("targetUsername",friendList.get(position).getUsername());
                                ViewAndAddFriendsFragment viewAndAddFriendsFragment = new ViewAndAddFriendsFragment();
                                viewAndAddFriendsFragment.setArguments(bundle);
                                viewAndAddFriendsFragment.show(getSupportFragmentManager(), "AddFriends");
                            }
                        });
                    }
                });

            }
        });

    }
}
