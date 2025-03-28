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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.braintribe.exception.Exceptions;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.ZipRequest;
import com.braintribe.model.processing.ddra.endpoints.api.v1.model.ZipTask;
import com.braintribe.model.processing.service.api.ServiceProcessor;
import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.resource.Resource;
import com.braintribe.utils.CollectionTools;
import com.braintribe.utils.CommonTools;
import com.braintribe.utils.stream.MultiplierOutputStream;
import com.braintribe.utils.stream.api.StreamPipe;
import com.braintribe.utils.stream.api.StreamPipes;

public class ZipperProcessor implements ServiceProcessor<ZipRequest, Object> {
	// public class ZipperProcessor implements ServiceProcessor<ZipRequest, List<Resource>>{

	@Override
	public Object process(ServiceRequestContext requestContext, ZipRequest request) {
		List<Resource> responseResources = new ArrayList<>();

		for (ZipTask task : request.getTasks()) {
			try {
				List<OutputStream> outputStreams = new ArrayList<>();

				Resource zipOutResource = null;
				if (task.getGenerateOutput()) {

					StreamPipe pipe = StreamPipes.simpleFactory().newPipe("demo-zip-" + task.getName());

					zipOutResource = Resource.createTransient(pipe::openInputStream);
					outputStreams.add(pipe.openOutputStream());
					zipOutResource.setName(task.getName() + ".zip");
					zipOutResource.setMimeType("application/zip");

					responseResources.add(zipOutResource);
				}

				if (task.getCapture() != null) {
					outputStreams.add(task.getCapture().openStream());
				}

				MessageDigest md5Instance = MessageDigest.getInstance("MD5");
				try (MultiplierOutputStream multiOut = new MultiplierOutputStream(outputStreams);
						ZipOutputStream zipOutputStream = new ZipOutputStream(new DigestOutputStream(multiOut, md5Instance))) {
					int i = 0;
					for (InputStream inputResourceStream : getInput(task)) {
						zipOutputStream.putNextEntry(new ZipEntry(task.getName() + (i++)));
						inputResourceStream.transferTo(zipOutputStream);
					}
					if (zipOutResource != null) {
						zipOutResource.setMd5(CommonTools.printHexBinary(md5Instance.digest()));
					}
				}
			} catch (Exception e) {
				throw Exceptions.unchecked(e, "I don't care what went wrong");
			}
		}

		// This is to be able to showcase the different ways of streaming resources (when enabled via endpoint param)
		// If there is a Resource (and not a collection of them) it can be directly streamed without the multipart detour
		// If there is no resource at all we can indicate that as well
		if (responseResources.isEmpty()) {
			return null;
		} else if (responseResources.size() == 1) {
			return responseResources.get(0);
		}

		return responseResources;
	}

	private List<InputStream> getInput(ZipTask zipTask) {
		if (zipTask.getInputResources() != null)
			return zipTask.getInputResources() //
					.stream() //
					.map(res -> res.openStream()) //
					.collect(Collectors.toList());

		URL resource = getClass().getResource("zip-default-resource.txt.vm");

		try {
			return CollectionTools.getList(resource.openStream());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}
