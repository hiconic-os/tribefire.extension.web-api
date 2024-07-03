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
package com.braintribe.model.processing.ddra.endpoints.wire.space;

import com.braintribe.gm.service.wire.common.contract.CommonServiceProcessingContract;
import com.braintribe.gm.service.wire.common.contract.ServiceProcessingConfigurationContract;
import com.braintribe.model.accessapi.GmqlRequest;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.processing.ddra.endpoints.api.v1.ResponseCodeOverridingProcessor;
import com.braintribe.model.processing.ddra.endpoints.api.v1.SimpleZipperProcessor;
import com.braintribe.model.processing.ddra.endpoints.api.v1.TestAmbigousNestingProcessor;
import com.braintribe.model.processing.ddra.endpoints.api.v1.TestReasonServiceProcessor;
import com.braintribe.model.processing.ddra.endpoints.api.v1.ZipperProcessor;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.BasicTestServiceRequest;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.NeutralRequest;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.NullRequest;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.ResponseCodeOverridingRequest;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.TestAmbigiousNestingRequest;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.TestReasoningServiceRequest;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.ZipRequest;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.ZipRequestSimple;
import com.braintribe.model.processing.ddra.endpoints.wire.contract.DdraTestContract;
import com.braintribe.model.processing.service.common.ConfigurableDispatchingServiceProcessor;
import com.braintribe.model.processing.service.common.IdentityServiceProcessor;
import com.braintribe.model.prototyping.api.StaticPrototyping;
import com.braintribe.model.prototyping.impl.StaticPrototypingProcessor;
import com.braintribe.model.securityservice.OpenUserSessionWithUserAndPassword;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.model.service.api.result.Neutral;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContextConfiguration;

@Managed
public class DdraTestSpace implements DdraTestContract{

	@Import
	private ServiceProcessingConfigurationContract serviceProcessingConfiguration;
	
	@Import
	private CommonServiceProcessingContract commonServiceProcessing;
	
	@Override
	public void onLoaded(WireContextConfiguration configuration) {
		serviceProcessingConfiguration.registerServiceConfigurer(this::configureServices);
	}
	
	private void configureServices(ConfigurableDispatchingServiceProcessor bean) {
		bean.removeInterceptor("auth");
	
		//bean.register(Authenticate.T, IdentityServiceProcessor.instance());
		bean.register(GmqlRequest.T, IdentityServiceProcessor.instance());
		bean.register(OpenUserSessionWithUserAndPassword.T, IdentityServiceProcessor.instance());
		bean.register(BasicTestServiceRequest.T, IdentityServiceProcessor.instance());
		bean.register(ZipRequest.T, new ZipperProcessor());
		bean.register(ZipRequestSimple.T, new SimpleZipperProcessor());
		bean.register(StaticPrototyping.T, new StaticPrototypingProcessor());
		bean.register(ResponseCodeOverridingRequest.T, new ResponseCodeOverridingProcessor());
		bean.register(NullRequest.T, (c, r) -> null);
		bean.register(NeutralRequest.T, (c, r) -> Neutral.NEUTRAL);
		bean.register(TestReasoningServiceRequest.T, new TestReasonServiceProcessor());
		bean.register(TestAmbigiousNestingRequest.T, new TestAmbigousNestingProcessor());
	}
	
	@Override
	public Evaluator<ServiceRequest> serviceRequestEvaluator() {
		return commonServiceProcessing.evaluator();
	}
}
