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

package org.springframework.context.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;
import org.springframework.util.StringUtils;

/**
 * Spring-specific {@link org.springframework.context.MessageSource} implementation
 * that accesses resource bundles using specified basenames, participating in the
 * Spring {@link org.springframework.context.ApplicationContext}'s resource loading.
 *
 * <p>In contrast to the JDK-based {@link ResourceBundleMessageSource}, this class uses
 * {@link java.util.Properties} instances as its custom data structure for messages,
 * loading them via a {@link org.springframework.util.PropertiesPersister} strategy
 * from Spring {@link Resource} handles. This strategy is not only capable of
 * reloading files based on timestamp changes, but also of loading properties files
 * with a specific character encoding. It will detect XML property files as well.
 *
 * <p>Note that the basenames set as {@link #setBasenames "basenames"} property
 * are treated in a slightly different fashion than the "basenames" property of
 * {@link ResourceBundleMessageSource}. It follows the basic ResourceBundle rule of not
 * specifying file extension or language codes, but can refer to any Spring resource
 * location (instead of being restricted to classpath resources). With a "classpath:"
 * prefix, resources can still be loaded from the classpath, but "cacheSeconds" values
 * other than "-1" (caching forever) might not work reliably in this case.
 *
 * <p>For a typical web application, message files could be placed in {@code WEB-INF}:
 * e.g. a "WEB-INF/messages" basename would find a "WEB-INF/messages.properties",
 * "WEB-INF/messages_en.properties" etc arrangement as well as "WEB-INF/messages.xml",
 * "WEB-INF/messages_en.xml" etc. Note that message definitions in a <i>previous</i>
 * resource bundle will override ones in a later bundle, due to sequential lookup.

 * <p>This MessageSource can easily be used outside of an
 * {@link org.springframework.context.ApplicationContext}: it will use a
 * {@link org.springframework.core.io.DefaultResourceLoader} as default,
 * simply getting overridden with the ApplicationContext's resource loader
 * if running in a context. It does not have any other specific dependencies.
 *
 * <p>Thanks to Thomas Achleitner for providing the initial implementation of
 * this message source!
 *
 * @author Juergen Hoeller
 * @see #setCacheSeconds
 * @see #setBasenames
 * @see #setDefaultEncoding
 * @see #setFileEncodings
 * @see #setPropertiesPersister
 * @see #setResourceLoader
 * @see org.springframework.util.DefaultPropertiesPersister
 * @see org.springframework.core.io.DefaultResourceLoader
 * @see ResourceBundleMessageSource
 * @see java.util.ResourceBundle
 */
public class ReloadableResourceBundleMessageSource extends AbstractResourceBasedMessageSource
		implements ResourceLoaderAware {

	private static final String PROPERTIES_SUFFIX = ".properties";

	private static final String XML_SUFFIX = ".xml";


	@Nullable
	private Properties fileEncodings;

	private boolean concurrentRefresh = true;

	private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();

	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	// Cache to hold filename lists per Locale
	private final ConcurrentMap<String, Map<Locale, List<String>>> cachedFilenames = new ConcurrentHashMap<>();

	// Cache to hold already loaded properties per filename
	private final ConcurrentMap<String, PropertiesHolder> cachedProperties = new ConcurrentHashMap<>();

	// Cache to hold already loaded properties per filename
	private final ConcurrentMap<Locale, PropertiesHolder> cachedMergedProperties = new ConcurrentHashMap<>();


	/**
	 * Set per-file charsets to use for parsing properties files.
	 * <p>Only applies to classic properties files, not to XML files.
	 * @param fileEncodings a Properties with filenames as keys and charset
	 * names as values. Filenames have to match the basename syntax,
	 * with optional locale-specific components: e.g. "WEB-INF/messages"
	 * or "WEB-INF/messages_en".
	 * @see #setBasenames
	 * @see org.springframework.util.PropertiesPersister#load
	 */
	public void setFileEncodings(Properties fileEncodings) {
		this.fileEncodings = fileEncodings;
	}

	/**
	 * Specify whether to allow for concurrent refresh behavior, i.e. one thread
	 * locked in a refresh attempt for a specific cached properties file whereas
	 * other threads keep returning the old properties for the time being, until
	 * the refresh attempt has completed.
	 * <p>Default is "true": this behavior is new as of Spring Framework 4.1,
	 * minimizing contention between threads. If you prefer the old behavior,
	 * i.e. to fully block on refresh, switch this flag to "false".
	 * @since 4.1
	 * @see #setCacheSeconds
	 */
	public void setConcurrentRefresh(boolean concurrentRefresh) {
		this.concurrentRefresh = concurrentRefresh;
	}

	/**
	 * Set the PropertiesPersister to use for parsing properties files.
	 * <p>The default is a DefaultPropertiesPersister.
	 * @see org.springframework.util.DefaultPropertiesPersister
	 */
	public void setPropertiesPersister(@Nullable PropertiesPersister propertiesPersister) {
		this.propertiesPersister =
				(propertiesPersister != null ? propertiesPersister : new DefaultPropertiesPersister());
	}

	/**
	 * Set the ResourceLoader to use for loading bundle properties files.
	 * <p>The default is a DefaultResourceLoader. Will get overridden by the
	 * ApplicationContext if running in a context, as it implements the
	 * ResourceLoaderAware interface. Can be manually overridden when
	 * running outside of an ApplicationContext.
	 * @see org.springframework.core.io.DefaultResourceLoader
	 * @see org.springframework.context.ResourceLoaderAware
	 */
	@Override
	public void setResourceLoader(@Nullable ResourceLoader resourceLoader) {
		this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
	}


	/**
	 * Resolves the given message code as key in the retrieved bundle files,
	 * returning the value found in the bundle as-is (without MessageFormat parsing).
	 */
	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		if (getCacheMillis() < 0) {
			PropertiesHolder propHolder = getMergedProperties(locale);
			String result = propHolder.getProperty(code);
			if (result != null) {
				return result;
			}
		}
		else {
			for (String basename : getBasenameSet()) {
				List<String> filenames = calculateAllFilenames(basename, locale);
				for (String filename : filenames) {
					PropertiesHolder propHolder = getProperties(filename);
					String result = propHolder.getProperty(code);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Resolves the given message code as key in the retrieved bundle files,
	 * using a cached MessageFormat instance per message code.
	 */
	@Override
	@Nullable
	protected MessageFormat resolveCode(String code, Locale locale) {
		if (getCacheMillis() < 0) {
			PropertiesHolder propHolder = getMergedProperties(locale);
			MessageFormat result = propHolder.getMessageFormat(code, locale);
			if (result != null) {
				return result;
			}
		}
		else {
			for (String basename : getBasenameSet()) {
				List<String> filenames = calculateAllFilenames(basename, locale);
				for (String filename : filenames) {
					PropertiesHolder propHolder = getProperties(filename);
					MessageFormat result = propHolder.getMessageFormat(code, locale);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}


	/**
	 * Get a PropertiesHolder that contains the actually visible properties
	 * for a Locale, after merging all specified resource bundles.
	 * Either fetches the holder from the cache or freshly loads it.
	 * <p>Only used when caching resource bundle contents forever, i.e.
	 * with cacheSeconds < 0. Therefore, merged properties are always
	 * cached forever.
	 */
	protected PropertiesHolder getMergedProperties(Locale locale) {
		PropertiesHolder mergedHolder = this.cachedMergedProperties.get(locale);
		if (mergedHolder != null) {
			return mergedHolder;
		}

		Properties mergedProps = newProperties();
		long latestTimestamp = -1;
		String[] basenames = StringUtils.toStringArray(getBasenameSet());
		for (int i = basenames.length - 1; i >= 0; i--) {
			List<String> filenames = calculateAllFilenames(basenames[i], locale);
			for (int j = filenames.size() - 1; j >= 0; j--) {
				String filename = filenames.get(j);
				PropertiesHolder propHolder = getProperties(filename);
				if (propHolder.getProperties() != null) {
					mergedProps.putAll(propHolder.getProperties());
					if (propHolder.getFileTimestamp() > latestTimestamp) {
						latestTimestamp = propHolder.getFileTimestamp();
					}
				}
			}
		}

		mergedHolder = new PropertiesHolder(mergedProps, latestTimestamp);
		PropertiesHolder existing = this.cachedMergedProperties.putIfAbsent(locale, mergedHolder);
		if (existing != null) {
			mergedHolder = existing;
		}
		return mergedHolder;
	}

	/**
	 * Calculate all filenames for the given bundle basename and Locale.
	 * Will calculate filenames for the given Locale, the system Locale
	 * (if applicable), and the default file.
	 * @param basename the basename of the bundle
	 * @param locale the locale
	 * @return the List of filenames to check
	 * @see #setFallbackToSystemLocale
	 * @see #calculateFilenamesForLocale
	 */
	protected List<String> calculateAllFilenames(String basename, Locale locale) {
		Map<Locale, List<String>> localeMap = this.cachedFilenames.get(basename);
		if (localeMap != null) {
			List<String> filenames = localeMap.get(locale);
			if (filenames != null) {
				return filenames;
			}
		}

		// Filenames for given Locale
		List<String> filenames = new ArrayList<>(7);
		filenames.addAll(calculateFilenamesForLocale(basename, locale));

		// Filenames for default Locale, if any
		if (isFallbackToSystemLocale()) {
			Locale defaultLocale = Locale.getDefault();
			if (!locale.equals(defaultLocale)) {
				List<String> fallbackFilenames = calculateFilenamesForLocale(basename, defaultLocale);
				for (String fallbackFilename : fallbackFilenames) {
					if (!filenames.contains(fallbackFilename)) {
						// Entry for fallback locale that isn't already in filenames list.
						filenames.add(fallbackFilename);
					}
				}
			}
		}

		// Filename for default bundle file
		filenames.add(basename);

		if (localeMap == null) {
			localeMap = new ConcurrentHashMap<>();
			Map<Locale, List<String>> existing = this.cachedFilenames.putIfAbsent(basename, localeMap);
			if (existing != null) {
				localeMap = existing;
			}
		}
		localeMap.put(locale, filenames);
		return filenames;
	}

	/**
	 * Calculate the filenames for the given bundle basename and Locale,
	 * appending language code, country code, and variant code.
	 * E.g.: basename "messages", Locale "de_AT_oo" -> "messages_de_AT_OO",
	 * "messages_de_AT", "messages_de".
	 * <p>Follows the rules defined by {@link java.util.Locale#toString()}.
	 * @param basename the basename of the bundle
	 * @param locale the locale
	 * @return the List of filenames to check
	 */
	protected List<String> calculateFilenamesForLocale(String basename, Locale locale) {
		List<String> result = new ArrayList<>(3);
		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();
		StringBuilder temp = new StringBuilder(basename);

		temp.append('_');
		if (language.length() > 0) {
			temp.append(language);
			result.add(0, temp.toString());
		}

		temp.append('_');
		if (country.length() > 0) {
			temp.append(country);
			result.add(0, temp.toString());
		}

		if (variant.length() > 0 && (language.length() > 0 || country.length() > 0)) {
			temp.append('_').append(variant);
			result.add(0, temp.toString());
		}

		return result;
	}


	/**
	 * Get a PropertiesHolder for the given filename, either from the
	 * cache or freshly loaded.
	 * @param filename the bundle filename (basename + Locale)
	 * @return the current PropertiesHolder for the bundle
	 */
	protected PropertiesHolder getProperties(String filename) {
		PropertiesHolder propHolder = this.cachedProperties.get(filename);
		long originalTimestamp = -2;

		if (propHolder != null) {
			originalTimestamp = propHolder.getRefreshTimestamp();
			if (originalTimestamp == -1 || originalTimestamp > System.currentTimeMillis() - getCacheMillis()) {
				// Up to date
				return propHolder;
			}
		}
		else {
			propHolder = new PropertiesHolder();
			PropertiesHolder existingHolder = this.cachedProperties.putIfAbsent(filename, propHolder);
			if (existingHolder != null) {
				propHolder = existingHolder;
			}
		}

		// At this point, we need to refresh...
		if (this.concurrentRefresh && propHolder.getRefreshTimestamp() >= 0) {
			// A populated but stale holder -> could keep using it.
			if (!propHolder.refreshLock.tryLock()) {
				// Getting refreshed by another thread already ->
				// let's return the existing properties for the time being.
				return propHolder;
			}
		}
		else {
			propHolder.refreshLock.lock();
		}
		try {
			PropertiesHolder existingHolder = this.cachedProperties.get(filename);
			if (existingHolder != null && existingHolder.getRefreshTimestamp() > originalTimestamp) {
				return existingHolder;
			}
			return refreshProperties(filename, propHolder);
		}
		finally {
			propHolder.refreshLock.unlock();
		}
	}

	/**
	 * Refresh the PropertiesHolder for the given bundle filename.
	 * The holder can be {@code null} if not cached before, or a timed-out cache entry
	 * (potentially getting re-validated against the current last-modified timestamp).
	 * @param filename the bundle filename (basename + Locale)
	 * @param propHolder the current PropertiesHolder for the bundle
	 */
	protected PropertiesHolder refreshProperties(String filename, @Nullable PropertiesHolder propHolder) {
		long refreshTimestamp = (getCacheMillis() < 0 ? -1 : System.currentTimeMillis());

		Resource resource = this.resourceLoader.getResource(filename + PROPERTIES_SUFFIX);
		if (!resource.exists()) {
			resource = this.resourceLoader.getResource(filename + XML_SUFFIX);
		}

		if (resource.exists()) {
			long fileTimestamp = -1;
			if (getCacheMillis() >= 0) {
				// Last-modified timestamp of file will just be read if caching with timeout.
				try {
					fileTimestamp = resource.lastModified();
					if (propHolder != null && propHolder.getFileTimestamp() == fileTimestamp) {
						if (logger.isDebugEnabled()) {
							logger.debug("Re-caching properties for filename [" + filename + "] - file hasn't been modified");
						}
						propHolder.setRefreshTimestamp(refreshTimestamp);
						return propHolder;
					}
				}
				catch (IOException ex) {
					// Probably a class path resource: cache it forever.
					if (logger.isDebugEnabled()) {
						logger.debug(resource + " could not be resolved in the file system - assuming that it hasn't changed", ex);
					}
					fileTimestamp = -1;
				}
			}
			try {
				Properties props = loadProperties(resource, filename);
				propHolder = new PropertiesHolder(props, fileTimestamp);
			}
			catch (IOException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Could not parse properties file [" + resource.getFilename() + "]", ex);
				}
				// Empty holder representing "not valid".
				propHolder = new PropertiesHolder();
			}
		}

		else {
			// Resource does not exist.
			if (logger.isDebugEnabled()) {
				logger.debug("No properties file found for [" + filename + "] - neither plain properties nor XML");
			}
			// Empty holder representing "not found".
			propHolder = new PropertiesHolder();
		}

		propHolder.setRefreshTimestamp(refreshTimestamp);
		this.cachedProperties.put(filename, propHolder);
		return propHolder;
	}

	/**
	 * Load the properties from the given resource.
	 * @param resource the resource to load from
	 * @param filename the original bundle filename (basename + Locale)
	 * @return the populated Properties instance
	 * @throws IOException if properties loading failed
	 */
	protected Properties loadProperties(Resource resource, String filename) throws IOException {
		Properties props = newProperties();
		try (InputStream is = resource.getInputStream()) {
			String resourceFilename = resource.getFilename();
			if (resourceFilename != null && resourceFilename.endsWith(XML_SUFFIX)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Loading properties [" + resource.getFilename() + "]");
				}
				this.propertiesPersister.loadFromXml(props, is);
			}
			else {
				String encoding = null;
				if (this.fileEncodings != null) {
					encoding = this.fileEncodings.getProperty(filename);
				}
				if (encoding == null) {
					encoding = getDefaultEncoding();
				}
				if (encoding != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Loading properties [" + resource.getFilename() + "] with encoding '" + encoding + "'");
					}
					this.propertiesPersister.load(props, new InputStreamReader(is, encoding));
				}
				else {
					if (logger.isDebugEnabled()) {
						logger.debug("Loading properties [" + resource.getFilename() + "]");
					}
					this.propertiesPersister.load(props, is);
				}
			}
			return props;
		}
	}

	/**
	 * Template method for creating a plain new {@link Properties} instance.
	 * The default implementation simply calls {@link Properties#Properties()}.
	 * <p>Allows for returning a custom {@link Properties} extension in subclasses.
	 * Overriding methods should just instantiate a custom {@link Properties} subclass,
	 * with no further initialization or population to be performed at that point.
	 * @return a plain Properties instance
	 * @since 4.2
	 */
	protected Properties newProperties() {
		return new Properties();
	}


	/**
	 * Clear the resource bundle cache.
	 * Subsequent resolve calls will lead to reloading of the properties files.
	 */
	public void clearCache() {
		logger.debug("Clearing entire resource bundle cache");
		this.cachedProperties.clear();
		this.cachedMergedProperties.clear();
	}

	/**
	 * Clear the resource bundle caches of this MessageSource and all its ancestors.
	 * @see #clearCache
	 */
	public void clearCacheIncludingAncestors() {
		clearCache();
		if (getParentMessageSource() instanceof ReloadableResourceBundleMessageSource) {
			((ReloadableResourceBundleMessageSource) getParentMessageSource()).clearCacheIncludingAncestors();
		}
	}


	@Override
	public String toString() {
		return getClass().getName() + ": basenames=" + getBasenameSet();
	}


	/**
	 * PropertiesHolder for caching.
	 * Stores the last-modified timestamp of the source file for efficient
	 * change detection, and the timestamp of the last refresh attempt
	 * (updated every time the cache entry gets re-validated).
	 */
	protected class PropertiesHolder {

		@Nullable
		private final Properties properties;

		private final long fileTimestamp;

		private volatile long refreshTimestamp = -2;

		private final ReentrantLock refreshLock = new ReentrantLock();

		/** Cache to hold already generated MessageFormats per message code. */
		private final ConcurrentMap<String, Map<Locale, MessageFormat>> cachedMessageFormats =
				new ConcurrentHashMap<>();

		public PropertiesHolder() {
			this.properties = null;
			this.fileTimestamp = -1;
		}

		public PropertiesHolder(Properties properties, long fileTimestamp) {
			this.properties = properties;
			this.fileTimestamp = fileTimestamp;
		}

		@Nullable
		public Properties getProperties() {
			return this.properties;
		}

		public long getFileTimestamp() {
			return this.fileTimestamp;
		}

		public void setRefreshTimestamp(long refreshTimestamp) {
			this.refreshTimestamp = refreshTimestamp;
		}

		public long getRefreshTimestamp() {
			return this.refreshTimestamp;
		}

		@Nullable
		public String getProperty(String code) {
			if (this.properties == null) {
				return null;
			}
			return this.properties.getProperty(code);
		}

		@Nullable
		public MessageFormat getMessageFormat(String code, Locale locale) {
			if (this.properties == null) {
				return null;
			}
			Map<Locale, MessageFormat> localeMap = this.cachedMessageFormats.get(code);
			if (localeMap != null) {
				MessageFormat result = localeMap.get(locale);
				if (result != null) {
					return result;
				}
			}
			String msg = this.properties.getProperty(code);
			if (msg != null) {
				if (localeMap == null) {
					localeMap = new ConcurrentHashMap<>();
					Map<Locale, MessageFormat> existing = this.cachedMessageFormats.putIfAbsent(code, localeMap);
					if (existing != null) {
						localeMap = existing;
					}
				}
				MessageFormat result = createMessageFormat(msg, locale);
				localeMap.put(locale, result);
				return result;
			}
			return null;
		}
	}

}
