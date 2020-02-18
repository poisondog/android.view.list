/*
 * Copyright (C) 2018 Adam Huang <poisondog@gmail.com>
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
package poisondog.android.view;

import android.content.Context;
import android.util.AttributeSet;
import poisondog.core.Mission;
import poisondog.core.NoMission;
import poisondog.net.UrlUtils;
import poisondog.string.GetPath;
import poisondog.vfs.FileFactory;
import poisondog.vfs.IFolder;

/**
 * @author Adam Huang
 * @since 2018-02-06
 */
public class FolderView extends FileView {
	private IFolder mRoot;
	private IFolder mCurrent;

	/**
	 * Constructor
	 */
	public FolderView(Context context) {
		super(context);
		setRefreshHandler(new DefaultRefresh());
	}

	/**
	 * Constructor
	 */
	public FolderView(Context context, AttributeSet attribute) {
		super(context, attribute);
		setRefreshHandler(new DefaultRefresh());
	}

	public void setRoot(IFolder folder) {
		mRoot = folder;
	}

	public void setFolder(IFolder folder) throws Exception {
		if (mRoot == null)
			mRoot = folder;
		mCurrent = folder;
	}

	public IFolder getFolder() {
		return mCurrent;
	}

	public IFolder getRoot() {
		return mRoot;
	}

	public void parent() throws Exception {
		if (isRoot())
			return;
		setFolder((IFolder) FileFactory.getFile(UrlUtils.parentUrl(mCurrent.getUrl())));
	}

	public boolean isRoot() throws Exception {
		if (mRoot == null)
			return false;
		GetPath path = new GetPath();
		return path.execute(getFolder().getUrl()).equals(path.execute(mRoot.getUrl()));
	}

	class DefaultRefresh implements Runnable {
		@Override
		public void run() {
			try {
				setFiles(mCurrent.getChildren());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
