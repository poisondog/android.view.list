package poisondog.android.view.list.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import java.util.ArrayList;
import java.util.Collection;
import poisondog.android.view.FileView;
import poisondog.android.view.list.app.R;
import poisondog.android.view.list.ListAdapter;
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

		FileView list = (FileView) findViewById(R.id.resources);

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
