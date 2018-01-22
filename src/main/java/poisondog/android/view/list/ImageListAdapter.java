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

/**
 * @author poisondog <poisondog@gmail.com>
 */
public class ImageListAdapter extends BaseAdapter {
	private Context mContext;
	private List<ListItem> mItems;
	private UpdateHandler mSubtitleHandler;

	/**
	 * Constructor
	 */
	public ImageListAdapter(Context context) {
		super();
		mContext = context;
		mItems = new ArrayList<ListItem>();
	}

	public ImageListAdapter(Context context, List<ListItem> items) {
		this(context);
		mItems = items;
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
//		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View row = inflater.inflate(R.layout.image_list_item, parent, false);
		ListItemView row = new ListItemView(mContext);
//		updateView(row, getItem(position));
		update(row, getItem(position));
		return row;
	}

	public TextView getSubtitle(View row) {
		return (TextView) row.findViewById(R.id.subtitle);
	}

	public TextView getComment(View row) {
		return (TextView) row.findViewById(R.id.comment);
	}

//	public void updateView(final ListItemView row, final ComplexListItem obj) {
////		ImageView image = (ImageView) row.findViewById(R.id.image);
////		ImageView state = (ImageView) row.findViewById(R.id.state);
////		TextView hide = (TextView) row.findViewById(R.id.hide);
////		TextView title = (TextView) row.findViewById(R.id.title);
////		TextView subtitle = (TextView) row.findViewById(R.id.subtitle);
////		TextView comment = (TextView) row.findViewById(R.id.comment);
////		ProgressBar progress = (ProgressBar) row.findViewById(R.id.progress);
//
//		row.getProgress().setVisibility(View.GONE);
//		row.getSubtitle().setVisibility(View.VISIBLE);
//		row.getComment().setVisibility(View.VISIBLE);
//
//		row.getHide().setText(obj.getHideMessage());
//		row.getTitle().setText(obj.getTitle());
//
//		obj.setSubTitle(row.getSubtitle());
//		obj.setComment(row.getComment());
//		obj.setImage(row.getImage());
//		obj.setState(row.getState());
//	}

	private void update(final ListItemView row, final ListItem obj) {
		AsyncTask<String, Void, String> updateTitle = new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... none) {
				return obj.getTitle();
			}
			@Override
			protected void onPostExecute(String result) {
				row.getTitle().setText(result);
			}
		};
		updateTitle.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

		AsyncTask<String, Void, String> updateSubtitle = new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... none) {
				return obj.getSubtitle();
			}
			@Override
			protected void onPostExecute(String result) {
				if (result == null)
					row.getSubtitle().setVisibility(View.GONE);
				row.getSubtitle().setText(result);
			}
		};
		updateSubtitle.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

		AsyncTask<String, Void, String> updateComment = new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... none) {
				return obj.getComment();
			}
			@Override
			protected void onPostExecute(String result) {
				if (result == null)
					row.getComment().setVisibility(View.GONE);
				row.getComment().setText(result);
			}
		};
		updateComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
	}

	@Override
	public boolean isEmpty() {
		return mItems.isEmpty();
	}

	public void setSubtitleHandler(UpdateHandler handler) {
		mSubtitleHandler = handler;
	}
}
