package com.venefica.utils;

import java.io.File;
import java.io.IOException;

import android.app.Application;
import android.os.Environment;

import com.venefica.services.User;

public class VeneficaApplication extends Application {
	/** Token authentication server */
	public static String authToken;


	/** The data about the user */
	public static User user;

	@Override
	public void onCreate() {
		
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
}
