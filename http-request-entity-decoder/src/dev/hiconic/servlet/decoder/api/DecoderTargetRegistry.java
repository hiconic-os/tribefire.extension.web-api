// ============================================================================
package dev.hiconic.servlet.decoder.api;

import com.braintribe.model.generic.GenericEntity;

public interface DecoderTargetRegistry {

	/**
	 * Adds a new target entity to receive values from the decoded request.
	 * 
	 * @param prefix
	 *            the prefix to use to bypass ordering, must not be {@code null}
	 * @param target
	 *            the target entity to decode into, must not be {@code null}
	 * @param onSet
	 * 			  code that gets executed when the target is actually addressed and set in a request
	 * 
	 * @return this decoder
	 */
	DecoderTargetRegistry target(String prefix, GenericEntity target, Runnable onSet);

}