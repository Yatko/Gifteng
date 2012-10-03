package com.venefica.utils;

import java.io.File;
import java.io.IOException;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.venefica.activity.R;
import com.venefica.activity.PostStepLogic.PostData;
import com.venefica.services.AsyncServices;
import com.venefica.services.ServicesManager;
import com.venefica.services.User;

import android.app.Application;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;

public class MyApp extends Application
{
	public static File MAIN_PATH;
	public static File TEMP_FILE;
	public static File CACHE_PATH;
	public static boolean UseSdCard;

	/** Token authentication server */
	public static String AuthToken;

	/** Work with the service manager */
	public static ServicesManager Services;

	/** Manager asynchronous work with service */
	public static AsyncServices AsyncServices;

	/** my Location */
	public static Location MyLocation = new Location(LocationManager.GPS_PROVIDER);

	public PostData Post;
	
	/** The data about the user */
	public static User user;

	public static ImageLoader ImgLoader = ImageLoader.getInstance();
	public static DisplayImageOptions ImgLoaderOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.default_photo).cacheInMemory().cacheOnDisc().build();
	
	public static boolean RememberMe = true;
	
	@Override
	public void onCreate()
	{
		File storage = Environment.getExternalStorageDirectory();
		MAIN_PATH = getCacheDir();//new File(starage, Constants.MAIN_PATH);
		TEMP_FILE = new File(storage, "temporary_image.bmp");
		CACHE_PATH = new File(MAIN_PATH, Constants.CACHE_FOLDER);

		CACHE_PATH.mkdirs();
		try
		{
			if (TEMP_FILE.exists() == false)
				TEMP_FILE.createNewFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		//problem with a crop, there are no problems CROP
		//if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		//	UseSdCard = true;
		//else
			UseSdCard = false;

		Services = new ServicesManager();
		AsyncServices = new AsyncServices();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)/*.memoryCacheSize(1500000)*/// 1.5 Mb
				.httpReadTimeout(10000) // 10 s
				.denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).enableLogging().discCacheSize(50 * 1024 * 1024) // Not necessary in common
				.build();

		ImageLoader.getInstance().init(config);
	}

	public static void SetAuthToken(String authToken)
	{
		AuthToken = authToken;
	}
}
