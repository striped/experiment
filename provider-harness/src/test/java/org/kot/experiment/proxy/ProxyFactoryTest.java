package org.kot.experiment.proxy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kot.experiment.BlackCat;
import org.kot.experiment.Cat;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.runners.Parameterized.Parameters;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 09/03/2014 19:07
 */
@RunWith(Parameterized.class)
public class ProxyFactoryTest {

	@Parameters(name = "{index}: {0}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{CGLibProxyFactory.class},
				{DynamicProxyFactory.class},
				{JavassistProxyFactory.class},
				{SpringAOPProxyFactory.class},
				{HibernateProxyFactory.class}
		});
	}

	private final ProxyFactory factory;

	private Cat object;

	@Before
	public void setUp() {
		object = new BlackCat();
	}

	@Test
	public void testSayMeow() {
		final Cat proxy = factory.newProxy(object);
		assertThat(new BlackCat().sayMeow(1), is(proxy.sayMeow(1)));
	}

	@Test
	public void testHashCode() {
		final Cat proxy = factory.newProxy(object);
		assertThat(object.hashCode(), is(not(proxy.hashCode())));
	}

	@Test
	public void testProxyNotEqualsTarget() {
		final Cat proxy = factory.newProxy(object);
		assertThat(object, not(equalTo(proxy)));
	}

	@Test
	public void testTargetNotEqualsProxy() {
		final Cat proxy = factory.newProxy(object);
		assertThat(proxy, not(equalTo(object)));
	}

	public ProxyFactoryTest(Class<ProxyFactory> clazz) throws Exception {
		factory = clazz.newInstance();
	}
}
