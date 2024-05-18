// ============================================================================
package dev.hiconic.servlet.decoder.api;

import java.util.ArrayList;
import java.util.List;

/*package*/ class HttpRequestEntityDecoderOptionsImpl implements HttpRequestEntityDecoderOptions {
	
	private boolean ignoringHeaders;
	private boolean ignoringUnmappedUrlParameters;
	private boolean ignoringUnmappedHeaders;
	private boolean nullAware;
	private PropertyTypeResolver propertyTypeResolver;
	private List<String> ignoredParameters = new ArrayList<>();

	@Override
	public boolean isIgnoringHeaders() {
		return ignoringHeaders;
	}

	@Override
	public HttpRequestEntityDecoderOptions setIgnoringHeaders(boolean ignoringHeaders) {
		this.ignoringHeaders = ignoringHeaders;
		return this;
	}

	@Override
	public boolean isIgnoringUnmappedUrlParameters() {
		return ignoringUnmappedUrlParameters;
	}

	@Override
	public HttpRequestEntityDecoderOptions setIgnoringUnmappedUrlParameters(boolean ignoringUnmappedUrlParameters) {
		this.ignoringUnmappedUrlParameters = ignoringUnmappedUrlParameters;
		return this;
	}

	@Override
	public boolean isIgnoringUnmappedHeaders() {
		return ignoringUnmappedHeaders;
	}

	@Override
	public HttpRequestEntityDecoderOptions setIgnoringUnmappedHeaders(boolean ignoringUnmappedHeaders) {
		this.ignoringUnmappedHeaders = ignoringUnmappedHeaders;
		return this;
	}

	@Override
	public PropertyTypeResolver getPropertyTypeResolver() {
		return propertyTypeResolver;
	}

	@Override
	public HttpRequestEntityDecoderOptions setPropertyTypeResolver(PropertyTypeResolver propertyTypeResolver) {
		this.propertyTypeResolver = propertyTypeResolver;
		return this;
	}

	@Override
	public List<String> getIgnoredParameters() {
		return ignoredParameters;
	}

	@Override
	public HttpRequestEntityDecoderOptions setIgnoredParameters(List<String> ignoredParameters) {
		this.ignoredParameters = ignoredParameters;
		return this;
	}

	@Override
	public HttpRequestEntityDecoderOptions addIgnoredParameter(String ignoredParameter) {
		if (ignoredParameters == null) {
			ignoredParameters = new ArrayList<>();
		}
		
		ignoredParameters.add(ignoredParameter);
		return this;
	}

	@Override
	public HttpRequestEntityDecoderOptions setNullAware(boolean nullAware) {
		this.nullAware = nullAware;
		return this;
	}

	@Override
	public boolean isNullAware() {
		return nullAware;
	}

}
