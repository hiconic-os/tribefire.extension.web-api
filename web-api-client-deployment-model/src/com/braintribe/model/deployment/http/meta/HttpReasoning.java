package com.braintribe.model.deployment.http.meta;

import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.annotation.meta.Description;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.meta.data.EntityTypeMetaData;

/**
 * Base metadata for interpreting an HTTP response as an unsatisfied service result.
 * <p>
 * Matching rules are ordered by metadata proximity, selector specificity, semantic subtype specificity and finally declaration order. Root and
 * detail rules occupy independent slots: exactly one {@link HttpRootReasoning} creates the result reason, while an optional
 * {@link HttpBodyDetailReasoning} may decode the response body as a cause. The body is consumed exactly once.
 */
@Abstract
@Description("Interprets matching HTTP responses as unsatisfied service results.")
public interface HttpReasoning extends EntityTypeMetaData {
	EntityType<HttpReasoning> T = EntityTypes.T(HttpReasoning.class);

	/** Selector such as {@code 400}, {@code 4xx}, {@code 400-499}, {@code 4xx,!404}, {@code !2xx} or {@code *}. */
	@Description("HTTP status selector supporting exact codes, families, ranges, unions and exclusions.")
	String getStatusCodeExpression();
	void setStatusCodeExpression(String statusCodeExpression);
}
