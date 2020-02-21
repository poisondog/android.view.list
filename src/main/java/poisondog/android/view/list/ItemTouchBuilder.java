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
package poisondog.android.view.list;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.RecyclerView;
import poisondog.core.Mission;
import poisondog.core.NoMission;

/**
 * @author Adam Huang
 * @since 2020-02-21
 */
public class ItemTouchBuilder implements Mission<RecyclerView> {
	private int mDrag;
	private int mSwipe;
	private Mission<RecyclerView.ViewHolder[]> mMoveHandler;
	private Mission<RecyclerView.ViewHolder> mLeftSwipeHandler;
	private Mission<RecyclerView.ViewHolder> mRightSwipeHandler;

	/**
	 * Constructor
	 */
	public ItemTouchBuilder() {
		mDrag = 0;
		mSwipe = 0;
		mMoveHandler = new NoMission<RecyclerView.ViewHolder[]>();
		mLeftSwipeHandler = new NoMission<RecyclerView.ViewHolder>();
		mRightSwipeHandler = new NoMission<RecyclerView.ViewHolder>();
	}

	public void setMoveHandler(Mission<RecyclerView.ViewHolder[]> handler) {
		mDrag |= ItemTouchHelper.UP;
		mDrag |= ItemTouchHelper.DOWN;
		mMoveHandler = handler;
	}

	public void setLeftHandler(Mission<RecyclerView.ViewHolder> handler) {
		mSwipe |= ItemTouchHelper.LEFT;
		mLeftSwipeHandler = handler;
	}

	public void setRightHandler(Mission<RecyclerView.ViewHolder> handler) {
		mSwipe |= ItemTouchHelper.RIGHT;
		mRightSwipeHandler = handler;
	}

	@Override
	public Void execute(RecyclerView view) {
		new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(mDrag, mSwipe) {
			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder fromHolder, RecyclerView.ViewHolder toHolder) {
				try {
					return (Boolean) mMoveHandler.execute(new RecyclerView.ViewHolder[]{fromHolder, toHolder});
				} catch(Exception e) {
					e.printStackTrace();
				}
				// true if moved, false otherwise
				return false;
			}
			@Override
			public void onSwiped(RecyclerView.ViewHolder targetHolder, int direction) {
				try {
					if (direction == ItemTouchHelper.LEFT) {
						mLeftSwipeHandler.execute(targetHolder);
					}
					if (direction == ItemTouchHelper.RIGHT) {
						mRightSwipeHandler.execute(targetHolder);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}).attachToRecyclerView(view);
		return null;
	}
}
