package com.example.alejandro.udlamsg.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by oswaldo on 22/11/2016.
 */
public class Imagen {

    public static int TAKE_PICTURE = 1;
    public static int SELECT_PICTURE = 2;

    //Conversión de String a Bitmap
    public Bitmap ConvertStringBItmap(String imagen){
        try {
            byte[] _imagen = Base64.decode(imagen,Base64.DEFAULT);
            Bitmap m = BitmapFactory.decodeByteArray(_imagen, 0, _imagen.length);
            return m;

        }catch (Exception e){
            return  null;
        }
    }
    // Conversión de Bitmap a String
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    // conversión de una ruta interna a Bitmap
    public Bitmap Urlmagen(String path)
    {
        String _simagen = encodeImage(path);
        Bitmap bmp = ConvertStringBItmap(_simagen);
        return bmp;

    }
    public String encodeImage(String path)
    {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;

    }




}
