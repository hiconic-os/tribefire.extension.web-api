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
package tribefire.cortex.openapi_v3.wire.space;

import com.braintribe.model.openapi.servlets.OpenapiUiServlet;
import com.braintribe.model.openapi.v3_0.export.ApiV1OpenapiProcessor;
import com.braintribe.model.openapi.v3_0.export.EntityOpenapiProcessor;
import com.braintribe.model.openapi.v3_0.export.PropertyOpenapiProcessor;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

import tribefire.module.wire.contract.RequestUserRelatedContract;
import tribefire.module.wire.contract.SystemUserRelatedContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;
import tribefire.module.wire.contract.WebPlatformResourcesContract;

/**
 * This space class hosts configuration of deployables based on their denotation types.
 */
@Managed
public class OpenapiV3DeployablesSpace implements WireSpace {

	@Import
	private TribefireWebPlatformContract tfPlatform;
	@Import
	private RequestUserRelatedContract requestUserRelated;
	@Import
	private SystemUserRelatedContract systemUserRelated;

	@Import
	private WebPlatformResourcesContract resources;

	@Managed
	public OpenapiUiServlet swaggerUi() {
		OpenapiUiServlet bean = new OpenapiUiServlet();
		bean.setTemplateLocation("com/braintribe/model/openapi/servlets/openapi_ui.vm");
		bean.setCortexSessionFactory(systemUserRelated.cortexSessionSupplier());
		bean.setModelAccessoryFactory(requestUserRelated.modelAccessoryFactory());

		return bean;
	}

	@Managed
	public ApiV1OpenapiProcessor openapiServicesProcessor() {
		ApiV1OpenapiProcessor bean = new ApiV1OpenapiProcessor();

		bean.setCortexSessionFactory(systemUserRelated.cortexSessionSupplier());
		bean.setModelAccessoryFactory(requestUserRelated.modelAccessoryFactory());

		return bean;
	}

	@Managed
	public EntityOpenapiProcessor openapiEntitiesProcessor() {
		EntityOpenapiProcessor bean = new EntityOpenapiProcessor();

		bean.setCortexSessionFactory(systemUserRelated.cortexSessionSupplier());
		bean.setModelAccessoryFactory(requestUserRelated.modelAccessoryFactory());

		return bean;
	}

	@Managed
	public PropertyOpenapiProcessor openapiPropertiesProcessor() {
		PropertyOpenapiProcessor bean = new PropertyOpenapiProcessor();

		bean.setCortexSessionFactory(systemUserRelated.cortexSessionSupplier());
		bean.setModelAccessoryFactory(requestUserRelated.modelAccessoryFactory());

		return bean;
	}

}
