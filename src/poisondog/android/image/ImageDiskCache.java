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
import android.graphics.BitmapFactory;
import java.security.MessageDigest;
import java.io.OutputStream;
import java.io.IOException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.FileSystemException;
import poisondog.commons.HashFunction;
/**
 * @author poisondog <poisondog@gmail.com>
 */
public class ImageDiskCache {
	private static final String MESSAGE_DIGEST_ALGORITHM = "MD5";
	private static ImageDiskCache instance;
	private FileObject cache;
	
	private ImageDiskCache(FileObject file) {
		this.cache = file;
	}

	public synchronized static ImageDiskCache open(FileObject file, int size) {
		if(instance != null)
			return instance;
		instance = new ImageDiskCache(file);
		return instance;
	}

	public synchronized Bitmap get(String key) throws FileSystemException {
		FileObject file = VFS.getManager().resolveFile(cache + "/" + HashFunction.md5(key));
		Bitmap result = BitmapFactory.decodeFile(file.getURL().getPath(), new BitmapFactory.Options());
		return result;
	}

	public synchronized void put(String key, Bitmap value) throws FileSystemException, IOException {
		FileObject file = VFS.getManager().resolveFile(cache + "/" + HashFunction.md5(key));
		OutputStream output = file.getContent().getOutputStream();
		value.compress(Bitmap.CompressFormat.JPEG, 90, output);
		output.close();
	}
}
