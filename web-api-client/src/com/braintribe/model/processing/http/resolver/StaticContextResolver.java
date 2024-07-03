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
package com.braintribe.model.processing.http.resolver;

import com.braintribe.model.processing.meta.cmd.builders.ModelMdResolver;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.session.api.managed.ModelAccessory;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.processing.http.client.HttpClient;

public class StaticContextResolver extends AbstractContextResolver {

	private HttpClient httpClient;
	private ModelAccessory modelAccessory;

	// ***************************************************************************************************
	// Setter
	// ***************************************************************************************************

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	public void setModelAccessory(ModelAccessory modelAccessory) {
		this.modelAccessory = modelAccessory;
	}

	// ***************************************************************************************************
	// ContextResolver
	// ***************************************************************************************************

	@Override
	protected HttpClient getHttpClient(RequestContextResolver contextResolver) {
		return httpClient;
	}
	
	@Override
	protected ModelMdResolver getModelResolver(ServiceRequestContext serviceContext, ServiceRequest serviceRequest) {
		return this.modelAccessory.getMetaData().useCases(resolverUseCases);
	}
}
