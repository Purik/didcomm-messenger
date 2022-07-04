package com.socialsirius.messenger.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.StringRes;

import com.google.gson.Gson;
import com.socialsirius.messenger.base.App;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


public class Utils {
    private final static String TAG = Utils.class.getName();
    public static final int RC_PROFILE_IMAGE_FIELD_CAMERA = 3;
    public static final int RC_PROFILE_IMAGE_FIELD_CAMERA_VIDEO = 6;
    public static final int RC_PROFILE_IMAGE_FIELD_GALLERY = 4;
    public static final int RC_PROFILE_IMAGE_FIELD_FILE = 5;
    public static final Gson GSON = new Gson();

    public static String getTranslation(String code) {
        @StringRes int resId = getStrId(App.getContext(), code);
        return App.getContext().getString(resId);
    }

    public static void  makeScreenshotUnavailable(Activity activity) {
         /*   if (activity != null) {
                val window = activity.window
                window?.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            }*/
    }

    public static  @StringRes int getStrId(Context context, String name) {
        int id = context.getResources().getIdentifier(name, "string", context.getPackageName());
        return id;
    }

    public static void logLongText(String TAG, String sb) {
        if (sb == null) {
            Log.v(TAG, sb);
            return;
        }
        if (sb.length() > 2900) {
            Log.v(TAG, "sb.length = " + sb.length());
            int chunkCount = sb.length() / 2900;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 2900 * (i + 1);
                if (max >= sb.length()) {
                    Log.v(TAG, "chunk " + i + " of " + chunkCount + ":" + sb.substring(2900 * i));
                } else {
                    Log.v(TAG, "chunk " + i + " of " + chunkCount + ":" + sb.substring(2900 * i, max));
                }
            }
        } else {
            Log.v(TAG, sb);
        }
    }




    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }



    public static int dipToPixels(int dipValue) {
        Resources r = App.getInstance().getResources();
        int px = (int) (dipValue * r.getDisplayMetrics().density + 0.5f);
        return px;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }


    public static void saveDownloadedImage(String localPath, Bitmap bitmap) {
        FileOutputStream out = null;
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String path = dir.getPath() + File.separator + localPath;
        try {
            out = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap loadImage(String localPath) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String path = dir.getPath() + File.separator + localPath;
        try {
            Bitmap bmp = BitmapFactory.decodeFile(path);
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static int[] getBitmapSize(Context context, Uri uri, int size) {
        InputStream input = null;
        int[] res = new int[2];
        try {
            input = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            if ((onlyBoundsOptions.outWidth != -1) && (onlyBoundsOptions.outHeight != -1)) {

                double originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ?
                        onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

                double ratio = (originalSize > size) ? (originalSize / size) : 1.0;
                int divider = getPowerOfTwoForSampleRatio(ratio);
                res[0] = onlyBoundsOptions.outWidth / divider;
                res[1] = onlyBoundsOptions.outHeight / divider;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static Bitmap getThumbnail(Context context, Uri uri, int size) {
        Bitmap res = null;
        InputStream input = null;
        InputStream input2 = null;
        try {
            input = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;//optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            if ((onlyBoundsOptions.outWidth != -1) && (onlyBoundsOptions.outHeight != -1)) {

                double originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ?
                        onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

                double ratio = (originalSize > size) ? (originalSize / size) : 1.0;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
                bitmapOptions.inDither = true;//optional
                bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
                input2 = context.getContentResolver().openInputStream(uri);
                res = BitmapFactory.decodeStream(input2, null, bitmapOptions);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (input2 != null) input2.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }



    public static void showKeyboard(Activity activity, final EditText editText) {
        if (activity == null){
            return;
        }
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                imm.showSoftInput(editText, 0);
            }
        }, 100);
    }

    public static void hideKeyboard(Activity activity, EditText editText) {
        if(activity == null){
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (editText != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public static void hideKeyboardWitouAnim(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (editText != null) {
            // imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public static void hideKeyboardWitoutAnimWithView(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            // imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void playSound(Context context, int soundId) {
        MediaPlayer soundCLickMainMenu = MediaPlayer.create(context, soundId);
        soundCLickMainMenu.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });

    }

    public static final String[] indexcolors = new String[]{
            "#FFFF00", "#1CE6FF", "#FF34FF", "#FF4A46", "#008941", "#006FA6", "#A30059",
            "#FFDBE5", "#7A4900", "#0000A6", "#63FFAC", "#B79762", "#004D43", "#8FB0FF", "#997D87",
            "#5A0007", "#809693", "#FEFFE6", "#1B4400", "#4FC601", "#3B5DFF", "#4A3B53", "#FF2F80",
            "#61615A", "#BA0900", "#6B7900", "#00C2A0", "#FFAA92", "#FF90C9", "#B903AA", "#D16100",
            "#DDEFFF", "#000035", "#7B4F4B", "#A1C299", "#300018", "#0AA6D8", "#013349", "#00846F",
            "#372101", "#FFB500", "#C2FFED", "#A079BF", "#CC0744", "#C0B9B2", "#C2FF99", "#001E09",
            "#00489C", "#6F0062", "#0CBD66", "#EEC3FF", "#456D75", "#B77B68", "#7A87A1", "#788D66",
            "#885578", "#FAD09F", "#FF8A9A", "#D157A0", "#BEC459", "#456648", "#0086ED", "#886F4C",

            "#34362D", "#B4A8BD", "#00A6AA", "#452C2C", "#636375", "#A3C8C9", "#FF913F", "#938A81",
            "#575329", "#00FECF", "#B05B6F", "#8CD0FF", "#3B9700", "#04F757", "#C8A1A1", "#1E6E00",
            "#7900D7", "#A77500", "#6367A9", "#A05837", "#6B002C", "#772600", "#D790FF", "#9B9700",
            "#549E79", "#FFF69F", "#201625", "#72418F", "#BC23FF", "#99ADC0", "#3A2465", "#922329",
            "#5B4534", "#FDE8DC", "#404E55", "#0089A3", "#CB7E98", "#A4E804", "#324E72", "#6A3A4C",
            "#83AB58", "#001C1E", "#D1F7CE", "#004B28", "#C8D0F6", "#A3A489", "#806C66", "#222800",
            "#BF5650", "#E83000", "#66796D", "#DA007C", "#FF1A59", "#8ADBB4", "#1E0200", "#5B4E51",
            "#C895C5", "#320033", "#FF6832", "#66E1D3", "#CFCDAC", "#D0AC94", "#7ED379", "#012C58",

            "#7A7BFF", "#D68E01", "#353339", "#78AFA1", "#FEB2C6", "#75797C", "#837393", "#943A4D",
            "#B5F4FF", "#D2DCD5", "#9556BD", "#6A714A", "#001325", "#02525F", "#0AA3F7", "#E98176",
            "#DBD5DD", "#5EBCD1", "#3D4F44", "#7E6405", "#02684E", "#962B75", "#8D8546", "#9695C5",
            "#E773CE", "#D86A78", "#3E89BE", "#CA834E", "#518A87", "#5B113C", "#55813B", "#E704C4",
            "#00005F", "#A97399", "#4B8160", "#59738A", "#FF5DA7", "#F7C9BF", "#643127", "#513A01",
            "#6B94AA", "#51A058", "#A45B02", "#1D1702", "#E20027", "#E7AB63", "#4C6001", "#9C6966",
            "#64547B", "#97979E", "#006A66", "#391406", "#F4D749", "#0045D2", "#006C31", "#DDB6D0",
            "#7C6571", "#9FB2A4", "#00D891", "#15A08A", "#BC65E9", "#FFFFFE", "#C6DC99", "#203B3C",

            "#671190", "#6B3A64", "#F5E1FF", "#FFA0F2", "#CCAA35", "#374527", "#8BB400", "#797868",
            "#C6005A", "#3B000A", "#C86240", "#29607C", "#402334", "#7D5A44", "#CCB87C", "#B88183",
            "#AA5199", "#B5D6C3", "#A38469", "#9F94F0", "#A74571", "#B894A6", "#71BB8C", "#00B433",
            "#789EC9", "#6D80BA", "#953F00", "#5EFF03", "#E4FFFC", "#1BE177", "#BCB1E5", "#76912F",
            "#003109", "#0060CD", "#D20096", "#895563", "#29201D", "#5B3213", "#A76F42", "#89412E",
            "#1A3A2A", "#494B5A", "#A88C85", "#F4ABAA", "#A3F3AB", "#00C6C8", "#EA8B66", "#958A9F",
            "#BDC9D2", "#9FA064", "#BE4700", "#658188", "#83A485", "#453C23", "#47675D", "#3A3F00",
            "#061203", "#DFFB71", "#868E7E", "#98D058", "#6C8F7D", "#D7BFC2", "#3C3E6E", "#D83D66",

            "#2F5D9B", "#6C5E46", "#D25B88", "#5B656C", "#00B57F", "#545C46", "#866097", "#365D25",
            "#252F99", "#00CCFF", "#674E60", "#FC009C", "#92896B"
    };




    public static boolean isPortraitMode(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

    public static final float ASPECT_RATIO_TOLERANCE = 0.01f;


    public static void backspace(EditText editText) {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }


    private void getDropboxIMGSize(Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

}
