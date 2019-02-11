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
import poisondog.android.view.list.ListItem;

/**
 * @author poisondog <poisondog@gmail.com>
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder> {
	private Context mContext;
	private List<ListItem> mItems;
	private ImageFetcher mFetcher;

	/**
	 * Constructor
	 */
	public RecycleAdapter(Context context) {
		super();
		mContext = context;
		mItems = new ArrayList<ListItem>();

		try {
			mFetcher = new ImageFetcher(mContext, 500, 500, mContext.getExternalCacheDir().getPath() + "/");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public RecycleAdapter(Context context, List<ListItem> items) {
		this(context);
		mItems = items;
	}

	public void clear() {
		mItems.clear();
	}

	public void addItem(ListItem file) {
		mItems.add(file);
	}

	public void setItems(List<ListItem> items) {
		mItems = items;
	}

	public void setItem(int index, ListItem file) {
		mItems.set(index, file);
	}

	public void removeItem(int index) {
		mItems.remove(index);
	}

	public List<ListItem> getItems() {
		return mItems;
	}

	public ListItem getItem(int position) {
		return mItems.get(position);
	}

	private void update(final ListItemView row, final ListItem obj) {
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
	public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		ListItemView view = new ListItemView(parent.getContext());
		return new RecycleViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecycleViewHolder holder, int position) {
		update(holder.getItemView(), getItem(position));
	}

	@Override
	public int getItemCount() {
		return mItems.size();
	}

	class RecycleViewHolder extends RecyclerView.ViewHolder {
		private ListItemView mView;

		/**
		 * Constructor
		 */
		public RecycleViewHolder(ListItemView v) {
			super(v);
			mView = v;
		}

		public ListItemView getItemView() {
			return mView;
		}
	}
}
