package com.example.a15862.mytraveldiary;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends DialogFragment {

    placeInfoReceiver context;
//    private EditText edtKeyWord;

    public SearchFragment() {
        // Required empty public constructor
    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_search, container, false);
//        edtKeyWord = view.findViewById(R.id.edtKeyWord);
//
////        return inflater.inflate(R.layout.fragment_search, container, false);
//        return view;
//    }



//    public interface SearchDialogListener {
//        public void onDialogPositiveClick(DialogFragment dialog);
//        public void onDialogNegativeClick(DialogFragment dialog);
//    }

    // Use this instance of the interface to deliver action events
//    SearchDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        context = (placeInfoReceiver) activity;
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = (SearchDialogListener) activity;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(activity.toString()
//                    + " must implement NoticeDialogListener");
//        }
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fragment_search,null);

        builder.setView(dialogView);
        builder.setMessage("Search")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        EditText edtKeyWord = (EditText) dialogView.findViewById(R.id.edtKeyWord);
                        context.getCallBackFromFrag(edtKeyWord.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity

                    }
                });
        return builder.create();
    }

}
