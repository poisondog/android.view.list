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

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.File;
import java.util.List;
import org.apache.commons.vfs2.VFS;
import poisondog.android.image.ImageCache;
import poisondog.android.image.ImageFetcher;
import poisondog.android.os.AsyncTask;
//import poisondog.android.mysky.task.LoadFileInfo;
import poisondog.android.view.list.R;
import org.apache.commons.vfs2.FileObject;
/**
 * @author poisondog <poisondog@gmail.com>
 */
public class ImageListAdapter extends BaseAdapter {
	private Activity activity;
	private List<ComplexListItem> mItems;
	private ImageFetcher fetcher;
	private AsyncTask<ComplexListItem, ?, ?> mTask;

	public ImageListAdapter(Activity activity, List<ComplexListItem> items, FileObject dest, FileObject cache) {
		super();
		this.activity = activity;
		this.mItems = items;

		this.fetcher = new ImageFetcher(activity, 100, 100, dest);
		this.fetcher.setImageCache(new ImageCache(activity, cache));
	}

	public void setMission(AsyncTask<ComplexListItem, ?, ?> task) {
		mTask = task;
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
		LayoutInflater inflater = activity.getLayoutInflater();
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
		TextView url = (TextView) row.findViewById(R.id.url);
		TextView title = (TextView) row.findViewById(R.id.title);
		TextView subtitle = (TextView) row.findViewById(R.id.subtitle);
		TextView comment = (TextView) row.findViewById(R.id.comment);
		ProgressBar progress = (ProgressBar) row.findViewById(R.id.progress);

		progress.setVisibility(View.GONE);
		subtitle.setVisibility(View.VISIBLE);
		comment.setVisibility(View.VISIBLE);

		url.setText(obj.getHideMessage());
		title.setText(obj.getTitle());
		subtitle.setText(obj.getSubtitle());
		comment.setText(obj.getComment());

		image.setImageResource(obj.getDefaultImage());
		if(obj.getImage() != null && !obj.getImage().isEmpty())
			fetcher.loadImage(obj.getImage(), image);

//		LoadFileInfo task = new LoadFileInfo(subtitle, comment);
//		task.execute(obj.toString());
		if(mTask != null)
			mTask.execute(obj);
	}

	@Override
	public boolean isEmpty() {
		return mItems.isEmpty();
	}

	public void setPauseWork(boolean flag) {
		fetcher.setPauseWork(flag);
	}
}
