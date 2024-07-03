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
package tribefire.extension.webapi.web_api_server_initializer.wire.space;

import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.initializer.support.integrity.wire.contract.CoreInstancesContract;

import tribefire.extension.webapi.web_api_server_initializer.wire.contract.WebApiServerInitializerContract;
import tribefire.extension.webapi.web_api_server_initializer.wire.contract.WebApiServerInitializerMainContract;
import tribefire.extension.webapi.web_api_server_initializer.wire.contract.WebApiServerInitializerModelsContract;
import tribefire.extension.webapi.web_api_server_initializer.wire.contract.ExistingInstancesContract;
import tribefire.extension.webapi.web_api_server_initializer.wire.contract.RuntimePropertiesContract;

@Managed
public class WebApiServerInitializerMainSpace implements WebApiServerInitializerMainContract {

	@Import
	private WebApiServerInitializerContract initializer;
	
	@Import
	private WebApiServerInitializerModelsContract models;
	
	@Import
	private ExistingInstancesContract existingInstances;
	
	@Import
	private RuntimePropertiesContract properties;
	
	@Import
	private CoreInstancesContract coreInstances;
	
	@Override
	public WebApiServerInitializerContract initializer() {
		return initializer;
	}

	@Override
	public WebApiServerInitializerModelsContract models() {
		return models;
	}

	@Override
	public ExistingInstancesContract existingInstances() {
		return existingInstances;
	}
	
	@Override
	public RuntimePropertiesContract properties() {
		return properties;
	}
	
	@Override
	public CoreInstancesContract coreInstances() {
		return coreInstances;
	}
}
