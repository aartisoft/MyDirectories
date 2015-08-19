package com.panaceasoft.citiesdirectory.utilities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.panaceasoft.citiesdirectory.R;
import com.panaceasoft.citiesdirectory.activities.MainActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Panacea-Soft on 7/15/15.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class Utils {

    public static MainActivity activity;
    private static Typeface fromAsset;
    private static TypefaceSpan fromAssetSpan;

    private static Fonts currentTypeface;


    public Utils(Context context){
        this.activity = (MainActivity) context;
    }

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static int pxToDp(int px) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static Point getScreenSize(){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getSize(size);
        }catch (NoSuchMethodError e) {
            // For lower than api 11
            size.x = display.getWidth();
            size.y = display.getHeight();
        }
        return size;
    }

    public static boolean isAndroid_5_0(){
        String version = Build.VERSION.RELEASE;
        if(version != "" && version != null){
            String[] versionDetail = version.split("\\.");
            Log.d("TEAMPS", "0 : " + versionDetail[0] + " 1 : " + versionDetail[1]);
            if(versionDetail[0].equals("5")){
                if(versionDetail[1].equals("0") || versionDetail[1].equals("00")){
                    return true;
                }
            }
        }

        return false;
    }

    public static void psLog(String log){
        Log.d("TEAMPS", log);
    }

    public static boolean isGooglePlayServicesOK(Activity activity) {

        final int GPS_ERRORDIALOG_REQUEST = 9001;

        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, activity, GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(activity, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }


    public static boolean isEmailFormatValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void saveBitmapImage(Context context, Bitmap b, String picName){
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (FileNotFoundException e) {
            Log.d("TEAMPS", "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("TEAMPS", "io exception");
            e.printStackTrace();
        }

    }

    public static Bitmap loadBitmapImage(Context context, String picName){
        Bitmap b = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
            fis.close();

        }
        catch (FileNotFoundException e) {
            Log.d("TEAMPS", "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("TEAMPS", "io exception");
            e.printStackTrace();
        }
        return b;
    }

    public static Typeface getTypeFace(Fonts fonts) {

        if(currentTypeface == fonts) {
            if (fromAsset == null) {
                if(fonts == Fonts.NOTO_SANS) {
                    fromAsset = Typeface.createFromAsset(activity.getAssets(), "fonts/NotoSans-Regular.ttf");
                }else if(fonts == Fonts.ROBOTO){
                    fromAsset = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Regular.ttf");
                }
            }
        }else{
            if(fonts == Fonts.NOTO_SANS){
                fromAsset = Typeface.createFromAsset(activity.getAssets(), "fonts/NotoSans-Regular.ttf");
            }else if(fonts == Fonts.ROBOTO){
                fromAsset = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Regular.ttf");
            }else{
                fromAsset = Typeface.createFromAsset(activity.getAssets(), "fonts/NotoSans-Regular.ttf");
            }
            currentTypeface = fonts;
        }
        return fromAsset;
    }


    public static TypefaceSpan getTypeFaceSpan(Fonts fonts){
        return fromAssetSpan;
    }

    public enum Fonts{
        ROBOTO,
        NOTO_SANS
    }

}
