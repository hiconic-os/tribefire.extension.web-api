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
package com.braintribe.model.openapi.v3_0.export.legacytests.wire.space;

import com.braintribe.model.openapi.v3_0.api.OpenapiPropertiesRequest;
import com.braintribe.model.openapi.v3_0.export.PropertyOpenapiProcessor;
import com.braintribe.model.openapi.v3_0.export.legacytests.wire.contract.EntitiesOpenapiProcessorTestContract;
import com.braintribe.model.processing.service.common.ConfigurableDispatchingServiceProcessor;
import com.braintribe.wire.api.annotation.Managed;

@Managed
public class PropertiesOpenapiProcessorTestSpace extends AbstractOpenapiProcessorTestSpace implements EntitiesOpenapiProcessorTestContract {
	@Override
	protected void configureServices(ConfigurableDispatchingServiceProcessor bean) {
		bean.register(OpenapiPropertiesRequest.T, propertyOpenapiProcessor());
	}

	@Managed
	private PropertyOpenapiProcessor propertyOpenapiProcessor() {
		PropertyOpenapiProcessor bean = new PropertyOpenapiProcessor();

		bean.setCortexSessionFactory(cortexSessionSupplier());
		bean.setModelAccessoryFactory(modelAccessoryFactory());

		return bean;
	}
}
