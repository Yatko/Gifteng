package com.venefica.module.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;

import com.venefica.utils.Constants;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.Toast;
import android.view.animation.Transformation;

/**
 * @author avinash
 *	Utility class for common functionality
 */
public class Utility {	
	/**
	 * Method to display long duration Toast
	 * @param context
	 * @param message
	 */
	public static void showLongToast(Context context, String message){
		Toast.makeText(context,message, Toast.LENGTH_LONG).show();
	}

	/**
	 * Method to display short duration Toast
	 * @param context
	 * @param message
	 */
	public static void showShortToast(Context context, String message){
		Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Method to display custom duration Toast
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void showToast(Context context, String message, int duration){
		Toast.makeText(context,message, duration).show();
	}

	/**
	 * Method to merge to Arrays
	 * @param first String Array
	 * @param second String Array
	 * @return result Array first+second
	 */
	public static String [] concatArray(String[] first, String[] second) {
		String[] result= new String[first.length+second.length];
		System.arraycopy(first, 0, result, 0, first.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	/**
	 * Get configured email account.
	 * @param context
	 * @return email of user
	 */
	public static String getEmail(Context context) {
		AccountManager accountManager = AccountManager.get(context);
		Account account = getAccount(accountManager);

		if (account == null) {
			return null;
		} else {
			return account.name;
		}
	}
	/**
	 * To get specified account type. 
	 * @param accountManager
	 * @return
	 */
	private static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];
		} else {
			account = null;
		}
		return account;
	}
	/**
	 * Method to get mobile no
	 * @param context
	 * @return phone number of user
	 */
	public static String getPhoneNo(Context context){
		TelephonyManager telephonyMgr =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyMgr.getLine1Number();
	}

	/**
	 * Method to get the users facebook profile pic
	 * @param userID
	 */
	public static Bitmap getUserFacebookPic(String userID) {
		String imageURL;
		Bitmap bitmap = null;
		imageURL = "http://graph.facebook.com/"+userID+"/picture?type=small";
		try {
			bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageURL).getContent());
		} catch (Exception e) {
			Log.e("Utility::getUserFacebookPic", "Loading Picture FAILED");
			e.printStackTrace();
		}
		return bitmap;
	}
	/**
	 * Convert date to string 
	 * @param date
	 * @return
	 */
	public static String convertDateToString(Date date){
		return Constants.dateTimeFormat.format(date);
	}
	/**
	 * Convert short date to string
	 * @param date
	 * @return
	 */
	public static String convertShortDateToString(Date date){
		return Constants.dateFormat.format(date);
	}
	
	/**
	 * Get service transport type
	 * @param url
	 * @return
	 */
	public static HttpTransportSE getServicesTransport(String url){
		if (Constants.USE_SSL_SERVICES_TRANSPORT)
			return new HttpsTransportSE(url);
		else
			return new HttpTransportSE(url);
	}
	
	/**
	 * Expand view vertically with animation
	 * @param v
	 */
	public static void expand(final View v) {
	    v.measure(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	    final int targtetHeight = v.getMeasuredHeight();

	    v.getLayoutParams().height = 0;
	    v.setVisibility(View.VISIBLE);
	    Animation a = new Animation(){
	        @Override
	        protected void applyTransformation(float interpolatedTime, Transformation t) {
	            v.getLayoutParams().height = interpolatedTime == 1
	                    ? LayoutParams.WRAP_CONTENT
	                    : (int)(targtetHeight * interpolatedTime);
	            v.requestLayout();
	        }

	        @Override
	        public boolean willChangeBounds() {
	            return true;
	        }
	    };

	    // 1dp/ms
	    a.setDuration((int)(targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
	    v.startAnimation(a);
	}

	/**
	 * Collapse view vertically with animation.
	 * @param v
	 */
	public static void collapse(final View v) {
	    final int initialHeight = v.getMeasuredHeight();

	    Animation a = new Animation(){
	        @Override
	        protected void applyTransformation(float interpolatedTime, Transformation t) {
	            if(interpolatedTime == 1){
	                v.setVisibility(View.GONE);
	            }else{
	                v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
	                v.requestLayout();
	            }
	        }

	        @Override
	        public boolean willChangeBounds() {
	            return true;
	        }
	    };

	    // 1dp/ms
	    a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
	    v.startAnimation(a);
	}
	
	/**
	 * Get temp bitmap Uri
	 * @return Uri
	 */
	public static Uri getTempUri() {
		return Uri.fromFile(getTempFile());
	}
	
	/**
	 * Get temp bitmap file
	 * @return File
	 */
	public static File getTempFile() {
		if (isSDCARDMounted()) {
			
			File f = new File(Environment.getExternalStorageDirectory(),Constants.TEMP_PHOTO_FILE);
			try {
				f.createNewFile();
			} catch (IOException e) {
			}
			return f;
		} else {
			return null;
		}
	}
	
	/**
	 * Check if SDcard is inserted
	 * @return
	 */
	public static boolean isSDCARDMounted(){
        String status = Environment.getExternalStorageState();
       
        if (status.equals(Environment.MEDIA_MOUNTED))
            return true;
        return false;
    }
	
	
	/**
	 * Check external storage
	 * @return status
	 */
	@SuppressLint("NewApi")
	public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        if (hasFroyo()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }
    /**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
    @TargetApi(9)
    public static long getUsableSpace(File path) {
        if (hasGingerbread()) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }
    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }
    
    /**
     * Method to resize bitmaps
     * @param bitmap
     * @param reqWidth
     * @param reqHeight
     * @return bitmap
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int reqWidth, int reqHeight){
    	Rect rect = new Rect(0, 0, reqWidth, reqHeight);
	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    opts.inInputShareable = false;
	    opts.inSampleSize = 1;
	    opts.inScaled = false;
	    opts.inDither = false;
	    opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    float scaleWidth = ((float) reqWidth) / width;
	    float scaleHeight = ((float) reqHeight) / height;

	    // create a matrix for the manipulation
	    Matrix matrix = new Matrix();

	    // resize the bit map
	    matrix.postScale(scaleWidth, scaleHeight);

	    // recreate the new Bitmap
	    return  Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    }
    
    /**
     * Method to convert DensityPixel to Pixel
     * @param context
     * @param densityPixel
     * @return pixels
     */
    public static int convertDpToPixel(Context context, int densityPixel){
    	DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    	return (int)((densityPixel * displayMetrics.density) + 0.5);
    }
    
    /**
     * Method to convert Pixel to DensityPixel
     * @param context
     * @param pixel
     * @return densityPixel
     */
    public static int convertPxToDensityPixel(Context context, int pixel){
    	DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    	return (int) ((pixel/displayMetrics.density)+0.5);
    }
}
