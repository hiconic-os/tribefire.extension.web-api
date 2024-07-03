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
package tribefire.extension.webapi.web_api_client.wire.space;

import com.braintribe.model.deployment.http.processor.ConfigurableHttpServiceProcessor;
import com.braintribe.model.deployment.http.processor.DynamicHttpServiceProcessor;
import com.braintribe.model.processing.deployment.api.binding.DenotationBindingBuilder;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;

/**
 * This module's javadoc is yet to be written.
 */
@Managed
public class WebApiClientModuleSpace implements TribefireModuleContract {

	@Import
	private WebApiClientServiceProcessorSpace webApiClientServiceProcessor;
	
	@Import
	private TribefireWebPlatformContract tfPlatform;


	//
	// Deployables
	//

	@Override
	public void bindDeployables(DenotationBindingBuilder bindings) {
		bindings.bind(ConfigurableHttpServiceProcessor.T) //
			.component(tfPlatform.binders().serviceProcessor()) //
			.expertFactory(webApiClientServiceProcessor::configurableServiceProcessor);
		
		bindings.bind(DynamicHttpServiceProcessor.T) //
			.component(tfPlatform.binders().serviceProcessor()) //
			.expertFactory(webApiClientServiceProcessor::metaDataMappedServiceProcessor);
	}

}
