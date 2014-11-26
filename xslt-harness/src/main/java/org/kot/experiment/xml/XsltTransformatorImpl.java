package org.kot.experiment.xml;

import net.sf.saxon.TransformerFactoryImpl;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * XSLT Transformation demo
 */
public class XsltTransformatorImpl implements Transformator {

	private static final ThreadLocal<Holder> holder = new ThreadLocal<Holder>() {
		@Override
		protected Holder initialValue() {
			return new Holder();
		}
	};

	@Override
	public void transform(final InputStream input, final Result output) throws TransformerException {
		final Transformer transformer = createTransformer();
		transformer.transform(new StreamSource(input), output);
	}

	@Override
	public String transform(final InputStream input) throws TransformerException, IOException {
		try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			transform(input, new StreamResult(output));
			return output.toString();
		}
	}

	public Transformer createTransformer() throws TransformerConfigurationException {
		return holder.get().transformer();
	}

	public static void main(String[] args) throws Exception {
		if (0 < args.length && "--help".equalsIgnoreCase(args[0])) {
			System.err.printf("Usage:%n\tjava %s [data]", XsltTransformatorImpl.class.getName());
			return;
		}
		final String data = (0 < args.length)? args[0]: "sample.xml";
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();

		final XsltTransformatorImpl app = new XsltTransformatorImpl();

		final String ethalon = app.transform(loader.getResourceAsStream(data));
		System.out.println(ethalon);

		final int max = Runtime.getRuntime().availableProcessors();
		final ArrayList<Future<String>> tasks = new ArrayList<>(max);
		final ExecutorService executor = Executors.newFixedThreadPool(max);
		for (int i = 0; i < max; i++) {
			tasks.add(
					executor.submit(
							new Callable<String>() {
								@Override
								public String call() throws Exception {
									return app.transform(loader.getResourceAsStream(data));
								}
							}
					)
			);
		}
		executor.shutdown();

		for (Future<String> task : tasks) {
			final String actual = task.get();
			if (!ethalon.equals(actual)) {
				System.err.println(actual);
			}
		}
	}

	static class Holder {
		private final Transformer transformer;

		Holder() {
			TransformerFactory factory = TransformerFactory.newInstance(
					TransformerFactoryImpl.class.getName(), Thread.currentThread().getContextClassLoader()
			);
			final ClassLoader loader = Thread.currentThread().getContextClassLoader();
			final URL styleSheet = loader.getResource("xslt/clean-up.xslt");
			try {
				Templates template = factory.newTemplates(new StreamSource(styleSheet.openStream()));
				transformer = template.newTransformer();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public Transformer transformer() {
			return transformer;
		}
	}
}
