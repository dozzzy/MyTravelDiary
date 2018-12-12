package com.example.a15862.mytraveldiary;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a15862.mytraveldiary.DAO.FollowshipDAO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteFriendFragment extends DialogFragment {

    String username;
    String target;
    private FirebaseFirestore db;
    List<String> newF;
    FragmentActivity fragmentActivity;

    public DeleteFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();
        newF = null;
        return inflater.inflate(R.layout.fragment_delete_friend, container, false);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle b = getArguments();
        username = b.getString("username");
        target = b.getString("target");
        fragmentActivity = getActivity();
        builder.setTitle("You do not want this friend?").setItems(R.array.Friend, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    FollowshipDAO fd = new FollowshipDAO();

                    db.collection("Followship").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.getData() != null)
                                newF = (ArrayList) documentSnapshot.getData().get("followed");
                            else newF = new ArrayList<>();
                            Map<String, Object> data = new HashMap<>();
                            if (newF.contains(target)) newF.remove(target);
                            data.put("followed", newF);
                            db.collection("Followship").document(username).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    ((ViewAllFriendsActivity) fragmentActivity).showFriends();
                                }
                            });
                        }
                    });
                } else if (which == 1) {

                }
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
