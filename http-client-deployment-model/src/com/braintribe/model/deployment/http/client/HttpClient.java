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
package com.braintribe.model.deployment.http.client;

import java.util.Map;

import com.braintribe.model.deployment.connector.Connector;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.meta.DeployableComponent;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.annotation.meta.Name;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@Abstract
@DeployableComponent
public interface HttpClient extends Connector {

	final EntityType<HttpClient> T = EntityTypes.T(HttpClient.class);

	String clientCertificate = "clientCertificate";
	String defaultHeaders = "defaultHeaders";
	String verifyServerCertificate = "verifyServerCertificate";
	String verifyServerHostname = "verifyServerHostname";

	String getBaseUrl();
	void setBaseUrl(String baseUrl);

	HttpCredentials getCredentials();
	void setCredentials(HttpCredentials credentials);

	@Name("Client Certificate")
	@Description("Certificate and private key presented during the TLS handshake, i.e. the client side of mutual TLS. "
			+ "If not set, the client does not authenticate itself on the transport layer.")
	HttpClientCertificate getClientCertificate();
	void setClientCertificate(HttpClientCertificate clientCertificate);

	@Name("Verify Server Certificate")
	@Description("Whether the certificate chain presented by the server is validated against the trust store. "
			+ "If not set, the platform default applies, which is derived from TRIBEFIRE_ACCEPT_SSL_CERTIFICATES.")
	Boolean getVerifyServerCertificate();
	void setVerifyServerCertificate(Boolean verifyServerCertificate);

	@Name("Verify Server Hostname")
	@Description("Whether the server certificate has to be issued for the host that was actually addressed. This is an independent check from "
			+ "the chain validation: without it, any certificate trusted by the trust store is accepted for any host, so an attacker who can "
			+ "redirect traffic and holds a certificate for a domain of their own can impersonate the counterpart. "
			+ "If not set, no hostname verification takes place, which is the long standing default of the underlying HTTP transport.")
	Boolean getVerifyServerHostname();
	void setVerifyServerHostname(Boolean verifyServerHostname);

	@Name("Default Headers")
	@Description("Static HTTP headers sent with every request of this client, e.g. gateway credentials like API key and API secret headers. "
			+ "Headers of the individual request take precedence, i.e. a default header is only added if the request does not carry it already.")
	Map<String, String> getDefaultHeaders();
	void setDefaultHeaders(Map<String, String> defaultHeaders);

}
