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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.braintribe.model.accessapi.ManipulationResponse;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.processing.ddra.endpoints.TestHttpRequest;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.AbstractRestV2Test;
import com.braintribe.testing.model.test.technical.features.ComplexEntity;
import com.braintribe.testing.model.test.technical.features.PrimitiveTypesEntity;
import com.braintribe.utils.lcd.CollectionTools;

public class RestV2DeletePropertiesHandlerTest extends AbstractRestV2Test {

	@BeforeClass
	public static void beforeClass() {
		setupServlet();

		RestV2DeletePropertiesHandler handler = new RestV2DeletePropertiesHandler();
		wireHandler(handler);
		addHandler("DELETE:properties", handler);
	}

	@AfterClass
	public static void afterClass() throws IOException {
		destroy();
	}

	@Before
	public void before() {
		resetServletAccess(true);
	}

	@Test
	public void stringProperty() {
		ComplexEntity entity = getEntity(ComplexEntity.T, "id", 0L);
		Assert.assertNotNull(entity.getStringProperty());

		//@formatter:off
		boolean result = requests.delete()
				.path(getPathProperties(ComplexEntity.T, 0, "stringProperty"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.execute(200);
		//@formatter:on

		entity = getEntity(ComplexEntity.T, "id", 0L);
		Assert.assertNull(entity.getStringProperty());
		Assert.assertTrue(result);
	}

	@Test
	public void intProperty() {
		setProperty(PrimitiveTypesEntity.T, 0, "primitiveIntegerProperty", 42);

		//@formatter:off
		boolean result = requests.delete()
				.path(getPathProperties(PrimitiveTypesEntity.T, 0, "primitiveIntegerProperty"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.execute(200);
		//@formatter:on

		PrimitiveTypesEntity entity = getEntity(PrimitiveTypesEntity.T, "id", 0L);
		Assert.assertEquals(0, entity.getPrimitiveIntegerProperty());
		Assert.assertTrue(result);
	}

	@Test
	public void stringPropertyWithEntitySimpleName() {
		ComplexEntity entity = getEntity(ComplexEntity.T, "id", 0L);
		Assert.assertNotNull(entity.getStringProperty());

		//@formatter:off
		boolean result = requests.delete()
				.path(getPathProperties("ComplexEntity", 0, "stringProperty"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.execute(200);
		//@formatter:on

		entity = getEntity(ComplexEntity.T, "id", 0L);
		Assert.assertNull(entity.getStringProperty());
		Assert.assertTrue(result);
	}

	@Test
	public void complexEntityPropertyListWithEntitySimpleName() {
		setProperty(ComplexEntity.T, 0, "stringList", CollectionTools.getList("1", "2"));
		ComplexEntity entity = getEntity(ComplexEntity.T, "id", 0L);
		Assert.assertEquals(2, entity.getStringList().size());

		//@formatter:off
		boolean result = requests.delete()
				.path(getPathProperties("ComplexEntity", 0, "stringList"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.execute(200);
		//@formatter:on

		Assert.assertTrue(result);
		entity = getEntity(ComplexEntity.T, "id", 0L);
		Assert.assertEquals(0, entity.getStringList().size());
	}

	@Test
	public void complexEntityPropertyMapWithEntitySimpleName() {
		List<GenericEntity> entities = getEntities(ComplexEntity.T, "integerProperty", 2);

		Map<Object, GenericEntity> map = entities.stream().collect(Collectors.toMap(GenericEntity::getId, Function.identity()));
		setProperty(ComplexEntity.T, 0, "complexEntityMap", map);
		ComplexEntity entity = getEntity(ComplexEntity.T, "id", 0L);
		Assert.assertEquals(3, entity.getComplexEntityMap().size());

		//@formatter:off
		boolean result = requests.delete()
				.path(getPathProperties("ComplexEntity", 0, "complexEntityMap"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.execute(200);
		//@formatter:on

		Assert.assertTrue(result);
		entity = getEntity(ComplexEntity.T, "id", 0L);
		Assert.assertEquals(0, entity.getComplexEntityMap().size());
	}

	@Test
	public void stringPropertyWithEntitySimpleNameAmbiguous() {
		//@formatter:off
		requests.delete()
				.path(getPathProperties("DuplicateSimpleNameEntity", 0, "stringProperty"))
				.urlParameter("sessionId", "anything")
				.accept(JSON)
				.execute(400);
		//@formatter:on
	}

	@Test
	public void withProjectionEnvelope() {
		Object result = deleteWithProjection("envelope");

		Assert.assertTrue(result instanceof ManipulationResponse);
	}

	@Test
	public void withProjectionSuccess() {
		boolean result = deleteWithProjection("success");

		Assert.assertTrue(result);
	}

	private <T> T deleteWithProjection(String projection) {
		TestHttpRequest request = requests.delete(getPathProperties(ComplexEntity.T, 0, "stringProperty")).urlParameter("sessionId", "anything")
				.urlParameter("projection", projection).accept(JSON);

		return request.execute(200);
	}

}
