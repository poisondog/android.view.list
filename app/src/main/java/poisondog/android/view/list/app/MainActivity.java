package poisondog.android.view.list.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import poisondog.android.view.EntityView;
import poisondog.android.view.FileView;
import poisondog.android.view.list.app.R;
import poisondog.android.view.list.DataItem;
import poisondog.android.view.list.ItemView;
import poisondog.android.view.list.ListItemView;
import poisondog.android.view.list.SimpleItem;
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
		mRoot.setLayoutManager(new LinearLayoutManager(this));
		mRoot.setRefreshHandler(new RefreshData());
		String download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/";
		try {
			IFolder mFolder = (IFolder)FileFactory.getFile(download);
			FileFilter filter = new FileFilter();
			filter.setIncludeRule(new OnlyImage());
			mContent = new ArrayList<IFile>(filter.execute(mFolder.getChildren()));
			mRoot.refresh();
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
							if (mRoot.getItemViewType(position) == ViewType.Header)
								return 2;
							return 1;
						}
					});
					mRoot.setLayoutManager(layoutManager);

					GridCreator mCreator = new GridCreator();
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

	class GridCreator implements Mission<IFile> {
		@Override
		public DataItem execute(IFile f) throws Exception {
			IData data = (IData)f;
			String filename = URLUtils.file(f.getUrl());
			String time = TimeFormatUtils.toString(f.getLastModifiedTime());
			String size = SizeFormatUtils.toString(data.getSize());
			SimpleItem item = SimpleItem.layout(filename, time, size, R.layout.image_grid_item);
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

}
