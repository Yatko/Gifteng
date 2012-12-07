package com.venefica.module.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
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

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
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
	public int THREAD_POOL_SIZE = 3;
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
	        //Log.d(null, "Item loaded from imgCache: " + url);  
	        imageView.setImageDrawable(drawable);  
	    } else {  
	        imageView.setImageDrawable(placeholder);  
	        queueJob(url, imageView, placeholder);  
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
	                        //Log.d(null, "fail " + url);  
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
	                //Log.d(null, "Item downloaded: " + url);  

	                handler.sendMessage(message);
	            }
	        }  
	    });  
	}  

	
	private Drawable downloadDrawable(String url) {  
	    try {  
	        InputStream is = getInputStream(url);

	        Drawable drawable = Drawable.createFromStream(is, url);
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
}
