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
package com.braintribe.model.processing.websocket.server;

import com.braintribe.model.processing.websocket.server.stub.evaluator.ValidateUserSessionEvaluatorStub;
import com.braintribe.model.processing.websocket.server.stub.marshaller.MarshallerRegistryStub;
import com.braintribe.model.service.api.InstanceId;

import tribefire.extension.web_api.ws.WsRegistry;
import tribefire.extension.web_api.ws.WsServer;

/**
 * Parent of test classes used for testing websocket server implemented in {@link GmWebSocketServer} class.
 * 
 */
public abstract class AbstractGmWebSocketServerTest {

	protected static WsServer wsServer;
	protected static WsRegistry wsSessionRegistry;
	
	protected static final String TYPE_GM_JSON = "gm/json";
	protected static final String TYPE_APPLICATION_JSON = "application/json";
	protected static final String TYPE_APPLICATION_XML = "application/xml";

	protected static void setupWsServer() {
		wsSessionRegistry = new WsRegistry();
		wsServer = createWsServer(wsSessionRegistry);
	}
	
	/**
	 * Creates a {@link GmWebSocketServer} instance populated with test doubles.
	 */
	private static WsServer createWsServer(WsRegistry wsSessionRegistry) {
		WsServer wsServer = new WsServer();
		wsServer.setMarshallerRegistry(new MarshallerRegistryStub());
		wsServer.setEvaluator(new ValidateUserSessionEvaluatorStub());
		wsServer.setProcessingInstanceId(InstanceId.T.create("master"));
		wsServer.setSessionRegistry(wsSessionRegistry);
		
		return wsServer;
	}
	
}
