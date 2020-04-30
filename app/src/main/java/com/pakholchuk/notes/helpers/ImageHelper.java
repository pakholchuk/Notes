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

public class ImageHelper {
    private Context context;

    public ImageHelper(Context context) {
        this.context = context;
    }

    public Single<Bitmap> loadImage(Uri uriFrom) {
        return Single.create(emitter -> {
            emitter.onSuccess(Picasso.get().load(uriFrom).get());
        });
    }

    public Bitmap createPreviewBitmap(final Bitmap bitmap) {

        float bitmapWidth = bitmap.getWidth();
        float bitmapHeight = bitmap.getHeight();
        float ratio = bitmapWidth / bitmapHeight;
        float density = context.getResources().getDisplayMetrics().density;
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

    public String getCachedImagePath(Bitmap bitmap) throws IOException {
        Log.d("TAG", "saveImageInApp: " + Thread.currentThread());
        String path = (context
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
        String name = System.currentTimeMillis() + ".jpg";
        File file = new File(path, name);
        OutputStream stream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        stream.flush();
        stream.close();
        return file.getPath();
    }

    public static void deleteImage(String path) {
        File file = new File(path);
        file.delete();
    }

    public void deleteAllImages() {
        File fileList = context
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (fileList != null){
            File[] filenames = fileList.listFiles();
            for (File f : filenames){
                f.delete();
            }
        }
    }
}

