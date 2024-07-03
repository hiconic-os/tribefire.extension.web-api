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
package com.braintribe.model.openapi.v3_0.reference.model;

import java.util.List;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.openapi.v3_0.JsonReferencable;

public interface ComplexReferencable extends HasDescription, JsonReferencable {

	EntityType<ComplexReferencable> T = EntityTypes.T(ComplexReferencable.class);

	ComplexReferencable getOther();
	void setOther(ComplexReferencable items);

	List<ComplexReferencable> getOthersList();
	void setOthersList(List<ComplexReferencable> items);

}
