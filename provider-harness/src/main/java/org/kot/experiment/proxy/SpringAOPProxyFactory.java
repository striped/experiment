package org.kot.experiment.proxy;

/**
 * Spring AOP Proxy factory implementation.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @created 24/02/2014 22:22
 */
public class SpringAOPProxyFactory implements ProxyFactory {

	private final boolean optimize;

	public SpringAOPProxyFactory() {
		this(true);
	}

	/**
	 * Constructor with an optimization flag.
	 * @param optimize {@code true} if "optimized" proxy needed.
	 */
	public SpringAOPProxyFactory(final boolean optimize) {
		this.optimize = optimize;
	}

	/**
	 * Instantiates typical Spring AOP proxy around provided instance
	 * @param object The object to be proxied.
	 * @param <T> Expected type of object to be proxied.
	 * @return The proxy instance.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T newProxy(final T object) {
		final org.springframework.aop.framework.ProxyFactory factory = new org.springframework.aop.framework.ProxyFactory(object);
		factory.setOptimize(optimize);
		return (T) factory.getProxy();
	}
}
