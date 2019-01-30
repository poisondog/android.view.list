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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import poisondog.android.mission.ScrollTopRefresh;
import poisondog.android.view.list.ListAdapter;
import poisondog.android.view.list.ListItem;
import poisondog.android.view.list.R;
import poisondog.android.view.list.SimpleItem;
import poisondog.android.view.LoadingView;
import poisondog.android.view.RefreshList;
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
	private ListView mListView;
	protected RefreshList mRefresh;
	private ListAdapter mAdapter;
	private LoadingView mLoading;
	private EmptyView mEmpty;
	private Mission<IFile> mItemCreator;
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
		mAdapter = new ListAdapter(context);
		mRefresh = new RefreshList(context);
		mLoading = new LoadingView(context);
		mEmpty = new EmptyView(context);
		mContent = new ArrayList<IFile>();

		mListView = new ListView(context);
		mListView.setAdapter(mAdapter);
		mListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		mListView.setOnScrollListener(new ScrollTopRefresh(mRefresh));
		mRefresh.addView(mListView);
		mRefresh.setHandler(new DefaultRefresh());

		mEmpty.setContent(mRefresh);
		mLoading.setContent(mEmpty);
		addView(mLoading);
		setLoading(false);
	}

	public void setRefreshHandler(Runnable handler) {
		mRefresh.setHandler(handler);
	}

	public void setItemCreator(Mission<IFile> creator) {
		mItemCreator = creator;
	}

	public void setLoading(boolean flag) {
		if (flag) {
			mAdapter.clear();
		}
		mLoading.setLoading(flag);
	}

	public void setEmpty(boolean flag) {
		mEmpty.setEmpty(flag);
	}

	public void setEmpty(View view) {
		mEmpty.setEmpty(view);
	}

	public void update(int position, IFile file) {
		mAdapter.setItem(position, createItem(file));
	}

	public void remove(int index) {
		mAdapter.removeItem(index);
	}

	public void update() {
		mAdapter.notifyDataSetChanged();
	}

	public void refresh() {
		mRefresh.onRefresh();
	}

	public void setItems(List<ListItem> items) {
		for (ListItem item : items) {
			mAdapter.addItem(item);
		}
		setLoading(false);
		mAdapter.notifyDataSetChanged();
	}

	public void setFiles(List<IFile> datas) {
		mContent = datas;
		mAdapter.clear();
		mRefresh.onRefresh();
	}

	public void setPosition(Pair<Integer, Integer> position) {
		mListView.setSelectionFromTop(position.getValue1(), position.getValue2());
	}

	public Pair<Integer, Integer> getPosition() {
		int index = mListView.getFirstVisiblePosition();
		View v = getChildAt(0);
		int top = (v == null) ? 0 : v.getTop() - getPaddingTop();
		return new Pair<Integer, Integer>(index, top);
	}

	public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
		mListView.setOnItemClickListener(listener);
	}

	public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
		mListView.setOnItemLongClickListener(listener);
	}

	public ListItem createItem(IFile f) {
		try {
			return (ListItem) mItemCreator.execute(f);
		} catch(Exception e) {
		}
		return null;
	}

	class DefaultCreator implements Mission<IFile> {
		@Override
		public ListItem execute(IFile f) {
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
			Collections.sort(mContent, new NameOrder());
			ArrayList<ListItem> result = new ArrayList<ListItem>();
			for (IFile f : mContent) {
				result.add(createItem(f));
			}
			setItems(result);
		}
	}

}
