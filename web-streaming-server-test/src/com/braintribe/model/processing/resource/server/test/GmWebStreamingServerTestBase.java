// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.model.processing.resource.server.test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

import com.braintribe.model.processing.resource.server.WebStreamingServer;
import com.braintribe.model.processing.resource.server.test.commons.TestResourceData;
import com.braintribe.model.processing.resource.server.test.commons.TestResourceDataTools;
import com.braintribe.model.processing.resource.server.test.wire.contract.MainContract;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;
import com.braintribe.model.processing.session.api.resource.ResourceAccess;
import com.braintribe.model.processing.session.api.resource.ResourceAccessFactory;
import com.braintribe.model.resource.Resource;
import com.braintribe.processing.test.web.undertow.UndertowServer;
import com.braintribe.processing.test.web.undertow.UndertowServer.UndertowServerBuilder;
import com.braintribe.wire.api.context.WireContext;

/**
 * <p>
 * Base class for {@link WebStreamingServer} test suites.
 * 
 */
public abstract class GmWebStreamingServerTestBase {

	protected static int THREAD_POOL_SIZE = 5;
	protected static int MAX_CONCURRENT_TESTS = 5;
	protected static long CONCURRENT_TESTS_TIMEOUT = 5;
	protected static TimeUnit CONCURRENT_TESTS_TIMEOUT_UNIT = TimeUnit.SECONDS;
	protected static int MAX_SEQUENTIAL_TESTS = 3;
	protected static long ASYNC_TESTS_TIMEOUT = 2;
	protected static TimeUnit ASYNC_TESTS_TIMEOUT_UNIT = TimeUnit.SECONDS;

	@Rule
	public TestRule globalTimeout = new DisableOnDebug(Timeout.seconds(15L));

	protected static UndertowServer serverControl;
	protected static URL servletUrl;

	protected static WireContext<MainContract> context;

	protected static Resource testResource = null;
	protected static TestResourceData testResourceData;

	// ============================= //
	// ======== LIFE CYCLE ========= //
	// ============================= //

	public static void initialize() throws Exception {
		context = MainContract.context();
	}

	public static void destroy() throws Exception {
		context.shutdown();
	}

	public static URL startServer() {

		List<Filter> filters = context.contract().filters();
		HttpServlet servlet = context.contract().servlet();

		UndertowServerBuilder builder = UndertowServer.create("/test-app");

		String servletName = servlet.getClass().getSimpleName();
		String servletPath = "/stream";

		for (Filter filter : filters) {
			String filterName = filter.getClass().getSimpleName();
			builder.addFilter(filterName, filter);
			builder.addFilterUrlMapping(filterName, servletPath, DispatcherType.REQUEST);
		}

		builder.addServlet(servletName, servlet, true, servletPath);

		serverControl = builder.start();

		servletUrl = serverControl.getServletUrl(servletName);

		return servletUrl;

	}

	public static void shutdownServer() {
		serverControl.stop();
	}

	// ===================================== //
	// ========= GENERIC TESTS - =========== //
	// ===================================== //

	@Ignore
	@Test
	public void _serverStart() throws Exception {
		Thread.sleep(1200000);
	}

	// ============================= //
	// ========= COMMONS =========== //
	// ============================= //

	protected static void createTestResource() throws Exception {

		testResourceData = TestResourceDataTools.createResourceData();

		PersistenceGmSessionFactory persistenceGmSessionFactory = context.contract().sessionFactory();

		ResourceAccessFactory<PersistenceGmSession> serverResourceAccessFactory = context.contract().resourceAccessFactory();

		PersistenceGmSession gmSession = persistenceGmSessionFactory.newSession("");

		ResourceAccess resourceAccess = serverResourceAccessFactory.newInstance(gmSession);

		testResource = resourceAccess.create().name("test-resouce-0.bin").store(testResourceData.openInputStream());

		gmSession.commit();

	}

	protected void testConcurrently(TestCaller test, int numThreads) throws Exception {

		List<TestCaller> callers = new ArrayList<>();

		for (int i = 0; i < numThreads; i++) {
			callers.add(test);
		}

		testConcurrently(callers);

	}

	protected void testConcurrently(List<TestCaller> tests) throws Exception {

		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE,
				new NamedPoolThreadFactory(GmWebStreamingServerTestBase.class.getSimpleName()));

		try {
			List<Future<Throwable>> results = executorService.invokeAll(tests, CONCURRENT_TESTS_TIMEOUT, CONCURRENT_TESTS_TIMEOUT_UNIT);

			List<Throwable> errors = new ArrayList<Throwable>();

			for (Future<Throwable> result : results) {
				Throwable error = null;

				try {
					error = result.get();
				} catch (CancellationException e) {
					System.out.println("Test cancelled as it didn't complete after " + CONCURRENT_TESTS_TIMEOUT + " "
							+ CONCURRENT_TESTS_TIMEOUT_UNIT.toString().toLowerCase());
					continue;
				}

				if (error != null) {
					errors.add(error);
				}
			}

			if (errors.isEmpty()) {
				System.out.println(tests.size() + " concurrent tests completed successfully.");
			} else {
				AssertionError error = new AssertionError("From " + tests.size() + " concurrent tests, " + errors.size() + " failed.");
				for (Throwable cause : errors) {
					error.addSuppressed(cause);
				}
				throw error;
			}
		} finally {
			executorService.shutdownNow();
		}

	}

	public abstract class TestCaller implements Callable<Throwable> {

		public abstract void test() throws Throwable;

		@Override
		public Throwable call() throws Exception {
			try {
				test();
				return null;
			} catch (Throwable e) {
				return e;
			}
		}

	}

	/**
	 * <p>
	 * A {@link ThreadFactory} allowing a custom name for the generated Thread(s).
	 */
	static class NamedPoolThreadFactory implements ThreadFactory {

		private static final AtomicInteger poolNumber = new AtomicInteger(1);
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;

		NamedPoolThreadFactory(String poolName) {
			namePrefix = poolName + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = Thread.ofVirtual().name(namePrefix + threadNumber.getAndIncrement()).unstarted(r);
			if (t.getPriority() != Thread.NORM_PRIORITY) {
				t.setPriority(Thread.NORM_PRIORITY);
			}
			return t;
		}

	}

}
