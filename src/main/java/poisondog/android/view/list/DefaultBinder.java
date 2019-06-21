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
import android.view.View;
import poisondog.android.image.ImageFetcher;
import poisondog.android.view.list.DataItem;
import poisondog.core.Mission;
import poisondog.util.Pair;

/**
 * @author Adam Huang
 * @since 2019-03-15
 */
public class DefaultBinder implements Mission<Pair<View, DataItem>> {
	private ImageFetcher mFetcher;

	/**
	 * Constructor
	 */
	public DefaultBinder(Context context) {
		try {
			mFetcher = new ImageFetcher(context, 500, 500, context.getExternalCacheDir().getPath() + "/");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Void execute(Pair<View, DataItem> pair) {
		if (!(pair.getValue1() instanceof ItemView))
			return null;
		ItemView row = (ItemView) pair.getValue1();
		DataItem obj = pair.getValue2();
		row.setItem(obj);
		row.getTitle().setText(obj.getTitle());
		if (obj.getSubtitle() == null) {
			// TODO no more to use setVisibility
			row.getSubtitle().setVisibility(View.GONE);
		} else {
			row.getSubtitle().setText(obj.getSubtitle());
		}

		if (obj.getComment() == null) {
			row.getComment().setText("");
//			row.getComment().setVisibility(View.GONE);
		} else {
//			row.getComment().setVisibility(View.VISIBLE);
			row.getComment().setText(obj.getComment());
		}

		if (obj.getImage() == null && obj.getDefaultImage() <= 0) {
			row.getImage().setVisibility(View.GONE);
		}
		if (obj.getDefaultImage() > 0) {
			mFetcher.setLoadingImage(obj.getDefaultImage());
			row.getImage().setImageResource(obj.getDefaultImage());
		}
		if (obj.getImage() != null) {
			mFetcher.loadImage(obj.getImage(), row.getImage());
		}

		if (obj.getState() == null)
			row.getState().setVisibility(View.GONE);
		else
			mFetcher.loadImage(obj.getState(), row.getState());
		return null;
	}
}
