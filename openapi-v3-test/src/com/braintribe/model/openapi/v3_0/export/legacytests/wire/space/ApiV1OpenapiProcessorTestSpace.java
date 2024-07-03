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

import com.braintribe.model.ddra.DdraConfiguration;
import com.braintribe.model.openapi.v3_0.api.OpenapiServicesRequest;
import com.braintribe.model.openapi.v3_0.export.ApiV1OpenapiProcessor;
import com.braintribe.model.openapi.v3_0.export.legacytests.wire.contract.ApiV1OpenapiProcessorTestContract;
import com.braintribe.model.processing.service.common.ConfigurableDispatchingServiceProcessor;
import com.braintribe.wire.api.annotation.Managed;

@Managed
public class ApiV1OpenapiProcessorTestSpace extends AbstractOpenapiProcessorTestSpace implements ApiV1OpenapiProcessorTestContract {

	@Override
	protected void configureServices(ConfigurableDispatchingServiceProcessor bean) {
		bean.register(OpenapiServicesRequest.T, openApiV1Processor());
	}

	@Managed
	private ApiV1OpenapiProcessor openApiV1Processor() {
		ApiV1OpenapiProcessor bean = new ApiV1OpenapiProcessor();
		bean.setDdraConfiguration(ddraConfiguration());
		bean.setModelAccessoryFactory(modelAccessoryFactory());
		bean.setCortexSessionFactory(cortexSessionSupplier());
		bean.setAutoUpdateDdraMappings(false);
		return bean;
	}

	@Override
	@Managed
	public DdraConfiguration ddraConfiguration() {
		return DdraConfiguration.T.create();
	}
}
