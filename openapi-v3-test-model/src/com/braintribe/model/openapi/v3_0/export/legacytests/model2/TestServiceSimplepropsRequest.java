// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.model.openapi.v3_0.export.legacytests.model2;

import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;

public interface TestServiceSimplepropsRequest extends ServiceRequest {

	EntityType<TestServiceSimplepropsRequest> T = EntityTypes.T(TestServiceSimplepropsRequest.class);

	@Override
	EvalContext<? extends TestServiceResponse> eval(Evaluator<ServiceRequest> evaluator);

	int getIntProperty();
	void setIntProperty(int intProperty);

	Integer getIntegerProperty();
	void setIntegerProperty(Integer intProperty);

	boolean getBooleanProperty();
	void setBooleanProperty(boolean b);

	Boolean getBoolProperty();
	void setBoolProperty(Boolean b);

}
