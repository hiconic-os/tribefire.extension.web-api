package com.braintribe.processing.http.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.braintribe.model.resource.Resource;

public class GmHttpClientMultipartTest {

	@Test
	public void writesStructuredAndRepeatedResourceParts() throws Exception {
		Resource first = resource("first.txt", "text/plain", "first-content");
		Resource second = resource("second.bin", null, "second-content");
		Map<String, String> customHeaders = Collections.singletonMap("Content-ID", "<details>");
		HttpMultipartFormData formData = new HttpMultipartFormData("request", "application/json", Arrays.asList(
				new HttpMultipartPart("files", first.getName(), first.getMimeType(), first),
				new HttpMultipartPart("files", second.getName(), second.getMimeType(), second),
				new HttpMultipartPart("comment", null, "text/plain; charset=UTF-8", "hello", HttpMultipartPartKind.TEXT,
						Collections.emptyMap()),
				new HttpMultipartPart("details", null, "application/json", Collections.singletonMap("answer", 42),
						HttpMultipartPartKind.MARSHALLED, customHeaders)));

		HttpRequestContext context = HttpRequestContextBuilder.instance(null)
				.requestPath("http://localhost/upload")
				.payload(Collections.singletonMap("title", "test"))
				.multipartFormData(formData)
				.build();

		GmHttpClient client = new GmHttpClient();
		Method method = GmHttpClient.class.getDeclaredMethod("requestBuilder", HttpRequestContext.class);
		method.setAccessible(true);
		RequestBuilder requestBuilder = (RequestBuilder) method.invoke(client, context);

		String contentType = requestBuilder.getFirstHeader("Content-Type").getValue();
		String body = EntityUtils.toString(requestBuilder.getEntity(), StandardCharsets.ISO_8859_1);

		assertTrue(contentType.startsWith("multipart/form-data; boundary="));
		assertTrue(body.contains("name=\"request\""));
		assertTrue(body.contains("Content-Type: application/json"));
		assertTrue(body.contains("{\"title\":\"test\"}"));
		assertEquals(2, occurrences(body, "name=\"files\""));
		assertTrue(body.contains("filename=\"first.txt\""));
		assertTrue(body.contains("Content-Type: text/plain"));
		assertTrue(body.contains("Content-Type: application/octet-stream"));
		assertTrue(body.contains("first-content"));
		assertTrue(body.contains("second-content"));
		assertTrue(body.contains("name=\"comment\""));
		assertTrue(body.contains("hello"));
		assertTrue(body.contains("name=\"details\""));
		assertTrue(body.contains("Content-ID: <details>"));
		assertTrue(body.contains("{\"answer\":42}"));
	}

	@Test
	public void writesUrlEncodedBody() throws Exception {
		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("single", "a value");
		payload.put("tag", Arrays.asList("one", "two"));
		HttpRequestContext context = HttpRequestContextBuilder.instance(null)
				.requestPath("http://localhost/form")
				.requestMethod("POST")
				.consumes("application/x-www-form-urlencoded")
				.payload(payload)
				.build();

		GmHttpClient client = new GmHttpClient();
		Method method = GmHttpClient.class.getDeclaredMethod("requestBuilder", HttpRequestContext.class);
		method.setAccessible(true);
		RequestBuilder requestBuilder = (RequestBuilder) method.invoke(client, context);

		assertEquals("single=a+value&tag=one&tag=two", EntityUtils.toString(requestBuilder.getEntity(), StandardCharsets.UTF_8));
	}

	@Test
	public void supportsMultipartWithoutSyntheticRequestPart() throws Exception {
		HttpMultipartFormData formData = new HttpMultipartFormData("", "application/json", Collections.singletonList(
				new HttpMultipartPart("details", null, "application/json", Collections.singletonMap("answer", 42),
						HttpMultipartPartKind.MARSHALLED, Collections.emptyMap())));
		HttpRequestContext context = HttpRequestContextBuilder.instance(null)
				.requestPath("http://localhost/upload")
				.payload(Collections.emptyMap())
				.multipartFormData(formData)
				.build();

		GmHttpClient client = new GmHttpClient();
		Method method = GmHttpClient.class.getDeclaredMethod("requestBuilder", HttpRequestContext.class);
		method.setAccessible(true);
		RequestBuilder requestBuilder = (RequestBuilder) method.invoke(client, context);
		String body = EntityUtils.toString(requestBuilder.getEntity(), StandardCharsets.ISO_8859_1);

		assertEquals(1, occurrences(body, "Content-Disposition: form-data"));
		assertTrue(body.contains("name=\"details\""));
		assertTrue(body.contains("{\"answer\":42}"));
	}

	@Test
	public void preservesRepeatedQueryParameters() throws Exception {
		HttpRequestContext context = HttpRequestContextBuilder.instance(null)
				.requestPath("http://localhost/search")
				.addQueryParameter("tag", "one")
				.addQueryParameter("tag", "two")
				.build();
		GmHttpClient client = new GmHttpClient();
		Method method = GmHttpClient.class.getDeclaredMethod("requestBuilder", HttpRequestContext.class);
		method.setAccessible(true);
		RequestBuilder requestBuilder = (RequestBuilder) method.invoke(client, context);

		assertEquals("tag=one&tag=two", requestBuilder.getUri().getRawQuery());
	}

	@Test
	public void appliesDefaultHeadersWithoutOverridingRequestHeaders() throws Exception {
		GmHttpClient client = new GmHttpClient();
		Map<String, String> defaults = new LinkedHashMap<>();
		defaults.put("Gen-Client-Id", "default-id");
		defaults.put("Gen-Client-Secret", "default-secret");
		client.setDefaultHeaders(defaults);

		HttpRequestContext context = HttpRequestContextBuilder.instance(null)
				.requestPath("http://localhost/headers")
				.addHeaderParameter("gen-client-id", "request-id")
				.build();
		Method method = GmHttpClient.class.getDeclaredMethod("requestBuilder", HttpRequestContext.class);
		method.setAccessible(true);
		RequestBuilder requestBuilder = (RequestBuilder) method.invoke(client, context);

		assertEquals("request-id", requestBuilder.getFirstHeader("Gen-Client-Id").getValue());
		assertEquals("default-secret", requestBuilder.getFirstHeader("Gen-Client-Secret").getValue());
		assertEquals(1, requestBuilder.getHeaders("Gen-Client-Id").length);
	}

	private Resource resource(String name, String mimeType, String content) {
		Resource resource = Resource.createTransient(() -> new java.io.ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
		resource.setName(name);
		resource.setMimeType(mimeType);
		return resource;
	}

	private int occurrences(String value, String searched) {
		int count = 0;
		for (int offset = 0; (offset = value.indexOf(searched, offset)) >= 0; offset += searched.length())
			count++;
		return count;
	}
}
