package org.kot.experiment.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyFactory implements ProxyFactory {

	@SuppressWarnings("unchecked")
	public <T> T newProxy(final T object) {
		return (T) Proxy.newProxyInstance(
				object.getClass().getClassLoader(),
				object.getClass().getInterfaces(),
				new InvocationHandler() {
					@Override
					public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
						if (null == args) {
							if ("hashCode".equals(method.getName())) {
								return System.identityHashCode(proxy);
							}
						} else if (1 == args.length && "equals".equals(method.getName())) {
							return proxy == args[0];
						}
						return method.invoke(object, args);
					}
				}
		);
	}

}
