package org.kot.experiment.proxy;

import org.junit.Test;
import org.kot.experiment.BlackCat;
import org.kot.experiment.Cat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * Test suite for Aspect/J Weaving.
 *
 * Supposed to be runned with:
 * <pre>
 *  -Dorg.aspectj.tracing.messages=true -Dorg.aspectj.tracing.enabled=true -Dorg.aspectj.tracing.factory=default
 *  -javaagent:/Users/striped/.m2/repository/org/aspectj/aspectjweaver/1.7.4/aspectjweaver-1.7.4.jar
 * </pre>
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 09/03/2014 19:07
 */
public class ProxyFactory1Test {

	@Test
	public void testSayMeow() {
		final Cat proxy = new SpringAOPProxyFactory(true).newProxy(new BlackCat());
		assertThat(new BlackCat().sayMeow(1), is(proxy.sayMeow(1)));
	}
}
