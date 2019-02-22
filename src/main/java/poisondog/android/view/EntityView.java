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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import java.util.List;
import poisondog.android.mission.ScrollTopRefresh;
import poisondog.android.view.list.DataItem;
import poisondog.android.view.list.RecyclerAdapter;
import poisondog.android.view.list.StickHeader;
import poisondog.android.view.list.ViewType;
import poisondog.android.view.LoadingView;
import poisondog.android.view.RefreshList;
import poisondog.core.Mission;

/**
 * @author Adam Huang
 * @since 2018-02-06
 */
public class EntityView extends RelativeLayout {
	protected RefreshList mRefresh;
	private RecyclerView mRecyclerView;
	private RecyclerAdapter mRecyclerAdapter;
	private LoadingView mLoading;
	private EmptyView mEmpty;
	private StickHeader mStickHeader;

	/**
	 * Constructor
	 */
	public EntityView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * Constructor
	 */
	public EntityView(Context context, AttributeSet attribute) {
		super(context, attribute);
		init(context);
	}

	private void init(Context context) {
		mRefresh = new RefreshList(context);
		mLoading = new LoadingView(context);
		mEmpty = new EmptyView(context);

		mRecyclerAdapter = new RecyclerAdapter(context);
		mRecyclerView = new RecyclerView(context);
		mStickHeader = new StickHeader(mRecyclerView, mRecyclerAdapter);
		mRecyclerView.setAdapter(mRecyclerAdapter);
		mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//		mRecyclerView.setOnScrollListener(new ScrollTopRefresh(mRefresh));
		mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
		mRecyclerView.addItemDecoration(mStickHeader);

		mRefresh.addView(mRecyclerView);

		mEmpty.setContent(mRefresh);
		mLoading.setContent(mEmpty);
		addView(mLoading);
		setLoading(false);
	}

	public void setLayoutManager(RecyclerView.LayoutManager manager) {
		mRecyclerView.setLayoutManager(manager);
	}

	public void setItemViewFactory(Mission<ViewType> factory) {
		mRecyclerAdapter.setItemViewFactory(factory);
	}

	public void setHeaderFactory(Mission<DataItem> factory) {
		mStickHeader.setHeaderFactory(factory);
	}

	public void setRefreshHandler(Runnable handler) {
		mRefresh.setHandler(handler);
	}

	public void clear() {
		mRecyclerAdapter.clear();
	}

	public void setLoading(boolean flag) {
		mLoading.setLoading(flag);
	}

	public void setEmpty(boolean flag) {
		mEmpty.setEmpty(flag);
	}

	public void setEmpty(View view) {
		mEmpty.setEmpty(view);
	}

	public void remove(int index) {
		mRecyclerAdapter.removeItem(index);
	}

	public void refresh() {
		mRefresh.onRefresh();
	}

	public void notifyDataSetChanged() {
		mRecyclerAdapter.notifyDataSetChanged();
	}

	public void setItem(int index, DataItem item) {
		mRecyclerAdapter.setItem(index, item);
	}

	public void setItems(List<DataItem> items) {
		mRecyclerAdapter.setItems(items);
		setLoading(false);
	}

	public void setOnClickListener(View.OnClickListener listener) {
		mRecyclerAdapter.setOnClickListener(listener);
	}

	public void setOnLongClickListener(View.OnLongClickListener listener) {
		mRecyclerAdapter.setOnLongClickListener(listener);
	}

	public int getItemViewType(int position) {
		return mRecyclerAdapter.getItemViewType(position);
	}

}
