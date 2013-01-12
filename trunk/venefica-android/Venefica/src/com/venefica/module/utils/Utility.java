package com.venefica.module.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;

import com.venefica.utils.Constants;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
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
}
