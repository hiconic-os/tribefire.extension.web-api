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
package com.braintribe.model.processing.websocket.server.stub;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.Extension;
import javax.websocket.MessageHandler;
import javax.websocket.MessageHandler.Partial;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

public class SessionStub implements Session {

	private boolean open;
	private Basic basicRemote;
	private Map<String, List<String>> params;
	
	public SessionStub(String sessionId, String clientId, String accept) {
		this(sessionId, clientId, accept, null);
	}
	
	public SessionStub(String sessionId, String clientId, String accept, Basic basicRemote) {
		this.open = true;
		this.basicRemote = basicRemote;
		initPathParamMap(sessionId, clientId, accept);
	}
	
	private void initPathParamMap(String sessionId, String clientId, String accept) {
		this.params = new HashMap<>();
		if(sessionId != null) {
			this.params.put("sessionId", Arrays.asList(sessionId));
		}
		if(clientId != null) {
			this.params.put("clientId", Arrays.asList(clientId));
		}
		if(accept != null) {
			this.params.put("accept", Arrays.asList(accept));
		}
	}
	
	public void setBasicRemote(Basic basicRemote) {
		this.basicRemote = basicRemote;
	}

	@Override
	public WebSocketContainer getContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addMessageHandler(MessageHandler handler) throws IllegalStateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void addMessageHandler(Class<T> clazz, Whole<T> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void addMessageHandler(Class<T> clazz, Partial<T> handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<MessageHandler> getMessageHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeMessageHandler(MessageHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getProtocolVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNegotiatedSubprotocol() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Extension> getNegotiatedExtensions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	@Override
	public long getMaxIdleTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxIdleTimeout(long milliseconds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxBinaryMessageBufferSize(int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMaxBinaryMessageBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxTextMessageBufferSize(int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMaxTextMessageBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Async getAsyncRemote() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Basic getBasicRemote() {
		return basicRemote;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws IOException {
		this.open = false;
	}

	@Override
	public void close(CloseReason closeReason) throws IOException {
		this.open = false;
	}

	@Override
	public URI getRequestURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getRequestParameterMap() {
		return params;
	}

	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getPathParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getUserProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Session> getOpenSessions() {
		// TODO Auto-generated method stub
		return null;
	}

}
