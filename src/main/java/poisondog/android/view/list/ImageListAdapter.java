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
import java.util.List;
import poisondog.android.view.list.R;
/**
 * @author poisondog <poisondog@gmail.com>
 */
public class ImageListAdapter extends BaseAdapter {
	private Context mContext;
	private List<ComplexListItem> mItems;

	public ImageListAdapter(Context context, List<ComplexListItem> items) {
		super();
		mContext = context;
		mItems = items;
	}

	public void addItem(ComplexListItem file) {
		mItems.add(file);
		notifyDataSetChanged();
	}

	public void setItems(List<ComplexListItem> items) {
		mItems = items;
		notifyDataSetChanged();
	}

	public void setItem(int index, ComplexListItem file) {
		mItems.set(index, file);
	}

	public void removeItem(int index) {
		mItems.remove(index);
	}

	public List<ComplexListItem> getItems() {
		return mItems;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public ComplexListItem getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.image_list_item, parent, false);
		ComplexListItem obj = getItem(position);

		updateView(row, obj);
		return row;
	}

	public TextView getSubtitle(View row) {
		return (TextView) row.findViewById(R.id.subtitle);
	}

	public TextView getComment(View row) {
		return (TextView) row.findViewById(R.id.comment);
	}

	public void updateView(View row, ComplexListItem obj) {
		ImageView image = (ImageView) row.findViewById(R.id.image);
		ImageView state = (ImageView) row.findViewById(R.id.state);
		TextView hide = (TextView) row.findViewById(R.id.hide);
		TextView title = (TextView) row.findViewById(R.id.title);
		TextView subtitle = (TextView) row.findViewById(R.id.subtitle);
		TextView comment = (TextView) row.findViewById(R.id.comment);
		ProgressBar progress = (ProgressBar) row.findViewById(R.id.progress);

		progress.setVisibility(View.GONE);
		subtitle.setVisibility(View.VISIBLE);
		comment.setVisibility(View.VISIBLE);

		hide.setText(obj.getHideMessage());
		title.setText(obj.getTitle());

		obj.setSubTitle(subtitle);
		obj.setComment(comment);
		obj.setImage(image);
		obj.setState(state);
	}

	@Override
	public boolean isEmpty() {
		return mItems.isEmpty();
	}
}
