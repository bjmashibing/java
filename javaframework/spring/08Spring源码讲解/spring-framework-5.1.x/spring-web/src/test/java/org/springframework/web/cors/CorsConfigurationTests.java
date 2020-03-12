/*
 * Copyright 2002-2016 the original author or authors.
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

package org.springframework.web.cors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import org.springframework.http.HttpMethod;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link CorsConfiguration}.
 *
 * @author Sebastien Deleuze
 * @author Sam Brannen
 */
public class CorsConfigurationTests {

	@Test
	public void setNullValues() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(null);
		assertNull(config.getAllowedOrigins());
		config.setAllowedHeaders(null);
		assertNull(config.getAllowedHeaders());
		config.setAllowedMethods(null);
		assertNull(config.getAllowedMethods());
		config.setExposedHeaders(null);
		assertNull(config.getExposedHeaders());
		config.setAllowCredentials(null);
		assertNull(config.getAllowCredentials());
		config.setMaxAge(null);
		assertNull(config.getMaxAge());
	}

	@Test
	public void setValues() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("*");
		assertEquals(Arrays.asList("*"), config.getAllowedOrigins());
		config.addAllowedHeader("*");
		assertEquals(Arrays.asList("*"), config.getAllowedHeaders());
		config.addAllowedMethod("*");
		assertEquals(Arrays.asList("*"), config.getAllowedMethods());
		config.addExposedHeader("header1");
		config.addExposedHeader("header2");
		assertEquals(Arrays.asList("header1", "header2"), config.getExposedHeaders());
		config.setAllowCredentials(true);
		assertTrue(config.getAllowCredentials());
		config.setMaxAge(123L);
		assertEquals(new Long(123), config.getMaxAge());
	}

	@Test(expected = IllegalArgumentException.class)
	public void asteriskWildCardOnAddExposedHeader() {
		CorsConfiguration config = new CorsConfiguration();
		config.addExposedHeader("*");
	}

	@Test(expected = IllegalArgumentException.class)
	public void asteriskWildCardOnSetExposedHeaders() {
		CorsConfiguration config = new CorsConfiguration();
		config.setExposedHeaders(Arrays.asList("*"));
	}

	@Test
	public void combineWithNull() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("*"));
		config.combine(null);
		assertEquals(Arrays.asList("*"), config.getAllowedOrigins());
	}

	@Test
	public void combineWithNullProperties() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("*");
		config.addAllowedHeader("header1");
		config.addExposedHeader("header3");
		config.addAllowedMethod(HttpMethod.GET.name());
		config.setMaxAge(123L);
		config.setAllowCredentials(true);
		CorsConfiguration other = new CorsConfiguration();
		config = config.combine(other);
		assertEquals(Arrays.asList("*"), config.getAllowedOrigins());
		assertEquals(Arrays.asList("header1"), config.getAllowedHeaders());
		assertEquals(Arrays.asList("header3"), config.getExposedHeaders());
		assertEquals(Arrays.asList(HttpMethod.GET.name()), config.getAllowedMethods());
		assertEquals(new Long(123), config.getMaxAge());
		assertTrue(config.getAllowCredentials());
	}

	@Test  // SPR-15772
	public void combineWithDefaultPermitValues() {
		CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
		CorsConfiguration other = new CorsConfiguration();
		other.addAllowedOrigin("https://domain.com");
		other.addAllowedHeader("header1");
		other.addAllowedMethod(HttpMethod.PUT.name());

		CorsConfiguration combinedConfig = config.combine(other);
		assertEquals(Arrays.asList("https://domain.com"), combinedConfig.getAllowedOrigins());
		assertEquals(Arrays.asList("header1"), combinedConfig.getAllowedHeaders());
		assertEquals(Arrays.asList(HttpMethod.PUT.name()), combinedConfig.getAllowedMethods());

		combinedConfig = other.combine(config);
		assertEquals(Arrays.asList("https://domain.com"), combinedConfig.getAllowedOrigins());
		assertEquals(Arrays.asList("header1"), combinedConfig.getAllowedHeaders());
		assertEquals(Arrays.asList(HttpMethod.PUT.name()), combinedConfig.getAllowedMethods());

		combinedConfig = config.combine(new CorsConfiguration());
		assertEquals(Arrays.asList("*"), config.getAllowedOrigins());
		assertEquals(Arrays.asList("*"), config.getAllowedHeaders());
		assertEquals(Arrays.asList(HttpMethod.GET.name(), HttpMethod.HEAD.name(),
				HttpMethod.POST.name()), combinedConfig.getAllowedMethods());

		combinedConfig = new CorsConfiguration().combine(config);
		assertEquals(Arrays.asList("*"), config.getAllowedOrigins());
		assertEquals(Arrays.asList("*"), config.getAllowedHeaders());
		assertEquals(Arrays.asList(HttpMethod.GET.name(), HttpMethod.HEAD.name(),
				HttpMethod.POST.name()), combinedConfig.getAllowedMethods());
	}

	@Test
	public void combineWithAsteriskWildCard() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		CorsConfiguration other = new CorsConfiguration();
		other.addAllowedOrigin("https://domain.com");
		other.addAllowedHeader("header1");
		other.addExposedHeader("header2");
		other.addAllowedMethod(HttpMethod.PUT.name());
		CorsConfiguration combinedConfig = config.combine(other);
		assertEquals(Arrays.asList("*"), combinedConfig.getAllowedOrigins());
		assertEquals(Arrays.asList("*"), combinedConfig.getAllowedHeaders());
		assertEquals(Arrays.asList("*"), combinedConfig.getAllowedMethods());
		combinedConfig = other.combine(config);
		assertEquals(Arrays.asList("*"), combinedConfig.getAllowedOrigins());
		assertEquals(Arrays.asList("*"), combinedConfig.getAllowedHeaders());
		assertEquals(Arrays.asList("*"), combinedConfig.getAllowedMethods());
	}

	@Test  // SPR-14792
	public void combineWithDuplicatedElements() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("https://domain1.com");
		config.addAllowedOrigin("https://domain2.com");
		config.addAllowedHeader("header1");
		config.addAllowedHeader("header2");
		config.addExposedHeader("header3");
		config.addExposedHeader("header4");
		config.addAllowedMethod(HttpMethod.GET.name());
		config.addAllowedMethod(HttpMethod.PUT.name());
		CorsConfiguration other = new CorsConfiguration();
		other.addAllowedOrigin("https://domain1.com");
		other.addAllowedHeader("header1");
		other.addExposedHeader("header3");
		other.addAllowedMethod(HttpMethod.GET.name());
		CorsConfiguration combinedConfig = config.combine(other);
		assertEquals(Arrays.asList("https://domain1.com", "https://domain2.com"), combinedConfig.getAllowedOrigins());
		assertEquals(Arrays.asList("header1", "header2"), combinedConfig.getAllowedHeaders());
		assertEquals(Arrays.asList("header3", "header4"), combinedConfig.getExposedHeaders());
		assertEquals(Arrays.asList(HttpMethod.GET.name(), HttpMethod.PUT.name()), combinedConfig.getAllowedMethods());
	}

	@Test
	public void combine() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("https://domain1.com");
		config.addAllowedHeader("header1");
		config.addExposedHeader("header3");
		config.addAllowedMethod(HttpMethod.GET.name());
		config.setMaxAge(123L);
		config.setAllowCredentials(true);
		CorsConfiguration other = new CorsConfiguration();
		other.addAllowedOrigin("https://domain2.com");
		other.addAllowedHeader("header2");
		other.addExposedHeader("header4");
		other.addAllowedMethod(HttpMethod.PUT.name());
		other.setMaxAge(456L);
		other.setAllowCredentials(false);
		config = config.combine(other);
		assertEquals(Arrays.asList("https://domain1.com", "https://domain2.com"), config.getAllowedOrigins());
		assertEquals(Arrays.asList("header1", "header2"), config.getAllowedHeaders());
		assertEquals(Arrays.asList("header3", "header4"), config.getExposedHeaders());
		assertEquals(Arrays.asList(HttpMethod.GET.name(), HttpMethod.PUT.name()), config.getAllowedMethods());
		assertEquals(new Long(456), config.getMaxAge());
		assertFalse(config.getAllowCredentials());
	}

	@Test
	public void checkOriginAllowed() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("*"));
		assertEquals("*", config.checkOrigin("https://domain.com"));
		config.setAllowCredentials(true);
		assertEquals("https://domain.com", config.checkOrigin("https://domain.com"));
		config.setAllowedOrigins(Arrays.asList("https://domain.com"));
		assertEquals("https://domain.com", config.checkOrigin("https://domain.com"));
		config.setAllowCredentials(false);
		assertEquals("https://domain.com", config.checkOrigin("https://domain.com"));
	}

	@Test
	public void checkOriginNotAllowed() {
		CorsConfiguration config = new CorsConfiguration();
		assertNull(config.checkOrigin(null));
		assertNull(config.checkOrigin("https://domain.com"));
		config.addAllowedOrigin("*");
		assertNull(config.checkOrigin(null));
		config.setAllowedOrigins(Arrays.asList("https://domain1.com"));
		assertNull(config.checkOrigin("https://domain2.com"));
		config.setAllowedOrigins(new ArrayList<>());
		assertNull(config.checkOrigin("https://domain.com"));
	}

	@Test
	public void checkMethodAllowed() {
		CorsConfiguration config = new CorsConfiguration();
		assertEquals(Arrays.asList(HttpMethod.GET, HttpMethod.HEAD), config.checkHttpMethod(HttpMethod.GET));
		config.addAllowedMethod("GET");
		assertEquals(Arrays.asList(HttpMethod.GET), config.checkHttpMethod(HttpMethod.GET));
		config.addAllowedMethod("POST");
		assertEquals(Arrays.asList(HttpMethod.GET, HttpMethod.POST), config.checkHttpMethod(HttpMethod.GET));
		assertEquals(Arrays.asList(HttpMethod.GET, HttpMethod.POST), config.checkHttpMethod(HttpMethod.POST));
	}

	@Test
	public void checkMethodNotAllowed() {
		CorsConfiguration config = new CorsConfiguration();
		assertNull(config.checkHttpMethod(null));
		assertNull(config.checkHttpMethod(HttpMethod.DELETE));
		config.setAllowedMethods(new ArrayList<>());
		assertNull(config.checkHttpMethod(HttpMethod.POST));
	}

	@Test
	public void checkHeadersAllowed() {
		CorsConfiguration config = new CorsConfiguration();
		assertEquals(Collections.emptyList(), config.checkHeaders(Collections.emptyList()));
		config.addAllowedHeader("header1");
		config.addAllowedHeader("header2");
		assertEquals(Arrays.asList("header1"), config.checkHeaders(Arrays.asList("header1")));
		assertEquals(Arrays.asList("header1", "header2"),
				config.checkHeaders(Arrays.asList("header1", "header2")));
		assertEquals(Arrays.asList("header1", "header2"),
				config.checkHeaders(Arrays.asList("header1", "header2", "header3")));
	}

	@Test
	public void checkHeadersNotAllowed() {
		CorsConfiguration config = new CorsConfiguration();
		assertNull(config.checkHeaders(null));
		assertNull(config.checkHeaders(Arrays.asList("header1")));
		config.setAllowedHeaders(Collections.emptyList());
		assertNull(config.checkHeaders(Arrays.asList("header1")));
		config.addAllowedHeader("header2");
		config.addAllowedHeader("header3");
		assertNull(config.checkHeaders(Arrays.asList("header1")));
	}

	@Test  // SPR-15772
	public void changePermitDefaultValues() {
		CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
		config.addAllowedOrigin("https://domain.com");
		config.addAllowedHeader("header1");
		config.addAllowedMethod("PATCH");
		assertEquals(Arrays.asList("*", "https://domain.com"), config.getAllowedOrigins());
		assertEquals(Arrays.asList("*", "header1"), config.getAllowedHeaders());
		assertEquals(Arrays.asList("GET", "HEAD", "POST", "PATCH"), config.getAllowedMethods());
	}

}
