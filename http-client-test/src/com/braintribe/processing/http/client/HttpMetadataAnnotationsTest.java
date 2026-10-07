package com.braintribe.processing.http.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;

import org.junit.Test;

import com.braintribe.model.generic.annotation.meta.api.MdaHandler;
import com.braintribe.model.generic.annotation.meta.api.MetaDataAnnotations;
import com.braintribe.model.generic.annotation.meta.api.synthesis.AnnotationDescriptor;
import com.braintribe.model.generic.annotation.meta.api.synthesis.ClassReference;
import com.braintribe.model.generic.annotation.meta.api.synthesis.MdaSynthesisContext;
import com.braintribe.model.generic.annotation.meta.api.synthesis.SingleAnnotationDescriptor;
import com.braintribe.gm.model.reason.essential.InvalidArgument;
import com.braintribe.model.meta.data.MetaData;

public class HttpMetadataAnnotationsTest {

	@com.braintribe.model.deployment.http.annotation.HttpMultipartFormData(globalId = "multipart", value = "request",
			requestPartMimeType = "application/xml")
	private static class MultipartAnnotated {}

	@com.braintribe.model.deployment.http.annotation.HttpProduces(globalId = "produces", mimeType = "application/xml",
			responseCode = 202, useOriginalStatusCode = true)
	private static class ProducesAnnotated {}

	@com.braintribe.model.deployment.http.annotation.HttpStatusReasoning(globalId = "status-reasoning", status = "4xx,!404",
			reasonType = InvalidArgument.class, useOriginalStatusCode = true)
	private static class StatusReasoningAnnotated {}

	@com.braintribe.model.deployment.http.annotation.HttpBodyDetailReasoning(globalId = "body-detail", status = "4xx,5xx",
			reasonType = InvalidArgument.class, mimeType = "application/problem+json")
	private static class BodyDetailReasoningAnnotated {}

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
	public void mapsProducesAnnotation() {
		com.braintribe.model.deployment.http.meta.HttpProduces metadata = build(
				ProducesAnnotated.class.getAnnotation(com.braintribe.model.deployment.http.annotation.HttpProduces.class));
		assertEquals("application/xml", metadata.getMimeType());
		assertEquals(202, metadata.getResponseCode());
		assertEquals(true, metadata.getUseOriginalStatusCode());
	}

	@Test
	public void mapsStatusReasoningIncludingClassLiteral() {
		com.braintribe.model.deployment.http.meta.HttpStatusReasoning metadata = build(
				StatusReasoningAnnotated.class.getAnnotation(com.braintribe.model.deployment.http.annotation.HttpStatusReasoning.class));
		assertEquals("4xx,!404", metadata.getStatusCodeExpression());
		assertEquals(InvalidArgument.class.getName(), metadata.getReasonTypeSignature());
		assertEquals(true, metadata.getUseOriginalStatusCode());
	}

	@Test
	public void mapsBodyReasoningMimeType() {
		com.braintribe.model.deployment.http.meta.HttpBodyDetailReasoning metadata = build(
				BodyDetailReasoningAnnotated.class.getAnnotation(com.braintribe.model.deployment.http.annotation.HttpBodyDetailReasoning.class));
		assertEquals("4xx,5xx", metadata.getStatusCodeExpression());
		assertEquals(InvalidArgument.class.getName(), metadata.getReasonTypeSignature());
		assertEquals("application/problem+json", metadata.getMimeType());
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void synthesizesTypeSignatureAsClassLiteral() {
		com.braintribe.model.deployment.http.meta.HttpStatusReasoning metadata =
				com.braintribe.model.deployment.http.meta.HttpStatusReasoning.T.create();
		metadata.setReasonTypeSignature(InvalidArgument.class.getName());
		MdaHandler handler = MetaDataAnnotations.registry().annoToHandler()
				.get(com.braintribe.model.deployment.http.annotation.HttpStatusReasoning.class);
		CapturingSynthesisContext context = new CapturingSynthesisContext();

		handler.buildAnnotation(context, metadata);

		Object value = context.descriptor.getAnnotationValues().get("reasonType");
		assertTrue(value instanceof ClassReference);
		assertEquals(InvalidArgument.class.getName(), ((ClassReference) value).className);
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

	private static class CapturingSynthesisContext implements MdaSynthesisContext {
		SingleAnnotationDescriptor descriptor;

		@Override
		public SingleAnnotationDescriptor newDescriptor(Class<? extends Annotation> annotationClass) {
			return descriptor = new SingleAnnotationDescriptor(annotationClass);
		}

		@Override
		public void setCurrentDescriptor(AnnotationDescriptor descriptor) {
			// The created descriptor is already retained for the assertion.
		}

		@Override
		public void setCurrentDescriptorMulti(SingleAnnotationDescriptor descriptor, Class<? extends Annotation> repeatabeAnnoClass) {
			this.descriptor = descriptor;
		}
	}
}
