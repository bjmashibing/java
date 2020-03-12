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

package org.springframework.test.web.servlet.request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.springframework.beans.Mergeable;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.UrlPathHelper;

/**
 * Default builder for {@link MockHttpServletRequest} required as input to
 * perform requests in {@link MockMvc}.
 *
 * <p>Application tests will typically access this builder through the static
 * factory methods in {@link MockMvcRequestBuilders}.
 *
 * <p>This class is not open for extension. To apply custom initialization to
 * the created {@code MockHttpServletRequest}, please use the
 * {@link #with(RequestPostProcessor)} extension point.
 *
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @author Arjen Poutsma
 * @author Sam Brannen
 * @author Kamill Sokol
 * @since 3.2
 */
public class MockHttpServletRequestBuilder
		implements ConfigurableSmartRequestBuilder<MockHttpServletRequestBuilder>, Mergeable {

	private static final UrlPathHelper urlPathHelper = new UrlPathHelper();


	private final String method;

	private final URI url;

	private String contextPath = "";

	private String servletPath = "";

	@Nullable
	private String pathInfo = "";

	@Nullable
	private Boolean secure;

	@Nullable
	private Principal principal;

	@Nullable
	private MockHttpSession session;

	@Nullable
	private String characterEncoding;

	@Nullable
	private byte[] content;

	@Nullable
	private String contentType;

	private final MultiValueMap<String, Object> headers = new LinkedMultiValueMap<>();

	private final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

	private final List<Cookie> cookies = new ArrayList<>();

	private final List<Locale> locales = new ArrayList<>();

	private final Map<String, Object> requestAttributes = new LinkedHashMap<>();

	private final Map<String, Object> sessionAttributes = new LinkedHashMap<>();

	private final Map<String, Object> flashAttributes = new LinkedHashMap<>();

	private final List<RequestPostProcessor> postProcessors = new ArrayList<>();


	/**
	 * Package private constructor. To get an instance, use static factory
	 * methods in {@link MockMvcRequestBuilders}.
	 * <p>Although this class cannot be extended, additional ways to initialize
	 * the {@code MockHttpServletRequest} can be plugged in via
	 * {@link #with(RequestPostProcessor)}.
	 * @param httpMethod the HTTP method (GET, POST, etc)
	 * @param url a URL template; the resulting URL will be encoded
	 * @param vars zero or more URI variables
	 */
	MockHttpServletRequestBuilder(HttpMethod httpMethod, String url, Object... vars) {
		this(httpMethod.name(), UriComponentsBuilder.fromUriString(url).buildAndExpand(vars).encode().toUri());
	}

	/**
	 * Alternative to {@link #MockHttpServletRequestBuilder(HttpMethod, String, Object...)}
	 * with a pre-built URI.
	 * @param httpMethod the HTTP method (GET, POST, etc)
	 * @param url the URL
	 * @since 4.0.3
	 */
	MockHttpServletRequestBuilder(HttpMethod httpMethod, URI url) {
		this(httpMethod.name(), url);
	}

	/**
	 * Alternative constructor for custom HTTP methods.
	 * @param httpMethod the HTTP method (GET, POST, etc)
	 * @param url the URL
	 * @since 4.3
	 */
	MockHttpServletRequestBuilder(String httpMethod, URI url) {
		Assert.notNull(httpMethod, "'httpMethod' is required");
		Assert.notNull(url, "'url' is required");
		this.method = httpMethod;
		this.url = url;
	}


	/**
	 * Specify the portion of the requestURI that represents the context path.
	 * The context path, if specified, must match to the start of the request URI.
	 * <p>In most cases, tests can be written by omitting the context path from
	 * the requestURI. This is because most applications don't actually depend
	 * on the name under which they're deployed. If specified here, the context
	 * path must start with a "/" and must not end with a "/".
	 * @see javax.servlet.http.HttpServletRequest#getContextPath()
	 */
	public MockHttpServletRequestBuilder contextPath(String contextPath) {
		if (StringUtils.hasText(contextPath)) {
			Assert.isTrue(contextPath.startsWith("/"), "Context path must start with a '/'");
			Assert.isTrue(!contextPath.endsWith("/"), "Context path must not end with a '/'");
		}
		this.contextPath = contextPath;
		return this;
	}

	/**
	 * Specify the portion of the requestURI that represents the path to which
	 * the Servlet is mapped. This is typically a portion of the requestURI
	 * after the context path.
	 * <p>In most cases, tests can be written by omitting the servlet path from
	 * the requestURI. This is because most applications don't actually depend
	 * on the prefix to which a servlet is mapped. For example if a Servlet is
	 * mapped to {@code "/main/*"}, tests can be written with the requestURI
	 * {@code "/accounts/1"} as opposed to {@code "/main/accounts/1"}.
	 * If specified here, the servletPath must start with a "/" and must not
	 * end with a "/".
	 * @see javax.servlet.http.HttpServletRequest#getServletPath()
	 */
	public MockHttpServletRequestBuilder servletPath(String servletPath) {
		if (StringUtils.hasText(servletPath)) {
			Assert.isTrue(servletPath.startsWith("/"), "Servlet path must start with a '/'");
			Assert.isTrue(!servletPath.endsWith("/"), "Servlet path must not end with a '/'");
		}
		this.servletPath = servletPath;
		return this;
	}

	/**
	 * Specify the portion of the requestURI that represents the pathInfo.
	 * <p>If left unspecified (recommended), the pathInfo will be automatically derived
	 * by removing the contextPath and the servletPath from the requestURI and using any
	 * remaining part. If specified here, the pathInfo must start with a "/".
	 * <p>If specified, the pathInfo will be used as-is.
	 * @see javax.servlet.http.HttpServletRequest#getPathInfo()
	 */
	public MockHttpServletRequestBuilder pathInfo(@Nullable String pathInfo) {
		if (StringUtils.hasText(pathInfo)) {
			Assert.isTrue(pathInfo.startsWith("/"), "Path info must start with a '/'");
		}
		this.pathInfo = pathInfo;
		return this;
	}

	/**
	 * Set the secure property of the {@link ServletRequest} indicating use of a
	 * secure channel, such as HTTPS.
	 * @param secure whether the request is using a secure channel
	 */
	public MockHttpServletRequestBuilder secure(boolean secure){
		this.secure = secure;
		return this;
	}

	/**
	 * Set the character encoding of the request.
	 * @param encoding the character encoding
	 */
	public MockHttpServletRequestBuilder characterEncoding(String encoding) {
		this.characterEncoding = encoding;
		return this;
	}

	/**
	 * Set the request body.
	 * @param content the body content
	 */
	public MockHttpServletRequestBuilder content(byte[] content) {
		this.content = content;
		return this;
	}

	/**
	 * Set the request body as a UTF-8 String.
	 * @param content the body content
	 */
	public MockHttpServletRequestBuilder content(String content) {
		this.content = content.getBytes(StandardCharsets.UTF_8);
		return this;
	}

	/**
	 * Set the 'Content-Type' header of the request.
	 * @param contentType the content type
	 */
	public MockHttpServletRequestBuilder contentType(MediaType contentType) {
		Assert.notNull(contentType, "'contentType' must not be null");
		this.contentType = contentType.toString();
		return this;
	}

	/**
	 * Set the 'Content-Type' header of the request.
	 * @param contentType the content type
	 * @since 4.1.2
	 */
	public MockHttpServletRequestBuilder contentType(String contentType) {
		this.contentType = MediaType.parseMediaType(contentType).toString();
		return this;
	}

	/**
	 * Set the 'Accept' header to the given media type(s).
	 * @param mediaTypes one or more media types
	 */
	public MockHttpServletRequestBuilder accept(MediaType... mediaTypes) {
		Assert.notEmpty(mediaTypes, "'mediaTypes' must not be empty");
		this.headers.set("Accept", MediaType.toString(Arrays.asList(mediaTypes)));
		return this;
	}

	/**
	 * Set the 'Accept' header to the given media type(s).
	 * @param mediaTypes one or more media types
	 */
	public MockHttpServletRequestBuilder accept(String... mediaTypes) {
		Assert.notEmpty(mediaTypes, "'mediaTypes' must not be empty");
		List<MediaType> result = new ArrayList<>(mediaTypes.length);
		for (String mediaType : mediaTypes) {
			result.add(MediaType.parseMediaType(mediaType));
		}
		this.headers.set("Accept", MediaType.toString(result));
		return this;
	}

	/**
	 * Add a header to the request. Values are always added.
	 * @param name the header name
	 * @param values one or more header values
	 */
	public MockHttpServletRequestBuilder header(String name, Object... values) {
		addToMultiValueMap(this.headers, name, values);
		return this;
	}

	/**
	 * Add all headers to the request. Values are always added.
	 * @param httpHeaders the headers and values to add
	 */
	public MockHttpServletRequestBuilder headers(HttpHeaders httpHeaders) {
		httpHeaders.forEach(this.headers::addAll);
		return this;
	}

	/**
	 * Add a request parameter to the {@link MockHttpServletRequest}.
	 * <p>If called more than once, new values get added to existing ones.
	 * @param name the parameter name
	 * @param values one or more values
	 */
	public MockHttpServletRequestBuilder param(String name, String... values) {
		addToMultiValueMap(this.parameters, name, values);
		return this;
	}

	/**
	 * Add a map of request parameters to the {@link MockHttpServletRequest},
	 * for example when testing a form submission.
	 * <p>If called more than once, new values get added to existing ones.
	 * @param params the parameters to add
	 * @since 4.2.4
	 */
	public MockHttpServletRequestBuilder params(MultiValueMap<String, String> params) {
		params.forEach((name, values) -> {
			for (String value : values) {
				this.parameters.add(name, value);
			}
		});
		return this;
	}

	/**
	 * Add the given cookies to the request. Cookies are always added.
	 * @param cookies the cookies to add
	 */
	public MockHttpServletRequestBuilder cookie(Cookie... cookies) {
		Assert.notEmpty(cookies, "'cookies' must not be empty");
		this.cookies.addAll(Arrays.asList(cookies));
		return this;
	}

	/**
	 * Add the specified locales as preferred request locales.
	 * @param locales the locales to add
	 * @since 4.3.6
	 * @see #locale(Locale)
	 */
	public MockHttpServletRequestBuilder locale(Locale... locales) {
		Assert.notEmpty(locales, "'locales' must not be empty");
		this.locales.addAll(Arrays.asList(locales));
		return this;
	}

	/**
	 * Set the locale of the request, overriding any previous locales.
	 * @param locale the locale, or {@code null} to reset it
	 * @see #locale(Locale...)
	 */
	public MockHttpServletRequestBuilder locale(@Nullable Locale locale) {
		this.locales.clear();
		if (locale != null) {
			this.locales.add(locale);
		}
		return this;
	}

	/**
	 * Set a request attribute.
	 * @param name the attribute name
	 * @param value the attribute value
	 */
	public MockHttpServletRequestBuilder requestAttr(String name, Object value) {
		addToMap(this.requestAttributes, name, value);
		return this;
	}

	/**
	 * Set a session attribute.
	 * @param name the session attribute name
	 * @param value the session attribute value
	 */
	public MockHttpServletRequestBuilder sessionAttr(String name, Object value) {
		addToMap(this.sessionAttributes, name, value);
		return this;
	}

	/**
	 * Set session attributes.
	 * @param sessionAttributes the session attributes
	 */
	public MockHttpServletRequestBuilder sessionAttrs(Map<String, Object> sessionAttributes) {
		Assert.notEmpty(sessionAttributes, "'sessionAttributes' must not be empty");
		sessionAttributes.forEach(this::sessionAttr);
		return this;
	}

	/**
	 * Set an "input" flash attribute.
	 * @param name the flash attribute name
	 * @param value the flash attribute value
	 */
	public MockHttpServletRequestBuilder flashAttr(String name, Object value) {
		addToMap(this.flashAttributes, name, value);
		return this;
	}

	/**
	 * Set flash attributes.
	 * @param flashAttributes the flash attributes
	 */
	public MockHttpServletRequestBuilder flashAttrs(Map<String, Object> flashAttributes) {
		Assert.notEmpty(flashAttributes, "'flashAttributes' must not be empty");
		flashAttributes.forEach(this::flashAttr);
		return this;
	}

	/**
	 * Set the HTTP session to use, possibly re-used across requests.
	 * <p>Individual attributes provided via {@link #sessionAttr(String, Object)}
	 * override the content of the session provided here.
	 * @param session the HTTP session
	 */
	public MockHttpServletRequestBuilder session(MockHttpSession session) {
		Assert.notNull(session, "'session' must not be null");
		this.session = session;
		return this;
	}

	/**
	 * Set the principal of the request.
	 * @param principal the principal
	 */
	public MockHttpServletRequestBuilder principal(Principal principal) {
		Assert.notNull(principal, "'principal' must not be null");
		this.principal = principal;
		return this;
	}

	/**
	 * An extension point for further initialization of {@link MockHttpServletRequest}
	 * in ways not built directly into the {@code MockHttpServletRequestBuilder}.
	 * Implementation of this interface can have builder-style methods themselves
	 * and be made accessible through static factory methods.
	 * @param postProcessor a post-processor to add
	 */
	@Override
	public MockHttpServletRequestBuilder with(RequestPostProcessor postProcessor) {
		Assert.notNull(postProcessor, "postProcessor is required");
		this.postProcessors.add(postProcessor);
		return this;
	}


	/**
	 * {@inheritDoc}
	 * @return always returns {@code true}.
	 */
	@Override
	public boolean isMergeEnabled() {
		return true;
	}

	/**
	 * Merges the properties of the "parent" RequestBuilder accepting values
	 * only if not already set in "this" instance.
	 * @param parent the parent {@code RequestBuilder} to inherit properties from
	 * @return the result of the merge
	 */
	@Override
	public Object merge(@Nullable Object parent) {
		if (parent == null) {
			return this;
		}
		if (!(parent instanceof MockHttpServletRequestBuilder)) {
			throw new IllegalArgumentException("Cannot merge with [" + parent.getClass().getName() + "]");
		}
		MockHttpServletRequestBuilder parentBuilder = (MockHttpServletRequestBuilder) parent;

		if (!StringUtils.hasText(this.contextPath)) {
			this.contextPath = parentBuilder.contextPath;
		}
		if (!StringUtils.hasText(this.servletPath)) {
			this.servletPath = parentBuilder.servletPath;
		}
		if ("".equals(this.pathInfo)) {
			this.pathInfo = parentBuilder.pathInfo;
		}

		if (this.secure == null) {
			this.secure = parentBuilder.secure;
		}
		if (this.principal == null) {
			this.principal = parentBuilder.principal;
		}
		if (this.session == null) {
			this.session = parentBuilder.session;
		}

		if (this.characterEncoding == null) {
			this.characterEncoding = parentBuilder.characterEncoding;
		}
		if (this.content == null) {
			this.content = parentBuilder.content;
		}
		if (this.contentType == null) {
			this.contentType = parentBuilder.contentType;
		}

		for (String headerName : parentBuilder.headers.keySet()) {
			if (!this.headers.containsKey(headerName)) {
				this.headers.put(headerName, parentBuilder.headers.get(headerName));
			}
		}
		for (String paramName : parentBuilder.parameters.keySet()) {
			if (!this.parameters.containsKey(paramName)) {
				this.parameters.put(paramName, parentBuilder.parameters.get(paramName));
			}
		}
		for (Cookie cookie : parentBuilder.cookies) {
			if (!containsCookie(cookie)) {
				this.cookies.add(cookie);
			}
		}
		for (Locale locale : parentBuilder.locales) {
			if (!this.locales.contains(locale)) {
				this.locales.add(locale);
			}
		}

		for (String attributeName : parentBuilder.requestAttributes.keySet()) {
			if (!this.requestAttributes.containsKey(attributeName)) {
				this.requestAttributes.put(attributeName, parentBuilder.requestAttributes.get(attributeName));
			}
		}
		for (String attributeName : parentBuilder.sessionAttributes.keySet()) {
			if (!this.sessionAttributes.containsKey(attributeName)) {
				this.sessionAttributes.put(attributeName, parentBuilder.sessionAttributes.get(attributeName));
			}
		}
		for (String attributeName : parentBuilder.flashAttributes.keySet()) {
			if (!this.flashAttributes.containsKey(attributeName)) {
				this.flashAttributes.put(attributeName, parentBuilder.flashAttributes.get(attributeName));
			}
		}

		this.postProcessors.addAll(0, parentBuilder.postProcessors);

		return this;
	}

	private boolean containsCookie(Cookie cookie) {
		for (Cookie cookieToCheck : this.cookies) {
			if (ObjectUtils.nullSafeEquals(cookieToCheck.getName(), cookie.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Build a {@link MockHttpServletRequest}.
	 */
	@Override
	public final MockHttpServletRequest buildRequest(ServletContext servletContext) {
		MockHttpServletRequest request = createServletRequest(servletContext);

		request.setAsyncSupported(true);
		request.setMethod(this.method);

		String requestUri = this.url.getRawPath();
		request.setRequestURI(requestUri);

		if (this.url.getScheme() != null) {
			request.setScheme(this.url.getScheme());
		}
		if (this.url.getHost() != null) {
			request.setServerName(this.url.getHost());
		}
		if (this.url.getPort() != -1) {
			request.setServerPort(this.url.getPort());
		}

		updatePathRequestProperties(request, requestUri);

		if (this.secure != null) {
			request.setSecure(this.secure);
		}
		if (this.principal != null) {
			request.setUserPrincipal(this.principal);
		}
		if (this.session != null) {
			request.setSession(this.session);
		}

		request.setCharacterEncoding(this.characterEncoding);
		request.setContent(this.content);
		request.setContentType(this.contentType);

		this.headers.forEach((name, values) -> {
			for (Object value : values) {
				request.addHeader(name, value);
			}
		});

		if (this.url.getRawQuery() != null) {
			request.setQueryString(this.url.getRawQuery());
		}
		addRequestParams(request, UriComponentsBuilder.fromUri(this.url).build().getQueryParams());

		this.parameters.forEach((name, values) -> {
			for (String value : values) {
				request.addParameter(name, value);
			}
		});

		if (this.content != null && this.content.length > 0) {
			String requestContentType = request.getContentType();
			if (requestContentType != null) {
				MediaType mediaType = MediaType.parseMediaType(requestContentType);
				if (MediaType.APPLICATION_FORM_URLENCODED.includes(mediaType)) {
					addRequestParams(request, parseFormData(mediaType));
				}
			}
		}

		if (!ObjectUtils.isEmpty(this.cookies)) {
			request.setCookies(this.cookies.toArray(new Cookie[0]));
		}
		if (!ObjectUtils.isEmpty(this.locales)) {
			request.setPreferredLocales(this.locales);
		}

		this.requestAttributes.forEach(request::setAttribute);
		this.sessionAttributes.forEach((name, attribute) -> {
			HttpSession session = request.getSession();
			Assert.state(session != null, "No HttpSession");
			session.setAttribute(name, attribute);
		});

		FlashMap flashMap = new FlashMap();
		flashMap.putAll(this.flashAttributes);
		FlashMapManager flashMapManager = getFlashMapManager(request);
		flashMapManager.saveOutputFlashMap(flashMap, request, new MockHttpServletResponse());

		return request;
	}

	/**
	 * Create a new {@link MockHttpServletRequest} based on the supplied
	 * {@code ServletContext}.
	 * <p>Can be overridden in subclasses.
	 */
	protected MockHttpServletRequest createServletRequest(ServletContext servletContext) {
		return new MockHttpServletRequest(servletContext);
	}

	/**
	 * Update the contextPath, servletPath, and pathInfo of the request.
	 */
	private void updatePathRequestProperties(MockHttpServletRequest request, String requestUri) {
		if (!requestUri.startsWith(this.contextPath)) {
			throw new IllegalArgumentException(
					"Request URI [" + requestUri + "] does not start with context path [" + this.contextPath + "]");
		}
		request.setContextPath(this.contextPath);
		request.setServletPath(this.servletPath);

		if ("".equals(this.pathInfo)) {
			if (!requestUri.startsWith(this.contextPath + this.servletPath)) {
				throw new IllegalArgumentException(
						"Invalid servlet path [" + this.servletPath + "] for request URI [" + requestUri + "]");
			}
			String extraPath = requestUri.substring(this.contextPath.length() + this.servletPath.length());
			this.pathInfo = (StringUtils.hasText(extraPath) ?
					urlPathHelper.decodeRequestString(request, extraPath) : null);
		}
		request.setPathInfo(this.pathInfo);
	}

	private void addRequestParams(MockHttpServletRequest request, MultiValueMap<String, String> map) {
		map.forEach((key, values) -> values.forEach(value -> {
			value = (value != null ? UriUtils.decode(value, StandardCharsets.UTF_8) : null);
			request.addParameter(UriUtils.decode(key, StandardCharsets.UTF_8), value);
		}));
	}

	private MultiValueMap<String, String> parseFormData(final MediaType mediaType) {
		HttpInputMessage message = new HttpInputMessage() {
			@Override
			public InputStream getBody() {
				return (content != null ? new ByteArrayInputStream(content) : StreamUtils.emptyInput());
			}
			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(mediaType);
				return headers;
			}
		};

		try {
			return new FormHttpMessageConverter().read(null, message);
		}
		catch (IOException ex) {
			throw new IllegalStateException("Failed to parse form data in request body", ex);
		}
	}

	private FlashMapManager getFlashMapManager(MockHttpServletRequest request) {
		FlashMapManager flashMapManager = null;
		try {
			ServletContext servletContext = request.getServletContext();
			WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
			flashMapManager = wac.getBean(DispatcherServlet.FLASH_MAP_MANAGER_BEAN_NAME, FlashMapManager.class);
		}
		catch (IllegalStateException | NoSuchBeanDefinitionException ex) {
			// ignore
		}
		return (flashMapManager != null ? flashMapManager : new SessionFlashMapManager());
	}

	@Override
	public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
		for (RequestPostProcessor postProcessor : this.postProcessors) {
			request = postProcessor.postProcessRequest(request);
		}
		return request;
	}


	private static void addToMap(Map<String, Object> map, String name, Object value) {
		Assert.hasLength(name, "'name' must not be empty");
		Assert.notNull(value, "'value' must not be null");
		map.put(name, value);
	}

	private static <T> void addToMultiValueMap(MultiValueMap<String, T> map, String name, T[] values) {
		Assert.hasLength(name, "'name' must not be empty");
		Assert.notEmpty(values, "'values' must not be empty");
		for (T value : values) {
			map.add(name, value);
		}
	}

}
