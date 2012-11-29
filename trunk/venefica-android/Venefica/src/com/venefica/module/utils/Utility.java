package com.venefica.module.utils;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import com.venefica.utils.Constants;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

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
}
