package com.venefica.module.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.location.Location;
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
import android.view.animation.Transformation;
import android.widget.Toast;

import com.venefica.utils.Constants;

/**
 * @author avinash
 *	Utility class for common functionality
 */
public class Utility {	
	private static final int TWO_MINUTES = 1000 * 60 * 2;
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
	 * @return date String MM/dd/yyyy HH:mm  
	 */
	public static String convertDateTimeToString(Date date){
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
	 * Convert short date to string in MMDDYYYY format
	 * @param date
	 * @return string Date
	 */
	public static String convertDateToString(Date date){
		return Constants.dateMMDDYYYYFormat.format(date);
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
    
    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
   public static  boolean isBetterLocation(Location location, Location currentBestLocation) {
       if (currentBestLocation == null) {
           // A new location is always better than no location
           return true;
       }

       // Check whether the new location fix is newer or older
       long timeDelta = location.getTime() - currentBestLocation.getTime();
       boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
       boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
       boolean isNewer = timeDelta > 0;

       // If it's been more than two minutes since the current location, use the new location
       // because the user has likely moved
       if (isSignificantlyNewer) {
           return true;
       // If the new location is more than two minutes older, it must be worse
       } else if (isSignificantlyOlder) {
           return false;
       }

       // Check whether the new location fix is more or less accurate
       int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
       boolean isLessAccurate = accuracyDelta > 0;
       boolean isMoreAccurate = accuracyDelta < 0;
       boolean isSignificantlyLessAccurate = accuracyDelta > 200;

       // Check if the old and new location are from the same provider
       boolean isFromSameProvider = isSameProvider(location.getProvider(),
               currentBestLocation.getProvider());

       // Determine location quality using a combination of timeliness and accuracy
       if (isMoreAccurate) {
           return true;
       } else if (isNewer && !isLessAccurate) {
           return true;
       } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
           return true;
       }
       return false;
   }

   /** Checks whether two providers are the same */
   private static boolean isSameProvider(String provider1, String provider2) {
       if (provider1 == null) {
         return provider2 == null;
       }
       return provider1.equals(provider2);
   }
   
   /** Check if this device has a camera */
   public static boolean checkCameraHardware(Context context) {
       if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
           // this device has a camera
           return true;
       } else {
           // no camera on this device
           return false;
       }
   }
   /** A safe way to get an instance of the Camera object. */
   public static Camera getCameraInstance(){
       Camera c = null;
       try {
           c = Camera.open(); // attempt to get a Camera instance
       }
       catch (Exception e){
           // Camera is not available (in use or does not exist)
       }
       return c; // returns null if camera is unavailable
   }
   
	/**
	 * Shifts the given Date to the same time at the given day. This uses the
	 * current time zone.
	 */
	public static Date shiftDate(Date date, int daysToShift) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, daysToShift);
		date.setTime(c.getTimeInMillis());
		return date;
	}
	
	/**
	 * Method to convert list to string
	 * @param list
	 * @return String
	 */
	public static String getStringFromList(ArrayList<Long> list) {
		StringBuffer stringBuf = new StringBuffer();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//			Long long1 = (Long) iterator.next();
			stringBuf.append((Long) iterator.next());
			if(iterator.hasNext())
				stringBuf.append(",");
		}
		/*for (Long value : list) {
			stringBuf.append(value);
			stringBuf.append(",");
		}*/				
		return stringBuf.toString();
	}
	
	/**
	 * Method to convert string to list
	 * @param string String
	 * @return ArrayList<Long> 
	 */
	public static ArrayList<Long> getListFromString(String string){
		List<String> list = Arrays.asList(string.split("\\s*,\\s*"));
		ArrayList<Long> longs = new ArrayList<Long>();
		for (String value : list) {
			longs.add(Long.parseLong(value));
		}
		return longs;
	}
}
