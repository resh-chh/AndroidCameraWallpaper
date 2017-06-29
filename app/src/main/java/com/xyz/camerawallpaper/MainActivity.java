package com.xyz.camerawallpaper;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView ivPhoto;
    Button btnTakePhoto, btnSetWallpaper, btnSave;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPhoto= (ImageView) findViewById(R.id.ivPhoto);
        btnTakePhoto= (Button) findViewById(R.id.btnTakePhoto);
        btnSetWallpaper= (Button) findViewById(R.id.btnSetWallpaper);
        btnSave= (Button) findViewById(R.id.btnSave);

        btnSetWallpaper.setEnabled(false);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 100);
            }
        });

        btnSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WallpaperManager wallpaperManager=WallpaperManager.getInstance(getApplicationContext());
                try {
                    wallpaperManager.setBitmap(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String state= Environment.getExternalStorageState();

                if(Environment.MEDIA_MOUNTED.equalsIgnoreCase(state))
                {
                    File root= Environment.getExternalStorageDirectory();
                    File dir= new File(root.getAbsolutePath() + "/ClassTesting");

                    if(!dir.exists())
                        dir.mkdir();

                    SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmmssSS");
                    Date date=new Date();
                    String fname= sdf.format(date) + ".jpg";

                    File file= new File(dir, fname);

                    try{
                        FileOutputStream fos= new FileOutputStream(file);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        photo.compress(Bitmap.CompressFormat.JPEG, 90, bos);
                        Toast.makeText(getApplicationContext(), "file saved", Toast.LENGTH_LONG).show();
                        bos.flush();
                        bos.close();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "file could not be saved " + e, Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                    Toast.makeText(getApplicationContext(), "External Storage Isseue", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            if(requestCode==100){
                photo=(Bitmap)data.getExtras().get("data");
                ivPhoto.setImageBitmap(photo);
                btnSetWallpaper.setEnabled(true);
            }
        }
    }
}
