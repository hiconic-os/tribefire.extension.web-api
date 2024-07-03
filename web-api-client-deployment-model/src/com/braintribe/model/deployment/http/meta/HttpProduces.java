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
package com.braintribe.model.deployment.http.meta;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.GmType;

public interface HttpProduces extends HasMimeType {

	final EntityType<HttpProduces> T = EntityTypes.T(HttpProduces.class);
	
	@Mandatory
	@Initializer("200")
	int getResponseCode();
	void setResponseCode(int responseCode);
	
	GmType getResponseType();
	void setResponseType(GmType responseType);
	
	boolean getUseOriginalStatusCode();
	void 	setUseOriginalStatusCode(boolean statusCode);

}
