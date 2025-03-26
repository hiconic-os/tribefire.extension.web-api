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
package com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.braintribe.model.processing.ddra.endpoints.rest.v2.AbstractRestV2Test;
import com.braintribe.model.query.PropertyQueryResult;
import com.braintribe.testing.model.test.technical.features.SimpleEntity;

public class RestV2GetPropertiesHandlerTest extends AbstractRestV2Test {

	@BeforeClass
	public static void beforeClass() {
		setupServlet();

		RestV2GetPropertiesHandler handler = new RestV2GetPropertiesHandler();
		wireHandler(handler);
		addHandler("GET:properties", handler);
	}

	@AfterClass
	public static void afterClass() throws IOException {
		destroy();
	}

	@Test
	public void simpleProperty() {
		//@formatter:off
		String value = requests.get()
				.path(getPathProperties(SimpleEntity.T, 4, "p0", "stringProperty"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.execute(200);
		//@formatter:on

		Assert.assertEquals("se4", value);
	}

	@Test
	public void byEntitySimpleName() {
		//@formatter:off
		String value = requests.get()
				.path(getPathProperties("SimpleEntity", 4, "p0", "stringProperty"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.execute(200);
		//@formatter:on

		Assert.assertEquals("se4", value);
	}

	@Test
	public void byEntitySimpleNameAmbiguous() {
		//@formatter:off
		requests.get()
				.path(getPathProperties("DuplicateSimpleNameEntity", 4, "p0", "stringProperty"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.execute(400);
		//@formatter:on
	}

	@Test
	public void propertyDoesNotExist() {
		//@formatter:off
		requests.get()
				.path(getPathProperties(SimpleEntity.T, 4, "p0", "thisPropertyDoesNotExist"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.execute(404);
		//@formatter:on
	}

	@Test
	public void noSessionId() {
		//@formatter:off
		requests.get()
				.path(getPathProperties(SimpleEntity.T, 4, "p0", "stringProperty"))
				.accept(JSON)
				.execute(401);
		//@formatter:on

	}

	@Test
	public void withProjectionValue() {
		//@formatter:off
		Object value = requests.get()
				.path(getPathProperties(SimpleEntity.T, 4, "p0", "stringProperty"))
				.urlParameter("sessionId", "anything")
				.urlParameter("projection", "value")
				.accept(JSON)
				.execute(200);
		//@formatter:on

		Assert.assertTrue(value instanceof String);
	}

	@Test
	public void withProjectionEnvelope() {
		//@formatter:off
		Object value = requests.get()
				.path(getPathProperties(SimpleEntity.T, 4, "p0", "stringProperty"))
				.urlParameter("sessionId", "anything")
				.urlParameter("projection", "envelope")
				.accept(JSON)
				.execute(200);
		//@formatter:on

		Assert.assertTrue(value instanceof PropertyQueryResult);
	}
}
