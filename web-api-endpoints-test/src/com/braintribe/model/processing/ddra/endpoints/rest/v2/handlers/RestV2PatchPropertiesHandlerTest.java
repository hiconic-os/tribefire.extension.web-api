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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.braintribe.model.accessapi.ManipulationResponse;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.AbstractRestV2Test;
import com.braintribe.testing.model.test.technical.features.ComplexEntity;
import com.braintribe.testing.model.test.technical.features.SimpleEntity;

public class RestV2PatchPropertiesHandlerTest extends AbstractRestV2Test {

	@BeforeClass
	public static void beforeClass() {
		setupServlet();

		RestV2PatchPropertiesHandler handler = new RestV2PatchPropertiesHandler();
		wireHandler(handler);
		addHandler("PATCH:properties", handler);
	}

	@AfterClass
	public static void afterClass() throws IOException {
		destroy();
	}

	@Before
	public void before() {
		resetServletAccess();
	}

	@Test
	public void noSessionId() {
		//@formatter:off
		requests.patch(getPathProperties(SimpleEntity.T, 0, "p0", "stringProperty"))
				.accept(JSON)
				.contentType(JSON)
				.body("\"New Value\"")
				.execute(401);
		//@formatter:on
	}

	@Test
	public void noBody() {
		//@formatter:off
		requests.patch(getPathProperties(SimpleEntity.T, 0, "stringProperty"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.contentType(JSON)
				.execute(200);
		//@formatter:on
	}

	@Test
	public void setPropertyWithEntitySimpleName() {
		//@formatter:off
		requests.patch(getPathProperties("SimpleEntity", 0, "p0", "stringProperty"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.contentType(JSON)
				.body("\"New Value\"")
				.execute(200);
		//@formatter:on

		SimpleEntity entity = getEntity(SimpleEntity.T, "stringProperty", "New Value");
		Assert.assertEquals(Long.valueOf(0), entity.getId());
		Assert.assertEquals("p0", entity.getPartition());
		Assert.assertEquals("New Value", entity.getStringProperty());
	}

	@Test
	public void setPropertyWithEntitySimpleNameAmbiguous() {
		//@formatter:off
		requests.patch(getPathProperties("DuplicateSimpleNameEntity", 0, "p0", "stringProperty"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.contentType(JSON)
				.body("\"New Value\"")
				.execute(400);
		//@formatter:on
	}

	@Test
	public void setStringProperty() {
		//@formatter:off
		boolean success = requests.patch(getPathProperties(SimpleEntity.T, 0, "p0", "stringProperty"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.contentType(JSON)
				.body("\"New Value\"")
				.execute(200);
		//@formatter:on

		Assert.assertTrue(success);

		SimpleEntity entity = getEntity(SimpleEntity.T, "stringProperty", "New Value");
		Assert.assertEquals(Long.valueOf(0), entity.getId());
		Assert.assertEquals("p0", entity.getPartition());
		Assert.assertEquals("New Value", entity.getStringProperty());
	}

	@Test
	public void setBooleanProperty() {
		SimpleEntity entityBefore = getEntity(SimpleEntity.T, "id", 0L);
		Assert.assertEquals(true, entityBefore.getBooleanProperty());

		//@formatter:off
		requests.patch(getPathProperties(SimpleEntity.T, 0, "p0", "booleanProperty"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.contentType(JSON)
				.body("false")
				.execute(200);
		//@formatter:on

		SimpleEntity entity = getEntity(SimpleEntity.T, "id", 0L);
		Assert.assertEquals(Long.valueOf(0), entity.getId());
		Assert.assertEquals("p0", entity.getPartition());
		Assert.assertEquals(false, entity.getBooleanProperty());
	}

	@Test
	public void setEntityProperty() {
		resetServletAccess(true);

		ComplexEntity entityBefore = getEntity(ComplexEntity.T, "id", 7L);
		Assert.assertEquals(null, entityBefore.getComplexEntityProperty());

		//@formatter:off
		requests.patch(getPathProperties(ComplexEntity.T, 7, "complexEntityProperty"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.contentType(JSON)
				// TODO is it ok to have to specify the type of the ID there?
				.body("{ \"id\": { \"_type\": \"long\", \"value\": 3 } }")
				.execute(200);
		//@formatter:on

		ComplexEntity entity = getEntity(ComplexEntity.T, "id", 7L);
		ComplexEntity targetEntity = getEntity(ComplexEntity.T, "id", 3L);
		Assert.assertEquals(Long.valueOf(7L), entity.getId());
		Assert.assertEquals(targetEntity.getGlobalId(), entity.getComplexEntityProperty().getGlobalId());
	}

	@Test
	public void withProjectionSuccess() {
		boolean success = deleteWithProjection("success");
		Assert.assertTrue(success);
	}

	@Test
	public void withProjectionEnvelope() {
		Object result = deleteWithProjection("envelope");
		Assert.assertTrue(result instanceof ManipulationResponse);
	}

	private <T> T deleteWithProjection(String projection) {
		//@formatter:off
		return requests.patch(getPathProperties(SimpleEntity.T, 0, "p0", "stringProperty"))
				.urlParameter("sessionId", "anything")
				.urlParameter("projection", projection)
				.accept(JSON)
				.contentType(JSON)
				.body("\"New Value\"")
				.execute(200);
		//@formatter:on
	}
}
