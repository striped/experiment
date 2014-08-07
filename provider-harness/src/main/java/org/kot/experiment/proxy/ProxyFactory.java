package org.kot.experiment.proxy;

/**
 * Proxy factory interface.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @created 09/03/2014 20:48
 */
public interface ProxyFactory {

	/**
	 * Instantiates proxy around provided instance.
	 * @param object The object to be proxied.
	 * @param <T> Expected type of object to be proxied.
	 * @return The proxy instance.
	 */
	<T> T newProxy(T object);
}
