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

import static org.hamcrest.CoreMatchers.equalTo;

import java.net.URI;

import org.apache.http.client.utils.URIBuilder;
import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import com.braintribe.codec.marshaller.api.Marshaller;
import com.braintribe.model.processing.websocket.server.client.ClientSocket;

/**
 * Parent class of all classes that perform integration testing of websocket server implemented in
 * {@link GmWebSocketServer} class.
 * 
 */
// TODO rework tests to test in multiple instance environment (multicast request) when multiple instance option becomes available for ITs
public class AbstractGmWebSocketServerTest {

	protected static String instanceHost;
	protected static int instancePort;

	protected static String instanceUri;

	protected static final String TYPE_GM_JSON = "gm/json";
	protected static final String TYPE_APPLICATION_JSON = "application/json";
	protected static final String TYPE_APPLICATION_XML = "application/xml";

	protected static void setup() throws Exception {

		if (System.getProperty("instanceHost") == null || System.getProperty("instancePort") == null) {
			throw new RuntimeException("Tests cannot proceed. Instance host and port are not configured via system properties.");
		}

		instanceHost = System.getProperty("instanceHost");
		instancePort = Integer.parseInt(System.getProperty("instancePort"));

		instanceUri = new URIBuilder().setScheme("http").setHost(instanceHost).setPort(instancePort).setPath("/tribefire-services").build()
				.toString();
	}

	/**
	 * Makes connection of ws client to ws server.
	 */
	protected static ClientSocket connectToWsServer(String wsEndpointUri, Marshaller marshaller) throws Exception {
		
		WebSocketClient client = new WebSocketClient();
		ClientSocket clientSocket = new ClientSocket(marshaller);
		client.start();
		
		URI targetUri = new URI(wsEndpointUri);
		client.connect(clientSocket, targetUri);
		
		return clientSocket;
	}
	
	/**
	 * Locks execution for maximum stated amount of time until connection procedures (onOpen, onClose, onError) finish their execution in ws server.
	 */
	protected static void awaitWsConnectionProcedureToFinish(ClientSocket clientSocket, Boolean expected) {

		Awaitility.await().atMost(Duration.FIVE_SECONDS).with().pollInterval(Duration.ONE_HUNDRED_MILLISECONDS).until(clientSocket::isConnectionOpen,
				equalTo(expected));
	}

	/**
	 * Locks execution for maximum stated amount of time until message procedure (onMessage) finish its execution in ws server.
	 */
	protected static void awaitWsMessageProcedureToFinish(ClientSocket clientSocket, Boolean expected) {

		Awaitility.await().atMost(Duration.FIVE_SECONDS).with().pollInterval(Duration.ONE_HUNDRED_MILLISECONDS).until(clientSocket::isMessageReceived,
				equalTo(expected));
	}


	/**
	 * Creates ws endpoint URI.
	 */
	protected static String wsEndpointUri(String host, Integer port, String sessionId, String clientId, String accept) {

		URIBuilder uriBuilder = new URIBuilder().setScheme("ws").setHost(host).setPort(port).setPath("/tribefire-services/websocket")
				.addParameter("sessionId", sessionId).addParameter("clientId", clientId).addParameter("accept", accept);
		return uriBuilder.toString();
	}

	/**
	 * Creates ws service URI.
	 */
	protected static String wsServiceUri(String host, Integer port, String sessionId) {

		URIBuilder uriBuilder = new URIBuilder().setScheme("http").setHost(host).setPort(port)
				.setPath("/tribefire-services/api/v1/serviceDomain:default/com.braintribe.model.service.api.PushRequest")
				.addParameter("sessionId", sessionId);
		return uriBuilder.toString();
	}

}
