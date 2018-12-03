package com.example.a15862.mytraveldiary;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a15862.mytraveldiary.DAO.DiaryDAO;
import com.example.a15862.mytraveldiary.Entity.Diary;


/**
 * A simple {@link Fragment} subclass.
 */
public class CRUDFragment extends DialogFragment {
    Diary curDiary;
    public CRUDFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crud, container, false);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle b = getArguments();
        curDiary = (Diary)b.getSerializable("Diary");
        builder.setTitle("Actions on diary:")
                .setItems(R.array.CRUD, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            Intent i = new Intent(getActivity(),ModifyActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable("Diary",curDiary);
                            i.putExtras(b);
                            startActivity(i);
                        }
                        else if(which==1){

                        }
                        else if(which==2){
                            DiaryDAO dd = new DiaryDAO();
                            dd.delete(curDiary);
                        }
                    }
                });
        return builder.create();
    }
}
