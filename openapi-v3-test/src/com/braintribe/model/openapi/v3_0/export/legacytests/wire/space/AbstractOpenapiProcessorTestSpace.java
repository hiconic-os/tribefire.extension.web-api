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

import java.util.function.Supplier;

import com.braintribe.gm.service.wire.common.contract.CommonServiceProcessingContract;
import com.braintribe.gm.service.wire.common.contract.ServiceProcessingConfigurationContract;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.openapi.v3_0.export.legacytests.ioc.TestAccessSpace;
import com.braintribe.model.openapi.v3_0.export.legacytests.ioc.TestSessionFactory;
import com.braintribe.model.processing.service.common.ConfigurableDispatchingServiceProcessor;
import com.braintribe.model.processing.session.api.managed.ModelAccessoryFactory;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSessionFactory;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.testing.tools.gm.session.TestModelAccessoryFactory;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContextConfiguration;
import com.braintribe.wire.api.space.WireSpace;

@Managed
public abstract class AbstractOpenapiProcessorTestSpace implements WireSpace {
	@Import
	private CommonServiceProcessingContract commonServiceProcessing;

	@Import
	private ServiceProcessingConfigurationContract serviceProcessingConfiguration;

	@Override
	public void onLoaded(WireContextConfiguration configuration) {
		TestAccessSpace.cortexAccess();
		serviceProcessingConfiguration.registerServiceConfigurer(this::configureServicesBase);
	}

	protected void configureServicesBase(ConfigurableDispatchingServiceProcessor bean) {
		bean.removeInterceptor("auth");
		configureServices(bean);
	}

	protected abstract void configureServices(ConfigurableDispatchingServiceProcessor bean);

	public Evaluator<ServiceRequest> evaluator() {
		return commonServiceProcessing.evaluator();
	}

	protected ModelAccessoryFactory modelAccessoryFactory() {
		TestModelAccessoryFactory bean = new TestModelAccessoryFactory();

		bean.registerAccessModelAccessory("cortex", TestAccessSpace.cortexModel);
		bean.registerAccessModelAccessory("test.access", TestAccessSpace.testServiceModel);
		bean.registerServiceModelAccessory("test.access", TestAccessSpace.testServiceModel);
		bean.registerServiceModelAccessory("test.domain1", TestAccessSpace.testModel);
		bean.registerServiceModelAccessory("test.domain2", TestAccessSpace.testModel);

		return bean;
	}

	protected Supplier<PersistenceGmSession> cortexSessionSupplier() {
		return () -> sessionFactory().newSession("cortex");
	}

	protected PersistenceGmSessionFactory sessionFactory() {
		TestSessionFactory bean = new TestSessionFactory();
		bean.addAccess(TestAccessSpace.cortexAccess);
		return bean;
	}
}
