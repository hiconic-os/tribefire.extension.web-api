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

import java.util.function.Function;

import com.braintribe.cfg.Configurable;
import com.braintribe.cfg.Required;
import com.braintribe.model.deployment.http.meta.HttpProcessWith;
import com.braintribe.model.processing.meta.cmd.builders.ModelMdResolver;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.session.api.managed.ModelAccessory;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryFactory;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.processing.http.client.HttpClient;

public class DynamicContextResolver extends AbstractContextResolver {

	private ModelAccessoryFactory modelAccessoryFactory;
	private Function<com.braintribe.model.deployment.http.client.HttpClient, HttpClient> clientResolver;
	
	// ***************************************************************************************************
	// Setter
	// ***************************************************************************************************

	@Required
	@Configurable
	public void setModelAccessoryFactory(ModelAccessoryFactory modelAccessoryFactory) {
		this.modelAccessoryFactory = modelAccessoryFactory;
	}

	@Required
	@Configurable
	public void setClientResolver(Function<com.braintribe.model.deployment.http.client.HttpClient, HttpClient> clientResolver) {
		this.clientResolver = clientResolver;
	}

	// ***************************************************************************************************
	// Context Resolver
	// ***************************************************************************************************

	@Override
	protected HttpClient getHttpClient(RequestContextResolver contextResolver) {
		ServiceRequest serviceRequest = contextResolver.serviceRequest;
		HttpProcessWith processWith = contextResolver.modelResolver.entity(serviceRequest).meta(HttpProcessWith.T).exclusive();
		if (processWith == null) {
			throw new IllegalArgumentException("No HttpProcessWith configured for request: "+serviceRequest);
		}
		com.braintribe.model.deployment.http.client.HttpClient clientDennotation = processWith.getClient();
		if (clientDennotation == null) {
			throw new IllegalArgumentException("No HttpClient configured for request: "+serviceRequest);
		}
		return clientResolver.apply(clientDennotation);

	}
	
	@Override
	protected ModelMdResolver getModelResolver(ServiceRequestContext serviceContext, ServiceRequest serviceRequest) {
		String domainId = serviceContext.getDomainId();
		ModelAccessory modelAccessory = modelAccessoryFactory.getForServiceDomain(domainId);
		return modelAccessory.getMetaData().useCases(resolverUseCases);

	}
}
