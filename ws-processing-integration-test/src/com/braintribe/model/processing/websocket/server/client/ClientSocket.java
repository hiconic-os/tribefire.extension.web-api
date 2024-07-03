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
package com.braintribe.model.processing.websocket.server.client;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.braintribe.codec.marshaller.api.Marshaller;
import com.braintribe.model.service.api.ServiceRequest;

/**
 * Class that represent a websocket client.
 * 
 */
@WebSocket
public class ClientSocket
{
	private Session session;
	private List<ServiceRequest> receivedServiceRequests;
	private Marshaller marshaller;
	
	public ClientSocket(Marshaller marshaller) {
		this.receivedServiceRequests = new ArrayList<>();
		this.marshaller = marshaller;
	}
	
	public List<ServiceRequest> getReceivedServiceRequests() {
		return receivedServiceRequests;
	}

	@OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
    }
	
	@SuppressWarnings("unused")
	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason) {
		 this.session = null;
	}
	
	@OnWebSocketMessage
	public void onMessage(String text) throws Exception {
	    receivedServiceRequests.add((ServiceRequest) marshaller.unmarshall(new ByteArrayInputStream(text.getBytes())));
	}
	
	public boolean isConnectionOpen() {
		return session != null;
	}
	
	public boolean isMessageReceived() {
		return !receivedServiceRequests.isEmpty();
	}
	
	public boolean ping() {
		try {
			session.getRemote().sendPing(ByteBuffer.wrap("ping".getBytes()));
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public void closeConnection() {
		session.close();
	}
	
	public void harshlyCloseConnection() throws Exception {
		session.disconnect();
	}

	
}