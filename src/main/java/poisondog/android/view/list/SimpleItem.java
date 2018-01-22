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

/**
 * @author Adam Huang
 * @since 2018-01-22
 */
public class SimpleItem implements ListItem {
	private String mTitle;
	private String mSubtitle;
	private String mComment;

	/**
	 * Constructor
	 */
	public SimpleItem(String title) {
		mTitle = title;
	}

	/**
	 * Constructor
	 */
	public SimpleItem(String title, String subtitle, String comment) {
		mTitle = title;
		mSubtitle = subtitle;
		mComment = comment;
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
		return null;
	}
	public String getState() {
		return null;
	}
	public Object getData() {
		return mTitle;
	}
	public void setData(Object object) {
		mTitle = object.toString();
	}
}
