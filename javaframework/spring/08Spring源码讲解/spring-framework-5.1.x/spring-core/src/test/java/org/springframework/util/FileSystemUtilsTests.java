/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.util;

import java.io.File;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Rob Harrop
 */
public class FileSystemUtilsTests {

	@Test
	public void deleteRecursively() throws Exception {
		File root = new File("./tmp/root");
		File child = new File(root, "child");
		File grandchild = new File(child, "grandchild");

		grandchild.mkdirs();

		File bar = new File(child, "bar.txt");
		bar.createNewFile();

		assertTrue(root.exists());
		assertTrue(child.exists());
		assertTrue(grandchild.exists());
		assertTrue(bar.exists());

		FileSystemUtils.deleteRecursively(root);

		assertFalse(root.exists());
		assertFalse(child.exists());
		assertFalse(grandchild.exists());
		assertFalse(bar.exists());
	}

	@Test
	public void copyRecursively() throws Exception {
		File src = new File("./tmp/src");
		File child = new File(src, "child");
		File grandchild = new File(child, "grandchild");

		grandchild.mkdirs();

		File bar = new File(child, "bar.txt");
		bar.createNewFile();

		assertTrue(src.exists());
		assertTrue(child.exists());
		assertTrue(grandchild.exists());
		assertTrue(bar.exists());

		File dest = new File("./dest");
		FileSystemUtils.copyRecursively(src, dest);

		assertTrue(dest.exists());
		assertTrue(new File(dest, child.getName()).exists());

		FileSystemUtils.deleteRecursively(src);
		assertFalse(src.exists());
	}


	@After
	public void tearDown() throws Exception {
		File tmp = new File("./tmp");
		if (tmp.exists()) {
			FileSystemUtils.deleteRecursively(tmp);
		}
		File dest = new File("./dest");
		if (dest.exists()) {
			FileSystemUtils.deleteRecursively(dest);
		}
	}

}
