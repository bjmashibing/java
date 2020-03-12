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

package org.springframework.mock.web;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link MockHttpSession}.
 *
 * @author Sam Brannen
 * @author Vedran Pavic
 * @since 3.2
 */
public class MockHttpSessionTests {

	private MockHttpSession session = new MockHttpSession();


	@Test
	public void invalidateOnce() {
		assertFalse(session.isInvalid());
		session.invalidate();
		assertTrue(session.isInvalid());
	}

	@Test(expected = IllegalStateException.class)
	public void invalidateTwice() {
		session.invalidate();
		session.invalidate();
	}

	/**
	 * @since 4.0
	 */
	@Test(expected = IllegalStateException.class)
	public void getCreationTimeOnInvalidatedSession() {
		session.invalidate();
		session.getCreationTime();
	}

	/**
	 * @since 4.0
	 */
	@Test(expected = IllegalStateException.class)
	public void getLastAccessedTimeOnInvalidatedSession() {
		session.invalidate();
		session.getLastAccessedTime();
	}

	/**
	 * @since 4.0
	 */
	@Test(expected = IllegalStateException.class)
	public void getAttributeOnInvalidatedSession() {
		session.invalidate();
		session.getAttribute("foo");
	}

	/**
	 * @since 4.0
	 */
	@Test(expected = IllegalStateException.class)
	public void getAttributeNamesOnInvalidatedSession() {
		session.invalidate();
		session.getAttributeNames();
	}

	/**
	 * @since 4.0
	 */
	@Test(expected = IllegalStateException.class)
	public void getValueOnInvalidatedSession() {
		session.invalidate();
		session.getValue("foo");
	}

	/**
	 * @since 4.0
	 */
	@Test(expected = IllegalStateException.class)
	public void getValueNamesOnInvalidatedSession() {
		session.invalidate();
		session.getValueNames();
	}

	/**
	 * @since 4.0
	 */
	@Test(expected = IllegalStateException.class)
	public void setAttributeOnInvalidatedSession() {
		session.invalidate();
		session.setAttribute("name", "value");
	}

	/**
	 * @since 4.0
	 */
	@Test(expected = IllegalStateException.class)
	public void putValueOnInvalidatedSession() {
		session.invalidate();
		session.putValue("name", "value");
	}

	/**
	 * @since 4.0
	 */
	@Test(expected = IllegalStateException.class)
	public void removeAttributeOnInvalidatedSession() {
		session.invalidate();
		session.removeAttribute("name");
	}

	/**
	 * @since 4.0
	 */
	@Test(expected = IllegalStateException.class)
	public void removeValueOnInvalidatedSession() {
		session.invalidate();
		session.removeValue("name");
	}

	/**
	 * @since 4.0
	 */
	@Test(expected = IllegalStateException.class)
	public void isNewOnInvalidatedSession() {
		session.invalidate();
		session.isNew();
	}

	@Test
	public void bindingListenerBindListener() {
		String bindingListenerName = "bindingListener";
		CountingHttpSessionBindingListener bindingListener = new CountingHttpSessionBindingListener();

		session.setAttribute(bindingListenerName, bindingListener);

		assertEquals(bindingListener.getCounter(), 1);
	}

	@Test
	public void bindingListenerBindListenerThenUnbind() {
		String bindingListenerName = "bindingListener";
		CountingHttpSessionBindingListener bindingListener = new CountingHttpSessionBindingListener();

		session.setAttribute(bindingListenerName, bindingListener);
		session.removeAttribute(bindingListenerName);

		assertEquals(bindingListener.getCounter(), 0);
	}

	@Test
	public void bindingListenerBindSameListenerTwice() {
		String bindingListenerName = "bindingListener";
		CountingHttpSessionBindingListener bindingListener = new CountingHttpSessionBindingListener();

		session.setAttribute(bindingListenerName, bindingListener);
		session.setAttribute(bindingListenerName, bindingListener);

		assertEquals(bindingListener.getCounter(), 1);
	}

	@Test
	public void bindingListenerBindListenerOverwrite() {
		String bindingListenerName = "bindingListener";
		CountingHttpSessionBindingListener bindingListener1 = new CountingHttpSessionBindingListener();
		CountingHttpSessionBindingListener bindingListener2 = new CountingHttpSessionBindingListener();

		session.setAttribute(bindingListenerName, bindingListener1);
		session.setAttribute(bindingListenerName, bindingListener2);

		assertEquals(bindingListener1.getCounter(), 0);
		assertEquals(bindingListener2.getCounter(), 1);
	}

	private static class CountingHttpSessionBindingListener
			implements HttpSessionBindingListener {

		private final AtomicInteger counter = new AtomicInteger(0);

		@Override
		public void valueBound(HttpSessionBindingEvent event) {
			this.counter.incrementAndGet();
		}

		@Override
		public void valueUnbound(HttpSessionBindingEvent event) {
			this.counter.decrementAndGet();
		}

		int getCounter() {
			return this.counter.get();
		}

	}

}
