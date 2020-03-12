/*
 * Copyright 2002-2015 the original author or authors.
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

package org.springframework.context.event;

import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.Lifecycle;
import org.springframework.context.support.StaticApplicationContext;

import static org.junit.Assert.*;

/**
 * @author Mark Fisher
 * @author Juergen Hoeller
 */
public class LifecycleEventTests {

	@Test
	public void contextStartedEvent() {
		StaticApplicationContext context = new StaticApplicationContext();
		context.registerSingleton("lifecycle", LifecycleTestBean.class);
		context.registerSingleton("listener", LifecycleListener.class);
		context.refresh();
		LifecycleTestBean lifecycleBean = (LifecycleTestBean) context.getBean("lifecycle");
		LifecycleListener listener = (LifecycleListener) context.getBean("listener");
		assertFalse(lifecycleBean.isRunning());
		assertEquals(0, listener.getStartedCount());
		context.start();
		assertTrue(lifecycleBean.isRunning());
		assertEquals(1, listener.getStartedCount());
		assertSame(context, listener.getApplicationContext());
	}

	@Test
	public void contextStoppedEvent() {
		StaticApplicationContext context = new StaticApplicationContext();
		context.registerSingleton("lifecycle", LifecycleTestBean.class);
		context.registerSingleton("listener", LifecycleListener.class);
		context.refresh();
		LifecycleTestBean lifecycleBean = (LifecycleTestBean) context.getBean("lifecycle");
		LifecycleListener listener = (LifecycleListener) context.getBean("listener");
		assertFalse(lifecycleBean.isRunning());
		context.start();
		assertTrue(lifecycleBean.isRunning());
		assertEquals(0, listener.getStoppedCount());
		context.stop();
		assertFalse(lifecycleBean.isRunning());
		assertEquals(1, listener.getStoppedCount());
		assertSame(context, listener.getApplicationContext());
	}


	private static class LifecycleListener implements ApplicationListener<ApplicationEvent> {

		private ApplicationContext context;

		private int startedCount;

		private int stoppedCount;

		@Override
		public void onApplicationEvent(ApplicationEvent event) {
			if (event instanceof ContextStartedEvent) {
				this.context = ((ContextStartedEvent) event).getApplicationContext();
				this.startedCount++;
			}
			else if (event instanceof ContextStoppedEvent) {
				this.context = ((ContextStoppedEvent) event).getApplicationContext();
				this.stoppedCount++;
			}
		}

		public ApplicationContext getApplicationContext() {
			return this.context;
		}

		public int getStartedCount() {
			return this.startedCount;
		}

		public int getStoppedCount() {
			return this.stoppedCount;
		}
	}


	private static class LifecycleTestBean implements Lifecycle {

		private boolean running;

		@Override
		public boolean isRunning() {
			return this.running;
		}

		@Override
		public void start() {
			this.running = true;
		}

		@Override
		public void stop() {
			this.running = false;
		}
	}

}
