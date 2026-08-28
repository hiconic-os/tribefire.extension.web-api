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
package tribefire.extension.webapi.http_client.wire.space;

import org.apache.http.conn.ssl.DefaultHostnameVerifier;

import com.braintribe.model.deployment.http.client.HttpClient;
import com.braintribe.model.deployment.http.client.HttpClientCertificate;
import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.transport.http.DefaultHttpClientProvider;
import com.braintribe.transport.http.HttpClientProvider;
import com.braintribe.transport.ssl.SslSocketFactoryProvider;
import com.braintribe.transport.ssl.impl.EasySslSocketFactoryProvider;
import com.braintribe.transport.ssl.impl.PemSslSocketFactoryProvider;
import com.braintribe.transport.ssl.impl.StrictSslSocketFactoryProvider;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.space.WireSpace;

@Managed
public class HttpSpace implements WireSpace {

	@Managed
	public HttpClientProvider clientProvider() {
		DefaultHttpClientProvider bean = new DefaultHttpClientProvider();
		bean.setSslSocketFactoryProvider(sslSocketFactoryProvider());
		return bean;
	}

	@Managed
	public HttpClientProvider nonPoolingClientProvider() {
		DefaultHttpClientProvider bean = new DefaultHttpClientProvider();
		bean.setSslSocketFactoryProvider(sslSocketFactoryProvider());
		bean.setPoolTimeToLive(1L);
		return bean;
	}

	@Managed
	public SslSocketFactoryProvider sslSocketFactoryProvider() {
		SslSocketFactoryProvider bean = TribefireRuntime.getAcceptSslCertificates()?
				new EasySslSocketFactoryProvider():
				new StrictSslSocketFactoryProvider();

		return bean;
	}

	/**
	 * The client provider for a concrete HTTP client. Clients which leave the TLS related properties untouched share the module's
	 * {@link #clientProvider() default provider} and thus its connection pool; anything deviating - a client certificate, an explicit
	 * certificate validation setting or hostname verification - gets its own provider with its own pool and SSL context.
	 */
	public HttpClientProvider clientProviderFor(HttpClient deployable) {
		boolean defaultTls = deployable.getClientCertificate() == null //
				&& deployable.getVerifyServerCertificate() == null //
				&& !Boolean.TRUE.equals(deployable.getVerifyServerHostname());

		return defaultTls ? clientProvider() : tlsSpecificClientProvider(deployable);
	}

	/** Not managed on purpose: the TLS settings behind it are per client. */
	private HttpClientProvider tlsSpecificClientProvider(HttpClient deployable) {
		DefaultHttpClientProvider bean = new DefaultHttpClientProvider();
		bean.setSslSocketFactoryProvider(tlsSpecificSslSocketFactoryProvider(deployable));

		// the transport defaults to no hostname verification, so this is strictly an opt-in
		if (Boolean.TRUE.equals(deployable.getVerifyServerHostname()))
			bean.setHostnameVerifier(new DefaultHostnameVerifier());

		return bean;
	}

	/**
	 * Provides the client side identity (mutual TLS) if a client certificate is configured, and in any case honours the client's certificate
	 * validation setting.
	 */
	private SslSocketFactoryProvider tlsSpecificSslSocketFactoryProvider(HttpClient deployable) {
		boolean trustAll = !verifyServerCertificate(deployable);
		HttpClientCertificate certificate = deployable.getClientCertificate();

		if (certificate == null)
			return trustAll ? new EasySslSocketFactoryProvider() : new StrictSslSocketFactoryProvider();

		PemSslSocketFactoryProvider bean = new PemSslSocketFactoryProvider();
		bean.setCertificatePem(certificate.getCertificate());
		bean.setPrivateKeyPem(certificate.getPrivateKey());
		bean.setTrustAll(trustAll);

		return bean;
	}

	/** Not set means the platform default, which is the inverse of TRIBEFIRE_ACCEPT_SSL_CERTIFICATES. */
	private boolean verifyServerCertificate(HttpClient deployable) {
		Boolean verify = deployable.getVerifyServerCertificate();
		return verify != null ? verify : !TribefireRuntime.getAcceptSslCertificates();
	}
}
