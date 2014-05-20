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

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import java.lang.ref.WeakReference;
import poisondog.android.os.AsyncTask;
/**
 * @author poisondog <poisondog@gmail.com>
 */
public class ImageAsyncTask extends AsyncTask<Object, Void, BitmapDrawable> {
	private Object data;
	private final ImageTask task;
	private final WeakReference<ImageView> imageViewReference;

	public ImageAsyncTask(ImageTask task, ImageView imageView) {
		this.task = task;
		imageViewReference = new WeakReference<ImageView>(imageView);
	}

	/**
	 * Background processing.
	 */
	@Override
	protected BitmapDrawable doInBackground(Object... params) {
		data = params[0];
		final String dataString = String.valueOf(data);
		Bitmap bitmap = null;
		BitmapDrawable drawable = null;

//		synchronized (task.mPauseWorkLock) {
//			System.out.println("::::pause work lock::::");
//			while (task.mPauseWork && !isCancelled()) {
//				System.out.println("::::pause work::::");
//				try {
//					task.mPauseWorkLock.wait();
//				} catch (InterruptedException e) {}
//			}
//		}

		if (task.mImageCache != null && !isCancelled() && getAttachedImageView() != null && !task.mExitTasksEarly) {
			try{
				bitmap = task.mImageCache.getBitmapFromCache(dataString);
			}catch(Exception e) {
			}
		}

		if (bitmap == null && !isCancelled() && getAttachedImageView() != null && !task.mExitTasksEarly) {
			bitmap = task.processBitmap(params[0]);
		}

		if (bitmap != null) {
				drawable = new RecyclingBitmapDrawable(task.mResources, bitmap);
			if (task.mImageCache != null) {
				try{
					task.mImageCache.addBitmapToCache(dataString, drawable);
				}catch(Exception e) {
				}
			}
		}

		System.gc();
		return drawable;
	}

	/**
	 * Once the image is processed, associates it to the imageView
	 */
	@Override
	protected void onPostExecute(BitmapDrawable value) {
		// if cancel was called on this task or the "exit early" flag is set then we're done
		if (isCancelled() || task.mExitTasksEarly) {
			value = null;
		}

		final ImageView imageView = getAttachedImageView();
		if (value != null && imageView != null) {
			imageView.setImageDrawable(value);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		synchronized (task.mPauseWorkLock) {
			task.mPauseWorkLock.notifyAll();
		}
	}

	/**
	 * Returns the ImageView associated with this task as long as the ImageView's task still
	 * points to this task as well. Returns null otherwise.
	 */
	private ImageView getAttachedImageView() {
		final ImageView imageView = imageViewReference.get();
		final ImageAsyncTask task = ImageTask.getImageAsyncTask(imageView);

		if (this == task) {
			return imageView;
		}

		return null;
	}

	public Object getData() {
		return data;
	}
}
