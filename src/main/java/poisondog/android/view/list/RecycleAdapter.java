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
import poisondog.android.image.ImageFetcher;
import poisondog.android.view.list.DataItem;
import poisondog.core.Mission;

/**
 * @author poisondog <poisondog@gmail.com>
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder> {
	private Context mContext;
	private List<DataItem> mItems;
	private ImageFetcher mFetcher;
	private Mission<Context> mItemViewFactor;

	/**
	 * Constructor
	 */
	public RecycleAdapter(Context context) {
		super();
		mContext = context;
		mItems = new ArrayList<DataItem>();
		mItemViewFactor = new DefaultItemViewFactory();

		try {
			mFetcher = new ImageFetcher(mContext, 500, 500, mContext.getExternalCacheDir().getPath() + "/");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor
	 */
	public RecycleAdapter(Context context, List<DataItem> items) {
		this(context);
		mItems = items;
	}

	public void clear() {
		mItems.clear();
	}

	public void addItem(DataItem file) {
		mItems.add(file);
	}

	public void setItems(List<DataItem> items) {
		mItems = items;
	}

	public void setItem(int index, DataItem file) {
		mItems.set(index, file);
	}

	public void removeItem(int index) {
		mItems.remove(index);
	}

	public List<DataItem> getItems() {
		return mItems;
	}

	public DataItem getItem(int position) {
		return mItems.get(position);
	}

	public void setItemViewFactory(Mission<Context> factory) {
		mItemViewFactor = factory;
	}

	class DefaultItemViewFactory implements Mission<Context> {
		@Override
		public GridItemView execute(Context context) {
			return new GridItemView(context);
		}
	}

	@Override
	public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		try {
			return new RecycleViewHolder((ItemView) mItemViewFactor.execute(parent.getContext()));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onBindViewHolder(RecycleViewHolder holder, int position) {
		ItemView row = holder.getItemView();
		DataItem obj = getItem(position);
		row.getTitle().setText(obj.getTitle());
		if (obj.getSubtitle() == null)
			row.getSubtitle().setVisibility(View.GONE);
		else
			row.getSubtitle().setText(obj.getSubtitle());

		if (obj.getComment() == null)
			row.getComment().setVisibility(View.GONE);
		else
			row.getComment().setText(obj.getComment());

		if (obj.getImage() == null && obj.getDefaultImage() <= 0) {
			row.getImage().setVisibility(View.GONE);
		}
		if (obj.getDefaultImage() > 0) {
			mFetcher.setLoadingImage(obj.getDefaultImage());
			row.getImage().setImageResource(obj.getDefaultImage());
		}
		if (obj.getImage() != null) {
			mFetcher.loadImage(obj.getImage(), row.getImage());
		}

		if (obj.getState() == null)
			row.getState().setVisibility(View.GONE);
		else
			mFetcher.loadImage(obj.getState(), row.getState());
	}

	@Override
	public int getItemCount() {
		return mItems.size();
	}

	class RecycleViewHolder extends RecyclerView.ViewHolder {
		private ItemView mView;

		/**
		 * Constructor
		 */
		public RecycleViewHolder(ItemView v) {
			super((View)v);
			mView = v;
		}

		public ItemView getItemView() {
			return mView;
		}
	}
}
