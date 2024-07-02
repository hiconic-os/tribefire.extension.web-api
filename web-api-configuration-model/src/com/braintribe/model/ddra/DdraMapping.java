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
package com.braintribe.model.ddra;

import java.util.Set;

import com.braintribe.model.ddra.endpoints.api.v1.ApiV1DdraEndpoint;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.annotation.SelectiveInformation;
import com.braintribe.model.generic.annotation.meta.Mandatory;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.prototyping.api.PrototypingRequest;
import com.braintribe.model.service.api.ServiceRequest;

/**
 * A single mapping for DDRA/REST: from a URL + Method (GET, POST...) to a service request.
 * 
 * This mapping MUST specify:
 * <ul>
 * <li>A URL</li>
 * <li>An HTTP method</li>
 * <li>At least one of the following:
 * <ul>
 * <li>A ServiceRequest's GmEntityType unmarshall from the URL/headers/body</li>
 * <li>An existing ServiceRequest (transformRequest) that wraps the ServiceRequest to execute. The idea here is to
 * decouple possible pre/post processing from actual service implementation</li>
 * </ul>
 * </li>
 * </ul>
 */
@SelectiveInformation("${method}:${path}")
public interface DdraMapping extends GenericEntity {

	EntityType<DdraMapping> T = EntityTypes.T(DdraMapping.class);

	@Initializer(value = "enum(com.braintribe.model.ddra.DdraUrlMethod,GET_POST)")
	DdraUrlMethod getMethod();
	void setMethod(DdraUrlMethod method);

	@Mandatory
	String getPath();
	void setPath(String path);

	// TODO check what happens if another _type gets passed in the marshaled body
	@Mandatory
	GmEntityType getRequestType();
	void setRequestType(GmEntityType requestType);

	PrototypingRequest getRequestPrototyping();
	void setRequestPrototyping(PrototypingRequest requestPrototyping);

	ApiV1DdraEndpoint getEndpointPrototype();
	void setEndpointPrototype(ApiV1DdraEndpoint endpointPrototype);

	ServiceRequest getTransformRequest();
	void setTransformRequest(ServiceRequest transformRequest);

	Boolean getAnnounceAsMultipart();
	void setAnnounceAsMultipart(Boolean announceAsMultipart);

	boolean getHideSerializedRequest();
	void setHideSerializedRequest(boolean hideSerializedRequest);

	String getDefaultProjection();
	void setDefaultProjection(String defaultProjection);

	String getDefaultMimeType();
	void setDefaultMimeType(String defaultMimeType);

	String getDefaultServiceDomain();
	void setDefaultServiceDomain(String defaultServiceDomain);

	String getDefaultPrettiness();
	void setDefaultPrettiness(String prettiness);

	String getDefaultDepth();
	void setDefaultDepth(String depth);

	Boolean getDefaultStabilizeOrder();
	void setDefaultStabilizeOrder(Boolean stabilizeOrder);

	Boolean getDefaultWriteEmptyProperties();
	void setDefaultWriteEmptyProperties(Boolean writeEmptyProperties);

	Boolean getDefaultWriteAbsenceInformation();
	void setDefaultWriteAbsenceInformation(Boolean writeAbsenceInformation);

	String getDefaultTypeExplicitness();
	void setDefaultTypeExplicitness(String typeExplicitness);

	Integer getDefaultEntityRecurrenceDepth();
	void setDefaultEntityRecurrenceDepth(Integer entityRecurrenceDepth);

	Set<String> getTags();
	void setTags(Set<String> tags);

	Boolean getDefaultDownloadResource();
	void setDefaultDownloadResource(Boolean downloadResource);

	Boolean getDefaultSaveLocally();
	void setDefaultSaveLocally(Boolean saveLocally);

	String getDefaultResponseFilename();
	void setDefaultResponseFilename(String responseFilename);

	String getDefaultResponseContentType();
	void setDefaultResponseContentType(String responseContentType);

	Boolean getDefaultUseSessionEvaluation();
	void setDefaultUseSessionEvaluation(Boolean defaultUseSessionEvaluation);

	Boolean getPreserveTransportPayload();
	void setPreserveTransportPayload(Boolean preserveTransportPayload);

	Boolean getDecodingLenience();
	void setDecodingLenience(Boolean decodingLenience);
}
