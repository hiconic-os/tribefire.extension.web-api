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
package com.braintribe.model.processing.ddra.endpoints.api.v1;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;

import org.junit.Test;

import com.braintribe.model.ddra.DdraMapping;
import com.braintribe.model.ddra.DdraUrlMethod;
import com.braintribe.model.processing.ddra.endpoints.TestHttpRequest;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.TestServiceRequestWithEntityProperty;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.ZipRequestSimple;
import com.braintribe.model.prototyping.api.StaticPrototyping;
import com.braintribe.model.resource.Icon;
import com.braintribe.model.service.api.result.Failure;
import com.braintribe.model.user.User;

public class ApiV1RestServletExtendedTest extends AbstractApiV1RestServletTest {

	@Test
	public void testEmbedded() {
		System.out.println("Testing handling of Embedded metadata in POST method using QUERY parameter:");
		testEmbedded(() -> requests.servicePost(TestServiceRequestWithEntityProperty.T).testUrlParams().contentType("application/x-www-form-urlencoded"));
		System.out.println("Testing handling of Embedded metadata in POST method using HEADER parameter:");
		testEmbedded(() -> requests.servicePost(TestServiceRequestWithEntityProperty.T).testHeaderParams().contentType("application/x-www-form-urlencoded"));
		
		System.out.println("Testing handling of Embedded metadata in GET method using QUERY parameter:");
		testEmbedded(() -> requests.serviceGet(TestServiceRequestWithEntityProperty.T).testUrlParams());
		System.out.println("Testing handling of Embedded metadata in GET method using HEADER parameter:");
		testEmbedded(() -> requests.serviceGet(TestServiceRequestWithEntityProperty.T).testHeaderParams());
		
		System.out.println("Done.");
	}
	
	public void testEmbedded(Supplier<TestHttpRequest> testHttpRequestSupplier) {

		TestServiceRequestWithEntityProperty result = testHttpRequestSupplier.get()
				.execute(200);
		
		assertThat(result.getZipRequest()).isNull();
		assertThat(result.getUser()).isNull();
		
		// ========== Second case ===========
		
		result = testHttpRequestSupplier.get()
				.paramViaHeaderOrUrl("zipRequest.resource.name", "Allegro scherzando")
				.execute(200);
		
		// There are two Embedded metadata set in the cortex access:
		// on TestServiceRequestWithEntityProperty.zipRequest and
		// on TestServiceRequestWithEntityProperty.zipRequest.resource
		// we only set the deep nested embedded property (TestServiceRequestWithEntityProperty.zipRequest.resource)
		// expecting that it connects the whole chain of parents by setting the respective properties
		ZipRequestSimple zipRequest = result.getZipRequest();
		assertThat(zipRequest).isNotNull();
		assertThat(zipRequest.getResource()).isNotNull();
		assertThat(zipRequest.getName()).isNull();
		assertThat(zipRequest.getResource().getName()).isEqualTo("Allegro scherzando");
		
		// There is no Embedded metadata on the user property so it should not have been created
		assertThat(result.getUser()).isNull();
		
		// ========== Third case ===========
		
		result = testHttpRequestSupplier.get()
				.paramViaHeaderOrUrl("zipRequest.name", "Vasily Kalinnikov")
				.paramViaHeaderOrUrl("zipRequest.resource.name", "Allegro scherzando")
				.execute(200);
		
		
		// This case is very similar to the previous one but setting now all embedded properties at once
		zipRequest = result.getZipRequest();
		assertThat(zipRequest).isNotNull();
		assertThat(zipRequest.getResource()).isNotNull();
		assertThat(zipRequest.getName()).isEqualTo("Vasily Kalinnikov");
		assertThat(zipRequest.getResource().getName()).isEqualTo("Allegro scherzando");
		
		// ========== Fourth case ===========
		
		// There is no Embedded metadata on the 'user' property so it should not have been created
		// This means that setting it via the property path notation is illegal

		Failure failure = testHttpRequestSupplier.get()
				.paramViaHeaderOrUrl("user.name", "Vasily Kalinnikov")
				.execute(400);
		
		assertThat(failure).isNotNull();
	}
	
	@Test
	public void testPrototype() {
		TestServiceRequestWithEntityProperty prototype = TestServiceRequestWithEntityProperty.T.create();
		User user = User.T.create();
		user.setName("Barkeeper");
		Icon picture = Icon.T.create();
		picture.setName("Bar");
		user.setPicture(picture);
		
		prototype.setUser(user);
		
		DdraMapping mapping = mapping("/test-request", DdraUrlMethod.POST, TestServiceRequestWithEntityProperty.T);
		
		StaticPrototyping requestPrototyping = StaticPrototyping.T.create();
		requestPrototyping.setPrototype(prototype);
		
		mapping.setRequestPrototyping(requestPrototyping );
		setMappings(mapping);
		
		TestServiceRequestWithEntityProperty result = requests.post("tribefire-services/api/v1/test-request")
			.execute(200);
		
		assertThat(result.getUser().getName())
			.isEqualTo(user.getName());
		
		assertThat(result.getUser().getPicture().getName())
			.isEqualTo(picture.getName());
	}
}
