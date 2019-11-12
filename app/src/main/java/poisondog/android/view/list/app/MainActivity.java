package poisondog.android.view.list.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import poisondog.android.view.EntityView;
import poisondog.android.view.list.app.R;
import poisondog.android.view.list.DataItem;
import poisondog.android.view.list.ItemView;
import poisondog.android.view.list.SimpleItem;
import poisondog.core.Mission;
import poisondog.format.SizeFormatUtils;
import poisondog.format.TimeFormatUtils;
import poisondog.net.URLUtils;
import poisondog.vfs.comparator.NameOrder;
import poisondog.vfs.FileFactory;
import poisondog.vfs.filter.FileFilter;
import poisondog.vfs.filter.OnlyImage;
import poisondog.vfs.IData;
import poisondog.vfs.IFile;
import poisondog.vfs.IFolder;

public class MainActivity extends Activity {
	private EntityView mRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		int resId = R.anim.layout_animation_right_to_left;
		LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);

		LinearLayout root = (LinearLayout) findViewById(R.id.root);
		mRoot = new EntityView(this);
		mRoot.getRecyclerView().setLayoutAnimation(animation);
		mRoot.setLayoutManager(new LinearLayoutManager(this));
		mRoot.setRefreshHandler(new RefreshData());

		mRoot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ItemView target = (ItemView) v;
				try {
					System.out.println(URLUtils.file(((IData)target.getItem().getData()).getUrl()));
				} catch(Exception e) {
				}
			}
		});

		mRoot.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
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
		});

		mRoot.refresh();
		root.addView(mRoot);
	}

	class ListItemFactory implements Mission<IFile> {
		@Override
		public DataItem execute(IFile f) throws Exception {
			IData data = (IData)f;
			String filename = URLUtils.file(f.getUrl());
			String time = TimeFormatUtils.toString(f.getLastModifiedTime());
			String size = SizeFormatUtils.toString(data.getSize());
			SimpleItem item = new SimpleItem(filename, time, size);
			item.setDefaultImage(R.drawable.file_txt);
			item.setData(data);
			item.setImage(data.getUrl());
			return item;
		}
	}

	class RefreshData implements Runnable {
		private ListItemFactory mCreator;
		/**
		 * Constructor
		 */
		public RefreshData() {
			mCreator = new ListItemFactory();
		}
		@Override
		public void run() {
			mRoot.setLoading(true);

			mRoot.setLayoutManager(new LinearLayoutManager(MainActivity.this));
			List<IFile> content = new ArrayList<IFile>();
			String download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/";
			try {
				IFolder mFolder = (IFolder)FileFactory.getFile(download);
				FileFilter filter = new FileFilter();
				filter.setIncludeRule(new OnlyImage());
				content = new ArrayList<IFile>(filter.execute(mFolder.getChildren()));
			} catch(Exception e) {
				e.printStackTrace();
			}

			Collections.sort(content, new NameOrder());
			ArrayList<DataItem> result = new ArrayList<DataItem>();
			result.add(SimpleItem.header("Head", "second", "third"));
			for (IFile f : content) {
				try {
					result.add(mCreator.execute(f));
				} catch(Exception e) {
				}
				if (Math.random() < 0.1)
					result.add(SimpleItem.header("first", "second", "third"));
			}
			mRoot.setItems(result);
		}
	}

}
