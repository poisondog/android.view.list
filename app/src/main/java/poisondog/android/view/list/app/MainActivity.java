package poisondog.android.view.list.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import poisondog.android.view.FileView;
import poisondog.android.view.FileView2;
import poisondog.android.view.list.app.R;
import poisondog.android.view.list.ListItem;
import poisondog.android.view.list.SimpleItem;
import poisondog.core.Mission;
import poisondog.format.SizeFormatUtils;
import poisondog.format.TimeFormatUtils;
import poisondog.net.URLUtils;
import poisondog.vfs.FileFactory;
import poisondog.vfs.filter.FileFilter;
import poisondog.vfs.filter.OnlyImage;
import poisondog.vfs.IData;
import poisondog.vfs.IFile;
import poisondog.vfs.IFolder;


public class MainActivity extends Activity {
	private DialogInterface.OnClickListener mListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		LinearLayout root = (LinearLayout) findViewById(R.id.root);
//		FileView list = new FileView(this);
		FileView2 list = new FileView2(this);
		String download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/";
		try {
			IFolder mFolder = (IFolder)FileFactory.getFile(download);
			FileFilter filter = new FileFilter();
			filter.setIncludeRule(new OnlyImage());
			list.setItemCreator(new PhotoCreator());
			list.setFiles(new ArrayList<IFile>(filter.execute(mFolder.getChildren())));
		} catch(Exception e) {
			e.printStackTrace();
		}
//		list.setEmpty(true);
//		list.setVisibility(View.GONE);
		root.addView(list);
	}

	class PhotoCreator implements Mission<IFile> {
		@Override
		public ListItem execute(IFile f) throws Exception {
			IData data = (IData)f;
			String filename = URLUtils.file(f.getUrl());
			String time = TimeFormatUtils.toString(f.getLastModifiedTime());
			String size = SizeFormatUtils.toString(data.getSize());
			SimpleItem item = new SimpleItem(filename, time, size);
			item.setDefaultImage(R.drawable.file_txt);
			item.setImage(data.getUrl());
			return item;
		}
	}
}
