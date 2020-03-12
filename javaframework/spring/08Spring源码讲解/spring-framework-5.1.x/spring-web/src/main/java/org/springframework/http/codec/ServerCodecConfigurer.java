/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.http.codec;

import org.springframework.core.codec.Encoder;

/**
 * Extension of {@link CodecConfigurer} for HTTP message reader and writer
 * options relevant on the server side.
 *
 * <p>HTTP message readers for the following are registered by default:
 * <ul>{@code byte[]}
 * <li>{@link java.nio.ByteBuffer}
 * <li>{@link org.springframework.core.io.buffer.DataBuffer DataBuffer}
 * <li>{@link org.springframework.core.io.Resource Resource}
 * <li>{@link String}
 * <li>{@link org.springframework.util.MultiValueMap
 * MultiValueMap&lt;String,String&gt;} for form data
 * <li>{@link org.springframework.util.MultiValueMap
 * MultiValueMap&lt;String,Object&gt;} for multipart data
 * <li>JSON and Smile, if Jackson is present
 * <li>XML, if JAXB2 is present
 * </ul>
 *
 * <p>HTTP message writers registered by default:
 * <ul>{@code byte[]}
 * <li>{@link java.nio.ByteBuffer}
 * <li>{@link org.springframework.core.io.buffer.DataBuffer DataBuffer}
 * <li>{@link org.springframework.core.io.Resource Resource}
 * <li>{@link String}
 * <li>{@link org.springframework.util.MultiValueMap
 * MultiValueMap&lt;String,String&gt;} for form data
 * <li>JSON and Smile, if Jackson is present
 * <li>XML, if JAXB2 is present
 * <li>Server-Sent Events
 * </ul>
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 */
public interface ServerCodecConfigurer extends CodecConfigurer {

	/**
	 * {@inheritDoc}
	 * <p>On the server side, built-in default also include customizations
	 * related to the encoder for SSE.
	 */
	@Override
	ServerDefaultCodecs defaultCodecs();

	/**
	 * {@inheritDoc}.
	 */
	@Override
	ServerCodecConfigurer clone();


	/**
	 * Static factory method for a {@code ServerCodecConfigurer}.
	 */
	static ServerCodecConfigurer create() {
		return CodecConfigurerFactory.create(ServerCodecConfigurer.class);
	}


	/**
	 * {@link CodecConfigurer.DefaultCodecs} extension with extra client-side options.
	 */
	interface ServerDefaultCodecs extends DefaultCodecs {

		/**
		 * Configure the {@code HttpMessageReader} to use for multipart requests.
		 * <p>By default, if
		 * <a href="https://github.com/synchronoss/nio-multipart">Synchronoss NIO Multipart</a>
		 * is present, this is set to
		 * {@link org.springframework.http.codec.multipart.MultipartHttpMessageReader
		 * MultipartHttpMessageReader} created with an instance of
		 * {@link org.springframework.http.codec.multipart.SynchronossPartHttpMessageReader
		 * SynchronossPartHttpMessageReader}.
		 * <p>Note that {@link #maxInMemorySize(int)} and/or
		 * {@link #enableLoggingRequestDetails(boolean)}, if configured, will be
		 * applied to the given reader, if applicable.
		 * @param reader the message reader to use for multipart requests.
		 * @since 5.1.11
		 */
		void multipartReader(HttpMessageReader<?> reader);

		/**
		 * Configure the {@code Encoder} to use for Server-Sent Events.
		 * <p>By default if this is not set, and Jackson is available, the
		 * {@link #jackson2JsonEncoder} override is used instead. Use this property
		 * if you want to further customize the SSE encoder.
		 */
		void serverSentEventEncoder(Encoder<?> encoder);
	}

}
