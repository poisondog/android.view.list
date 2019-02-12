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

import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * @author Adam Huang
 * @since 2019-02-12
 */
public interface ItemView {
	public ImageView getImage();
	public ImageView getState();
	public TextView getTitle();
	public TextView getSubtitle();
	public TextView getComment();
	public ProgressBar getProgress();
	public void setData(Object data);
	public Object getData();
}
