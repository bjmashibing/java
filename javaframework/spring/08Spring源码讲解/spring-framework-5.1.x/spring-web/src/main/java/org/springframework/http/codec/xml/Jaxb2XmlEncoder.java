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

package org.springframework.http.codec.xml;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;

import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.ResolvableType;
import org.springframework.core.codec.AbstractSingleValueEncoder;
import org.springframework.core.codec.CodecException;
import org.springframework.core.codec.EncodingException;
import org.springframework.core.codec.Hints;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

/**
 * Encode from single value to a byte stream containing XML elements.
 *
 * <p>{@link javax.xml.bind.annotation.XmlElements @XmlElements} and
 * {@link javax.xml.bind.annotation.XmlElement @XmlElement} can be used
 * to specify how collections should be marshalled.
 *
 * @author Sebastien Deleuze
 * @author Arjen Poutsma
 * @since 5.0
 * @see Jaxb2XmlDecoder
 */
public class Jaxb2XmlEncoder extends AbstractSingleValueEncoder<Object> {

	private final JaxbContextContainer jaxbContexts = new JaxbContextContainer();

	private Function<Marshaller, Marshaller> marshallerProcessor = Function.identity();


	public Jaxb2XmlEncoder() {
		super(MimeTypeUtils.APPLICATION_XML, MimeTypeUtils.TEXT_XML);
	}


	/**
	 * Configure a processor function to customize Marshaller instances.
	 * @param processor the function to use
	 * @since 5.1.3
	 */
	public void setMarshallerProcessor(Function<Marshaller, Marshaller> processor) {
		this.marshallerProcessor = this.marshallerProcessor.andThen(processor);
	}

	/**
	 * Return the configured processor for customizing Marshaller instances.
	 * @since 5.1.3
	 */
	public Function<Marshaller, Marshaller> getMarshallerProcessor() {
		return this.marshallerProcessor;
	}


	@Override
	public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
		if (super.canEncode(elementType, mimeType)) {
			Class<?> outputClass = elementType.toClass();
			return (outputClass.isAnnotationPresent(XmlRootElement.class) ||
					outputClass.isAnnotationPresent(XmlType.class));
		}
		else {
			return false;
		}
	}

	@Override
	protected Flux<DataBuffer> encode(Object value, DataBufferFactory bufferFactory,
			ResolvableType type, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {

		if (!Hints.isLoggingSuppressed(hints)) {
			LogFormatUtils.traceDebug(logger, traceOn -> {
				String formatted = LogFormatUtils.formatValue(value, !traceOn);
				return Hints.getLogPrefix(hints) + "Encoding [" + formatted + "]";
			});
		}

		return Mono.fromCallable(() -> {
			boolean release = true;
			DataBuffer buffer = bufferFactory.allocateBuffer(1024);
			try {
				OutputStream outputStream = buffer.asOutputStream();
				Class<?> clazz = ClassUtils.getUserClass(value);
				Marshaller marshaller = initMarshaller(clazz);
				marshaller.marshal(value, outputStream);
				release = false;
				return buffer;  // relying on doOnDiscard in base class
			}
			catch (MarshalException ex) {
				throw new EncodingException(
						"Could not marshal " + value.getClass() + " to XML", ex);
			}
			catch (JAXBException ex) {
				throw new CodecException("Invalid JAXB configuration", ex);
			}
			finally {
				if (release) {
					DataBufferUtils.release(buffer);
				}
			}
		}).flux();
	}

	private Marshaller initMarshaller(Class<?> clazz) throws CodecException, JAXBException {
		Marshaller marshaller = this.jaxbContexts.createMarshaller(clazz);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
		marshaller = this.marshallerProcessor.apply(marshaller);
		return marshaller;
	}

}
