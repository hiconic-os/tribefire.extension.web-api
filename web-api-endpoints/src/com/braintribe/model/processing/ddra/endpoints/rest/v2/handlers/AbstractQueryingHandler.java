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
package com.braintribe.model.processing.ddra.endpoints.rest.v2.handlers;

import com.braintribe.model.DdraEndpointDepth;
import com.braintribe.model.DdraEndpointDepthKind;
import com.braintribe.model.access.TmpQueryResultDepth;
import com.braintribe.model.ddra.endpoints.v2.RestV2Endpoint;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.utils.collection.impl.AttributeContexts;

public abstract class AbstractQueryingHandler<E extends RestV2Endpoint> extends AbstractRestV2Handler<E> {

	// CORETS-873: Uncomment this to activate the breadth-first implementation of depth
	public static final boolean DEPTH_ENABLED = true;

	protected <T> T evaluateQueryRequest(ServiceRequest request, E endpoint, boolean throw404OnNotFound) {
		boolean pushedDepth = pushDepthOnAttributeConetxt(endpoint);

		try {
			return evaluateServiceRequest(request, throw404OnNotFound);

		} finally {
			if (pushedDepth)
				AttributeContexts.pop();
		}
	}

	private boolean pushDepthOnAttributeConetxt(E endpoint) {
		DdraEndpointDepth ded = endpoint.getComputedDepth();
		if (ded == null || ded.getKind() != DdraEndpointDepthKind.custom)
			return false;

		if (!DEPTH_ENABLED)
			return false;

		AttributeContexts.push(AttributeContexts.derivePeek().set(TmpQueryResultDepth.class, ded.getCustomDepth()).build());
		return true;
	}

}
