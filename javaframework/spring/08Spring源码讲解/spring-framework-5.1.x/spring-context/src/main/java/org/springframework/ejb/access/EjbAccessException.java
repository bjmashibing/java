/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.ejb.access;

import org.springframework.core.NestedRuntimeException;

/**
 * Exception that gets thrown when an EJB stub cannot be accessed properly.
 *
 * @author Juergen Hoeller
 * @since 2.0
 */
@SuppressWarnings("serial")
public class EjbAccessException extends NestedRuntimeException {

	/**
	 * Constructor for EjbAccessException.
	 * @param msg the detail message
	 */
	public EjbAccessException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for EjbAccessException.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public EjbAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
