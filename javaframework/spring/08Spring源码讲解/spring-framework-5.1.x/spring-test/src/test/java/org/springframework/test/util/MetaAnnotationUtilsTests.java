/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.test.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.springframework.test.util.MetaAnnotationUtils.*;

/**
 * Unit tests for {@link MetaAnnotationUtils}.
 *
 * @author Sam Brannen
 * @since 4.0
 * @see OverriddenMetaAnnotationAttributesTests
 */
public class MetaAnnotationUtilsTests {

	private void assertAtComponentOnComposedAnnotation(
			Class<?> rootDeclaringClass, String name, Class<? extends Annotation> composedAnnotationType) {

		assertAtComponentOnComposedAnnotation(rootDeclaringClass, rootDeclaringClass, name, composedAnnotationType);
	}

	private void assertAtComponentOnComposedAnnotation(
			Class<?> startClass, Class<?> rootDeclaringClass, String name, Class<? extends Annotation> composedAnnotationType) {

		assertAtComponentOnComposedAnnotation(startClass, rootDeclaringClass, composedAnnotationType, name, composedAnnotationType);
	}

	private void assertAtComponentOnComposedAnnotation(Class<?> startClass, Class<?> rootDeclaringClass,
			Class<?> declaringClass, String name, Class<? extends Annotation> composedAnnotationType) {

		AnnotationDescriptor<Component> descriptor = findAnnotationDescriptor(startClass, Component.class);
		assertNotNull("AnnotationDescriptor should not be null", descriptor);
		assertEquals("rootDeclaringClass", rootDeclaringClass, descriptor.getRootDeclaringClass());
		assertEquals("declaringClass", declaringClass, descriptor.getDeclaringClass());
		assertEquals("annotationType", Component.class, descriptor.getAnnotationType());
		assertEquals("component name", name, descriptor.getAnnotation().value());
		assertNotNull("composedAnnotation should not be null", descriptor.getComposedAnnotation());
		assertEquals("composedAnnotationType", composedAnnotationType, descriptor.getComposedAnnotationType());
	}

	private void assertAtComponentOnComposedAnnotationForMultipleCandidateTypes(
			Class<?> startClass, String name, Class<? extends Annotation> composedAnnotationType) {

		assertAtComponentOnComposedAnnotationForMultipleCandidateTypes(
				startClass, startClass, name, composedAnnotationType);
	}

	private void assertAtComponentOnComposedAnnotationForMultipleCandidateTypes(Class<?> startClass,
			Class<?> rootDeclaringClass, String name, Class<? extends Annotation> composedAnnotationType) {

		assertAtComponentOnComposedAnnotationForMultipleCandidateTypes(
				startClass, rootDeclaringClass, composedAnnotationType, name, composedAnnotationType);
	}

	@SuppressWarnings("unchecked")
	private void assertAtComponentOnComposedAnnotationForMultipleCandidateTypes(Class<?> startClass,
			Class<?> rootDeclaringClass, Class<?> declaringClass, String name,
			Class<? extends Annotation> composedAnnotationType) {

		Class<Component> annotationType = Component.class;
		UntypedAnnotationDescriptor descriptor = findAnnotationDescriptorForTypes(
				startClass, Service.class, annotationType, Order.class, Transactional.class);

		assertNotNull("UntypedAnnotationDescriptor should not be null", descriptor);
		assertEquals("rootDeclaringClass", rootDeclaringClass, descriptor.getRootDeclaringClass());
		assertEquals("declaringClass", declaringClass, descriptor.getDeclaringClass());
		assertEquals("annotationType", annotationType, descriptor.getAnnotationType());
		assertEquals("component name", name, ((Component) descriptor.getAnnotation()).value());
		assertNotNull("composedAnnotation should not be null", descriptor.getComposedAnnotation());
		assertEquals("composedAnnotationType", composedAnnotationType, descriptor.getComposedAnnotationType());
	}

	@Test
	public void findAnnotationDescriptorWithNoAnnotationPresent() {
		assertNull(findAnnotationDescriptor(NonAnnotatedInterface.class, Transactional.class));
		assertNull(findAnnotationDescriptor(NonAnnotatedClass.class, Transactional.class));
	}

	@Test
	public void findAnnotationDescriptorWithInheritedAnnotationOnClass() {
		// Note: @Transactional is inherited
		assertEquals(InheritedAnnotationClass.class,
				findAnnotationDescriptor(InheritedAnnotationClass.class, Transactional.class).getRootDeclaringClass());
		assertEquals(InheritedAnnotationClass.class,
				findAnnotationDescriptor(SubInheritedAnnotationClass.class, Transactional.class).getRootDeclaringClass());
	}

	@Test
	public void findAnnotationDescriptorWithInheritedAnnotationOnInterface() {
		// Note: @Transactional is inherited
		Transactional rawAnnotation = InheritedAnnotationInterface.class.getAnnotation(Transactional.class);

		AnnotationDescriptor<Transactional> descriptor =
				findAnnotationDescriptor(InheritedAnnotationInterface.class, Transactional.class);
		assertNotNull(descriptor);
		assertEquals(InheritedAnnotationInterface.class, descriptor.getRootDeclaringClass());
		assertEquals(InheritedAnnotationInterface.class, descriptor.getDeclaringClass());
		assertEquals(rawAnnotation, descriptor.getAnnotation());

		descriptor = findAnnotationDescriptor(SubInheritedAnnotationInterface.class, Transactional.class);
		assertNotNull(descriptor);
		assertEquals(SubInheritedAnnotationInterface.class, descriptor.getRootDeclaringClass());
		assertEquals(InheritedAnnotationInterface.class, descriptor.getDeclaringClass());
		assertEquals(rawAnnotation, descriptor.getAnnotation());

		descriptor = findAnnotationDescriptor(SubSubInheritedAnnotationInterface.class, Transactional.class);
		assertNotNull(descriptor);
		assertEquals(SubSubInheritedAnnotationInterface.class, descriptor.getRootDeclaringClass());
		assertEquals(InheritedAnnotationInterface.class, descriptor.getDeclaringClass());
		assertEquals(rawAnnotation, descriptor.getAnnotation());
	}

	@Test
	public void findAnnotationDescriptorForNonInheritedAnnotationOnClass() {
		// Note: @Order is not inherited.
		assertEquals(NonInheritedAnnotationClass.class,
				findAnnotationDescriptor(NonInheritedAnnotationClass.class, Order.class).getRootDeclaringClass());
		assertEquals(NonInheritedAnnotationClass.class,
				findAnnotationDescriptor(SubNonInheritedAnnotationClass.class, Order.class).getRootDeclaringClass());
	}

	@Test
	public void findAnnotationDescriptorForNonInheritedAnnotationOnInterface() {
		// Note: @Order is not inherited.
		Order rawAnnotation = NonInheritedAnnotationInterface.class.getAnnotation(Order.class);

		AnnotationDescriptor<Order> descriptor =
				findAnnotationDescriptor(NonInheritedAnnotationInterface.class, Order.class);
		assertNotNull(descriptor);
		assertEquals(NonInheritedAnnotationInterface.class, descriptor.getRootDeclaringClass());
		assertEquals(NonInheritedAnnotationInterface.class, descriptor.getDeclaringClass());
		assertEquals(rawAnnotation, descriptor.getAnnotation());

		descriptor = findAnnotationDescriptor(SubNonInheritedAnnotationInterface.class, Order.class);
		assertNotNull(descriptor);
		assertEquals(SubNonInheritedAnnotationInterface.class, descriptor.getRootDeclaringClass());
		assertEquals(NonInheritedAnnotationInterface.class, descriptor.getDeclaringClass());
		assertEquals(rawAnnotation, descriptor.getAnnotation());
	}

	@Test
	public void findAnnotationDescriptorWithMetaComponentAnnotation() {
		assertAtComponentOnComposedAnnotation(HasMetaComponentAnnotation.class, "meta1", Meta1.class);
	}

	@Test
	public void findAnnotationDescriptorWithLocalAndMetaComponentAnnotation() {
		Class<Component> annotationType = Component.class;
		AnnotationDescriptor<Component> descriptor = findAnnotationDescriptor(
				HasLocalAndMetaComponentAnnotation.class, annotationType);

		assertEquals(HasLocalAndMetaComponentAnnotation.class, descriptor.getRootDeclaringClass());
		assertEquals(annotationType, descriptor.getAnnotationType());
		assertNull(descriptor.getComposedAnnotation());
		assertNull(descriptor.getComposedAnnotationType());
	}

	@Test
	public void findAnnotationDescriptorForInterfaceWithMetaAnnotation() {
		assertAtComponentOnComposedAnnotation(InterfaceWithMetaAnnotation.class, "meta1", Meta1.class);
	}

	@Test
	public void findAnnotationDescriptorForClassWithMetaAnnotatedInterface() {
		Component rawAnnotation = AnnotationUtils.findAnnotation(ClassWithMetaAnnotatedInterface.class, Component.class);
		AnnotationDescriptor<Component> descriptor =
				findAnnotationDescriptor(ClassWithMetaAnnotatedInterface.class, Component.class);

		assertNotNull(descriptor);
		assertEquals(ClassWithMetaAnnotatedInterface.class, descriptor.getRootDeclaringClass());
		assertEquals(Meta1.class, descriptor.getDeclaringClass());
		assertEquals(rawAnnotation, descriptor.getAnnotation());
		assertEquals(Meta1.class, descriptor.getComposedAnnotation().annotationType());
	}

	@Test
	public void findAnnotationDescriptorForClassWithLocalMetaAnnotationAndAnnotatedSuperclass() {
		AnnotationDescriptor<ContextConfiguration> descriptor = findAnnotationDescriptor(
				MetaAnnotatedAndSuperAnnotatedContextConfigClass.class, ContextConfiguration.class);

		assertNotNull("AnnotationDescriptor should not be null", descriptor);
		assertEquals("rootDeclaringClass", MetaAnnotatedAndSuperAnnotatedContextConfigClass.class, descriptor.getRootDeclaringClass());
		assertEquals("declaringClass", MetaConfig.class, descriptor.getDeclaringClass());
		assertEquals("annotationType", ContextConfiguration.class, descriptor.getAnnotationType());
		assertNotNull("composedAnnotation should not be null", descriptor.getComposedAnnotation());
		assertEquals("composedAnnotationType", MetaConfig.class, descriptor.getComposedAnnotationType());

		assertArrayEquals("configured classes", new Class<?>[] {String.class},
				descriptor.getAnnotationAttributes().getClassArray("classes"));
	}

	@Test
	public void findAnnotationDescriptorForClassWithLocalMetaAnnotationAndMetaAnnotatedInterface() {
		assertAtComponentOnComposedAnnotation(ClassWithLocalMetaAnnotationAndMetaAnnotatedInterface.class, "meta2", Meta2.class);
	}

	@Test
	public void findAnnotationDescriptorForSubClassWithLocalMetaAnnotationAndMetaAnnotatedInterface() {
		assertAtComponentOnComposedAnnotation(SubClassWithLocalMetaAnnotationAndMetaAnnotatedInterface.class,
				ClassWithLocalMetaAnnotationAndMetaAnnotatedInterface.class, "meta2", Meta2.class);
	}

	/**
	 * @since 4.0.3
	 */
	@Test
	public void findAnnotationDescriptorOnMetaMetaAnnotatedClass() {
		Class<MetaMetaAnnotatedClass> startClass = MetaMetaAnnotatedClass.class;
		assertAtComponentOnComposedAnnotation(startClass, startClass, Meta2.class, "meta2", MetaMeta.class);
	}

	/**
	 * @since 4.0.3
	 */
	@Test
	public void findAnnotationDescriptorOnMetaMetaMetaAnnotatedClass() {
		Class<MetaMetaMetaAnnotatedClass> startClass = MetaMetaMetaAnnotatedClass.class;
		assertAtComponentOnComposedAnnotation(startClass, startClass, Meta2.class, "meta2", MetaMetaMeta.class);
	}

	/**
	 * @since 4.0.3
	 */
	@Test
	public void findAnnotationDescriptorOnAnnotatedClassWithMissingTargetMetaAnnotation() {
		// InheritedAnnotationClass is NOT annotated or meta-annotated with @Component
		AnnotationDescriptor<Component> descriptor = findAnnotationDescriptor(
				InheritedAnnotationClass.class, Component.class);
		assertNull("Should not find @Component on InheritedAnnotationClass", descriptor);
	}

	/**
	 * @since 4.0.3
	 */
	@Test
	public void findAnnotationDescriptorOnMetaCycleAnnotatedClassWithMissingTargetMetaAnnotation() {
		AnnotationDescriptor<Component> descriptor = findAnnotationDescriptor(
				MetaCycleAnnotatedClass.class, Component.class);
		assertNull("Should not find @Component on MetaCycleAnnotatedClass", descriptor);
	}

	// -------------------------------------------------------------------------

	@Test
	@SuppressWarnings("unchecked")
	public void findAnnotationDescriptorForTypesWithNoAnnotationPresent() {
		assertNull(findAnnotationDescriptorForTypes(NonAnnotatedInterface.class, Transactional.class, Component.class));
		assertNull(findAnnotationDescriptorForTypes(NonAnnotatedClass.class, Transactional.class, Order.class));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void findAnnotationDescriptorForTypesWithInheritedAnnotationOnClass() {
		// Note: @Transactional is inherited
		assertEquals(InheritedAnnotationClass.class,
				findAnnotationDescriptorForTypes(InheritedAnnotationClass.class, Transactional.class).getRootDeclaringClass());
		assertEquals(
				InheritedAnnotationClass.class,
				findAnnotationDescriptorForTypes(SubInheritedAnnotationClass.class, Transactional.class).getRootDeclaringClass());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void findAnnotationDescriptorForTypesWithInheritedAnnotationOnInterface() {
		// Note: @Transactional is inherited
		Transactional rawAnnotation = InheritedAnnotationInterface.class.getAnnotation(Transactional.class);

		UntypedAnnotationDescriptor descriptor =
				findAnnotationDescriptorForTypes(InheritedAnnotationInterface.class, Transactional.class);
		assertNotNull(descriptor);
		assertEquals(InheritedAnnotationInterface.class, descriptor.getRootDeclaringClass());
		assertEquals(InheritedAnnotationInterface.class, descriptor.getDeclaringClass());
		assertEquals(rawAnnotation, descriptor.getAnnotation());

		descriptor = findAnnotationDescriptorForTypes(SubInheritedAnnotationInterface.class, Transactional.class);
		assertNotNull(descriptor);
		assertEquals(SubInheritedAnnotationInterface.class, descriptor.getRootDeclaringClass());
		assertEquals(InheritedAnnotationInterface.class, descriptor.getDeclaringClass());
		assertEquals(rawAnnotation, descriptor.getAnnotation());

		descriptor = findAnnotationDescriptorForTypes(SubSubInheritedAnnotationInterface.class, Transactional.class);
		assertNotNull(descriptor);
		assertEquals(SubSubInheritedAnnotationInterface.class, descriptor.getRootDeclaringClass());
		assertEquals(InheritedAnnotationInterface.class, descriptor.getDeclaringClass());
		assertEquals(rawAnnotation, descriptor.getAnnotation());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void findAnnotationDescriptorForTypesForNonInheritedAnnotationOnClass() {
		// Note: @Order is not inherited.
		assertEquals(NonInheritedAnnotationClass.class,
				findAnnotationDescriptorForTypes(NonInheritedAnnotationClass.class, Order.class).getRootDeclaringClass());
		assertEquals(NonInheritedAnnotationClass.class,
				findAnnotationDescriptorForTypes(SubNonInheritedAnnotationClass.class, Order.class).getRootDeclaringClass());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void findAnnotationDescriptorForTypesForNonInheritedAnnotationOnInterface() {
		// Note: @Order is not inherited.
		Order rawAnnotation = NonInheritedAnnotationInterface.class.getAnnotation(Order.class);

		UntypedAnnotationDescriptor descriptor =
				findAnnotationDescriptorForTypes(NonInheritedAnnotationInterface.class, Order.class);
		assertNotNull(descriptor);
		assertEquals(NonInheritedAnnotationInterface.class, descriptor.getRootDeclaringClass());
		assertEquals(NonInheritedAnnotationInterface.class, descriptor.getDeclaringClass());
		assertEquals(rawAnnotation, descriptor.getAnnotation());

		descriptor = findAnnotationDescriptorForTypes(SubNonInheritedAnnotationInterface.class, Order.class);
		assertNotNull(descriptor);
		assertEquals(SubNonInheritedAnnotationInterface.class, descriptor.getRootDeclaringClass());
		assertEquals(NonInheritedAnnotationInterface.class, descriptor.getDeclaringClass());
		assertEquals(rawAnnotation, descriptor.getAnnotation());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void findAnnotationDescriptorForTypesWithLocalAndMetaComponentAnnotation() {
		Class<Component> annotationType = Component.class;
		UntypedAnnotationDescriptor descriptor = findAnnotationDescriptorForTypes(
				HasLocalAndMetaComponentAnnotation.class, Transactional.class, annotationType, Order.class);
		assertEquals(HasLocalAndMetaComponentAnnotation.class, descriptor.getRootDeclaringClass());
		assertEquals(annotationType, descriptor.getAnnotationType());
		assertNull(descriptor.getComposedAnnotation());
		assertNull(descriptor.getComposedAnnotationType());
	}

	@Test
	public void findAnnotationDescriptorForTypesWithMetaComponentAnnotation() {
		Class<HasMetaComponentAnnotation> startClass = HasMetaComponentAnnotation.class;
		assertAtComponentOnComposedAnnotationForMultipleCandidateTypes(startClass, "meta1", Meta1.class);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void findAnnotationDescriptorForTypesWithMetaAnnotationWithDefaultAttributes() {
		Class<?> startClass = MetaConfigWithDefaultAttributesTestCase.class;
		Class<ContextConfiguration> annotationType = ContextConfiguration.class;

		UntypedAnnotationDescriptor descriptor = findAnnotationDescriptorForTypes(startClass,
				Service.class, ContextConfiguration.class, Order.class, Transactional.class);

		assertNotNull(descriptor);
		assertEquals(startClass, descriptor.getRootDeclaringClass());
		assertEquals(annotationType, descriptor.getAnnotationType());
		assertArrayEquals(new Class<?>[] {}, ((ContextConfiguration) descriptor.getAnnotation()).value());
		assertArrayEquals(new Class<?>[] {MetaConfig.DevConfig.class, MetaConfig.ProductionConfig.class},
				descriptor.getAnnotationAttributes().getClassArray("classes"));
		assertNotNull(descriptor.getComposedAnnotation());
		assertEquals(MetaConfig.class, descriptor.getComposedAnnotationType());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void findAnnotationDescriptorForTypesWithMetaAnnotationWithOverriddenAttributes() {
		Class<?> startClass = MetaConfigWithOverriddenAttributesTestCase.class;
		Class<ContextConfiguration> annotationType = ContextConfiguration.class;

		UntypedAnnotationDescriptor descriptor = findAnnotationDescriptorForTypes(
				startClass, Service.class, ContextConfiguration.class, Order.class, Transactional.class);

		assertNotNull(descriptor);
		assertEquals(startClass, descriptor.getRootDeclaringClass());
		assertEquals(annotationType, descriptor.getAnnotationType());
		assertArrayEquals(new Class<?>[] {}, ((ContextConfiguration) descriptor.getAnnotation()).value());
		assertArrayEquals(new Class<?>[] {MetaAnnotationUtilsTests.class},
				descriptor.getAnnotationAttributes().getClassArray("classes"));
		assertNotNull(descriptor.getComposedAnnotation());
		assertEquals(MetaConfig.class, descriptor.getComposedAnnotationType());
	}

	@Test
	public void findAnnotationDescriptorForTypesForInterfaceWithMetaAnnotation() {
		Class<InterfaceWithMetaAnnotation> startClass = InterfaceWithMetaAnnotation.class;
		assertAtComponentOnComposedAnnotationForMultipleCandidateTypes(startClass, "meta1", Meta1.class);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void findAnnotationDescriptorForTypesForClassWithMetaAnnotatedInterface() {
		Component rawAnnotation = AnnotationUtils.findAnnotation(ClassWithMetaAnnotatedInterface.class, Component.class);

		UntypedAnnotationDescriptor descriptor = findAnnotationDescriptorForTypes(
				ClassWithMetaAnnotatedInterface.class, Service.class, Component.class, Order.class, Transactional.class);

		assertNotNull(descriptor);
		assertEquals(ClassWithMetaAnnotatedInterface.class, descriptor.getRootDeclaringClass());
		assertEquals(Meta1.class, descriptor.getDeclaringClass());
		assertEquals(rawAnnotation, descriptor.getAnnotation());
		assertEquals(Meta1.class, descriptor.getComposedAnnotation().annotationType());
	}

	@Test
	public void findAnnotationDescriptorForTypesForClassWithLocalMetaAnnotationAndMetaAnnotatedInterface() {
		Class<ClassWithLocalMetaAnnotationAndMetaAnnotatedInterface> startClass = ClassWithLocalMetaAnnotationAndMetaAnnotatedInterface.class;
		assertAtComponentOnComposedAnnotationForMultipleCandidateTypes(startClass, "meta2", Meta2.class);
	}

	@Test
	public void findAnnotationDescriptorForTypesForSubClassWithLocalMetaAnnotationAndMetaAnnotatedInterface() {
		assertAtComponentOnComposedAnnotationForMultipleCandidateTypes(
				SubClassWithLocalMetaAnnotationAndMetaAnnotatedInterface.class,
				ClassWithLocalMetaAnnotationAndMetaAnnotatedInterface.class, "meta2", Meta2.class);
	}

	/**
	 * @since 4.0.3
	 */
	@Test
	public void findAnnotationDescriptorForTypesOnMetaMetaAnnotatedClass() {
		Class<MetaMetaAnnotatedClass> startClass = MetaMetaAnnotatedClass.class;
		assertAtComponentOnComposedAnnotationForMultipleCandidateTypes(
				startClass, startClass, Meta2.class, "meta2", MetaMeta.class);
	}

	/**
	 * @since 4.0.3
	 */
	@Test
	public void findAnnotationDescriptorForTypesOnMetaMetaMetaAnnotatedClass() {
		Class<MetaMetaMetaAnnotatedClass> startClass = MetaMetaMetaAnnotatedClass.class;
		assertAtComponentOnComposedAnnotationForMultipleCandidateTypes(
				startClass, startClass, Meta2.class, "meta2", MetaMetaMeta.class);
	}

	/**
	 * @since 4.0.3
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void findAnnotationDescriptorForTypesOnAnnotatedClassWithMissingTargetMetaAnnotation() {
		// InheritedAnnotationClass is NOT annotated or meta-annotated with @Component,
		// @Service, or @Order, but it is annotated with @Transactional.
		UntypedAnnotationDescriptor descriptor = findAnnotationDescriptorForTypes(
				InheritedAnnotationClass.class, Service.class, Component.class, Order.class);
		assertNull("Should not find @Component on InheritedAnnotationClass", descriptor);
	}

	/**
	 * @since 4.0.3
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void findAnnotationDescriptorForTypesOnMetaCycleAnnotatedClassWithMissingTargetMetaAnnotation() {
		UntypedAnnotationDescriptor descriptor = findAnnotationDescriptorForTypes(
				MetaCycleAnnotatedClass.class, Service.class, Component.class, Order.class);
		assertNull("Should not find @Component on MetaCycleAnnotatedClass", descriptor);
	}


	// -------------------------------------------------------------------------

	@Component(value = "meta1")
	@Order
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Documented
	static @interface Meta1 {
	}

	@Component(value = "meta2")
	@Transactional
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Documented
	static @interface Meta2 {
	}

	@Meta2
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Documented
	@interface MetaMeta {
	}

	@MetaMeta
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Documented
	@interface MetaMetaMeta {
	}

	@MetaCycle3
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.ANNOTATION_TYPE)
	@Documented
	@interface MetaCycle1 {
	}

	@MetaCycle1
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.ANNOTATION_TYPE)
	@Documented
	@interface MetaCycle2 {
	}

	@MetaCycle2
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Documented
	@interface MetaCycle3 {
	}

	@ContextConfiguration
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Documented
	static @interface MetaConfig {

		static class DevConfig {
		}

		static class ProductionConfig {
		}


		Class<?>[] classes() default { DevConfig.class, ProductionConfig.class };
	}

	// -------------------------------------------------------------------------

	@Meta1
	static class HasMetaComponentAnnotation {
	}

	@Meta1
	@Component(value = "local")
	@Meta2
	static class HasLocalAndMetaComponentAnnotation {
	}

	@Meta1
	static interface InterfaceWithMetaAnnotation {
	}

	static class ClassWithMetaAnnotatedInterface implements InterfaceWithMetaAnnotation {
	}

	@Meta2
	static class ClassWithLocalMetaAnnotationAndMetaAnnotatedInterface implements InterfaceWithMetaAnnotation {
	}

	static class SubClassWithLocalMetaAnnotationAndMetaAnnotatedInterface extends
			ClassWithLocalMetaAnnotationAndMetaAnnotatedInterface {
	}

	@MetaMeta
	static class MetaMetaAnnotatedClass {
	}

	@MetaMetaMeta
	static class MetaMetaMetaAnnotatedClass {
	}

	@MetaCycle3
	static class MetaCycleAnnotatedClass {
	}

	@MetaConfig
	public class MetaConfigWithDefaultAttributesTestCase {
	}

	@MetaConfig(classes = MetaAnnotationUtilsTests.class)
	public class MetaConfigWithOverriddenAttributesTestCase {
	}

	// -------------------------------------------------------------------------

	@Transactional
	static interface InheritedAnnotationInterface {
	}

	static interface SubInheritedAnnotationInterface extends InheritedAnnotationInterface {
	}

	static interface SubSubInheritedAnnotationInterface extends SubInheritedAnnotationInterface {
	}

	@Order
	static interface NonInheritedAnnotationInterface {
	}

	static interface SubNonInheritedAnnotationInterface extends NonInheritedAnnotationInterface {
	}

	static class NonAnnotatedClass {
	}

	static interface NonAnnotatedInterface {
	}

	@Transactional
	static class InheritedAnnotationClass {
	}

	static class SubInheritedAnnotationClass extends InheritedAnnotationClass {
	}

	@Order
	static class NonInheritedAnnotationClass {
	}

	static class SubNonInheritedAnnotationClass extends NonInheritedAnnotationClass {
	}

	@ContextConfiguration(classes = Number.class)
	static class AnnotatedContextConfigClass {
	}

	@MetaConfig(classes = String.class)
	static class MetaAnnotatedAndSuperAnnotatedContextConfigClass extends AnnotatedContextConfigClass {
	}

}
