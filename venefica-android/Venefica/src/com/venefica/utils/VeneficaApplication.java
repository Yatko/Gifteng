package com.venefica.utils;

import java.io.File;
import java.io.IOException;

import com.venefica.services.ServicesManager;
import com.venefica.services.User;

import android.app.Application;
import android.os.Environment;

public class VeneficaApplication extends Application {
	public static File MAIN_PATH;
	public static File TEMP_FILE;
	public static File CACHE_PATH;
	public static boolean useSdCard;

	/** Token authentication server */
	public static String authToken;

	/** Work with the service manager */
	public static ServicesManager services;

	/** The data about the user */
	public static User user;

	public static boolean rememberMe = true;

	@Override
	public void onCreate() {
		File storage = Environment.getExternalStorageDirectory();
		MAIN_PATH = getCacheDir();// new File(starage, Constants.MAIN_PATH);
		TEMP_FILE = new File(storage, "temporary_image.bmp");
		CACHE_PATH = new File(MAIN_PATH, Constants.CACHE_FOLDER);

		CACHE_PATH.mkdirs();
		try {
			if (TEMP_FILE.exists() == false)
				TEMP_FILE.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		useSdCard = false;

		services = new ServicesManager();
	}

	/**
	 * @return the authToken
	 */
	public String getAuthToken() {
		return authToken;
	}

	/**
	 * @param authToken
	 *            the authToken to set
	 */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
}
