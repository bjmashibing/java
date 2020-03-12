/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.core.log;

import java.util.function.Function;

import org.apache.commons.logging.Log;

import org.springframework.lang.Nullable;

/**
 * Utility methods for formatting and logging messages.
 *
 * <p>Mainly for internal use within the framework with Apache Commons Logging,
 * typically in the form of the {@code spring-jcl} bridge but also compatible
 * with other Commons Logging bridges.
 *
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @since 5.1
 */
public abstract class LogFormatUtils {

	/**
	 * Format the given value via {@code toString()}, quoting it if it is a
	 * {@link CharSequence}, and possibly truncating at 100 if limitLength is
	 * set to true.
	 * @param value the value to format
	 * @param limitLength whether to truncate large formatted values (over 100)
	 * @return the formatted value
	 */
	public static String formatValue(@Nullable Object value, boolean limitLength) {
		if (value == null) {
			return "";
		}
		String str;
		if (value instanceof CharSequence) {
			str = "\"" + value + "\"";
		}
		else {
			try {
				str = value.toString();
			}
			catch (Throwable ex) {
				str = ex.toString();
			}
		}
		return (limitLength && str.length() > 100 ? str.substring(0, 100) + " (truncated)..." : str);
	}

	/**
	 * Use this to log a message with different levels of detail (or different
	 * messages) at TRACE vs DEBUG log levels. Effectively, a substitute for:
	 * <pre class="code">
	 * if (logger.isDebugEnabled()) {
	 *   String str = logger.isTraceEnabled() ? "..." : "...";
	 *   if (logger.isTraceEnabled()) {
	 *     logger.trace(str);
	 *   }
	 *   else {
	 *     logger.debug(str);
	 *   }
	 * }
	 * </pre>
	 * @param logger the logger to use to log the message
	 * @param messageFactory function that accepts a boolean set to the value
	 * of {@link Log#isTraceEnabled()}
	 */
	public static void traceDebug(Log logger, Function<Boolean, String> messageFactory) {
		if (logger.isDebugEnabled()) {
			boolean traceEnabled = logger.isTraceEnabled();
			String logMessage = messageFactory.apply(traceEnabled);
			if (traceEnabled) {
				logger.trace(logMessage);
			}
			else {
				logger.debug(logMessage);
			}
		}
	}

}
