// ============================================================================
package dev.hiconic.servlet.decoder.api;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.Property;

/**
 * This interface is used by the HttpRequestEntityDecoder when properties of type "object" when decoding
 * HttpServletRequest.
 */
@FunctionalInterface
public interface PropertyTypeResolver {

	/**
	 * Resolves the actual type of the given property.
	 * 
	 * @param entityType
	 *            the entityType, never {@code null}
	 * @param property
	 *            the the property to resolve the type for, never {@code null}
	 * 
	 * @return the type, must not be {@code null}
	 */
	GenericModelType resolvePropertyType(EntityType<?> entityType, Property property);

}
