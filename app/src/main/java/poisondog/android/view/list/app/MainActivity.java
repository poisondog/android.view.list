package poisondog.android.view.list.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import poisondog.android.view.EntityContent;
import poisondog.android.view.list.app.R;
import poisondog.android.view.list.ItemTouchBuilder;
import poisondog.core.Mission;

public class MainActivity extends Activity {
	private EntityContent mRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		int resId = R.anim.layout_animation_right_to_left;
		LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);

		LinearLayout root = (LinearLayout) findViewById(R.id.root);
		mRoot = new EntityContent(this);
		mRoot.getRecyclerView().setLayoutAnimation(animation);
		mRoot.setLayoutManager(new LinearLayoutManager(this));
//		mRoot.setRefreshHandler(new RefreshData());

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
//		Thread t = new Thread(refresher);
//		t.start();

//		mRoot.refresh();
		root.addView(mRoot);
	}

}
