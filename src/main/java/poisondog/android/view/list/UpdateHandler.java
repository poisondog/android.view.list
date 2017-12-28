/*
 * Copyright (C) 2017 Adam Huang <poisondog@gmail.com>
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

import poisondog.core.Mission;

/**
 * @author Adam Huang
 * @since 2017-12-28
 */
public abstract class UpdateHandler implements Mission<UpdateHandler.Parameter> {
	public static class Parameter {
		private ListItemView mView;
		private ComplexListItem mContent;
		public Parameter(ListItemView view, ComplexListItem content) {
			mView = view;
			mContent = content;
		}

		public ComplexListItem getContent() {
			return mContent;
		}

		public ListItemView getView() {
			return mView;
		}
	}
}
