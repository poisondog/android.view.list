package poisondog.android.view.list.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.View;
import android.widget.Button;
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

		Button refresh = (Button) findViewById(R.id.refreshDemo);
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, RefreshDemo.class);
				startActivity(intent);
			}
		});

		Button itemTouch = (Button) findViewById(R.id.itemTouch);
		itemTouch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ItemTouchDemo.class);
				startActivity(intent);
			}
		});

	}

}
