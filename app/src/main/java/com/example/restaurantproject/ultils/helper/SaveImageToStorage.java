package com.example.restaurantproject.ultils.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveImageToStorage {
    private Context context;

    public SaveImageToStorage(Context context) {
        this.context = context;
    }
    public String saveImageToStorage(Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());

        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageName = "image_" + timeStamp + ".png";


        File mypath = new File(directory, imageName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mypath.getAbsolutePath();
    }

}
