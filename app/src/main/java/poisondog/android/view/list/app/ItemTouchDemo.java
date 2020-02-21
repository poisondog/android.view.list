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

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import poisondog.android.view.EntityContent;
import poisondog.android.view.list.app.R;
import poisondog.android.view.list.ItemTouchBuilder;
import poisondog.core.Mission;

/**
 * @author Adam Huang
 * @since 2020-02-21
 */
public class ItemTouchDemo extends Activity {
	private EntityContent mRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo);

		int resId = R.anim.layout_animation_right_to_left;
		LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);

		mRoot = new EntityContent(this);
		mRoot.getRecyclerView().setLayoutAnimation(animation);
		mRoot.setLayoutManager(new LinearLayoutManager(this));

//		mRoot.setOnClickListener(new LayoutToGrid(mRoot));
		mRoot.setOnClickListener(new PrintUrl());
//		mRoot.setOnLongClickListener(new LayoutToGrid(mRoot));

		ItemTouchBuilder builder = new ItemTouchBuilder();
		builder.setMoveHandler(new UpdateSort(mRoot.getRecyclerAdapter()));
		builder.setRightHandler(new RemoveItem(mRoot.getRecyclerAdapter()));
		builder.setLeftHandler(new Mission<RecyclerView.ViewHolder>() {
			@Override
			public Void execute(RecyclerView.ViewHolder holder) {
				System.out.println("swiped");
				return null;
			}
		});
		builder.execute(mRoot.getRecyclerView());

		RefreshData refresher = new RefreshData(mRoot);
		refresher.run();

		LinearLayout root = (LinearLayout) findViewById(R.id.root);
		root.addView(mRoot);
	}

}
