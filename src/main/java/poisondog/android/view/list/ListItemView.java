/*
 * Copyright (C) 2017 Adam Huang <poisondog@gmail.com>
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
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Adam Huang
 * @since 2017-12-28
 */
public class ListItemView extends RelativeLayout implements ItemView {
	private View mRoot;
	private ImageView mImage;
	private ImageView mState;
	private TextView mTitle;
	private TextView mSubtitle;
	private TextView mComment;
	private TextView mHide;
	private ProgressBar mProgress;
	private DataItem mItem;

	/**
	 * Constructor
	 */
	public ListItemView(Context context) {
		super(context);
		init();
	}

	/**
	 * Constructor
	 */
	public ListItemView(Context context, AttributeSet attribute) {
		super(context, attribute);
		init();
	}

	/**
	 * Constructor
	 */
	public ListItemView(Context context, AttributeSet attribute, int style) {
		super(context, attribute, style);
		init();
	}

	private void init() {
		setLayout(R.layout.image_list_item);
	}

	@Override
	public void setLayout(int resourceID) {
		inflate(getContext(), resourceID, this);
		mImage = (ImageView) findViewById(R.id.image);
		mState = (ImageView) findViewById(R.id.state);
		mHide = (TextView) findViewById(R.id.hide);
		mTitle = (TextView) findViewById(R.id.title);
		mSubtitle = (TextView) findViewById(R.id.subtitle);
		mComment = (TextView) findViewById(R.id.comment);
		mProgress = (ProgressBar) findViewById(R.id.progress);
	}

	@Override
	public ImageView getImage() {
		return mImage;
	}

	@Override
	public ImageView getState() {
		return mState;
	}

	public TextView getHide() {
		return mHide;
	}

	@Override
	public TextView getTitle() {
		return mTitle;
	}

	@Override
	public TextView getSubtitle() {
		return mSubtitle;
	}

	@Override
	public TextView getComment() {
		return mComment;
	}

	@Override
	public ProgressBar getProgress() {
		return mProgress;
	}

	@Override
	public void setItem(DataItem item) {
		mItem = item;
	}

	@Override
	public DataItem getItem() {
		return mItem;
	}
}
