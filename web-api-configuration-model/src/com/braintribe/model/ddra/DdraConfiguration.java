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
package com.braintribe.model.ddra;

import java.util.Set;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * This instance is a singleton in the cortex database.
 * 
 * TODO: we should ensure that there is always only 1 instance of this in the cortex.
 */
public interface DdraConfiguration extends GenericEntity {

	EntityType<DdraConfiguration> T = EntityTypes.T(DdraConfiguration.class);

	String mappings = "mappings";
	String lastChangeTimestamp = "lastChangeTimestamp";
	
	Set<DdraMapping> getMappings();
	void setMappings(Set<DdraMapping> mappings);
	
	String getLastChangeTimestamp();
	void setLastChangeTimestamp(String timestamp);
}
