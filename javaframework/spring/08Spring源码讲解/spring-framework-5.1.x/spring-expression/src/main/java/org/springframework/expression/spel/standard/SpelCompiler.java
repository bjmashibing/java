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

package org.springframework.expression.spel.standard;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.asm.ClassWriter;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.CodeFlow;
import org.springframework.expression.spel.CompiledExpression;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.ast.SpelNodeImpl;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * A SpelCompiler will take a regular parsed expression and create (and load) a class
 * containing byte code that does the same thing as that expression. The compiled form of
 * an expression will evaluate far faster than the interpreted form.
 *
 * <p>The SpelCompiler is not currently handling all expression types but covers many of
 * the common cases. The framework is extensible to cover more cases in the future. For
 * absolute maximum speed there is *no checking* in the compiled code. The compiled
 * version of the expression uses information learned during interpreted runs of the
 * expression when it generates the byte code. For example if it knows that a particular
 * property dereference always seems to return a Map then it will generate byte code that
 * expects the result of the property dereference to be a Map. This ensures maximal
 * performance but should the dereference result in something other than a map, the
 * compiled expression will fail - like a ClassCastException would occur if passing data
 * of an unexpected type in a regular Java program.
 *
 * <p>Due to the lack of checking there are likely some expressions that should never be
 * compiled, for example if an expression is continuously dealing with different types of
 * data. Due to these cases the compiler is something that must be selectively turned on
 * for an associated SpelExpressionParser (through the {@link SpelParserConfiguration}
 * object), it is not on by default.
 *
 * <p>Individual expressions can be compiled by calling {@code SpelCompiler.compile(expression)}.
 *
 * @author Andy Clement
 * @since 4.1
 */
public final class SpelCompiler implements Opcodes {

	private static final Log logger = LogFactory.getLog(SpelCompiler.class);

	private static final int CLASSES_DEFINED_LIMIT = 100;

	// A compiler is created for each classloader, it manages a child class loader of that
	// classloader and the child is used to load the compiled expressions.
	private static final Map<ClassLoader, SpelCompiler> compilers = new ConcurrentReferenceHashMap<>();

	// The child ClassLoader used to load the compiled expression classes
	private ChildClassLoader ccl;

	// Counter suffix for generated classes within this SpelCompiler instance
	private final AtomicInteger suffixId = new AtomicInteger(1);


	private SpelCompiler(@Nullable ClassLoader classloader) {
		this.ccl = new ChildClassLoader(classloader);
	}


	/**
	 * Attempt compilation of the supplied expression. A check is made to see
	 * if it is compilable before compilation proceeds. The check involves
	 * visiting all the nodes in the expression Ast and ensuring enough state
	 * is known about them that bytecode can be generated for them.
	 * @param expression the expression to compile
	 * @return an instance of the class implementing the compiled expression,
	 * or {@code null} if compilation is not possible
	 */
	@Nullable
	public CompiledExpression compile(SpelNodeImpl expression) {
		if (expression.isCompilable()) {
			if (logger.isDebugEnabled()) {
				logger.debug("SpEL: compiling " + expression.toStringAST());
			}
			Class<? extends CompiledExpression> clazz = createExpressionClass(expression);
			if (clazz != null) {
				try {
					return ReflectionUtils.accessibleConstructor(clazz).newInstance();
				}
				catch (Throwable ex) {
					throw new IllegalStateException("Failed to instantiate CompiledExpression", ex);
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SpEL: unable to compile " + expression.toStringAST());
		}
		return null;
	}

	private int getNextSuffix() {
		return this.suffixId.incrementAndGet();
	}

	/**
	 * Generate the class that encapsulates the compiled expression and define it.
	 * The  generated class will be a subtype of CompiledExpression.
	 * @param expressionToCompile the expression to be compiled
	 * @return the expression call, or {@code null} if the decision was to opt out of
	 * compilation during code generation
	 */
	@Nullable
	private Class<? extends CompiledExpression> createExpressionClass(SpelNodeImpl expressionToCompile) {
		// Create class outline 'spel/ExNNN extends org.springframework.expression.spel.CompiledExpression'
		String className = "spel/Ex" + getNextSuffix();
		ClassWriter cw = new ExpressionClassWriter();
		cw.visit(V1_5, ACC_PUBLIC, className, null, "org/springframework/expression/spel/CompiledExpression", null);

		// Create default constructor
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "org/springframework/expression/spel/CompiledExpression",
				"<init>", "()V", false);
		mv.visitInsn(RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();

		// Create getValue() method
		mv = cw.visitMethod(ACC_PUBLIC, "getValue",
				"(Ljava/lang/Object;Lorg/springframework/expression/EvaluationContext;)Ljava/lang/Object;", null,
				new String[ ]{"org/springframework/expression/EvaluationException"});
		mv.visitCode();

		CodeFlow cf = new CodeFlow(className, cw);

		// Ask the expression AST to generate the body of the method
		try {
			expressionToCompile.generateCode(mv, cf);
		}
		catch (IllegalStateException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug(expressionToCompile.getClass().getSimpleName() +
						".generateCode opted out of compilation: " + ex.getMessage());
			}
			return null;
		}

		CodeFlow.insertBoxIfNecessary(mv, cf.lastDescriptor());
		if ("V".equals(cf.lastDescriptor())) {
			mv.visitInsn(ACONST_NULL);
		}
		mv.visitInsn(ARETURN);

		mv.visitMaxs(0, 0);  // not supplied due to COMPUTE_MAXS
		mv.visitEnd();
		cw.visitEnd();

		cf.finish();

		byte[] data = cw.toByteArray();
		// TODO need to make this conditionally occur based on a debug flag
		// dump(expressionToCompile.toStringAST(), clazzName, data);
		return loadClass(StringUtils.replace(className, "/", "."), data);
	}

	/**
	 * Load a compiled expression class. Makes sure the classloaders aren't used too much
	 * because they anchor compiled classes in memory and prevent GC.  If you have expressions
	 * continually recompiling over time then by replacing the classloader periodically
	 * at least some of the older variants can be garbage collected.
	 * @param name name of the class
	 * @param bytes bytecode for the class
	 * @return the Class object for the compiled expression
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends CompiledExpression> loadClass(String name, byte[] bytes) {
		if (this.ccl.getClassesDefinedCount() > CLASSES_DEFINED_LIMIT) {
			this.ccl = new ChildClassLoader(this.ccl.getParent());
		}
		return (Class<? extends CompiledExpression>) this.ccl.defineClass(name, bytes);
	}

	/**
	 * Factory method for compiler instances. The returned SpelCompiler will
	 * attach a class loader as the child of the given class loader and this
	 * child will be used to load compiled expressions.
	 * @param classLoader the ClassLoader to use as the basis for compilation
	 * @return a corresponding SpelCompiler instance
	 */
	public static SpelCompiler getCompiler(@Nullable ClassLoader classLoader) {
		ClassLoader clToUse = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
		synchronized (compilers) {
			SpelCompiler compiler = compilers.get(clToUse);
			if (compiler == null) {
				compiler = new SpelCompiler(clToUse);
				compilers.put(clToUse, compiler);
			}
			return compiler;
		}
	}

	/**
	 * Request that an attempt is made to compile the specified expression. It may fail if
	 * components of the expression are not suitable for compilation or the data types
	 * involved are not suitable for compilation. Used for testing.
	 * @return true if the expression was successfully compiled
	 */
	public static boolean compile(Expression expression) {
		return (expression instanceof SpelExpression && ((SpelExpression) expression).compileExpression());
	}

	/**
	 * Request to revert to the interpreter for expression evaluation.
	 * Any compiled form is discarded but can be recreated by later recompiling again.
	 * @param expression the expression
	 */
	public static void revertToInterpreted(Expression expression) {
		if (expression instanceof SpelExpression) {
			((SpelExpression) expression).revertToInterpreted();
		}
	}


	/**
	 * A ChildClassLoader will load the generated compiled expression classes.
	 */
	private static class ChildClassLoader extends URLClassLoader {

		private static final URL[] NO_URLS = new URL[0];

		private int classesDefinedCount = 0;

		public ChildClassLoader(@Nullable ClassLoader classLoader) {
			super(NO_URLS, classLoader);
		}

		int getClassesDefinedCount() {
			return this.classesDefinedCount;
		}

		public Class<?> defineClass(String name, byte[] bytes) {
			Class<?> clazz = super.defineClass(name, bytes, 0, bytes.length);
			this.classesDefinedCount++;
			return clazz;
		}
	}


	private class ExpressionClassWriter extends ClassWriter {

		public ExpressionClassWriter() {
			super(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		}

		@Override
		protected ClassLoader getClassLoader() {
			return ccl;
		}
	}

}
