package org.kot.experiment.proxy;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import java.lang.reflect.Method;

public class JavassistProxyFactory implements ProxyFactory {

	private final ClassPool classPool;

	public JavassistProxyFactory() {
		classPool = new ClassPool();
		classPool.appendClassPath(new LoaderClassPath(getClass().getClassLoader()));
	}

	@SuppressWarnings("unchecked")
	public <T> T newProxy(final T object) {
		try {
			final Class<T> clazz = (Class<T>) generateProxyClass(object.getClass());
			return clazz.getConstructor(object.getClass()).newInstance(object);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}


	public Class generateProxyClass(Class clazz) throws CannotCompileException, NotFoundException {
		final String name = clazz.getPackage().getName() + ".Proxy" + clazz.getSimpleName();

		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			// no such class, lets create it
		}

		final CtClass proxyClass = classPool.makeClass(name, resolve(clazz));
		for (Class<?> face : clazz.getInterfaces()) {
			proxyClass.addInterface(resolve(face));
		}

		proxyClass.addField(new CtField(resolve(clazz), "provider", proxyClass));

		final CtConstructor proxyConstructor = new CtConstructor(new CtClass[] {resolve(clazz)}, proxyClass);
		proxyConstructor.setBody("{ this.provider = $1; }");
		proxyClass.addConstructor(proxyConstructor);

		final CtMethod equalsMethod = new CtMethod(resolve(Boolean.TYPE), "equals", new CtClass[] {resolve(Object.class)}, proxyClass);
		equalsMethod.setBody("{\n\treturn this == $1;\n}");
		proxyClass.addMethod(equalsMethod);

		final CtMethod hashCodeMethod = new CtMethod(resolve(Integer.TYPE), "hashCode", new CtClass[0], proxyClass);
		hashCodeMethod.setBody("{\n\treturn System.identityHashCode(this);\n}");
		proxyClass.addMethod(hashCodeMethod);

		for (Method method : clazz.getDeclaredMethods()) {
			final Class<?>[] params = method.getParameterTypes();
			if ("equals".equals(method.getName()) && 1 == params.length && Object.class == params[0]) {
				continue;
			}
			if ("hashCode".equals(method.getName()) && 0 == params.length) {
				continue;
			}
			final CtMethod ctMethod = new CtMethod(resolve(method.getReturnType()), method.getName(), resolve(params), proxyClass);
			ctMethod.setBody("{\n\treturn provider." + method.getName() + "($$);\n}");
			proxyClass.addMethod(ctMethod);
		}
		return classPool.toClass(proxyClass);
	}

	private CtClass resolve(final Class clazz) throws NotFoundException {
		return classPool.get(clazz.getName());
	}

	private CtClass[] resolve(final Class[] classes) throws NotFoundException {
		CtClass[] result = new CtClass[classes.length];
		int i = 0;
		for (Class clazz : classes) {
			result[i++] = resolve(clazz);
		}
		return result;
	}

}
