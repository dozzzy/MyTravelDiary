package com.example.a15862.mytraveldiary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AddDiaryActivity extends Activity {

    private Button button1, button2;
    private ImageView imageView1;
    private final int IMAGE_RESULT_CODE = 2;// 表示打开照相机
    private final int PICK = 1;// 选择图片库

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        imageView1 = (ImageView) findViewById(R.id.imageView);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, IMAGE_RESULT_CODE);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK);
            }
        });

    }


        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 表示 调用照相机拍照
            case IMAGE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    imageView1.setImageBitmap(bitmap);
                }
                break;
            // 选择图片库的图片
            case PICK:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    imageView1.setImageURI(uri);
                }
                break;
        }

    }
}
