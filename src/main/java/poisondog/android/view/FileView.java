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
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import poisondog.android.view.list.DataItem;
import poisondog.android.view.list.R;
import poisondog.android.view.list.SimpleItem;
import poisondog.core.Mission;
import poisondog.net.URLUtils;
import poisondog.util.Pair;
import poisondog.util.TimeUtils;
import poisondog.vfs.comparator.NameOrder;
import poisondog.vfs.IFile;

/**
 * @author Adam Huang
 * @since 2018-02-06
 */
public class FileView extends RelativeLayout {
	private Mission<IFile> mItemCreator;
	private EntityView mEntityView;
	private List<IFile> mContent;

	/**
	 * Constructor
	 */
	public FileView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * Constructor
	 */
	public FileView(Context context, AttributeSet attribute) {
		super(context, attribute);
		init(context);
	}

	private void init(Context context) {
		mItemCreator = new DefaultCreator();
		mContent = new ArrayList<IFile>();
		mEntityView = new EntityView(getContext());
		addView(mEntityView);
		setLoading(false);
		setRefreshHandler(new DefaultRefresh());
	}

	public void setRefreshHandler(Runnable handler) {
		mEntityView.setRefreshHandler(handler);
	}

	public void setItemCreator(Mission<IFile> creator) {
		mItemCreator = creator;
	}

	public void setLayoutManager(RecyclerView.LayoutManager manager) {
		mEntityView.setLayoutManager(manager);
	}

	public void setViewFactory(Mission<Integer> factory) {
		mEntityView.setViewFactory(factory);
	}

	public void setLoading(boolean flag) {
		mEntityView.setLoading(flag);
	}

	public void setEmpty(boolean flag) {
		mEntityView.setEmpty(flag);
	}

	public void setProgressBar(View view) {
		mEntityView.setProgressBar(view);
	}

	public void setEmpty(View view) {
		mEntityView.setEmpty(view);
	}

	public void setItemViewCacheSize(int size) {
		mEntityView.setItemViewCacheSize(size);
	}

	public Mission<IFile> getItemCreator() {
		return mItemCreator;
	}

	public int getItemViewLayout(int position) {
		return mEntityView.getItemViewLayout(position);
	}

//	// TODO 
//	public void update(int position, IFile file) {
//		mAdapter.setItem(position, createItem(file));
//	}

	public void remove(int index) {
		mEntityView.remove(index);
	}

	public void remove(DataItem item) {
		mEntityView.remove(item);
	}

	public void notifyDataSetChanged() {
		mEntityView.notifyDataSetChanged();
	}

	public void refresh() {
		mEntityView.refresh();
	}

	public void setItems(List<DataItem> items) {
		mEntityView.setItems(items);
	}

	public void setFiles(List<IFile> datas) {
		mContent = datas;
		mEntityView.clear();
		mEntityView.refresh();
	}

	public void setFile(int position, IFile file) {
		try {
			mEntityView.setItem(position, (DataItem) getItemCreator().execute(file));
		} catch(Exception e) {
		}
	}

	public List<IFile> getContent() {
		return mContent;
	}

	public List<DataItem> getItems() {
		return mEntityView.getItems();
	}

//	// TODO 
//	public void setPosition(Pair<Integer, Integer> position) {
//		mListView.setSelectionFromTop(position.getValue1(), position.getValue2());
//	}

//	// TODO 
//	public Pair<Integer, Integer> getPosition() {
//		int index = mListView.getFirstVisiblePosition();
//		View v = getChildAt(0);
//		int top = (v == null) ? 0 : v.getTop() - getPaddingTop();
//		return new Pair<Integer, Integer>(index, top);
//	}

	public void setOnClickListener(View.OnClickListener listener) {
		mEntityView.setOnClickListener(listener);
	}

	public void setOnLongClickListener(View.OnLongClickListener listener) {
		mEntityView.setOnLongClickListener(listener);
	}

	class DefaultCreator implements Mission<IFile> {
		@Override
		public DataItem execute(IFile f) {
			String filename = "Unknown";
			String time = "";
			try {
				filename = URLUtils.file(f.getUrl());
				time = TimeUtils.toString(f.getLastModifiedTime());
			} catch(Exception e) {
			}
			SimpleItem item = new SimpleItem(filename);
			item.setSubtitle(time);
			item.setData(f);
			try {
				item.setDefaultImage(f.getUrl().endsWith("/") ? R.drawable.folder : R.drawable.file);
			} catch(Exception e) {
			}
			return item;
		}
	}

	class DefaultRefresh implements Runnable {
		@Override
		public void run() {
			setLoading(true);
			Collections.sort(getContent(), new NameOrder());
			ArrayList<DataItem> result = new ArrayList<DataItem>();
			for (IFile f : getContent()) {
				try {
					result.add((DataItem) getItemCreator().execute(f));
				} catch(Exception e) {
				}
			}
			setItems(result);
		}
	}

}
