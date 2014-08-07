package org.kot.experiment.proxy;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Dispatcher;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.Method;

public class CGLibProxyFactory implements ProxyFactory {

	@SuppressWarnings("unchecked")
	public <T> T newProxy(final T object) {
		final Enhancer enhancer = new Enhancer();
		enhancer.setInterfaces(object.getClass().getInterfaces());
		enhancer.setSuperclass(object.getClass());
		enhancer.setCallbackFilter(new CGLibCallbackFilter());
		enhancer.setCallbacks(new Callback[] {new CGLibDispatcher<T>(object), new EqualsHandler(), new HashCodeHandler()});
		return (T) enhancer.create();
	}

	static class CGLibCallbackFilter implements CallbackFilter {

		@Override
		public int accept(final Method method) {
			final String name = method.getName();
			final Class<?>[] args = method.getParameterTypes();
			if (1 == args.length && Object.class == args[0] && "equals".equals(name)) {
				return 1;
			}
			if (0 == args.length && "hashCode".equals(name)) {
				return 2;
			}
			return 0;
		}
	}

	static class HashCodeHandler implements MethodInterceptor, Serializable {

		@Override
		public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
			return System.identityHashCode(o);
		}

	}

	static class EqualsHandler implements MethodInterceptor, Serializable {

		@Override
		public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
			return o == objects[0];
		}

	}

	static class CGLibDispatcher<T> implements Dispatcher, Serializable {

		private final T object;

		public CGLibDispatcher(final T object) {
			this.object = object;
		}

		@Override
		public Object loadObject() throws Exception {
			return object;
		}
	}
}
