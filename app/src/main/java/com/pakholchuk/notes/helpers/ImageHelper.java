package com.pakholchuk.notes.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class ImageHelper {

    public Single<Bitmap> loadImage(Uri uriFrom) {
        return Single.create(emitter -> {
            emitter.onSuccess(Picasso.get().load(uriFrom).get());
        });
    }

    public Bitmap createPreviewBitmap(final Bitmap bitmap, float density) {
        float bitmapWidth = bitmap.getWidth();
        float bitmapHeight = bitmap.getHeight();
        float ratio = bitmapWidth / bitmapHeight;
        float maxWidthDp = 180;
        float maxHeightDp = 90;
        Bitmap newBitmap;
        if (ratio > (maxWidthDp / maxHeightDp)) {
            newBitmap = Bitmap.createScaledBitmap(bitmap,
                    (int) (maxWidthDp * density),
                    (int) (maxWidthDp * density / ratio),
                    false);
        } else {
            newBitmap = Bitmap.createScaledBitmap(bitmap,
                    (int) (maxHeightDp * ratio * density),
                    (int) (maxHeightDp * density),
                    false);
        }
        return newBitmap;
    }

    public Single<Bitmap> getPreviewBitmap(String pathFrom, float deviceDensity) {
        return loadImage(parseUri(pathFrom))
                .map(bitmap -> createPreviewBitmap(bitmap, deviceDensity));
    }

    public String cacheImage(String appImagesDirPath, Bitmap bitmap) throws IOException {
        Log.d("TAG", "saveImageInApp: " + Thread.currentThread());
        String name = System.currentTimeMillis() + ".jpg";
        File file = new File(appImagesDirPath, name);
        OutputStream stream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        stream.flush();
        stream.close();
        return file.getPath();
    }

    public Single<String> saveImageToApp(String pathFrom, String pathTo) {

        return loadImage(parseUri(pathFrom))
                .map(bitmap -> cacheImage(pathTo, bitmap));
    }

    public Uri parseUri(String path) {
        if (!path.startsWith("file://")){
            path = "file://" + path;
        }
        return Uri.parse(path);
    }


    public void deleteImage(String path) {
        File file = new File(path);
        file.delete();
    }

    public void deleteAllImages(String imagesDirPath) {
        if (imagesDirPath != null){
            File dir = new File(imagesDirPath);
            File[] filenames = dir.listFiles();
            for (File f : filenames){
                f.delete();
            }
        }
    }
}

