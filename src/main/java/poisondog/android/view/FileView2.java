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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import poisondog.android.view.list.RecycleAdapter;
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
public class FileView2 extends RelativeLayout {
	protected RefreshList mRefresh;
	private ListView mListView;
	private RecyclerView mRecyclerView;
	private RecycleAdapter mRecyclerAdapter;
	private LoadingView mLoading;
	private EmptyView mEmpty;
	private Mission<IFile> mItemCreator;
	private List<IFile> mContent;

	/**
	 * Constructor
	 */
	public FileView2(Context context) {
		super(context);
		init(context);
	}

	/**
	 * Constructor
	 */
	public FileView2(Context context, AttributeSet attribute) {
		super(context, attribute);
		init(context);
	}

	private void init(Context context) {
		mItemCreator = new DefaultCreator();
		mRefresh = new RefreshList(context);
		mLoading = new LoadingView(context);
		mEmpty = new EmptyView(context);
		mContent = new ArrayList<IFile>();

//		mAdapter = new ListAdapter(context);
//		mListView = new ListView(context);
//		mListView.setAdapter(mAdapter);
//		mListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//		mListView.setOnScrollListener(new ScrollTopRefresh(mRefresh));

		mRecyclerAdapter = new RecycleAdapter(context);
		mRecyclerView = new RecyclerView(context);
		mRecyclerView.setAdapter(mRecyclerAdapter);
		mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		mRecyclerView.setOnScrollListener(new ScrollTopRefresh(mRefresh));
//		mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
		mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));

		mRefresh.addView(mRecyclerView);
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
			mRecyclerAdapter.clear();
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
		mRecyclerAdapter.setItem(position, createItem(file));
	}

	public void remove(int index) {
		mRecyclerAdapter.removeItem(index);
	}

	public void update() {
	}

	public void refresh() {
		mRefresh.onRefresh();
	}

	public void setItems(List<ListItem> items) {
		for (ListItem item : items) {
			mRecyclerAdapter.addItem(item);
		}
		setLoading(false);
	}

	public void setFiles(List<IFile> datas) {
		mContent = datas;
		mRecyclerAdapter.clear();
		mRefresh.onRefresh();
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
