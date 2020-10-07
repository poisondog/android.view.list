/*
 * Copyright (C) 2020 Adam Huang <poisondog@gmail.com>
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
package poisondog.android.view.list.app;

import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import poisondog.android.view.EntityContent;
import poisondog.android.view.list.DataItem;
import poisondog.android.view.list.SimpleItem;
import poisondog.android.view.list.app.R;
import poisondog.core.Mission;
import poisondog.format.SizeFormatUtils;
import poisondog.format.TimeFormatUtils;
import poisondog.net.UrlUtils;
import poisondog.vfs.FileFactory;
import poisondog.vfs.IData;
import poisondog.vfs.IFile;
import poisondog.vfs.IFolder;
import poisondog.vfs.comparator.NameOrder;
import poisondog.vfs.filter.FileFilter;
import poisondog.vfs.filter.OnlyImage;

/**
 * @author Adam Huang
 * @since 2020-02-21
 */
public class RefreshData implements Runnable, View.OnLongClickListener, View.OnClickListener {
	private ListItemFactory mCreator;
	private EntityContent mRoot;

	/**
	 * Constructor
	 */
	public RefreshData(EntityContent root) {
		mCreator = new ListItemFactory();
		mRoot = root;
	}

	@Override
	public void onClick(View v) {
		run();
	}

	@Override
	public boolean onLongClick(View v) {
		run();
		return true;
	}

	@Override
	public void run() {
		mRoot.setLoading(true);
		mRoot.setLayoutManager(new LinearLayoutManager(mRoot.getContext()));
		List<IFile> content = new ArrayList<IFile>();
		String download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/";
		try {
			IFolder mFolder = (IFolder)FileFactory.getFile(download);
			FileFilter filter = new FileFilter();
			filter.setIncludeRule(new OnlyImage());
			content = new ArrayList<IFile>(filter.execute(mFolder.getChildren()));
		} catch(Exception e) {
			e.printStackTrace();
		}

		Collections.sort(content, new NameOrder());
		ArrayList<DataItem> result = new ArrayList<DataItem>();
		result.add(SimpleItem.header("Head", "second", "third"));
		for (IFile f : content) {
			try {
				result.add(mCreator.execute(f));
			} catch(Exception e) {
			}
			if (Math.random() < 0.1)
				result.add(SimpleItem.header("first", "second", "third"));
		}
		mRoot.setItems(result);
	}

	class ListItemFactory implements Mission<IFile> {
		@Override
		public DataItem execute(IFile f) throws Exception {
			IData data = (IData)f;
			String filename = UrlUtils.filename(f.getUrl());
			String time = TimeFormatUtils.toString(f.getLastModifiedTime());
			String size = SizeFormatUtils.toString(data.getSize());
			SimpleItem item = new SimpleItem(filename, time, size);
			item.setDefaultImage(R.drawable.file_txt);
			item.setData(data);
			item.setImage(data.getUrl());
			return item;
		}
	}

}
