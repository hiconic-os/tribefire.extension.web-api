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
package tribefire.cortex.openapi_initializer.wire.space;

import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.initializer.support.integrity.wire.contract.CoreInstancesContract;
import tribefire.cortex.openapi_initializer.wire.contract.ExistingInstancesContract;
import tribefire.cortex.openapi_initializer.wire.contract.OpenapiInitializerModuleContract;
import tribefire.cortex.openapi_initializer.wire.contract.OpenapiInitializerModuleMainContract;
import tribefire.cortex.openapi_initializer.wire.contract.OpenapiInitializerModuleModelsContract;
import tribefire.cortex.openapi_initializer.wire.contract.RuntimePropertiesContract;

@Managed
public class OpenapiInitializerModuleMainSpace implements OpenapiInitializerModuleMainContract {

	@Import
	private OpenapiInitializerModuleContract initializer;

	@Import
	private OpenapiInitializerModuleModelsContract models;

	@Import
	private ExistingInstancesContract existingInstances;

	@Import
	private RuntimePropertiesContract properties;

	@Import
	private CoreInstancesContract coreInstances;

	@Override
	public OpenapiInitializerModuleContract initializer() {
		return initializer;
	}

	@Override
	public OpenapiInitializerModuleModelsContract models() {
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
