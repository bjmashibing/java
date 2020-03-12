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

package org.springframework.web.servlet.resource;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.resource.EncodedResourceResolver.EncodedResource;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link CssLinkResourceTransformer}.
 *
 * @author Rossen Stoyanchev
 * @author Brian Clozel
 * @author Sam Brannen
 * @since 4.1
 */
public class CssLinkResourceTransformerTests {

	private ResourceTransformerChain transformerChain;

	private MockHttpServletRequest request;


	@Before
	public void setUp() {
		VersionResourceResolver versionResolver = new VersionResourceResolver();
		versionResolver.setStrategyMap(Collections.singletonMap("/**", new ContentVersionStrategy()));
		PathResourceResolver pathResolver = new PathResourceResolver();
		pathResolver.setAllowedLocations(new ClassPathResource("test/", getClass()));
		List<ResourceResolver> resolvers = new ArrayList<>();
		resolvers.add(versionResolver);
		resolvers.add(new PathResourceResolver());
		ResourceUrlProvider resourceUrlProvider = createUrlProvider(resolvers);

		CssLinkResourceTransformer cssLinkTransformer = new CssLinkResourceTransformer();
		cssLinkTransformer.setResourceUrlProvider(resourceUrlProvider);

		this.transformerChain = new DefaultResourceTransformerChain(
				new DefaultResourceResolverChain(resolvers), Collections.singletonList(cssLinkTransformer));
	}

	private ResourceUrlProvider createUrlProvider(List<ResourceResolver> resolvers) {
		ResourceHttpRequestHandler resourceHandler = new ResourceHttpRequestHandler();
		resourceHandler.setResourceResolvers(resolvers);
		resourceHandler.setLocations(Collections.singletonList(new ClassPathResource("test/", getClass())));

		ResourceUrlProvider resourceUrlProvider = new ResourceUrlProvider();
		resourceUrlProvider.setHandlerMap(Collections.singletonMap("/static/**", resourceHandler));
		return resourceUrlProvider;
	}


	@Test
	public void transform() throws Exception {
		this.request = new MockHttpServletRequest("GET", "/static/main.css");
		Resource css = getResource("main.css");
		String expected = "\n" +
				"@import url(\"/static/bar-11e16cf79faee7ac698c805cf28248d2.css?#iefix\");\n" +
				"@import url('/static/bar-11e16cf79faee7ac698c805cf28248d2.css#bla-normal');\n" +
				"@import url(/static/bar-11e16cf79faee7ac698c805cf28248d2.css);\n\n" +
				"@import \"/static/foo-e36d2e05253c6c7085a91522ce43a0b4.css\";\n" +
				"@import '/static/foo-e36d2e05253c6c7085a91522ce43a0b4.css';\n\n" +
				"body { background: url(\"/static/images/image-f448cd1d5dba82b774f3202c878230b3.png?#iefix\") }\n";

		TransformedResource actual = (TransformedResource) this.transformerChain.transform(this.request, css);
		String result = new String(actual.getByteArray(), StandardCharsets.UTF_8);
		result = StringUtils.deleteAny(result, "\r");
		assertEquals(expected, result);
	}

	@Test
	public void transformNoLinks() throws Exception {
		this.request = new MockHttpServletRequest("GET", "/static/foo.css");
		Resource expected = getResource("foo.css");
		Resource actual = this.transformerChain.transform(this.request, expected);
		assertSame(expected, actual);
	}

	@Test
	public void transformExtLinksNotAllowed() throws Exception {
		this.request = new MockHttpServletRequest("GET", "/static/external.css");

		List<ResourceTransformer> transformers = Collections.singletonList(new CssLinkResourceTransformer());
		ResourceResolverChain mockChain = Mockito.mock(DefaultResourceResolverChain.class);
		ResourceTransformerChain chain = new DefaultResourceTransformerChain(mockChain, transformers);

		Resource resource = getResource("external.css");
		String expected = "@import url(\"https://example.org/fonts/css\");\n" +
				"body { background: url(\"file:///home/spring/image.png\") }\n" +
				"figure { background: url(\"//example.org/style.css\")}";

		TransformedResource transformedResource = (TransformedResource) chain.transform(this.request, resource);
		String result = new String(transformedResource.getByteArray(), StandardCharsets.UTF_8);
		result = StringUtils.deleteAny(result, "\r");
		assertEquals(expected, result);

		List<Resource> locations = Collections.singletonList(resource);
		Mockito.verify(mockChain, Mockito.never()).resolveUrlPath("https://example.org/fonts/css", locations);
		Mockito.verify(mockChain, Mockito.never()).resolveUrlPath("file:///home/spring/image.png", locations);
		Mockito.verify(mockChain, Mockito.never()).resolveUrlPath("//example.org/style.css", locations);
	}

	@Test
	public void transformSkippedForNonCssResource() throws Exception {
		this.request = new MockHttpServletRequest("GET", "/static/images/image.png");
		Resource expected = getResource("images/image.png");
		Resource actual = this.transformerChain.transform(this.request, expected);

		assertSame(expected, actual);
	}

	@Test
	public void transformSkippedForGzippedResource() throws Exception {

		EncodedResourceResolverTests.createGzippedFile("main.css");

		this.request = new MockHttpServletRequest("GET", "/static/main.css");
		Resource original = new ClassPathResource("test/main.css", getClass());
		EncodedResource gzipped = new EncodedResource(original, "gzip", ".gz");
		Resource actual = this.transformerChain.transform(this.request, gzipped);

		assertSame(gzipped, actual);
	}

	@Test // https://github.com/spring-projects/spring-framework/issues/22602
	public void transformEmptyUrlFunction() throws Exception {
		this.request = new MockHttpServletRequest("GET", "/static/empty_url_function.css");
		Resource css = getResource("empty_url_function.css");
		String expected =
				".fooStyle {\n" +
				"\tbackground: transparent url() no-repeat left top;\n" +
				"}";

		TransformedResource actual = (TransformedResource) this.transformerChain.transform(this.request, css);
		String result = new String(actual.getByteArray(), StandardCharsets.UTF_8);
		result = StringUtils.deleteAny(result, "\r");
		assertEquals(expected, result);
	}

	private Resource getResource(String filePath) {
		return new ClassPathResource("test/" + filePath, getClass());
	}

}
