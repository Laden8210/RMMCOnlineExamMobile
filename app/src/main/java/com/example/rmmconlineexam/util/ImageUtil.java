package com.example.rmmconlineexam.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rmmconlineexam.R;

import java.io.ByteArrayOutputStream;


public class ImageUtil {

    public static void createImage(Context context, String base64Image, ImageView imageView){
        try{
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(context).load(decodedByte).placeholder(R.drawable.loading).into(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String convertToBase64(ImageView imageView){
        try{
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
