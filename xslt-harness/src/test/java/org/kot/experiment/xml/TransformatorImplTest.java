package org.kot.experiment.xml;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.xml.transform.dom.DOMResult;

import java.io.InputStream;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasXPath;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.runners.Parameterized.Parameters;

/**
 * Unit test for simple App.
 */
@RunWith(Parameterized.class)
public class TransformatorImplTest {

	private final Transformator transformator;

	@Parameters(name = "{index}: Check {0}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{DomTransformatorImpl.class},
				{XsltTransformatorImpl.class}
		});
	}

	public TransformatorImplTest(final Class<Transformator> clazz) throws IllegalAccessException, InstantiationException {
		transformator = clazz.newInstance();
	}

	@Test
	public void test() throws Exception {
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		final InputStream data = loader.getResourceAsStream("sample.xml");
		final DOMResult dom = new DOMResult();

		transformator.transform(data, dom);

		assertThat(dom.getNode(), hasXPath("/BusinessLetter"));
		assertThat(dom.getNode(), hasXPath("/BusinessLetter/OrderNumber"));
		assertThat(dom.getNode(), hasXPath("/BusinessLetter/OrderNumber", isEmptyString()));
		assertThat(dom.getNode(), hasXPath("/BusinessLetter/Head/SendDate"));
		assertThat(dom.getNode(), hasXPath("/BusinessLetter/Head/SendDate", isEmptyString()));
		assertThat(dom.getNode(), hasXPath("/BusinessLetter/Head/Author/Name/FirstName", not(isEmptyString())));
		assertThat(dom.getNode(), hasXPath("/BusinessLetter/Head/Author/Name/LastName", not(isEmptyString())));
		assertThat(dom.getNode(), not(hasXPath("/BusinessLetter/Head/Author/Company")));
		assertThat(dom.getNode(), not(hasXPath("/BusinessLetter/Head/Author/Address")));
		assertThat(dom.getNode(), hasXPath("/BusinessLetter/Subject"));
		assertThat(dom.getNode(), hasXPath("/BusinessLetter/Subject", isEmptyString()));
		assertThat(dom.getNode(), hasXPath("/BusinessLetter/Body/h1", not(isEmptyString())));
		assertThat(dom.getNode(), not(hasXPath("/BusinessLetter/Body/h1/b")));
		assertThat(dom.getNode(), not(hasXPath("/BusinessLetter/Body/p/b")));
		assertThat(dom.getNode(), hasXPath("/BusinessLetter/Body/image"));
		assertThat(dom.getNode(), hasXPath("/BusinessLetter/Body/image", isEmptyString()));
	}
}
