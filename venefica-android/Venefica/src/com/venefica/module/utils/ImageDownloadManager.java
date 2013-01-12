package com.venefica.module.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.venefica.utils.Constants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.webkit.URLUtil;
import android.widget.ImageView;
/**
 * @author avinash
 * Class to manage lazy image download for list and grids
 */
public class ImageDownloadManager {
	private final Map<String, SoftReference<Drawable>> imgCache = new HashMap<String, SoftReference<Drawable>>();   
	private final LinkedList <Drawable> imgChacheController = new LinkedList <Drawable> ();
	private ExecutorService downloadThreadPool;  
	private final Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());  

	public static int MAX_CACHE_SIZE = 80; 
	public int THREAD_POOL_SIZE = 5;
	private static ImageDownloadManager instance;
	/**
	 * Constructor
	 */
	private ImageDownloadManager() {
		downloadThreadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	}

	public static ImageDownloadManager getImageDownloadManagerInstance(){
		if (instance == null) {
			instance = new ImageDownloadManager(); 
		}		
		return instance;		
	}
	/**
	 * Clears all instance data and stops running threads
	 */
	public void reset() {
	    ExecutorService oldThreadPool = downloadThreadPool;
	    downloadThreadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	    oldThreadPool.shutdownNow();

	    imgChacheController.clear();
	    imgCache.clear();
	    imageViews.clear();
	}  

	public void loadDrawable(final String url, final ImageView imageView,Drawable placeholder) {
	    imageViews.put(imageView, url);
	    Drawable drawable = getDrawableFromCache(url);  

	    // check in UI thread, so no concurrency issues  
	    if (drawable != null) {  
	        imageView.setImageDrawable(drawable);  
	    } else {  
	        imageView.setImageDrawable(placeholder);
	        if (url != null && URLUtil .isValidUrl(url)) {
	        	queueJob(url, imageView, placeholder);
	        }
	    }  
	} 


	private Drawable getDrawableFromCache(String url) {  
	    if (imgCache.containsKey(url)) {  
	        return imgCache.get(url).get();  
	    }  

	    return null;  
	}

	private synchronized void putDrawableInCache(String url,Drawable drawable) {  
	    int chacheControllerSize = imgChacheController.size();
	    if (chacheControllerSize > MAX_CACHE_SIZE) 
	        imgChacheController.subList(0, MAX_CACHE_SIZE/2).clear();

	    imgChacheController.addLast(drawable);
	    imgCache.put(url, new SoftReference<Drawable>(drawable));

	}  

	private void queueJob(final String url, final ImageView imageView,final Drawable placeholder) {  
	    /* Create handler in UI thread. */  
	    final Handler handler = new Handler() {  
	        @Override  
	        public void handleMessage(Message msg) {  
	            String tag = imageViews.get(imageView);  
	            if (tag != null && tag.equals(url)) {
	                if (imageView.isShown())
	                    if (msg.obj != null) {
	                        imageView.setImageDrawable((Drawable) msg.obj);  
	                    } else {  
	                        imageView.setImageDrawable(placeholder);  
	                    } 
	            }  
	        }  
	    };  

	    downloadThreadPool.submit(new Runnable() {  
	        @Override  
	        public void run() {  
	            final Drawable bmp = downloadDrawable(url);
	            // if the view is not visible anymore, the image will be ready for next time in cache
	            if (imageView.isShown())
	            {
	                Message message = Message.obtain();  
	                message.obj = bmp;
	                handler.sendMessage(message);
	            }
	        }  
	    });  
	}  

	
	private Drawable downloadDrawable(String url) {  
	    try {  
//	        InputStream is = getInputStream(url);
	        Drawable drawable = new BitmapDrawable( getScaledDownBitmap(url));
//	        Drawable drawable = Drawable.createFromStream(is, url);
	        putDrawableInCache(url,drawable);  
	        return drawable;  

	    } catch (MalformedURLException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }

	    return null;  
	}  

	/**
	 * Method to download image
	 * @param urlString
	 * @return response InputStream
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private InputStream getInputStream(String urlString) throws MalformedURLException, IOException {
	    URL url = new URL(urlString);
	    URLConnection connection;
	    connection = url.openConnection();
	    connection.setUseCaches(true); 
	    connection.connect();
	    InputStream response = connection.getInputStream();

	    return response;
	}
	
	private Bitmap getScaledDownBitmap(String urlString) throws MalformedURLException, IOException{
		URL url = new URL(urlString);
		HttpURLConnection connection;
	    connection = (HttpURLConnection)url.openConnection();
	    connection.setUseCaches(true); 
	    connection.connect();
	    InputStream is = connection.getInputStream();
	    Rect rect = new Rect(0, 0, Constants.IMAGE_MAX_SIZE_X, Constants.IMAGE_MAX_SIZE_Y);
	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    opts.inInputShareable = false;
	    opts.inSampleSize = 1;
	    Bitmap bm = BitmapFactory.decodeStream(is, rect, opts);
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) Constants.IMAGE_MAX_SIZE_X) / width;
	    float scaleHeight = ((float) Constants.IMAGE_MAX_SIZE_Y) / height;

	    // create a matrix for the manipulation
	    Matrix matrix = new Matrix();

	    // resize the bit map
	    matrix.postScale(scaleWidth, scaleHeight);

	    // recreate the new Bitmap
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

	    return resizedBitmap;		
	}
}
