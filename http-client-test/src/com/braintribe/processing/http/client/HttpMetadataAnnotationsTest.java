package com.braintribe.processing.http.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.annotation.Annotation;

import org.junit.Test;

import com.braintribe.model.generic.annotation.meta.api.MdaHandler;
import com.braintribe.model.generic.annotation.meta.api.MetaDataAnnotations;
import com.braintribe.model.meta.data.MetaData;

public class HttpMetadataAnnotationsTest {

	@com.braintribe.model.deployment.http.annotation.HttpMultipartFormData(globalId = "multipart", value = "request",
			requestPartMimeType = "application/xml")
	private static class MultipartAnnotated {}

	@com.braintribe.model.deployment.http.annotation.HttpProduces(globalId = "produces", mimeType = "application/xml",
			responseCode = 201, responseType = "string", useOriginalStatusCode = true)
	private static class ProducesAnnotated {}

	@com.braintribe.model.deployment.http.annotation.HttpSuccessCodes(globalId = "success-codes", value = { 200, 201, 204 })
	private static class SuccessCodesAnnotated {}

	private static class MultipartProperties {
		@com.braintribe.model.deployment.http.annotation.HttpPathParam(globalId = "optional-path", value = "optional",
				omitSegmentIfNull = true)
		String optionalPath() { return null; }

		@com.braintribe.model.deployment.http.annotation.HttpMultipartTextPart(globalId = "caption-part", value = "caption", mimeType = "text/markdown",
				headers = { "X-Part: caption" })
		String caption() { return null; }

		@com.braintribe.model.deployment.http.annotation.HttpMultipartMarshalledPart(globalId = "details-part", value = "details",
				mimeType = "application/json")
		Object details() { return null; }
	}

	@Test
	public void mapsMultipartAnnotation() {
		com.braintribe.model.deployment.http.meta.HttpMultipartFormData metadata = build(
				MultipartAnnotated.class.getAnnotation(com.braintribe.model.deployment.http.annotation.HttpMultipartFormData.class));
		assertEquals("request", metadata.getRequestPartName());
		assertEquals("application/xml", metadata.getRequestPartMimeType());
	}

	@Test
	public void mapsProducesAnnotationIncludingTypeSignature() {
		com.braintribe.model.deployment.http.meta.HttpProduces metadata = build(
				ProducesAnnotated.class.getAnnotation(com.braintribe.model.deployment.http.annotation.HttpProduces.class));
		assertEquals("application/xml", metadata.getMimeType());
		assertEquals(201, metadata.getResponseCode());
		assertEquals("string", metadata.getResponseTypeSignature());
		assertEquals(true, metadata.getUseOriginalStatusCode());
	}

	@Test
	public void mapsPrimitiveArrayAnnotation() {
		com.braintribe.model.deployment.http.meta.HttpSuccessCodes metadata = build(
				SuccessCodesAnnotated.class.getAnnotation(com.braintribe.model.deployment.http.annotation.HttpSuccessCodes.class));
		assertEquals(java.util.Arrays.asList(200, 201, 204), metadata.getSuccessCodes());
	}

	@Test
	public void mapsTextAndMarshalledPartAnnotations() throws Exception {
		com.braintribe.model.deployment.http.meta.params.HttpMultipartTextPart text = build(
				MultipartProperties.class.getDeclaredMethod("caption").getAnnotation(
						com.braintribe.model.deployment.http.annotation.HttpMultipartTextPart.class));
		assertEquals("caption", text.getParamName());
		assertEquals("text/markdown", text.getMimeType());
		assertEquals(java.util.Collections.singletonList("X-Part: caption"), text.getHeaders());

		com.braintribe.model.deployment.http.meta.params.HttpMultipartMarshalledPart marshalled = build(
				MultipartProperties.class.getDeclaredMethod("details").getAnnotation(
						com.braintribe.model.deployment.http.annotation.HttpMultipartMarshalledPart.class));
		assertEquals("details", marshalled.getParamName());
		assertEquals("application/json", marshalled.getMimeType());
	}

	@Test
	public void mapsOptionalPathAnnotation() throws Exception {
		com.braintribe.model.deployment.http.meta.params.HttpPathParam path = build(
				MultipartProperties.class.getDeclaredMethod("optionalPath").getAnnotation(
						com.braintribe.model.deployment.http.annotation.HttpPathParam.class));
		assertEquals("optional", path.getParamName());
		assertEquals(true, path.getOmitSegmentIfNull());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <M extends MetaData> M build(Annotation annotation) {
		MdaHandler handler = MetaDataAnnotations.registry().annoToHandler().get(annotation.annotationType());
		assertNotNull("No gmf.mda handler for " + annotation.annotationType(), handler);
		return (M) handler.buildMdList(annotation, null).get(0);
	}
}
