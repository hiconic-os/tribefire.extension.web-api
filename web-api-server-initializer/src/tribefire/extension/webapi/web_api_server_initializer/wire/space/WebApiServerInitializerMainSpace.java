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
package tribefire.extension.webapi.web_api_server_initializer.wire.space;

import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.cortex.initializer.support.integrity.wire.contract.CoreInstancesContract;

import tribefire.extension.webapi.web_api_server_initializer.wire.contract.WebApiServerInitializerContract;
import tribefire.extension.webapi.web_api_server_initializer.wire.contract.WebApiServerInitializerMainContract;
import tribefire.extension.webapi.web_api_server_initializer.wire.contract.WebApiServerInitializerModelsContract;
import tribefire.extension.webapi.web_api_server_initializer.wire.contract.ExistingInstancesContract;
import tribefire.extension.webapi.web_api_server_initializer.wire.contract.RuntimePropertiesContract;

@Managed
public class WebApiServerInitializerMainSpace implements WebApiServerInitializerMainContract {

	@Import
	private WebApiServerInitializerContract initializer;
	
	@Import
	private WebApiServerInitializerModelsContract models;
	
	@Import
	private ExistingInstancesContract existingInstances;
	
	@Import
	private RuntimePropertiesContract properties;
	
	@Import
	private CoreInstancesContract coreInstances;
	
	@Override
	public WebApiServerInitializerContract initializer() {
		return initializer;
	}

	@Override
	public WebApiServerInitializerModelsContract models() {
		return models;
	}

	@Override
	public ExistingInstancesContract existingInstances() {
		return existingInstances;
	}
	
	@Override
	public RuntimePropertiesContract properties() {
		return properties;
	}
	
	@Override
	public CoreInstancesContract coreInstances() {
		return coreInstances;
	}
}
