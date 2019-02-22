package poisondog.android.view.list.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import poisondog.android.view.FileView;
import poisondog.android.view.EntityView;
import poisondog.android.view.list.app.R;
import poisondog.android.view.list.DataItem;
import poisondog.android.view.list.ItemView;
import poisondog.android.view.list.SimpleItem;
import poisondog.android.view.list.GridItemView;
import poisondog.android.view.list.HeaderItemView;
import poisondog.android.view.list.ViewType;
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
	private DialogInterface.OnClickListener mListener;
	private EntityView mRoot;
	private List<IFile> mContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		LinearLayout root = (LinearLayout) findViewById(R.id.root);
//		mRoot = new FileView(this);
		mRoot = new EntityView(this);
		mRoot.setItemViewFactory(new MyItemViewFactory());
		GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
		layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
			@Override
			public int getSpanSize(int position) {
				switch (mRoot.getItemViewType(position)) {
					case 0:
						return 1;
					case 1:
						return 2;
					default:
						return 1;
				}
			}
		});
		mRoot.setLayoutManager(layoutManager);
		mRoot.setRefreshHandler(new RefreshData());
		String download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/";
		try {
			IFolder mFolder = (IFolder)FileFactory.getFile(download);
			FileFilter filter = new FileFilter();
			filter.setIncludeRule(new OnlyImage());
			mContent = new ArrayList<IFile>(filter.execute(mFolder.getChildren()));
			mRoot.refresh();
//			mRoot.setFiles(mContent);

//			mRoot.setItemCreator(new PhotoCreator());
			mRoot.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ItemView target = (ItemView) v;
					try {
						System.out.println(URLUtils.file(((IData)target.getData()).getUrl()));
					} catch(Exception e) {
					}
				}
			});
			mRoot.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					ItemView target = (ItemView) v;
					try {
						System.out.println(((IData)target.getData()).getUrl());
					} catch(Exception e) {
					}
					return true;
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
//		mRoot.setEmpty(true);
		root.addView(mRoot);
	}

	class PhotoCreator implements Mission<IFile> {
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
		private PhotoCreator mCreator;
		/**
		 * Constructor
		 */
		public RefreshData() {
			mCreator = new PhotoCreator();
		}
		@Override
		public void run() {
			mRoot.setLoading(true);
			Collections.sort(mContent, new NameOrder());
			ArrayList<DataItem> result = new ArrayList<DataItem>();
			result.add(SimpleItem.header("Head", "second", "third"));
			for (IFile f : mContent) {
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

	class MyItemViewFactory implements Mission<ViewType> {
		@Override
		public ItemView execute(ViewType viewType) {
			if (viewType == ViewType.Header)
				return new HeaderItemView(MainActivity.this);
			return new GridItemView(MainActivity.this);
		}
	}
}
