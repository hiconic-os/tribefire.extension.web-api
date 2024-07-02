// ============================================================================
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
package com.braintribe.model.openapi.v3_0.api;

import java.util.Set;

import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.openapi.v3_0.OpenApi;
import com.braintribe.model.service.api.AuthorizedRequest;
import com.braintribe.model.service.api.ServiceRequest;

@Abstract
public interface OpenapiRequest extends AuthorizedRequest {

	EntityType<OpenapiRequest> T = EntityTypes.T(OpenapiRequest.class);

	String getTribefireServicesUrl();
	void setTribefireServicesUrl(String tribefireServicesUrl);

	Set<String> getUseCases();
	void setUseCases(Set<String> useCases);

	GmMetaModel getModel();
	void setModel(GmMetaModel model);

	@Initializer("true")
	boolean getUseFullyQualifiedDefinitionName();
	void setUseFullyQualifiedDefinitionName(boolean useFullyQualifiedDefinitionName);

	@Initializer("false")
	boolean getUseJSONForExport();
	void setUseJSONForExport(boolean value);
	
	boolean getIncludeSessionId();
	void setIncludeSessionId(boolean includeSessionId);
	
	boolean getReflectSubtypes();
	void setReflectSubtypes(boolean reflectSubtypes);
	
	boolean getReflectSupertypes();
	void setReflectSupertypes(boolean reflectSupertypes);

	@Override
	EvalContext<? extends OpenApi> eval(Evaluator<ServiceRequest> evaluator);

}
