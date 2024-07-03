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
package tribefire.cortex.openapi_initializer.wire.contract;

import com.braintribe.model.ddra.DdraConfiguration;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.cortex.initializer.support.impl.lookup.GlobalId;
import tribefire.cortex.initializer.support.impl.lookup.InstanceLookup;

@InstanceLookup(lookupOnly = true)
public interface ExistingInstancesContract extends WireSpace {

	String TYPE_PREFIX = "type:";
	String MODEL_PREFIX = "model:";
	String OPENAPI_PACKAGE_PREFIX = "com.braintribe.model.openapi.v3_0.";
	String OPENAPI_API_TYPE_PREFIX = TYPE_PREFIX + OPENAPI_PACKAGE_PREFIX + "api.";

	@GlobalId("ddra:config")
	DdraConfiguration ddraConfiguration();

	@GlobalId(MODEL_PREFIX + "tribefire.extension.web-api:web-api-endpoints-model")
	GmMetaModel ddraEndpointsModel();

	@GlobalId(MODEL_PREFIX + "com.braintribe.gm:service-api-model")
	GmMetaModel serviceApiModel();

	@GlobalId(OPENAPI_API_TYPE_PREFIX + "OpenapiServicesRequest")
	GmEntityType openapiServicesRequestType();

	@GlobalId(OPENAPI_API_TYPE_PREFIX + "OpenapiEntitiesRequest")
	GmEntityType openapiEntitiesRequestType();

	@GlobalId(OPENAPI_API_TYPE_PREFIX + "OpenapiPropertiesRequest")
	GmEntityType openapiPropertiesRequestType();

}
