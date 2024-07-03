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
package com.braintribe.model.processing.ddra.endpoints.rest.v2;

import com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers.RestV2GetEntitiesHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class RestV2RestServletTest extends AbstractRestV2Test {

	@BeforeClass
	public static void beforeClass() {
		setupServlet();

		RestV2GetEntitiesHandler handler = new RestV2GetEntitiesHandler();
		handler.setEvaluator(evaluator);
		handler.setMarshallerRegistry(marshallerRegistry);
		addHandler("GET:entities", handler);
	}

	@AfterClass
	public static void afterClass() throws IOException {
		destroy();
	}

	@Test
	public void unsupportedUrl() {
		requests.get("/tribefire-services/rest/v2/incorrect/test.access/testEntity").accept(JSON).execute(404);
	}
}
