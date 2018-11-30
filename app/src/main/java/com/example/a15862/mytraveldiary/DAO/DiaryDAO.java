package com.example.a15862.mytraveldiary.DAO;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.a15862.mytraveldiary.Entity.Diary;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class DiaryDAO {
    private FirebaseFirestore db;
    private StorageReference mStorageRef;
    private StorageTask sk;
    private Diary uploadDiary;
    public DiaryDAO() {
        db = FirebaseFirestore.getInstance();

        mStorageRef=FirebaseStorage.getInstance().getReference();
    }


    public void uploadDiary(Diary diary){
        Log.i("jingD","begin");
        uploadDiary =diary;
        if (diary.getPhotoUri()!=null){
            Log.i("jingD","have photo");
            Uri uri=Uri.parse(diary.getPhotoUri());
            // if we have photo to upload we upload photo first, and retrieve the url of photo. then save the url with other things(username, comments....) in firestore
            final StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." +uri);
            // sk record upload task
            sk=fileRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // in the past getDownloadUrl can retrieve the url in no time. But now the call can be asynchronous, so we need to use an listener
                                    // after we upload photo to storage, we restore the url of photo and other stuff to firestore
                                    Log.i("upload diary","photo in storage");
                                    uploadDiary.setPhotoUri(uri.toString());
                                    db.collection("Diary").document().set(uploadDiary).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("upload diary","photo succ");
                                        }
                                    });
                                }
                            });

                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("my",e.getMessage());
                        }
                    });
        } else {
            Log.i("jingD","no photo start");
            db.collection("Diary").document().set(uploadDiary).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("jingD","no photo succ");
                }
            });
        }
    }

//    // this method is to get the full name of a photo. (apple -> apple.jpg)
//    private String getFileExtension(Uri uri){
//        ContentResolver cR=getContentResolver();
//        MimeTypeMap mime=MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cR.getType(uri));
//    }

}
