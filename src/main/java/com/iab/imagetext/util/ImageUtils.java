package com.iab.imagetext.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static Bitmap getResampleImageBitmap(Uri uri, Context context, int maxDim) throws IOException {
        String pathInput = getRealPathFromURI(uri, context);
        Bitmap bmp = null;
        try {
            bmp = resampleImage(pathInput, maxDim);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static Bitmap getResampleImageBitmap(Uri uri, Context context) throws IOException {
        String pathInput = getRealPathFromURI(uri, context);
        Bitmap bmp = null;
        try {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            bmp = resampleImage(pathInput, width);
        } catch (Exception e) {
            e.printStackTrace();
            bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(pathInput));
        }
        //OutputStream out = new FileOutputStream(pathOutput);
        //bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        return bmp;
    }

    @SuppressLint("UseValueOf")
    public static Bitmap resampleImage(String path, int maxDim) throws Exception {
        try {
            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, bfo);

            //maxDim = bfo.outWidth;

            BitmapFactory.Options optsDownSample = new BitmapFactory.Options();
            optsDownSample.inSampleSize = getClosestResampleSize(bfo.outWidth, bfo.outHeight, maxDim);


            Bitmap bmpt = BitmapFactory.decodeFile(path, optsDownSample);
            if (bmpt == null)
                return null;

            Matrix m = new Matrix();

            if (bmpt.getWidth() > maxDim || bmpt.getHeight() > maxDim) {
                BitmapFactory.Options optsScale = getResampling(bmpt.getWidth(), bmpt.getHeight(), maxDim);
                m.postScale((float) optsScale.outWidth / (float) bmpt.getWidth(), (float) optsScale.outHeight / (float) bmpt.getHeight());
            }

            @SuppressWarnings("deprecation")
            int sdk = new Integer(Build.VERSION.SDK).intValue();
            if (sdk > 4) {
                int rotation = ExifUtils.getExifRotation(path);
                if (rotation != 0) {
                    m.postRotate(rotation);
                }
            }
            return Bitmap.createBitmap(bmpt, 0, 0, bmpt.getWidth(), bmpt.getHeight(), m, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BitmapFactory.Options getResampling(int cx, int cy, int max) {
        float scaleVal = 1.0f;
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        if (cx > cy) {
            scaleVal = (float) max / (float) cx;
        } else if (cy > cx) {
            scaleVal = (float) max / (float) cy;
        } else {
            scaleVal = (float) max / (float) cx;
        }
        bfo.outWidth = (int) (cx * scaleVal + 0.5f);
        bfo.outHeight = (int) (cy * scaleVal + 0.5f);
        return bfo;
    }

    public static int getClosestResampleSize(int cx, int cy, int maxDim) {
		/*Log.e("cx",""+cx);
         Log.e("cy",""+cy);*/
        int max = Math.max(cx, cy);

        int resample = 1;
        for (resample = 1; resample < Integer.MAX_VALUE; resample++) {
            if (resample * maxDim > max) {
                resample--;
                break;
            }
        }

        if (resample > 0) {
            return resample;
        }
        return 1;
    }

    public static BitmapFactory.Options getBitmapDims(Uri uri, Context context) {
        String path = getRealPathFromURI(uri, context);
        Log.i("texting", "Path " + path);
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bfo);
        return bfo;
    }

//    public static String getRealPathFromURI(Uri contentURI, Context context) {
//        try {
//            String result;
//            Cursor cursor = context.getContentResolver().query(contentURI, null, null,
//                    null, null);
//            if (cursor == null) {
//
//                result = contentURI.getPath();
//            } else {
//                cursor.moveToFirst();
//                int idx = cursor
//                        .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                result = cursor.getString(idx);
//                cursor.close();
//            }
//            return result;
//        } catch (Exception e) {
//            CrashlyticsTracker.report(e, "Exception");
//            e.printStackTrace();
//        }
//        return contentURI.toString();
//    }

    public static String getRealPathFromURI(Uri contentURI, Context context) {
        try {
            String result;
            String[] projection = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentURI, projection, null,
                    null, null);
            if (cursor == null) {

                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA);
                result = cursor.getString(idx);
                cursor.close();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentURI.toString();
    }

    public static String getPathFromGooglePhotosUri(Activity context, Uri uriPhoto) {
        if (uriPhoto == null)
            return null;

        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uriPhoto, "r");
            FileDescriptor fd = pfd.getFileDescriptor();
            input = new FileInputStream(fd);

            String tempFilename = getTempFilename(context);
            output = new FileOutputStream(tempFilename);

            int read;
            byte[] bytes = new byte[4096];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
            return tempFilename;
        } catch (IOException ignored) {
            // Nothing we can do
        } finally {
            closeSilently(input);
            closeSilently(output);
        }
        return null;
    }

    public static void closeSilently(Closeable c) {
        if (c == null)
            return;
        try {
            c.close();
        } catch (Throwable t) {
            // Do nothing
        }
    }

    private static String getTempFilename(Context context) throws IOException {
        File outputDir = context.getCacheDir();
        File outputFile = File.createTempFile("image", "tmp", outputDir);
        return outputFile.getAbsolutePath();
    }

    public static Bitmap resizeBitmap(Bitmap bit, int width, int height) {
        try {
            if (bit == null)
                return null;

            float wd = bit.getWidth();
            float he = bit.getHeight();

            float ratio = (float) bit.getWidth() / (float) bit.getHeight();
            float nw1, nh1, nw2, nh2;

            nw1 = (float) width;
            nh1 = ((float) width) / ratio;

            nw2 = ((float) height) * ratio;
            nh2 = (float) height;

            if (nw1 <= width && nh1 <= height) {
                wd = (int) nw1;
                he = (int) nh1;

            } else if (nw2 <= width && nh2 <= height) {
                wd = (int) nw2;
                he = (int) nh2;

            }

            bit = Bitmap.createScaledBitmap(bit, (int) wd, (int) he, false);
            return bit;
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
        return bit;
    }

   /* public static float dpToPx(Context c, float dp) {
        return (dp * c.getResources().getSystem().getDisplayMetrics().density);
    }*/

    public static int dpToPx(Context context,int dp) {
        if (context != null) {
            // Get the screen's density scale
            final float density = context.getResources().getDisplayMetrics().density;
            // Convert the dps to pixels, based on density scale
            return (int) (dp * density + 0.5f);
        } else {
            return -1;
        }
    }

    public static Bitmap getTiledBitmap(Context ctx, int resId, int width, int height) {
        Rect rect = new Rect(0, 0, width, height);
        Paint paint = new Paint();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap t = BitmapFactory.decodeResource(ctx.getResources(), resId, options);
        paint.setShader(new BitmapShader(t, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        canvas.drawRect(rect, paint);
        return b;
    }

    public static Bitmap getColoredBitmap(int color, int width, int height) {
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        mCanvas.drawColor(color);
        return result;
    }

//    public static CharSequence getSpannableString(Context ctx, Typeface ttf, int stringId) {
//        SpannableString spannableString = new SpannableString(ctx.getResources().getString(stringId));
//        spannableString.setSpan(new CustomTypefaceSpan(ttf), 0, ctx.getResources().getString(stringId).length(), 0);
//        SpannableStringBuilder builder = new SpannableStringBuilder().append(spannableString);
//        return builder.subSequence(0, builder.length());
//    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Check if Drawable Width and Height are zeros
        if (reqWidth <= 0) {
            reqWidth = 1;
        }
        if (reqHeight <= 0) {
            reqHeight = 1;
        }
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static int[] getSizeUsingRatio(int ratioX, int ratioY, int width, int height) {
        int[] size = {width, height};
        float nw1, nh1, nw2, nh2;
        float ratio = (float) ratioX / (float) ratioY;

        nw1 = (float) width;
        nh1 = ((float) width) / ratio;

        nw2 = ((float) height) * ratio;
        nh2 = (float) height;

        if (nw1 <= width && nh1 <= height) {
            size[0] = (int) nw1;
            size[1] = (int) nh1;

        } else if (nw2 <= width && nh2 <= height) {
            size[0] = (int) nw2;
            size[1] = (int) nh2;

        }
        return size;
    }

    public static Bitmap getImageBitmapBestSize(String temp_path, Context context) {
        Bitmap bitmap = null;
        try {
            File file1 = new File(temp_path);
            if (file1.exists()) {
                Uri uri = Uri.parse(temp_path);

                float size = 1.0f;
                while (size > 0.1f) {
                    try {
                        bitmap = initCropData(size, uri, context, 0.0f, 0.0f, 1.0f, 1.0f);
                        break;
                    } catch (Error | Exception e) {
                        //e.printStackTrace();
                        size = size - 0.10f;
                    }
                }
            }
        } catch (Error | Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    public static Bitmap initCropData(float size, Uri uriStr, Context context, float Xr, float Yr, float Wr, float Hr) throws IOException {
        Bitmap bitmap = null;

        String path = ImageUtils.getRealPathFromURI(uriStr, context);
        Uri uri = Uri.parse("file://" + path);
        BitmapFactory.Options bitmapDimsMax = ImageUtils.getBitmapDims(uri, context);
        int rotation = ExifUtils.getExifRotation(path);

        float maxWidth = bitmapDimsMax.outWidth * size, maxHeight = bitmapDimsMax.outHeight * size;
        if (rotation != 0) {
            if (rotation == 90 || rotation == 270) {
                maxWidth = bitmapDimsMax.outHeight * size;
                maxHeight = bitmapDimsMax.outWidth * size;
            }
        }
        bitmap = ImageUtils.getResampleImageBitmap(uri, context, (int) ((maxWidth > maxHeight) ? maxWidth : maxHeight));

        int left = (int) (bitmap.getWidth() * Xr);
        int top = (int) (bitmap.getHeight() * Yr);
        int width = (int) (bitmap.getWidth() * Wr);
        int height = (int) (bitmap.getHeight() * Hr);

        bitmap = Bitmap.createBitmap(bitmap, left, top, width, height);

        return bitmap;
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri, float screenWidth, float screenHeight) throws IOException {
        Bitmap image = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                contentResolver.getPersistedUriPermissions();
            }
            ParcelFileDescriptor parcelFileDescriptor =
                    contentResolver.openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, bfo);

            BitmapFactory.Options optsDownSample = new BitmapFactory.Options();
            int maxDim = (int) ((screenWidth > screenHeight) ? screenWidth : screenHeight);
            optsDownSample.inSampleSize = ImageUtils.getClosestResampleSize(bfo.outWidth, bfo.outHeight, maxDim);


            image = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, optsDownSample);

            Matrix m = new Matrix();

            if (image.getWidth() > maxDim || image.getHeight() > maxDim) {
                BitmapFactory.Options optsScale = ImageUtils.getResampling(image.getWidth(), image.getHeight(), maxDim);
                m.postScale((float) optsScale.outWidth / (float) image.getWidth(), (float) optsScale.outHeight / (float) image.getHeight());
            }
            String pathInput = ImageUtils.getRealPathFromURI(uri, context);

            @SuppressWarnings("deprecation")
            int sdk = new Integer(Build.VERSION.SDK).intValue();
            if (sdk > 4) {
                int rotation = ExifUtils.getExifRotation(pathInput);
                if (rotation != 0) {
                    m.postRotate(rotation);
                }
            }

            image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), m, true);

            parcelFileDescriptor.close();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return image;
    }



}
