package com.venefica.utils;

import java.io.File;
import java.io.IOException;

import android.app.Application;
import android.os.Environment;

import com.venefica.module.utils.ImageDownloadManager;
import com.venefica.services.User;

public class VeneficaApplication extends Application {
	/** Token authentication server */
	public static String authToken;


	/** The data about the user */
	public static User user;
	
	private ImageDownloadManager imgManager;
	@Override
	public void onCreate() {
		imgManager = new ImageDownloadManager(getApplicationContext());
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

	/**
	 * @return the user
	 */
	public static User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public static void setUser(User user) {
		VeneficaApplication.user = user;
	}

	/**
	 * @return the imgManager
	 */
	public ImageDownloadManager getImgManager() {
		return imgManager;
	}

	/**
	 * @param imgManager the imgManager to set
	 */
	public void setImgManager(ImageDownloadManager imgManager) {
		this.imgManager = imgManager;
	}
}
