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

import static com.braintribe.web.api.registry.WebRegistries.servlet;

import com.braintribe.model.openapi.v3_0.api.OpenapiEntitiesRequest;
import com.braintribe.model.openapi.v3_0.api.OpenapiPropertiesRequest;
import com.braintribe.model.openapi.v3_0.api.OpenapiServicesRequest;
import com.braintribe.model.service.domain.ServiceDomain;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.openapi_v3.impl.OpenApiLandingPageLinkConfigurer;
import tribefire.module.api.WebRegistryConfiguration;
import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;

@Managed
public class OpenapiV3ModuleSpace implements TribefireModuleContract {

	private static final String CORTEX = "cortex";

	@Import
	private TribefireWebPlatformContract tfPlatform;
	
	@Import
	private OpenapiV3DeployablesSpace deployables;

	@Override
	public void bindHardwired() {
		WebRegistryConfiguration webRegistry = tfPlatform.hardwiredDeployables().webRegistry();
		
		webRegistry.addServlet(
			servlet()
				.name("Openapi UI")
				.instance(deployables.swaggerUi())
				.pattern("/openapi/ui/*")
			);
		
		webRegistry.loginRedirectingAuthFilter().addPattern("/openapi/ui/*");
		
		tfPlatform.hardwiredDeployables().bindOnExistingServiceDomain(CORTEX) //
			.serviceProcessor( //
					"openapi.processor.services", // 
					"openapi processor.services",  //
					OpenapiServicesRequest.T, //
					deployables.openapiServicesProcessor() //
			) //
			.serviceProcessor( //
					"openapi.processor.entities", // 
					"openapi processor.entities",  //
					OpenapiEntitiesRequest.T, //
					deployables.openapiEntitiesProcessor() //
			) //
			.serviceProcessor( //
					"openapi.processor.properties", // 
					"openapi processor.properties",  //
					OpenapiPropertiesRequest.T, //
					deployables.openapiPropertiesProcessor() //
			) //
			.please();
		
		tfPlatform.hardwiredExperts().bindLandingPageLinkConfigurer(null, ServiceDomain.T, openApiLandingPageLinkConfigurer());
	}
	
	@Managed
	private OpenApiLandingPageLinkConfigurer openApiLandingPageLinkConfigurer() {
		OpenApiLandingPageLinkConfigurer bean = new OpenApiLandingPageLinkConfigurer();
		bean.setModelAccessoryFactory(tfPlatform.requestUserRelated().modelAccessoryFactory());
		return bean;
	}

}
