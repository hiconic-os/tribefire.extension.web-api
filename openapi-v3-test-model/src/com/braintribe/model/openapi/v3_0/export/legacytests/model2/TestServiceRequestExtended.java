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

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.service.api.ServiceRequest;

public interface TestServiceRequestExtended extends TestServiceRequest {

	EntityType<TestServiceRequestExtended> T = EntityTypes.T(TestServiceRequestExtended.class);

	@Override
	EvalContext<? extends TestServiceResponse> eval(Evaluator<ServiceRequest> evaluator);

	List<String> getStringList();
	void setStringList(List<String> list);

	Set<Integer> getIntSet();
	void setIntSet(Set<Integer> set);

	List<Object> getObjectList();
	void setObjectList(List<Object> list);

	Map<String, String> getStringMap();
	void setStringMap(Map<String, String> map);

	String getVeryHighPriorityProperty();
	void setVeryHighPriorityProperty(String s);

	String getLowPriorityProperty();
	void setLowPriorityProperty(String s);

	String getInvisibleProperty();
	void setInvisibleProperty(String s);
}
