package org.kot.experiment.proxy;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Hibernate proxy factory.
 * <p/>
 * Proxy factory that mimics the {@link org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer default Hibernate bean initializer}. But unlike origin doesn't use
 * {@link org.hibernate.Session session} to fetch the data from DBMS.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @created 11/03/2014 23:54
 */
public class HibernateProxyFactory implements ProxyFactory {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T newProxy(final T object) {
		final HashSet<Class<?>> faces = new HashSet<>(Arrays.asList(object.getClass().getInterfaces()));
		faces.add(HibernateProxy.class);
		final Class[] interfaces = faces.toArray(new Class[faces.size()]);
		final T proxy = (T) JavassistLazyInitializer.getProxy(object.getClass().getSimpleName(), object.getClass(), interfaces, null, null, null, null, null);
		final LazyInitializer initializer = ((HibernateProxy) proxy).getHibernateLazyInitializer();
		init(initializer, "initialized", true);
		init(initializer, "target", object);
		return proxy;
	}

	static void init(final LazyInitializer proxy, final String field, final Object value) {
		for (Class<?> clazz = proxy.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				final Field f = clazz.getDeclaredField(field);
				f.setAccessible(true);
				f.set(proxy, value);
				return;
			} catch (Exception e) {
				// ignore
			}
		}
	}


}
