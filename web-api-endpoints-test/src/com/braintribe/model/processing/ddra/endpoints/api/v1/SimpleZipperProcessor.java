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
package com.braintribe.model.processing.ddra.endpoints.api.v1;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.braintribe.exception.Exceptions;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.ZipRequestSimple;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.resource.Resource;
import com.braintribe.utils.IOTools;
import com.braintribe.utils.stream.api.StreamPipe;
import com.braintribe.utils.stream.api.StreamPipes;

public class SimpleZipperProcessor implements ServiceProcessor<ZipRequestSimple, Resource> {

	@Override
	public Resource process(ServiceRequestContext requestContext, ZipRequestSimple request) {
		try {

			Resource zipOutResource = null;

			StreamPipe pipe = StreamPipes.simpleFactory().newPipe("demo-zip-" + request.getName());

			zipOutResource = Resource.createTransient(pipe::openInputStream);
			zipOutResource.setName(request.getName() + ".zip");
			zipOutResource.setMimeType("application/zip");

			MessageDigest md5Instance = MessageDigest.getInstance("MD5");
			try (OutputStream out = pipe.openOutputStream();
					InputStream inputResourceStream = request.getResource().openStream();
					ZipOutputStream zipOutputStream = new ZipOutputStream(new DigestOutputStream(out, md5Instance))) {

				zipOutputStream.putNextEntry(new ZipEntry(request.getName()));
				IOTools.transferBytes(inputResourceStream, zipOutputStream);
			}
			return zipOutResource;
		} catch (Exception e) {
			throw Exceptions.unchecked(e, "I don't care what went wrong");
		}

	}

}
