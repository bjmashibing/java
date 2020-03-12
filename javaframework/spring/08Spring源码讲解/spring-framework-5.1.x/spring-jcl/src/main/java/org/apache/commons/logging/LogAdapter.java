/*
 * Copyright 2002-2018 the original author or authors.
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

package org.apache.commons.logging;

import java.io.Serializable;
import java.util.logging.LogRecord;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

/**
 * Spring's common JCL adapter behind {@link LogFactory} and {@link LogFactoryService}.
 * Detects the presence of Log4j 2.x / SLF4J, falling back to {@code java.util.logging}.
 *
 * @author Juergen Hoeller
 * @since 5.1
 */
final class LogAdapter {

	private static final String LOG4J_SPI = "org.apache.logging.log4j.spi.ExtendedLogger";

	private static final String LOG4J_SLF4J_PROVIDER = "org.apache.logging.slf4j.SLF4JProvider";

	private static final String SLF4J_SPI = "org.slf4j.spi.LocationAwareLogger";

	private static final String SLF4J_API = "org.slf4j.Logger";


	private static final LogApi logApi;

	static {
		if (isPresent(LOG4J_SPI)) {
			if (isPresent(LOG4J_SLF4J_PROVIDER) && isPresent(SLF4J_SPI)) {
				// log4j-to-slf4j bridge -> we'll rather go with the SLF4J SPI;
				// however, we still prefer Log4j over the plain SLF4J API since
				// the latter does not have location awareness support.
				logApi = LogApi.SLF4J_LAL;
			}
			else {
				// Use Log4j 2.x directly, including location awareness support
				logApi = LogApi.LOG4J;
			}
		}
		else if (isPresent(SLF4J_SPI)) {
			// Full SLF4J SPI including location awareness support
			logApi = LogApi.SLF4J_LAL;
		}
		else if (isPresent(SLF4J_API)) {
			// Minimal SLF4J API without location awareness support
			logApi = LogApi.SLF4J;
		}
		else {
			// java.util.logging as default
			logApi = LogApi.JUL;
		}
	}


	private LogAdapter() {
	}


	/**
	 * Create an actual {@link Log} instance for the selected API.
	 * @param name the logger name
	 */
	public static Log createLog(String name) {
		switch (logApi) {
			case LOG4J:
				return Log4jAdapter.createLog(name);
			case SLF4J_LAL:
				return Slf4jAdapter.createLocationAwareLog(name);
			case SLF4J:
				return Slf4jAdapter.createLog(name);
			default:
				// Defensively use lazy-initializing adapter class here as well since the
				// java.logging module is not present by default on JDK 9. We are requiring
				// its presence if neither Log4j nor SLF4J is available; however, in the
				// case of Log4j or SLF4J, we are trying to prevent early initialization
				// of the JavaUtilLog adapter - e.g. by a JVM in debug mode - when eagerly
				// trying to parse the bytecode for all the cases of this switch clause.
				return JavaUtilAdapter.createLog(name);
		}
	}

	private static boolean isPresent(String className) {
		try {
			Class.forName(className, false, LogAdapter.class.getClassLoader());
			return true;
		}
		catch (ClassNotFoundException ex) {
			return false;
		}
	}


	private enum LogApi {LOG4J, SLF4J_LAL, SLF4J, JUL}


	private static class Log4jAdapter {

		public static Log createLog(String name) {
			return new Log4jLog(name);
		}
	}


	private static class Slf4jAdapter {

		public static Log createLocationAwareLog(String name) {
			Logger logger = LoggerFactory.getLogger(name);
			return (logger instanceof LocationAwareLogger ?
					new Slf4jLocationAwareLog((LocationAwareLogger) logger) : new Slf4jLog<>(logger));
		}

		public static Log createLog(String name) {
			return new Slf4jLog<>(LoggerFactory.getLogger(name));
		}
	}


	private static class JavaUtilAdapter {

		public static Log createLog(String name) {
			return new JavaUtilLog(name);
		}
	}


	@SuppressWarnings("serial")
	private static class Log4jLog implements Log, Serializable {

		private static final String FQCN = Log4jLog.class.getName();

		private static final LoggerContext loggerContext =
				LogManager.getContext(Log4jLog.class.getClassLoader(), false);

		private final ExtendedLogger logger;

		public Log4jLog(String name) {
			this.logger = loggerContext.getLogger(name);
		}

		@Override
		public boolean isFatalEnabled() {
			return this.logger.isEnabled(Level.FATAL);
		}

		@Override
		public boolean isErrorEnabled() {
			return this.logger.isEnabled(Level.ERROR);
		}

		@Override
		public boolean isWarnEnabled() {
			return this.logger.isEnabled(Level.WARN);
		}

		@Override
		public boolean isInfoEnabled() {
			return this.logger.isEnabled(Level.INFO);
		}

		@Override
		public boolean isDebugEnabled() {
			return this.logger.isEnabled(Level.DEBUG);
		}

		@Override
		public boolean isTraceEnabled() {
			return this.logger.isEnabled(Level.TRACE);
		}

		@Override
		public void fatal(Object message) {
			log(Level.FATAL, message, null);
		}

		@Override
		public void fatal(Object message, Throwable exception) {
			log(Level.FATAL, message, exception);
		}

		@Override
		public void error(Object message) {
			log(Level.ERROR, message, null);
		}

		@Override
		public void error(Object message, Throwable exception) {
			log(Level.ERROR, message, exception);
		}

		@Override
		public void warn(Object message) {
			log(Level.WARN, message, null);
		}

		@Override
		public void warn(Object message, Throwable exception) {
			log(Level.WARN, message, exception);
		}

		@Override
		public void info(Object message) {
			log(Level.INFO, message, null);
		}

		@Override
		public void info(Object message, Throwable exception) {
			log(Level.INFO, message, exception);
		}

		@Override
		public void debug(Object message) {
			log(Level.DEBUG, message, null);
		}

		@Override
		public void debug(Object message, Throwable exception) {
			log(Level.DEBUG, message, exception);
		}

		@Override
		public void trace(Object message) {
			log(Level.TRACE, message, null);
		}

		@Override
		public void trace(Object message, Throwable exception) {
			log(Level.TRACE, message, exception);
		}

		private void log(Level level, Object message, Throwable exception) {
			if (message instanceof String) {
				// Explicitly pass a String argument, avoiding Log4j's argument expansion
				// for message objects in case of "{}" sequences (SPR-16226)
				if (exception != null) {
					this.logger.logIfEnabled(FQCN, level, null, (String) message, exception);
				}
				else {
					this.logger.logIfEnabled(FQCN, level, null, (String) message);
				}
			}
			else {
				this.logger.logIfEnabled(FQCN, level, null, message, exception);
			}
		}
	}


	@SuppressWarnings("serial")
	private static class Slf4jLog<T extends Logger> implements Log, Serializable {

		protected final String name;

		protected transient T logger;

		public Slf4jLog(T logger) {
			this.name = logger.getName();
			this.logger = logger;
		}

		public boolean isFatalEnabled() {
			return isErrorEnabled();
		}

		public boolean isErrorEnabled() {
			return this.logger.isErrorEnabled();
		}

		public boolean isWarnEnabled() {
			return this.logger.isWarnEnabled();
		}

		public boolean isInfoEnabled() {
			return this.logger.isInfoEnabled();
		}

		public boolean isDebugEnabled() {
			return this.logger.isDebugEnabled();
		}

		public boolean isTraceEnabled() {
			return this.logger.isTraceEnabled();
		}

		public void fatal(Object message) {
			error(message);
		}

		public void fatal(Object message, Throwable exception) {
			error(message, exception);
		}

		public void error(Object message) {
			if (message instanceof String || this.logger.isErrorEnabled()) {
				this.logger.error(String.valueOf(message));
			}
		}

		public void error(Object message, Throwable exception) {
			if (message instanceof String || this.logger.isErrorEnabled()) {
				this.logger.error(String.valueOf(message), exception);
			}
		}

		public void warn(Object message) {
			if (message instanceof String || this.logger.isWarnEnabled()) {
				this.logger.warn(String.valueOf(message));
			}
		}

		public void warn(Object message, Throwable exception) {
			if (message instanceof String || this.logger.isWarnEnabled()) {
				this.logger.warn(String.valueOf(message), exception);
			}
		}

		public void info(Object message) {
			if (message instanceof String || this.logger.isInfoEnabled()) {
				this.logger.info(String.valueOf(message));
			}
		}

		public void info(Object message, Throwable exception) {
			if (message instanceof String || this.logger.isInfoEnabled()) {
				this.logger.info(String.valueOf(message), exception);
			}
		}

		public void debug(Object message) {
			if (message instanceof String || this.logger.isDebugEnabled()) {
				this.logger.debug(String.valueOf(message));
			}
		}

		public void debug(Object message, Throwable exception) {
			if (message instanceof String || this.logger.isDebugEnabled()) {
				this.logger.debug(String.valueOf(message), exception);
			}
		}

		public void trace(Object message) {
			if (message instanceof String || this.logger.isTraceEnabled()) {
				this.logger.trace(String.valueOf(message));
			}
		}

		public void trace(Object message, Throwable exception) {
			if (message instanceof String || this.logger.isTraceEnabled()) {
				this.logger.trace(String.valueOf(message), exception);
			}
		}

		protected Object readResolve() {
			return Slf4jAdapter.createLog(this.name);
		}
	}


	@SuppressWarnings("serial")
	private static class Slf4jLocationAwareLog extends Slf4jLog<LocationAwareLogger> implements Serializable {

		private static final String FQCN = Slf4jLocationAwareLog.class.getName();

		public Slf4jLocationAwareLog(LocationAwareLogger logger) {
			super(logger);
		}

		@Override
		public void fatal(Object message) {
			error(message);
		}

		@Override
		public void fatal(Object message, Throwable exception) {
			error(message, exception);
		}

		@Override
		public void error(Object message) {
			if (message instanceof String || this.logger.isErrorEnabled()) {
				this.logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, String.valueOf(message), null, null);
			}
		}

		@Override
		public void error(Object message, Throwable exception) {
			if (message instanceof String || this.logger.isErrorEnabled()) {
				this.logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, String.valueOf(message), null, exception);
			}
		}

		@Override
		public void warn(Object message) {
			if (message instanceof String || this.logger.isWarnEnabled()) {
				this.logger.log(null, FQCN, LocationAwareLogger.WARN_INT, String.valueOf(message), null, null);
			}
		}

		@Override
		public void warn(Object message, Throwable exception) {
			if (message instanceof String || this.logger.isWarnEnabled()) {
				this.logger.log(null, FQCN, LocationAwareLogger.WARN_INT, String.valueOf(message), null, exception);
			}
		}

		@Override
		public void info(Object message) {
			if (message instanceof String || this.logger.isInfoEnabled()) {
				this.logger.log(null, FQCN, LocationAwareLogger.INFO_INT, String.valueOf(message), null, null);
			}
		}

		@Override
		public void info(Object message, Throwable exception) {
			if (message instanceof String || this.logger.isInfoEnabled()) {
				this.logger.log(null, FQCN, LocationAwareLogger.INFO_INT, String.valueOf(message), null, exception);
			}
		}

		@Override
		public void debug(Object message) {
			if (message instanceof String || this.logger.isDebugEnabled()) {
				this.logger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, String.valueOf(message), null, null);
			}
		}

		@Override
		public void debug(Object message, Throwable exception) {
			if (message instanceof String || this.logger.isDebugEnabled()) {
				this.logger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, String.valueOf(message), null, exception);
			}
		}

		@Override
		public void trace(Object message) {
			if (message instanceof String || this.logger.isTraceEnabled()) {
				this.logger.log(null, FQCN, LocationAwareLogger.TRACE_INT, String.valueOf(message), null, null);
			}
		}

		@Override
		public void trace(Object message, Throwable exception) {
			if (message instanceof String || this.logger.isTraceEnabled()) {
				this.logger.log(null, FQCN, LocationAwareLogger.TRACE_INT, String.valueOf(message), null, exception);
			}
		}

		@Override
		protected Object readResolve() {
			return Slf4jAdapter.createLocationAwareLog(this.name);
		}
	}


	@SuppressWarnings("serial")
	private static class JavaUtilLog implements Log, Serializable {

		private String name;

		private transient java.util.logging.Logger logger;

		public JavaUtilLog(String name) {
			this.name = name;
			this.logger = java.util.logging.Logger.getLogger(name);
		}

		public boolean isFatalEnabled() {
			return isErrorEnabled();
		}

		public boolean isErrorEnabled() {
			return this.logger.isLoggable(java.util.logging.Level.SEVERE);
		}

		public boolean isWarnEnabled() {
			return this.logger.isLoggable(java.util.logging.Level.WARNING);
		}

		public boolean isInfoEnabled() {
			return this.logger.isLoggable(java.util.logging.Level.INFO);
		}

		public boolean isDebugEnabled() {
			return this.logger.isLoggable(java.util.logging.Level.FINE);
		}

		public boolean isTraceEnabled() {
			return this.logger.isLoggable(java.util.logging.Level.FINEST);
		}

		public void fatal(Object message) {
			error(message);
		}

		public void fatal(Object message, Throwable exception) {
			error(message, exception);
		}

		public void error(Object message) {
			log(java.util.logging.Level.SEVERE, message, null);
		}

		public void error(Object message, Throwable exception) {
			log(java.util.logging.Level.SEVERE, message, exception);
		}

		public void warn(Object message) {
			log(java.util.logging.Level.WARNING, message, null);
		}

		public void warn(Object message, Throwable exception) {
			log(java.util.logging.Level.WARNING, message, exception);
		}

		public void info(Object message) {
			log(java.util.logging.Level.INFO, message, null);
		}

		public void info(Object message, Throwable exception) {
			log(java.util.logging.Level.INFO, message, exception);
		}

		public void debug(Object message) {
			log(java.util.logging.Level.FINE, message, null);
		}

		public void debug(Object message, Throwable exception) {
			log(java.util.logging.Level.FINE, message, exception);
		}

		public void trace(Object message) {
			log(java.util.logging.Level.FINEST, message, null);
		}

		public void trace(Object message, Throwable exception) {
			log(java.util.logging.Level.FINEST, message, exception);
		}

		private void log(java.util.logging.Level level, Object message, Throwable exception) {
			if (this.logger.isLoggable(level)) {
				LogRecord rec;
				if (message instanceof LogRecord) {
					rec = (LogRecord) message;
				}
				else {
					rec = new LocationResolvingLogRecord(level, String.valueOf(message));
					rec.setLoggerName(this.name);
					rec.setResourceBundleName(logger.getResourceBundleName());
					rec.setResourceBundle(logger.getResourceBundle());
					rec.setThrown(exception);
				}
				logger.log(rec);
			}
		}

		protected Object readResolve() {
			return new JavaUtilLog(this.name);
		}
	}


	@SuppressWarnings("serial")
	private static class LocationResolvingLogRecord extends LogRecord {

		private static final String FQCN = JavaUtilLog.class.getName();

		private volatile boolean resolved;

		public LocationResolvingLogRecord(java.util.logging.Level level, String msg) {
			super(level, msg);
		}

		@Override
		public String getSourceClassName() {
			if (!this.resolved) {
				resolve();
			}
			return super.getSourceClassName();
		}

		@Override
		public void setSourceClassName(String sourceClassName) {
			super.setSourceClassName(sourceClassName);
			this.resolved = true;
		}

		@Override
		public String getSourceMethodName() {
			if (!this.resolved) {
				resolve();
			}
			return super.getSourceMethodName();
		}

		@Override
		public void setSourceMethodName(String sourceMethodName) {
			super.setSourceMethodName(sourceMethodName);
			this.resolved = true;
		}

		private void resolve() {
			StackTraceElement[] stack = new Throwable().getStackTrace();
			String sourceClassName = null;
			String sourceMethodName = null;
			boolean found = false;
			for (StackTraceElement element : stack) {
				String className = element.getClassName();
				if (FQCN.equals(className)) {
					found = true;
				}
				else if (found) {
					sourceClassName = className;
					sourceMethodName = element.getMethodName();
					break;
				}
			}
			setSourceClassName(sourceClassName);
			setSourceMethodName(sourceMethodName);
		}

		@SuppressWarnings("deprecation")  // setMillis is deprecated in JDK 9
		protected Object writeReplace() {
			LogRecord serialized = new LogRecord(getLevel(), getMessage());
			serialized.setLoggerName(getLoggerName());
			serialized.setResourceBundle(getResourceBundle());
			serialized.setResourceBundleName(getResourceBundleName());
			serialized.setSourceClassName(getSourceClassName());
			serialized.setSourceMethodName(getSourceMethodName());
			serialized.setSequenceNumber(getSequenceNumber());
			serialized.setParameters(getParameters());
			serialized.setThreadID(getThreadID());
			serialized.setMillis(getMillis());
			serialized.setThrown(getThrown());
			return serialized;
		}
	}

}
