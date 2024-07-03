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
package com.braintribe.model.processing.resource.server.stream;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Supplier;

import com.braintribe.model.generic.session.InputStreamProvider;
import com.braintribe.utils.stream.MemoryThresholdBuffer;
import com.braintribe.utils.stream.RepeatableInputStream;
import com.braintribe.utils.stream.WriteOnReadInputStream;

public class ReopenableInputStreamProviders {

	public static ReopenableInputStreamProvider create(Supplier<InputStream> originalSupplier) {
		return createSync(originalSupplier);
	}

	/**
	 * <p>
	 * Returns a {@code ReopenableInputStreamProvider} which processes the data read from the provided
	 * {@code InputStream} by {@code originalSupplier} so that it is also synchronously written to a backup storage.
	 */
	public static ReopenableInputStreamProvider createSync(Supplier<InputStream> originalSupplier) {
		return new ReopenableInputStreamProviderSync(originalSupplier);
	}

	/**
	 * <p>
	 * Returns a {@code ReopenableInputStreamProvider} which processes the data read from the provided
	 * {@code InputStream} by {@code originalSupplier} so that it is also written to a pipe, which writes it to a backup
	 * storage in another thread.
	 */
	public static ReopenableInputStreamProvider createAsync(Supplier<InputStream> originalSupplier) {
		return new ReopenableInputStreamProviderAsync(originalSupplier);
	}

	public static abstract class ReopenableInputStreamProvider implements InputStreamProvider, Supplier<InputStream>, Closeable {

		Supplier<InputStream> source;

		public ReopenableInputStreamProvider(Supplier<InputStream> originalSupplier) {
			this.source = Objects.requireNonNull(originalSupplier, "originalSupplier must not be null");
		}

		@Override
		public InputStream get() {
			try {
				return openInputStream();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}

	}

	public static class ReopenableInputStreamProviderAsync extends ReopenableInputStreamProvider {

		private RepeatableInputStream buffer;

		protected ReopenableInputStreamProviderAsync(Supplier<InputStream> originalSupplier) {
			super(originalSupplier);
		}

		@Override
		public InputStream openInputStream() throws IOException {
			if (buffer == null) {
				buffer = new RepeatableInputStream(source.get());
				return buffer;
			} else {
				try {
					return buffer.reopen();
				} catch (InterruptedException e) {
					throw new IOException("Interrupted while waiting reopen to be available", e);
				}
			}
		}

		@Override
		public void close() throws IOException {
			if (buffer != null) {
				buffer.destroy();
			}
		}

	}

	public static class ReopenableInputStreamProviderSync extends ReopenableInputStreamProvider {

		private MemoryThresholdBuffer buffer;

		protected ReopenableInputStreamProviderSync(Supplier<InputStream> originalSupplier) {
			super(originalSupplier);
		}

		@Override
		public InputStream openInputStream() throws IOException {
			if (buffer == null) {
				buffer = new MemoryThresholdBuffer();
				WriteOnReadInputStream inputStream = new WriteOnReadInputStream(source.get(), buffer, true, true);
				return inputStream;
			} else {
				return buffer.openInputStream(false);
			}
		}

		@Override
		public void close() throws IOException {
			if (buffer != null) {
				buffer.delete();
			}
		}

	}

}
