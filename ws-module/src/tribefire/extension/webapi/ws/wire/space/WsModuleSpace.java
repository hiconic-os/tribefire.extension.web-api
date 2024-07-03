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
package tribefire.extension.webapi.ws.wire.space;

import static com.braintribe.web.api.registry.WebRegistries.websocketEndpoint;

import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import tribefire.extension.web_api.ws.WsServer;
import tribefire.module.wire.contract.TribefireModuleContract;
import tribefire.module.wire.contract.TribefireWebPlatformContract;

/**
 * This module's javadoc is yet to be written.
 */
@Managed
public class WsModuleSpace implements TribefireModuleContract {

	@Import
	private TribefireWebPlatformContract tfPlatform;

	//
	// Hardwiring
	//

	@Override
	public void bindHardwired() {
		tfPlatform.hardwiredExperts().addPushHandler(wsServer());
		tfPlatform.hardwiredDeployables().webRegistry().addWebsocketEndpoint(
			websocketEndpoint()
				.path("/websocket")
				.instance(wsServer())
		);
	}
	

	//
	// Experts
	//
	
	@Managed
	public WsServer wsServer() {
		WsServer bean = new WsServer();
		bean.setMarshallerRegistry(tfPlatform.marshalling().registry());
		bean.setProcessingInstanceId(tfPlatform.platformReflection().instanceId());
		bean.setEvaluator(tfPlatform.systemUserRelated().evaluator());
		return bean;
	}
	
}
