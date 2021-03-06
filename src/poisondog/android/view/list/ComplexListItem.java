/*
 * Copyright (C) 2014 Adam Huang
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
/**
 * @author Adam Huang <poisondog@gmail.com>
 */
public interface ComplexListItem {
	public String getHideMessage();
	public String getTitle();
	public Object getData();

	public void setData(Object object);
	public void setSubTitle(TextView view);
	public void setComment(TextView view);
	public void setImage(ImageView view);
	public void setState(ImageView view);
}
