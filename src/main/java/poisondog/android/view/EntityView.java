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
package poisondog.android.view;

import android.content.Context;
import android.util.AttributeSet;
import poisondog.android.view.RefreshList;

/**
 * @author Adam Huang
 * @since 2018-02-06
 */
public class EntityView extends EntityContent {
	protected RefreshList mRefresh;

	/**
	 * Constructor
	 */
	public EntityView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * Constructor
	 */
	public EntityView(Context context, AttributeSet attribute) {
		super(context, attribute);
		init(context);
	}

	private void init(Context context) {
		mRefresh = new RefreshList(context);
		removeView(mLoading);
		mRefresh.addView(mLoading);
		addView(mRefresh);
		setLoading(false);
	}

	public void setRefreshHandler(Runnable handler) {
		mRefresh.setHandler(handler);
	}

	public void refresh() {
		mRefresh.onRefresh();
	}

}
