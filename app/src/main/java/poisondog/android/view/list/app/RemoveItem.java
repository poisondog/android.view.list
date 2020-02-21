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

import android.support.v7.widget.RecyclerView;
import poisondog.android.view.list.RecyclerAdapter;
import poisondog.core.Mission;

/**
 * @author Adam Huang
 * @since 2020-02-21
 */
public class RemoveItem implements Mission<RecyclerView.ViewHolder> {
	private RecyclerAdapter mAdapter;

	/**
	 * Constructor
	 */
	public RemoveItem(RecyclerAdapter adapter) {
		mAdapter = adapter;
	}

	@Override
	public Void execute(RecyclerView.ViewHolder holder) {
		int position = holder.getAdapterPosition();
		mAdapter.remove(position);
		mAdapter.notifyDataSetChanged();
		return null;
	}
}
