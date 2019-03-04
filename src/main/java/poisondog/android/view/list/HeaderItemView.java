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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author Adam Huang
 * @since 2017-12-28
 */
public class HeaderItemView extends LinearLayout implements ItemView {
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
	public HeaderItemView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * Constructor
	 */
	public HeaderItemView(Context context, AttributeSet attribute) {
		super(context, attribute);
		init(context);
	}

	/**
	 * Constructor
	 */
	public HeaderItemView(Context context, AttributeSet attribute, int style) {
		super(context, attribute, style);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRoot = inflater.inflate(R.layout.header_item, null, false);
		mImage = (ImageView) mRoot.findViewById(R.id.image);
		mState = (ImageView) mRoot.findViewById(R.id.state);
		mHide = (TextView) mRoot.findViewById(R.id.hide);
		mTitle = (TextView) mRoot.findViewById(R.id.title);
		mSubtitle = (TextView) mRoot.findViewById(R.id.subtitle);
		mComment = (TextView) mRoot.findViewById(R.id.comment);
		mProgress = (ProgressBar) mRoot.findViewById(R.id.progress);
		addView(mRoot);
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
