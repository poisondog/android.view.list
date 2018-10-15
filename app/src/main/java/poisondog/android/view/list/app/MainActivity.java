package poisondog.android.view.list.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;
import java.util.Collection;
import poisondog.android.view.list.ListAdapter;
import poisondog.android.view.list.SimpleItem;
import poisondog.format.SizeFormatUtils;
import poisondog.android.view.list.app.R;
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

		ListView list = (ListView) findViewById(R.id.resources);
		ListAdapter adapter = new ListAdapter(this);
		adapter.addItem(new SimpleItem("title 1"));
		adapter.addItem(new SimpleItem("title 2", "hey", "hello"));
		adapter.addItem(new SimpleItem("title 3", "world", "!!"));
		list.setAdapter(adapter);

		String download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/";
		try {
			IFolder mFolder = (IFolder)FileFactory.getFile(download);
			FileFilter filter = new FileFilter();
			filter.setIncludeRule(new OnlyImage());
			Collection<IFile> result = filter.execute(mFolder.getChildren());
			for (IFile f : result) {
				IData data = (IData)f;
				String filename = URLUtils.file(f.getUrl());
				String time = TimeFormatUtils.toString(f.getLastModifiedTime());
				String size = SizeFormatUtils.toString(data.getSize());
				SimpleItem item = new SimpleItem(filename, time, size);
				item.setDefaultImage(R.drawable.file_txt);
				item.setImage(data.getUrl());
				adapter.addItem(item);
			}
			adapter.notifyDataSetChanged();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
