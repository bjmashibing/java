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

package org.springframework.orm.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.transaction.InvalidIsolationLevelException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

/**
 * @author Costin Leau
 * @author Juergen Hoeller
 * @author Phillip Webb
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class JpaTransactionManagerTests {

	private EntityManagerFactory factory;

	private EntityManager manager;

	private EntityTransaction tx;

	private JpaTransactionManager tm;

	private TransactionTemplate tt;


	@Before
	public void setup() {
		factory = mock(EntityManagerFactory.class);
		manager = mock(EntityManager.class);
		tx = mock(EntityTransaction.class);

		tm = new JpaTransactionManager(factory);
		tt = new TransactionTemplate(tm);

		given(factory.createEntityManager()).willReturn(manager);
		given(manager.getTransaction()).willReturn(tx);
		given(manager.isOpen()).willReturn(true);
	}

	@After
	public void verifyTransactionSynchronizationManagerState() {
		assertTrue(TransactionSynchronizationManager.getResourceMap().isEmpty());
		assertFalse(TransactionSynchronizationManager.isSynchronizationActive());
		assertFalse(TransactionSynchronizationManager.isCurrentTransactionReadOnly());
		assertFalse(TransactionSynchronizationManager.isActualTransactionActive());
	}


	@Test
	public void testTransactionCommit() {
		given(manager.getTransaction()).willReturn(tx);

		final List<String> l = new ArrayList<>();
		l.add("test");

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		Object result = tt.execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				assertTrue(TransactionSynchronizationManager.hasResource(factory));
				EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
				return l;
			}
		});
		assertSame(l, result);

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(tx).commit();
		verify(manager).flush();
		verify(manager).close();
	}

	@Test
	public void testTransactionCommitWithRollbackException() {
		given(manager.getTransaction()).willReturn(tx);
		given(tx.getRollbackOnly()).willReturn(true);
		willThrow(new RollbackException()).given(tx).commit();

		final List<String> l = new ArrayList<>();
		l.add("test");

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		try {
			Object result = tt.execute(new TransactionCallback() {
				@Override
				public Object doInTransaction(TransactionStatus status) {
					assertTrue(TransactionSynchronizationManager.hasResource(factory));
					EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
					return l;
				}
			});
			assertSame(l, result);
		}
		catch (TransactionSystemException tse) {
			// expected
			assertTrue(tse.getCause() instanceof RollbackException);
		}

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(manager).flush();
		verify(manager).close();
	}

	@Test
	public void testTransactionRollback() {
		given(manager.getTransaction()).willReturn(tx);
		given(tx.isActive()).willReturn(true);

		final List<String> l = new ArrayList<>();
		l.add("test");

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		try {
			tt.execute(new TransactionCallback() {
				@Override
				public Object doInTransaction(TransactionStatus status) {
					assertTrue(TransactionSynchronizationManager.hasResource(factory));
					EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
					throw new RuntimeException("some exception");
				}
			});
			fail("Should have propagated RuntimeException");
		}
		catch (RuntimeException ex) {
			// expected
			assertEquals("some exception", ex.getMessage());
		}

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(tx).rollback();
		verify(manager).close();
	}

	@Test
	public void testTransactionRollbackWithAlreadyRolledBack() {
		given(manager.getTransaction()).willReturn(tx);

		final List<String> l = new ArrayList<>();
		l.add("test");

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		try {
			tt.execute(new TransactionCallback() {
				@Override
				public Object doInTransaction(TransactionStatus status) {
					assertTrue(TransactionSynchronizationManager.hasResource(factory));
					EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
					throw new RuntimeException("some exception");
				}
			});
			fail("Should have propagated RuntimeException");
		}
		catch (RuntimeException ex) {
			// expected
		}

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(manager).close();
	}

	@Test
	public void testTransactionRollbackOnly() {
		given(manager.getTransaction()).willReturn(tx);
		given(tx.isActive()).willReturn(true);

		final List<String> l = new ArrayList<>();
		l.add("test");

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		tt.execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				assertTrue(TransactionSynchronizationManager.hasResource(factory));

				EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
				status.setRollbackOnly();

				return l;
			}
		});

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(manager).flush();
		verify(tx).rollback();
		verify(manager).close();
	}

	@Test
	public void testParticipatingTransactionWithCommit() {
		given(manager.getTransaction()).willReturn(tx);

		final List<String> l = new ArrayList<>();
		l.add("test");

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		tt.execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				assertTrue(TransactionSynchronizationManager.hasResource(factory));

				return tt.execute(new TransactionCallback() {
					@Override
					public Object doInTransaction(TransactionStatus status) {
						EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
						return l;
					}
				});
			}
		});

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(manager).flush();
		verify(tx).commit();
		verify(manager).close();
	}

	@Test
	public void testParticipatingTransactionWithRollback() {
		given(manager.getTransaction()).willReturn(tx);
		given(tx.isActive()).willReturn(true);

		final List<String> l = new ArrayList<>();
		l.add("test");

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		try {
			tt.execute(new TransactionCallback() {
				@Override
				public Object doInTransaction(TransactionStatus status) {
					assertTrue(TransactionSynchronizationManager.hasResource(factory));
					return tt.execute(new TransactionCallback() {
						@Override
						public Object doInTransaction(TransactionStatus status) {
							EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
							throw new RuntimeException("some exception");
						}
					});
				}
			});
			fail("Should have propagated RuntimeException");
		}
		catch (RuntimeException ex) {
			// expected
		}

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(tx).setRollbackOnly();
		verify(tx).rollback();
		verify(manager).close();
	}

	@Test
	public void testParticipatingTransactionWithRollbackOnly() {
		given(manager.getTransaction()).willReturn(tx);
		given(tx.isActive()).willReturn(true);
		given(tx.getRollbackOnly()).willReturn(true);
		willThrow(new RollbackException()).given(tx).commit();

		final List<String> l = new ArrayList<>();
		l.add("test");

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		try {
			tt.execute(new TransactionCallback() {
				@Override
				public Object doInTransaction(TransactionStatus status) {
					assertTrue(TransactionSynchronizationManager.hasResource(factory));

					return tt.execute(new TransactionCallback() {
						@Override
						public Object doInTransaction(TransactionStatus status) {
							EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
							status.setRollbackOnly();
							return null;
						}
					});
				}
			});
			fail("Should have thrown TransactionSystemException");
		}
		catch (TransactionSystemException tse) {
			// expected
			assertTrue(tse.getCause() instanceof RollbackException);
		}

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(manager).flush();
		verify(tx).setRollbackOnly();
		verify(manager).close();
	}

	@Test
	public void testParticipatingTransactionWithRequiresNew() {
		tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		given(factory.createEntityManager()).willReturn(manager);
		given(manager.getTransaction()).willReturn(tx);
		given(manager.isOpen()).willReturn(true);

		final List<String> l = new ArrayList<>();
		l.add("test");

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		Object result = tt.execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				assertTrue(TransactionSynchronizationManager.hasResource(factory));
				return tt.execute(new TransactionCallback() {
					@Override
					public Object doInTransaction(TransactionStatus status) {
						EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
						return l;
					}
				});
			}
		});
		assertSame(l, result);

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(manager).flush();
		verify(manager, times(2)).close();
		verify(tx, times(2)).begin();
	}

	@Test
	public void testParticipatingTransactionWithRequiresNewAndPrebound() {
		tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		given(manager.getTransaction()).willReturn(tx);

		final List<String> l = new ArrayList<>();
		l.add("test");

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		TransactionSynchronizationManager.bindResource(factory, new EntityManagerHolder(manager));

		try {
			Object result = tt.execute(new TransactionCallback() {
				@Override
				public Object doInTransaction(TransactionStatus status) {
					EntityManagerFactoryUtils.getTransactionalEntityManager(factory);

					assertTrue(TransactionSynchronizationManager.hasResource(factory));
					return tt.execute(new TransactionCallback() {
						@Override
						public Object doInTransaction(TransactionStatus status) {
							EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
							return l;
						}
					});
				}
			});
			assertSame(l, result);
		}
		finally {
			TransactionSynchronizationManager.unbindResource(factory);
		}

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(tx, times(2)).begin();
		verify(tx, times(2)).commit();
		verify(manager).flush();
		verify(manager).close();
	}

	@Test
	public void testPropagationSupportsAndRequiresNew() {
		tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);

		given(manager.getTransaction()).willReturn(tx);

		final List<String> l = new ArrayList<>();
		l.add("test");

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		Object result = tt.execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				assertFalse(TransactionSynchronizationManager.hasResource(factory));
				TransactionTemplate tt2 = new TransactionTemplate(tm);
				tt2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
				return tt2.execute(new TransactionCallback() {
					@Override
					public Object doInTransaction(TransactionStatus status) {
						EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
						return l;
					}
				});
			}
		});
		assertSame(l, result);

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(tx).commit();
		verify(manager).flush();
		verify(manager).close();
	}

	@Test
	public void testPropagationSupportsAndRequiresNewAndEarlyAccess() {
		tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);

		given(factory.createEntityManager()).willReturn(manager);
		given(manager.getTransaction()).willReturn(tx);
		given(manager.isOpen()).willReturn(true);

		final List<String> l = new ArrayList<>();
		l.add("test");

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		Object result = tt.execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				EntityManagerFactoryUtils.getTransactionalEntityManager(factory);

				assertTrue(TransactionSynchronizationManager.hasResource(factory));
				TransactionTemplate tt2 = new TransactionTemplate(tm);
				tt2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
				return tt2.execute(new TransactionCallback() {
					@Override
					public Object doInTransaction(TransactionStatus status) {
						EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
						return l;
					}
				});
			}
		});
		assertSame(l, result);

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(tx).commit();
		verify(manager).flush();
		verify(manager, times(2)).close();
	}

	@Test
	public void testTransactionWithRequiresNewInAfterCompletion() {
		tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		EntityManager manager2 = mock(EntityManager.class);
		EntityTransaction tx2 = mock(EntityTransaction.class);

		given(manager.getTransaction()).willReturn(tx);
		given(factory.createEntityManager()).willReturn(manager, manager2);
		given(manager2.getTransaction()).willReturn(tx2);
		given(manager2.isOpen()).willReturn(true);

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		tt.execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCompletion(int status) {
						tt.execute(new TransactionCallback() {
							@Override
							public Object doInTransaction(TransactionStatus status) {
								EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
								return null;
							}
						});
					}
				});
				return null;
			}
		});

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(tx).commit();
		verify(tx2).begin();
		verify(tx2).commit();
		verify(manager).flush();
		verify(manager).close();
		verify(manager2).flush();
		verify(manager2).close();
	}

	@Test
	public void testTransactionCommitWithPropagationSupports() {
		given(manager.isOpen()).willReturn(true);

		final List<String> l = new ArrayList<>();
		l.add("test");

		tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		Object result = tt.execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				assertTrue(!TransactionSynchronizationManager.hasResource(factory));
				assertTrue(TransactionSynchronizationManager.isSynchronizationActive());
				assertTrue(!status.isNewTransaction());
				EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
				return l;
			}
		});
		assertSame(l, result);

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(manager).flush();
		verify(manager).close();
	}

	@Test
	public void testTransactionRollbackWithPropagationSupports() {
		given(manager.isOpen()).willReturn(true);

		tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		tt.execute(new TransactionCallback() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				assertTrue(!TransactionSynchronizationManager.hasResource(factory));
				assertTrue(TransactionSynchronizationManager.isSynchronizationActive());
				assertTrue(!status.isNewTransaction());
				EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
				status.setRollbackOnly();
				return null;
			}
		});

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(manager).flush();
		verify(manager).close();
	}

	@Test
	public void testTransactionCommitWithPrebound() {
		given(manager.getTransaction()).willReturn(tx);

		final List<String> l = new ArrayList<>();
		l.add("test");

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());
		TransactionSynchronizationManager.bindResource(factory, new EntityManagerHolder(manager));

		try {
			Object result = tt.execute(new TransactionCallback() {
				@Override
				public Object doInTransaction(TransactionStatus status) {
					assertTrue(TransactionSynchronizationManager.hasResource(factory));
					assertTrue(TransactionSynchronizationManager.isSynchronizationActive());
					EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
					return l;
				}
			});
			assertSame(l, result);

			assertTrue(TransactionSynchronizationManager.hasResource(factory));
			assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());
		}
		finally {
			TransactionSynchronizationManager.unbindResource(factory);
		}

		verify(tx).begin();
		verify(tx).commit();
	}

	@Test
	public void testTransactionRollbackWithPrebound() {
		given(manager.getTransaction()).willReturn(tx);
		given(tx.isActive()).willReturn(true);

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());
		TransactionSynchronizationManager.bindResource(factory, new EntityManagerHolder(manager));

		try {
			tt.execute(new TransactionCallback() {
				@Override
				public Object doInTransaction(TransactionStatus status) {
					assertTrue(TransactionSynchronizationManager.hasResource(factory));
					assertTrue(TransactionSynchronizationManager.isSynchronizationActive());
					EntityManagerFactoryUtils.getTransactionalEntityManager(factory);
					status.setRollbackOnly();
					return null;
				}
			});

			assertTrue(TransactionSynchronizationManager.hasResource(factory));
			assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());
		}
		finally {
			TransactionSynchronizationManager.unbindResource(factory);
		}

		verify(tx).begin();
		verify(tx).rollback();
		verify(manager).clear();
	}

	@Test
	public void testTransactionCommitWithPreboundAndPropagationSupports() {
		final List<String> l = new ArrayList<>();
		l.add("test");

		tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());
		TransactionSynchronizationManager.bindResource(factory, new EntityManagerHolder(manager));

		try {
			Object result = tt.execute(new TransactionCallback() {
				@Override
				public Object doInTransaction(TransactionStatus status) {
					assertTrue(TransactionSynchronizationManager.hasResource(factory));
					assertTrue(TransactionSynchronizationManager.isSynchronizationActive());
					assertTrue(!status.isNewTransaction());
					EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
					return l;
				}
			});
			assertSame(l, result);

			assertTrue(TransactionSynchronizationManager.hasResource(factory));
			assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());
		}
		finally {
			TransactionSynchronizationManager.unbindResource(factory);
		}

		verify(manager).flush();
	}

	@Test
	public void testTransactionRollbackWithPreboundAndPropagationSupports() {
		tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());
		TransactionSynchronizationManager.bindResource(factory, new EntityManagerHolder(manager));

		try {
			tt.execute(new TransactionCallback() {
				@Override
				public Object doInTransaction(TransactionStatus status) {
					assertTrue(TransactionSynchronizationManager.hasResource(factory));
					assertTrue(TransactionSynchronizationManager.isSynchronizationActive());
					assertTrue(!status.isNewTransaction());
					EntityManagerFactoryUtils.getTransactionalEntityManager(factory).flush();
					status.setRollbackOnly();
					return null;
				}
			});

			assertTrue(TransactionSynchronizationManager.hasResource(factory));
			assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());
		}
		finally {
			TransactionSynchronizationManager.unbindResource(factory);
		}

		verify(manager).flush();
		verify(manager).clear();
	}

	@Test
	public void testInvalidIsolation() {
		tt.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);

		given(manager.isOpen()).willReturn(true);

		try {
			tt.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
				}
			});
			fail("Should have thrown InvalidIsolationLevelException");
		}
		catch (InvalidIsolationLevelException ex) {
			// expected
		}

		verify(manager).close();
	}

	@Test
	public void testTransactionFlush() {
		given(manager.getTransaction()).willReturn(tx);

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		tt.execute(new TransactionCallbackWithoutResult() {
			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				assertTrue(TransactionSynchronizationManager.hasResource(factory));
				status.flush();
			}
		});

		assertTrue(!TransactionSynchronizationManager.hasResource(factory));
		assertTrue(!TransactionSynchronizationManager.isSynchronizationActive());

		verify(tx).commit();
		verify(manager).flush();
		verify(manager).close();
	}

}
