package com.braintribe.model.processing.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.braintribe.gm.model.http.reason.HttpStatusReason;
import com.braintribe.gm.model.reason.Reason;
import com.braintribe.gm.model.reason.essential.InvalidArgument;
import com.braintribe.gm.model.reason.essential.NotFound;
import com.braintribe.processing.http.client.HttpRequestContext;
import com.braintribe.processing.http.client.HttpRequestContextBuilder;
import com.braintribe.processing.http.client.HttpResponseMapping;

public class WebApiClientServiceProcessorTest {

	@Test
	public void genericDetailIsNestedBetweenLogicalReasonAndStatus() {
		HttpRequestContext context = HttpRequestContextBuilder.instance(null)
				.addResponseMapping(new HttpResponseMapping("400", HttpResponseMapping.Kind.STATUS, InvalidArgument.T, false, false))
				.addResponseMapping(new HttpResponseMapping("4xx", HttpResponseMapping.Kind.BODY_DETAIL, NotFound.T, false, false))
				.build();
		NotFound detail = NotFound.T.create();
		detail.setText("remote problem details");

		Reason result = WebApiClientServiceProcessor.mapFailure(context, 400, detail);

		assertTrue(result instanceof InvalidArgument);
		assertEquals("remote problem details", result.getText());
		assertSame(detail, result.getReasons().get(0));
		assertStatus(detail.getReasons().get(0), 400);
	}

	@Test
	public void meaningfulRemoteReasonRemainsTheMainReason() {
		HttpRequestContext context = HttpRequestContextBuilder.instance(null)
				.addResponseMapping(new HttpResponseMapping("409", HttpResponseMapping.Kind.BODY, NotFound.T, false, false))
				.build();
		NotFound remoteReason = NotFound.T.create();
		remoteReason.setText("meaningful remote reason");

		Reason result = WebApiClientServiceProcessor.mapFailure(context, 409, remoteReason);

		assertSame(remoteReason, result);
		assertStatus(result.getReasons().get(0), 409);
	}

	private static void assertStatus(Reason reason, int expectedStatus) {
		assertTrue(reason instanceof HttpStatusReason);
		assertEquals(Integer.valueOf(expectedStatus), ((HttpStatusReason) reason).getStatusCode());
	}
}
