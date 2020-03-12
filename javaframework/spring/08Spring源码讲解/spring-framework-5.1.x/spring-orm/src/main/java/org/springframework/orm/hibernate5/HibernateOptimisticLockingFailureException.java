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

package org.springframework.orm.hibernate5;

import org.hibernate.StaleObjectStateException;
import org.hibernate.StaleStateException;
import org.hibernate.dialect.lock.OptimisticEntityLockException;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

/**
 * Hibernate-specific subclass of ObjectOptimisticLockingFailureException.
 * Converts Hibernate's StaleObjectStateException, StaleStateException
 * and OptimisticEntityLockException.
 *
 * @author Juergen Hoeller
 * @since 4.2
 * @see SessionFactoryUtils#convertHibernateAccessException
 */
@SuppressWarnings("serial")
public class HibernateOptimisticLockingFailureException extends ObjectOptimisticLockingFailureException {

	public HibernateOptimisticLockingFailureException(StaleObjectStateException ex) {
		super(ex.getEntityName(), ex.getIdentifier(), ex);
	}

	public HibernateOptimisticLockingFailureException(StaleStateException ex) {
		super(ex.getMessage(), ex);
	}

	public HibernateOptimisticLockingFailureException(OptimisticEntityLockException ex) {
		super(ex.getMessage(), ex);
	}

}
