/*
 * Copyright (C) 2020 Adam Huang <poisondog@gmail.com>
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
package poisondog.android.view.list.app;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import poisondog.android.view.EntityContent;
import poisondog.android.view.list.app.R;
import poisondog.android.view.list.DataItem;

/**
 * @author Adam Huang
 * @since 2020-02-21
 */
public class LayoutToGrid implements View.OnLongClickListener, View.OnClickListener {
	private EntityContent mRoot;

	/**
	 * Constructor
	 */
	public LayoutToGrid(EntityContent root) {
		mRoot = root;
	}

	@Override
	public void onClick(View v) {
		onLongClick(v);
	}

	@Override
	public boolean onLongClick(View v) {
		GridLayoutManager layoutManager = new GridLayoutManager(mRoot.getContext(), 2);
		layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
			@Override
			public int getSpanSize(int position) {
				if (mRoot.getItemViewLayout(position) == R.layout.header_item)
					return 2;
				return 1;
			}
		});
		mRoot.setLayoutManager(layoutManager);
		for (DataItem item : mRoot.getItems()) {
			if (item.getLayout() == R.layout.list_item) {
				item.setLayout(R.layout.grid_item);
			}
		}
		return true;
	}
}
