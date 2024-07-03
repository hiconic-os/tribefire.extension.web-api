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

import static com.braintribe.testing.junit.assertions.gm.assertj.core.api.GmAssertions.assertThat;
import static com.braintribe.utils.lcd.CollectionTools2.asList;

import java.io.IOException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.AbstractRestV2Test;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.testing.model.test.technical.features.ComplexEntity;
import com.braintribe.testing.tools.gm.GmTestTools;

public class RestV2GetPropertiesHandlerXtraTest extends AbstractRestV2Test {

	@BeforeClass
	public static void beforeClass() {
		setupServlet();

		RestV2GetPropertiesHandler handler = new RestV2GetPropertiesHandler();
		wireHandler(handler);
		addHandler("GET:properties", handler);
	}

	@Before
	public void before() {
		// This means the access will contain no data
		resetServletAccess(false, false);
	}

	@AfterClass
	public static void afterClass() throws IOException {
		destroy();
	}

	@Test
	public void listPropertyWithDepth() {
		if (!AbstractQueryingHandler.DEPTH_ENABLED)
			return;

		addData(access);

		//@formatter:off
		List<ComplexEntity> results = requests.get()
				.path(getPathProperties(ComplexEntity.T, 1L, "part", "complexEntityList"))
				.urlParameter("sessionId", "anything")
				.urlParameter("depth", "1")
				.accept(JSON)
				.execute(200);
		//@formatter:on

		ComplexEntity first = results.get(0);
		ComplexEntity second = results.get(1);
		ComplexEntity third = results.get(2);

		ComplexEntity _second = first.getComplexEntityProperty();
		assertThat(_second).isSameAs(second);

		ComplexEntity _third = second.getComplexEntityProperty();
		assertThat(_third).isSameAs(third);
	}

	private void addData(IncrementalAccess access) {
		PersistenceGmSession session = GmTestTools.newSession(access);

		ComplexEntity main = newComplexEntity(session, "main");
		main.setId(1L);
		main.setPartition("part");

		ComplexEntity first = newComplexEntity(session, "first");
		ComplexEntity second = newComplexEntity(session, "second");
		ComplexEntity third = newComplexEntity(session, "third");

		first.setComplexEntityProperty(second);
		second.setComplexEntityProperty(third);

		main.getComplexEntityList().addAll(asList(first, second, third));

		session.commit();
	}

	private ComplexEntity newComplexEntity(PersistenceGmSession session, String identifier) {
		ComplexEntity result = session.create(ComplexEntity.T);
		result.setStringProperty(identifier);
		return result;
	}

}
