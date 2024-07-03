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
package com.braintribe.model.ddra.endpoints.v2;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.manipulation.DeleteMode;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * Properties relevant for all delete entities rest endpoints: both with and without ID
 */
public interface DdraDeleteEntitiesEndpointBase extends RestV2Endpoint {

	EntityType<DdraDeleteEntitiesEndpointBase> T = EntityTypes.T(DdraDeleteEntitiesEndpointBase.class);

	@Initializer("enum(com.braintribe.model.generic.manipulation.DeleteMode,dropReferencesIfPossible)")
	DeleteMode getDeleteMode();
	void setDeleteMode(DeleteMode deleteMode);
	
	@Initializer("enum(com.braintribe.model.ddra.endpoints.v2.DdraDeleteEntitiesProjection,count)")
	DdraDeleteEntitiesProjection getProjection();
	void setProjection(DdraDeleteEntitiesProjection projection);
}
