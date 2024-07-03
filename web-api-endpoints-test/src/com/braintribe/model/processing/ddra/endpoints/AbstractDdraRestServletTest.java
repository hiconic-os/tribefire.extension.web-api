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
package com.braintribe.model.processing.ddra.endpoints;


import static com.braintribe.wire.api.util.Maps.entry;
import static com.braintribe.wire.api.util.Maps.linkedMap;
import static com.braintribe.wire.api.util.Sets.set;

import java.io.IOException;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.braintribe.codec.marshaller.api.MarshallerRegistry;
import com.braintribe.exception.AuthorizationException;
import com.braintribe.model.processing.ddra.endpoints.ioc.TestMarshallerRegistry;
import com.braintribe.model.processing.securityservice.api.exceptions.SecurityServiceException;
import com.braintribe.model.processing.session.api.managed.NotFoundException;
import com.braintribe.processing.test.web.undertow.UndertowServer;
import com.braintribe.servlet.exception.ExceptionFilter;
import com.braintribe.servlet.exception.StandardExceptionHandler;
import com.braintribe.servlet.exception.StandardExceptionHandler.Exposure;

public abstract class AbstractDdraRestServletTest {

	protected static final String JSON = "application/json";
	protected static final String XML = "application/xml";
	protected static final String MULTIPART = "multipart/form-data";

	protected static UndertowServer server;
	protected static CloseableHttpClient httpClient;
	protected static TestHttpRequestFactory requests;
	
	protected static MarshallerRegistry marshallerRegistry = TestMarshallerRegistry.getMarshallerRegistry();

	
	protected static void setupStandardUndertowServer(String basePath, String servletName, HttpServlet servlet, String... mappings) {
		ExceptionFilter exceptionFilter = new ExceptionFilter();
		exceptionFilter.setExceptionHandlers(set(standardExceptionHandler()));

		//@formatter:off
		AbstractDdraRestServletTest.server =
				UndertowServer.create(basePath)
					.addFilter("exception-filter", exceptionFilter)
					.addFilterUrlMapping("exception-filter", "/*", DispatcherType.REQUEST)
					.addServlet(servletName, servlet, mappings)
					.start();
		//@formatter:on

		httpClient = HttpClients.createDefault();
		requests = new TestHttpRequestFactory(httpClient, server.getServerUrl());
	}
	
	
	protected static void destroy() throws IOException {
		server.stop();
		httpClient.close();
	}

	
	private static StandardExceptionHandler standardExceptionHandler() {
		StandardExceptionHandler bean = new StandardExceptionHandler();
		bean.setExceptionExposure(Exposure.auto);
		bean.setMarshallerRegistry(marshallerRegistry);
		bean.setStatusCodeMap(exceptionStatusCodeMap());
		return bean;
	}
	
	private static Map<Class<? extends Throwable>,Integer> exceptionStatusCodeMap() {
		//@formatter:off
		return linkedMap(
				entry(IllegalArgumentException.class, HttpServletResponse.SC_BAD_REQUEST),
				entry(UnsupportedOperationException.class, HttpServletResponse.SC_NOT_IMPLEMENTED),
				entry(NotFoundException.class, HttpServletResponse.SC_NOT_FOUND),
				entry(AuthorizationException.class, HttpServletResponse.SC_FORBIDDEN),
				entry(SecurityServiceException.class, HttpServletResponse.SC_FORBIDDEN)
		);
		//@formatter:on
	}
	

}
