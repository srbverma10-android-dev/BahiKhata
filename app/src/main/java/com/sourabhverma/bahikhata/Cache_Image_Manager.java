package com.sourabhverma.bahikhata;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Cache_Image_Manager {

    private final static String TAG = "TAG";

    public static Bitmap getImage (Context context, String string){

        Log.d(TAG, "getImage: " + string);

        String filename = context.getCacheDir()+"/"+string;

        File file = new File(filename);

        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;

    }

    public static void setImage(Context context,String string, Bitmap bitmap){

        Log.d(TAG, "setImage: "+ string);

        String filename = context.getCacheDir()+"/"+ string;

        File file = new File(filename);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null){

                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

}
