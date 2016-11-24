package com.example.alejandro.udlamsg.Model;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by oswaldo on 22/11/2016.
 */
public class Camara {

    public static String APP_DIRECTORY = "MyPictureApp/";
    public static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private void openCamera() {
       /* File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }
        */
    }
}
