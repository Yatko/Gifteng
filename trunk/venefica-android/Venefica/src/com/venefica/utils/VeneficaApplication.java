package com.venefica.utils;

import android.app.Application;
import com.venefica.module.user.UserDto;
import com.venefica.module.utils.ImageDownloadManager;

public class VeneficaApplication extends Application {
	/** Token authentication server */
	private String authToken;


	/** The data about the user */
	private UserDto user;
	
	private ImageDownloadManager imgManager;
	@Override
	public void onCreate() {
		super.onCreate();
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
	public UserDto getUser() {
		return user;
	}

	/**
	 * @param userDto the user to set
	 */
	public void setUser(UserDto userDto) {
		user = userDto;
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
