package com.braintribe.processing.http.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.braintribe.gm.model.reason.essential.InvalidArgument;
import com.braintribe.gm.model.reason.essential.NotFound;

public class HttpResponseMappingTest {

	@Test
	public void matchesStatusExpressions() {
		HttpResponseMapping mapping = mapping("4xx,!404", HttpResponseMapping.Kind.STATUS, InvalidArgument.T, false);

		assertTrue(mapping.matches(400));
		assertTrue(mapping.matches(422));
		assertFalse(mapping.matches(404));
		assertFalse(mapping.matches(500));
		assertTrue(mapping.specificity(400) > 0);
	}

	@Test
	public void supportsRangesAndExclusionOnlySelectors() {
		HttpResponseMapping range = mapping("400-410", HttpResponseMapping.Kind.STATUS, InvalidArgument.T, false);
		HttpResponseMapping nonSuccess = mapping("!2xx", HttpResponseMapping.Kind.STATUS, InvalidArgument.T, false);

		assertTrue(range.matches(405));
		assertFalse(range.matches(411));
		assertFalse(nonSuccess.matches(204));
		assertTrue(nonSuccess.matches(400));
		assertTrue(range.specificity(405) > nonSuccess.specificity(405));
	}

	@Test
	public void explicitBodyDetailEnrichesDefaultStatusReason() {
		HttpRequestContext context = HttpRequestContextBuilder.instance(null)
				.addResponseMapping(new HttpResponseMapping("4xx", HttpResponseMapping.Kind.BODY_DETAIL, NotFound.T,
						"application/problem+json", false, false, 0))
				.addResponseMapping(mapping("400", HttpResponseMapping.Kind.STATUS, InvalidArgument.T, true))
				.build();

		HttpResponseMapping selected = context.responseMappingForCode(400);

		assertSame(InvalidArgument.T, selected.reasonType());
		assertSame(NotFound.T, selected.detailType());
		assertSame(NotFound.T, selected.bodyType());
		assertEquals("application/problem+json", selected.bodyMimeType());
	}

	@Test
	public void bodyReasonWinsStatusReasonAtEqualSpecificity() {
		HttpRequestContext context = HttpRequestContextBuilder.instance(null)
				.addResponseMapping(mapping("409", HttpResponseMapping.Kind.STATUS, InvalidArgument.T, false))
				.addResponseMapping(mapping("409", HttpResponseMapping.Kind.BODY, NotFound.T, false))
				.build();

		HttpResponseMapping selected = context.responseMappingForCode(409);
		assertEquals(HttpResponseMapping.Kind.BODY, selected.kind());
		assertSame(NotFound.T, selected.reasonType());
	}

	@Test
	public void closerMetadataWinsBeforeSelectorSpecificity() {
		HttpRequestContext context = HttpRequestContextBuilder.instance(null)
				.addResponseMapping(mapping("4xx", HttpResponseMapping.Kind.STATUS, NotFound.T, false, 0))
				.addResponseMapping(mapping("400", HttpResponseMapping.Kind.STATUS, InvalidArgument.T, false, 1))
				.build();

		HttpResponseMapping selected = context.responseMappingForCode(400);
		assertSame(NotFound.T, selected.reasonType());
	}

	@Test
	public void selectorSpecificityBreaksEqualMetadataPrecedence() {
		HttpRequestContext context = HttpRequestContextBuilder.instance(null)
				.addResponseMapping(mapping("4xx", HttpResponseMapping.Kind.STATUS, NotFound.T, false, 0))
				.addResponseMapping(mapping("400", HttpResponseMapping.Kind.STATUS, InvalidArgument.T, false, 0))
				.build();

		HttpResponseMapping selected = context.responseMappingForCode(400);
		assertSame(InvalidArgument.T, selected.reasonType());
	}

	private static HttpResponseMapping mapping(String expression, HttpResponseMapping.Kind kind,
			com.braintribe.model.generic.reflection.GenericModelType reasonType, boolean defaultRule) {
		return new HttpResponseMapping(expression, kind, reasonType, false, defaultRule);
	}

	private static HttpResponseMapping mapping(String expression, HttpResponseMapping.Kind kind,
			com.braintribe.model.generic.reflection.GenericModelType reasonType, boolean defaultRule, int metadataPrecedence) {
		return new HttpResponseMapping(expression, kind, reasonType, false, defaultRule, metadataPrecedence);
	}
}
