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
package com.braintribe.model;

import com.braintribe.model.ddra.endpoints.IdentityManagementMode;
import com.braintribe.model.ddra.endpoints.OutputPrettiness;
import com.braintribe.model.ddra.endpoints.TypeExplicitness;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@Abstract
public interface DdraEndpoint extends DdraEndpointHeaders {

	EntityType<DdraEndpoint> T = EntityTypes.T(DdraEndpoint.class);
	
	@Initializer(value="enum(com.braintribe.model.ddra.endpoints.OutputPrettiness,mid)")
	OutputPrettiness getPrettiness();
	void setPrettiness(OutputPrettiness prettiness);

	@Initializer("'3'")
	String getDepth();
	void setDepth(String depth);

	boolean getStabilizeOrder();
	void setStabilizeOrder(boolean stabilizeOrder);

	DdraEndpointDepth getComputedDepth();
	void setComputedDepth(DdraEndpointDepth computedDepth);

	boolean getWriteEmptyProperties();
	void setWriteEmptyProperties(boolean writeEmptyProperties);
	
	boolean getWriteAbsenceInformation();
	void setWriteAbsenceInformation(boolean writeAbsenceInformation);
	
	boolean getInferResponseType();
	void setInferResponseType(boolean inferResponseType);
	
	@Initializer(value="enum(com.braintribe.model.ddra.endpoints.TypeExplicitness,auto)")
	TypeExplicitness getTypeExplicitness();
	void setTypeExplicitness(TypeExplicitness typeExplicitness);
	
	@Initializer("0")
	Integer getEntityRecurrenceDepth();
	void setEntityRecurrenceDepth(Integer entityRecurrenceDepth);
	
	@Initializer(value="enum(com.braintribe.model.ddra.endpoints.IdentityManagementMode,auto)")
	IdentityManagementMode getIdentityManagementMode();
	void setIdentityManagementMode(IdentityManagementMode identityManagementMode);
	
	boolean getDownloadResource();
	void setDownloadResource(boolean downloadResource);
	
	boolean getSaveLocally();
	void setSaveLocally(boolean saveLocally);
	
	String getResponseFilename();
	void setResponseFilename(String responseFilename);
	
	String getResponseContentType();
	void setResponseContentType(String responseContentType);
	
}
