/*
 * Copyright (C) 2019 Adam Huang <poisondog@gmail.com>
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
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import poisondog.core.Mission;

/**
 * reference from https://stackoverflow.com/questions/32949971/how-can-i-make-sticky-headers-in-recyclerview-without-external-lib
 * @author Adam Huang
 * @since 2019-02-21
 */
public class StickHeader extends RecyclerView.ItemDecoration {
	private RecyclerAdapter mAdapter;
	private int mStickyHeaderHeight;
	private Mission<DataItem> mHeaderFactory;

	public StickHeader(RecyclerView recyclerView, RecyclerAdapter adapter) {
		mAdapter = adapter;
		mHeaderFactory = new DefaultHeaderFactory(recyclerView.getContext());

		// On Sticky Header Click
		recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
			public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
				if (motionEvent.getY() <= mStickyHeaderHeight) {
					// Handle the clicks on the header here ...
					return true;
				}
				return false;
			}
			public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {}
			public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
		});
	}

	@Override
	public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
		super.onDrawOver(c, parent, state);

		View topChild = parent.getChildAt(0);
		if (topChild == null) {
			return;
		}

		int topChildPosition = parent.getChildAdapterPosition(topChild);
		if (topChildPosition == RecyclerView.NO_POSITION) {
			return;
		}

		View currentHeader = getHeaderViewForItem(topChildPosition, parent);
		if (currentHeader == null) {
			return;
		}

		fixLayoutSize(parent, currentHeader);
		int contactPoint = currentHeader.getBottom();
		View childInContact = getChildInContact(parent, contactPoint);
		if (childInContact == null) {
			return;
		}

		if (isHeader(parent.getChildAdapterPosition(childInContact))) {
			moveHeader(c, currentHeader, childInContact);
			return;
		}

		drawHeader(c, currentHeader);
	}

	private View getHeaderViewForItem(int itemPosition, RecyclerView parent) {
		int headerPosition = getHeaderPosition(itemPosition);
		if (headerPosition < 0)
			return null;
		try {
			return (View) mHeaderFactory.execute(mAdapter.getItem(headerPosition));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void drawHeader(Canvas c, View header) {
		c.save();
		c.translate(0, 0);
		header.draw(c);
		c.restore();
	}

	private void moveHeader(Canvas c, View currentHeader, View nextHeader) {
		c.save();
		c.translate(0, nextHeader.getTop() - currentHeader.getHeight());
		currentHeader.draw(c);
		c.restore();
	}

	private View getChildInContact(RecyclerView parent, int contactPoint) {
		View childInContact = null;
		for (int i = 0; i < parent.getChildCount(); i++) {
			View child = parent.getChildAt(i);
			if (child.getBottom() > contactPoint) {
				if (child.getTop() <= contactPoint) {
					// This child overlaps the contactPoint
					childInContact = child;
					break;
				}
			}
		}
		return childInContact;
	}

	/**
	 * Properly measures and layouts the top sticky header.
	 * @param parent ViewGroup: RecyclerView in this case.
	 */
	private void fixLayoutSize(ViewGroup parent, View view) {
		// Specs for parent (RecyclerView)
		int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
		int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

		// Specs for children (headers)
		ViewGroup.LayoutParams params = view.getLayoutParams();
		int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), params.width);
		int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), params.height);

		view.measure(childWidthSpec, childHeightSpec);
		view.layout(0, 0, view.getMeasuredWidth(), mStickyHeaderHeight = view.getMeasuredHeight());
	}

	private int getHeaderPosition(int itemPosition) {
		do {
			if (isHeader(itemPosition)) {
				return itemPosition;
			}
			itemPosition -= 1;
		} while (itemPosition >= 0);
		return -1;
	}

	private boolean isHeader(int itemPosition) {
		// TODO need to consider custom Header situation, current use default header layout
		return mAdapter.getItem(itemPosition).getLayout() == R.layout.header_item;
	}

	public void setHeaderFactory(Mission<DataItem> factory) {
		mHeaderFactory = factory;
	}

	public void setAdapter(RecyclerAdapter adapter) {
		mAdapter = adapter;
	}

	class DefaultHeaderFactory implements Mission<DataItem> {
		private Context mContext;
		public DefaultHeaderFactory(Context context) {
			mContext = context;
		}
		@Override
		public View execute(DataItem item) {
			ComplexItemView result = new ComplexItemView(mContext);
			result.setLayout(R.layout.header_item);
			result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			result.setItem(item);
			result.getTitle().setText(item.getTitle());
			if (item.getSubtitle() == null)
				result.getSubtitle().setVisibility(View.GONE);
			else
				result.getSubtitle().setText(item.getSubtitle());

			if (item.getComment() == null)
				result.getComment().setVisibility(View.GONE);
			else
				result.getComment().setText(item.getComment());
			return result;
		}
	}

}
