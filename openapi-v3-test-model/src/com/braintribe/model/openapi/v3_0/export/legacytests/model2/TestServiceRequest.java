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
package com.braintribe.model.openapi.v3_0.export.legacytests.model2;

import java.math.BigDecimal;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;

public interface TestServiceRequest extends ServiceRequest {

	final EntityType<TestServiceRequest> T = EntityTypes.T(TestServiceRequest.class);

	@Override
	EvalContext<? extends TestServiceResponse> eval(Evaluator<ServiceRequest> evaluator);

	@Mandatory
	String getMandatoryProperty();
	void setMandatoryProperty(String mandatoryProperty);

	@Initializer("'test value'")
	String getPropertyWithInitializer();
	void setPropertyWithInitializer(String propertyWithInitializer);

	@Initializer("42")
	int getIntProperty();
	void setIntProperty(int intProperty);

	BigDecimal getBigDecimalProperty();
	void setBigDecimalProperty(BigDecimal bigDecimalProperty);

}
