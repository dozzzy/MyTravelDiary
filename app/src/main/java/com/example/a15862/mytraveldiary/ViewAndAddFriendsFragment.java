package com.example.a15862.mytraveldiary;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a15862.mytraveldiary.DAO.DiaryDAO;
import com.example.a15862.mytraveldiary.DAO.FollowshipDAO;
import com.example.a15862.mytraveldiary.Entity.Diary;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewAndAddFriendsFragment extends DialogFragment {

    private String myUsername;
    private String targetUsername;
    public ViewAndAddFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myUsername = getArguments().getString("myUsername");
        targetUsername = getArguments().getString("targetUsername");
        return inflater.inflate(R.layout.fragment_view_and_add_friends, container, false);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Would you like follow this user?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        FollowshipDAO fDAO=new FollowshipDAO();
                        fDAO.follow(myUsername,targetUsername);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        return builder.create();
    }

}
