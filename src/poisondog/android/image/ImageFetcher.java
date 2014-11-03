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
import java.io.IOException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;
import poisondog.io.CopyTask;
/**
 * @author poisondog <poisondog@gmail.com>
 */
public class ImageFetcher extends ImageResize {
	private FileObject mDestination;

	public ImageFetcher(Context context, int imageWidth, int imageHeight, FileObject dest) {
		super(context, imageWidth, imageHeight);
		setDestination(dest);
	}

	public void setDestination(FileObject obj) {
		mDestination = obj;
	}

	protected Bitmap processBitmap(Object data){
		if(!(data instanceof String) )
			return null;
		try{
			FileObject remote = VFS.getManager().resolveFile((String)data);
			if(remote.getURL().getProtocol() == "file")
				return super.processBitmap(remote.getURL().toString());

			FileObject file = mDestination.resolveFile(remote.getName().getBaseName());
			CopyTask task = new CopyTask(remote.getContent().getInputStream(), file.getContent().getOutputStream());
			task.transport();

			if(file == null){
				return null;
			}
			return super.processBitmap(file.getURL().toString());
		}catch(FileSystemException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
