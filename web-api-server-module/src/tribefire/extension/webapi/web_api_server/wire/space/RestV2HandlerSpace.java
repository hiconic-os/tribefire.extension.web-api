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
package tribefire.extension.webapi.web_api_server.wire.space;

import java.util.HashMap;
import java.util.Map;

import com.braintribe.model.ddra.endpoints.v2.RestV2Endpoint;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers.AbstractRestV2Handler;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers.RestV2DeleteEntitiesHandler;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers.RestV2DeletePropertiesHandler;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers.RestV2GetEntitiesHandler;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers.RestV2GetPropertiesHandler;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers.RestV2Handler;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers.RestV2OptionsHandler;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers.RestV2PatchEntitiesHandler;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers.RestV2PatchPropertiesHandler;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers.RestV2PostEntitiesHandler;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers.RestV2PostPropertiesHandler;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers.RestV2PutEntitiesHandler;
import com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers.RestV2PutPropertiesHandler;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.module.wire.contract.TribefireWebPlatformContract;

@Managed
public class RestV2HandlerSpace implements WireSpace{
	
	@Import
	private TribefireWebPlatformContract tfPlatform;
	
	@Import
	private TcSpace tc;
	
	@Managed
	public Map<String, RestV2Handler<RestV2Endpoint>> handlers() {
		Map<String, RestV2Handler<?>> handlers = new HashMap<>();

		// entities
		handlers.put("GET:entities", getEntities());
		handlers.put("POST:entities", postEntities());
		handlers.put("PUT:entities", putEntities());
		handlers.put("PATCH:entities", patchEntities());
		handlers.put("DELETE:entities", deleteEntities());
		handlers.put("OPTIONS:entities", optionsHandler());

		// properties
		handlers.put("GET:properties", getProperties());
		handlers.put("POST:properties", postProperties());
		handlers.put("PUT:properties", putProperties());
		handlers.put("PATCH:properties", patchProperties());
		handlers.put("DELETE:properties", deleteProperties());
		handlers.put("OPTIONS:properties", optionsHandler());

		return (Map<String, RestV2Handler<RestV2Endpoint>>) (Map<?, ?>) handlers;
	}

	@Managed
	private RestV2GetEntitiesHandler getEntities() {
		RestV2GetEntitiesHandler handler = new RestV2GetEntitiesHandler();
		handler.setModelAccessoryFactory(tfPlatform.requestUserRelated().modelAccessoryFactory());
		configureHandlerCommons(handler);
		return handler;
	}

	@Managed
	private RestV2PostEntitiesHandler postEntities() {
		RestV2PostEntitiesHandler handler = new RestV2PostEntitiesHandler();
		handler.setSessionFactory(tfPlatform.systemUserRelated().sessionFactory());
		configureHandlerCommons(handler);
		return handler;
	}

	@Managed
	private RestV2PutEntitiesHandler putEntities() {
		RestV2PutEntitiesHandler handler = new RestV2PutEntitiesHandler();
		handler.setSessionFactory(tfPlatform.systemUserRelated().sessionFactory());
		configureHandlerCommons(handler);
		return handler;
	}

	@Managed
	private RestV2PatchEntitiesHandler patchEntities() {
		RestV2PatchEntitiesHandler handler = new RestV2PatchEntitiesHandler();
		handler.setSessionFactory(tfPlatform.systemUserRelated().sessionFactory());
		configureHandlerCommons(handler);
		return handler;
	}

	@Managed
	private RestV2DeleteEntitiesHandler deleteEntities() {
		RestV2DeleteEntitiesHandler handler = new RestV2DeleteEntitiesHandler();
		handler.setModelAccessoryFactory(tfPlatform.requestUserRelated().modelAccessoryFactory());
		configureHandlerCommons(handler);
		return handler;
	}

	@Managed
	private RestV2OptionsHandler optionsHandler() {
		RestV2OptionsHandler handler = new RestV2OptionsHandler();
		return handler;
	}

	@Managed
	private RestV2GetPropertiesHandler getProperties() {
		RestV2GetPropertiesHandler handler = new RestV2GetPropertiesHandler();
		configureHandlerCommons(handler);
		return handler;
	}

	@Managed
	private RestV2PostPropertiesHandler postProperties() {
		RestV2PostPropertiesHandler handler = new RestV2PostPropertiesHandler();
		configureHandlerCommons(handler);
		return handler;
	}

	@Managed
	private RestV2PutPropertiesHandler putProperties() {
		RestV2PutPropertiesHandler handler = new RestV2PutPropertiesHandler();
		configureHandlerCommons(handler);
		return handler;
	}

	@Managed
	private RestV2PatchPropertiesHandler patchProperties() {
		RestV2PatchPropertiesHandler handler = new RestV2PatchPropertiesHandler();
		configureHandlerCommons(handler);
		return handler;
	}

	@Managed
	private RestV2DeletePropertiesHandler deleteProperties() {
		RestV2DeletePropertiesHandler handler = new RestV2DeletePropertiesHandler();
		configureHandlerCommons(handler);
		return handler;
	}
	
	private void configureHandlerCommons(AbstractRestV2Handler<?> handler) {
		handler.setEvaluator(tfPlatform.requestUserRelated().evaluator());
		handler.setMarshallerRegistry(tfPlatform.marshalling().registry());
		handler.setTraversingCriteriaMap(tc.criteriaMap());
	}

}
