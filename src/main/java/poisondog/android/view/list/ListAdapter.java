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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import poisondog.android.os.AsyncTask;
import poisondog.android.view.list.R;
import poisondog.android.image.ImageFetcher;
import poisondog.core.Mission;

/**
 * @author poisondog <poisondog@gmail.com>
 */
public class ListAdapter extends BaseAdapter {
	private Context mContext;
	private List<ListItem> mItems;
	private UpdateHandler mSubtitleHandler;
	private ImageFetcher mFetcher;
	private Mission<ListItem> mViewCreator;

	/**
	 * Constructor
	 */
	public ListAdapter(Context context) {
		super();
		mContext = context;
		mItems = new ArrayList<ListItem>();
		mViewCreator = new ListItemViewCreator();

		try {
			mFetcher = new ImageFetcher(mContext, 500, 500, mContext.getExternalCacheDir().getPath() + "/");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public ListAdapter(Context context, List<ListItem> items) {
		this(context);
		mItems = items;
	}

	public void clear() {
		mItems.clear();
	}

	public void addItem(ListItem file) {
		mItems.add(file);
		notifyDataSetChanged();
	}

	public void setItems(List<ListItem> items) {
		mItems = items;
		notifyDataSetChanged();
	}

	public void setItem(int index, ListItem file) {
		mItems.set(index, file);
	}

	public void setViewCreator(Mission<ListItem> creator) {
		mViewCreator = creator;
	}

	public void removeItem(int index) {
		mItems.remove(index);
	}

	public List<ListItem> getItems() {
		return mItems;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public ListItem getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			return (View) mViewCreator.execute(getItem(position));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return mItems.isEmpty();
	}

	private TextView getSubtitle(View row) {
		return (TextView) row.findViewById(R.id.subtitle);
	}

	private TextView getComment(View row) {
		return (TextView) row.findViewById(R.id.comment);
	}

	private void setSubtitleHandler(UpdateHandler handler) {
		mSubtitleHandler = handler;
	}

	class ListItemViewCreator implements Mission<ListItem> {
		@Override
		public ListItemView execute(ListItem item) {
			ListItemView row = new ListItemView(mContext);
			update(row, item);
			return row;
		}
	}

	private void update(final ListItemView row, final ListItem obj) {
		row.getTitle().setText(obj.getTitle());
//		AsyncTask<String, Void, String> updateTitle = new AsyncTask<String, Void, String>() {
//			@Override
//			protected String doInBackground(String... none) {
//				return obj.getTitle();
//			}
//			@Override
//			protected void onPostExecute(String result) {
//				row.getTitle().setText(result);
//			}
//		};
//		updateTitle.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

		if (obj.getSubtitle() == null)
			row.getSubtitle().setVisibility(View.GONE);
		else
			row.getSubtitle().setText(obj.getSubtitle());
//		AsyncTask<String, Void, String> updateSubtitle = new AsyncTask<String, Void, String>() {
//			@Override
//			protected String doInBackground(String... none) {
//				return obj.getSubtitle();
//			}
//			@Override
//			protected void onPostExecute(String result) {
//				if (result == null)
//					row.getSubtitle().setVisibility(View.GONE);
//				row.getSubtitle().setText(result);
//			}
//		};
//		updateSubtitle.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

		if (obj.getComment() == null)
			row.getComment().setVisibility(View.GONE);
		else
			row.getComment().setText(obj.getComment());
//		AsyncTask<String, Void, String> updateComment = new AsyncTask<String, Void, String>() {
//			@Override
//			protected String doInBackground(String... none) {
//				return obj.getComment();
//			}
//			@Override
//			protected void onPostExecute(String result) {
//				if (result == null)
//					row.getComment().setVisibility(View.GONE);
//				row.getComment().setText(result);
//			}
//		};
//		updateComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

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

}
