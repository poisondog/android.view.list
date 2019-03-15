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
package poisondog.android.view.list;

import poisondog.android.view.list.R;

/**
 * @author Adam Huang
 * @since 2018-01-22
 */
public class SimpleItem implements DataItem {
	private String mTitle;
	private String mSubtitle;
	private String mComment;
	private String mImage;
	private String mState;
	private int mResource;
	private int mLayout;
	private Object mData;

	/**
	 * Constructor
	 */
	public SimpleItem(String title) {
		this(title, null, null);
	}

	/**
	 * Constructor
	 */
	public SimpleItem(String title, String subtitle, String comment) {
		mTitle = title;
		mSubtitle = subtitle;
		mComment = comment;
		mLayout = R.layout.list_item;
	}

	public static SimpleItem header(String title, String subtitle, String comment) {
		SimpleItem result = layout(title, subtitle, comment, R.layout.header_item);
		return result;
	}

	public static SimpleItem layout(String title, String subtitle, String comment, int layout) {
		SimpleItem item = new SimpleItem(title, subtitle, comment);
		item.setLayout(layout);
		return item;
	}

	public int getLayout() {
		return mLayout;
	}

	public String getTitle() {
		return mTitle;
	}
	public String getSubtitle() {
		return mSubtitle;
	}
	public String getComment() {
		return mComment;
	}
	public String getImage() {
		return mImage;
	}
	public int getDefaultImage() {
		return mResource;
	}
	public String getState() {
		return mState;
	}
	public Object getData() {
		return mData;
	}
	public void setTitle(String title) {
		mTitle = title;
	}
	public void setSubtitle(String subtitle) {
		mSubtitle = subtitle;
	}
	public void setComment(String comment) {
		mComment = comment;
	}
	public void setImage(String url) {
		mImage = url;
	}
	public void setDefaultImage(int resource) {
		mResource = resource;
	}
	public void setState(String url) {
		mState = url;
	}
	public void setData(Object object) {
		mData = object;
	}
	public void setLayout(int layout) {
		mLayout = layout;
	}
}
