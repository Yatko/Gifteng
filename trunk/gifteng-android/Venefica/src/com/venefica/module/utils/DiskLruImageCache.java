package com.venefica.module.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.venefica.module.main.BuildConfig;
import com.venefica.utils.Constants;

/**
 * @author avinash
 * Class for to handle disk cache operations
 */
public class DiskLruImageCache {

	private Context context;
	private DiskLruCache mDiskCache;
    private CompressFormat mCompressFormat = CompressFormat.JPEG;
    private int mCompressQuality = 80;
    private static final int VALUE_COUNT = 1;
    private static final String TAG = "DiskLruImageCache";
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;

    /**
     * Constructor to create DiskLruImageCache object
     * @param context
     * @param diskCacheSize
     * @param compressFormat
     * @param quality
     */
    public DiskLruImageCache( Context context, CompressFormat compressFormat, int quality ) {
    	this.context = context;
    	this.mCompressFormat = compressFormat;
        this.mCompressQuality = quality;       
    }
    
    /**
     * Initializes the disk cache.  Note that this includes disk access so this should not be
     * executed on the main/UI thread. By default an ImageCache does not initialize the disk
     * cache when it is created, instead you should call initDiskCache() to initialize it on a
     * background thread.
     */
	public void initDiskCache() {
		// Set up disk cache
		synchronized (mDiskCacheLock) {
			if (mDiskCache == null || mDiskCache.isClosed()) {
				File diskCacheDir = getDiskCacheDir(this.context,
						Constants.EXTERNAL_IMAGE_CACHE_NAME);
				if (diskCacheDir != null) {
					if (!diskCacheDir.exists()) {
						diskCacheDir.mkdirs();
					}
					if (Utility.getUsableSpace(diskCacheDir) > Constants.EXTERNAL_IMAGE_CACHE_LIMIT) {
						try {
							mDiskCache = DiskLruCache.open(diskCacheDir, 1,
									VALUE_COUNT,
									Constants.EXTERNAL_IMAGE_CACHE_LIMIT);
							if (BuildConfig.DEBUG) {
								Log.d(TAG, "Disk cache initialized");
							}
						} catch (final IOException e) {
							diskCacheDir = null;
							Log.e(TAG, "initDiskCache - " + e);
						}
					}
				}
			}
			mDiskCacheStarting = false;
			mDiskCacheLock.notifyAll();
		}
	}
    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		// Check if media is mounted or storage is built-in, if so, try and use
		// external cache dir
		// otherwise use internal cache dir
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Utility.isExternalStorageRemovable() ? Utility
				.getExternalCacheDir(context).getPath() : context.getCacheDir()
				.getPath();

		return new File(cachePath + File.separator + uniqueName);
	}
    
    /**
     * Adds a bitmap to both memory and disk cache.
     * @param data Unique identifier for the bitmap to store
     * @param bitmap The bitmap to store
     */
	public void put(String data, Bitmap bitmap) {
		if (data == null || bitmap == null) {
			return;
		}

		synchronized (mDiskCacheLock) {
			// Add to disk cache
			if (mDiskCache != null) {
				final String key = hashKeyForDisk(data);
				OutputStream out = null;
				try {
					DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
					if (snapshot == null) {
						final DiskLruCache.Editor editor = mDiskCache.edit(key);
						if (editor != null) {
							out = editor.newOutputStream(0);
							bitmap.compress(mCompressFormat, mCompressQuality,
									out);
							editor.commit();
							out.close();
						}
					} else {
						snapshot.getInputStream(0).close();
					}
				} catch (final IOException e) {
					Log.e(TAG, "addBitmapToCache - " + e);
				} catch (Exception e) {
					Log.e(TAG, "addBitmapToCache - " + e);
				} finally {
					try {
						if (out != null) {
							out.close();
						}
					} catch (IOException e) {
					}
				}
			}
		}
	}
    /**
     * Get from disk cache.
     *
     * @param data Unique identifier for which item to get
     * @return The bitmap if found in cache, null otherwise
     */
	public Bitmap getBitmapFromDiskCache(String data) {
		final String key = hashKeyForDisk(data);
		synchronized (mDiskCacheLock) {
			while (mDiskCacheStarting) {
				try {
					mDiskCacheLock.wait();
				} catch (InterruptedException e) {
				}
			}
			if (mDiskCache != null) {
				InputStream inputStream = null;
				try {
					final DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
					if (snapshot != null) {
						if (BuildConfig.DEBUG) {
							Log.d(TAG, "Disk cache hit");
						}
						inputStream = snapshot.getInputStream(0);
						if (inputStream != null) {
							final Bitmap bitmap = BitmapFactory
									.decodeStream(inputStream);
							return bitmap;
						}
					}
				} catch (final IOException e) {
					Log.e(TAG, "getBitmapFromDiskCache - " + e);
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
					}
				}
			}
			return null;
		}
	}

    /**
     * Check if image already exists
     * @param data
     * @return
     */
	public boolean containsKey(String data) {
		final String key = hashKeyForDisk(data);
		boolean contained = false;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = mDiskCache.get(key);
			contained = snapshot != null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (snapshot != null) {
				snapshot.close();
			}
		}

		return contained;

	}

    /**
     * Clears disk cache associated with this ImageCache object. Note that
     * this includes disk access so this should not be executed on the main/UI thread.
     */
	public void clearCache() {
		synchronized (mDiskCacheLock) {
			mDiskCacheStarting = true;
			if (mDiskCache != null && !mDiskCache.isClosed()) {
				try {
					mDiskCache.delete();
					if (BuildConfig.DEBUG) {
						Log.d(TAG, "Disk cache cleared");
					}
				} catch (IOException e) {
					Log.e(TAG, "clearCache - " + e);
				}
				mDiskCache = null;
			}
		}
	}

    /**
     * Flushes the disk cache associated with this ImageCache object. Note that this includes
     * disk access so this should not be executed on the main/UI thread.
     */
	public void flush() {
		synchronized (mDiskCacheLock) {
			if (mDiskCache != null) {
				try {
					mDiskCache.flush();
					if (BuildConfig.DEBUG) {
						Log.d(TAG, "Disk cache flushed");
					}
				} catch (IOException e) {
					Log.e(TAG, "flush - " + e);
				}
			}
		}
	}

    /**
     * Closes the disk cache associated with this ImageCache object. Note that this includes
     * disk access so this should not be executed on the main/UI thread.
     */
	public void close() {
		synchronized (mDiskCacheLock) {
			if (mDiskCache != null) {
				try {
					if (!mDiskCache.isClosed()) {
						mDiskCache.close();
						mDiskCache = null;
						if (BuildConfig.DEBUG) {
							Log.d(TAG, "Disk cache closed");
						}
					}
				} catch (IOException e) {
					Log.e(TAG, "close - " + e);
				}
			}
		}
	}
    
    /**
     * A hashing method that changes a string (like a URL) into a hash suitable for using as a
     * disk filename.
     */
	public static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

    /**
     * Get Hex key for given bytes
     * @param bytes
     * @return Hex String
     */
	private static String bytesToHexString(byte[] bytes) {
		// http://stackoverflow.com/questions/332079
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
}
