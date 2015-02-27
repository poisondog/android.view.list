/*
 * Copyright (C) 2015 Adam Huang <poisondog@gmail.com>
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
package poisondog.android.log;

import android.util.Log;
import java.util.Set;
import poisondog.log.Logger;
import poisondog.log.LogLevel;
import poisondog.log.LowerThen;
/**
 * @author Adam Huang <poisondog@gmail.com>
 */
public class AndroidLogger implements Logger {
	private static Set<LogLevel> sLevels;
	private String mTag;

	static {
		sLevels = new LowerThen(LogLevel.INFO);
	}

	public AndroidLogger(String tag) {
		mTag = tag;
	}

	@Override
	public void log(LogLevel level, String message) {
		if (!sLevels.contains(level))
			return;
		if (level == LogLevel.TRACE)
			Log.v(mTag, message);
		else if (level == LogLevel.DEBUG)
			Log.d(mTag, message);
		else if (level == LogLevel.INFO)
			Log.i(mTag, message);
		else if (level == LogLevel.WARNING)
			Log.w(mTag, message);
		else if (level == LogLevel.ERROR)
			Log.e(mTag, message);
	}

	public static void setDefaultLogLevel(LogLevel level) {
		sLevels = new LowerThen(level);
	}
}
