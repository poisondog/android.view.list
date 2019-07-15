/*
 * Copyright (C) 2014 Adam Huang
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
package poisondog.android.view.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import poisondog.android.view.list.DataItem;
import poisondog.core.Mission;
import poisondog.util.Pair;

/**
 * @author poisondog <poisondog@gmail.com>
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecycleViewHolder> {
	private Context mContext;
	private List<DataItem> mItems;
	private View.OnClickListener mOnClickListener;
	private View.OnLongClickListener mOnLongClickListener;
	private Mission<Integer> mViewFactory;
	private Mission<Pair<View, DataItem>> mBinder;

	/**
	 * Constructor
	 */
	public RecyclerAdapter(Context context) {
		super();
		mContext = context;
		mItems = new ArrayList<DataItem>();
		mViewFactory = new DefaultItemViewFactory();
		mBinder = new DefaultBinder(context);
	}

	/**
	 * Constructor
	 */
	public RecyclerAdapter(Context context, List<DataItem> items) {
		this(context);
		mItems = items;
	}

	public void clear() {
		mItems.clear();
		notifyDataSetChanged();
	}

	public void addItem(DataItem file) {
		mItems.add(file);
		notifyDataSetChanged();
	}

	public void setItems(List<DataItem> items) {
		mItems = items;
		notifyDataSetChanged();
	}

	public void setItem(int index, DataItem file) {
		mItems.set(index, file);
		notifyDataSetChanged();
	}

	public void remove(int index) {
		mItems.remove(index);
		notifyDataSetChanged();
	}

	public void remove(DataItem item) {
		mItems.remove(item);
		notifyDataSetChanged();
	}

	public List<DataItem> getItems() {
		return mItems;
	}

	public DataItem getItem(int position) {
		return mItems.get(position);
	}

	/**
	 * set factory for Mission with layout input View output
	 */
	public void setViewFactory(Mission<Integer> factory) {
		mViewFactory = factory;
	}

	public void setBinder(Mission<Pair<View, DataItem>> binder) {
		mBinder = binder;
	}

	public void setOnClickListener(View.OnClickListener listener) {
		mOnClickListener = listener;
	}

	public void setOnLongClickListener(View.OnLongClickListener listener) {
		mOnLongClickListener = listener;
	}

	public View.OnClickListener getOnClickListener() {
		return mOnClickListener;
	}

	public View.OnLongClickListener getOnLongClickListener() {
		return mOnLongClickListener;
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position).getLayout();
	}

	@Override
	public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		try {
			View view = (View) mViewFactory.execute(viewType);
			return new RecycleViewHolder(view);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onBindViewHolder(RecycleViewHolder holder, int position) {
		View row = holder.getView();
		DataItem obj = getItem(position);
		try {
			mBinder.execute(new Pair<View, DataItem>(row, obj));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getItemCount() {
		return mItems.size();
	}

	class RecycleViewHolder extends RecyclerView.ViewHolder {
		private View mView;

		/**
		 * Constructor
		 */
		public RecycleViewHolder(View v) {
			super(v);
			mView = v;
		}

		public View getView() {
			return mView;
		}

		public ItemView getItemView() {
			return (ItemView) mView;
		}
	}

	class DefaultItemViewFactory implements Mission<Integer> {
		@Override
		public ItemView execute(Integer layout) {
			ComplexItemView item = new ComplexItemView(mContext);
			item.setLayout(layout);
			item.setOnClickListener(mOnClickListener);
			item.setOnLongClickListener(mOnLongClickListener);
			return item;
		}
	}

}
