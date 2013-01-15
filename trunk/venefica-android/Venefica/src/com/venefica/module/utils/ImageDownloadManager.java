package com.venefica.module.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.venefica.utils.Constants;
/**
 * @author avinash
 * Class to manage lazy image download for list and grids
 */
public class ImageDownloadManager {
	private Context context;
	private static final int MESSAGE_CLEAR = 0;
    private static final int MESSAGE_INIT_DISK_CACHE = 1;
    private static final int MESSAGE_FLUSH = 2;
    private static final int MESSAGE_CLOSE = 3;
	private DiskLruImageCache imgCache ;
	private ExecutorService downloadThreadPool;  
	private final Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());  

	public static int MAX_CACHE_SIZE = 80; 
	public int THREAD_POOL_SIZE = 5;
	/**
	 * Constructor
	 */
	public ImageDownloadManager(Context context) {
		this.context = context;
		imgCache = new DiskLruImageCache(context, Constants.IMAGE_COMPRESS_FORMAT, Constants.JPEG_COMPRESS_QUALITY);
		new CacheAsyncTask().execute(MESSAGE_INIT_DISK_CACHE);
		downloadThreadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	}
	
	/**
	 * Clears all instance data and stops running threads
	 */
	public void reset() {
	    ExecutorService oldThreadPool = downloadThreadPool;
	    downloadThreadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	    oldThreadPool.shutdownNow();
	    clearCache();
	    closeCache();
	    imageViews.clear();
	    
	}  

	/**
	 * Download or get image from cache
	 * @param url
	 * @param imageView
	 * @param placeholder
	 */
	public void loadImage(final String url, final ImageView imageView,Drawable placeholder) {
	    imageViews.put(imageView, url);
	    Bitmap bitmap = getBitmapFromCache(url);  

	    // check in UI thread, so no concurrency issues  
	    if (bitmap != null) {  
	        imageView.setImageBitmap(bitmap);  
	    } else {  
	        imageView.setImageDrawable(placeholder);
	        if (url != null && URLUtil .isValidUrl(url)) {
	        	queueJob(url, imageView, placeholder);
	        }
	    }  
	} 


	/**
	 * Get image from cache for given url 
	 * @param url
	 * @return
	 */
	private Bitmap getBitmapFromCache(String url) {  
	    if (imgCache.containsKey(url)) {  
	        return imgCache.getBitmapFromDiskCache(url);
	    }  

	    return null;  
	}

	/**
	 * Store image in cache
	 * @param url
	 * @param bitmap
	 */
	private synchronized void putBitmapInCache(String url,Bitmap bitmap) {  
	    imgCache.put(url, bitmap);
	}  

	/**
	 * Start new job in thread pool
	 * @param url
	 * @param imageView
	 * @param placeholder
	 */
	private void queueJob(final String url, final ImageView imageView,final Drawable placeholder) {  
	    /* Create handler in UI thread. */  
	    final Handler handler = new Handler() {  
	        @Override  
	        public void handleMessage(Message msg) {  
	            String tag = imageViews.get(imageView);  
	            if (tag != null && tag.equals(url)) {
	                if (imageView.isShown())
	                    if (msg.obj != null) {
	                        imageView.setImageBitmap((Bitmap) msg.obj);  
	                    } else {  
	                        imageView.setImageDrawable(placeholder);  
	                    } 
	            }  
	        }  
	    };  

	    downloadThreadPool.submit(new Runnable() {  
	        @Override  
	        public void run() {  
	            final Bitmap bmp = downloadBitmap(url);
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

	
	/**
	 * @param url
	 * @return
	 */
	private Bitmap downloadBitmap(String url) {  
	    try {
	    	Bitmap bitmap = getScaledDownBitmap(url);
	    	putBitmapInCache(url,bitmap);
	        return bitmap;  

	    } catch (MalformedURLException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }

	    return null;  
	} 
		
	/**
	 * Download specified image from url
	 * @param urlString
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
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
	
	/**
	 * @author avinash
	 * Task to handle cache operations in background
	 */
	protected class CacheAsyncTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            switch ((Integer)params[0]) {
                case MESSAGE_CLEAR:
                    clearCacheInternal();
                    break;
                case MESSAGE_INIT_DISK_CACHE:
                    initDiskCacheInternal();
                    break;
                case MESSAGE_FLUSH:
                    flushCacheInternal();
                    break;
                case MESSAGE_CLOSE:
                    closeCacheInternal();
                    break;
            }
            return null;
        }
    }
	
	protected void initDiskCacheInternal() {
        if (imgCache != null) {
        	imgCache.initDiskCache();
        }
    }

    protected void clearCacheInternal() {
        if (imgCache != null) {
        	imgCache.clearCache();
        }
    }

    protected void flushCacheInternal() {
        if (imgCache != null) {
        	imgCache.flush();
        }
    }

    protected void closeCacheInternal() {
        if (imgCache != null) {
        	imgCache.close();
        	imgCache = null;
        }
    }

    /**
     * Clear disk cache data
     */
    public void clearCache() {
        new CacheAsyncTask().execute(MESSAGE_CLEAR);
    }

    /**
     * Flush disk cache
     */
    public void flushCache() {
        new CacheAsyncTask().execute(MESSAGE_FLUSH);
    }

    /**
     * Close disk cache
     */
    public void closeCache() {
        new CacheAsyncTask().execute(MESSAGE_CLOSE);
    }
}
