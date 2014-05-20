/*
 * Copyright (C) 2013 Google Inc.
 * Copyright (C) 2013 Adam Huang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package poisondog.android.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

/**
 * @author poisondog <poisondog@gmail.com>
 */
public class ImageCache {
	private LruCache<String, BitmapDrawable> mMemoryCache;
	private ImageDiskCache mImageDiskCache;
//	private final Object mDiskCacheLock = new Object();
//	private boolean mDiskCacheStarting = true;
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB

	public ImageCache(Context context, FileObject cacheDirectory) {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 4;

		mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
			@Override
			protected void entryRemoved(boolean evicted, String key, BitmapDrawable oldValue, BitmapDrawable newValue) {
				if (RecyclingBitmapDrawable.class.isInstance(oldValue)) {
					((RecyclingBitmapDrawable) oldValue).setIsCached(false);
				}
			}
			@Override
			protected int sizeOf(String key, BitmapDrawable bitmap) {
				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				bitmap.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, bao);
				return bao.toByteArray().length/ 1024;
			}
		};
		new InitDiskCacheTask().execute(cacheDirectory);
	}

	class InitDiskCacheTask extends AsyncTask<FileObject, Void, Void> {
		@Override
		protected Void doInBackground(FileObject... params) {
//			synchronized (mDiskCacheLock) {
				mImageDiskCache = ImageDiskCache.open(params[0], DISK_CACHE_SIZE);
//				mDiskCacheStarting = false; // Finished initialization
//				mDiskCacheLock.notifyAll(); // Wake any waiting threads
//			}
			return null;
		}
	}

	class PutMemoryCacheTask extends AsyncTask<Pair, Void, Void> {
		@Override
		protected Void doInBackground(Pair... params) {
			mMemoryCache.put(params[0].mKey, params[0].mBitmap);
			return null;
		}
	}

	class Pair {
		public String mKey;
		public BitmapDrawable mBitmap;
		public Pair(String key, BitmapDrawable bitmap) {
			mKey = key;
			mBitmap = bitmap;
		}
	}

	public void addBitmapToCache(String key, BitmapDrawable bitmap) throws FileSystemException, IOException {
//		if (getBitmapFromMemCache(key) == null) {
//			if (RecyclingBitmapDrawable.class.isInstance(bitmap)) {
//				((RecyclingBitmapDrawable) bitmap).setIsCached(true);
//			}
//			new PutMemoryCacheTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Pair(key, bitmap));
//		}
//		synchronized (mDiskCacheLock) {
			if (mImageDiskCache != null && mImageDiskCache.get(key) == null) {
				mImageDiskCache.put(key, bitmap.getBitmap());
			}
//		}
	}

	public Bitmap getBitmapFromCache(String key) throws FileSystemException {
		BitmapDrawable bitmap = getBitmapFromMemCache(key);
		if(bitmap != null)
			return bitmap.getBitmap();
		return getBitmapFromDiskCache(key);
	}

	private BitmapDrawable getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	private Bitmap getBitmapFromDiskCache(String key) throws FileSystemException {
//		synchronized (mDiskCacheLock) {
//			while (mDiskCacheStarting) {
//				try {
//					mDiskCacheLock.wait();
//				} catch (InterruptedException e) {}
//			}
			if (mImageDiskCache != null) {
				return mImageDiskCache.get(key);
			}
//		}
		return null;
	}
}
